package com.example.posgpt.service;

import com.example.posgpt.model.Product;
import com.example.posgpt.model.Sale;
import com.example.posgpt.model.SaleItem;
import com.example.posgpt.repository.ProductRepository;
import com.example.posgpt.repository.SaleItemRepository;
import com.example.posgpt.repository.SaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author lakithaprabudh
 */
@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final CustomerService customerService;

    public SaleService(SaleRepository saleRepository, SaleItemRepository saleItemRepository, ProductRepository productRepository, ProductService productService, CustomerService customerService) {
        this.saleRepository = saleRepository;
        this.saleItemRepository = saleItemRepository;
        this.productRepository = productRepository;
        this.productService = productService;
        this.customerService = customerService;
    }

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    public Optional<Sale> getSaleById(Long id) {
        return saleRepository.findById(id);
    }

    public List<Sale> getSalesByDateRange(LocalDateTime start, LocalDateTime end) {
        return saleRepository.findByDateTimeBetween(start, end);
    }

    public List<Sale> getSalesByStatus(String status) {
        return saleRepository.findByStatus(status);
    }

    public List<Sale> getHighValueSales(Double amount) {
        return saleRepository.findHighValueSales(amount);
    }

    @Transactional
    public Sale createSale(Sale sale, Map<Long, Integer> productQuantities) {
        // Set the current date and time
        sale.setDateTime(LocalDateTime.now());

        // Calculate total amount
        double totalAmount = 0.0;

        // Save the sale first
        Sale savedSale = saleRepository.save(sale);

        // Create sale items
        for (Map.Entry<Long, Integer> entry : productQuantities.entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

            // Update stock
            productService.updateStock(productId, -quantity);

            // Create sale item
            SaleItem saleItem = new SaleItem();
            saleItem.setSale(savedSale);
            saleItem.setProduct(product);
            saleItem.setQuantity(quantity);
            saleItem.setUnitPrice(product.getPrice());
            saleItem.setDiscount(0.0); // Default discount is 0
            saleItem.setSubtotal(product.getPrice() * quantity);

            saleItemRepository.save(saleItem);

            // Add to total
            totalAmount += saleItem.getSubtotal();
        }

        // Update sale with total amount
        savedSale.setTotalAmount(totalAmount);
        savedSale = saleRepository.save(savedSale);

        // Update customer loyalty points if customer exists
        if (savedSale.getCustomer() != null) {
            // Award 1 point per $1 spent
            int pointsToAdd = (int) totalAmount;
            customerService.updateLoyaltyPoints(savedSale.getCustomer().getId(), pointsToAdd);
        }

        return savedSale;
    }

    @Transactional
    public Sale updateSaleStatus(Long saleId, String newStatus) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        // If refunding, restore stock
        if (newStatus.equals("REFUNDED") && !sale.getStatus().equals("REFUNDED")) {
            for (SaleItem item : sale.getItems()) {
                productService.updateStock(item.getProduct().getId(), item.getQuantity());
            }

            // Also remove loyalty points if customer exists
            if (sale.getCustomer() != null) {
                int pointsToRemove = sale.getTotalAmount().intValue();
                customerService.updateLoyaltyPoints(sale.getCustomer().getId(), pointsToRemove);
            }
        }

        sale.setStatus(newStatus);
        return saleRepository.save(sale);
    }

    public List<Object[]> getTopSellingProducts() {
        return saleItemRepository.findTopSellingProducts();
    }
}
