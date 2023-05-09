package me.r1411.askapi.service;

import me.r1411.askapi.model.Answer;
import me.r1411.askapi.repository.AnswerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnswerServiceTest {
    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private AnswerService answerService;

    @Test
    void findByQuestionId_shouldCallRepository() {
        Pageable pageable = mock(Pageable.class);
        Page<Answer> answersPage = mock(Page.class);
        when(answerRepository.findByQuestionId(0, pageable)).thenReturn(answersPage);

        Page<Answer> found = answerService.findByQuestionId(0, pageable);

        assertEquals(answersPage, found);
        verify(answerRepository).findByQuestionId(0, pageable);
    }

    @Test
    void findByUserId_shouldCallRepository() {
        Pageable pageable = mock(Pageable.class);
        Page<Answer> answersPage = mock(Page.class);
        when(answerRepository.findByUserId(0, pageable)).thenReturn(answersPage);

        Page<Answer> found = answerService.findByUserId(0, pageable);

        assertEquals(answersPage, found);
        verify(answerRepository).findByUserId(0, pageable);
    }
}
