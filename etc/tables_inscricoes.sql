-- ==================================================================================
--  ZONA DEPARTAMENTAL
-- ==================================================================================

-- ----------------------------
--  Table structure for DEPARTMENT
-- ----------------------------
drop table if exists DEPARTMENT;
create table DEPARTMENT (
   ID_INTERNAL int(11) not null auto_increment,
   NAME varchar(50) not null,
   CODE varchar(50) not null,
   primary key (ID_INTERNAL),
   unique U1 (NAME),
   unique U2 (CODE)
) type=InnoDB;

-- ----------------------------
--  Table structure for DEPARTMENT_COURSE
-- ----------------------------
drop table if exists DEPARTMENT_COURSE;
create table DEPARTMENT_COURSE (
   ID_INTERNAL int(11) not null auto_increment,
   NAME varchar(50) not null,
   CODE varchar(50) not null,
   KEY_DEPARTMENT int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (NAME, CODE)
)type=InnoDB;


-- ==================================================================================
--  ZONA CURRICULAR
-- ==================================================================================

-- ----------------------------
--  Table structure for DEGREE
-- ----------------------------
drop table if exists DEGREE;
create table DEGREE (
   ID_INTERNAL int(11) not null auto_increment,
   CODE varchar(100) not null,
   NAME varchar(100) not null,
   TYPE_DEGREE int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (CODE),
   unique U2 (NAME, TYPE_DEGREE)
)type=InnoDB;

-- ----------------------------
--  Table structure for DEGREE_CURRICULAR_PLAN
-- ----------------------------
drop table if exists DEGREE_CURRICULAR_PLAN;
create table DEGREE_CURRICULAR_PLAN (
   ID_INTERNAL int(11) not null auto_increment,
   NAME varchar(50) not null,
   KEY_DEGREE int(11) not null,
   STATE int(11) not null,
   INITIAL_DATE date,
   END_DATE date,
   DEGREE_DURATION int(11) not null,
   MINIMAL_YEAR_FOR_OPTIONAL_COURSES int(11) not null,
   NEEDED_CREDITS float (11,2),
   MARK_TYPE integer (11),
   primary key (ID_INTERNAL),
   unique U1 (NAME, KEY_DEGREE)
)type=InnoDB;

drop table if exists BRANCH;
create table BRANCH (
   ID_INTERNAL int(11) not null auto_increment,
   BRANCH_CODE varchar(50) not null,
   BRANCH_NAME varchar(255) not null,
   KEY_DEGREE_CURRICULAR_PLAN integer(11) not null,
   primary key (ID_INTERNAL),
   index U2 (BRANCH_CODE, KEY_DEGREE_CURRICULAR_PLAN),
   unique U1 (BRANCH_CODE, KEY_DEGREE_CURRICULAR_PLAN)
)type=InnoDB;

-- ----------------------------
--  Table structure for CURRICULAR_COURSE
-- ----------------------------
drop table if exists CURRICULAR_COURSE;
create table CURRICULAR_COURSE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_DEPARTMENT_COURSE int(11),
   KEY_DEGREE_CURRICULAR_PLAN int(11) not null default '0',
   CREDITS double,
   THEORETICAL_HOURS double,
   PRATICAL_HOURS double,
   THEO_PRAT_HOURS double,
   LAB_HOURS double,
   NAME varchar(100) not null,
   CODE varchar(100) not null,
   TYPE int(11),
   EXECUTION_SCOPE int(11),
   MANDATORY bit,
   UNIVERSITY_CODE varchar(255),
   PRIMARY KEY  (ID_INTERNAL),
   UNIQUE KEY U1 (CODE, NAME, KEY_DEGREE_CURRICULAR_PLAN),
   index U2 (CODE, KEY_DEGREE_CURRICULAR_PLAN)
)type=InnoDB;

-- ----------------------------
--  Table structure for STUDENT_CURRICULAR_PLAN
-- ----------------------------
drop table if exists STUDENT_CURRICULAR_PLAN;
create table STUDENT_CURRICULAR_PLAN (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_STUDENT int(11) not null,
   KEY_DEGREE_CURRICULAR_PLAN int(11) not null,
   CURRENT_STATE int(11) not null,
   START_DATE date not null,
   KEY_BRANCH int(11) not null,
   SPECIALIZATION int(11),
   GIVEN_CREDITS float(10,2),
   primary key (ID_INTERNAL),
   unique U1 (KEY_STUDENT, KEY_DEGREE_CURRICULAR_PLAN, CURRENT_STATE, KEY_BRANCH)
)type=InnoDB;

