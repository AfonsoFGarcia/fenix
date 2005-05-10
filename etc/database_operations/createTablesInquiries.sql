CREATE TABLE INQUIRIES_COURSE (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_EXECUTION_PERIOD int(11) unsigned NOT NULL default '0',
  KEY_EXECUTION_COURSE int(11) unsigned NOT NULL default '0',
  KEY_EXECUTION_DEGREE_COURSE int(11) unsigned NOT NULL default '0',
  KEY_EXECUTION_DEGREE_STUDENT int(11) unsigned NOT NULL default '0',
  KEY_SCHOOL_CLASS int(11) unsigned default NULL,
  STUDENT_CURRICULAR_YEAR int(11) unsigned NOT NULL default '0',
  STUDENT_FIRST_ENROLLMENT smallint(1) unsigned NOT NULL default '0',
  CLASS_COORDINATION double unsigned default NULL,
  STUDY_ELEMENTS_CONTRIBUTION double unsigned default NULL,
  PREVIOUS_KNOWLEDGE_ARTICULATION double unsigned default NULL,
  CONTRIBUTION_FOR_GRADUATION double unsigned default NULL,
  EVALUATION_METHOD_ADEQUATION double unsigned default NULL,
  WEEKLY_SPENT_HOURS int(11) unsigned default NULL,
  GLOBAL_APPRECIATION double unsigned default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE INQUIRIES_REGISTRY (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_EXECUTION_PERIOD int(11) unsigned NOT NULL default '0',
  KEY_EXECUTION_COURSE int(11) unsigned NOT NULL default '0',
  KEY_EXECUTION_DEGREE_COURSE int(11) unsigned NOT NULL default '0',
  KEY_EXECUTION_DEGREE_STUDENT int(11) unsigned NOT NULL default '0',
  KEY_STUDENT int(11) unsigned NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_EXECUTION_PERIOD,KEY_EXECUTION_COURSE,KEY_EXECUTION_DEGREE_STUDENT,KEY_STUDENT)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE INQUIRIES_ROOM (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_INQUIRIES_COURSE int(11) unsigned NOT NULL default '0',
  KEY_EXECUTION_PERIOD int(11) unsigned NOT NULL default '0',
  KEY_EXECUTION_COURSE int(11) unsigned NOT NULL default '0',
  KEY_EXECUTION_DEGREE_COURSE int(11) unsigned NOT NULL default '0',
  KEY_EXECUTION_DEGREE_STUDENT int(11) unsigned NOT NULL default '0',
  KEY_ROOM int(11) unsigned NOT NULL default '0',
  SPACE_ADEQUATION int(11) unsigned default NULL,
  ENVIRONMENTAL_CONDITIONS int(11) unsigned default NULL,
  EQUIPMENT_QUALITY int(11) unsigned default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE INQUIRIES_TEACHER (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_INQUIRIES_COURSE int(11) unsigned NOT NULL default '0',
  KEY_EXECUTION_PERIOD int(11) unsigned NOT NULL default '0',
  KEY_EXECUTION_COURSE int(11) unsigned NOT NULL default '0',
  KEY_EXECUTION_DEGREE_COURSE int(11) unsigned NOT NULL default '0',
  KEY_EXECUTION_DEGREE_STUDENT int(11) unsigned NOT NULL default '0',
  KEY_TEACHER int(11) unsigned default NULL,
  KEY_NON_AFFILIATED_TEACHER int(11) unsigned default NULL,
  CLASS_TYPE int(11) unsigned NOT NULL default '0',
  STUDENT_ASSIDUITY int(11) unsigned default NULL,
  TEACHER_ASSIDUITY int(11) unsigned default NULL,
  TEACHER_PUNCTUALITY double unsigned default NULL,
  TEACHER_CLARITY double unsigned default NULL,
  TEACHER_ASSURANCE double unsigned default NULL,
  TEACHER_INTEREST_SIMULATION double unsigned default NULL,
  TEACHER_AVAILABILITY double unsigned default NULL,
  TEACHER_REASONING_STIMULATION double unsigned default NULL,
  GLOBAL_APPRECIATION double unsigned default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS OLD_INQUIRIES_COURSES_RES;

CREATE TABLE OLD_INQUIRIES_COURSES_RES (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  INQUIRY_ID int(11) unsigned NOT NULL default '0',
  GEP_EXECUTION_YEAR int(11) unsigned NOT NULL default '0',
  SEMESTER int(11) unsigned NOT NULL default '0',
  KEY_EXECUTION_PERIOD int(11) unsigned NOT NULL default '0',
  KEY_DEGREE int(11) unsigned NOT NULL default '0',
  GEP_COURSE_ID int(11) unsigned NOT NULL default '0',
  COURSE_CODE varchar(50) default NULL,
  GEP_COURSE_NAME varchar(100) default NULL,
  CURRICULAR_YEAR int(11) unsigned NOT NULL default '0',
  NUMBER_ANSWERS int(11) unsigned default NULL,
  NUMBER_ENROLLMENTS double unsigned default NULL,
  NUMBER_APPROVED double unsigned default NULL,
  NUMBER_EVALUATED double unsigned default NULL,
  REPRESENTATION_QUOTA double unsigned default NULL,
  FIRST_ENROLLMENT int(11) unsigned default NULL,
  AVERAGE_2_2 double unsigned default NULL,
  DEVIATION_2_2 double unsigned default NULL,
  TOLERANCE_2_2 double unsigned default NULL,
  N_ANSWERS_2_2 int(11) unsigned default NULL,
  AVERAGE_2_3 double unsigned default NULL,
  DEVIATION_2_3 double unsigned default NULL,
  TOLERANCE_2_3 double unsigned default NULL,
  N_ANSWERS_2_3 int(11) unsigned default NULL,
  AVERAGE_2_4 double unsigned default NULL,
  DEVIATION_2_4 double unsigned default NULL,
  TOLERANCE_2_4 double unsigned default NULL,
  N_ANSWERS_2_4 int(11) unsigned default NULL,
  AVERAGE_2_5 double unsigned default NULL,
  DEVIATION_2_5 double unsigned default NULL,
  TOLERANCE_2_5 double unsigned default NULL,
  N_ANSWERS_2_5_NUMBER int(11) unsigned default NULL,
  N_ANSWERS_2_5_TEXT int(11) unsigned default NULL,
  AVERAGE_2_6 double unsigned default NULL,
  DEVIATION_2_6 double unsigned default NULL,
  TOLERANCE_2_6 double unsigned default NULL,
  N_ANSWERS_2_6 int(11) unsigned default NULL,
  AVERAGE_2_7 varchar(255) default NULL,
  N_ANSWERS_2_7 int(11) unsigned default NULL,
  AVERAGE_2_8 double unsigned default NULL,
  DEVIATION_2_8 double unsigned default NULL,
  TOLERANCE_2_8 double unsigned default NULL,
  N_ANSWERS_2_8 int(11) unsigned default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_EXECUTION_PERIOD,KEY_DEGREE,COURSE_CODE)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS OLD_INQUIRIES_SUMMARY;

CREATE TABLE OLD_INQUIRIES_SUMMARY (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  INQUIRY_ID int(11) unsigned NOT NULL default '0',
  GEP_EXECUTION_YEAR int(11) unsigned NOT NULL default '0',
  KEY_EXECUTION_PERIOD int(11) unsigned NOT NULL default '0',
  SEMESTER int(11) unsigned NOT NULL default '0',
  KEY_DEGREE int(11) unsigned NOT NULL default '0',
  GEP_DEGREE_NAME varchar(250) default NULL,
  AVERAGE_APPRECIATION_TEACHERS double unsigned default NULL,
  AVERAGE_APPRECIATION_COURSE double unsigned default NULL,
  CURRICULAR_YEAR int(11) unsigned NOT NULL default '0',
  REPRESENTATION_QUOTA double unsigned default NULL,
  COURSE_CODE varchar(50) default NULL,
  GEP_COURSE_ID int(11) unsigned NOT NULL default '0',
  GEP_COURSE_NAME varchar(100) default NULL,
  NUMBER_ENROLLMENTS int(11) unsigned default NULL,
  NUMBER_APPROVED int(11) unsigned default NULL,
  NUMBER_EVALUATED int(11) unsigned default NULL,
  NUMBER_ANSWERS int(11) unsigned default NULL,
  ROOM_AVERAGE double unsigned default NULL,
  FIRST_ENROLLMENT double unsigned default NULL,
  AVERAGE_2_2 double unsigned default NULL,
  AVERAGE_2_3 double unsigned default NULL,
  AVERAGE_2_4 double unsigned default NULL,
  AVERAGE_2_5 double unsigned default NULL,
  AVERAGE_2_6 double unsigned default NULL,
  AVERAGE_2_7_NUMERICAL double unsigned default NULL,
  AVERAGE_2_7_INTERVAL varchar(255) default NULL,
  AVERAGE_2_8 double unsigned default NULL,
  AVERAGE_3_3_NUMERICAL double unsigned default NULL,
  AVERAGE_3_3_INTERVAL varchar(255) default NULL,
  AVERAGE_3_4_NUMERICAL double unsigned default NULL,
  AVERAGE_3_4_INTERVAL varchar(255) default NULL,
  AVERAGE_3_5 double unsigned default NULL,
  AVERAGE_3_6 double unsigned default NULL,
  AVERAGE_3_7 double unsigned default NULL,
  AVERAGE_3_8 double unsigned default NULL,
  AVERAGE_3_9 double unsigned default NULL,
  AVERAGE_3_10 double unsigned default NULL,
  AVERAGE_3_11 double unsigned default NULL,
  AVERAGE_6_1 double unsigned default NULL,
  AVERAGE_6_2 double unsigned default NULL,
  AVERAGE_6_3 double unsigned default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_EXECUTION_PERIOD,KEY_DEGREE,COURSE_CODE)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS OLD_INQUIRIES_TEACHERS_RES;

CREATE TABLE OLD_INQUIRIES_TEACHERS_RES (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  INQUIRY_ID int(11) unsigned NOT NULL default '0',
  GEP_EXECUTION_YEAR int(11) unsigned NOT NULL default '0',
  KEY_EXECUTION_PERIOD int(11) unsigned NOT NULL default '0',
  SEMESTER int(11) unsigned NOT NULL default '0',
  KEY_DEGREE int(11) unsigned NOT NULL default '0',
  CURRICULAR_YEAR int(11) unsigned NOT NULL default '0',
  GEP_COURSE_ID int(11) unsigned NOT NULL default '0',
  COURSE_CODE varchar(50) default NULL,
  GEP_COURSE_NAME varchar(100) default NULL,
  CLASS_TYPE varchar(10) default NULL,
  CLASS_TYPE_LONG varchar(50) default NULL,
  TEACHER_NUMBER int(11) unsigned NOT NULL default '0',
  KEY_TEACHER int(11) unsigned default NULL,
  KEY_RESPONSIBLE_TEACHER int(11) unsigned default NULL,
  ACTIVE varchar(50) default NULL,
  TEACHER_NAME varchar(255) default NULL,
  CATEGORY varchar(255) default NULL,
  DEPARTMENT_CODE int(11) unsigned NOT NULL default '0',
  MAIL int(11) unsigned default NULL,
  N_ANSWERS_3_3 int(11) unsigned default NULL,
  AVERAGE_3_3 varchar(255) default NULL,
  DEVIATION_3_3 double unsigned default NULL,
  TOLERANCE_3_3 double unsigned default NULL,
  N_ANSWERS_3_4 int(11) unsigned default NULL,
  AVERAGE_3_4 varchar(255) default NULL,
  DEVIATION_3_4 double unsigned default NULL,
  TOLERANCE_3_4 double unsigned default NULL,
  N_ANSWERS_3_5 int(11) unsigned default NULL,
  AVERAGE_3_5 double unsigned default NULL,
  DEVIATION_3_5 double unsigned default NULL,
  TOLERANCE_3_5 double unsigned default NULL,
  N_ANSWERS_3_6 int(11) unsigned default NULL,
  AVERAGE_3_6 double unsigned default NULL,
  DEVIATION_3_6 double unsigned default NULL,
  TOLERANCE_3_6 double unsigned default NULL,
  N_ANSWERS_3_7 int(11) unsigned default NULL,
  AVERAGE_3_7 double unsigned default NULL,
  DEVIATION_3_7 double unsigned default NULL,
  TOLERANCE_3_7 double unsigned default NULL,
  N_ANSWERS_3_8 int(11) unsigned default NULL,
  AVERAGE_3_8 double unsigned default NULL,
  DEVIATION_3_8 double unsigned default NULL,
  TOLERANCE_3_8 double unsigned default NULL,
  N_ANSWERS_3_9 int(11) unsigned default NULL,
  AVERAGE_3_9 double unsigned default NULL,
  DEVIATION_3_9 double unsigned default NULL,
  TOLERANCE_3_9 double unsigned default NULL,
  N_ANSWERS_3_10 int(11) unsigned default NULL,
  AVERAGE_3_10 double unsigned default NULL,
  DEVIATION_3_10 double unsigned default NULL,
  TOLERANCE_3_10 double unsigned default NULL,
  N_ANSWERS_3_11 int(11) unsigned default NULL,
  AVERAGE_3_11 double unsigned default NULL,
  DEVIATION_3_11 double unsigned default NULL,
  TOLERANCE_3_11 double unsigned default NULL,
  TOTAL_N_ANSWERS int(11) unsigned default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_EXECUTION_PERIOD,KEY_DEGREE,COURSE_CODE,TEACHER_NUMBER,CLASS_TYPE)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


