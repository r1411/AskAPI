package me.r1411.askapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import me.r1411.askapi.controller.wrapper.SuccessResponseEntity;
import me.r1411.askapi.dto.answer.AnswerListPageResponseDto;
import me.r1411.askapi.dto.answer.AnswerRequestDto;
import me.r1411.askapi.dto.answer.AnswerResponseDto;
import me.r1411.askapi.exception.ObjectNotFoundException;
import me.r1411.askapi.mapper.MapStructMapper;
import me.r1411.askapi.model.Answer;
import me.r1411.askapi.model.Question;
import me.r1411.askapi.model.Role;
import me.r1411.askapi.security.userdetails.UserDetailsImpl;
import me.r1411.askapi.service.AnswerService;
import me.r1411.askapi.service.QuestionService;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SecurityRequirement(name = "bearer-key")
@Tag(name = "answers", description = "Действия с ответами")
@RestController
@RequestMapping("/answers")
public class AnswerController {
    private final AnswerService answerService;

    private final QuestionService questionService;

    private final MapStructMapper mapper;

    @Autowired
    public AnswerController(AnswerService answerService, QuestionService questionService, MapStructMapper mapper) {
        this.answerService = answerService;
        this.questionService = questionService;
        this.mapper = mapper;
    }

    @Operation(
            operationId = "answersForQuestion",
            summary = "Получить ответы на вопрос по id вопроса"
    )
    @GetMapping("")
    public SuccessResponseEntity<AnswerListPageResponseDto> answersForQuestion(
            @NotNull(message = "missing param: question_id")
            @RequestParam("question_id")
            int questionId,

            @NotNull(message = "Missing param: page")
            @Min(value = 0, message = "Param 'page' should be > 0")
            @RequestParam("page")
            int page,

            @NotNull(message = "Missing param: per_page")
            @Range(min = 1, max = 100, message = "Param 'per_page' should be between 1 and 100")
            @RequestParam("per_page")
            int perPage) {
        Optional<Question> question = questionService.findById(questionId);
        if (question.isEmpty())
            throw new ObjectNotFoundException("Question with id " + questionId + " does not exist");

        Pageable pageable = PageRequest.of(page, perPage);
        Page<Answer> answersPage = answerService.findByQuestionId(questionId, pageable);
        List<Answer> answers = answersPage.getContent();
        List<AnswerResponseDto> answersDtos = answers.stream().map(mapper::answerToAnswerResponse).toList();
        AnswerListPageResponseDto responseDto = new AnswerListPageResponseDto(page, answersPage.getTotalPages(), answersDtos);

        return new SuccessResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(
            operationId = "createAnswer",
            summary = "Ответить на вопрос по id вопроса"
    )
    @PostMapping("")
    public SuccessResponseEntity<AnswerResponseDto> createAnswer(
            @RequestParam("question_id")
            @NotNull(message = "missing param: question_id")
            int questionId,

            @Valid
            @RequestBody
            AnswerRequestDto requestDto,

            @AuthenticationPrincipal
            UserDetailsImpl userDetails) {
        Question question = questionService.findById(questionId)
                .orElseThrow(() -> new ObjectNotFoundException("Question with id " + questionId + " does not exist"));

        if (question.getAuthor().getId() == userDetails.getUser().getId() && userDetails.getUser().getRole() != Role.ADMIN)
            throw new AccessDeniedException("You can't reply to your own question");

        Answer created = answerService.create(mapper.answerRequestToAnswer(requestDto), userDetails.getUser(), question);
        AnswerResponseDto responseDto = mapper.answerToAnswerResponse(created);

        return new SuccessResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(
            operationId = "updateAnswer",
            summary = "Изменить ответ по его id"
    )
    @PutMapping("/{id}")
    public SuccessResponseEntity<AnswerResponseDto> updateAnswer(
            @PathVariable("id") int answerId,
            @Valid @RequestBody AnswerRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Answer existing = answerService.findById(answerId)
                .orElseThrow(() -> new ObjectNotFoundException("Answer with id " + answerId + " does not exist"));

        if (existing.getUser().getId() != userDetails.getUser().getId() && userDetails.getUser().getRole() != Role.ADMIN)
            throw new AccessDeniedException("You can't edit this answer");

        Answer updated = answerService.update(existing, mapper.answerRequestToAnswer(requestDto));
        AnswerResponseDto responseDto = mapper.answerToAnswerResponse(updated);

        return new SuccessResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(
            operationId = "deleteAnswer",
            summary = "Удалить ответ по его id"
    )
    @DeleteMapping("/{id}")
    public SuccessResponseEntity<?> deleteAnswer(
            @PathVariable("id") int answerId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Answer existing = answerService.findById(answerId)
                .orElseThrow(() -> new ObjectNotFoundException("Answer with id " + answerId + " does not exist"));

        if (existing.getUser().getId() != userDetails.getUser().getId() && userDetails.getUser().getRole() != Role.ADMIN)
            throw new AccessDeniedException("You can't delete this answer");

        answerService.deleteById(answerId);

        return new SuccessResponseEntity<>(HttpStatus.OK);
    }
}
