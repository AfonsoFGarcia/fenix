#==================================================================================
# ZONA DEPARTAMENTAL
#==================================================================================

#----------------------------
# Table structure for DEPARTMENT
#----------------------------
drop table if exists DEPARTMENT;
create table DEPARTMENT (
   ID_INTERNAL int(11) not null auto_increment,
   NAME varchar(50) not null,
   CODE varchar(50) not null,
   primary key (ID_INTERNAL),
   unique U1 (NAME),
   unique U2 (CODE)
) type=InnoDB;

#----------------------------
# Table structure for DEPARTMENT_COURSE
#----------------------------
drop table if exists DEPARTMENT_COURSE;
create table DEPARTMENT_COURSE (
   ID_INTERNAL int(11) not null auto_increment,
   NAME varchar(50) not null,
   CODE varchar(50) not null,
   KEY_DEPARTMENT int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (NAME, CODE)
)type=InnoDB;


#==================================================================================
# ZONA CURRICULAR
#==================================================================================

#----------------------------
# Table structure for DEGREE
#----------------------------
drop table if exists DEGREE;
create table DEGREE (
   ID_INTERNAL int(11) not null auto_increment,
   CODE varchar(100) not null,
   NAME varchar(100) not null,
   TYPE_DEGREE int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (CODE),
   unique U2 (NAME)
)type=InnoDB;

#----------------------------
# Table structure for DEGREE_CURRICULAR_PLAN
#----------------------------
drop table if exists DEGREE_CURRICULAR_PLAN;
create table DEGREE_CURRICULAR_PLAN (
   ID_INTERNAL int(11) not null auto_increment,
   NAME varchar(50) not null,
   KEY_DEGREE int(11) not null,
   STATE int(11),
   INITIAL_DATE date,
   END_DATE date,
   primary key (ID_INTERNAL),
   unique U1 (NAME, KEY_DEGREE)
)type=InnoDB;

#------------------------------------------
# Table structure for curricular_course
# key_department should be not null
#   ainda n�o se tem a disciplina departamento
#------------------------------------------
#drop table if exists CURRICULAR_COURSE;
#create table CURRICULAR_COURSE (
#   ID_INTERNAL int(11) not null auto_increment,
#   KEY_DEPARTMENT_COURSE int(11),
#   KEY_DEGREE_CURRICULAR_PLAN int(11) not null,
#   CREDITS double,
#   THEORETICAL_HOURS double,
#   PRATICAL_HOURS double,
#   THEO_PRAT_HOURS double,
#   LAB_HOURS double,
#   CURRICULAR_YEAR int(11),
#   SEMESTER int(11),
#   NAME varchar(100),
#   CODE varchar(50) not null,
#   primary key (ID_INTERNAL ),
#   unique U1 (CODE, NAME, SEMESTER, CURRICULAR_YEAR,  KEY_DEGREE_CURRICULAR_PLAN))
#   type=InnoDB;

#----------------------------
# Table structure for CURRICULAR_COURSE
#----------------------------
drop table if exists CURRICULAR_COURSE;
create table CURRICULAR_COURSE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_DEPARTMENT_COURSE int(11) not null default '0',
   KEY_DEGREE_CURRICULAR_PLAN int(11) not null default '0',
   CREDITS double,
   THEORETICAL_HOURS double,
   PRATICAL_HOURS double,
   THEO_PRAT_HOURS double,
   LAB_HOURS double,
   NAME varchar(100) not null,
   CODE varchar(50) not null,
   PRIMARY KEY  (ID_INTERNAL),
   UNIQUE KEY U1 (CODE, NAME, KEY_DEGREE_CURRICULAR_PLAN)
)type=InnoDB;

#----------------------------
# Table structure for STUDENT_CURRICULAR_PLAN
#----------------------------
drop table if exists STUDENT_CURRICULAR_PLAN;
create table STUDENT_CURRICULAR_PLAN (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_STUDENT int(11) not null,
   KEY_DEGREE_CURRICULAR_PLAN int(11) not null,
   CURRENT_STATE int(11) not null,
   START_DATE date not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_STUDENT, KEY_DEGREE_CURRICULAR_PLAN, CURRENT_STATE)
)type=InnoDB;
#  KEY_COURSE_CURRICULAR_PLAN int(11) not null,

