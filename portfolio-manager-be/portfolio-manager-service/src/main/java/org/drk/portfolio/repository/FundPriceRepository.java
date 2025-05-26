package org.drk.portfolio.repository;

import org.drk.portfolio.entity.FundPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface FundPriceRepository extends JpaRepository<FundPrice, Long> {
    FundPrice findTopByFundCodeAndPriceDateLessThanEqualOrderByPriceDateDesc(String fundCode, Date priceDate);
    FundPrice findFirstByFundCodeAndPriceDateGreaterThanEqualOrderByPriceDate(String fundCode, Date priceDate);
}