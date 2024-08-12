package com.twentyone.steachserver;

import com.twentyone.steachserver.domain.auth.service.AuthService;
import com.twentyone.steachserver.domain.classroom.dto.FocusTimeRequestDto;
import com.twentyone.steachserver.domain.classroom.service.ClassroomService;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumAddRequest;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculumDetailResponse;
import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import com.twentyone.steachserver.domain.curriculum.service.CurriculumService;
import com.twentyone.steachserver.domain.gpt.service.GPTService;
import com.twentyone.steachserver.domain.lecture.dto.LectureResponseDto;
import com.twentyone.steachserver.domain.lecture.dto.AllLecturesInCurriculaResponseDto;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.service.LectureService;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.model.Teacher;
import com.twentyone.steachserver.domain.member.repository.StudentRepository;
import com.twentyone.steachserver.domain.member.repository.TeacherRepository;
import com.twentyone.steachserver.domain.member.service.StudentService;
import com.twentyone.steachserver.domain.member.service.TeacherService;
import com.twentyone.steachserver.domain.quiz.dto.QuizListRequestDto;
import com.twentyone.steachserver.domain.quiz.dto.QuizListResponseDto;
import com.twentyone.steachserver.domain.quiz.dto.QuizRequestDto;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.quiz.service.QuizService;
import com.twentyone.steachserver.domain.statistic.service.StatisticService;
import com.twentyone.steachserver.domain.studentLecture.service.StudentLectureService;
import com.twentyone.steachserver.domain.studentQuiz.dto.StudentQuizRequestDto;
import com.twentyone.steachserver.domain.studentQuiz.service.StudentQuizService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Transactional
@SpringBootTest
@DisplayName("관통 테스트 (서비스) 스크립트 1")
public class TestScenario1 {
    @Autowired
    AuthService authService;

    @Autowired
    TeacherService teacherService;

    @Autowired
    StudentService studentService;

    @Autowired
    CurriculumService curriculumService;

    @Autowired
    StudentLectureService studentLectureService;

    @Autowired
    LectureService lectureService;

    @Autowired
    QuizService quizService;

    @Autowired
    StatisticService statisticService;

    @Autowired
    GPTService gptService;

    @Autowired
    StudentQuizService studentQuizService;

    @Autowired
    ClassroomService classroomService;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    StudentRepository studentRepository;

