package com.hardware.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@ActiveProfiles("dev")
class HardwareInventoryApplicationTests {


    @Test
    void contextLoads() {
        // Smoke test to ensure application context loads properly with dev profile
    }

}
