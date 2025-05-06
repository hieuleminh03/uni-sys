CREATE TABLE `tokens` (
                          `id` integer PRIMARY KEY,
                          `user_id` integer,
                          `token` string,
                          `revoked` boolean,
                          `expired` boolean
);

CREATE TABLE `accounts` (
                            `id` interger PRIMARY KEY,
                            `username` varchar(255),
                            `password` varchar(255),
                            `status` ENUM ('ACTIVE', 'ARCHIVED', 'BANNED', 'PENDING')
);

CREATE TABLE `users` (
                         `id` integer PRIMARY KEY,
                         `account_id` integer,
                         `role` ENUM ('STUDENT', 'TEACHER', 'ADMIN'),
                         `full_name` varchar(255),
                         `dob` date,
                         `gender` ENUM ('MALE', 'FEMALE', 'UNDEFINED'),
                         `phone_number` varchar(255),
                         `email` varchar(255),
                         `avatar_url` varchar(255)
);

CREATE TABLE `admins` (
                          `id` integer PRIMARY KEY,
                          `user_id` integer,
                          `note` JSON
);

CREATE TABLE `teachers` (
                            `id` integer PRIMARY KEY,
                            `user_id` integer
);

CREATE TABLE `teacher_information` (
                                       `id` integer PRIMARY KEY,
                                       `teacher_id` integer,
                                       `career_desc` JSON,
                                       `diploma_level` ENUM ('HIGH_SCHOOL', 'VOCATIONAL', 'ASSOCIATE', 'DIPLOMA', 'ADVANCED_DIPLOMA', 'BACHELOR', 'POSTGRADUATE_DIPLOMA', 'MASTER', 'DOCTORATE')
);

CREATE TABLE `students` (
                            `id` integer PRIMARY KEY,
                            `user_id` integer
);

CREATE TABLE `student_information` (
                                       `id` integer PRIMARY KEY,
                                       `student_id` integer,
                                       `ethicity` text,
                                       `id_card_number` text,
                                       `id_card_place_of_issue` text,
                                       `residence` text,
                                       `address` text,
                                       `religion` text,
                                       `mother_name` text,
                                       `mother_yob` year,
                                       `mother_phone` text,
                                       `mother_mail` text,
                                       `mother_occupation` text,
                                       `father_name` text,
                                       `father_yob` yaer,
                                       `father_phone` text,
                                       `father_mail` text,
                                       `father_occupation` text,
                                       `notes` JSON
);

CREATE TABLE `subjects` (
                            `id` integer PRIMARY KEY,
                            `name` varchar(255),
                            `code` varchar(255),
                            `description` text
);

CREATE TABLE `classes` (
                           `id` integer PRIMARY KEY,
                           `teacher_id` integer,
                           `subject_id` integer,
                           `class_name` varchar(255),
                           `start_date` date,
                           `end_date` date,
                           `description` text,
                           `study_materials` JSON,
                           `tuition` number,
                           `tuition_due_date` date,
                           `absence_warning_threshold` integer,
                           `absence_limit` integer
);

CREATE TABLE `class_schedule` (
                                  `id` integer PRIMARY KEY,
                                  `class_id` integer,
                                  `day_of_week` ENUM ('MON', 'TUES', 'WED', 'THUS', 'FRI', 'SAR', 'SUN'),
                                  `period_start` integer,
                                  `period_end` integer,
                                  `room` varchar(255)
);

CREATE TABLE `class_student` (
                                 `id` integer PRIMARY KEY,
                                 `student_id` integer,
                                 `class_id` integer,
                                 `teacher_note` JSON,
                                 `status` ENUM ('ENROLLED', 'COMPLETED', 'FAILED', 'DROPPED_OUT', 'UNPAID_EXPELLED')
);

CREATE TABLE `attendances` (
                               `id` integer PRIMARY KEY,
                               `class_id` integer,
                               `type` ENUM ('MANUAL', 'QR', 'RFID'),
                               `date` date,
                               `length_in_minute` integer
);

CREATE TABLE `attendance_hist` (
                                   `id` integer PRIMARY KEY,
                                   `attendance_id` integer,
                                   `student_id` integer,
                                   `status` ENUM ('PRESENT', 'ABSENT', 'LATE', 'EXCUSED')
);

