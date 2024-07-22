package com.twentyone.steachserver.domain.classroom.model;


import com.twentyone.steachserver.domain.classroom.service.util.ClassroomUtil;
import com.twentyone.steachserver.domain.lecture.model.Lecture;
import jakarta.persistence.*;
import lombok.*;


@Getter(value = AccessLevel.PUBLIC)


@NoArgsConstructor
@Entity
@Table(name = "classrooms")
public class Classroom {

    @Id
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    private String sessionId;

    private Classroom(Lecture lecture) {
        this.lecture = lecture;
        // 추후 자동 sessionId 만들어주는 로직을 만들어볼까??
        this.sessionId = ClassroomUtil.generateSessionId(lecture.getId()); // 임시 로직
    }

    public static Classroom createClassroom(Lecture lecture) {
        return new Classroom(lecture);
    }
}
