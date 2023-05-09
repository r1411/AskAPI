package me.r1411.askapi.service;

import me.r1411.askapi.model.Answer;
import me.r1411.askapi.model.Question;
import me.r1411.askapi.model.User;
import me.r1411.askapi.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class AnswerService {
    private final AnswerRepository answerRepository;

    @Autowired
    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Answer> findById(int answerId) {
        return answerRepository.findById(answerId);
    }

    @Transactional(readOnly = true)
    public Page<Answer> findByQuestionId(int questionId, Pageable pageable) {
        return answerRepository.findByQuestionId(questionId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Answer> findByUserId(int userId, Pageable pageable) {
        return answerRepository.findByUserId(userId, pageable);
    }

    public Answer create(Answer answer, User user, Question question) {
        answer.setQuestion(question);
        answer.setUser(user);
        answer.setCreatedAt(new Date());
        return answerRepository.save(answer);
    }

    public Answer update(Answer old, Answer answer) {
        answer.setId(old.getId());
        answer.setUser(old.getUser());
        answer.setQuestion(old.getQuestion());
        answer.setCreatedAt(old.getCreatedAt());
        return answerRepository.save(answer);
    }

    public void deleteById(int answerId) {
        answerRepository.deleteById(answerId);
    }
}
