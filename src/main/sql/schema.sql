-- Mysql
-- CREATE DATABASE

CREATE DATABASE db_summary;

-- CREATE TABLE

USE accesslog_count;

CREATE TABLE `t_summary`(
  `aid`         BIGINT           NOT NULL  AUTO_INCREMENT,
  `time`        DATETIME      NOT NULL  COMMENT 'TimeDuration',
  `resource`    VARCHAR(240)  NOT NULL  COMMENT 'ResourceUrl',
  `page_view`   INT           NOT NULL  COMMENT 'PageViewCount',
  `user_view`   INT           NOT NULL  COMMENT 'UserViewCount',
  `pv_3`        INT           NOT NULL  COMMENT 'State 3xx PV',
  `pv_4`        INT           NOT NULL  COMMENT 'State 4xx PV',
  `pv_other`    INT           NOT NULL  COMMENT 'State Other Pv',
  `max_method`  VARCHAR(12)   NOT NULL  COMMENT 'MaxCount Method',
  `max_user`    VARCHAR(16)   NOT NULL  COMMENT 'MaxCount User',
  PRIMARY KEY (`aid`),
  INDEX time_index(`time`)
);

/*

use accesslog_count;
set @@sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
-- count by hour, need year month and day
SELECT B.time, sum(B.page_view)PV, sum(B.user_view)UV,
(select resource from t_count where time = B.time group by resource order by sum(page_view) desc limit 0,1)resource,
(select concat(max_user,' ',max_method) from t_count where time = B.time group by max_user order by count(1) desc limit 0,1)`max`
FROM t_count B
where year(B.time) = '2016' and month(B.time)='10' and day(B.time)='1'
group by B.time
;
-- count by day, need year and month
SELECT date(B.time)`time`, sum(B.page_view)PV, sum(B.user_view)UV,
(select resource from t_count where date(time) = date(B.time) group by resource order by sum(page_view) desc limit 0,1)resource,
(select concat(max_user,' ',max_method) from t_count where date(time) = date(B.time) group by max_user order by count(1) desc limit 0,1)`max`
FROM t_count B
where year(B.time) = '2016' and month(B.time)='10'
group by date(B.time)
;
-- count by month, need year
SELECT month(B.time)`time`, sum(B.page_view)PV, sum(B.user_view)UV,
(select resource from t_count where year(time)=year(B.time) and month(time) = month(B.time) group by resource order by sum(page_view) desc limit 0,1)resource,
(select concat(max_user,' ',max_method) from t_count where year(time)=year(B.time) and month(time) = month(B.time) group by max_user order by count(1) desc limit 0,1)`max`
FROM t_count B
where year(B.time) = '2016'
group by year(B.time), month(B.time)
;

 */
