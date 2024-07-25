package com.twentyone.steachserver.domain.curriculum.service;

import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.curriculum.dto.*;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.model.Teacher;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface CurriculumService {
    CurriculumDetailResponse getDetail(Integer id);

    CurriculumDetailResponse create(LoginCredential credential, CurriculumAddRequest request);

    void registration(LoginCredential credential, Integer curriculaId);

    @Transactional(readOnly = true)
    CurriculumListResponse getTeachersCurricula(Teacher teacher);

    @Transactional(readOnly = true)
    CurriculumListResponse getStudentsCurricula(Student student);

    CurriculumListResponse search(CurriculaSearchCondition condition);

    List<SimpleCurriculumDto> getCurriculumListInOrder(CurriculaOrderType order);

    List<LocalDateTime> getSelectedWeekdays(LocalDateTime startDate, LocalDateTime endDate,
                                            int weekdaysBitmask);
}