-- ==================================================================================
--  ZONA INSCRICAO
-- ==================================================================================

-- ----------------------------
--  Table structure for ENROLMENT
-- ----------------------------
drop table if exists ENROLMENT;
create table ENROLMENT (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_STUDENT_CURRICULAR_PLAN int(11) not null,
   KEY_CURRICULAR_COURSE_SCOPE int(11) not null,
   KEY_EXECUTION_PERIOD int(11) not null,
   STATE int(11) not null,
   CLASS_NAME varchar(255) not null,
   KEY_CURRICULAR_COURSE_FOR_OPTION int (11),
   EVALUATION_TYPE int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_STUDENT_CURRICULAR_PLAN, KEY_CURRICULAR_COURSE_SCOPE, KEY_EXECUTION_PERIOD)
)type=InnoDB;

-- ----------------------------
--  Table structure for ENROLMENT_EVALUATION
-- ----------------------------
drop table if exists ENROLMENT_EVALUATION;
create table ENROLMENT_EVALUATION (
   ID_INTERNAL int(11) not null auto_increment,
   GRADE varchar(11),
   EVALUATION_TYPE int(11) not null,
   EXAM_DATE date,
   GRADE_AVAILABLE_DATE date,
   RESPONSIBLE_PERSON int(11),
   STATE int(11) not null,
   KEY_ENROLMENT int(11) not null,
   WHEN_ALTER date,
   CHECKSUM varchar(50),
   KEY_EMPLOYEE int(11),
   OBSERVATION varchar(255),
   primary key (ID_INTERNAL),
   index U2 (KEY_ENROLMENT, EVALUATION_TYPE, GRADE),
   unique U1 (GRADE, EVALUATION_TYPE, EXAM_DATE, GRADE_AVAILABLE_DATE, RESPONSIBLE_PERSON, KEY_ENROLMENT, WHEN_ALTER, KEY_EMPLOYEE, OBSERVATION)
)type=InnoDB;

drop table if exists CURRICULAR_COURSE_SCOPE;
create table CURRICULAR_COURSE_SCOPE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_CURRICULAR_SEMESTER int(11) not null,
   KEY_CURRICULAR_COURSE int(11) not null,
   KEY_BRANCH int(11) not null,
   THEORETICAL_HOURS double default '0',
   PRATICAL_HOURS double default '0',
   THEO_PRAT_HOURS double default '0',
   LAB_HOURS double default '0',
   MAX_INCREMENT_NAC int(11) not null,
   MIN_INCREMENT_NAC int(11) not null,
   WEIGTH int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_CURRICULAR_SEMESTER, KEY_CURRICULAR_COURSE, KEY_BRANCH),
   index U2 (KEY_CURRICULAR_SEMESTER, KEY_CURRICULAR_COURSE, KEY_BRANCH)
)type=InnoDB;


-- ----------------------------
--  Table structure for ENROLMENT_EQUIVALENCE
-- ----------------------------
drop table if exists ENROLMENT_EQUIVALENCE;
create table ENROLMENT_EQUIVALENCE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_EQUIVALENT_ENROLMENT int(11) not null,
   KEY_ENROLMENT int(11) not null,
   EQUIVALENCE_TYPE int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_EQUIVALENT_ENROLMENT, KEY_ENROLMENT)
)type=InnoDB;

-- ----------------------------
--  Table structure for POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE
-- ----------------------------
drop table if exists POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE;
create table POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_POSSIBLE_CURRICULAR_COURSE int(11) not null,
   KEY_OPTIONAL_CURRICULAR_COURSE int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_POSSIBLE_CURRICULAR_COURSE, KEY_OPTIONAL_CURRICULAR_COURSE)
)type=InnoDB;

