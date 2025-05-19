package org.drk.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetProfitSummary {
    private BigDecimal totalValue;
    private BigDecimal totalProfit;
    private BigDecimal profitPercentage;
}