package org.drk.portfolio.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "nasdaq_stocks")
@Data
public class NasdaqStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String symbol;
    private Integer quantity;
    private Double price;
    private String dailyChange;
    private String monthlyChange;
    private Double value;
}