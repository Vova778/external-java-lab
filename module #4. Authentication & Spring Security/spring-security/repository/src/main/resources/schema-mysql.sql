-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema external_lab
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS external_lab;
CREATE SCHEMA IF NOT EXISTS `external_lab` DEFAULT CHARACTER SET utf8mb3;
USE `external_lab`;
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
    `duration`         INT          NOT NULL,
    `price`            DOUBLE       NOT NULL,
    `create_date`      DATETIME(6)  NOT NULL,
    `id`               BIGINT       NOT NULL AUTO_INCREMENT,
    `last_update_date` DATETIME(6)  NULL DEFAULT NULL,
    `description`      VARCHAR(255) NOT NULL,
    `name`             VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 5
    DEFAULT CHARACTER SET = utf8mb3;
-- -----------------------------------------------------
-- Table `external_lab`.`gift_certificates_audit`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `external_lab`.`gift_certificates_AUD`
(
    `id`               BIGINT       NOT NULL,
    `name`             VARCHAR(45)  ,
    `description`      VARCHAR(255) ,
    `price`            FLOAT        ,
    `duration`         INT          ,
    `create_date`      DATETIME(3)  ,
    `last_update_date` DATETIME(3)  ,
    `REV`              INTEGER      NOT NULL,
    `REVTYPE`          TINYINT,
    PRIMARY KEY (`id`, `REV`)
);
-- -----------------------------------------------------
-- Table `external_lab`.`tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `external_lab`.`tag`
(
    `id`   BIGINT       NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 31
    DEFAULT CHARACTER SET = utf8mb3;
-- -----------------------------------------------------
-- Table `external_lab`.`tags_audit`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `external_lab`.`tags_AUD`
(
    `id`      BIGINT      NOT NULL,
    `name`    VARCHAR(45) ,
    `REV`     INTEGER     NOT NULL,
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
    INDEX `fk_gift_certificate_has_tag_gift_certificate_idx` (`gift_certificate_id` ASC) VISIBLE,
    INDEX `fk_gift_certificate_has_tag_tag_idx` (`tag_id` ASC) VISIBLE,
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
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `external_lab`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `external_lab`.user
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `email`      VARCHAR(255) NOT NULL,
    `first_name` VARCHAR(255) NOT NULL,
    `last_name`  VARCHAR(255) NOT NULL,
    `password`   VARCHAR(255) NOT NULL,
    `user_role`  ENUM('CUSTOMER', 'ADMIN') NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 11
    DEFAULT CHARACTER SET = utf8mb3;


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
    INDEX `user_id_UNIQUE` (`user_id` ASC) VISIBLE,
    CONSTRAINT `receipt_user_id`
        FOREIGN KEY (`user_id`)
            REFERENCES `external_lab`.user (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;
-- -----------------------------------------------------
-- Table `external_lab`.`receipts_audit`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `external_lab`.`receipts_AUD`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT,
    `price`       DOUBLE      ,
    `create_date` DATETIME(6) ,
    `REV`         INTEGER     NOT NULL,
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
    INDEX `receipt_id_UNIQUE` (`receipt_id` ASC) VISIBLE,
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
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;

SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;