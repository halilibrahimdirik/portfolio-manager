package org.drk.portfolio.controller;

import lombok.RequiredArgsConstructor;
import org.drk.portfolio.service.FundPriceCrawlerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fund-prices")
@RequiredArgsConstructor
public class FundPriceController {

    private final FundPriceCrawlerService fundPriceCrawlerService;

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
}