package me.r1411.askapi;

import me.r1411.askapi.controller.AuthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SmokeTests {
    private final AuthController authController;

    @Autowired
    SmokeTests(AuthController authController) {
        this.authController = authController;
    }

    @Test
    void contextLoads() {
        assert (authController != null);
    }
}
