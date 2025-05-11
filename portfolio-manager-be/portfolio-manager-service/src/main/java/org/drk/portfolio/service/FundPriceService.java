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
}