    /* StudentLectureQueryRepository의 updateStudentLectureByFinishLecture에 가서 lectureDurationMinutes = 60; 주석을 풀어야 제대로 작동함 */
    @Test
    public void scenario1() throws Exception {
        /* FIXME 1. 선생님 회원가입 */
        Teacher teacher = Teacher.of("teacher11", "teacher11", "teacher11", "teacher11", "teacher11");
        teacher = teacherRepository.save(teacher);

        /* FIXME 2. 학생 회원가입 1 */
        Student student1 = Student.of("student11", "student11", "student11", "student11");
        student1 = studentRepository.save(student1);

        /* FIXME 3. 학생 회원가입 2 */
        Student student2 = Student.of("student22", "student22", "student22", "student22");
        student2 = studentRepository.save(student2);

        /* FIXME 4. 학생 회원가입 3 */
        Student student3 = Student.of("student33", "student33", "student33", "student33");
        student3 = studentRepository.save(student3);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(teacher, teacher.getPassword(), teacher.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        authentication = new UsernamePasswordAuthenticationToken(student1, student1.getPassword(), student1.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        authentication = new UsernamePasswordAuthenticationToken(student2, student2.getPassword(), student2.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        authentication = new UsernamePasswordAuthenticationToken(student3, student3.getPassword(), student3.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        /* FIXME 5. 커리큘럼 생성 */
        CurriculumAddRequest curriculumAddRequest = CurriculumAddRequest.builder()
                .title("주효림의 자바강의")
                .subCategory("자바 정복하자!")
                .intro("저 자바 잘합니다. 저한테 배워가세요")
                .information("매주 월화 7시부터 9시까지 강의합니다.")
                .category(CurriculumCategory.getCategoryByIndex(0))
                .subCategory("프로그래밍")
                .bannerImgUrl("http:~~")
                .startDate(LocalDate.of(2024, 8, 30))
                .endDate(LocalDate.of(2024, 9, 25))
                .weekdaysBitmask("1100000")
                .lectureStartTime(LocalTime.of(19, 0))
                .lectureEndTime(LocalTime.of(21, 0))
                .maxAttendees(4)
                .build();
        CurriculumDetailResponse curriculumDetailResponse = curriculumService.create(teacher, curriculumAddRequest);

        Integer curriculumId = curriculumDetailResponse.getCurriculumId();

        /* FIXME 6. 강의 리스트 받기 */
        AllLecturesInCurriculaResponseDto allLecturesInCurriculaResponseDto = lectureService.findByCurriculum(curriculumId);
        Map<Integer, List<LectureResponseDto>> lecturesMap = allLecturesInCurriculaResponseDto.getLectures();

        List<Integer> lectures = new ArrayList<>();
        for (Integer key: lecturesMap.keySet()) {
            for (LectureResponseDto lectureResponseDto : lecturesMap.get(key)) {
                lectures.add(lectureResponseDto.getLectureId());
            }
        }

        /* FIXME 7. 커리큘럼 수강신청 */
        curriculumService.registration(student1, curriculumId);
        curriculumService.registration(student2, curriculumId);
        curriculumService.registration(student3, curriculumId);

        /* FIXME 8. 강의시작 1 =========================================================================================== */
        /* FIXME 8-1 클래스룸 들어가기 */
        Integer lectureId1 = lectures.get(0);
        classroomService.createClassroom(lectureId1);

        /* FIXME 8-2. 강의 진짜 시작 */
        lectureService.updateRealStartTime(lectureId1);

        /* FIXME 8-3. 퀴즈만들기, 퀴즈풀기 */
        QuizRequestDto quizRequestDto1 = new QuizRequestDto(1, "자바는 객체지향 언어이다", List.of("O", "X"), 1, 1);
        QuizListRequestDto quizListRequestDto = new QuizListRequestDto(List.of(quizRequestDto1));
        QuizListResponseDto quiz1 = quizService.createQuizList(lectureId1, quizListRequestDto);
        Integer quizId1 = quiz1.quizList().get(0).getQuizId();

        student1 = studentRepository.findById(student1.getId()).get();
        StudentQuizRequestDto studentQuizRequestDto1 = new StudentQuizRequestDto(90, "O");
        studentQuizService.createStudentQuiz(student1, quizId1, studentQuizRequestDto1);

        student2 = studentRepository.findById(student2.getId()).get();
        StudentQuizRequestDto studentQuizRequestDto2 = new StudentQuizRequestDto(80, "O");
        studentQuizService.createStudentQuiz(student2, quizId1, studentQuizRequestDto2);

        student3 = studentRepository.findById(student3.getId()).get();
        StudentQuizRequestDto studentQuizRequestDto3 = new StudentQuizRequestDto(0, "X");
        studentQuizService.createStudentQuiz(student3, quizId1, studentQuizRequestDto3);

        // FIXME ========================
        QuizRequestDto quizRequestDto2 = new QuizRequestDto(2, "자바 컬렉션이 아닌것은?", List.of("List", "Set", "Vector", "HashMap"), 3, 1);
//        Quiz quiz2 = quizService.createQuiz(lectureId1, quizRequestDto2).get();
        quizListRequestDto = new QuizListRequestDto(List.of(quizRequestDto2));
        QuizListResponseDto quiz2 = quizService.createQuizList(lectureId1, quizListRequestDto);
        Integer quizId2 = quiz2.quizList().get(0).getQuizId();

        StudentQuizRequestDto studentQuizRequestDto4 = new StudentQuizRequestDto(90, "Vector");
        studentQuizService.createStudentQuiz(student1, quizId2, studentQuizRequestDto4);

        StudentQuizRequestDto studentQuizRequestDto5 = new StudentQuizRequestDto(100, "Vector");
        studentQuizService.createStudentQuiz(student2, quizId2, studentQuizRequestDto5);

        StudentQuizRequestDto studentQuizRequestDto6 = new StudentQuizRequestDto(0, "HashMap");
        studentQuizService.createStudentQuiz(student3, quizId2, studentQuizRequestDto6);

        // FIXME ========================
        QuizRequestDto quizRequestDto3 = new QuizRequestDto(2, "자바는 플랫폼 종속적이다?", List.of("O", "X"), 1, 1);
//        Quiz quiz3 = quizService.createQuiz(lectureId1, quizRequestDto3).get();
        quizListRequestDto = new QuizListRequestDto(List.of(quizRequestDto3));
        QuizListResponseDto quiz3 = quizService.createQuizList(lectureId1, quizListRequestDto);
        Integer quizId3 = quiz3.quizList().get(0).getQuizId();

        StudentQuizRequestDto studentQuizRequestDto7 = new StudentQuizRequestDto(90, "O");
        studentQuizService.createStudentQuiz(student1, quizId3, studentQuizRequestDto7);

        StudentQuizRequestDto studentQuizRequestDto8 = new StudentQuizRequestDto(100, "O");
        studentQuizService.createStudentQuiz(student2, quizId3, studentQuizRequestDto8);

        StudentQuizRequestDto studentQuizRequestDto9 = new StudentQuizRequestDto(0, "X");
        studentQuizService.createStudentQuiz(student3, quizId3, studentQuizRequestDto9);

        FocusTimeRequestDto focusTimeRequestDto = new FocusTimeRequestDto(10);
        studentLectureService.saveTimeFocusTime(student1.getId(), lectureId1, 50);
        studentLectureService.saveTimeFocusTime(student2.getId(), lectureId1, 40);
        studentLectureService.saveTimeFocusTime(student3.getId(), lectureId1, 30);

        /* FIXME 8-4. 강의 종료 */
        Lecture updateLecture = lectureService.updateRealEndTime(lectureId1);
        lectureService.addVolunteerMinute(updateLecture);
        studentLectureService.updateStudentLectureByFinishLecture(lectureId1);
        statisticService.createStatisticsByFinalLecture(updateLecture);
        lectureService.getFinalLectureInformation(lectureId1);

        /* FIXME 9. 강의시작 2 =========================================================================================== */
        /* FIXME 9-1 클래스룸 들어가기 */
        lectureId1 = lectures.get(1);
        classroomService.createClassroom(lectureId1);

        /* FIXME 9-2. 강의 진짜 시작 */
        lectureService.updateRealStartTime(lectureId1);

        /* FIXME 9-3. 퀴즈만들기, 퀴즈풀기 */
        quizRequestDto1 = new QuizRequestDto(1, "객체지향 5대원칙이 아닌것은?", List.of("OCP", "LSP", "RIP", "DIP"), 4, 1);
//        quiz1 = quizService.createQuiz(lectureId1, quizRequestDto1).get();
        quizListRequestDto = new QuizListRequestDto(List.of(quizRequestDto1));
        quiz1 = quizService.createQuizList(lectureId1, quizListRequestDto);
        quizId1 = quiz1.quizList().get(0).getQuizId();

        studentQuizRequestDto1 = new StudentQuizRequestDto(90, "RIP");
        studentQuizService.createStudentQuiz(student1, quizId1, studentQuizRequestDto1);

        studentQuizRequestDto2 = new StudentQuizRequestDto(80, "RIP");
        studentQuizService.createStudentQuiz(student2, quizId1, studentQuizRequestDto2);

        studentQuizRequestDto3 = new StudentQuizRequestDto(100, "RIP");
        studentQuizService.createStudentQuiz(student3, quizId1, studentQuizRequestDto3);

        // FIXME ========================
        quizRequestDto2 = new QuizRequestDto(2, "자바에서 MinHeap을 구현하는 방법은?", List.of("Heap", "PriorityQueue"), 2, 1);
//        quiz2 = quizService.createQuiz(lectureId1, quizRequestDto2).get();
        quizListRequestDto = new QuizListRequestDto(List.of(quizRequestDto2));
        quiz2 = quizService.createQuizList(lectureId1, quizListRequestDto);
        quizId2 = quiz2.quizList().get(0).getQuizId();

        studentQuizRequestDto4 = new StudentQuizRequestDto(90, "PriorityQueue");
        studentQuizService.createStudentQuiz(student1, quizId2, studentQuizRequestDto4);

        studentQuizRequestDto5 = new StudentQuizRequestDto(0, "Heap");
        studentQuizService.createStudentQuiz(student2, quizId2, studentQuizRequestDto5);

        studentQuizRequestDto6 = new StudentQuizRequestDto(0, "Heap");
        studentQuizService.createStudentQuiz(student3, quizId2, studentQuizRequestDto6);

        // FIXME ========================
        quizRequestDto3 = new QuizRequestDto(2, "클래스의 가장 최고조상은?", List.of("Object", "ANCESTOR"), 1, 1);
//        quiz3 = quizService.createQuiz(lectureId1, quizRequestDto3).get();
        quizListRequestDto = new QuizListRequestDto(List.of(quizRequestDto3));
        quiz3 = quizService.createQuizList(lectureId1, quizListRequestDto);
        quizId3 = quiz3.quizList().get(0).getQuizId();

        studentQuizRequestDto7 = new StudentQuizRequestDto(90, "Object");
        studentQuizService.createStudentQuiz(student1, quizId3, studentQuizRequestDto7);

        studentQuizRequestDto8 = new StudentQuizRequestDto(80, "Object");
        studentQuizService.createStudentQuiz(student2, quizId3, studentQuizRequestDto8);

        studentQuizRequestDto9 = new StudentQuizRequestDto(100, "Object");
        studentQuizService.createStudentQuiz(student3, quizId3, studentQuizRequestDto9);

        studentLectureService.saveTimeFocusTime(student1.getId(), lectureId1, 10);
        studentLectureService.saveTimeFocusTime(student2.getId(), lectureId1, 20);
        studentLectureService.saveTimeFocusTime(student3.getId(), lectureId1, 30);

        /* FIXME 9-4. 강의 종료 */
        updateLecture = lectureService.updateRealEndTime(lectureId1);
        lectureService.addVolunteerMinute(updateLecture);
        studentLectureService.updateStudentLectureByFinishLecture(lectureId1);
        statisticService.createStatisticsByFinalLecture(updateLecture);
        lectureService.getFinalLectureInformation(lectureId1);

        log.info(statisticService.createGPTString(student1));
        log.info(statisticService.createGPTString(student2));
        log.info(statisticService.createGPTString(student3));

        log.info(gptService.getChatGPTResponse(statisticService.createGPTString(student1)));
    }
}
