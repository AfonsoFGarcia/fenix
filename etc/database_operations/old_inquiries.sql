# MySQL-Front 3.1  (Build 14.6)


# Host: localhost    Database: ciapl
# ------------------------------------------------------
# Server version 4.0.21-nt

#
# Table structure for table OLD_INQUIRIES_SUMMARY
#

DROP TABLE IF EXISTS `OLD_INQUIRIES_SUMMARY`;

CREATE TABLE `OLD_INQUIRIES_SUMMARY` (
  `ID_INTERNAL` int(4) NOT NULL auto_increment,
  `INQUIRY_ID` int(4) unsigned NOT NULL default '0',
  `GEP_EXECUTION_YEAR` int(4) NOT NULL default '0',
  `KEY_EXECUTION_PERIOD` int(4) NOT NULL default '0',
  `SEMESTER` int(1) NOT NULL default '0',
  `KEY_DEGREE` int(2) NOT NULL default '0',
  `GEP_DEGREE_NAME` varchar(250) default NULL,
  `AVERAGE_APPRECIATION_TEACHERS` double default NULL,
  `AVERAGE_APPRECIATION_COURSE` double default NULL,
  `CURRICULAR_YEAR` int(1) NOT NULL default '0',
  `REPRESENTATION_QUOTA` double default NULL,
  `COURSE_CODE` varchar(50) default NULL,
  `KEY_CURRICULAR_COURSE` int(4) default NULL,
  `GEP_COURSE_ID` int(4) NOT NULL default '0',
  `GEP_COURSE_NAME` varchar(100) default NULL,
  `NUMBER_ENROLLMENTS` int(4) default NULL,
  `NUMBER_APPROVED` int(4) default NULL,
  `NUMBER_EVALUATED` int(4) default NULL,
  `NUMBER_ANSWERS` int(4) default NULL,
  `ROOM_AVERAGE` double default NULL,
  `FIRST_ENROLLMENT` double default NULL,
  `AVERAGE_2_2` double default NULL,
  `AVERAGE_2_3` double default NULL,
  `AVERAGE_2_4` double default NULL,
  `AVERAGE_2_5` double default NULL,
  `AVERAGE_2_6` double default NULL,
  `AVERAGE_2_7_NUMERICAL` double default NULL,
  `AVERAGE_2_7_INTERVAL` varchar(255) default NULL,
  `AVERAGE_2_8` double default NULL,
  `AVERAGE_3_3_NUMERICAL` double default NULL,
  `AVERAGE_3_3_INTERVAL` varchar(255) default NULL,
  `AVERAGE_3_4_NUMERICAL` double default NULL,
  `AVERAGE_3_4_INTERVAL` varchar(255) default NULL,
  `AVERAGE_3_5` double default NULL,
  `AVERAGE_3_6` double default NULL,
  `AVERAGE_3_7` double default NULL,
  `AVERAGE_3_8` double default NULL,
  `AVERAGE_3_9` double default NULL,
  `AVERAGE_3_10` double default NULL,
  `AVERAGE_3_11` double default NULL,
  `AVERAGE_6_1` double default NULL,
  `AVERAGE_6_2` double default NULL,
  `AVERAGE_6_3` double default NULL,
  PRIMARY KEY  (`ID_INTERNAL`),
  UNIQUE KEY `U1` (`KEY_EXECUTION_PERIOD`,`KEY_DEGREE`,`COURSE_CODE`)
) TYPE=InnoDB;


#
# Table structure for table OLD_INQUIRIES_TEACHERS_RES
#

DROP TABLE IF EXISTS `OLD_INQUIRIES_TEACHERS_RES`;

CREATE TABLE `OLD_INQUIRIES_TEACHERS_RES` (
  `ID_INTERNAL` int(4) NOT NULL auto_increment,
  `INQUIRY_ID` int(4) unsigned NOT NULL default '0',
  `GEP_EXECUTION_YEAR` int(4) NOT NULL default '0',
  `KEY_EXECUTION_PERIOD` int(4) NOT NULL default '0',
  `SEMESTER` int(1) NOT NULL default '0',
  `KEY_DEGREE` int(2) NOT NULL default '0',
  `CURRICULAR_YEAR` int(1) NOT NULL default '0',
  `GEP_COURSE_ID` int(4) NOT NULL default '0',
  `COURSE_CODE` varchar(50) default NULL,
  `KEY_CURRICULAR_COURSE` int(4) default NULL,
  `GEP_COURSE_NAME` varchar(100) default NULL,
  `CLASS_TYPE` varchar(10) default NULL,
  `CLASS_TYPE_LONG` varchar(50) default NULL,
  `TEACHER_NUMBER` int(4) NOT NULL default '0',
  `KEY_TEACHER` int(4) default NULL,
  `ACTIVE` varchar(50) default NULL,
  `TEACHER_NAME` varchar(255) default NULL,
  `CATEGORY` varchar(255) default NULL,
  `DEPARTMENT_CODE` int(4) NOT NULL default '0',
  `MAIL` int(4) default NULL,
  `N_ANSWERS_3_3` int(4) default NULL,
  `AVERAGE_3_3` varchar(255) default NULL,
  `DEVIATION_3_3` double default NULL,
  `TOLERANCE_3_3` double default NULL,
  `N_ANSWERS_3_4` int(4) default NULL,
  `AVERAGE_3_4` varchar(255) default NULL,
  `DEVIATION_3_4` double default NULL,
  `TOLERANCE_3_4` double default NULL,
  `N_ANSWERS_3_5` int(4) default NULL,
  `AVERAGE_3_5` double default NULL,
  `DEVIATION_3_5` double default NULL,
  `TOLERANCE_3_5` double default NULL,
  `N_ANSWERS_3_6` int(4) default NULL,
  `AVERAGE_3_6` double default NULL,
  `DEVIATION_3_6` double default NULL,
  `TOLERANCE_3_6` double default NULL,
  `N_ANSWERS_3_7` int(4) default NULL,
  `AVERAGE_3_7` double default NULL,
  `DEVIATION_3_7` double default NULL,
  `TOLERANCE_3_7` double default NULL,
  `N_ANSWERS_3_8` int(4) default NULL,
  `AVERAGE_3_8` double default NULL,
  `DEVIATION_3_8` double default NULL,
  `TOLERANCE_3_8` double default NULL,
  `N_ANSWERS_3_9` int(4) default NULL,
  `AVERAGE_3_9` double default NULL,
  `DEVIATION_3_9` double default NULL,
  `TOLERANCE_3_9` double default NULL,
  `N_ANSWERS_3_10` int(4) default NULL,
  `AVERAGE_3_10` double default NULL,
  `DEVIATION_3_10` double default NULL,
  `TOLERANCE_3_10` double default NULL,
  `N_ANSWERS_3_11` int(4) default NULL,
  `AVERAGE_3_11` double default NULL,
  `DEVIATION_3_11` double default NULL,
  `TOLERANCE_3_11` double default NULL,
  `TOTAL_N_ANSWERS` int(4) default NULL,
  PRIMARY KEY  (`ID_INTERNAL`),
  UNIQUE KEY `U1` (`KEY_EXECUTION_PERIOD`,`KEY_DEGREE`,`COURSE_CODE`,`TEACHER_NUMBER`,`CLASS_TYPE`)
) TYPE=InnoDB;

