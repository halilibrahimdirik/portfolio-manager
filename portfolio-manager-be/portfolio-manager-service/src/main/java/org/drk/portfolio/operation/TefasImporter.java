package org.drk.portfolio.operation;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drk.portfolio.config.TefasImportConfig;
import org.drk.portfolio.dto.TefasAssetDTO;
import org.drk.portfolio.entity.Asset;
import org.drk.portfolio.entity.AssetType;
import org.drk.portfolio.repository.AssetRepository;
import org.drk.portfolio.repository.FundPriceRepository;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TefasImporter {
    private final FundPriceRepository fundPriceRepository;
    private final AssetRepository assetRepository;  // Add this repository
    private final TefasImportConfig config;

    public void importFromCsv() throws IOException {
        File file = new File(config.getPdfPath());
        if (!file.exists()) {
            throw new IOException("PDF file not found: " + config.getPdfPath());
        }

        try {
            List<TefasAssetDTO> tefasAssetDTOS = importAssetsFromCsv(file);

            // Sonuçları yazdır
            for (TefasAssetDTO portfolio : tefasAssetDTOS) {
                System.out.println(portfolio);
            }

            saveFundPrices(tefasAssetDTOS);
            log.info("Successfully imported {} fund prices", tefasAssetDTOS.size());
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }


    }


    public static List<TefasAssetDTO> importAssetsFromCsv(File file)
            throws IOException, CsvValidationException {
        List<TefasAssetDTO> assets = new ArrayList<>();

        // Configure CSV reader with semicolon separator
        CSVParser csvParser = new CSVParserBuilder()
                .withSeparator(';')
                .build();
                
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(file, StandardCharsets.UTF_8))
                .withCSVParser(csvParser)
                .build()) {
                
            // Skip first two rows (empty line and header)
            csvReader.readNext();

            String[] row;
            while ((row = csvReader.readNext()) != null) {
                try {
                    // Skip empty rows
                    if (row.length < 6 || row[0].trim().isEmpty()) {
                        continue;
                    }

                    // Parse and clean data
                    String fundName = row[0].trim().replace("\"", "");
                    
                    // Extract fund code from name (e.g., "YFBL7 / YVD" from the fund name)
                    String fundCode = "";
                    int lastOpenParen = fundName.lastIndexOf("(");
                    int lastCloseParen = fundName.lastIndexOf(")");
                    if (lastOpenParen != -1 && lastCloseParen != -1) {
                        String[] codes = fundName.substring(lastOpenParen + 1, lastCloseParen).split("/");
                        fundCode = codes[1].trim();
                    }

                    // Clean and parse numerical values
                    double fundPrice = parseCurrency(row[1]);
                    double averageCost = parseCurrency(row[2]);
                    double returnPercentage = parsePercentage(row[3]);
                    double shareCount = parseNumber(row[4]);
                    double totalAmount = parseCurrency(row[5]);

                    TefasAssetDTO asset = new TefasAssetDTO(
                            fundName,
                            fundPrice,
                            averageCost,
                            returnPercentage,
                            shareCount,
                            totalAmount
                    );
                    assets.add(asset);
                    log.debug("Successfully parsed asset: {}", asset);
                } catch (Exception e) {
                    log.error("Error processing row: {}", String.join(";", row), e);
                }
            }
        }

        return assets;
    }

    // Helper method to parse currency values (e.g., "65,102656 TL" -> 65.102656)
    private static double parseCurrency(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        String cleaned = null;
        try {
            // Remove currency symbol and all types of spaces (including NBSP)
            cleaned = value.replaceAll("[\\s\\u00A0]+", "").replace("TL", "");

            // Handle Turkish number format (1.234,56 -> 1234.56)
            if (cleaned.contains(".") && cleaned.contains(",")) {
                cleaned = cleaned.replace(".", "").replace(",", ".");
            } else if (cleaned.contains(",")) {
                cleaned = cleaned.replace(",", ".");
            }

            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            log.warn("Failed to parse currency value: '{}', cleaned value: '{}'", value, cleaned);
            return 0.0;
        }
    }

    // Helper method to parse percentage values (e.g., "%0,20" -> 0.20)
    private static double parsePercentage(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        try {
            String cleaned = value.replace("%", "").trim();
            if (cleaned.contains(",")) {
                cleaned = cleaned.replace(",", ".");
            }
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            log.warn("Failed to parse percentage value: {}", value);
            return 0.0;
        }
    }

    // Helper method to parse numerical values (e.g., "1.547" -> 1547.0)
    private static double parseNumber(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        try {
            String cleaned = value.trim();
            if (cleaned.contains(".") && cleaned.contains(",")) {
                cleaned = cleaned.replace(".", "").replace(",", ".");
            } else if (cleaned.contains(",")) {
                cleaned = cleaned.replace(",", ".");
            }else if (cleaned.contains(".")) {
                cleaned = cleaned.replace(".", "");
            }
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            log.warn("Failed to parse number value: {}", value);
            return 0.0;
        }
    }

    private void saveFundPrices(List<TefasAssetDTO> tefasAssets) {
        List<Asset> assets = tefasAssets.stream()
                .map(this::convertToAsset)
                .collect(Collectors.toList());

        try {
            assetRepository.saveAll(assets);
            log.info("Successfully saved {} TEFAS assets to database", assets.size());
        } catch (Exception e) {
            log.error("Error saving TEFAS assets to database", e);
            throw new RuntimeException("Failed to save TEFAS assets", e);
        }
    }

    private Asset convertToAsset(TefasAssetDTO dto) {
        Asset asset = new Asset();
        asset.setId(UUID.randomUUID().toString());
        
        // Extract asset name and code from full name
        String fullName = dto.getFundName();
        String assetName;
        String assetCode;
        
        // Find the last parentheses for the code
        int lastParenthesesIndex = fullName.lastIndexOf("(");
        if (lastParenthesesIndex != -1) {
            // Get everything before the last parentheses as the name
            assetName = fullName.substring(0, lastParenthesesIndex).trim();
            
            // Extract code from the last parentheses
            int endParenthesesIndex = fullName.lastIndexOf(")");
            if (endParenthesesIndex != -1) {
                String codeSection = fullName.substring(lastParenthesesIndex + 1, endParenthesesIndex);
                String[] codes = codeSection.split("/");
                assetCode = codes[1].trim();
            } else {
                assetCode = "";
            }
        } else {
            assetName = fullName;
            assetCode = "";
        }

        asset.setAssetName(assetName);
        asset.setAssetCode(assetCode);
        asset.setCurrentPrice(BigDecimal.valueOf(dto.getFundPrice()));
        asset.setPurchasePrice(BigDecimal.valueOf(dto.getAverageCost()));
        asset.setQuantity(BigDecimal.valueOf(dto.getShareCount()));
        asset.setPurchaseDate(Date.valueOf(LocalDate.now()));
        asset.setType(AssetType.TEFAS);

        return asset;
    }
}