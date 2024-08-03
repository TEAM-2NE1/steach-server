package com.twentyone.steachserver.unit.domain.quiz.validator;

import com.twentyone.steachserver.SteachTest;
import com.twentyone.steachserver.domain.quiz.validator.QuizChoiceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DisplayName("퀴즈 보기 검증 단위 테스트")
public class QuizChoiceValidatorTest extends SteachTest {

    @InjectMocks
    private QuizChoiceValidator quizChoiceValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateQuizChoices_Failure_NullChoices() {
        List<String> choices = null;
        int answer = 1;

        Exception exception = assertThrows(NullPointerException.class, () -> {
            quizChoiceValidator.validateQuizChoices(choices, answer);
        });

        assertEquals("Choices cannot be null", exception.getMessage());
    }

    @Test
    public void testCreateQuizChoices_Failure_AnswerZero() {
        List<String> choices = Arrays.asList("Choice1", "Choice2");
        int answer = 1;

        Exception exception = assertThrows(NullPointerException.class, () -> {
            quizChoiceValidator.validateQuizChoices(choices, answer);
        });

        assertEquals("Answers cannot be null", exception.getMessage());
    }

    @Test
    public void testCreateQuizChoices_Failure_EmptyChoices() {
        List<String> choices = List.of();
        int answer = 1;

        Exception exception = assertThrows(NullPointerException.class, () -> {
            quizChoiceValidator.validateQuizChoices(choices, answer);
        });

        assertEquals("Choices cannot be empty", exception.getMessage());
    }

}
