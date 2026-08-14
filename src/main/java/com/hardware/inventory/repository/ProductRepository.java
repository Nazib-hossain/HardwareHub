package com.hardware.inventory.repository;

import com.hardware.inventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category")
    List<Product> findAllWithCategory();

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.id = :id")
    Product findByIdWithCategory(@Param("id") Long id);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.stockQuantity <= :threshold")
    long countLowStock(@Param("threshold") int threshold);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.category.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Product> searchProducts(@Param("query") String query);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE LOWER(p.category.name) = LOWER(:categoryName)")
    List<Product> findByCategoryName(@Param("categoryName") String categoryName);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "LOWER(p.category.name) = LOWER(:categoryName)")
    List<Product> searchProductsWithCategory(@Param("query") String query, @Param("categoryName") String categoryName);
}
