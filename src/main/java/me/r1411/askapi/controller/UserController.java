package me.r1411.askapi.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import me.r1411.askapi.controller.wrapper.SuccessResponseEntity;
import me.r1411.askapi.dto.answer.AnswerListPageResponseDto;
import me.r1411.askapi.dto.answer.AnswerResponseDto;
import me.r1411.askapi.dto.question.QuestionListPageResponseDto;
import me.r1411.askapi.dto.question.QuestionResponseDto;
import me.r1411.askapi.dto.user.UserResponseDto;
import me.r1411.askapi.exception.ObjectNotFoundException;
import me.r1411.askapi.mapper.MapStructMapper;
import me.r1411.askapi.model.Answer;
import me.r1411.askapi.model.Question;
import me.r1411.askapi.model.User;
import me.r1411.askapi.service.AnswerService;
import me.r1411.askapi.service.QuestionService;
import me.r1411.askapi.service.UserService;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    private final QuestionService questionService;

    private final AnswerService answerService;

    private final MapStructMapper mapper;

    @Autowired
    public UserController(UserService userService, QuestionService questionService, AnswerService answerService, MapStructMapper mapper) {
        this.userService = userService;
        this.questionService = questionService;
        this.answerService = answerService;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public SuccessResponseEntity<UserResponseDto> userById(@PathVariable int id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User with id " + id + " does not exist"));

        return new SuccessResponseEntity<>(mapper.userToUserResponse(user), HttpStatus.OK);
    }

    @GetMapping("/{id}/questions")
    public SuccessResponseEntity<QuestionListPageResponseDto> questionsByUser(
            @PathVariable
            int id,

            @NotNull(message = "Missing param: page")
            @Min(value = 0, message = "Param 'page' should be > 0")
            @RequestParam("page")
            int page,

            @NotNull(message = "Missing param: per_page")
            @Range(min = 1, max = 100, message = "Param 'per_page' should be between 1 and 100")
            @RequestParam("per_page")
            int perPage) {
        Optional<User> user = userService.findById(id);
        if (user.isEmpty())
            throw new ObjectNotFoundException("User with id " + id + " does not exist");

        Pageable pageable = PageRequest.of(page, perPage);
        Page<Question> questionsPage = questionService.findByAuthorId(id, pageable);
        List<Question> questions = questionsPage.getContent();
        List<QuestionResponseDto> questionDtos = questions.stream().map(mapper::questionToQuestionResponse).toList();
        QuestionListPageResponseDto responseDto = new QuestionListPageResponseDto(page, questionsPage.getTotalPages(), questionDtos);

        return new SuccessResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/{id}/answers")
    public SuccessResponseEntity<AnswerListPageResponseDto> answersByUser(
            @PathVariable
            int id,

            @NotNull(message = "Missing param: page")
            @Min(value = 0, message = "Param 'page' should be > 0")
            @RequestParam("page")
            int page,

            @NotNull(message = "Missing param: per_page")
            @Range(min = 1, max = 100, message = "Param 'per_page' should be between 1 and 100")
            @RequestParam("per_page")
            int perPage) {
        Optional<User> user = userService.findById(id);
        if (user.isEmpty())
            throw new ObjectNotFoundException("User with id " + id + " does not exist");

        Pageable pageable = PageRequest.of(page, perPage);
        Page<Answer> answersPage = answerService.findByUserId(id, pageable);
        List<Answer> answers = answersPage.getContent();
        List<AnswerResponseDto> answerDtos = answers.stream().map(mapper::answerToAnswerResponse).toList();
        AnswerListPageResponseDto responseDto = new AnswerListPageResponseDto(page, answersPage.getTotalPages(), answerDtos);

        return new SuccessResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
