package com.twentyone.steachserver.domain.classroom.model;


import com.twentyone.steachserver.domain.lecture.model.Lecture;
import jakarta.persistence.*;
import lombok.*;


@Getter(value = AccessLevel.PUBLIC)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "classrooms")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Classroom {

    // 하위 관계여서 classroom에서 lecture을 조회할 일이 없으면 이렇게.
    // ???
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    // 이거랑 참여 코드랑 같게하면 안될까??
    // 아니면 참여코드만 쓰던가!
    private String sessionId;

    @Id
    @OneToOne
    @JoinColumn(name = "lectures_id")
    private Lecture lecture;


    private Classroom(Lecture lecture) {
        this.lecture = lecture;
        // 추후 자동 sessionId 만들어주는 로직을 만들어볼까??
        this.sessionId = "classroom" + lecture.getId(); // 임시 로직
        // 이 로직 만들어줘야함.
        // 만약 상위에서만 조회할 수 있다면 어자피 같은 leactureId로 조회 가능
        // ???
        lecture.addClassroom(this);
    }

    public static Classroom createClassroom(Lecture lecture) {
        return new Classroom(lecture);
    }
}
