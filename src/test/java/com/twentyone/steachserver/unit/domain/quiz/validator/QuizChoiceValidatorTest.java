package com.twentyone.steachserver.unit.domain.quiz.validator;

import com.twentyone.steachserver.SteachTest;
import com.twentyone.steachserver.domain.quiz.validator.QuizChoiceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


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
    public void testCreateQuizChoices_Failure_AnswersMoreThanChoices() {
        List<String> choices = Arrays.asList("Choice1", "Choice2");
        List<String> answers = Arrays.asList("Answer1", "Answer2", "Answer3");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            quizChoiceValidator.validateQuizChoices(choices, answers);
        });

        assertEquals("Answers cannot be more than choices", exception.getMessage());
    }

    @Test
    public void testCreateQuizChoices_Failure_EmptyAnswers() {
        List<String> choices = Arrays.asList("Choice1", "Choice2");
        List<String> answers = List.of();

        Exception exception = assertThrows(NullPointerException.class, () -> {
            quizChoiceValidator.validateQuizChoices(choices, answers);
        });

        assertEquals("Answers cannot be empty", exception.getMessage());
    }

    @Test
    public void testCreateQuizChoices_Failure_EmptyChoices() {
        List<String> choices = List.of();
        List<String> answers = Arrays.asList("Answer1", "Answer2");

        Exception exception = assertThrows(NullPointerException.class, () -> {
            quizChoiceValidator.validateQuizChoices(choices, answers);
        });

        assertEquals("Choices cannot be empty", exception.getMessage());
    }

}
