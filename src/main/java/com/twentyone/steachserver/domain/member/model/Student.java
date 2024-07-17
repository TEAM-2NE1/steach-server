package com.twentyone.steachserver.domain.member.model;

import com.twentyone.steachserver.domain.lectureStudent.model.LectureStudent;
import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.studentCurriculum.model.StudentCurriculum;
import com.twentyone.steachserver.domain.studentQuiz.model.StudentQuiz;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

/**
 * 설명
 * 상위 클래스(LoginCredential): @Inheritance(strategy = InheritanceType.JOINED)를 사용하여 상속 전략을 정의합니다.
 * 하위 클래스(Student 및 Admin): @PrimaryKeyJoinColumn을 지정할 필요 없이 상위 클래스 LoginCredential에서 상속됩니다.
 * @PrimaryKeyJoinColumn 사용법
 * 필요한 경우 @PrimaryKeyJoinColumn 주석을 사용하여 외래 키 매핑을 명시적으로 정의할 수 있지만 기본 JPA 동작이 요구 사항에 맞는 경우 필수는 아닙니다. JPA는 상위 클래스에 정의된 상속 전략에 따라 기본 키 조인을 자동으로 처리합니다.
 *
 * 요약
 * 상위 클래스에 @Inheritance 주석만 있으면 상속이 가능합니다.
 * 하위 클래스의 @PrimaryKeyJoinColumn 주석은 선택사항이며 외래 키 매핑을 사용자 정의해야 하는 경우에만 사용됩니다. 상위 클래스에 '@Id' 필드가 있고 상속 전략이 정의된 경우 위 설정이 올바르게 작동해야 합니다.
 */
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PROTECTED)
@Entity
@Table(name = "students")
@AllArgsConstructor
@NoArgsConstructor
//@DiscriminatorValue("STUDENT") // 엔티티 타입 식별자 값 지정 / Student 엔티티가 type 컬럼에 저장할 값을 지정합니다.
@PrimaryKeyJoinColumn(name = "id") // 상속받은 엔티티의 기본 키를 지정
public class Student extends LoginCredential{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;

    @Column(length = 30, nullable = false)
    private String name;

    private String email = "";

//    @OneToOne
//    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
//    private LoginCredential loginCredential;

    @OneToMany(mappedBy = "student")
    private List<StudentQuiz> studentQuiz = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<StudentCurriculum> studentsCurricula = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<LectureStudent> lectureStudents = new ArrayList<>();

    public static Student of(String username, String password, String name) {
        Student student = new Student();
        student.setUsername(username);
        student.setPassword(password);
        student.name = name;
//        student.loginCredential = loginCredential;
        LoginCredential studentLoginCredential = student;
//        studentLoginCredential.of(loginCredential);
        return student;
    }
}
