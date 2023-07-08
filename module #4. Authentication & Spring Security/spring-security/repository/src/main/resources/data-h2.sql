USE external_lab;

INSERT INTO EXTERNAL_LAB.GIFT_CERTIFICATE (id, name, description, price, duration, create_date)
VALUES (DEFAULT, 'jvm', 'jvm based languages', 255.0, 2, '2023-03-23 15:58:05.284'),
       (DEFAULT, 'microsoft', 'monopoly', 255.0, 2, '2023-03-23 15:59:05.284'),
       (DEFAULT, 'mixed', 'all-in-one', 55.0, 2, '2023-03-23 16:00:05.284'),
       (DEFAULT, 'android', 'not familiar', 225.0, 2, '2023-03-23 17:58:05.284');

INSERT INTO EXTERNAL_LAB.TAG (id, name)
VALUES (DEFAULT, 'java'),
       (DEFAULT, 'scala'),
       (DEFAULT, 'c'),
       (DEFAULT, 'c-sharp'),
       (DEFAULT, 'kotlin'),
       (DEFAULT, 'visual basic');

INSERT INTO EXTERNAL_LAB.GIFT_CERTIFICATE_HAS_TAG (gift_certificate_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (1, 5),
       (2, 3),
       (2, 4),
       (2, 6),
       (3, 4),
       (3, 6),
       (4, 5);

insert into external_lab.users (email, first_name, last_name, password, user_role) values ('ebutchard0@ox.ac.uk', 'Eyde', 'Butchard', '$2a$10$CJSDqMFXSIpJ48bvn6h44.qnk/FsUl2IYBsuiwtdzdAvJCbJhERaW', 'ADMIN');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('garbuckel1@google.co.jp', 'Georgina', 'Arbuckel', '$2a$10$CJSDqMFXSIpJ48bvn6h44.qnk/FsUl2IYBsuiwtdzdAvJCbJhERaW', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('lblatchford2@rambler.ru', 'Liesa', 'Blatchford', 'Iddpw3', 'ADMIN');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('btooher3@wikispaces.com', 'Bent', 'Tooher', '4SEgIXZKaXOZ', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('mbilney4@ucoz.com', 'Martelle', 'Bilney', 'jIkAAQwsP', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('vhalbord5@washingtonpost.com', 'Vale', 'Halbord', '3Dx9KZp', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('ttodhunter6@macromedia.com', 'Theodoric', 'Todhunter', 'BiZIzGokPq', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('lianiello7@tripod.com', 'Lenci', 'Ianiello', 'w9qdwSvFb2So', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('apinwill8@census.gov', 'Algernon', 'Pinwill', 'pu1iNg', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('bmyrick9@wp.com', 'Beck', 'Myrick', 'di7uJL4Fhuwa', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('gvaggesa@upenn.edu', 'Giselbert', 'Vagges', 'xYNvdz7CFvE', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('eperrisb@freewebs.com', 'Elinore', 'Perris', 'VW8Q4sQS2EM1', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('rkullerc@toplist.cz', 'Ruthe', 'Kuller', 'Et0ygqb', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('rflatmand@multiply.com', 'Ronica', 'Flatman', 'O8Mey9b0S', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('kbayforde@chronoengine.com', 'King', 'Bayford', 'vGOEpECUp0', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('mandrichukf@ifeng.com', 'Mayor', 'Andrichuk', 'cUdDP5sjYxd', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('rwoodhallg@buzzfeed.com', 'Reggie', 'Woodhall', 'eMbvwIIY', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('kandrich@mysql.com', 'Kathrine', 'Andric', 'azhiUUalBxp', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('gelmesi@cyberchimps.com', 'Gusella', 'Elmes', 'iEnOwg9U', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('lswetlandj@stanford.edu', 'Latashia', 'Swetland', 'T0xFikyE', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('ahorneyk@imageshack.us', 'Adriana', 'Horney', 'pq3h5ufhdp9', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('rgrzegorczykl@xinhuanet.com', 'Roderick', 'Grzegorczyk', 'vRv4og', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('drenzom@google.ca', 'Darlene', 'Renzo', '0UP0T8mjJHU', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('eromainesn@cdbaby.com', 'Ebba', 'Romaines', 'Yz8vpLeDn', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('ssaddlero@sun.com', 'Shani', 'Saddler', 'RWL0Na', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('svasilkovp@china.com.cn', 'Sutherlan', 'Vasilkov', 'hsUXlWd7gqJ', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('dslowcockq@nymag.com', 'Devan', 'Slowcock', 'm74nzYn', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('evanderbekenr@dropbox.com', 'Eloisa', 'Van der Beken', 'tdr6k23J', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('lourrys@elegantthemes.com', 'Lev', 'Ourry', 'dUuNQOM5', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('mstaniont@cloudflare.com', 'Maybelle', 'Stanion', 'jI3oGR0vKvD', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('dderoosu@lycos.com', 'Darby', 'De Roos', 'yroR72kqD', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('sglendzerv@noaa.gov', 'Stanton', 'Glendzer', 'Sw58Du', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('dshillumw@vkontakte.ru', 'Derick', 'Shillum', '1Ws9WhLxtgHt', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('cdilworthx@g.co', 'Crosby', 'Dilworth', 'LiY7Iap3eagd', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('fdinnagey@dot.gov', 'Franky', 'Dinnage', 'LjyDjTh5p', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('cbrinsonz@arizona.edu', 'Conant', 'Brinson', 'IVkVul', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('ntabourin10@cmu.edu', 'Nathanil', 'Tabourin', 'nI6oNjtld', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('spountney11@biglobe.ne.jp', 'Stern', 'Pountney', 'EU8DoiHTyoa', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('nkingswoode12@sohu.com', 'Neal', 'Kingswoode', 'wbKQ7AFF1gBo', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('cwillerson13@homestead.com', 'Carolynn', 'Willerson', '8Tn8UGcMZYY', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('fbullard14@jigsy.com', 'Felipa', 'Bullard', 'CqTmv7U2w5', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('dseefeldt15@cdbaby.com', 'Dewey', 'Seefeldt', 'bqcCzvX7F', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('jgaddesby16@reverbnation.com', 'Joly', 'Gaddesby', 'QJ2UYx', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('lbavester17@mediafire.com', 'Lorne', 'Bavester', '0g2KxDJXG', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('tcrush18@springer.com', 'Tomaso', 'Crush', 'LSXhp7GVjz6', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('tbartusek19@parallels.com', 'Tate', 'Bartusek', 'DtwNlnjJl', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('sphillis1a@cpanel.net', 'Shannen', 'Phillis', 'iGSZ8nrg8M2', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('jblacksell1b@sina.com.cn', 'Jarid', 'Blacksell', 'SA3QdptIyqNl', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('jwhiston1c@netscape.com', 'Jordan', 'Whiston', 'fODYB9urUx', 'CUSTOMER');
insert into external_lab.users (email, first_name, last_name, password, user_role) values ('earnholz1d@storify.com', 'Etty', 'Arnholz', '$2a$10$Fh1nqm9sJSADGbjCgTNBSuNRKaeNLGMhVQ9pjUSP8OYOCmrNxBW5G', 'CUSTOMER');


INSERT INTO EXTERNAL_LAB.RECEIPT (ID, PRICE, CREATE_DATE, USER_ID)
VALUES (DEFAULT, 210.0, '2023-03-23 15:58:05.284', 1),
       (DEFAULT, 255.0, '2023-04-23 15:58:05.284', 1),
       (DEFAULT, 256.0, '2023-04-29 15:58:05.284', 2),
       (DEFAULT, 128.0, '2023-05-01 15:58:05.284', 3),
       (DEFAULT, 50.0, '2023-03-13 15:58:05.284', 4),
       (DEFAULT, 111.0, '2023-03-22 15:58:05.284', 3);
INSERT INTO EXTERNAL_LAB.RECEIPT_HAS_GIFT_CERTIFICATE (RECEIPT_ID, GIFT_CERTIFICATE_ID)
VALUES (1, 2),
       (1, 3),
       (2, 1),
       (2, 2),
       (2, 3),
       (3, 2),
       (4, 3),
       (4, 1),
       (4, 2),
       (5, 3),
       (6, 1),
       (6, 2),
       (6, 3),
       (6, 4);