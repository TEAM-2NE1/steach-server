package com.twentyone.steachserver.domain.quiz.service;

import com.twentyone.steachserver.domain.auth.error.ForbiddenException;
import com.twentyone.steachserver.domain.lecture.repository.LectureRepository;
import com.twentyone.steachserver.domain.member.model.Teacher;
import com.twentyone.steachserver.domain.quiz.dto.QuizListRequestDto;
import com.twentyone.steachserver.domain.quiz.dto.QuizListResponseDto;
import com.twentyone.steachserver.domain.quiz.model.QuizChoice;
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
    public QuizListResponseDto createQuizList(Integer lectureId, QuizListRequestDto request) throws RuntimeException {
        Lecture lecture = getLecture(lectureId);

        List<Quiz> quizList = new ArrayList<>();
        for (QuizRequestDto quizRequestDto: request.quizList()) {
            Quiz quiz = createQuiz(lecture, quizRequestDto);

            quizList.add(quiz);
        }

        return QuizListResponseDto.fromDomainList(quizList);
    }

    @Override
    @Transactional
    public Quiz createQuiz(Lecture lecture, QuizRequestDto quizRequestDto) {
        //퀴즈 생성
        Quiz quiz = Quiz.createQuiz(quizRequestDto, lecture);
        quiz = quizRepository.save(quiz);

        //실제 인덱스 = 클라이언트 인덱스 - 1
        Integer answerIdx = quizRequestDto.answers() - 1;

        List<String> choices = quizRequestDto.choices();

        //클라이언트 인덱스 유효성 검사
        if (answerIdx >= choices.size() || answerIdx < 0) {
            throw new IllegalArgumentException("정답관련 인덱스가 유효하지 않습니다.");
        }

        // Create and save QuizChoice entities
        List<QuizChoice> quizChoices = quizChoiceService.createQuizChoices(choices, answerIdx, quiz);
        quiz.addChoiceList(quizChoices);

        return quiz;
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
        List<QuizChoice> quizChoices = quiz.getQuizChoices();
        List<String> choices = quizChoiceService.getChoices(quiz);

        int answerInt = 1;
        for (QuizChoice quizChoice: quizChoices) {
            if (quizChoice.getIsAnswer()) {
                break;
            }
            answerInt++;
        }

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

    @Override
    @Transactional
    public QuizResponseDto modifyQuiz(Teacher teacher, Integer quizId, QuizRequestDto dto) {
        //dto 검증로직
        if (dto.answers() == null && dto.choices() != null) {
            throw new IllegalArgumentException("quiz choices에 수정사항이 있다면 answer는 null이어서는 안됩니다.");
        }

        Quiz quiz = quizRepository.findByIdWithQuizChoice(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("퀴즈를 찾을 수 없음"));

        //퀴즈를 만든 사람만 수정가능
        Teacher makeTeacher = quiz.getLecture().getCurriculum().getTeacher();
        if (!teacher.equals(makeTeacher)) {
            throw new ForbiddenException("퀴즈를 수정할 권한이 없습니다");
        }

        //answer만 바뀌는 경우
        if (dto.choices() == null && dto.answers() != null) {
            List<QuizChoice> quizChoices = quiz.getQuizChoices();
            int answerIndex = dto.answers() - 1; //TODO 이건 어떻게 처리하지? 실수 발생할 것 같다..

            if (answerIndex >= quiz.getQuizChoices().size() || answerIndex < 0) {
                throw new IllegalArgumentException("정답관련 인덱스가 유효하지 않습니다.");
            }

            for (int i =0; i<quizChoices.size(); i++) {
                if (answerIndex == i) {
                    quizChoices.get(i).setIsAnswer();
                } else {
                    quizChoices.get(i).setIsNotAnswer();
                }
            }
        }

        //choice + answer 둘 다 바뀌는 경우
        if (dto.choices() != null && dto.answers() != null) {
            List<QuizChoice> quizChoices = quiz.getQuizChoices();

            //TODO choice 도 몇 개 없는데, 그냥 삭제하는 게 나을까? 고민해보기..
            quiz.deleteAllQuizChoice();
            for (QuizChoice qc: quizChoices) {
                quizChoiceService.deleteChoice(qc);
            }

            int answerIndex = dto.answers() - 1; //TODO 이건 어떻게 처리하지? 실수 발생할 것 같다..
            //클라이언트 인덱스 유효성 검사
            if (answerIndex >= dto.choices().size() || answerIndex < 0) {
                throw new IllegalArgumentException("정답관련 인덱스가 유효하지 않습니다.");
            }

            List<String> inputChoices = dto.choices();
            quizChoiceService.createQuizChoices(inputChoices, answerIndex, quiz);

            quiz.modify(dto.quizNumber(), dto.question());
        }

        return mapToDto(quiz);
    }
}
