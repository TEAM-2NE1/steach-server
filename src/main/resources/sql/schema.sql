create or replace table s11p11d201.auth_codes
(
    is_registered bit         null,
    auth_code     varchar(30) not null
        primary key
);

create or replace table s11p11d201.login_credentials
(
    id       int auto_increment
        primary key,
    password varchar(255) null,
    username varchar(255) null
);

create or replace table s11p11d201.admins
(
    id                   int auto_increment
        primary key,
    login_credentials_id int         null,
    created_at           datetime(6) null,
    updated_at           datetime(6) null,
    name                 varchar(30) null,
    constraint UK_7gb6d5m9o9af46g8xnjokff0u
        unique (login_credentials_id)
);

create or replace table s11p11d201.students
(
    id                   int auto_increment
        primary key,
    login_credentials_id int         null,
    created_at           datetime(6) null,
    updated_at           datetime(6) null,
    name                 varchar(30) null,
    constraint UK_d59e5ior60dtpfp7qcp7bwxwe
        unique (login_credentials_id)
);

create or replace table s11p11d201.teachers
(
    id                   int auto_increment
        primary key,
    login_credentials_id int          null,
    volunteer_time       int          null,
    created_at           datetime(6)  null,
    updated_at           datetime(6)  null,
    name                 varchar(30)  null,
    path_qualification   varchar(255) null,
    constraint UK_420ipp4yo11x41nvehbom22bg
        unique (login_credentials_id)
);

create or replace table s11p11d201.curricula
(
    id              int auto_increment
        primary key,
    informations_id int                                                                                                            null,
    schedules_id    int                                                                                                            null,
    teachers_id     int                                                                                                            null,
    title           varchar(255)                                                                                                   null,
    category        enum ('ARTS_AND_PHYSICAL', 'EDUCATION', 'ENGINEERING', 'ETC', 'FOREIGN_LANGUAGE', 'KOREAN', 'MATH', 'SCIENCE') null,
    constraint UK_3m5b0y0y68fimnu9ts7e594hn
        unique (schedules_id),
    constraint UK_dy8yxxsgybs20u0yabwq66epf
        unique (informations_id)
);

create or replace table s11p11d201.curricula_informations
(
    curricula_id   int           null,
    id             int auto_increment
        primary key,
    banner_img_url varchar(1000) null,
    intro          text          null,
    requirement    text          null,
    sub_title      text          null,
    target         text          null,
    information    mediumtext    null,
    constraint UK_k6mb0o2j1cr8nvwe84sjhmmya
        unique (curricula_id)
);

create or replace table s11p11d201.curricula_schedules
(
    closed_time  time(6) null,
    curricula_id int     null,
    friday       bit     null,
    id           int auto_increment
        primary key,
    monday       bit     null,
    saturday     bit     null,
    started_date date    null,
    started_time time(6) null,
    sunday       bit     null,
    thursday     bit     null,
    tuesday      bit     null,
    wednesday    bit     null,
    week_number  int     null,
    constraint UK_suh1ekadi3vjpp3mp5741rwc5
        unique (curricula_id)
);

create or replace table s11p11d201.lectures
(
    curricula_id  int          null,
    id            int auto_increment
        primary key,
    lecture_order int          null,
    title         varchar(255) null
);

create or replace table s11p11d201.classrooms
(
    lectures_id int          null,
    id          bigint auto_increment
        primary key,
    session_id  varchar(255) null,
    constraint UK_r7lhedcbk9daarouy38w6hiab
        unique (lectures_id)
);

create or replace table s11p11d201.lectures_students
(
    focus_ratio int null,
    lectures_id int not null,
    students_id int not null,
    primary key (students_id, lectures_id)
);

create or replace table s11p11d201.quizzes
(
    id          int auto_increment
        primary key,
    lectures_id int          null,
    quiz_number int          null,
    question    varchar(255) not null
);

create or replace table s11p11d201.quiz_choices
(
    id              int auto_increment
        primary key,
    is_answer       bit          not null,
    quizzes_id      int          null,
    choice_sentence varchar(255) not null
);

create or replace table s11p11d201.students_curricula
(
    curricula_id int not null,
    students_id  int not null,
    primary key (curricula_id, students_id)
);

create or replace table s11p11d201.students_quizzes
(
    quizzes_id  int not null,
    students_id int not null,
    total_score int null,
    primary key (students_id, quizzes_id)
);


alter table s11p11d201.admins
    add constraint FKam2dh29lw1vgu1ukeic6l4btv
        foreign key (login_credentials_id) references s11p11d201.login_credentials (id);

alter table s11p11d201.students
    add constraint FKclrseyfh1ji810fi68cxk319d
        foreign key (login_credentials_id) references s11p11d201.login_credentials (id);

alter table s11p11d201.teachers
    add constraint FKcghy6q5so9aw54b98cy15etxe
        foreign key (login_credentials_id) references s11p11d201.login_credentials (id);

alter table s11p11d201.curricula
    add constraint FKjtxmcaavk7iqt0oxtl1kldw3
        foreign key (teachers_id) references s11p11d201.teachers (id);

alter table s11p11d201.curricula_informations
    add constraint FK819x6kfbkhv22l64ou1kqbem2
        foreign key (curricula_id) references s11p11d201.curricula (id);

alter table s11p11d201.curricula
    add constraint FKhv5kbk5rgxb126l9ssdkjuaqd
        foreign key (informations_id) references s11p11d201.curricula_informations (id);

alter table s11p11d201.curricula_schedules
    add constraint FKnagslltf7rtxfnpps04o2w55n
        foreign key (curricula_id) references s11p11d201.curricula (id);

alter table s11p11d201.curricula
    add constraint FKq3jpem50qtt3frq9spe1coada
        foreign key (schedules_id) references s11p11d201.curricula_schedules (id);

alter table s11p11d201.lectures
    add constraint FK5aebkf3r8y4y6x6ofe6n76rly
        foreign key (curricula_id) references s11p11d201.curricula (id);

alter table s11p11d201.classrooms
    add constraint FKi1kxqyxkeik9i8fcd5098996p
        foreign key (lectures_id) references s11p11d201.lectures (id);

alter table s11p11d201.lectures_students
    add constraint FK43ydrd9tdva87j0hamxt2lci8
        foreign key (lectures_id) references s11p11d201.lectures (id),
    add constraint FKi8usn04ep6rpscv6l0coyy2iw
        foreign key (students_id) references s11p11d201.students (id);

alter table s11p11d201.quizzes
    add constraint FKaqlun3vh70i3uxk2qk2ti1hpm
        foreign key (lectures_id) references s11p11d201.lectures (id);

alter table s11p11d201.quiz_choices
    add constraint FK7da4yq3u45r0567jd18y7j1rq
        foreign key (quizzes_id) references s11p11d201.quizzes (id);

alter table s11p11d201.students_curricula
    add constraint FK55t3gwgg0pruu9iald3tqxg5i
        foreign key (curricula_id) references s11p11d201.curricula (id),
    add constraint FKc59sdu6e0g7hutb9ghqk7o7e3
        foreign key (students_id) references s11p11d201.students (id);

alter table s11p11d201.students_quizzes
    add constraint FK4a7p5nnwvd8pgjwhyt3sjuhu9
        foreign key (students_id) references s11p11d201.students (id),
    add constraint FK787gh3xortb230l1g3meg3ao0
        foreign key (quizzes_id) references s11p11d201.quizzes (id);
