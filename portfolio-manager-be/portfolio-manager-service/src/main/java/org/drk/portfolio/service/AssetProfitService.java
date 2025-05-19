package org.drk.portfolio.service;

import lombok.RequiredArgsConstructor;
import org.drk.portfolio.dto.AssetProfitSummary;
import org.drk.portfolio.entity.Asset;
import org.drk.portfolio.entity.AssetType;
import org.drk.portfolio.repository.AssetRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetProfitService {
    
    private final AssetRepository assetRepository;
    
    public AssetProfitSummary calculateProfitSummary(AssetType assetType) {
        List<Asset> assets = assetRepository.findByType(assetType);
        
        BigDecimal totalValue = assets.stream()
            .map(asset -> asset.getCurrentPrice().multiply(asset.getQuantity()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        BigDecimal totalCost = assets.stream()
            .map(asset -> asset.getPurchasePrice().multiply(asset.getQuantity()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        BigDecimal totalProfit = totalValue.subtract(totalCost);
        
        BigDecimal profitPercentage = totalCost.compareTo(BigDecimal.ZERO) != 0 
            ? totalProfit.multiply(new BigDecimal("100")).divide(totalCost, 2, RoundingMode.HALF_UP)
            : BigDecimal.ZERO;
            
        return new AssetProfitSummary(totalValue, totalProfit, profitPercentage);
    }
}