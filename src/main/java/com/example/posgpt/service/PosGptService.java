package com.example.posgpt.service;

import com.example.posgpt.dto.ChatMessageDto;
import com.example.posgpt.model.*;
import com.example.posgpt.repository.*;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author lakithaprabudh
 */
@Service
public class PosGptService {
    private final ChatModel chatModel;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;
    private final ChatRepository chatRepository;

    public PosGptService(ChatModel chatModel, CustomerRepository customerRepository, ProductRepository productRepository, SaleRepository saleRepository, ChatRepository chatRepository) {
        this.chatModel = chatModel;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.saleRepository = saleRepository;
        this.chatRepository = chatRepository;
    }

    /**
     * Process a customer chat message and generate an AI response
     */
    public ChatMessage processCustomerMessage(Long customerId, String messageContent) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setCustomer(customer);
        chatMessage.setMessageContent(messageContent);
        chatMessage.setTimestamp(LocalDateTime.now());

        // Determine message type
        String messageType = determineMessageType(messageContent);
        chatMessage.setMessageType(messageType);

        // Generate context-aware response
        String response = generateResponse(customer, messageContent, messageType);
        chatMessage.setResponseContent(response);

        // Extract any product references
        Optional<Product> referencedProduct = extractProductReference(messageContent);
        referencedProduct.ifPresent(product -> chatMessage.setRelatedProductId(product.getId()));

        // Extract any order/sale references
        Optional<Sale> referencedSale = extractSaleReference(messageContent, customer);
        referencedSale.ifPresent(sale -> chatMessage.setRelatedSaleId(sale.getId()));