#==================================================================================
# ZONA INSCRICAO
#==================================================================================

#----------------------------
# Table structure for ENROLMENT
#----------------------------
drop table if exists ENROLMENT;
create table ENROLMENT (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_STUDENT_CURRICULAR_PLAN int(11) not null,
   KEY_CURRICULAR_COURSE int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_STUDENT_CURRICULAR_PLAN, KEY_CURRICULAR_COURSE)
)type=InnoDB;

#==================================================================================
# ZONA TABELAS NOVAS
#==================================================================================

#----------------------------
# Table structure for CURRICULAR_YEAR
#----------------------------
drop table if exists CURRICULAR_YEAR;
create table CURRICULAR_YEAR (
   ID_INTERNAL int(11) not null auto_increment,
   YEAR int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (YEAR)
)type=InnoDB;

#----------------------------
# Table structure for CURRICULAR_SEMESTER
#----------------------------
drop table if exists CURRICULAR_SEMESTER;
create table CURRICULAR_SEMESTER (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_CURRICULAR_YEAR int(11) not null,
   SEMESTER int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (SEMESTER, KEY_CURRICULAR_YEAR)
)type=InnoDB;

#----------------------------
# Table structure for BRANCH
#----------------------------
drop table if exists BRANCH;
create table BRANCH (
   ID_INTERNAL int(11) not null auto_increment,
   BRANCH_CODE varchar(50) not null,
   BRANCH_NAME varchar(255),
   primary key (ID_INTERNAL),
   unique U1 (BRANCH_NAME, BRANCH_CODE)
)type=InnoDB;

#==================================================================================
# ZONA TABELAS DE RELACAO MUITOS PARA MUITOS
#==================================================================================

#----------------------------
# Table structure for CURRICULAR_COURSE_CURRICULAR_SEMESTER
#----------------------------
drop table if exists CURRICULAR_COURSE_CURRICULAR_SEMESTER;
create table CURRICULAR_COURSE_CURRICULAR_SEMESTER (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_CURRICULAR_SEMESTER int(11) not null,
   KEY_CURRICULAR_COURSE int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_CURRICULAR_SEMESTER, KEY_CURRICULAR_COURSE)
)type=InnoDB;

#----------------------------
# Table structure for CURRICULAR_COURSE_BRANCH
#----------------------------
drop table if exists CURRICULAR_COURSE_BRANCH;
create table CURRICULAR_COURSE_BRANCH (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_BRANCH int(11) not null,
   KEY_CURRICULAR_COURSE int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_BRANCH, KEY_CURRICULAR_COURSE)
)type=InnoDB;

#----------------------------
# Table structure for STUDENT_CURRICULAR_PLAN_BRANCH
#----------------------------
drop table if exists STUDENT_CURRICULAR_PLAN_BRANCH;
create table STUDENT_CURRICULAR_PLAN_BRANCH (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_BRANCH int(11) not null,
   KEY_STUDENT_CURRICULAR_PLAN int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_BRANCH, KEY_STUDENT_CURRICULAR_PLAN)
)type=InnoDB;

#----------------------------
# Table structure for EQUIVALENCE
#----------------------------
drop table if exists EQUIVALENCE;
create table EQUIVALENCE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_EQUIVALENT_ENROLMENT int(11) not null,
   KEY_ENROLMENT int(11) not null,
   EQUIVALENCE_TYPE int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_EQUIVALENT_ENROLMENT, KEY_ENROLMENT)
)type=InnoDB;

#----------------------------
# Table structure for PRECEDENCE
#----------------------------
drop table if exists PRECEDENCE;
create table PRECEDENCE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_CURRICULAR_COURSE int(11) not null,
   KEY_PRECEDING_CURRICULAR_COURSE int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_CURRICULAR_COURSE, KEY_PRECEDING_CURRICULAR_COURSE)
)type=InnoDB;

