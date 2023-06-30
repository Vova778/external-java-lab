-- -----------------------------------------------------
-- Schema external_lab
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `external_lab` CASCADE;
CREATE SCHEMA IF NOT EXISTS `external_lab`;
SET SCHEMA `external_lab`;
-- -----------------------------------------------------
-- Table `external_lab`.`revinfo` for envers audit
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `external_lab`.`REVINFO`
(
       `REV`      INTEGER NOT NULL AUTO_INCREMENT,
       `REVTSTMP` BIGINT,
       PRIMARY KEY (`REV`)
);
-- -----------------------------------------------------
-- Table `external_lab`.`gift_certificate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `external_lab`.`gift_certificate`
(
       `id`               BIGINT       NOT NULL AUTO_INCREMENT,
       `name`             VARCHAR(45)  NOT NULL UNIQUE,
       `description`      VARCHAR(255) NOT NULL,
       `price`            FLOAT        NOT NULL,
       `duration`         INT          NOT NULL,
       `create_date`      DATETIME(3)  NOT NULL,
       `last_update_date` DATETIME(3)  NULL,
       PRIMARY KEY (`id`)
);
-- -----------------------------------------------------
-- Table `external_lab`.`gift_certificates_audit`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `external_lab`.`gift_certificates_AUD`
(
       `id`               BIGINT  NOT NULL,
       `name`             VARCHAR(45),
       `description`      VARCHAR(255),
       `price`            FLOAT,
       `duration`         INT,
       `create_date`      DATETIME(3),
       `last_update_date` DATETIME(3),
       `REV`              INTEGER NOT NULL,
       `REVTYPE`          TINYINT,
       PRIMARY KEY (`id`, `REV`)
);
-- -----------------------------------------------------
-- Table `external_lab`.`tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `external_lab`.`tag`
(
       `id`   BIGINT      NOT NULL AUTO_INCREMENT,
       `name` VARCHAR(45) NOT NULL UNIQUE,
       PRIMARY KEY (`id`)
);
-- -----------------------------------------------------
-- Table `external_lab`.`tags_audit`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `external_lab`.`tags_AUD`
(
       `id`      BIGINT  NOT NULL,
       `name`    VARCHAR(45),
       `REV`     INTEGER NOT NULL,
       `REVTYPE` TINYINT,
       PRIMARY KEY (`id`, `REV`)
);
-- -----------------------------------------------------
-- Table `external_lab`.`gift_certificate_has_tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `external_lab`.`gift_certificate_has_tag`
(
       `gift_certificate_id` BIGINT NOT NULL,
       `tag_id`              BIGINT NOT NULL,
       PRIMARY KEY (`gift_certificate_id`, `tag_id`),
       CONSTRAINT `fk_gift_certificate_has_tag_gift_certificate`
              FOREIGN KEY (`gift_certificate_id`)
                     REFERENCES `external_lab`.`gift_certificate` (`id`)
                     ON DELETE CASCADE
                     ON UPDATE CASCADE,
       CONSTRAINT `fk_gift_certificate_has_tag_tag`
              FOREIGN KEY (`tag_id`)
                     REFERENCES `external_lab`.`tag` (`id`)
                     ON DELETE CASCADE
                     ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Table `external_lab`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `external_lab`.`users`
(
       `id`         BIGINT       NOT NULL AUTO_INCREMENT,
       `email`      VARCHAR(255) NOT NULL UNIQUE,
       `first_name` VARCHAR(255) NOT NULL,
       `last_name`  VARCHAR(255) NOT NULL,
       `password`   VARCHAR(255) NOT NULL,
       `user_role`  ENUM('CUSTOMER', 'ADMIN') NOT NULL,
       PRIMARY KEY (`id`)
);


-- -----------------------------------------------------
-- Table `external_lab`.`receipt`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `external_lab`.`receipt`
(
       `id`          BIGINT      NOT NULL AUTO_INCREMENT,
       `price`       DOUBLE      NOT NULL,
       `create_date` DATETIME(6) NOT NULL,
       `user_id`     BIGINT      NOT NULL,
       PRIMARY KEY (`id`),
       CONSTRAINT `receipt_user_id`
              FOREIGN KEY (`user_id`)
                     REFERENCES `external_lab`.`users` (`id`)
                     ON DELETE CASCADE
                     ON UPDATE CASCADE
);
-- -----------------------------------------------------
-- Table `external_lab`.`receipts_audit`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `external_lab`.`receipts_AUD`
(
       `id`          BIGINT  NOT NULL AUTO_INCREMENT,
       `price`       DOUBLE,
       `create_date` DATETIME(6),
       `user_id`     BIGINT,
       `REV`         INTEGER NOT NULL,
       `REVTYPE`     TINYINT,
       PRIMARY KEY (`id`, `REV`)
);

-- -----------------------------------------------------
-- Table `external_lab`.`receipt_has_gift_certificate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `external_lab`.`receipt_has_gift_certificate`
(
       `gift_certificate_id` BIGINT NOT NULL,
       `receipt_id`          BIGINT NOT NULL,
       PRIMARY KEY (`gift_certificate_id`, `receipt_id`),
       CONSTRAINT `receipt_has_gift_certificate_gift_certificate_id`
              FOREIGN KEY (`gift_certificate_id`)
                     REFERENCES `external_lab`.`gift_certificate` (`id`)
                     ON DELETE CASCADE
                     ON UPDATE CASCADE,
       CONSTRAINT `receipt_has_gift_certificate_receipt_id`
              FOREIGN KEY (`receipt_id`)
                     REFERENCES `external_lab`.`receipt` (`id`)
                     ON DELETE CASCADE
                     ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Table `external_lab`.`token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `external_lab`.`token`
(
       `id`          BIGINT      NOT NULL AUTO_INCREMENT,
       `jwt`         VARCHAR(255)  NOT NULL UNIQUE ,
       `revoked`     BOOLEAN NOT NULL,
       `expired`     BOOLEAN NOT NULL,
       `user_id`     BIGINT      NOT NULL,
       PRIMARY KEY (`id`),
       CONSTRAINT `token_user_id`
              FOREIGN KEY (`user_id`)
                     REFERENCES `external_lab`.`users` (`id`)
                     ON DELETE CASCADE
                     ON UPDATE CASCADE
);
