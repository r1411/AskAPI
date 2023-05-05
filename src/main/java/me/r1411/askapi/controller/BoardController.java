package me.r1411.askapi.controller;

import jakarta.validation.Valid;
import me.r1411.askapi.controller.wrapper.SuccessResponseEntity;
import me.r1411.askapi.dto.board.BoardCreateRequestDto;
import me.r1411.askapi.dto.board.BoardListResponseDto;
import me.r1411.askapi.dto.board.BoardResponseDto;
import me.r1411.askapi.exception.ObjectNotFoundException;
import me.r1411.askapi.model.Board;
import me.r1411.askapi.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("")
    public SuccessResponseEntity<BoardListResponseDto> allBoards() {
        List<Board> boards = boardService.findAll();
        List<BoardResponseDto> boardsDtos = boards.stream().map(b -> new BoardResponseDto(b.getId(), b.getTitle())).toList();

        return new SuccessResponseEntity<>(new BoardListResponseDto(boardsDtos), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public SuccessResponseEntity<BoardResponseDto> boardById(@PathVariable("id") int id) {
        Optional<Board> boardOptional = boardService.findById(id);
        Board board = boardOptional.orElseThrow(() -> new ObjectNotFoundException("Board with id " + id + " does not exist"));

        return new SuccessResponseEntity<>(new BoardResponseDto(board.getId(), board.getTitle()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public SuccessResponseEntity<BoardResponseDto> createBoard(@RequestBody @Valid BoardCreateRequestDto requestDto) {
        Board board = requestDto.toBoard();
        Board created = boardService.create(board);

        return new SuccessResponseEntity<>(new BoardResponseDto(created.getId(), created.getTitle()), HttpStatus.OK);
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
    public SuccessResponseEntity<BoardResponseDto> updateBoard(@PathVariable("id") int id, @RequestBody @Valid BoardCreateRequestDto requestDto) {
        Optional<Board> boardOptional = boardService.findById(id);
        if (boardOptional.isEmpty())
            throw new ObjectNotFoundException("Board with id " + id + " does not exist");

        Board board = requestDto.toBoard();
        Board updated = boardService.update(id, board);

        return new SuccessResponseEntity<>(new BoardResponseDto(updated.getId(), updated.getTitle()), HttpStatus.OK);
    }
}
