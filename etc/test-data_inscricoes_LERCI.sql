-- -----------------------------
-- Data for table 'DEPARTMENT'
-- (ID_INTERNAL, NAME, CODE)
-- -----------------------------
delete from DEPARTMENT;
insert into DEPARTMENT values (1, 'Departamento de Engenharia Inform�tica', 'DEI');

-- -----------------------------
-- Data for table 'DEPARTMENT_COURSE'
-- NOTAS:
-- FALTA COLOCAR A INFORMA��O DAS DISCIPLINAS DEPARTAMENTAIS CORESPONDENTES �S DISCIPLINAS CURRICULARES ABAIXO.
-- (ID_INTERNAL, CODE, NAME, KEY_DEPARTMENT)
-- -----------------------------
delete from DEPARTMENT_COURSE;
insert into DEPARTMENT_COURSE values (1, 'Disciplina Departamento', 'DD', 1);

-- -----------------------------
-- Data for table 'BRANCH'
-- (ID_INTERNAL, BRANCH_CODE, BRANCH_NAME)
-- -----------------------------
delete from BRANCH;
insert into BRANCH values (1, '', '', 1);
insert into BRANCH values (2, 'AAGR', '�REA DE ARQUITECTURA E GEST�O DE REDES', 1);
insert into BRANCH values (3, 'AAS', '�REA DE APLICA��ES E SERVI�OS', 1);

-- -----------------------------
-- Data for table 'DEGREE'
-- (ID_INTERNAL, CODE, NAME, TYPE_DEGREE)
-- -----------------------------
delete from DEGREE;
insert into DEGREE values (1, 'LERCI', 'Licenciatura em Engenharia de Redes de Comunica��o e de Informa��o', 1);

-- -----------------------------
-- -----------------------------
-- Data for table 'DEGREE_CURRICULAR_PLAN'
-- (ID_INTERNAL, NAME, KEY_DEGREE, STATE, INITIAL_DATE, END_DATE, KEY_DEGREE_CURRICULAR_PLAN_ENROLMENT_INFO)
-- -----------------------------
delete from DEGREE_CURRICULAR_PLAN;
insert into DEGREE_CURRICULAR_PLAN values (1, 'LERCI-2003', 1, 1, '0000-00-00', '0000-00-00', 5, 3, 0, 20);

-- -----------------------------
-- Data for table 'CURRICULAR_COURSE'
-- (ID_INTERNAL, KEY_DEPARTMENT_COURSE, KEY_DEGREE_CURRICULAR_PLAN, CREDITS, THEORETICAL_HOURS, PRATICAL_HOURS, THEO_PRAT_HOURS, LAB_HOURS, 
-- NAME, CODE, TYPE, EXECUTION_SCOPE, MANDATORY, KEY_CURRICULAR_COURSE_ENROLMENT_INFO)
-- -----------------------------

