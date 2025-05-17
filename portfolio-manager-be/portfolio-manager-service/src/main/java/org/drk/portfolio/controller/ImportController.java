package org.drk.portfolio.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drk.portfolio.operation.TefasImporter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportController {
    private final TefasImporter tefasImporter;

    @PostMapping("/tefas")
    public ResponseEntity<String> importTefasFon() {
        try {
            tefasImporter.importFromCsv();
            return ResponseEntity.ok("TEFAS portfolio import completed successfully");
        } catch (IOException e) {
            log.error("Failed to import TEFAS portfolio", e);
            return ResponseEntity.internalServerError()
                    .body("Failed to read PDF file: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during TEFAS import", e);
            return ResponseEntity.internalServerError()
                    .body("Unexpected error during import: " + e.getMessage());
        }
    }
}