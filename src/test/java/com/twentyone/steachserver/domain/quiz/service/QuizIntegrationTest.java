package com.twentyone.steachserver.domain.quiz.service;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
public class QuizIntegrationTest {
    public static final String CHOICE1 = "asdf";
    public static final String CHOICE2 = "qwer";
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
                .lectureStartTime(LocalDateTime.now())
                .lectureCloseTime(LocalDateTime.now())
                .maxAttendees(4)
                .build();
        curriculumDetailRepository.save(curriculumDetail);

        curriculum = Curriculum.of("title", CurriculumCategory.EDUCATION, teacher, curriculumDetail);
        curriculumRepository.save(curriculum);
    }

    @Test
    void 퀴즈생성() throws Exception {
        Lecture createdLecture = lectureRepository.save(Lecture.of("title", 1, LocalDateTime.now(), curriculum));
        int quizNumber = 1;
        String question = "asdf";
        List<String> choices = List.of(new String[]{CHOICE1, CHOICE2});
        List<String> answers = List.of(new String[]{CHOICE1});

        Quiz quiz = quizService.createQuiz(createdLecture.getId(), new QuizRequestDto(quizNumber, question, choices, answers))
                .orElseThrow(() -> new RuntimeException("에러남"));

        assertEquals(quiz.getQuestion(), question);
        assertEquals(quiz.getQuizNumber(), quizNumber);
        for (int i =0; i<quiz.getQuizChoices().size(); i++) {
            assertEquals(quiz.getQuizChoices().get(i).getChoiceSentence(), choices.get(i));
        }

        assertEquals(quiz.getQuestion(), question);
    }
}
