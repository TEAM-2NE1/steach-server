package com.twentyone.steachserver.domain.quiz.service;

import com.twentyone.steachserver.domain.quiz.validator.QuizChoiceValidator;
import com.twentyone.steachserver.domain.quiz.validator.QuizValidator;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.service.LectureService;
import com.twentyone.steachserver.domain.quiz.dto.QuizRequestDto;
import com.twentyone.steachserver.domain.quiz.dto.QuizResponseDto;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.quiz.repository.QuizRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class  QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;

    private final QuizChoiceService quizChoiceService;
    private final LectureService lectureService;

    private final QuizValidator quizValidator;
    private final QuizChoiceValidator quizChoiceValidator;


    @Override
    @Transactional
    public Optional<Quiz> createQuiz(Integer lectureId, QuizRequestDto request) throws Exception {
        Lecture lecture = getLecture(lectureId);

        Quiz quiz = Quiz.createQuiz(request, lecture);
        Quiz savedQuiz = quizRepository.save(quiz);

        quizValidator.validateEmptyQuiz(savedQuiz);

        // Create and save QuizChoice entities
        List<String> choices = request.choices();
        List<String> answers = request.answers();
        quizChoiceService.createQuizChoices(choices, answers, savedQuiz);

        return Optional.of(savedQuiz);
    }

    private Lecture getLecture(Integer lectureId) {
        Optional<Lecture> lectureOpt = lectureService.findLectureById(lectureId);

        if (lectureOpt.isEmpty()) {
            throw new RuntimeException("Lecture not found");
        }
        return lectureOpt.get();
    }

    @Override
    public Optional<QuizResponseDto> getQuizResponseDto(Integer quizId) {
        Optional<Quiz> quizOpt = findQuizById(quizId);
        return Optional.of(mapToDto(quizOpt.orElse(null)));
    }

    private QuizResponseDto mapToDto(Quiz quiz) {
        List<String> choices = quizChoiceService.getChoices(quiz);
        List<String> answers = quizChoiceService.getAnswers(quiz);

        quizChoiceValidator.validateQuizChoices(answers, choices);

        return QuizResponseDto.createQuizResponseDto(quiz, choices, answers);
    }

    public Optional<Quiz> findQuizById(Integer quizId) {
        Optional<Quiz> QuizOpt = quizRepository.findById(quizId);

        try {
            quizValidator.validateEmptyQuiz(QuizOpt);
        } catch (Exception e) {
            throw new RuntimeException("Quiz not found");
        }

        return QuizOpt;
    }
}
