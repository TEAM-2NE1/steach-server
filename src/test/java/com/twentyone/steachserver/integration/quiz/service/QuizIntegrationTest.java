package com.twentyone.steachserver.integration.quiz.service;

import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.curriculum.model.CurriculumDetail;
import com.twentyone.steachserver.domain.curriculum.repository.CurriculumDetailRepository;
import com.twentyone.steachserver.domain.curriculum.repository.CurriculumRepository;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import com.twentyone.steachserver.domain.lecture.repository.LectureRepository;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.model.Teacher;
import com.twentyone.steachserver.domain.member.repository.StudentRepository;
import com.twentyone.steachserver.domain.member.repository.TeacherRepository;
import com.twentyone.steachserver.domain.quiz.dto.QuizListRequestDto;
import com.twentyone.steachserver.domain.quiz.dto.QuizListResponseDto;
import com.twentyone.steachserver.domain.quiz.dto.QuizRequestDto;
import com.twentyone.steachserver.domain.quiz.dto.QuizResponseDto;
import com.twentyone.steachserver.domain.quiz.model.Quiz;
import com.twentyone.steachserver.domain.quiz.model.QuizChoice;
import java.time.LocalTime;

import com.twentyone.steachserver.domain.quiz.service.QuizService;
import com.twentyone.steachserver.integration.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("퀴즈 통합 테스트")
public class QuizIntegrationTest extends IntegrationTest {
    public static final String CHOICE1 = "asdf";
    public static final String CHOICE2 = "qwer";
    public static final int QUIZ_NUMBER = 1;

    @Autowired
    QuizService quizService;
    @Autowired
    LectureRepository lectureRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    private CurriculumDetailRepository curriculumDetailRepository;
    @Autowired
    private CurriculumRepository curriculumRepository;

    private Teacher teacher;
    private Student student;
    private Curriculum curriculum;
    private CurriculumDetail curriculumDetail;

    @BeforeEach
    void setUp() throws IOException {
        teacher = Teacher.of("teacher2", "password1", "김범식1", "bumsik1@gmail.com", "imagePath");
        student = Student.of("student2", "password2", "주효림1", "mylime1@gmail.com");

        teacherRepository.save(teacher);
        studentRepository.save(student);

        curriculumDetail = CurriculumDetail.builder()
                .subTitle("title")
                .intro("subTitle")
                .subCategory("intro")
                .information("information")
                .bannerImgUrl("bannerImgUrl")
                .weekdaysBitmask((byte) 7)
                .startDate(LocalDate.from(LocalDate.now()))
                .endDate(LocalDate.from(LocalDate.now()))
                .lectureStartTime(LocalTime.now())
                .lectureCloseTime(LocalTime.now())
                .maxAttendees(4)
                .build();
        curriculumDetailRepository.save(curriculumDetail);

        curriculum = Curriculum.of("title", CurriculumCategory.getCategoryByIndex(0), teacher, curriculumDetail);
        curriculumRepository.save(curriculum);
    }

    @Test
    void 퀴즈생성() throws Exception {
        Lecture createdLecture = lectureRepository.save(Lecture.of("title", 1, LocalDateTime.now(), curriculum));
        String question = "asdf";
        List<String> choices = List.of(new String[]{CHOICE1, CHOICE2});
//        List<String> answers = List.of(new String[]{CHOICE1});
        Integer answer = 1; //CHOICE

        QuizRequestDto quizRequestDto = new QuizRequestDto(QUIZ_NUMBER, question, choices, answer);
        QuizListRequestDto quizListRequestDto = new QuizListRequestDto(List.of(quizRequestDto));
        QuizResponseDto quiz = quizService.createQuizList(createdLecture.getId(), quizListRequestDto).quizList().get(0);

        assertEquals(quiz.question(), question);
        assertEquals(quiz.quizNumber(), QUIZ_NUMBER);
        for (int i = 0; i < quiz.choices().size(); i++) {
            assertEquals(quiz.choices().get(i), choices.get(i));
        }

        assertEquals(quiz.question(), question);
    }

    @Test
    void 퀴즈조회() throws Exception {
        //given
        Lecture createdLecture = lectureRepository.save(Lecture.of("title", 1, LocalDateTime.now(), curriculum));
        String question = "asdf";


        List<String> choices = List.of(new String[]{CHOICE1, CHOICE2});
        Integer answer = 1; //CHOICE1

        List<QuizRequestDto> list = List.of(new QuizRequestDto(QUIZ_NUMBER, question, choices, answer));

        QuizListResponseDto quizListResponse = quizService.createQuizList(createdLecture.getId(), new QuizListRequestDto(list));

        Quiz quiz = quizService.findById(quizListResponse.quizList().get(0).quizId())
                .orElseThrow(() -> new RuntimeException("asdf"));

        assertEquals(quiz.getQuizNumber(), QUIZ_NUMBER);
        assertEquals(quiz.getQuestion(), question);
        assertEquals(choices.size(), quiz.getQuizChoices().size());

        for (int i = 0; i < choices.size(); i++) {
            QuizChoice quizChoice = quiz.getQuizChoices().get(i);

            //정답 문장이 잘 들어가는지 확인
            assertEquals(choices.get(i), quizChoice.getChoiceSentence());

            //answer 여부가 잘 저장되는지 확인
            if (i+1 == answer) {
                assertTrue(quizChoice.getIsAnswer());
            } else {
                assertFalse(quizChoice.getIsAnswer());
            }
        }
    }
}
