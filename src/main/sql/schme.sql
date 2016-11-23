-- Mysql
-- CREATE DATABASE

CREATE DATABASE accesslog_count;

-- CREATE TABLE

USE accesslog_count;

CREATE TABLE `t_count`(
  `time`  VARCHAR(15)  NOT NULL ,
  `resource` VARCHAR(240) NOT NULL ,
  `page_view`  INT   NOT NULL ,
  `user_view`  INT   NOT NULL ,
  `pv_3`     INT   NOT NULL ,
  `pv_4`     INT   NOT NULL ,
  `pv_other`  INT  NOT NULL ,
  `max_method`  VARCHAR(12)  NOT NULL ,
  `max_user`   VARCHAR(16) NOT NULL ,
  PRIMARY KEY (`time`, `resource`)
);