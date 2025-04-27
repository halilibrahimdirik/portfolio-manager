package org.drk.portfolio.service;

import lombok.extern.slf4j.Slf4j;
import org.drk.portfolio.entity.FundPrice;
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

    private static final String TEFAS_URL = "https://www.tefas.gov.tr/FonAnaliz.aspx?FonKod=%s";
    private static final String[] FUND_CODES = {"NNF", "AN1", "IPB"}; // Add more fund codes as needed

    // Run every day at 21:00 and 10:00
    @Scheduled(cron = "0 0 21 * * ?") // 21:00
    @Scheduled(cron = "0 0 10 * * ?") // 10:00
    public void crawlAndPersistFundPrices() {
        for (String fundCode : FUND_CODES) {
            try {
                crawlFundData(fundCode);
            } catch (IOException e) {
                System.err.println("Error crawling fund " + fundCode + ": " + e.getMessage());
            }
        }
    }

    public void crawlFundData(String fundCode) throws IOException {
        // Send GET request and parse HTML
        String url = String.format(TEFAS_URL, fundCode);
        Document doc = Jsoup.connect(url).get();

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
        List<FundPrice> fundPrices = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            try {
                String[] dateParts = dates.get(i).split("\\.");
                String formattedDate = String.format("%s-%s-%s", dateParts[2], dateParts[1], dateParts[0]);
                
                FundPrice fundPrice = new FundPrice();
                fundPrice.setFundCode(fundCode);
                fundPrice.setPriceDate(Date.valueOf(formattedDate));
                fundPrice.setPrice(prices.get(i));
                
                fundPrices.add(fundPrice);
            } catch (Exception e) {
                log.error("Error processing date {} for fund {}: {}", dates.get(i), fundCode, e.getMessage());
            }
        }

        fundPriceRepository.saveAll(fundPrices);
    }


}