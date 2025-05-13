package com.example.posgpt.repository;

import com.example.posgpt.model.ChatMessage;
import com.example.posgpt.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lakithaprabudh
 */
public interface ChatRepository  extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByCustomerOrderByTimestampDesc(Customer customer);
    List<ChatMessage> findByCustomerAndTimestampAfterOrderByTimestampDesc(Customer customer, LocalDateTime since);
    List<ChatMessage> findByMessageTypeOrderByTimestampDesc(String messageType);
}
