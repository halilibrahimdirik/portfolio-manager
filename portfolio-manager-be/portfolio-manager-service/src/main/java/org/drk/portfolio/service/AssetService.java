package org.drk.portfolio.service;

import lombok.RequiredArgsConstructor;
import org.drk.portfolio.entity.Asset;
import org.drk.portfolio.entity.AssetType;
import org.drk.portfolio.repository.AssetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetService {
    private final AssetRepository assetRepository;

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public List<Asset> getAssetsByType(String type) {
        return assetRepository.findByType(AssetType.valueOf(type));
    }

    public Asset saveAsset(Asset asset) {
        return assetRepository.save(asset);
    }

    public void deleteAsset(String id) {
        assetRepository.deleteById(id);
    }
    
    public Asset getAssetByCode(String code) {
        return assetRepository.findByAssetCode(code);
    }
}