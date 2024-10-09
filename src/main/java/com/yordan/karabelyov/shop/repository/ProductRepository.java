package com.yordan.karabelyov.shop.repository;

import com.yordan.karabelyov.shop.entity.Product;
import com.yordan.karabelyov.shop.repository.impl.criteria.holder.ProductCriteriaHolder;
import com.yordan.karabelyov.shop.repository.impl.criteria.holder.root.SimplePredicateGenerationHolder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom, CriteriaRepository<Product, ProductCriteriaHolder, SimplePredicateGenerationHolder<Product>> {

    @Modifying
    @Query("DELETE FROM Product p JOIN p.productSubscribers ps WHERE p.id = :productId")
    void deleteProductSubscriptions(@Param("productId") Long productId);

    boolean existsByName(String name);

    Product findByName(String name);

    @Query("SELECT p FROM Product p " +
            "JOIN FETCH p.productStatus ps " +
            "LEFT JOIN FETCH p.productSubscribers ps2 " +
            "WHERE p.id = :productId")
    Optional<Product> findProductWithStatusAndSubscribers(@Param("productId") Long productId);

    @EntityGraph(attributePaths = {"productSubscribers"})
    Optional<Product> findById(@Param("id") Long id);

}
