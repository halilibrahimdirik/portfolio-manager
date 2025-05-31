package org.drk.portfolio.service;

import lombok.RequiredArgsConstructor;
import org.drk.portfolio.entity.FundPrice;
import org.drk.portfolio.repository.FundPriceRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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

    public Double calculateReturn(String fundCode, LocalDate currentDate, int months) {
        FundPrice currentPrice = getLatestPrice(fundCode, currentDate);
        if (currentPrice == null) return null;

        LocalDate pastDate = currentDate.minusMonths(months);
        // Try to find the closest available price within a reasonable date range
        FundPrice pastPrice = fundPriceRepository.findFirstByFundCodeAndPriceDateBetweenOrderByPriceDate(
            fundCode,
            java.sql.Date.valueOf(pastDate.minusDays(7)), // Allow 7 days buffer
            java.sql.Date.valueOf(pastDate.plusDays(7)), // Allow 7 days buffer
            Sort.by(Sort.Direction.ASC, "priceDate")
        );
        if (pastPrice == null) return null;

        return ((currentPrice.getPrice() - pastPrice.getPrice()) / pastPrice.getPrice()) * 100;
    }

    public Double getMonthlyIncrease(String fundCode, LocalDate currentDate) {
        return calculateReturn(fundCode, currentDate, 1);
    }

    public Map<String, Double> getHistoricalReturns(String fundCode, LocalDate currentDate) {
        Map<String, Double> returns = new HashMap<>();
        returns.put("1M", calculateReturn(fundCode, currentDate, 1));
        returns.put("2M", calculateReturn(fundCode, currentDate, 2));
        returns.put("3M", calculateReturn(fundCode, currentDate, 3));
        returns.put("6M", calculateReturn(fundCode, currentDate, 6));
        returns.put("1Y", calculateReturn(fundCode, currentDate, 12));
        return returns;
    }

    public List<FundPrice> getYearlyPrices(String fundCode, LocalDate currentDate) {
        LocalDate yearAgo = currentDate.minusYears(1);
        return fundPriceRepository.findByFundCodeAndPriceDateBetweenOrderByPriceDate(
            fundCode,
            java.sql.Date.valueOf(yearAgo),
            java.sql.Date.valueOf(currentDate)
        );
    }
}