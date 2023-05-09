package me.r1411.askapi.service;

import me.r1411.askapi.model.Board;
import me.r1411.askapi.model.Question;
import me.r1411.askapi.model.User;
import me.r1411.askapi.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {
    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    @Test
    void create_shouldCallRepository() {
        Question question = mock(Question.class);
        Board board = mock(Board.class);
        User author = mock(User.class);
        when(questionRepository.save(question)).thenReturn(question);

        questionService.create(question, board, author);

        verify(questionRepository).save(question);
    }

    @Test
    void create_shouldSetQuestionFields() {
        Question question = mock(Question.class);
        Board board = mock(Board.class);
        User author = mock(User.class);
        when(questionRepository.save(question)).thenReturn(question);

        questionService.create(question, board, author);

        verify(question).setBoard(board);
        verify(question).setAuthor(author);
        verify(question).setCreatedAt(any(Date.class));
    }

    @Test
    void findById_shouldCallRepository() {
        Question question = mock(Question.class);
        when(questionRepository.findById(0)).thenReturn(Optional.of(question));

        Optional<Question> questionOptional = questionService.findById(0);

        assertTrue(questionOptional.isPresent());
        assertEquals(questionOptional.get(), question);
        verify(questionRepository).findById(0);
    }

    @Test
    void update_shouldCallRepository() {
        Question question1 = mock(Question.class);
        Question question2 = mock(Question.class);
        when(questionRepository.save(question2)).thenReturn(question2);

        questionService.update(question1, question2);

        verify(questionRepository).save(question2);
    }

    @Test
    void update_shouldSetQuestionFields() {
        User author = mock(User.class);
        Board board = mock(Board.class);
        Date createdAt = new Date();
        Question question1 = mock(Question.class);
        when(question1.getAuthor()).thenReturn(author);
        when(question1.getBoard()).thenReturn(board);
        when(question1.getCreatedAt()).thenReturn(createdAt);
        when(question1.getId()).thenReturn(123);
        Question question2 = mock(Question.class);
        when(questionRepository.save(question2)).thenReturn(question2);

        questionService.update(question1, question2);

        verify(question2).setBoard(board);
        verify(question2).setAuthor(author);
        verify(question2).setCreatedAt(createdAt);
        verify(question2).setId(123);
    }

    @Test
    void deleteById_shouldCallRepository() {
        questionService.deleteById(0);

        verify(questionRepository).deleteById(0);
    }

    @Test
    void findByBoardId_shouldCallRepository() {
        Pageable pageable = mock(Pageable.class);
        Page<Question> questionsPage = mock(Page.class);
        when(questionRepository.findByBoardId(0, pageable)).thenReturn(questionsPage);

        Page<Question> found = questionService.findByBoardId(0, pageable);

        assertEquals(questionsPage, found);
        verify(questionRepository).findByBoardId(0, pageable);
    }

    @Test
    void findByAuthorId_shouldCallRepository() {
        Pageable pageable = mock(Pageable.class);
        Page<Question> questionsPage = mock(Page.class);
        when(questionRepository.findByAuthorId(0, pageable)).thenReturn(questionsPage);

        Page<Question> found = questionService.findByAuthorId(0, pageable);

        assertEquals(questionsPage, found);
        verify(questionRepository).findByAuthorId(0, pageable);
    }
}
