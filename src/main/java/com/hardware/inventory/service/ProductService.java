package com.hardware.inventory.service;

import com.hardware.inventory.model.Product;
import com.hardware.inventory.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAllWithCategory();
    }

    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(productRepository.findByIdWithCategory(id));
    }

    @Transactional(readOnly = true)
    public List<Product> findByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Transactional(readOnly = true)
    public List<Product> search(String query, String categoryName) {
        boolean hasQuery = query != null && !query.trim().isEmpty();
        boolean hasCategory = categoryName != null && !categoryName.trim().isEmpty();

        if (hasQuery && hasCategory) {
            return productRepository.searchProductsWithCategory(query.trim(), categoryName.trim());
        } else if (hasQuery) {
            return productRepository.searchProducts(query.trim());
        } else if (hasCategory) {
            return productRepository.findByCategoryName(categoryName.trim());
        }
        return findAll();
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long count() {
        return productRepository.count();
    }

    @Transactional(readOnly = true)
    public long countLowStock(int threshold) {
        return productRepository.countLowStock(threshold);
    }

    @Transactional(readOnly = true)
    public List<Product> findFeatured(int limit) {
        List<Product> all = new ArrayList<>(findAll());
        Collections.shuffle(all);
        return all.subList(0, Math.min(limit, all.size()));
    }
}
