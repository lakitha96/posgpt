package com.example.posgpt.repository;

import com.example.posgpt.model.Customer;
import com.example.posgpt.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lakithaprabudh
 */
public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByCustomer(Customer customer);
    List<Sale> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Sale> findByStatus(String status);

    @Query("SELECT s FROM Sale s WHERE s.totalAmount > :amount")
    List<Sale> findHighValueSales(Double amount);
}