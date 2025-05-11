package org.drk.portfolio.controller;

import lombok.RequiredArgsConstructor;
import org.drk.portfolio.entity.Asset;
import org.drk.portfolio.entity.AssetType;
import org.drk.portfolio.entity.FundPrice;
import org.drk.portfolio.service.AssetService;
import org.drk.portfolio.service.FundPriceService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {
    private final AssetService assetService;
    private final FundPriceService fundPriceService;

    @GetMapping
    public List<Asset> getAllAssets() {
        return assetService.getAllAssets();
    }

    @GetMapping("/{type}")
    public List<Asset> getAssetsByType(@PathVariable String type) {
        List<Asset> assets = assetService.getAssetsByType(type);
        
        if (AssetType.valueOf(type) == AssetType.TEFAS) {
            LocalDate priceDate = LocalDate.now();
            
            // If weekend, get last Friday's price
            if (priceDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
                priceDate = priceDate.minusDays(1);
            } else if (priceDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                priceDate = priceDate.minusDays(2);
            }
            
            for (Asset asset : assets) {
                FundPrice latestPrice = fundPriceService.getLatestPrice(asset.getId(), priceDate);
                if (latestPrice != null) {
                    asset.setCurrentPrice(BigDecimal.valueOf(latestPrice.getPrice()));
                }
            }
        }
        
        return assets;
    }

    @PostMapping
    public Asset saveAsset(@RequestBody Asset asset) {
        return assetService.saveAsset(asset);
    }

    @PutMapping("/{id}")
    public Asset updateAsset(@PathVariable String id, @RequestBody Asset asset) {
        asset.setId(id);
        return assetService.saveAsset(asset);
    }

    @DeleteMapping("/{id}")
    public void deleteAsset(@PathVariable String id) {
        assetService.deleteAsset(id);
    }
}