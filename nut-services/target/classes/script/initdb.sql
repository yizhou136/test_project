DROP TABLE IF EXISTS USER;
CREATE TABLE USER (uid BIGINT UNSIGNED PRIMARY KEY AUtO_INCREMENT,
                   uname VARCHAR(100) NOT NULL,
                   age TINYINT DEFAULT 0,
                   login_time INT DEFAULT 0,
                   ip INT DEFAULT 0)  ENGINE=InnoDB DEFAULT CHARSET=UTF8;
CREATE UNIQUE INDEX user_uname_uni_idx ON USER(uname);