package org.drk.portfolio.controller;

import lombok.RequiredArgsConstructor;
import org.drk.portfolio.dto.FundDetailDTO;
import org.drk.portfolio.entity.Asset;
import org.drk.portfolio.entity.FundPrice;
import org.drk.portfolio.service.AssetService;
import org.drk.portfolio.service.FundPriceCrawlerService;
import org.drk.portfolio.service.FundPriceService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fund-prices")
@RequiredArgsConstructor
public class FundPriceController {

    private final FundPriceCrawlerService fundPriceCrawlerService;
    private final FundPriceService fundPriceService;
    private final AssetService assetService;

    @PostMapping("/crawl")
    public ResponseEntity<String> triggerCrawl() {
        try {
            fundPriceCrawlerService.crawlAndPersistFundPrices();
            return ResponseEntity.ok("Fund price crawling completed successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error during fund price crawling: " + e.getMessage());
        }
    }

    @GetMapping("/{fundCode}/returns")
    public ResponseEntity<FundDetailDTO> getFundReturns(
            @PathVariable String fundCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate currentDate) {
        Asset asset = assetService.getAssetByCode(fundCode);
        Map<String, Double> returns = fundPriceService.getHistoricalReturns(fundCode, currentDate);
        
        FundDetailDTO fundDetail = new FundDetailDTO();
        fundDetail.setFundCode(fundCode);
        fundDetail.setFundName(asset != null ? asset.getAssetName() : "");
        fundDetail.setReturns(returns);
        
        return ResponseEntity.ok(fundDetail);
    }

    @GetMapping("/{fundCode}/yearly-prices")
    public ResponseEntity<List<FundPrice>> getYearlyPrices(
            @PathVariable String fundCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate currentDate) {
        return ResponseEntity.ok(fundPriceService.getYearlyPrices(fundCode, currentDate));
    }
}