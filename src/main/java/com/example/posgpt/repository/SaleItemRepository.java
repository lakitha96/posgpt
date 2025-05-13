package com.example.posgpt.repository;

import com.example.posgpt.model.Product;
import com.example.posgpt.model.Sale;
import com.example.posgpt.model.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author lakithaprabudh
 */
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
    List<SaleItem> findBySale(Sale sale);
    List<SaleItem> findByProduct(Product product);

    @Query("SELECT si.product.id, SUM(si.quantity) as totalSold FROM SaleItem si GROUP BY si.product.id ORDER BY totalSold DESC")
    List<Object[]> findTopSellingProducts();
}