-- --------------------------------------------
--  Table structure for STUDENT_KIND
-- --------------------------------------------
drop table if exists STUDENT_KIND;
create table STUDENT_KIND (
   ID_INTERNAL int(11) not null auto_increment,
   STUDENT_TYPE int(11) not null,
   MIN_COURSES_TO_ENROL int(11) not null,
   MAX_COURSES_TO_ENROL int(11) not null,
   MAX_NAC_TO_ENROL int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (STUDENT_TYPE)
)type=InnoDB;

-- ==================================================================================
--  ZONA TABELAS REPETIDAS
-- ==================================================================================

-- ----------------------------
--  Table structure for PRECEDENCE
-- ----------------------------
drop table if exists PRECEDENCE;
create table PRECEDENCE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_CURRICULAR_COURSE int(11) not null,
   SCOPE_TO_APPLY varchar(3) not null,
   primary key (ID_INTERNAL))
   type=InnoDB;

-- ----------------------------
--  Table structure for RESTRICTION
-- ----------------------------
DROP TABLE IF EXISTS RESTRICTION;
CREATE TABLE RESTRICTION (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  CLASS_NAME varchar(250) NOT NULL,
  KEY_PRECEDENCE int(11) not null,
  KEY_CURRICULAR_COURSE int (11),
  NUMBER_OF_CURRICULAR_COURSE_DONE int(11),
  primary key (ID_INTERNAL)) TYPE=InnoDB;

#----------------------------
# Table structure for EXECUTION_PERIOD
# State : A = Actual; O= Open; NO= Not open; C= Closed
#----------------------------
drop table if exists EXECUTION_PERIOD;
create table EXECUTION_PERIOD (
   ID_INTERNAL int(11) not null auto_increment,
   NAME varchar(50) not null,
   KEY_EXECUTION_YEAR int(11) not null,
   STATE varchar(3) not null default "NO",
   SEMESTER int (11) not null,
   primary key (ID_INTERNAL),
   unique U1 (NAME, KEY_EXECUTION_YEAR))
   type=InnoDB comment="InnoDB free: 373760 kB";
   
   
#----------------------------
# Table structure for EXECUTION_YEAR
#----------------------------
drop table if exists EXECUTION_YEAR;
create table EXECUTION_YEAR (
   ID_INTERNAL int(11) not null auto_increment,
   `YEAR` varchar(9) not null,
   STATE varchar(3) not null default "NO",   
   primary key (ID_INTERNAL),
   unique U1 (`YEAR`))
   type=InnoDB comment="InnoDB free: 373760 kB";

-- ----------------------------
--  Table structure for CURRICULAR_COURSE_EQUIVALENCE
-- ----------------------------
drop table if exists CURRICULAR_COURSE_EQUIVALENCE;
create table CURRICULAR_COURSE_EQUIVALENCE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_EQUIVALENT_CURRICULAR_COURSE int(11) not null,
   KEY_CURRICULAR_COURSE int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_EQUIVALENT_CURRICULAR_COURSE, KEY_CURRICULAR_COURSE)
)type=InnoDB;

-- ----------------------------
-- Table struture for ENROLMENT_PERIOD
-- ----------------------------
drop table if exists ENROLMENT_PERIOD;
create table ENROLMENT_PERIOD (
	ID_INTERNAL int(11) not null auto_increment,
	KEY_DEGREE_CURRICULAR_PLAN int(11) not null,
	KEY_EXECUTION_PERIOD int(11) not null,
	START_DATE date not null,
	END_DATE date not null,
	primary key (ID_INTERNAL),
	unique U1 (KEY_DEGREE_CURRICULAR_PLAN, KEY_EXECUTION_PERIOD)
)type=InnoDB;

-- ----------------------------
--  Table structure for CURRICULAR_YEAR
-- ----------------------------
drop table if exists CURRICULAR_YEAR;
create table CURRICULAR_YEAR (
   ID_INTERNAL int(11) not null auto_increment,
   YEAR int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (YEAR)
)type=InnoDB;

-- ----------------------------
--  Table structure for CURRICULAR_SEMESTER
-- ----------------------------
drop table if exists CURRICULAR_SEMESTER;
create table CURRICULAR_SEMESTER (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_CURRICULAR_YEAR int(11) not null,
   SEMESTER int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (SEMESTER, KEY_CURRICULAR_YEAR)
)type=InnoDB;
