package com.twentyone.steachserver.integration.quiz.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.quiz.model.QuizChoice;
import com.twentyone.steachserver.domain.quiz.repository.QuizChoiceRepository;
import com.twentyone.steachserver.domain.quiz.service.QuizChoiceService;
import com.twentyone.steachserver.domain.quiz.validator.QuizChoiceValidator;
import com.twentyone.steachserver.integration.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Transactional
@DisplayName("퀴즈 보기 관련 서비스 테스트")
public class QuizChoiceServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private QuizChoiceService quizChoiceService;
    /**
     * @Mock
     * @Mock은 Mockito에서 제공하는 어노테이션으로, 단순히 해당 필드의 모의 객체를 생성합니다. 이 모의 객체는 Mockito의 컨텍스트에서만 존재하며, Spring의 애플리케이션 컨텍스트와는 무관합니다.
     *
     * @MockBean
     * @MockBean은 Spring Boot에서 제공하는 어노테이션으로, Spring 애플리케이션 컨텍스트에 모의 객체를 생성하고 주입합니다. 이는 해당 모의 객체를 Spring 컨텍스트에 등록된 실제 빈 대신 사용할 수 있게 해줍니다.
     *
     * 테스트 코드에서 예외가 발생하지 않는 이유는 QuizChoiceValidator가 모킹되어 실제 메서드가 호출되지 않기 때문일 가능성이 높습니다.
     * 통합 테스트에서는 실제 QuizChoiceValidator 객체가 필요하며, 이를 위해 @MockBean 대신 @Autowired를 사용해야 합니다.
     */
    @Autowired
    private QuizChoiceValidator quizChoiceValidator;

    @MockBean
    private QuizChoiceRepository quizChoiceRepository;


    @Mock
    private Quiz savedQuiz;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateQuizChoices_Failure_NullChoices() {
        List<String> choices = null;
        List<String> answers = Arrays.asList("Answer1");

        Exception exception = assertThrows(NullPointerException.class, () -> {
            quizChoiceValidator.validateQuizChoices(choices, answers);
        });

        assertEquals("Choices cannot be null", exception.getMessage());
    }

    @Test
    public void testCreateQuizChoices_Failure_NullAnswers() {
        List<String> choices = Arrays.asList("Choice1", "Choice2");
        List<String> answers = null;

        Exception exception = assertThrows(NullPointerException.class, () -> {
            quizChoiceValidator.validateQuizChoices(choices, answers);
        });

        assertEquals("Answers cannot be null", exception.getMessage());
    }

    @Test
    public void testCreateQuizChoices_Failure_EmptyAnswers() {
        List<String> choices = Arrays.asList("Choice1", "Choice2");
        List<String> answers = List.of();
        Quiz savedQuiz = mock(Quiz.class);

        Exception exception = assertThrows(NullPointerException.class, () -> {
            quizChoiceService.createQuizChoices(choices, answers, savedQuiz);
        });

        assertEquals("Answers cannot be empty", exception.getMessage());
    }

    @Test
    public void testCreateQuizChoices_Failure_EmptyChoices() {
        List<String> choices = List.of();
        List<String> answers = Arrays.asList("Answer1", "Answer2");
        Quiz savedQuiz = mock(Quiz.class);

        Exception exception = assertThrows(NullPointerException.class, () -> {
            quizChoiceService.createQuizChoices(choices, answers, savedQuiz);
        });

        assertEquals("Choices cannot be empty", exception.getMessage());
    }


    @Test
    public void testCreateQuizChoices_Success() throws Exception {
        List<String> choices = Arrays.asList("Choice1", "Choice2", "Answer1");
        List<String> answers = Arrays.asList("Answer1");

        // 단위 테스트에서 테스트
//        doNothing().when(quizChoiceValidator).validateQuizChoices(choices, answers);
//        doNothing().when(savedQuiz).addChoice(any(QuizChoice.class));
        when(quizChoiceRepository.save(any(QuizChoice.class))).thenReturn(null);

        quizChoiceService.createQuizChoices(choices, answers, savedQuiz);

        verify(savedQuiz, times(3)).addChoice(any(QuizChoice.class));
        verify(quizChoiceRepository, times(3)).save(any(QuizChoice.class));
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
    public void testCreateQuizChoices_Success_SameSizeAnswersAndChoices() throws Exception {
        // Given
        List<String> choices = Arrays.asList("Answer3", "Answer2", "Answer1");
        List<String> answers = Arrays.asList("Answer1", "Answer2", "Answer3");
        Quiz savedQuiz = mock(Quiz.class);  // Quiz 객체를 mock으로 생성

//        doNothing().when(quizChoiceValidator).validateQuizChoices(choices, answers);
//        doNothing().when(savedQuiz).addChoice(any(QuizChoice.class));
        when(quizChoiceRepository.save(any(QuizChoice.class))).thenReturn(QuizChoice.createEmptyQuizChoice());  // null 대신 새 QuizChoice 객체 반환

        // When
        quizChoiceService.createQuizChoices(choices, answers, savedQuiz);

        // Then
        verify(savedQuiz, times(3)).addChoice(any(QuizChoice.class));
        verify(quizChoiceRepository, times(3)).save(any(QuizChoice.class));
    }
}
