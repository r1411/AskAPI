package me.r1411.askapi.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import me.r1411.askapi.controller.wrapper.SuccessResponseEntity;
import me.r1411.askapi.dto.board.BoardRequestDto;
import me.r1411.askapi.dto.board.BoardListResponseDto;
import me.r1411.askapi.dto.board.BoardResponseDto;
import me.r1411.askapi.dto.question.QuestionListPageResponseDto;
import me.r1411.askapi.dto.question.QuestionResponseDto;
import me.r1411.askapi.exception.ObjectNotFoundException;
import me.r1411.askapi.mapper.MapStructMapper;
import me.r1411.askapi.model.Board;
import me.r1411.askapi.model.Question;
import me.r1411.askapi.service.BoardService;
import me.r1411.askapi.service.QuestionService;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    private final QuestionService questionService;

    private final MapStructMapper mapper;

    @Autowired
    public BoardController(BoardService boardService, QuestionService questionService, MapStructMapper mapper) {
        this.boardService = boardService;
        this.questionService = questionService;
        this.mapper = mapper;
    }

    @GetMapping("")
    public SuccessResponseEntity<BoardListResponseDto> allBoards() {
        List<Board> boards = boardService.findAll();
        List<BoardResponseDto> boardsDtos = boards.stream().map(mapper::boardToBoardResponse).toList();

        return new SuccessResponseEntity<>(new BoardListResponseDto(boardsDtos), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public SuccessResponseEntity<BoardResponseDto> boardById(@PathVariable("id") int id) {
        Optional<Board> boardOptional = boardService.findById(id);
        Board board = boardOptional.orElseThrow(() -> new ObjectNotFoundException("Board with id " + id + " does not exist"));

        return new SuccessResponseEntity<>(mapper.boardToBoardResponse(board), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public SuccessResponseEntity<BoardResponseDto> createBoard(@RequestBody @Valid BoardRequestDto requestDto) {
        Board board = mapper.boardRequestToBoard(requestDto);
        Board created = boardService.create(board);

        return new SuccessResponseEntity<>(mapper.boardToBoardResponse(created), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public SuccessResponseEntity<?> deleteBoard(@PathVariable("id") int id) {
        Optional<Board> boardOptional = boardService.findById(id);
        if (boardOptional.isEmpty())
            throw new ObjectNotFoundException("Board with id " + id + " does not exist");

        boardService.delete(id);

        return new SuccessResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public SuccessResponseEntity<BoardResponseDto> updateBoard(@PathVariable("id") int id, @RequestBody @Valid BoardRequestDto requestDto) {
        Optional<Board> boardOptional = boardService.findById(id);
        if (boardOptional.isEmpty())
            throw new ObjectNotFoundException("Board with id " + id + " does not exist");

        Board board = mapper.boardRequestToBoard(requestDto);
        Board updated = boardService.update(id, board);

        return new SuccessResponseEntity<>(mapper.boardToBoardResponse(updated), HttpStatus.OK);
    }

    @GetMapping("/{id}/questions")
    public SuccessResponseEntity<QuestionListPageResponseDto> questionListPage(@PathVariable("id") int boardId,

                                                                               @NotNull(message = "Missing param: page")
                                                                               @Min(value = 0, message = "Param 'page' should be > 0")
                                                                               @RequestParam("page") int page,

                                                                               @NotNull(message = "Missing param: per_page")
                                                                               @Range(min = 1, max = 100, message = "Param 'per_page' should be between 1 and 100")
                                                                               @RequestParam("per_page") int perPage) {
        Optional<Board> boardOptional = boardService.findById(boardId);
        if (boardOptional.isEmpty())
            throw new ObjectNotFoundException("Board with id " + boardId + " does not exist");

        Pageable pageable = PageRequest.of(page, perPage);
        Page<Question> questionsPage = questionService.findByBoardId(boardId, pageable);
        List<Question> questions = questionsPage.getContent();
        List<QuestionResponseDto> questionDtos = questions.stream().map(mapper::questionToQuestionResponse).toList();
        QuestionListPageResponseDto responseDto = new QuestionListPageResponseDto(page, questionsPage.getTotalPages(), questionDtos);

        return new SuccessResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
