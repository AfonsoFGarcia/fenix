#---------------------------------------------------------------
# Table structure for person
#---------------------------------------------------------------
drop table if exists PERSON;
create table PERSON (
   ID_INTERNAL integer(11) not null auto_increment,
   DOCUMENT_ID_NUMBER varchar(50) not null,
   EMISSION_LOCATION_OF_DOCUMENT_ID varchar(100),
   EMISSION_DATE_OF_DOCUMENT_ID date,
   EXPERATION_DATE_OF_DOCUMENT_ID date,
   NAME varchar(100),
   DATE_OF_BIRTH date,
   NAME_OF_FATHER varchar(100),
   NAME_OF_MOTHER varchar(100),
   NATIONALITY varchar(50),
   PARISH_OF_BIRTH varchar(100),
   DISTRICT_SUBDIVISION_OF_BIRTH varchar(100),
   DISTRICT_OF_BIRTH varchar(100),
   ADDRESS varchar(100),
   AREA varchar(100),
   AREA_CODE varchar(8),
   AREA_OF_AREA_CODE varchar(100),
   PARISH_OF_RESIDENCE varchar(100),
   DISTRICT_SUBDIVISION_OF_RESIDENCE varchar(100),
   DISTRICT_OF_RESIDENCE varchar(100),
   PHONE varchar(50),
   MOBILE varchar(50),
   EMAIL varchar(100),
   WEB_ADRDRESS varchar(200),
   SOCIAL_SECURITY_NUMBER varchar(50),
   PROFESSION varchar(100),
   USERNAME varchar(50),
   PASSWD varchar(50),
   KEY_COUNTRY int(11),
   FISCAL_CODE varchar(50),
   TYPE_ID_DOCUMENT int(11) not null,
   SEX int(11),
   MARITAL_STATUS int(11),
   primary key (ID_INTERNAL),
   unique U1 (DOCUMENT_ID_NUMBER, TYPE_ID_DOCUMENT),
   UNIQUE U2 (USERNAME)
   )type=InnoDB;

#----------------------------------
# Table structure for table ROLE
#----------------------------------
drop table if exists ROLE;
create table ROLE(
  ID_INTERNAL int(11) not null,
  ROLE_NAME enum('PERSON','TIME_TABLE_MANAGER','TEACHER','STUDENT') not null,
  PORTAL_ACTION varchar(100),
  PRIMARY KEY (ID_INTERNAL),
  UNIQUE U1 (ROLE_NAME)
)type=InnoDB;

#----------------------------------
# Table structure for table PERSON_ROLE
#----------------------------------
drop table if exists PERSON_ROLE;
create table PERSON_ROLE(
  ID_INTERNAL int(11) not null,
  KEY_ROLE int(11) not null,
  KEY_PERSON int(11) not null,
  PRIMARY KEY (ID_INTERNAL),
  UNIQUE U1 (KEY_ROLE, KEY_PERSON)
)type=InnoDB;

#----------------------------------
# Table structure for table SERVICE
#----------------------------------
drop table if exists SERVICE;
create table SERVICE(
  ID_INTERNAL int(11) not null,
  SERVICE_NAME varchar(100) not null,
  PRIMARY KEY (ID_INTERNAL),
  UNIQUE U1 (SERVICE_NAME)
)type=InnoDB;

#----------------------------------
# Table structure for table SERVICE_ROLE
#----------------------------------
drop table if exists SERVICE_ROLE;
create table SERVICE_ROLE(
  ID_INTERNAL int(11) not null,
  KEY_ROLE int(11) not null,
  KEY_SERVICE int(11) not null,
  PRIMARY KEY (ID_INTERNAL),
  UNIQUE U1 (KEY_ROLE, KEY_SERVICE)
)type=InnoDB;


#----------------------------------
# Table structure for table student
#----------------------------------
DROP TABLE IF EXISTS STUDENT;
CREATE TABLE STUDENT (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  NUMBER smallint(10) unsigned NOT NULL default '0',
  KEY_PERSON int(11) unsigned NOT NULL default '0',
  STATE int(11) unsigned NOT NULL default '1',
  DEGREE_TYPE integer(11) not null,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (NUMBER, DEGREE_TYPE),
  UNIQUE KEY U2 (DEGREE_TYPE, KEY_PERSON)
) TYPE=InnoDB;

#----------------------------
# Table structure for TEACHER
#----------------------------
drop table if exists TEACHER;
create table TEACHER (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   USERNAME varchar(20),
   `PASSWORD` varchar(16),
   TEACHER_NUMBER int(10) unsigned,
   primary key (ID_INTERNAL))
   type=InnoDB comment="InnoDB free: 372736 kB";

#----------------------------------------
# Table structure for table 'PRIVILEGIO'
#----------------------------------------
drop table if exists PRIVILEGIO;
CREATE TABLE PRIVILEGIO (
  CHAVE_PESSOA int(11) NOT NULL default '0',
  SERVICO varchar(100) NOT NULL default '0',
  CODIGO_INTERNO int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (CODIGO_INTERNO)
) TYPE=InnoDB;