        // Save the chat message
        return chatRepository.save(chatMessage);
    }

    /**
     * Determine the type of customer message
     */
    private String determineMessageType(String message) {
        message = message.toLowerCase();

        if (message.contains("order") || message.contains("purchase") || message.contains("delivery") ||
                message.contains("shipping") || message.contains("track") || message.contains("status")) {
            return "ORDER_TRACKING";
        }

        if (message.contains("recommend") || message.contains("suggestion") ||
                message.contains("what should i") || message.contains("best product")) {
            return "RECOMMENDATION";
        }

        if (message.contains("product") || message.contains("item") || message.contains("price") ||
                message.contains("cost") || message.contains("specification") || message.contains("details")) {
            return "PRODUCT_INQUIRY";
        }

        return "GENERAL";
    }

    /**
     * Generate a context-aware response using Anthropic's Claude AI
     */
    private String generateResponse(Customer customer, String messageContent, String messageType) {
        // Prepare customer context
        List<Sale> customerPurchases = saleRepository.findByCustomer(customer);
        StringBuilder purchaseHistory = new StringBuilder();

        if (!customerPurchases.isEmpty()) {
            purchaseHistory.append("Customer's purchase history:\n");
            for (Sale sale : customerPurchases) {
                purchaseHistory.append("- Order #").append(sale.getId())
                        .append(" (").append(sale.getDateTime()).append("): ");

                List<String> itemNames = new ArrayList<>();
                for (SaleItem item : sale.getItems()) {
                    itemNames.add(item.getProduct().getName());
                }

                purchaseHistory.append(String.join(", ", itemNames))
                        .append(". Status: ").append(sale.getStatus())
                        .append("\n");
            }
        } else {
            purchaseHistory.append("Customer has no previous purchase history.\n");
        }

        // Get product information for context
        List<Product> allProducts = productRepository.findAll();
        StringBuilder productContext = new StringBuilder("Available products:\n");
        for (Product product : allProducts) {
            productContext.append("- ").append(product.getName())
                    .append(" (").append(product.getCategory()).append("): $")
                    .append(product.getPrice())
                    .append(". Description: ").append(product.getDescription())
                    .append("\n");
        }

        // Create system prompt based on message type
        String systemPrompt;
        switch (messageType) {
            case "ORDER_TRACKING":
                systemPrompt = "You are a helpful POS system assistant specializing in order tracking. " +
                        "Help the customer track their orders or understand delivery status. " +
                        "Be precise about order details. If an order number is mentioned, provide specific details about that order. " +
                        "If no specific order is mentioned, summarize recent orders.\n\n" +
                        purchaseHistory;
                break;

            case "RECOMMENDATION":
                systemPrompt = "You are a helpful POS system assistant specializing in product recommendations. " +
                        "Recommend products based on the customer's purchase history and preferences. " +
                        "Be specific about why you're recommending each product. Limit to 3 recommendations.\n\n" +
                        purchaseHistory + "\n" + productContext;
                break;

            case "PRODUCT_INQUIRY":
                systemPrompt = "You are a helpful POS system assistant specializing in product information. " +
                        "Provide detailed information about products when asked. Include price, features, and availability. " +
                        "If a specific product is mentioned, focus on that. Otherwise, suggest popular categories.\n\n" +
                        productContext;
                break;

            default: // GENERAL
                systemPrompt = "You are a helpful POS system assistant for a retail store. " +
                        "Provide friendly and helpful responses to customer queries. " +
                        "You can help with product information, order tracking, recommendations, and general inquiries. " +
                        "Be conversational and supportive.\n\n" +
                        "Customer name: " + customer.getName() + "\n" +
                        "Loyalty level: " + customer.getLoyaltyLevel() + "\n" +
                        "Loyalty points: " + customer.getLoyaltyPoints() + "\n\n" +
                        purchaseHistory + "\n" + productContext.toString();
                break;
        }

        // Call Claude AI with the context
        SystemMessage systemMessage = new SystemMessage(systemPrompt);
        UserMessage userMessage = new UserMessage(messageContent);

        List<org.springframework.ai.chat.messages.Message> messages = new ArrayList<>();
        messages.add(systemMessage);
        messages.add(userMessage);

        return chatModel.call(systemMessage, userMessage);
    }

    /**
     * Extract product references from a message
     */
    private Optional<Product> extractProductReference(String message) {
        List<Product> allProducts = productRepository.findAll();

        for (Product product : allProducts) {
            if (message.toLowerCase().contains(product.getName().toLowerCase())) {
                return Optional.of(product);
            }
        }

        return Optional.empty();
    }

    /**
     * Extract sale/order references from a message
     */
    private Optional<Sale> extractSaleReference(String message, Customer customer) {
        // Look for order numbers in the message
        Pattern orderPattern = Pattern.compile("order\\s+#?(\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = orderPattern.matcher(message);

        if (matcher.find()) {
            try {
                Long orderId = Long.parseLong(matcher.group(1));
                return saleRepository.findById(orderId);
            } catch (NumberFormatException e) {
                // Not a valid number
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    /**
     * Get recent chat history for a customer
     */
    public List<ChatMessageDto> getCustomerChatHistory(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        List<ChatMessage> chatMessages = chatRepository.findByCustomerOrderByTimestampDesc(customer);
        return convertToDtoList(chatMessages);
    }

    /**
     * Get recent chat history for a customer within the last hours
     */
    public List<ChatMessageDto> getRecentCustomerChat(Long customerId, int hours) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        List<ChatMessage> chatMessages = chatRepository.findByCustomerAndTimestampAfterOrderByTimestampDesc(customer, since);
        return convertToDtoList(chatMessages);
    }

    /**
     * Convert ChatMessage entities to DTOs to avoid circular references in JSON
     */
    private List<ChatMessageDto> convertToDtoList(List<ChatMessage> chatMessages) {
        return chatMessages.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Convert a single ChatMessage entity to DTO
     */
    private ChatMessageDto convertToDto(ChatMessage chatMessage) {
        ChatMessageDto dto = new ChatMessageDto();

        dto.setId(chatMessage.getId());
        dto.setCustomerId(chatMessage.getCustomer().getId());
        dto.setCustomerName(chatMessage.getCustomer().getName());
        dto.setMessageContent(chatMessage.getMessageContent());
        dto.setResponseContent(chatMessage.getResponseContent());
        dto.setTimestamp(chatMessage.getTimestamp());
        dto.setMessageType(chatMessage.getMessageType());
        dto.setResultedInSale(chatMessage.isResultedInSale());
        dto.setResultedInReturn(chatMessage.isResultedInReturn());

        // Set related product info if present - look up the product by ID
        Long relatedProductId = chatMessage.getRelatedProductId();
        if (relatedProductId != null) {
            Optional<Product> product = productRepository.findById(relatedProductId);
            if (product.isPresent()) {
                dto.setRelatedProductId(relatedProductId);
                dto.setRelatedProductName(product.get().getName());
                dto.setRelatedProductPrice(product.get().getPrice());
                dto.setRelatedProductDescription(product.get().getDescription());
            }
        }

        // Set related sale info if present - look up the sale by ID
        Long relatedSaleId = chatMessage.getRelatedSaleId();
        if (relatedSaleId != null) {
            Optional<Sale> sale = saleRepository.findById(relatedSaleId);
            if (sale.isPresent()) {
                dto.setRelatedSaleId(relatedSaleId);
                dto.setRelatedSaleStatus(sale.get().getStatus());
                dto.setRelatedSaleDateTime(sale.get().getDateTime());
            }
        }

        return dto;
    }
}
