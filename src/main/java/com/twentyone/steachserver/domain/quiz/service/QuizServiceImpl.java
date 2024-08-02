package com.twentyone.steachserver.domain.quiz.service;

import com.twentyone.steachserver.domain.auth.error.ForbiddenException;
import com.twentyone.steachserver.domain.lecture.repository.LectureRepository;
import com.twentyone.steachserver.domain.member.model.Teacher;
import com.twentyone.steachserver.domain.quiz.dto.QuizListRequestDto;
import com.twentyone.steachserver.domain.quiz.validator.QuizChoiceValidator;
import com.twentyone.steachserver.domain.quiz.validator.QuizValidator;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.quiz.dto.QuizRequestDto;
import com.twentyone.steachserver.domain.quiz.dto.QuizResponseDto;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.quiz.repository.QuizRepository;
import com.twentyone.steachserver.global.error.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class  QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepository;
    private final LectureRepository lectureRepository;

    private final QuizChoiceService quizChoiceService;

    private final QuizValidator quizValidator;
    private final QuizChoiceValidator quizChoiceValidator;

    @Override
    @Transactional
    public List<Quiz> createQuiz(Integer lectureId, QuizListRequestDto request) throws RuntimeException {
        Lecture lecture = getLecture(lectureId);
        List<Quiz> quizList = new ArrayList<>();

        for (QuizRequestDto quizRequestDto: request.quizList()) {
            Quiz quiz = Quiz.createQuiz(quizRequestDto, lecture);
            quiz = quizRepository.save(quiz);
            quizValidator.validateEmptyQuiz(quiz);

            // Create and save QuizChoice entities
            List<String> choices = quizRequestDto.choices();
            Integer answerIdx = quizRequestDto.answers() - 1; //user answer -> real index

            if (answerIdx >= choices.size() || answerIdx < 0) {
                throw new IllegalArgumentException("정답관련 인덱스가 유효하지 않습니다.");
            }

            quizChoiceService.createQuizChoices(choices, choices.get(answerIdx), quiz);
            quizList.add(quiz);
        }

        return quizList;
    }

    private Lecture getLecture(Integer lectureId) {
        Optional<Lecture> lectureOpt = lectureRepository.findById(lectureId);

        if (lectureOpt.isEmpty()) {
            throw new IllegalArgumentException("Lecture not found");
        }

        return lectureOpt.get();
    }

    @Override
    public Optional<Quiz> findById(Integer quizId) {
        return quizRepository.findById(quizId);
    }

    @Override
    public QuizResponseDto mapToDto(Quiz quiz) {
        List<String> choices = quizChoiceService.getChoices(quiz);
        String answers = quizChoiceService.getAnswers(quiz);

        int answerInt = 0;
        for (int i =0; i<choices.size(); i++) {
            if (choices.get(i).equals(answers)) {
                answerInt = i+1;
                break;
            }
        }
        quizChoiceValidator.validateQuizChoices(choices, answers);

        return QuizResponseDto.createQuizResponseDto(quiz, choices, answerInt);
    }

    @Override
    public List<Quiz> findAllByLectureId(Integer lectureId) {
        return quizRepository.findALlByLectureId(lectureId);
    }

    @Override
    @Transactional
    public void delete(Integer quizId, Teacher teacher) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("퀴즈를 찾을 수 없음"));

        //퀴즈를 만든 사람인지 확인
        if (!quiz.getLecture().getCurriculum().getTeacher().equals(teacher)) {
            throw new ForbiddenException("퀴즈를 만든 사람만 삭제가 가능합니다.");
        }

        quizRepository.delete(quiz);
    }
}
