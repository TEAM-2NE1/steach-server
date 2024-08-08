package com.twentyone.steachserver.domain.curriculum.service;

import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.curriculum.dto.*;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.model.Teacher;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface CurriculumService {
    CurriculumDetailResponse getDetail(Integer id);

    CurriculumDetailResponse create(LoginCredential credential, CurriculumAddRequest request);

    void registration(LoginCredential credential, Integer curriculaId);

    @Transactional(readOnly = true)
    CurriculumListResponse getTeachersCurricula(Teacher teacher, Pageable pageable);
    CurriculumListResponse getTeachersCurricula(Teacher teacher);
    CurriculumListResponse getTeachersCurricula(Integer teacherId, Pageable pageable);
    CurriculumListResponse getTeachersCurricula(Integer teacherId);

    @Transactional(readOnly = true)
    CurriculumListResponse getStudentsCurricula(Student student, Pageable pageable);
    CurriculumListResponse getStudentsCurricula(Student student);

    CurriculumListResponse search(CurriculaSearchCondition condition, Pageable pageable);

    CurriculumListResponse search(CurriculaSearchCondition condition);

    List<LocalDateTime> getSelectedWeekdays(LocalDateTime startDate, LocalDateTime endDate,
                                            int weekdaysBitmask);

    CurriculumDetailResponse updateCurriculum(Integer curriculumId, Teacher teacher, CurriculumAddRequest request);

    void deleteCurriculum(Teacher teacher, Integer curriculumId);

    List<CurriculumDetailResponse> getPopularRatioCurriculums();

    List<CurriculumDetailResponse> getLatestCurriculums();

    Boolean getIsApplyForCurriculum(Student student, Integer curriculumId);

    @Transactional(readOnly = true)
    CurriculumIncludesStudentListResponseDto getTeachersCurriculaIncludesStudents(Teacher teacher, Pageable pageable);
    CurriculumIncludesStudentListResponseDto getTeachersCurriculaIncludesStudents(Teacher teacher);

    void cancel(Student student, Integer curriculaId);

}
