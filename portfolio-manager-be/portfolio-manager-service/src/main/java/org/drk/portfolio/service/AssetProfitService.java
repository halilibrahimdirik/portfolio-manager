package org.drk.portfolio.service;

import lombok.RequiredArgsConstructor;
import org.drk.portfolio.dto.AssetProfitSummary;
import org.drk.portfolio.entity.Asset;
import org.drk.portfolio.entity.AssetSource;
import org.drk.portfolio.entity.AssetType;
import org.drk.portfolio.repository.AssetRepository;
import org.drk.portfolio.entity.FundPrice;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.time.LocalDate;
import java.time.DayOfWeek;

@Service
@RequiredArgsConstructor
public class AssetProfitService {
    
    private final AssetRepository assetRepository;
    private final FundPriceService fundPriceService;
    
    public AssetProfitSummary calculateProfitSummary(AssetType assetType) {
        return calculateProfitSummary(assetType, null);
    }

    public AssetProfitSummary calculateProfitSummary(AssetType assetType, AssetSource source) {
        List<Asset> assets;
        if (source != null) {
            assets = assetRepository.findByTypeAndAssetSource(assetType, source);
        } else {
            assets = assetRepository.findByType(assetType);
        }
        
        if (assetType == AssetType.TEFAS) {
            LocalDate priceDate = LocalDate.now();
            if (priceDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
                priceDate = priceDate.minusDays(1);
            } else if (priceDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                priceDate = priceDate.minusDays(2);
            }
            for (Asset asset : assets) {
                FundPrice latestPrice = fundPriceService.getLatestPrice(asset.getAssetCode(), priceDate);
                if (latestPrice != null) {
                    asset.setCurrentPrice(BigDecimal.valueOf(latestPrice.getPrice()));
                }
            }
        }
        
        BigDecimal totalValue = assets.stream()
            .map(asset -> (asset.getCurrentPrice() != null ? asset.getCurrentPrice() : BigDecimal.ZERO)
                .multiply(asset.getQuantity() != null ? asset.getQuantity() : BigDecimal.ZERO))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        BigDecimal totalCost = assets.stream()
            .map(asset -> (asset.getPurchasePrice() != null ? asset.getPurchasePrice() : BigDecimal.ZERO)
                .multiply(asset.getQuantity() != null ? asset.getQuantity() : BigDecimal.ZERO))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        BigDecimal totalProfit = totalValue.subtract(totalCost);
        
        BigDecimal profitPercentage = totalCost.compareTo(BigDecimal.ZERO) != 0 
            ? totalProfit.multiply(new BigDecimal("100")).divide(totalCost, 2, RoundingMode.HALF_UP)
            : BigDecimal.ZERO;
            
        return new AssetProfitSummary(totalValue, totalProfit, profitPercentage);
    }
}
