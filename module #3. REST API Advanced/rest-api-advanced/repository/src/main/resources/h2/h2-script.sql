DROP SCHEMA IF EXISTS EXTERNAL_LAB CASCADE;
CREATE SCHEMA IF NOT EXISTS EXTERNAL_LAB;
SET SCHEMA EXTERNAL_LAB;

CREATE TABLE IF NOT EXISTS EXTERNAL_LAB.TAG
(
    `id`   INT         NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NOT NULL UNIQUE,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS EXTERNAL_LAB.GIFT_CERTIFICATE
(
    `id`               INT            NOT NULL AUTO_INCREMENT,
    `name`             VARCHAR(45)    NOT NULL UNIQUE ,
    `description`      VARCHAR(255)   NOT NULL,
    `price`            DECIMAL(10, 2) NOT NULL,
    `duration`         INT            NOT NULL,
    `create_date`      DATETIME(3)    NOT NULL,
    `last_update_date` DATETIME(3)    NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS EXTERNAL_LAB.TAG_HAS_GIFT_CERTIFICATE
(
    `tag_id`              INT NOT NULL,
    `gift_certificate_id` INT NOT NULL,
    PRIMARY KEY (`tag_id`, `gift_certificate_id`),
    FOREIGN KEY (`tag_id`)
        REFERENCES EXTERNAL_LAB.TAG (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE ,
    FOREIGN KEY (`gift_certificate_id`)
        REFERENCES EXTERNAL_LAB.GIFT_CERTIFICATE (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

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
(DEFAULT , 'android', 'not familiar', 55.0, 2,'2023-03-23 17:58:05.284');

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