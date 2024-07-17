-- 정수 타입 (Integer Types)
-- TINYINT: -128 ~ 127 (SIGNED), 0 ~ 255 (UNSIGNED)
-- SMALLINT: -32,768 ~ 32,767 (SIGNED), 0 ~ 65,535 (UNSIGNED)
-- MEDIUMINT: -8,388,608 ~ 8,388,607 (SIGNED), 0 ~ 16,777,215 (UNSIGNED)
-- INT 또는 INTEGER: -2,147,483,648 ~ 2,147,483,647 (SIGNED), 0 ~ 4,294,967,295 (UNSIGNED)
-- BIGINT: -9,223,372,036,854,775,808 ~ 9,223,372,036,854,775,807 (SIGNED), 0 ~ 18,446,744,073,709,551,615 (UNSIGNED)

-- 소수점 타입 (Decimal Types)
-- FLOAT(M, D): 소수점 이하 D 자리를 포함한 M 자리의 부동 소수점 숫자. 예: FLOAT(7, 3) -> 최대 7자리 숫자, 소수점 이하 3자리
-- DOUBLE(M, D): 소수점 이하 D 자리를 포함한 M 자리의 배정밀도 부동 소수점 숫자. 예: DOUBLE(16, 4) -> 최대 16자리 숫자, 소수점 이하 4자리
-- DECIMAL(M, D): 소수점 이하 D 자리를 포함한 M 자리의 고정 소수점 숫자. 예: DECIMAL(10, 2) -> 최대 10자리 숫자, 소수점 이하 2자리

CREATE TABLE `login_credentials`
(
    `id`       INTEGER(11)  NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(16)  NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    CONSTRAINT `PK_login_credentials` PRIMARY KEY (`id`)
);

CREATE TABLE `teachers`
(
    `id`                   INTEGER(11)  NOT NULL AUTO_INCREMENT,
    `login_credential_id`  INTEGER(11)  NOT NULL,
    `name`                 VARCHAR(30)  NOT NULL,
    `email`                VARCHAR(255) NULL UNIQUE,
    `volunteer_time`       SMALLINT(6)  NOT NULL DEFAULT 0,
    `path_qualification`   VARCHAR(255) NULL,
    `created_at`           DATETIME     NOT NULL,
    `updated_at`           DATETIME     NOT NULL,
    CONSTRAINT `PK_teachers` PRIMARY KEY (`id`),
    CONSTRAINT `FK_teachers_login_credentials` FOREIGN KEY (`login_credential_id`) REFERENCES `login_credentials` (`id`) ON DELETE CASCADE
);

CREATE TABLE `students`
(
    `id`                   INTEGER(11) NOT NULL AUTO_INCREMENT,
    `login_credential_id`  INTEGER(11) NOT NULL,
    `name`                 VARCHAR(30) NOT NULL UNIQUE,
    `created_at`           DATETIME    NOT NULL,
    `updated_at`           DATETIME    NOT NULL,
    `email` VARCHAR(255) NULL UNIQUE,
    CONSTRAINT `PK_students` PRIMARY KEY (`id`),
    CONSTRAINT `FK_students_login_credentials` FOREIGN KEY (`login_credential_id`) REFERENCES `login_credentials` (`id`) ON DELETE CASCADE
);

CREATE TABLE `admins`
(
    `id`                   INTEGER(11) NOT NULL AUTO_INCREMENT,
    `login_credential_id`  INTEGER(11) NOT NULL,
    `name`                 VARCHAR(30) NOT NULL UNIQUE,
    `created_at`           DATETIME    NOT NULL,
    `updated_at`           DATETIME    NOT NULL,
    CONSTRAINT `PK_admins` PRIMARY KEY (`id`),
    CONSTRAINT `FK_admins_login_credentials` FOREIGN KEY (`login_credential_id`) REFERENCES `login_credentials` (`id`) ON DELETE CASCADE
);


CREATE TABLE `curriculum_details`
(
    `id`          INTEGER(11)  NOT NULL AUTO_INCREMENT,
    `sub_title`   VARCHAR(255) NULL,
    `intro`       VARCHAR(255) NULL,
    `banner_img_url` VARCHAR(255) NULL,
    `information`   TEXT     NOT NULL,
    `sub_category`       VARCHAR(255) NOT NULL,
    `weekdays_bitmask`  BIT(7) NOT NULL DEFAULT 0,
    `start_date`        DATE        NOT NULL,
    `end_date`          DATE        NOT NULL,
    `lecture_start_time` TIME        NOT NULL,
    `lecture_close_time` TIME        NOT NULL,
    CONSTRAINT `PK_curriculum_details` PRIMARY KEY (`id`)
);

CREATE TABLE `curricula`
(
    `id`             INTEGER(11)                                             NOT NULL AUTO_INCREMENT,
    `teacher_id`     INTEGER(11)                                             NULL,
    `detail_id`      INTEGER(11)                                             NOT NULL,
    `schedule_id`    INTEGER(11)                                             NOT NULL,
    `title`          VARCHAR(255)                                            NOT NULL,
    `category`       ENUM ('국어', '외국어', '수학', '과학', '사회', '공학', '예체능', '기타') NOT NULL,
    CONSTRAINT `PK_curricula` PRIMARY KEY (`id`),
    CONSTRAINT `FK_curricula_teachers` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`id`) ON DELETE SET NULL,
    CONSTRAINT `FK_curricula_curriculum_details` FOREIGN KEY (`detail_id`) REFERENCES `curriculum_details` (`id`) ON DELETE CASCADE
);

