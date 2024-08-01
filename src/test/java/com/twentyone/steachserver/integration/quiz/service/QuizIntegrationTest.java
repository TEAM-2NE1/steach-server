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
import com.twentyone.steachserver.domain.quiz.dto.QuizRequestDto;
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

        curriculum = Curriculum.of("title", CurriculumCategory.EDUCATION, teacher, curriculumDetail);
        curriculumRepository.save(curriculum);
    }

    @Test
    void 퀴즈생성() throws Exception {
        Lecture createdLecture = lectureRepository.save(Lecture.of("title", 1, LocalDateTime.now(), curriculum));
        String question = "asdf";
        List<String> choices = List.of(new String[]{CHOICE1, CHOICE2});
        List<String> answers = List.of(new String[]{CHOICE1});

        Quiz quiz = quizService.createQuiz(createdLecture.getId(), new QuizRequestDto(QUIZ_NUMBER, question, choices, answers))
                .orElseThrow(() -> new RuntimeException("에러남"));

        assertEquals(quiz.getQuestion(), question);
        assertEquals(quiz.getQuizNumber(), QUIZ_NUMBER);
        for (int i = 0; i < quiz.getQuizChoices().size(); i++) {
            assertEquals(quiz.getQuizChoices().get(i).getChoiceSentence(), choices.get(i));
        }

        assertEquals(quiz.getQuestion(), question);
    }

    @Test
    void 퀴즈조회() throws Exception {
        //given
        Lecture createdLecture = lectureRepository.save(Lecture.of("title", 1, LocalDateTime.now(), curriculum));
        String question = "asdf";
        List<String> choices = List.of(new String[]{CHOICE1, CHOICE2});
        List<String> answers = List.of(new String[]{CHOICE1});

        Quiz quiz1 = quizService.createQuiz(createdLecture.getId(), new QuizRequestDto(QUIZ_NUMBER, question, choices, answers))
                .orElseThrow(() -> new RuntimeException("에러남"));

        Quiz quiz = quizService.findById(quiz1.getId())
                .orElseThrow(() -> new RuntimeException("asdf"));

        assertEquals(quiz.getQuizNumber(), QUIZ_NUMBER);
        assertEquals(quiz.getQuestion(), question);
        assertEquals(choices.size(), quiz.getQuizChoices().size());

        for (int i = 0; i < choices.size(); i++) {
            QuizChoice quizChoice = quiz.getQuizChoices().get(i);

            //정답 문장이 잘 들어가는지 확인
            assertEquals(choices.get(i), quizChoice.getChoiceSentence());

            //answer 여부가 잘 저장되는지 확인
            if (answers.contains(choices.get(i))) {
                assertTrue(quizChoice.getIsAnswer());
            } else {
                assertFalse(quizChoice.getIsAnswer());
            }
        }
    }
}
