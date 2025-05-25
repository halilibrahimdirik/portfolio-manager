package org.drk.portfolio.service;

import lombok.RequiredArgsConstructor;
import org.drk.portfolio.entity.FundPrice;
import org.drk.portfolio.repository.FundPriceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FundPriceService {
    private final FundPriceRepository fundPriceRepository;

    public FundPrice getLatestPrice(String fundCode, LocalDate priceDate) {
        return fundPriceRepository.findTopByFundCodeAndPriceDateLessThanEqualOrderByPriceDateDesc(
            fundCode, 
            java.sql.Date.valueOf(priceDate)
        );
    }

    public Double getMonthlyIncrease(String fundCode, LocalDate currentDate) {
        // Get current price
        FundPrice currentPrice = getLatestPrice(fundCode, currentDate);
        if (currentPrice == null) return null;

        // Get price from one month ago
        LocalDate oneMonthAgo = currentDate.minusMonths(1);
        FundPrice monthAgoPrice = getLatestPrice(fundCode, oneMonthAgo);
        if (monthAgoPrice == null) return null;

        // Calculate percentage increase
        return ((currentPrice.getPrice() - monthAgoPrice.getPrice()) / monthAgoPrice.getPrice()) * 100;
    }
}