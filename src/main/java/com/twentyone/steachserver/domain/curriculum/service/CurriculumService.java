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

    @Transactional(readOnly = true)
    CurriculumListResponse getStudentsCurricula(Student student, Pageable pageable);

    CurriculumListResponse search(CurriculaSearchCondition condition, Pageable pageable);

    List<SimpleCurriculumDto> getCurriculumListInOrder(CurriculaOrderType order);

    List<LocalDateTime> getSelectedWeekdays(LocalDateTime startDate, LocalDateTime endDate,
                                            int weekdaysBitmask);
}
