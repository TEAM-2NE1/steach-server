package com.twentyone.steachserver.domain.quiz.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.quiz.model.QuizChoice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

public class QuizChoiceServiceImplTest {

    private QuizChoiceServiceImpl quizChoiceService;

    @Mock
    private Quiz savedQuiz;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        quizChoiceService = new QuizChoiceServiceImpl();
    }

    @Test
    public void testCreateQuizChoices_Failure_NullQuiz() {
        List<String> choices = Arrays.asList("Choice1", "Choice2");
        List<String> answers = Arrays.asList("Answer1");

        Exception exception = assertThrows(NullPointerException.class, () -> {
            quizChoiceService.createQuizChoices(choices, answers, null);
        });

        assertEquals("Quiz cannot be null", exception.getMessage());
    }

    @Test
    public void testCreateQuizChoices_Failure_NullChoices() {
        List<String> choices = null;
        List<String> answers = Arrays.asList("Answer1");

        Exception exception = assertThrows(NullPointerException.class, () -> {
            quizChoiceService.createQuizChoices(choices, answers, savedQuiz);
        });

        assertEquals("Choices cannot be empty", exception.getMessage());
    }

    @Test
    public void testCreateQuizChoices_Failure_NullAnswers() {
        List<String> choices = Arrays.asList("Choice1", "Choice2");
        List<String> answers = null;

        Exception exception = assertThrows(NullPointerException.class, () -> {
            quizChoiceService.createQuizChoices(choices, answers, savedQuiz);
        });

        assertEquals("Answers cannot be empty", exception.getMessage());
    }

    @Test
    public void testCreateQuizChoices_Failure_AnswersMoreThanChoices() {
        List<String> choices = Arrays.asList("Choice1", "Choice2");
        List<String> answers = Arrays.asList("Answer1", "Answer2", "Answer3");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            quizChoiceService.createQuizChoices(choices, answers, savedQuiz);
        });

        assertEquals("Answers cannot be more than choices", exception.getMessage());
    }

    @Test
    public void testCreateQuizChoices_Failure_EmptyAnswers() {
        List<String> choices = Arrays.asList("Choice1", "Choice2");
        List<String> answers = Arrays.asList();

        Exception exception = assertThrows(NullPointerException.class, () -> {
            quizChoiceService.createQuizChoices(choices, answers, savedQuiz);
        });

        assertEquals("Answers cannot be empty", exception.getMessage());
    }

    @Test
    public void testCreateQuizChoices_Failure_EmptyChoices() {
        List<String> choices = Arrays.asList();
        List<String> answers = Arrays.asList("Answer", "Answer2");

        Exception exception = assertThrows(NullPointerException.class, () -> {
            quizChoiceService.createQuizChoices(choices, answers, savedQuiz);
        });

        assertEquals("Choices cannot be empty", exception.getMessage());
    }

    @Test
    public void testCreateQuizChoices_Failure_NotSameAnswersAndChoices() {
        List<String> choices = Arrays.asList("Choice1", "Choice2");
        List<String> answers = Arrays.asList("Answer1", "Answer2");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            quizChoiceService.createQuizChoices(choices, answers, savedQuiz);
        });

        assertEquals("Answers cannot be empty", exception.getMessage());
    }

    @Test
    public void testCreateQuizChoices_Success() throws Exception {
        List<String> choices = Arrays.asList("Choice1", "Choice2", "Answer1");
        List<String> answers = Arrays.asList("Answer1");

        // 실제로 QuizChoice.createQuizChoice 메서드를 호출하는 것을 스터빙합니다.
        doNothing().when(savedQuiz).addChoice(any(QuizChoice.class));

        quizChoiceService.createQuizChoices(choices, answers, savedQuiz);

        // 기대하는대로 QuizChoice.createQuizChoice 메서드가 호출되었는지 확인합니다.
        // 여기서는 QuizChoice의 정적 메서드 호출을 확인할 수 없으므로, 실제로 잘 작동하는지 여부는
        // 다른 방법으로 확인해야 합니다.
        // 예: mockito-inline을 사용하여 정적 메서드를 모킹하거나, QuizChoice.createQuizChoice의 구현을
        // 인스턴스 메서드로 변경하여 테스트할 수 있습니다.
        // 각 선택지가 저장된 퀴즈에 추가되는지 확인합니다.
        verify(savedQuiz, times(3)).addChoice(any(QuizChoice.class));
    }

    @Test
    public void testCreateQuizChoices_Success_SameSizeAnswersAndChoices() throws Exception {
        List<String> choices = Arrays.asList("Answer3", "Answer2", "Answer1");
        List<String> answers = Arrays.asList("Answer1", "Answer2", "Answer3");

        // 실제로 QuizChoice.createQuizChoice 메서드를 호출하는 것을 스터빙합니다.
        doNothing().when(savedQuiz).addChoice(any(QuizChoice.class));

        quizChoiceService.createQuizChoices(choices, answers, savedQuiz);

        // 기대하는대로 QuizChoice.createQuizChoice 메서드가 호출되었는지 확인합니다.
        // 여기서는 QuizChoice의 정적 메서드 호출을 확인할 수 없으므로, 실제로 잘 작동하는지 여부는
        // 다른 방법으로 확인해야 합니다.
        // 예: mockito-inline을 사용하여 정적 메서드를 모킹하거나, QuizChoice.createQuizChoice의 구현을
        // 인스턴스 메서드로 변경하여 테스트할 수 있습니다.
        // 각 선택지가 저장된 퀴즈에 추가되는지 확인합니다.
        verify(savedQuiz, times(3)).addChoice(any(QuizChoice.class));
    }
}
