package me.r1411.askapi;

import me.r1411.askapi.controller.AuthController;
import me.r1411.askapi.controller.BoardController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SmokeTests {
    private final AuthController authController;
    private final BoardController boardController;

    @Autowired
    SmokeTests(AuthController authController, BoardController boardController) {
        this.authController = authController;
        this.boardController = boardController;
    }

    @Test
    void contextLoads() {
        assert (authController != null);
        assert (boardController != null);
    }
}
