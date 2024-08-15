package com.twentyone.steachserver.domain.studentCurriculum.repository;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.studentCurriculum.model.StudentCurriculum;
import com.twentyone.steachserver.domain.studentCurriculum.model.StudentCurriculumId;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentCurriculumRepository extends JpaRepository<StudentCurriculum, StudentCurriculumId> {
    @Query("select sc from StudentCurriculum sc left join sc.curriculum where sc.student = :student")
    Page<StudentCurriculum> findByStudent(@Param("student") Student student, Pageable pageable);

    @Query(value = "select * from students_curricula sc left join curricula c on sc.curriculum_id = c.id where sc.student_id = :studentId", nativeQuery = true)
    List<StudentCurriculum> findByStudent(@Param("studentId") Integer studentId);

    Optional<StudentCurriculum> findTop1ByStudentAndCurriculum(Student student, Curriculum curriculum);

    List<StudentCurriculum> findAllByCurriculumId(Integer curriculumId);

    @Transactional
    default void deleteByStudentAndCurriculumWithException(Student student, Curriculum curriculum) {
        long deletedCount = this.deleteByStudentAndCurriculum(student, curriculum);
        if (deletedCount == 0) {
            throw new IllegalArgumentException("해당 커리큘럼에 대해서 수강 신청 내역이 존재하지 않았습니다.");
        }
    }

    long deleteByStudentAndCurriculum(Student student, Curriculum referenceById);
}
