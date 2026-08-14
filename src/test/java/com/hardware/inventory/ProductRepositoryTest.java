package com.hardware.inventory;

import com.hardware.inventory.model.Category;
import com.hardware.inventory.model.Product;
import com.hardware.inventory.repository.CategoryRepository;
import com.hardware.inventory.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.flyway.enabled=false"
})
@ActiveProfiles("dev")
class ProductRepositoryTest {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Should save and find product by ID")
    void testSaveAndFindProduct() {
        Category category = categoryRepository.save(Category.builder()
                .name("Storage Spec")
                .description("Storage testing")
                .build());

        Product product = Product.builder()
                .name("NVMe SSD 1TB")
                .description("High speed SSD")
                .price(new BigDecimal("99.99"))
                .stockQuantity(25)
                .category(category)
                .build();

        Product saved = productRepository.save(product);

        assertThat(saved.getId()).isNotNull();
        Optional<Product> found = productRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("NVMe SSD 1TB");
        assertThat(found.get().getCategory().getName()).isEqualTo("Storage Spec");
    }

    @Test
    @DisplayName("Should find products by category ID")
    void testFindByCategoryId() {
        Category category = categoryRepository.save(Category.builder()
                .name("Monitors")
                .description("Display screens")
                .build());

        productRepository.save(Product.builder()
                .name("4K OLED Display")
                .price(new BigDecimal("799.00"))
                .stockQuantity(5)
                .category(category)
                .build());

        List<Product> products = productRepository.findByCategoryId(category.getId());
        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("4K OLED Display");
    }

    @Test
    @DisplayName("Should count low stock products correctly")
    void testCountLowStock() {
        Category category = categoryRepository.save(Category.builder()
                .name("Peripherals")
                .build());

        productRepository.save(Product.builder()
                .name("Gaming Mouse")
                .price(new BigDecimal("49.99"))
                .stockQuantity(2) // low stock <= 5
                .category(category)
                .build());

        productRepository.save(Product.builder()
                .name("Mechanical Keyboard")
                .price(new BigDecimal("129.99"))
                .stockQuantity(20) // normal stock > 5
                .category(category)
                .build());

        long lowStockCount = productRepository.countLowStock(5);
        assertThat(lowStockCount).isGreaterThanOrEqualTo(1);
    }
}
