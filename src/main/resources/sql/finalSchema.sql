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
    `id`                   INTEGER(11)  NOT NULL AUTO_INCREMENT, -- 로그인 정보의 고유 ID
    `username`             VARCHAR(16)  NOT NULL UNIQUE,         -- 아이디 (중복 불가)
    `password`             VARCHAR(255) NOT NULL,                -- 비밀번호
    `created_at`           DATETIME     NOT NULL,                -- 생성 시간
    `updated_at`           DATETIME     NOT NULL,                -- 수정 시간
    CONSTRAINT `PK_login_credentials` PRIMARY KEY (`id`)
);

CREATE TABLE `admins`
(
    `id`                   INTEGER(11) NOT NULL,                 -- 관리자의 login_credentials ID
    `name`                 VARCHAR(30) NOT NULL UNIQUE,          -- 이름 (중복 불가)
    CONSTRAINT `PK_admins` PRIMARY KEY (`id`),
    CONSTRAINT `FK_admins_login_credentials` FOREIGN KEY (`id`)
        REFERENCES `login_credentials` (`id`) ON DELETE CASCADE
);

CREATE TABLE `teachers`
(
    `id`                   INTEGER(11)  NOT NULL,                -- 강사의 login_credentials ID
    `name`                 VARCHAR(30)  NOT NULL,                -- 이름
    `email`                VARCHAR(255) NULL UNIQUE,             -- 이메일 (중복 불가)
    `volunteer_time`       SMALLINT(6)  NOT NULL DEFAULT 0,      -- 봉사 시간
    `path_qualification`   VARCHAR(255) NULL,                    -- 자격 증명서 경로(첨부 파일 경로)
    `brief_introduction`   VARCHAR(255) NULL,                    -- 강사 소개
    `academic_background`  VARCHAR(255) NULL,                    -- 주요 학력
    `specialization`       VARCHAR(255) NULL,                    -- 강의 분야
    CONSTRAINT `PK_teachers` PRIMARY KEY (`id`),
    CONSTRAINT `FK_teachers_login_credentials` FOREIGN KEY (`id`) REFERENCES `login_credentials` (`id`) ON DELETE CASCADE
);

CREATE TABLE `students`
(
    `id`                   INTEGER(11) NOT NULL,                 -- 학생의 login_credentials ID
    `name`                 VARCHAR(30) NOT NULL UNIQUE,          -- 닉네임 (중복 불가)
    `email`                VARCHAR(255) NULL UNIQUE,             -- 이메일 (중복 불가)
    CONSTRAINT `PK_students` PRIMARY KEY (`id`),
    CONSTRAINT `FK_students_login_credentials` FOREIGN KEY (`id`) REFERENCES `login_credentials` (`id`) ON DELETE CASCADE
);
-- drop table students_statistics;
CREATE TABLE `statistics`
(
    `student_id` INTEGER(11) NOT NULL,                           -- 학생의 login_credentials ID
    `gpt_career_suggestion` VARCHAR(255) NOT NULL DEFAULT '데이터가 더 필요합니다.', -- GPT 분석을 통한 진로 제안
    `average_focus_ratio1` DECIMAL(5, 2) NOT NULL DEFAULT 0,     -- 평균 참여도 1 (국어)
    `lecture_count1` SMALLINT(6) NOT NULL DEFAULT 0,             -- 강의 수 1
    `sum_lecture_minutes1` INTEGER(11) NOT NULL DEFAULT 0,       -- 총 강의 시간 1
    `average_focus_ratio2` DECIMAL(5, 2) NOT NULL DEFAULT 0,     -- 평균 참여도 2 (외국어)
    `lecture_count2` SMALLINT(6) NOT NULL DEFAULT 0,             -- 강의 수 2
    `sum_lecture_minutes2` INTEGER(11) NOT NULL DEFAULT 0,       -- 총 강의 시간 2
    `average_focus_ratio3` DECIMAL(5, 2) NOT NULL DEFAULT 0,     -- 평균 참여도 3 (수학)
    `lecture_count3` SMALLINT(6) NOT NULL DEFAULT 0,             -- 강의 수 3
    `sum_lecture_minutes3` INTEGER(11) NOT NULL DEFAULT 0,       -- 총 강의 시간 3
    `average_focus_ratio4` DECIMAL(5, 2) NOT NULL DEFAULT 0,     -- 평균 참여도 4 (사회)
    `lecture_count4` SMALLINT(6) NOT NULL DEFAULT 0,             -- 강의 수 4
    `sum_lecture_minutes4` INTEGER(11) NOT NULL DEFAULT 0,       -- 총 강의 시간 4
    `average_focus_ratio5` DECIMAL(5, 2) NOT NULL DEFAULT 0,     -- 평균 참여도 5 (과학)
    `lecture_count5` SMALLINT(6) NOT NULL DEFAULT 0,             -- 강의 수 5
    `sum_lecture_minutes5` INTEGER(11) NOT NULL DEFAULT 0,       -- 총 강의 시간 5
    `average_focus_ratio6` DECIMAL(5, 2) NOT NULL DEFAULT 0,     -- 평균 참여도 6 (공학)
    `lecture_count6` SMALLINT(6) NOT NULL DEFAULT 0,             -- 강의 수 6
    `sum_lecture_minutes6` INTEGER(11) NOT NULL DEFAULT 0,       -- 총 강의 시간 6
    `average_focus_ratio7` DECIMAL(5, 2) NOT NULL DEFAULT 0,     -- 평균 참여도 7 (예체능)
    `lecture_count7` SMALLINT(6) NOT NULL DEFAULT 0,             -- 강의 수 7
    `sum_lecture_minutes7` INTEGER(11) NOT NULL DEFAULT 0,       -- 총 강의 시간 7
    CONSTRAINT `PK_students_statistics` PRIMARY KEY (`student_id`),
    CONSTRAINT `FK_students_statistics_students` FOREIGN KEY students_statistics(`student_id`) REFERENCES students(`id`) ON DELETE CASCADE
);

