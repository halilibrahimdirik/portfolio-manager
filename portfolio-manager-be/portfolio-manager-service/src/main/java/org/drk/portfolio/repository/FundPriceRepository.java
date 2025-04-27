package org.drk.portfolio.repository;

import org.drk.portfolio.entity.FundPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundPriceRepository extends JpaRepository<FundPrice, Long> {
}