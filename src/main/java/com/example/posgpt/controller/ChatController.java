package com.example.posgpt.controller;

import com.example.posgpt.dto.ChatMessageDto;
import com.example.posgpt.model.ChatMessage;
import com.example.posgpt.service.PosGptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author lakithaprabudh
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final PosGptService posGptService;

    public ChatController(PosGptService posGptService) {
        this.posGptService = posGptService;
    }

    /**
     * Send a customer message and get AI response
     */
    @PostMapping("/{customerId}")
    public ResponseEntity<ChatMessage> sendMessage(
            @PathVariable Long customerId,
            @RequestBody Map<String, String> request) {

        String messageContent = request.get("message");
        if (messageContent == null || messageContent.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        ChatMessage chatMessage = posGptService.processCustomerMessage(customerId, messageContent);
        return ResponseEntity.ok(chatMessage);
    }

    /**
     * Get chat history for a customer
     */
    @GetMapping("/{customerId}/history")
    public ResponseEntity<List<ChatMessageDto>> getChatHistory(@PathVariable Long customerId) {
        List<ChatMessageDto> chatHistory = posGptService.getCustomerChatHistory(customerId);
        return ResponseEntity.ok(chatHistory);
    }

    /**
     * Get recent chat for a customer (last N hours)
     */
    @GetMapping("/{customerId}/recent")
    public ResponseEntity<List<ChatMessageDto>> getRecentChat(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "24") int hours) {

        List<ChatMessageDto> recentChat = posGptService.getRecentCustomerChat(customerId, hours);
        return ResponseEntity.ok(recentChat);
    }
}
