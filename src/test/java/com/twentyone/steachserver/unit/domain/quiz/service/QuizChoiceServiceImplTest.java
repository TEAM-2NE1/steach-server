package com.twentyone.steachserver.unit.domain.quiz.service;

import static org.mockito.Mockito.*;

import com.twentyone.steachserver.SteachTest;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.quiz.model.QuizChoice;
import com.twentyone.steachserver.domain.quiz.repository.QuizChoiceRepository;
import com.twentyone.steachserver.domain.quiz.service.QuizChoiceServiceImpl;
import com.twentyone.steachserver.domain.quiz.validator.QuizChoiceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Transactional
@DisplayName("퀴즈 보기 관련 서비스 테스트")
public class QuizChoiceServiceImplTest extends SteachTest {

    @InjectMocks
    private QuizChoiceServiceImpl quizChoiceService;

    @Mock
    private QuizChoiceRepository quizChoiceRepository;

    @Mock
    private QuizChoiceValidator quizChoiceValidator;

    @Mock
    private Quiz savedQuiz;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateQuizChoices_Success_SameSizeAnswersAndChoices() throws Exception {
        List<String> choices = Arrays.asList("Answer3", "Answer2", "Answer1");
        List<String> answers = Arrays.asList("Answer1", "Answer2", "Answer3");

        doNothing().when(quizChoiceValidator).validateQuizChoices(choices, answers);
        doNothing().when(savedQuiz).addChoice(any(QuizChoice.class));
        when(quizChoiceRepository.save(any(QuizChoice.class))).thenReturn(null);

        quizChoiceService.createQuizChoices(choices, answers, savedQuiz);

        verify(savedQuiz, times(3)).addChoice(any(QuizChoice.class));
        verify(quizChoiceRepository, times(3)).save(any(QuizChoice.class));
    }
    @Test
    public void testCreateQuizChoices_Success() throws Exception {
        List<String> choices = Arrays.asList("Choice1", "Choice2", "Answer1");
        List<String> answers = Arrays.asList("Answer1");

        doNothing().when(quizChoiceValidator).validateQuizChoices(choices, answers);
        doNothing().when(savedQuiz).addChoice(any(QuizChoice.class));
        when(quizChoiceRepository.save(any(QuizChoice.class))).thenReturn(null);

        quizChoiceService.createQuizChoices(choices, answers, savedQuiz);

        verify(savedQuiz, times(3)).addChoice(any(QuizChoice.class));
    }
}
