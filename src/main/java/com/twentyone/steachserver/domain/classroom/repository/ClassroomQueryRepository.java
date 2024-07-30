package com.twentyone.steachserver.domain.classroom.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twentyone.steachserver.domain.classroom.model.Classroom;
import com.twentyone.steachserver.domain.classroom.model.QClassroom;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.studentCurriculum.model.QStudentCurriculum;
import com.twentyone.steachserver.domain.studentCurriculum.model.StudentCurriculum;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.twentyone.steachserver.domain.classroom.model.QClassroom.classroom;
import static com.twentyone.steachserver.domain.studentCurriculum.model.QStudentCurriculum.studentCurriculum;

@Repository
public class ClassroomQueryRepository {

    private final JPAQueryFactory query;

    public ClassroomQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public Optional<Classroom> findClassroomBySessionIdAndStudent(Integer studentId, String sessionId) {
        QClassroom qClassroom = classroom;
        QStudentCurriculum qStudentCurriculum = studentCurriculum;

        Classroom classroom = query.selectFrom(qClassroom)
                .where(qClassroom.sessionId.eq(sessionId))
                .fetchOne();
        if (classroom == null) {
            throw new RuntimeException("Classroom not found");
        }

        // Curriculum을 가져옴
        Curriculum curriculum = classroom.getLecture().getCurriculum();

        // StudentCurriculum 목록을 가져옴
        List<StudentCurriculum> studentCurricula = query.selectFrom(qStudentCurriculum)
                .where(qStudentCurriculum.curriculum.eq(curriculum))
                .fetch();

        for (StudentCurriculum studentCurriculum : studentCurricula) {
            Student student = studentCurriculum.getStudent();
            Integer id = student.getId();
            if (Objects.equals(id, studentId)) {
                return Optional.of(classroom);
            }
        }

        return Optional.empty();
    }
}
