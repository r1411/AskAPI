package me.r1411.askapi.controller;

import jakarta.validation.Valid;
import me.r1411.askapi.controller.wrapper.SuccessResponseEntity;
import me.r1411.askapi.dto.question.QuestionCreateRequestDto;
import me.r1411.askapi.dto.question.QuestionResponseDto;
import me.r1411.askapi.dto.question.QuestionUpdateRequestDto;
import me.r1411.askapi.exception.ObjectNotFoundException;
import me.r1411.askapi.mapper.MapStructMapper;
import me.r1411.askapi.model.Board;
import me.r1411.askapi.model.Question;
import me.r1411.askapi.model.Role;
import me.r1411.askapi.security.userdetails.UserDetailsImpl;
import me.r1411.askapi.service.BoardService;
import me.r1411.askapi.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;
    private final BoardService boardService;

    private final MapStructMapper mapper;

    @Autowired
    public QuestionController(QuestionService questionService, BoardService boardService, MapStructMapper mapper) {
        this.questionService = questionService;
        this.boardService = boardService;
        this.mapper = mapper;
    }

    @PostMapping("")
    public SuccessResponseEntity<QuestionResponseDto> createQuestion(
            @Valid @RequestBody QuestionCreateRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Board board = boardService.findById(requestDto.getBoardId())
                .orElseThrow(() -> new ObjectNotFoundException("Board with id " + requestDto.getBoardId() + " does not exist"));

        Question created = questionService.create(mapper.questionCreateRequestToQuestion(requestDto), board, userDetails.getUser());

        QuestionResponseDto responseDto = mapper.questionToQuestionResponse(created);

        return new SuccessResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public SuccessResponseEntity<QuestionResponseDto> questionById(@PathVariable("id") int id) {
        Question question = questionService.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Question with id " + id + " does not exist"));

        return new SuccessResponseEntity<>(mapper.questionToQuestionResponse(question), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public SuccessResponseEntity<QuestionResponseDto> updateQuestion(
            @PathVariable("id") int id,
            @Valid @RequestBody QuestionUpdateRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Question existing = questionService.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Question with id " + id + " does not exist"));

        if (existing.getAuthor().getId() != userDetails.getUser().getId() && userDetails.getUser().getRole() != Role.ADMIN)
            throw new AccessDeniedException("You can't edit this question");

        Question replaceWith = mapper.questionUpdateRequestToQuestion(requestDto);

        Question updated = questionService.update(existing, replaceWith);

        QuestionResponseDto responseDto = mapper.questionToQuestionResponse(updated);

        return new SuccessResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public SuccessResponseEntity<?> deleteById(@PathVariable("id") int id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Question existing = questionService.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Question with id " + id + " does not exist"));

        if (existing.getAuthor().getId() != userDetails.getUser().getId() && userDetails.getUser().getRole() != Role.ADMIN)
            throw new AccessDeniedException("You can't delete this question");

        questionService.deleteById(id);

        return new SuccessResponseEntity<>(HttpStatus.OK);
    }
}
