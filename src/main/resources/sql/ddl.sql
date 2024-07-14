
create table admins (
                        id integer auto_increment not null ,
                        login_credentials_id integer,
                        created_at datetime(6),
                        updated_at datetime(6),
                        name varchar(30),
                        primary key (id)
)  engine=InnoDB;

create table auth_codes (
                            is_registered bit,
                            auth_code varchar(30) not null,
                            primary key (auth_code)
)  engine=InnoDB;

create table classrooms (
                            lectures_id integer,
                            id bigint not null auto_increment,
                            session_id varchar(255),
                            primary key (id)
)  engine=InnoDB;

create table curricula (
                           id integer not null auto_increment,
                           informations_id integer,
                           schedules_id integer,
                           teachers_id integer,
                           title varchar(255),
                           category enum ('ARTS_AND_PHYSICAL','EDUCATION','ENGINEERING','ETC','FOREIGN_LANGUAGE','KOREAN','MATH','SCIENCE'),
                           primary key (id)
)  engine=InnoDB;

create table curricula_informations (
                                        id integer not null auto_increment,
                                        field varchar(255),
                                        information varchar(255),
                                        intro varchar(255),
                                        requirement varchar(255),
                                        sub_title varchar(255),
                                        target varchar(255),
                                        primary key (id)
)  engine=InnoDB;

create table curricula_schedules (
                                     friday bit,
                                     id integer not null auto_increment,
                                     monday bit,
                                     saturday bit,
                                     sunday bit,
                                     thursday bit,
                                     tuesday bit,
                                     wednesday bit,
                                     closed_at datetime(6),
                                     started_at datetime(6),
                                     primary key (id)
)  engine=InnoDB;

create table lectures (
                          curricula_id integer,
                          id integer not null auto_increment,
                          lecture_order integer,
                          title varchar(255),
                          primary key (id)
)  engine=InnoDB;

create table lectures_students (
                                   focus_ratio integer,
                                   id integer not null auto_increment,
                                   lectures_id integer,
                                   students_id integer,
                                   primary key (id)
)  engine=InnoDB;

create table login_credentials (
                                   id integer not null auto_increment,
                                   password varchar(255),
                                   username varchar(255),
                                   primary key (id)
)  engine=InnoDB;

create table quiz_choices (
                              id integer not null auto_increment,
                              is_answer integer,
                              quizzes_id integer,
                              choice_sentence varchar(255),
                              primary key (id)
)  engine=InnoDB;

create table quizzes (
                         id integer not null auto_increment,
                         lectures_id integer,
                         quiz_number integer,
                         question varchar(255),
                         primary key (id)
)  engine=InnoDB;

create table students (
                          id integer not null auto_increment,
                          login_credentials_id integer,
                          created_at datetime(6),
                          updated_at datetime(6),
                          name varchar(30),
                          primary key (id)
)  engine=InnoDB;

create table students_curricula (
                                    curricula_id integer not null,
                                    id integer not null auto_increment,
                                    students_id integer not null,
                                    primary key (id)
)  engine=InnoDB;

create table students_quizzes (
                                  id integer not null auto_increment,
                                  quizzes_id integer,
                                  students_id integer,
                                  total_score integer,
                                  primary key (id)
)  engine=InnoDB;

create table teachers (
                          id integer not null auto_increment,
                          login_credentials_id integer,
                          volunteer_time integer,
                          created_at datetime(6),
                          updated_at datetime(6),
                          name varchar(30),
                          path_qualification varchar(255),
                          primary key (id)
)  engine=InnoDB;



-- unique key constraint ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
alter table admins
    add constraint UK_7gb6d5m9o9af46g8xnjokff0u unique (login_credentials_id);

alter table classrooms
    add constraint UK_r7lhedcbk9daarouy38w6hiab unique (lectures_id);

alter table curricula
    add constraint UK_dy8yxxsgybs20u0yabwq66epf unique (informations_id);

