package com.twentyone.steachserver.domain.quiz.service;

import com.twentyone.steachserver.domain.quiz.validator.QuizChoiceValidator;
import com.twentyone.steachserver.domain.quiz.validator.QuizValidator;
import com.twentyone.steachserver.domain.studentsQuizzes.service.StudentQuizzesService;
import com.twentyone.steachserver.domain.lecture.Lecture;
import com.twentyone.steachserver.domain.lecture.service.LectureService;
import com.twentyone.steachserver.domain.quiz.dto.QuizRequestDto;
import com.twentyone.steachserver.domain.quiz.dto.QuizResponseDto;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.quiz.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;

    private final QuizChoiceService quizChoiceService;
    private final LectureService lectureService;
    private final StudentQuizzesService studentQuizzesService;

    private final QuizValidator quizValidator;
    private final QuizChoiceValidator quizChoiceValidator;


    @Override
    public Optional<QuizResponseDto> createQuiz(QuizRequestDto request) throws Exception {
        Lecture lecture = getLecture(request);

        Quiz quiz = Quiz.createQuiz(request, lecture);
        Quiz savedQuiz = quizRepository.save(quiz);

        quizValidator.validateEmptyQuiz(savedQuiz);

        // Create and save QuizChoice entities
        List<String> choices = request.getChoices();
        List<String> answers = request.getAnswers();
        quizChoiceService.createQuizChoices(choices, answers, savedQuiz);

        return Optional.of(QuizResponseDto.createQuizResponseDto(request));
    }

    private Lecture getLecture(QuizRequestDto request) {
        Optional<Lecture> lectureOpt = lectureService.findLectureById(request.getLectureId());

        if (lectureOpt.isEmpty()) {
            throw new RuntimeException("Lecture not found");
        }
        return lectureOpt.get();
    }

    @Override
    public Optional<QuizResponseDto> getQuizResponseDto(Integer quizId) {
        Quiz quizById = findQuizById(quizId);
        return Optional.of(mapToDto(quizById));
    }

    private QuizResponseDto mapToDto(Quiz quiz) {
        List<String> choices = quizChoiceService.getChoices(quiz);
        List<String> answers = quizChoiceService.getAnswers(quiz);

        quizChoiceValidator.validateQuizChoices(answers, choices);

        return QuizResponseDto.createQuizResponseDto(quiz, choices, answers);
    }

    private Quiz findQuizById(Integer quizId) {
        Optional<Quiz> QuizOpt = quizRepository.findById(quizId);

        try {
            quizValidator.validateEmptyQuiz(QuizOpt);
        } catch (Exception e) {
            throw new RuntimeException("Quiz not found");
        }

        return QuizOpt.orElseThrow(() -> new RuntimeException("Quiz not found"));
    }


}