CREATE TABLE `curriculum_details`
(
    `id`          INTEGER(11)  NOT NULL AUTO_INCREMENT,          -- 커리큘럼 세부 ID(커리큘럼과 같은 ID를 가지나 연결되어 있지는 않음)
    `sub_title`   VARCHAR(255) NULL,                             -- 부제목
    `intro`       VARCHAR(255) NULL,                             -- 강의 소개
    `banner_img_url` VARCHAR(255) NULL,                          -- 배너 이미지 URL
    `current_attendees` TINYINT(4) NOT NULL DEFAULT 0,           -- 현재 수강 신청 인원
    `max_attendees`     TINYINT(4) NOT NULL DEFAULT 4,           -- 최대 수강 정원
    `information`   TEXT     NOT NULL,                           -- 강의 상세 정보
    `sub_category`       VARCHAR(255) NOT NULL,                  -- 강의 중분류 (ex. category = 공학 > sub_category(직접 입력) = 전자공학
    `weekdays_bitmask`  BIT(7) NOT NULL DEFAULT 0,               -- 강의 진행 요일 (ex. bitmask = 1000001(월요일, 일요일)
    `start_date`        DATE        NOT NULL,                    -- 강의 시작일(처음 강의 시작일)
    `end_date`          DATE        NOT NULL,                    -- 강의 종료일(마지막 강의 종료일)
    `lecture_start_time` TIME        NOT NULL,                   -- 강의 시작 시간(매 요일 강의 시작 예정 시간)
    `lecture_close_time` TIME        NOT NULL,                   -- 강의 종료 시간(매 요일 강의 종료 예정 시간)
    CONSTRAINT `PK_curriculum_details` PRIMARY KEY (`id`)
);

CREATE TABLE `curricula`
(
    `id`             INTEGER(11)  NOT NULL AUTO_INCREMENT,       -- 강의 고유 ID
    `teacher_id`     INTEGER(11)  NULL,                          -- 강사 ID
    `title`          VARCHAR(255) NOT NULL,                      -- 강의 제목
    `curriculum_detail_id` INTEGER(11) unique,
    created_at datetime(6),
    updated_at datetime(6),
    `category`  enum ('ARTS_AND_PHYSICAL','EDUCATION','ENGINEERING','ETC','FOREIGN_LANGUAGE','KOREAN','MATH','SCIENCE') NOT NULL, -- 강의 카테고리
    CONSTRAINT `PK_curricula` PRIMARY KEY (`id`),
    CONSTRAINT `FK_curricula_teachers` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`id`) ON DELETE SET NULL,
    CONSTRAINT `FK_curricula_curriculum_detail` FOREIGN KEY (`curriculum_detail_id`) REFERENCES `curriculum_details` (`id`) ON DELETE cascade
);

CREATE TABLE `lectures`
(
    `id`            INTEGER(11)  NOT NULL AUTO_INCREMENT, -- 수업 ID
    `curriculum_id` INTEGER(11)  NOT NULL,                -- 강의 고유 ID
    `lecture_order` TINYINT(4)   NOT NULL,                -- 수업 순서(몇 번째 수업인지)
    `title`         VARCHAR(255) NOT NULL,                -- 수업 제목
    `real_start_time`    TIMESTAMP    NULL,               -- 수업 실제 시작 시간
    `real_end_time`      TIMESTAMP    NULL,               -- 수업 실제 종료 시간
    `lecture_start_date` DATETIME     NULL,               -- 수업 시작 날짜(예정된 강의 제공에 필요)
    `number_of_quizzes` TINYINT(4)   NOT NULL DEFAULT 0,  -- 퀴즈 수(수업에서 풀어야 하는 퀴즈 수)
    CONSTRAINT `PK_lectures` PRIMARY KEY (`id`),
    CONSTRAINT `FK_lectures_curricula` FOREIGN KEY (`curriculum_id`) REFERENCES `curricula` (`id`) ON DELETE CASCADE
);

CREATE TABLE `quizzes`
(
    `id`          INTEGER(11)  NOT NULL AUTO_INCREMENT, -- 퀴즈 고유 ID
    `lecture_id`  INTEGER(11)  NOT NULL,                -- 수업 고유 ID
    `quiz_number` TINYINT(4)   NOT NULL,                -- 퀴즈 번호(몇 번째 퀴즈인지)
    `question`    VARCHAR(255) NOT NULL,                -- 퀴즈 문제 내용
    CONSTRAINT `PK_quizzes` PRIMARY KEY (`id`),
    CONSTRAINT `FK_quizzes_lectures` FOREIGN KEY (`lecture_id`) REFERENCES `lectures` (`id`) ON DELETE CASCADE
);

CREATE TABLE `quiz_choices`
(
    `id`              INTEGER(11)  NOT NULL AUTO_INCREMENT, -- 퀴즈 선택지 고유 ID
    `quiz_id`         INTEGER(11)  NOT NULL,                -- 퀴즈 고유 ID
    `is_answer`       BIT(1)       NOT NULL,                -- 정답 여부(정답 : 1(true), 오답 : 0(false))
    `choice_sentence` VARCHAR(255) NOT NULL,                -- 선택지 문장(제공해야 하는 선택지들 내용)
    CONSTRAINT `PK_quiz_choices` PRIMARY KEY (`id`),
    CONSTRAINT `FK_quiz_choices_quizzes` FOREIGN KEY (`quiz_id`) REFERENCES `quizzes` (`id`) ON DELETE CASCADE
);

CREATE TABLE `students_quizzes`
(
    `student_id` INTEGER(11) NOT NULL,                     -- 학생 고유 ID
    `quiz_id`    INTEGER(11) NOT NULL,                     -- 퀴즈 고유 ID
    `score` INTEGER(11) NOT NULL,                          -- 학생이 각 퀴즈를 맞춘 점수
    `student_choice` VARCHAR(255) NOT NULL,                -- 학생이 선택한 정답지 내용
    CONSTRAINT `PK_students_quizzes` PRIMARY KEY (`student_id`, `quiz_id`),
    CONSTRAINT `FK_students_quizzes_students` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_students_quizzes_quizzes` FOREIGN KEY (`quiz_id`) REFERENCES `quizzes` (`id`) ON DELETE CASCADE
);

CREATE TABLE `students_curricula`
(
    `curriculum_id` INTEGER(11) NOT NULL,                  -- 강의 고유 ID
    `student_id`    INTEGER(11) NOT NULL,                  -- 학생 고유 ID
    CONSTRAINT `PK_students_curricula` PRIMARY KEY (`curriculum_id`, `student_id`),
    CONSTRAINT `FK_students_curricula_curricula` FOREIGN KEY (`curriculum_id`) REFERENCES `curricula` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_students_curricula_students` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE
);

CREATE TABLE `auth_codes`
(
    `auth_code`     VARCHAR(30) NOT NULL,                  -- 인증 코드 (취약계층 코드)
    `is_registered` BIT(1)      NOT NULL DEFAULT 0,        -- 등록 여부 (등록 : 1(true), 미등록 : 0(false))
    CONSTRAINT `PK_auth_codes` PRIMARY KEY (`auth_code`)
);

CREATE TABLE `lectures_students`
(
    `student_id`  INTEGER(11) NOT NULL,                    -- 학생 고유 ID
    `lecture_id`  INTEGER(11) NOT NULL,                    -- 수업 고유 ID
    `focus_ratio` DECIMAL(5, 2) NULL,                      -- 수업 참여도
    `focus_time`  SMALLINT(6) NOT NULL,                    -- 수업에 집중한 시간(분)
    `quiz_answer_count` SMALLINT(6) NOT NULL DEFAULT 0,    -- 수업에서 맞힌 총 정답 개수
    `quiz_total_score` SMALLINT(6) NOT NULL DEFAULT 0,     -- 수업에서 맞힌 총 퀴즈 점수
    CONSTRAINT `PK_lectures_students` PRIMARY KEY (`student_id`, `lecture_id`),
    CONSTRAINT `FK_lectures_students_students` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_lectures_students_lectures` FOREIGN KEY (`lecture_id`) REFERENCES `lectures` (`id`) ON DELETE CASCADE
);

CREATE TABLE `classrooms`
(
    `lecture_id` INTEGER(11) NOT NULL,                     -- 교실 고유 ID
    `session_id` VARCHAR(255) NOT NULL,                    -- 교실에 입장하기 위한 세션 ID
    CONSTRAINT `PK_classrooms_lecture_id` PRIMARY KEY (`lecture_id`),
    CONSTRAINT `FK_classrooms_lectures` FOREIGN KEY (`lecture_id`) REFERENCES `lectures` (`id`) ON DELETE CASCADE
);