alter table curricula
    add constraint UK_3m5b0y0y68fimnu9ts7e594hn unique (schedules_id);

alter table lectures_students
    add constraint uk_lecture_student unique (students_id, lectures_id);

alter table students
    add constraint UK_d59e5ior60dtpfp7qcp7bwxwe unique (login_credentials_id);

alter table students_curricula
    add constraint uk_student_curricula unique (curricula_id, students_id);

alter table students_quizzes
    add constraint uk_student_quiz unique (students_id, quizzes_id);

alter table teachers
    add constraint UK_420ipp4yo11x41nvehbom22bg unique (login_credentials_id);


-- foreign key constraint ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

alter table admins
    add constraint FKam2dh29lw1vgu1ukeic6l4btv
        foreign key (login_credentials_id)
            references login_credentials (id)
            on delete cascade;
-- login_credentials 가 삭제되면 admins도 삭제


alter table classrooms
    add constraint FKi1kxqyxkeik9i8fcd5098996p
        foreign key (lectures_id)
            references lectures (id)
            on delete set null;
-- lectures가 삭제되면 클래스룸은 삭제되지 않음


alter table curricula
    add constraint FKhv5kbk5rgxb126l9ssdkjuaqd
        foreign key (informations_id)
            references curricula_informations (id)
            on delete cascade;



alter table curricula
    add constraint FKjtxmcaavk7iqt0oxtl1kldw3
        foreign key (teachers_id)
            references teachers (id)
            on delete set null;
-- teacher가 삭제되어도 curricula는 삭제되지 않음


alter table lectures
    add constraint FK5aebkf3r8y4y6x6ofe6n76rly
        foreign key (curricula_id)
            references curricula (id)
            on delete cascade;
-- curricula가 사라지면 lecture도 삭제


alter table lectures_students
    add constraint FK43ydrd9tdva87j0hamxt2lci8
        foreign key (lectures_id)
            references lectures (id)
            on delete cascade;
-- lecture가 사라지면 lectures_students도 삭제


alter table lectures_students
    add constraint FKi8usn04ep6rpscv6l0coyy2iw
        foreign key (students_id)
            references students (id)
            on delete cascade;
-- student가 삭제되면 lectures_students도 삭제


alter table quiz_choices
    add constraint FK7da4yq3u45r0567jd18y7j1rq
        foreign key (quizzes_id)
            references quizzes (id)
            on delete cascade;
-- quiz가 삭제되면 quiz_chocies도 삭제


alter table quizzes
    add constraint FKaqlun3vh70i3uxk2qk2ti1hpm
        foreign key (lectures_id)
            references lectures (id)
            on delete cascade;
-- lecture가 삭제되면 quiz 삭제


alter table students
    add constraint FKclrseyfh1ji810fi68cxk319d
        foreign key (login_credentials_id)
            references login_credentials (id)
            on delete cascade;
-- login_credentials가 삭제되면 students 삭제


alter table students_curricula
    add constraint FK55t3gwgg0pruu9iald3tqxg5i
        foreign key (curricula_id)
            references curricula (id)
            on delete cascade;
-- curricula가 삭제되면 students_curricula 삭제


alter table students_curricula
    add constraint FKc59sdu6e0g7hutb9ghqk7o7e3
        foreign key (students_id)
            references students (id)
            on delete cascade;


alter table students_quizzes
    add constraint FK787gh3xortb230l1g3meg3ao0
        foreign key (quizzes_id)
            references quizzes (id)
            on delete cascade;


alter table students_quizzes
    add constraint FK4a7p5nnwvd8pgjwhyt3sjuhu9
        foreign key (students_id)
            references students (id)
            on delete cascade;


alter table teachers
    add constraint FKcghy6q5so9aw54b98cy15etxe
        foreign key (login_credentials_id)
            references login_credentials (id)
            on delete cascade;