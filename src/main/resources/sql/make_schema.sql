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
    `login_credentials_id` INTEGER(11)  NOT NULL,
    `name`                 VARCHAR(30)  NOT NULL,
    `volunteer_time`       SMALLINT(6)  NOT NULL DEFAULT 0,
    `path_qualification`   VARCHAR(255) NULL,
    `created_at`           DATETIME     NOT NULL,
    `updated_at`           DATETIME     NOT NULL,
    -- `email` VARCHAR(255) NOT NULL UNIQUE,
    CONSTRAINT `PK_teachers` PRIMARY KEY (`id`),
    CONSTRAINT `FK_teachers_login_credentials` FOREIGN KEY (`login_credentials_id`) REFERENCES `login_credentials` (`id`)
);

CREATE TABLE `students`
(
    `id`                   INTEGER(11) NOT NULL AUTO_INCREMENT,
    `login_credentials_id` INTEGER(11) NOT NULL,
    `name`                 VARCHAR(30) NOT NULL UNIQUE,
    `created_at`           DATETIME    NOT NULL,
    `updated_at`           DATETIME    NOT NULL,
    -- `email` VARCHAR(255) NOT NULL UNIQUE,
    CONSTRAINT `PK_students` PRIMARY KEY (`id`),
    CONSTRAINT `FK_students_login_credentials` FOREIGN KEY (`login_credentials_id`) REFERENCES `login_credentials` (`id`)
);

CREATE TABLE `admins`
(
    `id`                   INTEGER(11) NOT NULL AUTO_INCREMENT,
    `login_credentials_id` INTEGER(11) NOT NULL,
    `name`                 VARCHAR(30) NOT NULL UNIQUE,
    `created_at`           DATETIME    NOT NULL,
    `updated_at`           DATETIME    NOT NULL,
    -- `email` VARCHAR(255) NOT NULL UNIQUE,
    CONSTRAINT `PK_admins` PRIMARY KEY (`id`),
    CONSTRAINT `FK_admins_login_credentials` FOREIGN KEY (`login_credentials_id`) REFERENCES `login_credentials` (`id`)
);

CREATE TABLE `curricula_schedules`
(
    `id`           INTEGER(11) NOT NULL AUTO_INCREMENT,
    `weekdays_bitmask` BIT(7) NOT NULL DEFAULT 0,
    `start_date` DATE        NOT NULL,
    `end_date`  DATE        NOT NULL,
    `lecture_start_time` TIME        NOT NULL,
    `lecture_close_time`  TIME        NOT NULL,
    CONSTRAINT `PK_curricula_schedules` PRIMARY KEY (`id`)
);

CREATE TABLE `curricula_information`
(
    `id`          INTEGER(11)  NOT NULL AUTO_INCREMENT,
    `sub_title`   VARCHAR(255) NULL DEFAULT NULL,
    `intro`       VARCHAR(255) NULL DEFAULT NULL,
    `target`      VARCHAR(255) NULL,
    `requirement` VARCHAR(255) NULL,
    `information` TEXT         NULL,
    `Field`       VARCHAR(255) NOT NULL,
    CONSTRAINT `PK_curricula_information` PRIMARY KEY (`id`)
);

CREATE TABLE `curricula`
(
    `id`             INTEGER(11)                                             NOT NULL AUTO_INCREMENT,
    `teachers_id`    INTEGER(11)                                             NOT NULL,
    `information_id` INTEGER(11)                                             NOT NULL,
    `schedules_id`   INTEGER(11)                                             NOT NULL,
    `title`          VARCHAR(255)                                            NOT NULL,
    `category`       ENUM ('국어', '외국어', '수학', '과학', '사회', '공학', '예체능', '기타') NOT NULL,
    CONSTRAINT `PK_curricula` PRIMARY KEY (`id`),
    CONSTRAINT `FK_curricula_teachers` FOREIGN KEY (`teachers_id`) REFERENCES `teachers` (`id`),
    CONSTRAINT `FK_curricula_curricula_information` FOREIGN KEY (`information_id`) REFERENCES `curricula_information` (`id`),
    CONSTRAINT `FK_curricula_curricula_schedules` FOREIGN KEY (`schedules_id`) REFERENCES `curricula_schedules` (`id`)
);

