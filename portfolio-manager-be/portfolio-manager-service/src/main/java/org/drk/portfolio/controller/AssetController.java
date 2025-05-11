package org.drk.portfolio.controller;

import lombok.RequiredArgsConstructor;
import org.drk.portfolio.entity.Asset;
import org.drk.portfolio.service.AssetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {
    private final AssetService assetService;

    @GetMapping
    public List<Asset> getAllAssets() {
        return assetService.getAllAssets();
    }

    @GetMapping("/{type}")
    public List<Asset> getAssetsByType(@PathVariable String type) {
        return assetService.getAssetsByType(type);
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