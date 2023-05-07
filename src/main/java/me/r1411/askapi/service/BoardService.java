package me.r1411.askapi.service;

import me.r1411.askapi.model.Board;
import me.r1411.askapi.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Board create(Board board) {
        return boardRepository.save(board);
    }

    public Board update(int id, Board board) {
        board.setId(id);
        return boardRepository.save(board);
    }

    public void delete(int id) {
        boardRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Board> findById(int id) {
        return boardRepository.findById(id);
    }
}
