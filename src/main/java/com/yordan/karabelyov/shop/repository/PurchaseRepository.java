package com.yordan.karabelyov.shop.repository;

import com.yordan.karabelyov.shop.entity.Product;
import com.yordan.karabelyov.shop.entity.Purchase;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    @Query(value = "SELECT SUM(p.quantity) FROM purchases p " +
            "JOIN products pr ON p.product_id = pr.id " +
            "WHERE (:startDate IS NULL OR p.created_at >= CAST(:startDate AS timestamp)) " +
            "AND (:endDate IS NULL OR p.created_at < CAST(:endDate AS timestamp)) " +
            "AND pr.status_id = :statusId", nativeQuery = true)
    Long countSoldProducts(@Param("startDate") LocalDate startDate,
                           @Param("endDate") LocalDate endDate,
                           @Param("statusId") Long statusId);

    @Query("SELECT p.product FROM Purchase p " +
            "GROUP BY p.product ORDER BY SUM(p.quantity) DESC")
    List<Product> findMostPopularProducts(Pageable pageable);
}
