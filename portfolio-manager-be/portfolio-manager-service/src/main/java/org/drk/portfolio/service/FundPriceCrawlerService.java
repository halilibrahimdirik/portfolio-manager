package org.drk.portfolio.service;

import lombok.extern.slf4j.Slf4j;
import org.drk.portfolio.entity.AssetType;
import org.drk.portfolio.entity.FundPrice;
import org.drk.portfolio.repository.AssetRepository;
import org.drk.portfolio.repository.FundPriceRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class FundPriceCrawlerService {


    @Autowired
    private FundPriceRepository fundPriceRepository;
    
    @Autowired
    private AssetRepository assetRepository;  // Add this

    private static final String TEFAS_URL = "https://www.tefas.gov.tr/FonAnaliz.aspx?FonKod=%s";
    private static final long DELAY_BETWEEN_REQUESTS = 10000; // 10 seconds in milliseconds

    @Scheduled(cron = "0 0 21 * * ?") // 21:00
    @Scheduled(cron = "0 0 10 * * ?") // 10:00
    public void crawlAndPersistFundPrices() {
        List<String> fundCodes = assetRepository.findDistinctAssetCodesByType(AssetType.TEFAS);
        
        for (String fundCode : fundCodes) {
            try {
                if (fundCode != null && !fundCode.isEmpty()) {
                    crawlFundData(fundCode);
                    Thread.sleep(DELAY_BETWEEN_REQUESTS);
                }
            } catch (IOException e) {
                log.error("Error crawling fund {}: {}", fundCode, e.getMessage());
            } catch (InterruptedException e) {
                log.error("Sleep interrupted while waiting between requests", e);
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void crawlFundData(String fundCode) throws IOException {
        // Send GET request and parse HTML
        String url = String.format(TEFAS_URL, fundCode);
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                .header("Accept-Language", "en-US,en;q=0.5")
                .header("Connection", "keep-alive")
                .header("Upgrade-Insecure-Requests", "1")
                .header("Sec-Fetch-Dest", "document")
                .header("Sec-Fetch-Mode", "navigate")
                .header("Sec-Fetch-Site", "none")
                .header("Sec-Fetch-User", "?1")
                .timeout(30000)
                .followRedirects(true)
                .get();

        // Check if we got the anti-bot page
        if (doc.html().contains("This question is for testing whether you are a human visitor")) {
            log.error("Anti-bot protection detected for fund code: {}", fundCode);
            throw new IOException("Access blocked by anti-bot protection");
        }

        // Extract Highcharts script
        String scriptContent = doc.select("script:containsData(chartMainContent_FonFiyatGrafik)").html();

        // Extract xAxis.categories (dates)
        List<String> dates = extractCategories(scriptContent);
        // Extract series.data (prices)
        List<Double> prices = extractSeriesData(scriptContent);

        // Ensure dates and prices align
        if (dates.size() != prices.size()) {
            throw new IOException("Mismatch between dates and prices for fund " + fundCode);
        }

        // Persist to database
        persistFundPrices(fundCode, dates, prices);
    }

    private List<String> extractCategories(String scriptContent) {
        List<String> categories = new ArrayList<>();
        Pattern pattern = Pattern.compile("\"categories\":\\s*\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(scriptContent);
        if (matcher.find()) {
            String categoriesStr = matcher.group(1);
            // Split by comma, remove quotes, and trim
            categoriesStr = categoriesStr.replaceAll("\"", "").trim();
            categories = Arrays.asList(categoriesStr.split(",\\s*"));
        }
        return categories;
    }

    private List<Double> extractSeriesData(String scriptContent) {
        List<Double> data = new ArrayList<>();
        Pattern pattern = Pattern.compile("\"data\":\\s*\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(scriptContent);
        if (matcher.find()) {
            String dataStr = matcher.group(1);
            // Split by comma and parse to Double
            for (String value : dataStr.split(",\\s*")) {
                try {
                    data.add(Double.parseDouble(value.trim()));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid price value: " + value);
                }
            }
        }
        return data;
    }

    private void persistFundPrices(String fundCode, List<String> dates, List<Double> prices) {
        // Get the latest price date for this fund from the database
        Date latestPriceDate = null;
        try {
            FundPrice latestPrice = fundPriceRepository.findTopByFundCodeAndPriceDateLessThanEqualOrderByPriceDateDesc(
                fundCode, 
                new Date(System.currentTimeMillis())
            );
            if (latestPrice != null) {
                latestPriceDate = latestPrice.getPriceDate();
                log.info("Latest price date for fund {} is {}", fundCode, latestPriceDate);
            }
        } catch (Exception e) {
            log.error("Error getting latest price date for fund {}: {}", fundCode, e.getMessage());
        }

        for (int i = 0; i < dates.size(); i++) {
            try {
                String[] dateParts = dates.get(i).split("\\.");
                String formattedDate = String.format("%s-%s-%s", dateParts[2], dateParts[1], dateParts[0]);
                Date currentDate = Date.valueOf(formattedDate);
                
                // Skip if the current date is not newer than the latest price date
                if (latestPriceDate != null && !currentDate.after(latestPriceDate)) {
                    log.debug("Skipping existing price for fund {} on date {}", fundCode, formattedDate);
                    continue;
                }
                
                FundPrice fundPrice = new FundPrice();
                fundPrice.setFundCode(fundCode);
                fundPrice.setPriceDate(currentDate);
                fundPrice.setPrice(prices.get(i));
                
                try {
                    fundPriceRepository.save(fundPrice);
                    log.info("Saved new price for fund {} on date {}", fundCode, formattedDate);
                } catch (Exception e) {
                    if (e.getMessage().contains("duplicate key value")) {
                        log.info("Skipping duplicate entry for fund {} on date {}", fundCode, formattedDate);
                        continue;
                    }
                    throw e;
                }
            } catch (Exception e) {
                log.error("Error processing date {} for fund {}: {}", dates.get(i), fundCode, e.getMessage());
            }
        }
    }
}