package com.example.posgpt.controller;

import com.example.posgpt.service.AiAssistantService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author lakithaprabudh
 */
@RestController
@RequestMapping("/api/ai")
public class AiAssistantController {
    private final AiAssistantService aiAssistantService;

    public AiAssistantController(AiAssistantService aiAssistantService) {
        this.aiAssistantService = aiAssistantService;
    }

    /**
     * Anthropic AI Feature 1: Get product recommendations for a customer based on their purchase history
     */
    @GetMapping("/recommendations/{customerId}")
    public ResponseEntity<String> getProductRecommendations(@PathVariable Long customerId) {
        try {
            String recommendations = aiAssistantService.getProductRecommendations(customerId);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting recommendations: " + e.getMessage());
        }
    }

    /**
     * Anthropic AI Feature 2: Get sales insights and business intelligence for a specific period
     */
    @GetMapping("/sales-insights")
    public ResponseEntity<String> getSalesInsights(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            String insights = aiAssistantService.getSalesInsights(startDate, endDate);
            return ResponseEntity.ok(insights);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting sales insights: " + e.getMessage());
        }
    }
}
