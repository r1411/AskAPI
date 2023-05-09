package me.r1411.askapi;

import me.r1411.askapi.controller.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SmokeTests {
    private final AuthController authController;
    private final BoardController boardController;
    private final QuestionController questionController;
    private final AnswerController answerController;
    private final UserController userController;

    @Autowired
    SmokeTests(AuthController authController, BoardController boardController, QuestionController questionController, AnswerController answerController, UserController userController) {
        this.authController = authController;
        this.boardController = boardController;
        this.questionController = questionController;
        this.answerController = answerController;
        this.userController = userController;
    }

    @Test
    void contextLoads() {
        assert (authController != null);
        assert (boardController != null);
        assert (questionController != null);
        assert (answerController != null);
        assert (userController != null);
    }
}
