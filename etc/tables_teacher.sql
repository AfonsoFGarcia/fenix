-----------------------------
-- Table structure for CAREER
-----------------------------
drop table if exists CAREER;
create table CAREER (
   ID_INTERNAL integer(11) not null auto_increment,
   CLASS_NAME varchar(250) not null,
   BEGIN_YEAR integer(10),
   END_YEAR integer(10),
   LAST_MODIFICATION_DATE timestamp(14) not null,
   KEY_TEACHER integer(11) not null,
   ENTITY varchar(50),
   FUNCTION varchar(50),
   COURSE_OR_POSITION varchar(100),
   KEY_CATEGORY integer(11),
   primary key (ID_INTERNAL))
   type=InnoDB;

-----------------------------
-- Table structure for WEEKLY_OCUPATION
-----------------------------
drop table if exists WEEKLY_OCUPATION;
create table WEEKLY_OCUPATION (
   ID_INTERNAL integer(11) not null auto_increment,
   RESEARCH integer(10),
   MANAGEMENT integer(10),
   LECTURE integer(10),
   SUPPORT integer(10),
   OTHER integer(10),
   LAST_MODIFICATION_DATE timestamp(14) not null,
   KEY_TEACHER integer(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_TEACHER))
   type=InnoDB;

-----------------------------
-- Table structure for EXTERNAL_ACTIVITY
-----------------------------
drop table if exists EXTERNAL_ACTIVITY;
create table EXTERNAL_ACTIVITY (
	ID_INTERNAL integer(11) not null auto_increment,
	ACTIVITY text,
	LAST_MODIFICATION_DATE timestamp(14) not null,
	KEY_TEACHER integer(11) not null,
	primary key (ID_INTERNAL))
	type=InnoDB;
	
-----------------------------
-- Table structure for SERVICE_PROVIDER_REGIME
-----------------------------
drop table if exists SERVICE_PROVIDER_REGIME;
create table SERVICE_PROVIDER_REGIME (
	ID_INTERNAL integer(11) not null auto_increment,
	PROVIDER_REGIME_TYPE integer(10),
	LAST_MODIFICATION_DATE timestamp(14) not null,
	KEY_TEACHER integer(11) not null,
	primary key (ID_INTERNAL),
	unique U1 (KEY_TEACHER))
	type=InnoDB;
	
-----------------------------
-- Table structure for ORIENTATION
-----------------------------
drop table if exists ORIENTATION;
create table ORIENTATION (
	ID_INTERNAL integer(11) not null auto_increment,
	ORIENTATION_TYPE integer(10),
	LAST_MODIFICATION_DATE timestamp(14) not null,
	NUMBER_OF_STUDENTS integer(10),
	DESCRIPTION text,
	KEY_TEACHER integer(11) not null,
	primary key (ID_INTERNAL),
	unique U1 (KEY_TEACHER, ORIENTATION_TYPE))
	type=InnoDB;

-----------------------------
-- Table structure for PUBLICATIONS_NUMBER
-----------------------------
drop table if exists PUBLICATIONS_NUMBER;
create table PUBLICATIONS_NUMBER (
	ID_INTERNAL integer(11) not null auto_increment,
	PUBLICATION_TYPE integer(10),
	LAST_MODIFICATION_DATE timestamp(14) not null,
	NATIONAL integer(10),
	INTERNATIONAL integer(10),
	KEY_TEACHER integer(11) not null,
	primary key (ID_INTERNAL),
	unique U1 (KEY_TEACHER, PUBLICATION_TYPE))
	type=InnoDB;