delete from CURRICULAR_COURSE;
-- PRIMEIRO ANO, PRIMEIRO SEMESTRE:
insert into CURRICULAR_COURSE values (1, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "INTRODU��O � PROGRAMA��O", "IK", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (2, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "AN�LISE MATEM�TICA I", "PY", 1, 1, 1, null);
insert into CURRICULAR_COURSE values (3, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "�LGEBRA LINEAR", "QN", 1, 1, 1, null);
insert into CURRICULAR_COURSE values (4, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "SISTEMAS DIGITAIS", "TU", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (5, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "TEORIA DA COMPUTA��O", "VI", 1, 1, 0, null);
-- PRIMEIRO ANO, SEGUNDO SEMESTRE:
insert into CURRICULAR_COURSE values (6, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "ALGORITMOS E ESTRUTURA DE DADOS", "01", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (7, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "ARQUITECTURA DE COMPUTADORES", "02", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (8, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "AN�LISE MATEM�TICA II", "P5", 1, 1, 1, null);
insert into CURRICULAR_COURSE values (9, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "F�SICA I - CURSO INFORM�TICA", "A37", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (10, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "MATEM�TICA COMPUTACIONAL", "AG7", 1, 1, 0, null);
-- SEGUNDO ANO, PRIMEIRO SEMESTRE:
insert into CURRICULAR_COURSE values (11, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "AN�LISE MATEM�TICA III", "", 1, 1, 1, null);
insert into CURRICULAR_COURSE values (12, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "F�SICA II", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (13, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "SISTEMAS OPERATIVOS", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (14, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "PROGRAMA��O COM OBJECTOS", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (15, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "REDES DE COMPUTADORES I", "", 1, 1, 0, null);
-- SEGUNDO ANO, SEGUNDO SEMESTRE:
insert into CURRICULAR_COURSE values (16, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "AN�LISE MATEM�TICA IV", "", 1, 1, 1, null);
insert into CURRICULAR_COURSE values (17, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "PROBABILIDADES E ESTAT�STICA", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (18, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "COMPUTA��O GR�FICA", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (19, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "SINAIS E SISTEMAS", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (20, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "BASES DE DADOS", "", 1, 1, 0, null);
-- TERCEIRO ANO, PRIMEIRO SEMESTRE:
insert into CURRICULAR_COURSE values (21, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "ELECTR�NICA I", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (22, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "FUNDAMENTOS DAS TELECOMUNICA��ES", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (23, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "SISTEMAS DISTRIBU�DOS", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (24, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "REDES DE COMPUTADORES II", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (25, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "INTERFACES PESSOA-M�QUINA", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (26, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "GEST�O DE REDES E SISTEMAS DISTRIBU�DOS", "", 1, 1, 0, null);
-- TERCEIRO ANO, SEGUNDO SEMESTRE:
insert into CURRICULAR_COURSE values (27, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "ELECTR�NICA II", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (28, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "SISTEMAS EMBEBIDOS", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (29, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "PROPAGA��O E ANTENAS", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (30, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "REDES COM INTEGRA��O DE SERVI�OS", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (31, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "INTELIG�NCIA ARTIFICIAL", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (32, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "APLICA��ES EM REDES DE GRANDE ESCALA", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (33, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "COMPILADORES", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (34, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "MODELA��O DE SISTEMAS DE INFORMA��O", "", 1, 1, 0, null);
-- QUARTO ANO, PRIMEIRO SEMESTRE:
insert into CURRICULAR_COURSE values (35, 1, 1, 4.0, 3.0, 2.0, 0.0, 2.0, "SOFTWARE DE TELECOMUNICA��ES", "", 1, 1, 0, null);
-- insert into CURRICULAR_COURSE values (36, 1, 1, 4.0, 3.0, 2.0, 0.0, 2.0, "GEST�O DE REDES E SISTEMAS DISTRIBU�DOS", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (37, 1, 1, 4.0, 3.0, 0.0, 2.0, 2.0, "SEGURAN�A EM REDES", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (38, 1, 1, 4.0, 3.0, 1.0, 2.0, 2.0, "SISTEMAS DE TELECOMUNICA��ES", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (39, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "APLICA��ES PARA SISTEMAS EMBEBIDOS", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (40, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "ENGENHARIA DE SOFTWARE", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (41, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "GEST�O DE PROJECTOS INFORM�TICOS", "", 1, 1, 0, null);
-- QUARTO ANO, SEGUNDO SEMESTRE:
insert into CURRICULAR_COURSE values (42, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "REDES M�VEIS E SEM FIOS", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (43, 1, 1, 4.0, 3.0, 0.0, 0.0, 0.0, "REDES DE ACESSO", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (44, 1, 1, 4.0, 3.0, 0.0, 3.0, 0.0, "COMUNICA��O DE �UDIO E V�DEO", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (45, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "PLANEAMENTO DE PROJECTO E REDES", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (46, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "COMPUTA��O M�VEL", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (47, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "PRODU��O DE CONTE�DOS MULTIM�DIA", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (48, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "PROJECTO DE APLICA��ES E SERVI�OS", "", 1, 1, 0, null);
-- insert into CURRICULAR_COURSE values (49, 1, 1, 4.0, 3.0, 0.0, 3.0, 0.0, "COMUNICA��O DE �UDIO E V�DEO", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (50, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "CARTEIRA PESSOAL", "", 1, 2, 0);
-- QUINTO ANO, PRIMEIRO SEMESTRE:
insert into CURRICULAR_COURSE values (51, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "OP��O I", "", 2, 1, 0, null);
insert into CURRICULAR_COURSE values (52, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "OP��O II", "", 2, 1, 0, null);
insert into CURRICULAR_COURSE values (53, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "TRABALHO FINAL DE CURSO", "", 4, 1, 0, null);
-- QUINTO ANO, SEGUNDO SEMESTRE:
insert into CURRICULAR_COURSE values (54, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "ORGANIZA��O E GEST�O DE EMPRESAS", "", 1, 1, 0, null);
insert into CURRICULAR_COURSE values (55, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "OP��O III", "", 2, 1, 0, null);
-- insert into CURRICULAR_COURSE values (56, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "TRABALHO FINAL DE CURSO II", "", 4, 1, 0, 2);

-- -----------------------------
-- Data for table 'CURRICULAR_COURSE_SCOPE'
-- (ID_INTERNAL, KEY_CURRICULAR_SEMESTER, KEY_CURRICULAR_COURSE, KEY_BRANCH, THEORETICAL_HOURS, PRATICAL_HOURS, THEO_PRAT_HOURS, LAB_HOURS)
-- -----------------------------
delete from CURRICULAR_COURSE_SCOPE;
insert into CURRICULAR_COURSE_SCOPE  values (1, 1, 1, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (2, 1, 2, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (3, 1, 3, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (4, 1, 4, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (5, 1, 5, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (6, 2, 6, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (7, 2, 7, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (8, 2, 8, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (9, 2, 9, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (10, 2, 10, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (11, 3, 11, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (12, 3, 12, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (13, 3, 13, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (14, 3, 14, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (15, 3, 15, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (16, 4, 16, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (17, 4, 17, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (18, 4, 18, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (19, 4, 19, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (20, 4, 20, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (21, 5, 21, 2, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (22, 5, 22, 2, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (23, 5, 23, 2, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (24, 5, 24, 2, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (25, 5, 23, 3, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (26, 5, 24, 3, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (27, 5, 25, 3, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (28, 5, 26, 3, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (29, 6, 27, 2, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (30, 6, 28, 2, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (31, 6, 29, 2, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (32, 6, 30, 2, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (33, 6, 31, 3, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (34, 6, 32, 3, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (35, 6, 33, 3, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (36, 6, 34, 3, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (37, 7, 35, 2, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (38, 7, 26, 2, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (39, 7, 37, 2, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (40, 7, 38, 2, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (41, 7, 37, 3, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (42, 7, 39, 3, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (43, 7, 40, 3, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (44, 7, 41, 3, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (45, 8, 42, 2, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (46, 8, 43, 2, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (47, 8, 44, 2, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (48, 8, 45, 2, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (49, 8, 46, 3, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (50, 8, 47, 3, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (51, 8, 48, 3, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (52, 8, 44, 3, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (53, 5, 50, 2, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (54, 6, 50, 2, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (55, 7, 50, 2, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (56, 8, 50, 2, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (57, 5, 50, 3, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (58, 6, 50, 3, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (59, 7, 50, 3, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (60, 8, 50, 3, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (61, 9, 51, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (62, 9, 52, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (63, 9, 53, 1, 0.0, 0.0, 0.0, 0.0, 4, 2, 2, null);
insert into CURRICULAR_COURSE_SCOPE  values (64, 10, 54, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (65, 10, 55, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
-- insert into CURRICULAR_COURSE_SCOPE  values (66, 10, 56, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);

insert into CURRICULAR_COURSE_SCOPE  values (67, 2, 2, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (68, 2, 3, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (69, 1, 8, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (70, 4, 11, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (71, 3, 16, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);

insert into CURRICULAR_COURSE_SCOPE  values (72, 9, 50, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (73, 10, 50, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);
insert into CURRICULAR_COURSE_SCOPE  values (74, 10, 53, 1, 0.0, 0.0, 0.0, 0.0, 2, 1, 1, null);


-- -----------------------------
-- Data for table 'PRECEDENCE'
-- (ID_INTERNAL, KEY_CURRICULAR_COURSE, SCOPE_TO_APPLY)
-- -----------------------------
delete from PRECEDENCE;
insert into PRECEDENCE values (1, 29, 'SP');
insert into PRECEDENCE values (2, 8, 'SP');
insert into PRECEDENCE values (3, 11, 'SP');
insert into PRECEDENCE values (4, 16, 'SP');
insert into PRECEDENCE values (5, 17, 'SP');
insert into PRECEDENCE values (6, 21, 'SP');
insert into PRECEDENCE values (7, 27, 'SP');
insert into PRECEDENCE values (8, 19, 'SP');
insert into PRECEDENCE values (9, 22, 'SP');
insert into PRECEDENCE values (10, 38, 'SP');
insert into PRECEDENCE values (11, 44, 'SP');
insert into PRECEDENCE values (12, 35, 'SP');
insert into PRECEDENCE values (13, 45, 'SP');
insert into PRECEDENCE values (14, 15, 'SP');
insert into PRECEDENCE values (15, 24, 'SP');
insert into PRECEDENCE values (16, 30, 'SP');
insert into PRECEDENCE values (17, 42, 'SP');
insert into PRECEDENCE values (18, 43, 'SP');
insert into PRECEDENCE values (19, 7, 'SP');
insert into PRECEDENCE values (20, 13, 'SP');
insert into PRECEDENCE values (21, 28, 'SP');
insert into PRECEDENCE values (22, 23, 'SP');
insert into PRECEDENCE values (23, 37, 'SP');
insert into PRECEDENCE values (24, 20, 'SP');
insert into PRECEDENCE values (25, 26, 'SP');
insert into PRECEDENCE values (26, 6, 'SP');
insert into PRECEDENCE values (27, 14, 'SP');
insert into PRECEDENCE values (28, 18, 'SP');
insert into PRECEDENCE values (29, 25, 'SP');
insert into PRECEDENCE values (30, 34, 'SP');
insert into PRECEDENCE values (31, 32, 'SP');
insert into PRECEDENCE values (32, 31, 'SP');
insert into PRECEDENCE values (33, 33, 'SP');
insert into PRECEDENCE values (34, 39, 'SP');
insert into PRECEDENCE values (35, 40, 'SP');
insert into PRECEDENCE values (36, 41, 'SP');
insert into PRECEDENCE values (37, 48, 'SP');
insert into PRECEDENCE values (38, 46, 'SP');
insert into PRECEDENCE values (39, 47, 'SP');
insert into PRECEDENCE values (40, 53, 'SP');

-- -----------------------------
-- Data for table 'RESTRICTION'
-- (ID_INTERNAL, CLASS_NAME, KEY_PRECEDENCE, KEY_CURRICULAR_COURSE, NUMBER_OF_CURRICULAR_COURSE_DONE)
-- -----------------------------
delete from RESTRICTION;
insert into RESTRICTION values (1, 'Dominio.RestrictionCurricularCourseDone', 1, 12, 0);
insert into RESTRICTION values (2, 'Dominio.RestrictionCurricularCourseDone', 1, 11, 0);
insert into RESTRICTION values (3, 'Dominio.RestrictionCurricularCourseDone', 2, 2, 0);
insert into RESTRICTION values (4, 'Dominio.RestrictionCurricularCourseDone', 3, 8, 0);
insert into RESTRICTION values (5, 'Dominio.RestrictionCurricularCourseDone', 4, 11, 0);
insert into RESTRICTION values (6, 'Dominio.RestrictionCurricularCourseDone', 5, 11, 0);
insert into RESTRICTION values (7, 'Dominio.RestrictionCurricularCourseDone', 6, 12, 0);
insert into RESTRICTION values (8, 'Dominio.RestrictionCurricularCourseDone', 6, 10, 0);
insert into RESTRICTION values (9, 'Dominio.RestrictionCurricularCourseDone', 7, 21, 0);
insert into RESTRICTION values (10, 'Dominio.RestrictionCurricularCourseDone', 8, 8, 0);
insert into RESTRICTION values (11, 'Dominio.RestrictionCurricularCourseDone', 9, 19, 0);
insert into RESTRICTION values (12, 'Dominio.RestrictionCurricularCourseDone', 9, 17, 0);
insert into RESTRICTION values (13, 'Dominio.RestrictionCurricularCourseDone', 10, 22, 0);
insert into RESTRICTION values (14, 'Dominio.RestrictionCurricularCourseDone', 11, 19, 0);
insert into RESTRICTION values (15, 'Dominio.RestrictionCurricularCourseDone', 12, 15, 0);
insert into RESTRICTION values (16, 'Dominio.RestrictionCurricularCourseDone', 12, 14, 0);
insert into RESTRICTION values (17, 'Dominio.RestrictionCurricularCourseDone', 13, 30, 0);
insert into RESTRICTION values (18, 'Dominio.RestrictionCurricularCourseDone', 13, 35, 0);
insert into RESTRICTION values (19, 'Dominio.RestrictionCurricularCourseDone', 14, 6, 0);
insert into RESTRICTION values (20, 'Dominio.RestrictionCurricularCourseDone', 15, 15, 0);
insert into RESTRICTION values (21, 'Dominio.RestrictionCurricularCourseDone', 15, 17, 0);
insert into RESTRICTION values (22, 'Dominio.RestrictionCurricularCourseDone', 16, 24, 0);
insert into RESTRICTION values (23, 'Dominio.RestrictionCurricularCourseDone', 17, 30, 0);
insert into RESTRICTION values (24, 'Dominio.RestrictionCurricularCourseDone', 18, 30, 0);
insert into RESTRICTION values (25, 'Dominio.RestrictionCurricularCourseDone', 19, 4, 0);
insert into RESTRICTION values (26, 'Dominio.RestrictionCurricularCourseDone', 20, 6, 0);
insert into RESTRICTION values (27, 'Dominio.RestrictionCurricularCourseDone', 21, 13, 0);
insert into RESTRICTION values (28, 'Dominio.RestrictionCurricularCourseDone', 22, 13, 0);
insert into RESTRICTION values (29, 'Dominio.RestrictionCurricularCourseDone', 22, 15, 0);
insert into RESTRICTION values (30, 'Dominio.RestrictionCurricularCourseDone', 23, 23, 0);
insert into RESTRICTION values (31, 'Dominio.RestrictionCurricularCourseDone', 24, 14, 0);
insert into RESTRICTION values (32, 'Dominio.RestrictionCurricularCourseDone', 25, 15, 0);
insert into RESTRICTION values (33, 'Dominio.RestrictionCurricularCourseDone', 26, 1, 0);
insert into RESTRICTION values (34, 'Dominio.RestrictionCurricularCourseDone', 27, 6, 0);
insert into RESTRICTION values (35, 'Dominio.RestrictionCurricularCourseDone', 28, 3, 0);
insert into RESTRICTION values (36, 'Dominio.RestrictionCurricularCourseDone', 28, 14, 0);
insert into RESTRICTION values (37, 'Dominio.RestrictionCurricularCourseDone', 29, 17, 0);
insert into RESTRICTION values (38, 'Dominio.RestrictionCurricularCourseDone', 29, 18, 0);
insert into RESTRICTION values (39, 'Dominio.RestrictionCurricularCourseDone', 30, 20, 0);
insert into RESTRICTION values (40, 'Dominio.RestrictionCurricularCourseDone', 31, 14, 0);
insert into RESTRICTION values (41, 'Dominio.RestrictionCurricularCourseDone', 31, 23, 0);
insert into RESTRICTION values (42, 'Dominio.RestrictionCurricularCourseDone', 32, 6, 0);
insert into RESTRICTION values (43, 'Dominio.RestrictionCurricularCourseDone', 33, 6, 0);
insert into RESTRICTION values (44, 'Dominio.RestrictionCurricularCourseDone', 33, 5, 0);
insert into RESTRICTION values (45, 'Dominio.RestrictionCurricularCourseDone', 34, 32, 0);
insert into RESTRICTION values (46, 'Dominio.RestrictionCurricularCourseDone', 35, 34, 0);
insert into RESTRICTION values (47, 'Dominio.RestrictionCurricularCourseDone', 36, 20, 0);
insert into RESTRICTION values (48, 'Dominio.RestrictionCurricularCourseDone', 37, 40, 0);
insert into RESTRICTION values (49, 'Dominio.RestrictionCurricularCourseDone', 37, 32, 0);
insert into RESTRICTION values (50, 'Dominio.RestrictionCurricularCourseDone', 38, 32, 0);
insert into RESTRICTION values (51, 'Dominio.RestrictionCurricularCourseDone', 39, 25, 0);
insert into RESTRICTION values (52, 'Dominio.RestrictionNumberOfCurricularCourseDone', 40, null, 28);

-- -----------------------------
-- Data for table 'ENROLMENT'
-- (ID_INTERNAL, KEY_STUDENT_CURRICULAR_PLAN, KEY_CURRICULAR_COURSE, KEY_EXECUTION_PERIOD, STATE)
-- -----------------------------
delete from ENROLMENT;
insert into ENROLMENT values (1, 1, 1, 1, 1, 'Dominio.Enrolment', null, 1);
insert into ENROLMENT values (2, 1, 2, 1, 1, 'Dominio.Enrolment', null, 1);
insert into ENROLMENT values (3, 1, 3, 1, 1, 'Dominio.Enrolment', null, 1);
insert into ENROLMENT values (4, 1, 4, 1, 1, 'Dominio.Enrolment', null, 1);
insert into ENROLMENT values (5, 1, 5, 1, 1, 'Dominio.Enrolment', null, 1);

-- insert into ENROLMENT values (6, 1, 6, 2, 1, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (7, 1, 7, 2, 1, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (8, 1, 8, 2, 1, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (9, 1, 9, 2, 1, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (10, 1, 10, 2, 1, 'Dominio.Enrolment', null, 1);

-- insert into ENROLMENT values (11, 1, 11, 3, 1, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (12, 1, 12, 3, 1, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (13, 1, 13, 3, 1, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (14, 1, 14, 3, 1, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (15, 1, 15, 3, 1, 'Dominio.Enrolment', null, 1);

-- insert into ENROLMENT values (16, 1, 16, 4, 1, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (17, 1, 17, 4, 1, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (18, 1, 18, 4, 1, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (19, 1, 19, 4, 1, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (20, 1, 20, 4, 1, 'Dominio.Enrolment', null, 1);

-- insert into ENROLMENT values (21, 1, 21, 5, 1, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (22, 1, 22, 5, 1, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (23, 1, 23, 5, 1, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (24, 1, 24, 5, 1, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (25, 1, 50, 5, 3, 'Dominio.Enrolment', null, 1);

-- insert into ENROLMENT values (26, 1, 27, 6, 1, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (27, 1, 28, 6, 1, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (28, 1, 29, 6, 1, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (29, 1, 30, 6, 1, 'Dominio.Enrolment', null, 1);

-- insert into ENROLMENT values (30, 1, 26, 7, 2, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (31, 1, 35, 7, 2, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (32, 1, 37, 7, 2, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (33, 1, 38, 7, 2, 'Dominio.Enrolment', null, 1);

-- insert into ENROLMENT values (34, 1, 42, 8, 1, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (35, 1, 43, 8, 1, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (36, 1, 44, 8, 1, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (37, 1, 45, 8, 1, 'Dominio.Enrolment', null, 1);

-- insert into ENROLMENT values (38, 1, 51, 9, 1, 'Dominio.EnrolmentInOptionalCurricularCourse', 25, 1);
-- insert into ENROLMENT values (39, 1, 52, 9, 1, 'Dominio.EnrolmentInOptionalCurricularCourse', 26, 1);
-- insert into ENROLMENT values (40, 1, 53, 9, 3, 'Dominio.Enrolment', null, 1);

-- insert into ENROLMENT values (41, 1, 54, 10, 1, 'Dominio.Enrolment', null, 1);
-- insert into ENROLMENT values (42, 1, 55, 10, 1, 'Dominio.EnrolmentInOptionalCurricularCourse', 39, 1);

