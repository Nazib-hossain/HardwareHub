package com.hardware.inventory;

import com.hardware.inventory.model.Product;
import com.hardware.inventory.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.junit.jupiter.api.Disabled;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ActiveProfiles("prod")
@Disabled("Disabled because Docker is not installed/running on the local system")
class ProductIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Should run Flyway migrations and seed data against Dockerized PostgreSQL container")
    void testPostgresIntegrationWithFlyway() {
        assertThat(postgres.isRunning()).isTrue();

        List<Product> products = productRepository.findAll();
        assertThat(products).isNotEmpty();
        assertThat(products.size()).isGreaterThanOrEqualTo(10);
    }
}
