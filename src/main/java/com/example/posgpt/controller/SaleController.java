package com.example.posgpt.controller;

import com.example.posgpt.model.Sale;
import com.example.posgpt.service.SaleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author lakithaprabudh
 */
@RestController
@RequestMapping("/api/sales")
public class SaleController {
    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public ResponseEntity<List<Sale>> getAllSales() {
        return ResponseEntity.ok(saleService.getAllSales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        return saleService.getSaleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Sale>> getSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(saleService.getSalesByDateRange(start, end));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Sale>> getSalesByStatus(@PathVariable String status) {
        return ResponseEntity.ok(saleService.getSalesByStatus(status));
    }

    @GetMapping("/high-value")
    public ResponseEntity<List<Sale>> getHighValueSales(@RequestParam Double amount) {
        return ResponseEntity.ok(saleService.getHighValueSales(amount));
    }

    @GetMapping("/top-selling-products")
    public ResponseEntity<List<Object[]>> getTopSellingProducts() {
        return ResponseEntity.ok(saleService.getTopSellingProducts());
    }

    @PostMapping
    public ResponseEntity<Sale> createSale(
            @RequestBody Sale sale,
            @RequestBody Map<Long, Integer> productQuantities) {
        try {
            Sale createdSale = saleService.createSale(sale, productQuantities);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSale);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Sale> updateSaleStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            Sale updatedSale = saleService.updateSaleStatus(id, status);
            return ResponseEntity.ok(updatedSale);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
