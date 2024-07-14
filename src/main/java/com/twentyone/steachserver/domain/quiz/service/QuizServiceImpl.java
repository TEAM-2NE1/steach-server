package com.twentyone.steachserver.domain.quiz.service;

import com.twentyone.steachserver.domain.quiz.model.QuizChoice;
import com.twentyone.steachserver.domain.studentsQuizzes.StudentQuizzesService;
import com.twentyone.steachserver.domain.studentsQuizzes.StudentsQuizzes;
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
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;

    private final QuizChoiceService quizChoiceService;
    private final LectureService lectureService;
    private final StudentQuizzesService studentQuizzesService;

    @Override
    public Optional<QuizResponseDto> createQuiz(QuizRequestDto request) throws Exception {
        Lecture lecture = getLecture(request);

        Quiz quiz = Quiz.createQuiz(request, lecture);
        Quiz savedQuiz = quizRepository.save(quiz);

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
        Lecture lecture = lectureOpt.get();
        return lecture;
    }

    @Override
    public void enterScore(Integer studentId, Integer quizId, Integer score) {
        StudentsQuizzes byQuizIdAndStudentId = studentQuizzesService.findByQuizIdAndStudentId(quizId, studentId);
        byQuizIdAndStudentId.updateScore(score);
    }

    @Override
    public Optional<QuizResponseDto> getQuizResponseDto(Integer quizId) {
        return quizRepository.findById(quizId)
                .map(this::mapToDto);
    }

    private QuizResponseDto mapToDto(Quiz quiz) {
        List<String> choices = quiz.getQuiz().stream()
                .map(QuizChoice::getChoiceSentence)
                .collect(Collectors.toList());

        List<String> answers = quiz.getQuiz().stream()
                .filter(choice -> choice.getIsAnswer() == 1)
                .map(QuizChoice::getChoiceSentence)
                .collect(Collectors.toList());

        return QuizResponseDto.createQuizResponseDto(quiz, choices, answers);
    }



}