CREATE TABLE `lectures`
(
    `id`            INTEGER(11)  NOT NULL AUTO_INCREMENT,
    `curricula_id`  INTEGER(11)  NOT NULL,
    `lecture_order` TINYINT(4)   NOT NULL,
    `title`         VARCHAR(255) NOT NULL,
    `start_time` TIMESTAMP NOT NULL,
    `end_time` TIMESTAMP NOT NULL,
    CONSTRAINT `PK_lectures` PRIMARY KEY (`id`),
    CONSTRAINT `FK_lectures_curricula` FOREIGN KEY (`curricula_id`) REFERENCES `curricula` (`id`)
);

CREATE TABLE `quizzes`
(
    `id`          INTEGER(11)  NOT NULL AUTO_INCREMENT,
    `lectures_id` INTEGER(11)  NOT NULL,
    `quiz_number` TINYINT(4)   NOT NULL,
    `question`    VARCHAR(255) NOT NULL,
    CONSTRAINT `PK_quizzes` PRIMARY KEY (`id`),
    CONSTRAINT `FK_quizzes_lectures` FOREIGN KEY (`lectures_id`) REFERENCES `lectures` (`id`)
);

CREATE TABLE `quiz_choices`
(
    `id`              INTEGER(11)  NOT NULL AUTO_INCREMENT,
    `quizzes_id`      INTEGER(11)  NOT NULL,
    `is_answer`       BIT(1)       NOT NULL,
    `choice_sentence` VARCHAR(255) NOT NULL,
    CONSTRAINT `PK_quiz_choices` PRIMARY KEY (`id`),
    CONSTRAINT `FK_quiz_choices_quizzes` FOREIGN KEY (`quizzes_id`) REFERENCES `quizzes` (`id`)
);

CREATE TABLE `students_quizzes`
(
    `id`          INTEGER(11) NOT NULL AUTO_INCREMENT,
    `students_id` INTEGER(11) NOT NULL,
    `quizzes_id`  INTEGER(11) NOT NULL,
    `total_score` INTEGER(11) NOT NULL,
    CONSTRAINT `PK_students_quizzes` PRIMARY KEY (`id`),
    CONSTRAINT `FK_students_quizzes_students` FOREIGN KEY (`students_id`) REFERENCES `students` (`id`),
    CONSTRAINT `FK_students_quizzes_quizzes` FOREIGN KEY (`quizzes_id`) REFERENCES `quizzes` (`id`)
);

CREATE TABLE `students_curricula`
(
    `id`           INTEGER(11) NOT NULL AUTO_INCREMENT,
    `curricula_id` INTEGER(11) NOT NULL,
    `students_id`  INTEGER(11) NOT NULL,
    CONSTRAINT `PK_students_curricula` PRIMARY KEY (`id`),
    CONSTRAINT `FK_students_curricula_curricula` FOREIGN KEY (`curricula_id`) REFERENCES `curricula` (`id`),
    CONSTRAINT `FK_students_curricula_students` FOREIGN KEY (`students_id`) REFERENCES `students` (`id`)
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
    CONSTRAINT `PK_lectures_students` PRIMARY KEY (`id`),
    CONSTRAINT `FK_lectures_students_students` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`),
    CONSTRAINT `FK_lectures_students_lectures` FOREIGN KEY (`lecture_id`) REFERENCES `lectures` (`id`)
);

CREATE TABLE `classrooms`
(
    `lecture_id` INTEGER(11) NOT NULL,
    `session_id` VARCHAR(255) NOT NULL,
    CONSTRAINT `PK_classrooms_lecture_id` PRIMARY KEY (`lecture_id`),
    CONSTRAINT `FK_classrooms_lectures` FOREIGN KEY (`lecture_id`) REFERENCES `lectures` (`id`) ON DELETE CASCADE
);
