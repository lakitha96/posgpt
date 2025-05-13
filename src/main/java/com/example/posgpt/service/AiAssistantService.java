package com.example.posgpt.service;

import com.example.posgpt.model.Customer;
import com.example.posgpt.model.Product;
import com.example.posgpt.model.Sale;
import com.example.posgpt.repository.CustomerRepository;
import com.example.posgpt.repository.ProductRepository;
import com.example.posgpt.repository.SaleRepository;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lakithaprabudh
 */
@Service
public class AiAssistantService {
    private final ChatModel chatModel;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final SaleRepository saleRepository;

    public AiAssistantService(ChatModel chatModel, ProductRepository productRepository, CustomerRepository customerRepository, SaleRepository saleRepository) {
        this.chatModel = chatModel;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.saleRepository = saleRepository;
    }

    // Feature 1: Product recommendation based on customer purchase history
    public String getProductRecommendations(Long customerId) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isEmpty()) {
            return "Customer not found";
        }

        Customer customer = customerOptional.get();
        List<Sale> customerSales = saleRepository.findByCustomer(customer);

        if (customerSales.isEmpty()) {
            return "No purchase history found for this customer";
        }

        // Collect customer purchase data
        List<String> purchasedProducts = customerSales.stream()
                .flatMap(sale -> sale.getItems().stream())
                .map(item -> item.getProduct().getName())
                .distinct()
                .collect(Collectors.toList());

        String customerData = String.format(
                "Customer Name: %s\nLoyalty Level: %s\nPreviously Purchased Products: %s",
                customer.getName(),
                customer.getLoyaltyLevel(),
                String.join(", ", purchasedProducts)
        );

        // Get all available products
        List<Product> availableProducts = productRepository.findAll();
        String productsData = availableProducts.stream()
                .map(p -> String.format("%s (Category: %s, Price: $%.2f)",
                        p.getName(), p.getCategory(), p.getPrice()))
                .collect(Collectors.joining("\n"));

        // Create the prompt for Anthropic
        String prompt = String.format(
                "Based on this customer's purchase history, recommend 3 products they might be interested in purchasing.\n\n" +
                        "CUSTOMER INFORMATION:\n%s\n\n" +
                        "AVAILABLE PRODUCTS:\n%s\n\n" +
                        "Please provide your top 3 product recommendations with a brief explanation for each recommendation.",
                customerData, productsData
        );

        return chatModel.call(prompt);
    }

    // Feature 2: Sales insights and business intelligence
    public String getSalesInsights(LocalDateTime startDate, LocalDateTime endDate) {
        List<Sale> salesInPeriod = saleRepository.findByDateTimeBetween(startDate, endDate);

        if (salesInPeriod.isEmpty()) {
            return "No sales data found for the specified period";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Collect sales data
        double totalRevenue = salesInPeriod.stream()
                .mapToDouble(Sale::getTotalAmount)
                .sum();

        long transactionCount = salesInPeriod.size();

        double averageTransactionValue = totalRevenue / transactionCount;

        Map<String, Long> salesByPaymentMethod = salesInPeriod.stream()
                .collect(Collectors.groupingBy(Sale::getPaymentMethod, Collectors.counting()));

        // Create the prompt for Anthropic
        String prompt = String.format(
                "Analyze this sales data and provide business insights and recommendations:\n\n" +
                        "SALES DATA (Period: %s to %s):\n" +
                        "- Total Revenue: $%.2f\n" +
                        "- Transaction Count: %d\n" +
                        "- Average Transaction Value: $%.2f\n" +
                        "- Payment Methods Breakdown: %s\n\n" +
                        "Please provide:\n" +
                        "1. An overall assessment of the business performance during this period\n" +
                        "2. Key trends or patterns observed in the data\n" +
                        "3. Three actionable recommendations to improve sales performance\n" +
                        "4. Suggestions for inventory management based on the sales data",
                formatter.format(startDate), formatter.format(endDate),
                totalRevenue, transactionCount, averageTransactionValue,
                salesByPaymentMethod.entrySet().stream()
                        .map(e -> String.format("%s (%d)", e.getKey(), e.getValue()))
                        .collect(Collectors.joining(", "))
        );

        UserMessage userMessage = new UserMessage(prompt);

        return chatModel.call(userMessage.getText());
    }
}
