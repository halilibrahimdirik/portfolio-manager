package org.drk.portfolio.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "fund_prices", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"fund_code", "price_date"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fund_code", nullable = false)
    private String fundCode;

    @Column(name = "price_date", nullable = false)
    private Date priceDate;

    @Column(nullable = false)
    private Double price;
}