-- -----------------------------
-- Data for table 'STUDENT_KIND'
-- (ID_INTERNAL, STUDENT_TYPE, MIN_COURSES_TO_ENROL, MAX_COURSES_TO_ENROL, MAX_NAC_TO_ENROL) 
-- -----------------------------
delete from STUDENT_KIND;
insert into STUDENT_KIND values (1, 1, 3, 7, 10);

-- -----------------------------
-- Data for table 'STUDENT_CURRICULAR_PLAN'
-- (ID_INTERNAL, KEY_STUDENT, KEY_DEGREE_CURRICULAR_PLAN, CURRENT_STATE, START_DATE, KEY_BRANCH, SPECIALIZATION, GIVEN_CREDITS)
-- -----------------------------
delete from STUDENT_CURRICULAR_PLAN;
insert into STUDENT_CURRICULAR_PLAN values (1, 6, 1, 1, '0000-00-00', 1, 1, 0);

-- -----------------------------
-- Data for table 'ENROLMENT_PERIOD'
-- (ID_INTERNAL, KEY_DEGREE_CURRICULAR_PLAN, KEY_EXECUTION_PERIOD, START_DATE, END_DATE) 
-- -----------------------------
delete from ENROLMENT_PERIOD;
insert into ENROLMENT_PERIOD values (1, 1, 5, SYSDATE(), '2010-01-10');

-- Isto e para sair
delete from EXECUTION_PERIOD;
insert into EXECUTION_PERIOD values (1, '1� Semestre', 1, 'C', 1);
insert into EXECUTION_PERIOD values (2, '2� Semestre', 1, 'NO', 2);
insert into EXECUTION_PERIOD values (3, '1� Semestre', 2, 'NO', 1);
insert into EXECUTION_PERIOD values (4, '2� Semestre', 2, 'NO', 2);
insert into EXECUTION_PERIOD values (5, '1� Semestre', 3, 'NO', 1);
insert into EXECUTION_PERIOD values (6, '2� Semestre', 3, 'NO', 2);
insert into EXECUTION_PERIOD values (7, '1� Semestre', 4, 'NO', 1);
insert into EXECUTION_PERIOD values (8, '2� Semestre', 4, 'NO', 2);
insert into EXECUTION_PERIOD values (9, '1� Semestre', 5, 'NO', 1);
insert into EXECUTION_PERIOD values (10, '2� Semestre', 5, 'NO', 2);
insert into EXECUTION_PERIOD values (11, '1� Semestre', 6, 'NO', 1);
insert into EXECUTION_PERIOD values (12, '2� Semestre', 6, 'NO', 2);
insert into EXECUTION_PERIOD values (13, '1� Semestre', 7, 'NO', 1);
insert into EXECUTION_PERIOD values (14, '2� Semestre', 7, 'NO', 2);
insert into EXECUTION_PERIOD values (15, '1� Semestre', 8, 'NO', 1);
insert into EXECUTION_PERIOD values (16, '2� Semestre', 8, 'NO', 2);

delete from EXECUTION_YEAR;
insert into EXECUTION_YEAR values (1, '2002/2003','C');
insert into EXECUTION_YEAR values (2, '2003/2004','NO');
insert into EXECUTION_YEAR values (3, '2005/2006','NO');
insert into EXECUTION_YEAR values (4, '2006/2007','NO');
insert into EXECUTION_YEAR values (5, '2007/2008','NO');
insert into EXECUTION_YEAR values (6, '2008/2009','NO');
insert into EXECUTION_YEAR values (7, '2009/2010','NO');
insert into EXECUTION_YEAR values (8, '2010/2011','NO');

-- -----------------------------
-- Data for table 'CURRICULAR_YEAR'
-- (ID_INTERNAL, YEAR)
-- -----------------------------
delete from CURRICULAR_YEAR;
insert into CURRICULAR_YEAR values (1, 1);
insert into CURRICULAR_YEAR values (2, 2);
insert into CURRICULAR_YEAR values (3, 3);
insert into CURRICULAR_YEAR values (4, 4);
insert into CURRICULAR_YEAR values (5, 5);

-- -----------------------------
-- Data for table 'CURRICULAR_SEMESTER'
-- (ID_INTERNAL, KEY_CURRICULAR_YEAR, SEMESTER)
-- -----------------------------
delete from CURRICULAR_SEMESTER;
insert into CURRICULAR_SEMESTER values (1, 1, 1);
insert into CURRICULAR_SEMESTER values (2, 1, 2);
insert into CURRICULAR_SEMESTER values (3, 2, 1);
insert into CURRICULAR_SEMESTER values (4, 2, 2);
insert into CURRICULAR_SEMESTER values (5, 3, 1);
insert into CURRICULAR_SEMESTER values (6, 3, 2);
insert into CURRICULAR_SEMESTER values (7, 4, 1);
insert into CURRICULAR_SEMESTER values (8, 4, 2);
insert into CURRICULAR_SEMESTER values (9, 5, 1);
insert into CURRICULAR_SEMESTER values (10, 5, 2);

