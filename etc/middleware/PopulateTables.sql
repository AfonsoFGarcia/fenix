LOAD DATA INFILE '@load.data.infile.root@alunos.txt' into table mw_STUDENT_AUXILIARY_TABLE;
LOAD DATA INFILE '@load.data.infile.root@pessoa.txt' into table mw_PERSON IGNORE 1 LINES;
LOAD DATA INFILE '@load.data.infile.root@curram.txt' into table mw_BRANCH;
LOAD DATA INFILE '@load.data.infile.root@medias.txt' into table mw_AVERAGE;
LOAD DATA INFILE '@load.data.infile.root@curriculo.txt' into table mw_ENROLMENT_AUXILIARY_TABLE_1;
LOAD DATA INFILE '@load.data.infile.root@nomedis.txt' into table mw_CURRICULAR_COURSE;
LOAD DATA INFILE '@load.data.infile.root@disciplinas.txt' into table mw_CURRICULAR_COURSE_SCOPE_AUXILIARY_TABLE;
LOAD DATA INFILE '@load.data.infile.root@escolas.txt' into table mw_UNIVERSITY;
--LOAD DATA INFILE '@load.data.infile.root@student-class-distribution.txt' into table mw_STUDENT_CLASS IGNORE 1 LINES;
--LOAD DATA INFILE '@load.data.infile.root@student-class-distribution-2nd-phase.txt' into table mw_STUDENT_CLASS IGNORE 1 LINES (STUDENT_NUMBER, DEGREE_CODE,CLASS_NAME);

CREATE INDEX mw_ENROLMENT_AUXILIARY_TABLE_1_INDEX_1 on mw_ENROLMENT_AUXILIARY_TABLE_1(number,enrolmentYear,curricularCourseYear,curricularCourseSemester,season,courseCode,degreeCode,branchCode,grade,teacherNumber,examDate,universityCode,remarks);
CREATE INDEX mw_ENROLMENT_AUXILIARY_TABLE_1_INDEX_2 on mw_ENROLMENT_AUXILIARY_TABLE_1(enrolmentYear,degreeCode);
CREATE INDEX mw_ENROLMENT_AUXILIARY_TABLE_1_INDEX_3 on mw_ENROLMENT_AUXILIARY_TABLE_1(number);
CREATE INDEX mw_ENROLMENT_AUXILIARY_TABLE_1_INDEX_4 on mw_ENROLMENT_AUXILIARY_TABLE_1(enrolmentYear);

CREATE INDEX mw_STUDENT_AUXILIARY_TABLE_INDEX_1 on mw_STUDENT_AUXILIARY_TABLE(number);

UPDATE mw_CURRICULAR_COURSE SET courseCode = LTRIM(courseCode);
UPDATE mw_ENROLMENT_AUXILIARY_TABLE_1 SET courseCode = LTRIM(courseCode);
UPDATE mw_CURRICULAR_COURSE_SCOPE_AUXILIARY_TABLE SET courseCode = LTRIM(courseCode);
UPDATE mw_UNIVERSITY SET universityCode = LTRIM(universityCode);

delete from mw_DEGREE_TRANSLATION;
insert into mw_DEGREE_TRANSLATION (ADM_DEGREE_CODE, KEY_DEGREE) values (01,1),(02,2),(03,3),(04,4),(05,5),(06,6)
,(07,7),(08,8),(09,9),(10,10),(19,19),(11,11),(12,12),(13,13),(14,14)
,(15,15),(16,16),(17,17),(18,18),(20,20),(21,21),(22,22),(23,23),(24,51)
,(51,1),(53,3),(54,4),(64,14);



---------- TUDO O QUE EST� DAQUI PARA BAIXO � PARA SAIR T�O BREVEMENTE QUANTO SE CONSEGUIR ----------

UPDATE ENROLMENT_EVALUATION SET GRADE = 'NA' WHERE GRADE = '0';

-----------------------------------------------------------------------------------------------------------------------------
-- Temporary Delete's (Student's that changed degree but the Branch wasn't updated)
delete from mw_STUDENT_AUXILIARY_TABLE where number = 42980;
delete from mw_STUDENT_AUXILIARY_TABLE where number = 51335;
delete from mw_STUDENT_AUXILIARY_TABLE where number = 46896;
delete from mw_STUDENT_AUXILIARY_TABLE where number = 42455;
delete from mw_STUDENT_AUXILIARY_TABLE where number = 45067;

delete from mw_ENROLMENT_AUXILIARY_TABLE_1 where number = 42980;
delete from mw_ENROLMENT_AUXILIARY_TABLE_1 where number = 51335;
delete from mw_ENROLMENT_AUXILIARY_TABLE_1 where number = 46896;
delete from mw_ENROLMENT_AUXILIARY_TABLE_1 where number = 42455;
delete from mw_ENROLMENT_AUXILIARY_TABLE_1 where number = 45067;

delete from mw_PERSON where documentidnumber = 'ERASMUS';
delete from mw_STUDENT_AUXILIARY_TABLE where documentidnumber = 'ERASMUS';

drop table if exists mw_PERSON_WITH_DUPLICATE_ID;
create table mw_PERSON_WITH_DUPLICATE_ID
select documentidnumber, max(idinternal) as maxidinternal, count(1) as total from mw_PERSON group by documentidnumber having count(1) > 1;

drop table if exists mw_STUDENTS_WITH_VARIOUS_NUMBERS;
create table mw_STUDENTS_WITH_VARIOUS_NUMBERS
select documentidnumber, number, max(number) as maxidinternal, count(1) as total from mw_STUDENT_AUXILIARY_TABLE group by documentidnumber having count(1) > 1;
-----------------------------------------------------------------------------------------------------------------------------
