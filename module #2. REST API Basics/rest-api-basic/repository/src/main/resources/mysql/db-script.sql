-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema external_lab
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `external_lab` ;

-- -----------------------------------------------------
-- Schema external_lab
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `external_lab` DEFAULT CHARACTER SET utf8 ;
USE `external_lab` ;

-- -----------------------------------------------------
-- Table `external_lab`.`tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `external_lab`.`tag` (
                                                    `id` INT NOT NULL AUTO_INCREMENT,
                                                    `name` VARCHAR(45) NOT NULL,
                                                    PRIMARY KEY (`id`),
                                                    UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
                                                    UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE);


-- -----------------------------------------------------
-- Table `external_lab`.`gift_certificate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `external_lab`.`gift_certificate` (
                                                                 `id` INT NOT NULL AUTO_INCREMENT,
                                                                 `name` VARCHAR(45) NOT NULL,
                                                                 `description` VARCHAR(255) NOT NULL,
                                                                 `price` DECIMAL(10,2) NOT NULL,
                                                                 `duration` INT NOT NULL,
                                                                 `create_date` DATETIME(3) NOT NULL,
                                                                 `last_update_date` DATETIME(3) NULL,
                                                                 UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
                                                                 UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
                                                                 PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `external_lab`.`tag_has_gift_certificate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `external_lab`.`tag_has_gift_certificate` (
                                                                         `tag_id` INT NOT NULL,
                                                                         `gift_certificate_id` INT NOT NULL,
                                                                         PRIMARY KEY (`tag_id`, `gift_certificate_id`),
                                                                         INDEX `fk_tag_has_gift_certificate_gift_certificate1_idx` (`gift_certificate_id` ASC) VISIBLE,
                                                                         INDEX `fk_tag_has_gift_certificate_tag_idx` (`tag_id` ASC) VISIBLE,
                                                                         CONSTRAINT `fk_tag_has_gift_certificate_tag`
                                                                             FOREIGN KEY (`tag_id`)
                                                                                 REFERENCES `external_lab`.`tag` (`id`)
                                                                                 ON DELETE CASCADE
                                                                                 ON UPDATE CASCADE ,
                                                                         CONSTRAINT `fk_tag_has_gift_certificate_gift_certificate1`
                                                                             FOREIGN KEY (`gift_certificate_id`)
                                                                                 REFERENCES `external_lab`.`gift_certificate` (`id`)
                                                                                 ON DELETE CASCADE
                                                                                 ON UPDATE CASCADE);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


INSERT INTO EXTERNAL_LAB.TAG (ID, NAME )
VALUES ( DEFAULT, 'java'),
       (DEFAULT , 'scala'),
       ( DEFAULT, 'c'),
       (DEFAULT , 'c-sharp'),
       ( DEFAULT, 'kotlin'),
       (DEFAULT , 'visual basic');
INSERT INTO EXTERNAL_LAB.GIFT_CERTIFICATE  (ID, NAME, DESCRIPTION , PRICE , DURATION , CREATE_DATE )
VALUES
(DEFAULT , 'jvm', 'jvm based languages', 55.0, 2, '2023-03-23 15:58:05.284'),
(DEFAULT , 'microsoft', 'monopoly', 55.0, 2, '2023-03-23 15:59:05.284'),
(DEFAULT , 'mixed', 'all-in-one', 55.0, 2, '2023-03-23 16:00:05.284'),
(DEFAULT , 'android', 'not familiar', 55.0, 2, '2023-03-23 17:58:05.284');

INSERT INTO EXTERNAL_LAB.tag_has_gift_certificate  (gift_certificate_id, tag_id )
VALUES
(1, 1),
(1, 2),
(1, 5),
(2, 3),
(2, 4),
(2, 6),
(3, 1),
(3, 4),
(3, 6),
(4, 5),
(4, 1);
