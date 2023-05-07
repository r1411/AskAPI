package me.r1411.askapi.service;

import me.r1411.askapi.model.Board;
import me.r1411.askapi.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {
    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardService boardService;

    @Test
    void create_shouldCallRepository() {
        Board board = mock(Board.class);
        when(boardRepository.save(board)).thenReturn(board);

        boardService.create(board);

        verify(boardRepository).save(board);
    }

    @Test
    void update_shouldCallRepository() {
        Board board = mock(Board.class);
        when(boardRepository.save(board)).thenReturn(board);

        boardService.update(1, board);

        verify(board).setId(1);
        verify(boardRepository).save(board);
    }

    @Test
    void delete_shouldCallRepository() {
        boardService.delete(1);

        verify(boardRepository).deleteById(1);
    }

    @Test
    void findById_shouldCallRepository() {
        Board board = mock(Board.class);
        when(boardRepository.findById(0)).thenReturn(Optional.of(board));

        Optional<Board> foundBoard = boardService.findById(0);

        assertTrue(foundBoard.isPresent());
        assertEquals(foundBoard.get(), board);
        verify(boardRepository).findById(0);
    }

    @Test
    void findAll_shouldCallRepository() {
        Board board1 = mock(Board.class);
        Board board2 = mock(Board.class);
        Board board3 = mock(Board.class);
        List<Board> boardList = Arrays.asList(board1, board2, board3);
        when(boardRepository.findAll()).thenReturn(boardList);

        List<Board> foundBoards = boardService.findAll();

        assertEquals(foundBoards, boardList);
        verify(boardRepository).findAll();
    }
}
