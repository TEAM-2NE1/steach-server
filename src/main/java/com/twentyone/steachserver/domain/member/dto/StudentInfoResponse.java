package com.twentyone.steachserver.domain.member.dto;

import com.twentyone.steachserver.domain.member.model.Student;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class StudentInfoResponse extends MemberInfoResponse {
    public static StudentInfoResponse fromDomain(Student student) {
        StudentInfoResponse response = new StudentInfoResponse();
        response.username = student.getUsername();
        response.nickname = student.getName();
        response.email = student.getEmail();

        return response;
    }
}
