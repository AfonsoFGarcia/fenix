drop table if exists mw_ALUNO_temp;
create table mw_ALUNO_temp(
  documentIdNumber varchar(250) not null,
  number integer(50),
  degreeCode integer(20),
  branchCode integer(30),
  year integer(30),
  ISTUniversity varchar(30),
  worker varchar(30),
  gratuitySituation varchar(30),
  trash varchar(10),
  answer1 varchar(1),
  answer2 varchar(1),
  answer3 varchar(1),
  answer4 varchar(1),
  answer5 varchar(1),
  answer6 varchar(1),
  answer7 varchar(1),
  primary key (documentIdNumber)
)type= InnoDB;


drop table if exists mw_PESSOA;
create table mw_PESSOA(
  documentIdNumber varchar(250) not null,
  documentIdType varchar(50),
  documentIdPlace varchar(20),
  documentIdDate date,
  name varchar(250),
  sex varchar(30),
  maritalStatus varchar(30),
  dateOfBirth date,
  fatherName varchar(250),
  motherName varchar(250),
  countryCode integer(10),
  parishOfBirth varchar(250),
  municipalityOfBirth varchar(250),
  districtOfBirth varchar(250),
  address varchar(250),
  areaCode varchar(250),
  zipCode varchar(250),
  parishOfAddress varchar(250),
  municipalityOfAddress varchar(250),
  districtOfAddress varchar(250),
  phone varchar(250),
  mobilePhone varchar(250),
  email varchar(250),
  homePage varchar(250),
  contributorNumber integer(11),
  username varchar(250),
  password varchar(250),
  fiscalCode varchar(250),
  documentIdValidation date,
  financialRepCode varchar(250),
  primary key (documentIdNumber)
)type= InnoDB;


drop table if exists mw_BRANCH;
create table mw_BRANCH(
  degreeCode integer(10),
  branchCode integer(10),
  orientationCode integer(10),
  description varchar(250),
  id_internal integer(11) not null auto_increment,
  primary key (id_internal)

)type= InnoDB;


drop table if exists mw_AVERAGE;
create table mw_AVERAGE(
 number integer(10) not null,
 numberOfCoursesEnrolled integer(10),
 numberOfCoursesApproved integer(10),
 sum integer(10),
 average float(10),
 primary key (number)
)type= InnoDB;


drop table if exists mw_ENROLMENT_temp;
create table mw_ENROLMENT_temp(
	number integer(11), 
	enrolmentYear integer(11), 
	curricularCourseYear integer(11),
	curricularCourseSemester integer(11),
	season integer(11), 
	courseCode varchar(11),
	degreeCode integer(11),
	branchCode integer(11),
	grade varchar(10),
	teacherNumber integer(11),
	examDate date,
	universityCode varchar(10),
	remarks varchar(255),
	idinternal integer(11) not null auto_increment,
	primary key (idInternal)
)type= InnoDB;



drop table if exists mw_CURRICULAR_COURSE;
create table mw_CURRICULAR_COURSE(
	courseCode varchar(10) not null, 
	courseName varchar(255) not null, 
	universityCode varchar(10),
	primary key (courseCode)	
)type= InnoDB;



drop table if exists mw_CURRICULAR_COURSE_SCOPE_temp;
create table mw_CURRICULAR_COURSE_SCOPE_temp(
	executionYear integer(11) not null,
	courseCode varchar(10) not null, 
	degreeCode integer(11) not null,
	branchCode integer(11) not null,		 
	curricularYear integer(11),
	curricularSemester integer(11),	
	courseType integer(11),
	theoreticalHours float(10),	
	praticaHours float(10),
	labHours float(10),
	theoPratHours float(10),
	credits float(10),		
	orientation varchar(255),
	idInternal integer(11) not null auto_increment,
	primary key(idinternal)
)type= InnoDB;

drop table if exists mw_DEGREE_TRANSLATION;
create table mw_DEGREE_TRANSLATION(
	ADM_DEGREE_CODE int(11),
	KEY_DEGREE int(11)
)type=InnoDB;

drop table if exists mw_STUDENT_CLASS;
create table mw_STUDENT_CLASS(
	STUDENT_NUMBER varchar(20),
	STUDENT_NAME varchar(255),
	DEGREE_CODE int(11),
	AVERAGE float(10,2),
	CLASS_NAME varchar(20)
)type=InnoDB;
#	ID_INTERNAL int(11) auto_increment,
#	PRIMARY KEY(ID_INTERNAL)

