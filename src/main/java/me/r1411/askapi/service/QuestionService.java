package me.r1411.askapi.service;

import me.r1411.askapi.model.Board;
import me.r1411.askapi.model.Question;
import me.r1411.askapi.model.User;
import me.r1411.askapi.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Question create(Question question, Board board, User author) {
        question.setBoard(board);
        question.setAuthor(author);
        question.setCreatedAt(new Date());
        return questionRepository.save(question);
    }

    public Optional<Question> findById(int id) {
        return questionRepository.findById(id);
    }

    public Question update(Question old, Question question) {
        question.setAuthor(old.getAuthor());
        question.setBoard(old.getBoard());
        question.setCreatedAt(old.getCreatedAt());
        question.setId(old.getId());
        return questionRepository.save(question);
    }

    public void deleteById(int id) {
        questionRepository.deleteById(id);
    }

    public Page<Question> findByBoardId(int boardId, Pageable pageable) {
        return questionRepository.findByBoardId(boardId, pageable);
    }
}
