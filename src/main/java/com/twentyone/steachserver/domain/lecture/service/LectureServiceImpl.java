package com.twentyone.steachserver.domain.lecture.service;

import com.twentyone.steachserver.domain.curriculum.dto.SimpleCurriculumByLectureDto;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.curriculum.repository.CurriculumDetailRepository;
import com.twentyone.steachserver.domain.curriculum.repository.CurriculumRepository;
import com.twentyone.steachserver.domain.lecture.dto.*;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.repository.LectureQueryRepository;
import com.twentyone.steachserver.domain.lecture.repository.LectureRepository;

import com.twentyone.steachserver.domain.studentLecture.dto.StudentLectureStatisticDto;
import com.twentyone.steachserver.domain.studentLecture.model.StudentLecture;
import com.twentyone.steachserver.domain.studentLecture.repository.StudentLectureMongoRepository;
import com.twentyone.steachserver.domain.studentLecture.repository.StudentLectureQueryRepository;
import com.twentyone.steachserver.domain.studentLecture.repository.StudentLectureRepository;
import com.twentyone.steachserver.domain.studentLecture.service.StudentLectureService;
import com.twentyone.steachserver.domain.studentLecture.service.StudentLectureServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final LectureQueryRepository lectureQueryRepository;

    private final CurriculumRepository curriculumRepository;
    private final CurriculumDetailRepository curriculumDetailRepository;

    private final StudentLectureMongoRepository studentLectureMongoRepository;
    private final StudentLectureRepository studentLectureRepository;
    private final StudentLectureQueryRepository studentLectureQueryRepository;
    private final StudentLectureService studentLectureService;


    public Optional<Lecture> findLectureById(Integer id) {
        return lectureRepository.findById(id);
    }

    @Override
    public List<Lecture> upcomingLecture(int toMinute, int fromMinute) {
        LocalDateTime toTime = LocalDateTime.now().plusMinutes(toMinute);
        LocalDateTime fromTime = LocalDateTime.now().plusMinutes(toMinute);

        return lectureRepository.findByLectureStartTimeBetween(fromTime, toTime);
    }


    // 이 부분 해결해야함!!!!!!!!!
    @Override
    public LectureBeforeStartingResponseDto getLectureInformation(Integer lectureId) {
        Optional<Lecture> lectureOpt = findLectureById(lectureId);
        if (lectureOpt.isEmpty()){
            throw new IllegalArgumentException("not found Lecture");
        }
        Lecture lecture = lectureOpt.get();
        LectureBeforeStartingResponseDto lectureBeforeStartingResponse = studentLectureQueryRepository.getLectureBeforeStartingResponse(lectureId);

        if(lecture.getRealEndTime() == null){
            return lectureBeforeStartingResponse;
        }

        List<StudentInfoByLectureDto> studentInfoByLecture = studentLectureQueryRepository.getStudentInfoByLecture(lectureId);

        CompletedLecturesResponseDto completedLecturesResponseDto = CompletedLecturesResponseDto.of(lectureBeforeStartingResponse, studentInfoByLecture, lecture);
        completedLecturesResponseDto.completeLecture();

        return completedLecturesResponseDto;

//        Optional<Lecture> lectureOptional = lectureRepository.findById(lectureId);
//        if (lectureOptional.isEmpty()) {
//            throw new IllegalStateException("lecture not found");
//        }
//        Lecture lecture = lectureOptional.get();
//        // 시작 전
//
//        // 커리큘럼
//        Optional<Curriculum> CurriculumOpt = curriculumRepository.findByLecturesContaining(lecture);
//        if (CurriculumOpt.isEmpty()) {
//            throw new IllegalStateException("curriculum not found");
//        }
//        Curriculum curriculum = CurriculumOpt.get();
//        SimpleCurriculumByLectureDto simpleCurriculumByLectureDto = SimpleCurriculumByLectureDto.createSimpleCurriculumByLectureDto(curriculum);
//
//        // 커리큘럼 디테일
//        Optional<CurriculumDetail> curriculumDetailOpt = curriculumDetailRepository.findById(curriculum.getId());
//        if (curriculumDetailOpt.isEmpty()) {
//            throw new IllegalStateException("curriculum detail not found");
//        }
//        CurriculumDetail curriculumDetail = curriculumDetailOpt.get();
//        CurriculumDetailByLectureDto curriculumDetailByLectureDto = CurriculumDetailByLectureDto.createCurriculumDetailByLectureDto(curriculumDetail);
//
//        // 학생커리큘림
//        List<StudentCurriculum> studentCurricula = curriculum.getStudentCurricula();
//
//        List<StudentByLectureDto> studentByLectureDtos = new ArrayList<>();
//        for (StudentCurriculum studentCurriculum : studentCurricula) {
//            StudentByLectureDto studentByLectureDto = StudentByLectureDto.createStudentByLectureDto(studentCurriculum.getStudent());
//            studentByLectureDtos.add(studentByLectureDto);
//        }
//
//        LectureBeforeStartingResponseDto lectureBeforeStartingResponseDto = LectureBeforeStartingResponseDto.of(lecture, simpleCurriculumByLectureDto, curriculumDetailByLectureDto, studentByLectureDtos);
//
//        // 아직 안 마쳤다면
//        if (lecture.getRealEndTime() == null) {
//            return lectureBeforeStartingResponseDto;
//        }
//
////        List<Student> students = lecture.g
//
//        List<Quiz> quizzes = lecture.getQuizzes();
//        for (Quiz quiz : quizzes) {
//            Set<StudentQuiz> studentQuizzes = quiz.getStudentQuizzes();
//        }
//        List<StudentQuizDto> studentQuizDtos = new ArrayList<>();
//
//        List<StudentLecture> studentLectures = lecture.getStudentLectures();
//        for (StudentLecture studentLecture : studentLectures) {
//            String name = studentLecture.getStudent().getName();
//            StudentQuizDto.createStudentQuizDto(studentQuiz, name);
//        }
//        StudentInfoByLectureDto.of(studentQuizDtos, studentLecture);
//
//
//        List<StudentInfoByLectureDto> studentsQuizzesByLectureDto = new ArrayList<>();
//
//        CompletedLecturesResponseDto.of(lectureBeforeStartingResponseDto, studentsQuizzesByLectureDto,lecture);
//
//        return lectureBeforeStartingResponseDto;
//
//        // 완료 된 수업
//        return lectureQueryRepository.findLectureDetailsByLectureId(lectureId);
    }

    @Override
    public LectureBeforeStartingResponseDto updateLectureInformation(Integer lectureId, UpdateLectureRequestDto lectureRequestDto) {
        Optional<Lecture> lectureById = findLectureById(lectureId);
        if (lectureById.isPresent()) {
            Lecture lecture = lectureById.get();
            lecture.update(lectureRequestDto);
            return lectureQueryRepository.findLectureDetailsByLectureId(lectureId);
        }
        return null;
    }

    @Override
    public void updateRealEndTime(Integer lectureId) {
        Optional<Lecture> lectureById = findLectureById(lectureId);
        if (lectureById.isPresent()) {
            Lecture lecture = lectureById.get();
            lecture.updateRealEndTimeWithNow();
        }
    }

    @Override
    public void createGPTData(Integer lectureId, StudentLectureStatisticDto lectureStudentStatistic) {
        Lecture lecture = lectureRepository.getReferenceById(lectureId);
        Optional<Curriculum> curriculumOpt = curriculumRepository.findByLecturesContaining(lecture);

        if (curriculumOpt.isEmpty()) {
            throw new IllegalStateException("curriculum not found");
        }
        Curriculum curriculum = curriculumOpt.get();

        GPTDataByLectureDto gptDataByLectureDto = GPTDataByLectureDto.of(curriculum, lecture, lectureStudentStatistic);
        studentLectureMongoRepository.save(gptDataByLectureDto);
    }

    @Override
    public FinalLectureInfoByTeacherDto getFinalLectureInformation(Integer lectureId){
        return lectureQueryRepository.getFinalLectureInfoByTeacher(lectureId);
    }
}

