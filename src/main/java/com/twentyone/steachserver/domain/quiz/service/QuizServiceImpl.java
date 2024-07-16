package com.twentyone.steachserver.domain.quiz.service;

import com.twentyone.steachserver.domain.lecture.Lecture;
import com.twentyone.steachserver.domain.lecture.service.LectureService;
import com.twentyone.steachserver.domain.quiz.dto.QuizRequestDto;
import com.twentyone.steachserver.domain.quiz.dto.QuizResponseDto;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.quiz.model.QuizChoice;
import com.twentyone.steachserver.domain.quiz.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class  QuizServiceImpl implements QuizService {

    private QuizRepository quizRepository;

    private QuizChoiceService quizChoiceService;
    private LectureService lectureService;

    @Override
    public QuizResponseDto createQuiz(QuizRequestDto request) {
        Optional<Lecture> lectureOpt = lectureService.findLectureById(request.getLectureId());
        if (lectureOpt.isEmpty()) {
            throw new RuntimeException("Lecture not found");
        }
        Lecture lecture = lectureOpt.get();

        Quiz quiz = Quiz.createQuiz(request, lecture);
        Quiz savedQuiz = quizRepository.save(quiz);

        // Create and save QuizChoice entities
        List<String> choices = request.getChoices();
        List<String> answers = request.getAnswers();
        quizChoiceService.createQuizChoices(choices, answers, savedQuiz);


        return QuizResponseDto.createQuizResponseDto(request);
    }


}