CREATE TABLE `lectures`
(
    `id`            INTEGER(11)  NOT NULL AUTO_INCREMENT,
    `curriculum_id` INTEGER(11)  NOT NULL,
    `lecture_order` TINYINT(4)   NOT NULL,
    `title`         VARCHAR(255) NOT NULL,
    `real_start_time`    TIMESTAMP    NULL,
    `real_end_time`      TIMESTAMP    NULL,
    `lecture_start_date` DATETIME     NULL,
    `number_of_quizzes` TINYINT(4)   NOT NULL DEFAULT 0,
    CONSTRAINT `PK_lectures` PRIMARY KEY (`id`),
    CONSTRAINT `FK_lectures_curricula` FOREIGN KEY (`curriculum_id`) REFERENCES `curricula` (`id`) ON DELETE CASCADE
);

CREATE TABLE `quizzes`
(
    `id`          INTEGER(11)  NOT NULL AUTO_INCREMENT,
    `lecture_id`  INTEGER(11)  NOT NULL,
    `quiz_number` TINYINT(4)   NOT NULL,
    `question`    VARCHAR(255) NOT NULL,
    CONSTRAINT `PK_quizzes` PRIMARY KEY (`id`),
    CONSTRAINT `FK_quizzes_lectures` FOREIGN KEY (`lecture_id`) REFERENCES `lectures` (`id`) ON DELETE CASCADE
);

CREATE TABLE `quiz_choices`
(
    `id`              INTEGER(11)  NOT NULL AUTO_INCREMENT,
    `quiz_id`         INTEGER(11)  NOT NULL,
    `is_answer`       BIT(1)       NOT NULL,
    `choice_sentence` VARCHAR(255) NOT NULL,
    CONSTRAINT `PK_quiz_choices` PRIMARY KEY (`id`),
    CONSTRAINT `FK_quiz_choices_quizzes` FOREIGN KEY (`quiz_id`) REFERENCES `quizzes` (`id`) ON DELETE CASCADE
);

CREATE TABLE `students_quizzes`
(
    `id`         INTEGER(11) NOT NULL AUTO_INCREMENT,
    `student_id` INTEGER(11) NOT NULL,
    `quiz_id`    INTEGER(11) NOT NULL,
    `score` INTEGER(11) NOT NULL,
    `student_choice` VARCHAR(255) NOT NULL,
    CONSTRAINT `PK_students_quizzes` PRIMARY KEY (`id`),
    CONSTRAINT `FK_students_quizzes_students` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE ,
    CONSTRAINT `FK_students_quizzes_quizzes` FOREIGN KEY (`quiz_id`) REFERENCES `quizzes` (`id`) ON DELETE CASCADE
);

CREATE TABLE `students_curricula`
(
    `id`           INTEGER(11) NOT NULL AUTO_INCREMENT,
    `curriculum_id` INTEGER(11) NOT NULL,
    `student_id`    INTEGER(11) NOT NULL,
    CONSTRAINT `PK_students_curricula` PRIMARY KEY (`id`),
    CONSTRAINT `FK_students_curricula_curricula` FOREIGN KEY (`curriculum_id`) REFERENCES `curricula` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_students_curricula_students` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE
);

CREATE TABLE `auth_codes`
(
    `auth_code`     VARCHAR(30) NOT NULL,
    `is_registered` BIT(1)      NOT NULL DEFAULT 0,
    CONSTRAINT `PK_auth_codes` PRIMARY KEY (`auth_code`)
);

CREATE TABLE `lectures_students`
(
    `id`          INTEGER(11) NOT NULL AUTO_INCREMENT,
    `student_id`  INTEGER(11) NOT NULL,
    `lecture_id`  INTEGER(11) NOT NULL,
    `focus_ratio` DECIMAL(5, 2) NULL,
    `focus_time`  SMALLINT(6) NOT NULL,
    `quiz_answer_count` SMALLINT(6) NOT NULL DEFAULT 0,
    `quiz_total_score` SMALLINT(6) NOT NULL DEFAULT 0,
    CONSTRAINT `PK_lectures_students` PRIMARY KEY (`id`),
    CONSTRAINT `FK_lectures_students_students` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_lectures_students_lectures` FOREIGN KEY (`lecture_id`) REFERENCES `lectures` (`id`) ON DELETE CASCADE
);

CREATE TABLE `classrooms`
(
    `lecture_id` INTEGER(11) NOT NULL,
    `session_id` VARCHAR(255) NOT NULL,
    CONSTRAINT `PK_classrooms_lecture_id` PRIMARY KEY (`lecture_id`),
    CONSTRAINT `FK_classrooms_lectures` FOREIGN KEY (`lecture_id`) REFERENCES `lectures` (`id`) ON DELETE CASCADE
);
