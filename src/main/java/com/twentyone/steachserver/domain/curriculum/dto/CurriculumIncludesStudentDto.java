package com.twentyone.steachserver.domain.curriculum.dto;

import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import com.twentyone.steachserver.domain.member.dto.StudentInfoResponse;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.studentCurriculum.model.StudentCurriculum;

import java.util.ArrayList;
import java.util.List;

public record CurriculumIncludesStudentDto(CurriculumDetailResponse curriculumListResponse
        , List<StudentInfoResponse> studentInfoResponses) {
    public static CurriculumIncludesStudentDto of(Curriculum curriculum, List<StudentCurriculum> StudentCurricula) {
        CurriculumDetailResponse curriculumDetailResponse = CurriculumDetailResponse.fromDomain(curriculum);
        List<StudentInfoResponse> studentInfoResponses = new ArrayList<>();
        for (StudentCurriculum studentCurriculum : StudentCurricula) {
            Student student = studentCurriculum.getStudent();
            StudentInfoResponse studentInfoResponse = StudentInfoResponse.fromDomain(student);
            studentInfoResponses.add(studentInfoResponse);
        }
        return new CurriculumIncludesStudentDto(curriculumDetailResponse, studentInfoResponses);
    }
}
