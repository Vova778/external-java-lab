-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema external_lab_module_2
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema external_lab_module_2
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `external_lab_module_2` DEFAULT CHARACTER SET utf8 ;
USE `external_lab_module_2` ;

-- -----------------------------------------------------
-- Table `external_lab_module_2`.`gift_certificate`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `external_lab_module_2`.`gift_certificate` ;

CREATE TABLE IF NOT EXISTS `external_lab_module_2`.`gift_certificate` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  `price` DECIMAL(9,2) NOT NULL,
  `duration` INT NOT NULL,
  `create_date` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update_date` DATETIME(3) NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `external_lab_module_2`.`tag`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `external_lab_module_2`.`tag` ;

CREATE TABLE IF NOT EXISTS `external_lab_module_2`.`tag` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `external_lab_module_2`.`tag_has_gift_certificate`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `external_lab_module_2`.`tag_has_gift_certificate` ;

CREATE TABLE IF NOT EXISTS `external_lab_module_2`.`tag_has_gift_certificate` (
  `tag_id` INT NOT NULL,
  `gift_certificate_id` INT NOT NULL,
  PRIMARY KEY (`tag_id`, `gift_certificate_id`),
  INDEX `fk_tag_has_gift_certificate_gift_certificate1_idx` (`gift_certificate_id` ASC) VISIBLE,
  INDEX `fk_tag_has_gift_certificate_tag_idx` (`tag_id` ASC) VISIBLE,
  CONSTRAINT `fk_tag_has_gift_certificate_tag`
    FOREIGN KEY (`tag_id`)
    REFERENCES `external_lab_module_2`.`tag` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tag_has_gift_certificate_gift_certificate1`
    FOREIGN KEY (`gift_certificate_id`)
    REFERENCES `external_lab_module_2`.`gift_certificate` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
