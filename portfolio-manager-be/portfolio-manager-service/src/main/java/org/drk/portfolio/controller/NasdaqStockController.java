package org.drk.portfolio.controller;

import lombok.RequiredArgsConstructor;
import org.drk.portfolio.entity.NasdaqStock;
import org.drk.portfolio.repository.NasdaqStockRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nasdaq-stocks")
@RequiredArgsConstructor
public class NasdaqStockController {

    private final NasdaqStockRepository nasdaqStockRepository;

    @GetMapping
    public List<NasdaqStock> getAllStocks() {
        return nasdaqStockRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NasdaqStock> getStockById(@PathVariable Long id) {
        return nasdaqStockRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public NasdaqStock createStock(@RequestBody NasdaqStock stock) {
        return nasdaqStockRepository.save(stock);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NasdaqStock> updateStock(@PathVariable Long id, @RequestBody NasdaqStock stock) {
        if (!nasdaqStockRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        stock.setId(id);
        return ResponseEntity.ok(nasdaqStockRepository.save(stock));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        if (!nasdaqStockRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        nasdaqStockRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}