DROP TABLE IF EXISTS USER;
CREATE TABLE USER (uid BIGINT UNSIGNED PRIMARY KEY AUtO_INCREMENT,
                   uname VARCHAR(100) NOT NULL,
                   age TINYINT DEFAULT 0,
                   login_time BIGINT DEFAULT 0,
                   ip INT DEFAULT 0)  ENGINE=InnoDB DEFAULT CHARSET=UTF8;
CREATE UNIQUE INDEX user_uname_uni_idx ON USER(uname);



DROP TABLE IF EXISTS BOKER;
CREATE TABLE BOKER (uid BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
                   beans INT UNSIGNED NOT NULL DEFAULT 0
                   )  ENGINE=InnoDB DEFAULT CHARSET=UTF8;

DROP TABLE IF EXISTS CHARGIN_RECORD;
CREATE TABLE CHARGIN_RECORD (uid BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
                       account DOUBLE UNSIGNED NOT NULL DEFAULT 0.0,
                       ctime BIGINT DEFAULT 0,
                       bank VARCHAR(50) NOT NULL)  ENGINE=InnoDB DEFAULT CHARSET=UTF8;

DROP TABLE IF EXISTS DIALOG_MSG;
CREATE TABLE DIALOG_MSG (mid BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
                       fuid BIGINT UNSIGNED NOT NULL DEFAULT 0,
                       tuid BIGINT UNSIGNED NOT NULL DEFAULT 0,
                       msg VARCHAR(1000) NOT NULL,
                       ctime BIGINT DEFAULT 0,
                       lctime BIGINT DEFAULT 0)  ENGINE=InnoDB DEFAULT CHARSET=UTF8;

DROP TABLE IF EXISTS ROOM;
CREATE TABLE ROOM (rid BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
                    title VARCHAR(1000) NOT NULL,
                    rtmpUrl VARCHAR(1000) NOT NULL,
                    ctime BIGINT DEFAULT 0)  ENGINE=InnoDB DEFAULT CHARSET=UTF8;

DROP TABLE IF EXISTS ROOM_MSG;
CREATE TABLE ROOM_MSG (mid BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
                       fuid BIGINT UNSIGNED NOT NULL DEFAULT 0,
                       rid BIGINT UNSIGNED NOT NULL DEFAULT 0,
                       msg VARCHAR(1000) NOT NULL,
                       ctime BIGINT DEFAULT 0,
                       lctime BIGINT DEFAULT 0)  ENGINE=InnoDB DEFAULT CHARSET=UTF8;