package org.drk.portfolio.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drk.portfolio.entity.Asset;
import org.drk.portfolio.entity.AssetSource;
import org.drk.portfolio.entity.AssetType;
import org.drk.portfolio.repository.AssetRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetService {
    private final AssetRepository assetRepository;
    private final FundPriceService fundPriceService;

    @PostConstruct
    public void init() {
        List<Asset> assetsWithNullSource = assetRepository.findByAssetSourceIsNull();
        if (!assetsWithNullSource.isEmpty()) {
            log.info("Found {} assets with null source. Updating to YAPIKREDI.", assetsWithNullSource.size());
            assetsWithNullSource.forEach(asset -> asset.setAssetSource(AssetSource.YAPIKREDI));
            assetRepository.saveAll(assetsWithNullSource);
        }
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public List<Asset> getAssetsByType(String type) {
        return assetRepository.findByType(AssetType.valueOf(type));
    }

    public List<Asset> getAssetsByTypeAndSource(String type, String source) {
        return assetRepository.findByTypeAndAssetSource(AssetType.valueOf(type), AssetSource.valueOf(source));
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

    public void importAssets(MultipartFile file, AssetSource source) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean isFirstLine = true;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    // Check if it's a header line
                    if (line.toLowerCase().contains("code") || line.toLowerCase().contains("kod")) {
                        isFirstLine = false;
                        continue;
                    }
                    isFirstLine = false;
                }

                String[] parts = line.split("[,;]");
                if (parts.length < 3) continue;

                String code = parts[0].trim();
                if (code.isEmpty()) continue;

                BigDecimal quantity = new BigDecimal(parts[1].trim());
                BigDecimal purchasePrice = new BigDecimal(parts[2].trim());
                Date purchaseDate;
                
                if (parts.length > 3 && !parts[3].trim().isEmpty()) {
                    try {
                        purchaseDate = Date.valueOf(LocalDate.parse(parts[3].trim(), formatter));
                    } catch (Exception e) {
                        purchaseDate = Date.valueOf(LocalDate.now());
                    }
                } else {
                    purchaseDate = Date.valueOf(LocalDate.now());
                }

                Asset asset = assetRepository.findByAssetCodeAndAssetSource(code, source)
                        .orElse(new Asset());

                if (asset.getId() == null) {
                    asset.setId(UUID.randomUUID().toString());
                    asset.setAssetCode(code);
                    asset.setAssetName(code); // Initial name, can be updated later via crawler
                    asset.setAssetSource(source);
                    asset.setType(AssetType.TEFAS);
                }

                asset.setQuantity(quantity);
                asset.setPurchasePrice(purchasePrice);
                asset.setPurchaseDate(purchaseDate);

                // Try to get current price immediately
                try {
                    var latestPrice = fundPriceService.getLatestPrice(code, LocalDate.now());
                    if (latestPrice != null) {
                        asset.setCurrentPrice(BigDecimal.valueOf(latestPrice.getPrice()));
                        asset.setPriceDate(latestPrice.getPriceDate());
                    } else {
                        // If no current price, use purchase price temporarily
                        if (asset.getCurrentPrice() == null) {
                            asset.setCurrentPrice(purchasePrice);
                        }
                    }
                } catch (Exception e) {
                    log.warn("Could not fetch price for imported asset: {}", code);
                    if (asset.getCurrentPrice() == null) {
                        asset.setCurrentPrice(purchasePrice);
                    }
                }

                assetRepository.save(asset);
            }
        } catch (Exception e) {
            log.error("Error importing assets", e);
            throw new RuntimeException("Failed to import assets: " + e.getMessage());
        }
    }
}