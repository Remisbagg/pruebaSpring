CREATE TABLE IF NOT EXISTS `user`
(
    `user_id`         BIGINT                                                               NOT NUll AUTO_INCREMENT PRIMARY KEY,
    `email`           VARCHAR(100)                                                         NOT NULL UNIQUE,
    `password`        LONGTEXT                                                             NOT NULL,
    `avatar`          LONGTEXT,
    `first_name`      VARCHAR(20),
    `last_name`       VARCHAR(20),
    `birthdate`       DATE,
    `phone_number`    VARCHAR(20),
    `current_address` LONGTEXT,
    `occupation`      LONGTEXT,
    `delete_status`   BOOLEAN                                          DEFAULT (false),
    `gender`          ENUM ('MALE','FEMALE','RATHER_NOT_TO_SAY')       DEFAULT 'RATHER_NOT_TO_SAY',
    `status`          ENUM ('MARRIED','SINGLE','RATHER_NOT_TO_SAY')    DEFAULT 'RATHER_NOT_TO_SAY',
    `role`            ENUM ('ROLE_ADMIN','ROLE_MODERATOR','ROLE_USER') DEFAULT 'ROLE_USER' NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8mb4;

CREATE TABLE IF NOT EXISTS `authentication`
(
    `code_id`      BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`      BIGINT   NOT NULL UNIQUE,
    `code`         LONGTEXT NOT NULL,
    `created_time` DATETIME NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES user (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8mb4;

CREATE TABLE IF NOT EXISTS `reset_password`
(
    `reset_id`     BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`      BIGINT       NOT NULL UNIQUE,
    `token`        VARCHAR(255) NOT NULL UNIQUE,
    `created_time` DATETIME     NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES user (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8mb4;

CREATE TABLE IF NOT EXISTS `post`
(
    `post_id`       BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`       BIGINT   NOT NULL,
    `content`       LONGTEXT,
    `delete_status` BOOLEAN  NOT NULL                   DEFAULT (false),
    `privacy`       ENUM ('PRIVATE','FRIENDS','PUBLIC') DEFAULT 'PUBLIC' NOT NULL,
    `created_time`  DATETIME NOT NULL,
    `updated_time`  DATETIME NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES user (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8mb4;

CREATE TABLE IF NOT EXISTS `post_image`
(
    `image_id` BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `url`      LONGTEXT NOT NULL,
    `post_id`  BIGINT   NOT NULL,
    FOREIGN KEY (`post_id`) REFERENCES post (`post_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8mb4;

CREATE TABLE IF NOT EXISTS `friend_request`
(
    `request_id`  BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `sender_id`   BIGINT NOT NULL,
    `receiver_id` BIGINT NOT NULL,
    FOREIGN KEY (`sender_id`) REFERENCES user (`user_id`),
    FOREIGN KEY (`receiver_id`) REFERENCES user (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8mb4;

CREATE TABLE IF NOT EXISTS `friendship`
(
    `friendship_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user1`         BIGINT NOT NULL,
    `user2`         BIGINT NOT NULL,
    `created_time`  DATE,
    FOREIGN KEY (`user1`) REFERENCES user (`user_id`),
    FOREIGN KEY (`user2`) REFERENCES user (`user_id`),
    UNIQUE (`user1`, `user2`)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8mb4;

CREATE TABLE IF NOT EXISTS `post_comment`
(
    `comment_id`    BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `post_id`       BIGINT   NOT NULL,
    `user_id`       BIGINT   NOT NULL,
    `content`       LONGTEXT,
    `created_time`  DATETIME NOT NULL,
    `updated_time`  DATETIME NOT NULL,
    `delete_status` BOOLEAN  NOT NULL DEFAULT false,
    FOREIGN KEY (`post_id`) REFERENCES post (`post_id`),
    FOREIGN KEY (`user_id`) REFERENCES user (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8mb4;

CREATE TABLE IF NOT EXISTS `post_like`
(
    `like_id`      BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `post_id`      BIGINT   NOT NULL,
    `user_id`      BIGINT   NOT NULL,
    `created_time` DATETIME NOT NULL,
    FOREIGN KEY (`post_id`) REFERENCES post (`post_id`),
    FOREIGN KEY (`user_id`) REFERENCES user (`user_id`),
    UNIQUE (post_id, user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8mb4;

Insert Into `user` (email, password)
values ('user1@email', '$2a$10$CkYKFccF/n1ZLD2sdFj5YOGN8jkfSGVtQTSs8U4geTTO0GFGfmKW6');
Insert Into `user` (email, password)
values ('user2@email', '$2a$10$CkYKFccF/n1ZLD2sdFj5YOGN8jkfSGVtQTSs8U4geTTO0GFGfmKW6');
Insert Into `user` (email, password)
values ('user3@email', '$2a$10$CkYKFccF/n1ZLD2sdFj5YOGN8jkfSGVtQTSs8U4geTTO0GFGfmKW6');

INSERT INTO `authentication` (user_id, code, created_time)
values (1, '', '2023-12-26 10:26:23');
INSERT INTO `authentication` (user_id, code, created_time)
values (2, '', '2023-12-26 10:26:23');
INSERT INTO `authentication` (user_id, code, created_time)
values (3, '', '2023-12-26 10:26:23')