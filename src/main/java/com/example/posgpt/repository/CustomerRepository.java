package com.example.posgpt.repository;

import com.example.posgpt.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author lakithaprabudh
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByPhone(String phone);
    List<Customer> findByLoyaltyLevel(String loyaltyLevel);
    List<Customer> findByNameContainingIgnoreCase(String name);
}