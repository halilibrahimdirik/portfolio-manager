package org.drk.portfolio.repository;

import org.drk.portfolio.entity.NasdaqStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NasdaqStockRepository extends JpaRepository<NasdaqStock, Long> {
}