CREATE TABLE `examinations` (
                                `id` integer PRIMARY KEY,
                                `class_id` integer,
                                `name` text,
                                `type` ENUM ('MIDTERM', 'FINAL', 'REGULAR'),
                                `status` ENUM ('PLANNED', 'ONGOING', 'GRADING', 'GRADED', 'FINALIZED', 'CANCELED'),
                                `date` date,
                                `notes` JSON
);

CREATE TABLE `student_examination` (
                                       `id` integer PRIMARY KEY,
                                       `examination_id` integer,
                                       `student_id` integer,
                                       `grade` number,
                                       `grade_status` ENUM ('NONE', 'GRADING', 'GRADED', 'REMARKING', 'FINALIZED')
);

CREATE TABLE `tuition_records` (
                                   `id` integer PRIMARY KEY,
                                   `class_student_id` integer,
                                   `status` ENUM ('UNPAID', 'PAID', 'PROCESSING'),
                                   `method` ENUM ('CASH', 'BANKING', 'SCHOLARSHIP', 'FINANCIAL_AID')
);

CREATE TABLE `homeroom` (
                            `id` interger PRIMARY KEY,
                            `name` text,
                            `teacher_id` integer
);

CREATE TABLE `homeroom_student` (
                                    `id` interger PRIMARY KEY,
                                    `student_id` integer,
                                    `homeroom_id` integer,
                                    `status` ENUM ('ANTICIPATED', 'EXPELLED', 'GRADUATED')
);

CREATE TABLE `announcements` (
                                 `id` integer PRIMARY KEY,
                                 `admin_id` integer,
                                 `date` date,
                                 `title` text,
                                 `content` text,
                                 `target` ENUM ('STUDENT', 'TEACHER', 'ADMIN'),
                                 `tags` text
);

ALTER TABLE `tokens` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `accounts` ADD FOREIGN KEY (`id`) REFERENCES `users` (`account_id`);

ALTER TABLE `users` ADD FOREIGN KEY (`id`) REFERENCES `teachers` (`user_id`);

ALTER TABLE `users` ADD FOREIGN KEY (`id`) REFERENCES `students` (`user_id`);

ALTER TABLE `users` ADD FOREIGN KEY (`id`) REFERENCES `admins` (`user_id`);

ALTER TABLE `classes` ADD FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`id`);

ALTER TABLE `classes` ADD FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`id`);

ALTER TABLE `class_schedule` ADD FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`);

ALTER TABLE `classes` ADD FOREIGN KEY (`id`) REFERENCES `class_student` (`class_id`);

ALTER TABLE `students` ADD FOREIGN KEY (`id`) REFERENCES `class_student` (`student_id`);

ALTER TABLE `attendances` ADD FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`);

ALTER TABLE `attendance_hist` ADD FOREIGN KEY (`student_id`) REFERENCES `students` (`id`);

ALTER TABLE `attendance_hist` ADD FOREIGN KEY (`attendance_id`) REFERENCES `attendances` (`id`);

ALTER TABLE `students` ADD FOREIGN KEY (`id`) REFERENCES `student_information` (`student_id`);

ALTER TABLE `teachers` ADD FOREIGN KEY (`id`) REFERENCES `teacher_information` (`teacher_id`);

ALTER TABLE `examinations` ADD FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`);

ALTER TABLE `student_examination` ADD FOREIGN KEY (`examination_id`) REFERENCES `examinations` (`id`);

ALTER TABLE `student_examination` ADD FOREIGN KEY (`student_id`) REFERENCES `students` (`id`);

ALTER TABLE `class_student` ADD FOREIGN KEY (`id`) REFERENCES `tuition_records` (`class_student_id`);

ALTER TABLE `homeroom` ADD FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`id`);

ALTER TABLE `homeroom_student` ADD FOREIGN KEY (`homeroom_id`) REFERENCES `homeroom` (`id`);

ALTER TABLE `homeroom_student` ADD FOREIGN KEY (`student_id`) REFERENCES `students` (`id`);

ALTER TABLE `announcements` ADD FOREIGN KEY (`admin_id`) REFERENCES `admins` (`id`);
