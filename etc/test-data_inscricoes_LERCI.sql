#------------------------------------------------------------------------------------------------------------------
# DADOS ESPECIFICOS LERCI:
#------------------------------------------------------------------------------------------------------------------

#-----------------------------
# Data for table 'CURRICULAR_YEAR'
#-----------------------------
# (ID_INTERNAL, YEAR)
;
delete from CURRICULAR_YEAR;
insert into CURRICULAR_YEAR values (1, 1);
insert into CURRICULAR_YEAR values (2, 2);
insert into CURRICULAR_YEAR values (3, 3);
insert into CURRICULAR_YEAR values (4, 4);
insert into CURRICULAR_YEAR values (5, 5);

#-----------------------------
# Data for table 'CURRICULAR_SEMESTER'
#-----------------------------
# (ID_INTERNAL, KEY_CURRICULAR_YEAR, SEMESTER)
;
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

#-----------------------------
# Data for table 'DEPARTMENT'
#-----------------------------
# (ID_INTERNAL, NAME, CODE)
;
delete from DEPARTMENT;
insert into DEPARTMENT values (1, 'Departamento de Engenharia Inform�tica', 'DEI');

#-----------------------------
# Data for table 'DEPARTMENT_COURSE'
#-----------------------------
# NOTAS:
# FALTA COLOCAR A INFORMA��O DAS DISCIPLINAS DEPARTAMENTAIS CORESPONDENTES �S DISCIPLINAS CURRICULARES ABAIXO.
# (ID_INTERNAL, CODE, NAME, KEY_DEPARTMENT)
;
delete from DEPARTMENT_COURSE;
insert into DEPARTMENT_COURSE values (1, 'Disciplina Departamento', 'DD', 1);

#-----------------------------
# Data for table 'BRANCH'
#-----------------------------
# (ID_INTERNAL, BRANCH_CODE, BRANCH_NAME)
;
delete from BRANCH;
insert into BRANCH values (1, '', '');
insert into BRANCH values (2, 'AAGR', '�REA DE ARQUITECTURA E GEST�O DE REDES');
insert into BRANCH values (3, 'AAS', '�REA DE APLICA��ES E SERVI�OS');

#-----------------------------
# Data for table 'DEGREE'
#-----------------------------
# (ID_INTERNAL, CODE, NAME, TYPE_DEGREE)
;
delete from DEGREE;
insert into DEGREE values (1, 'LERCI', 'Licenciatura em Engenharia de Redes de Comunica��o e de Informa��o', 1);

#-----------------------------
# Data for table 'DEGREE_CURRICULAR_PLAN'
#-----------------------------
# NOTAS:
# VERIFICAR OS VALORES DAS DATAS PARA OS CAMPOS 'INITIAL_YEAR' E 'END_YEAR'.
# (ID_INTERNAL, NAME, KEY_DEGREE, STATE, INITIAL_DATE, END_DATE)
;
delete from DEGREE_CURRICULAR_PLAN;
insert into DEGREE_CURRICULAR_PLAN values (1, 'Licenciatura em Engenharia de Redes de Comunica��o e de Informa��o', 1, 1, '0000-00-00', '0000-00-00');

#-----------------------------
# Data for table 'STUDENT_CURRICULAR_PLAN'
#-----------------------------
# NOTAS:
# VERIFICAR O VALOR DA DATA PARA O CAMPO 'START_DATE'.
# VERIFICAR O VALOR DA CHAVE PARA ALUNO.
# (ID_INTERNAL, KEY_STUDENT, KEY_DEGREE_CURRICULAR_PLAN, CURRENT_STATE, START_DATE, KEY_BRANCH)
;
delete from STUDENT_CURRICULAR_PLAN;
insert into STUDENT_CURRICULAR_PLAN values (1, 1, 1, 1, '0000-00-00', 2);

#-----------------------------
# Data for table 'CURRICULAR_COURSE'
#-----------------------------
# NOTAS:
# VERIFICAR O VALOR DA CHAVE PARA DICIPLINA DEPARTAMENTO.
# VERIFICAR O VALOR DOS CAMPOS 'CREDITS' E 'CODE' PARA AS DISCIPLINAS DO SEGUNDO ANO PARA A FRENTE.
# (ID_INTERNAL, KEY_DEPARTMENT_COURSE, KEY_DEGREE_CURRICULAR_PLAN, CREDITS, THEORETICAL_HOURS, PRATICAL_HOURS, THEO_PRAT_HOURS, LAB_HOURS, NAME, CODE)

# PRIMEIRO ANO, PRIMEIRO SEMESTRE:
;
delete from CURRICULAR_COURSE;
insert into CURRICULAR_COURSE values (1, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "INTRODU��O � PROGRAMA��O", "IK", 1);
insert into CURRICULAR_COURSE values (2, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "AN�LISE MATEM�TICA I", "PY", 1);
insert into CURRICULAR_COURSE values (3, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "�LGEBRA LINEAR", "QN", 1);
insert into CURRICULAR_COURSE values (4, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "SISTEMAS DIGITAIS", "TU", 1);
insert into CURRICULAR_COURSE values (5, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "TEORIA DA COMPUTA��O", "VI", 1);
# PRIMEIRO ANO, SEGUNDO SEMESTRE:
;
insert into CURRICULAR_COURSE values (6, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "ALGORITMOS E ESTRUTURA DE DADOS", "01", 1);
insert into CURRICULAR_COURSE values (7, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "ARQUITECTURA DE COMPUTADORES", "02", 1);
insert into CURRICULAR_COURSE values (8, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "AN�LISE MATEM�TICA II", "P5", 1);
insert into CURRICULAR_COURSE values (9, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "F�SICA I - CURSO INFORM�TICA", "A37", 1);
insert into CURRICULAR_COURSE values (10, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "MATEM�TICA COMPUTACIONAL", "AG7", 1);
# SEGUNDO ANO, PRIMEIRO SEMESTRE:
;
insert into CURRICULAR_COURSE values (11, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "AN�LISE MATEM�TICA III", "", 1);
insert into CURRICULAR_COURSE values (12, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "F�SICA II", "", 1);
insert into CURRICULAR_COURSE values (13, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "SISTEMAS OPERATIVOS", "", 1);
insert into CURRICULAR_COURSE values (14, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "PROGRAMA��O COM OBJECTOS", "", 1);
insert into CURRICULAR_COURSE values (15, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "REDES DE COMPUTADORES I", "", 1);
# SEGUNDO ANO, SEGUNDO SEMESTRE:
;
insert into CURRICULAR_COURSE values (16, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "AN�LISE MATEM�TICA IV", "", 1);
insert into CURRICULAR_COURSE values (17, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "PROBABILIDADES E ESTAT�STICA", "", 1);
insert into CURRICULAR_COURSE values (18, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "COMPUTA��O GR�FICA", "", 1);
insert into CURRICULAR_COURSE values (19, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "SINAIS E SISTEMAS", "", 1);
insert into CURRICULAR_COURSE values (20, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "BASES DE DADOS", "", 1);
# TERCEIRO ANO, PRIMEIRO SEMESTRE:
;
insert into CURRICULAR_COURSE values (21, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "ELECTR�NICA I", "", 1);
insert into CURRICULAR_COURSE values (22, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "FUNDAMENTOS DAS TELECOMUNICA��ES", "", 1);
insert into CURRICULAR_COURSE values (23, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "SISTEMAS DISTRIBU�DOS", "", 1);
insert into CURRICULAR_COURSE values (24, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "REDES DE COMPUTADORES II", "", 1);
insert into CURRICULAR_COURSE values (25, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "INTERFACES PESSOA-M�QUINA", "", 1);
insert into CURRICULAR_COURSE values (26, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "GEST�O DE REDES E SISTEMAS DISTRIBU�DOS", "", 1);
# TERCEIRO ANO, SEGUNDO SEMESTRE:
;
insert into CURRICULAR_COURSE values (27, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "ELECTR�NICA II", "", 1);
insert into CURRICULAR_COURSE values (28, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "SISTEMAS EMBEBIDOS", "", 1);
insert into CURRICULAR_COURSE values (29, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "PROPAGA��O E ANTENAS", "", 1);
insert into CURRICULAR_COURSE values (30, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "REDES COM INTEGRA��O DE SERVI�OS", "", 1);
insert into CURRICULAR_COURSE values (31, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "INTELIG�NCIA ARTIFICIAL", "", 1);
insert into CURRICULAR_COURSE values (32, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "APLICA��ES EM REDES DE GRANDE ESCALA", "", 1);
insert into CURRICULAR_COURSE values (33, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "COMPILADORES", "", 1);
insert into CURRICULAR_COURSE values (34, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "MODELA��O DE SISTEMAS DE INFORMA��O", "", 1);
# QUARTO ANO, PRIMEIRO SEMESTRE:
;
insert into CURRICULAR_COURSE values (35, 1, 1, 4.0, 3.0, 2.0, 0.0, 2.0, "SOFTWARE DE TELECOMUNICA��ES", "", 1);
#insert into CURRICULAR_COURSE values (36, 1, 1, 4.0, 3.0, 2.0, 0.0, 2.0, "GEST�O DE REDES E SISTEMAS DISTRIBU�DOS", "", 1);
;
insert into CURRICULAR_COURSE values (37, 1, 1, 4.0, 3.0, 0.0, 2.0, 2.0, "SEGURAN�A EM REDES", "", 1);
insert into CURRICULAR_COURSE values (38, 1, 1, 4.0, 3.0, 1.0, 2.0, 2.0, "SISTEMAS DE TELECOMUNICA��ES", "", 1);
insert into CURRICULAR_COURSE values (39, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "APLICA��ES PARA SISTEMAS EMBEBIDOS", "", 1);
insert into CURRICULAR_COURSE values (40, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "ENGENHARIA DE SOFTWARE", "", 1);
insert into CURRICULAR_COURSE values (41, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "GEST�O DE PROJECTOS INFORM�TICOS", "", 1);
# QUARTO ANO, SEGUNDO SEMESTRE:
;
insert into CURRICULAR_COURSE values (42, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "REDES M�VEIS E SEM FIOS", "", 1);
insert into CURRICULAR_COURSE values (43, 1, 1, 4.0, 3.0, 0.0, 0.0, 0.0, "REDES DE ACESSO", "", 1);
insert into CURRICULAR_COURSE values (44, 1, 1, 4.0, 3.0, 0.0, 3.0, 0.0, "COMUNICA��O DE �UDIO E V�DEO", "", 1);
insert into CURRICULAR_COURSE values (45, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "PLANEAMENTO DE PROJECTO E REDES", "", 1);
insert into CURRICULAR_COURSE values (46, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "COMPUTA��O M�VEL", "", 1);
insert into CURRICULAR_COURSE values (47, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "PRODU��O DE CONTE�DOS MULTIM�DIA", "", 1);
insert into CURRICULAR_COURSE values (48, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "PROJECTO DE APLICA��ES E SERVI�OS", "", 1);
#insert into CURRICULAR_COURSE values (49, 1, 1, 4.0, 3.0, 0.0, 3.0, 0.0, "COMUNICA��O DE �UDIO E V�DEO", "", 1);
;
insert into CURRICULAR_COURSE values (50, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "CARTEIRA PESSOAL", "", 1);
# QUINTO ANO, PRIMEIRO SEMESTRE:
;
insert into CURRICULAR_COURSE values (51, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "OP��O I", "", 2);
insert into CURRICULAR_COURSE values (52, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "OP��O II", "", 2);
insert into CURRICULAR_COURSE values (53, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "TRABALHO FINAL DE CURSO", "", 4);
# QUINTO ANO, SEGUNDO SEMESTRE:
;
insert into CURRICULAR_COURSE values (54, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "ORGANIZA��O E GEST�O DE EMPRESAS", "", 1);
insert into CURRICULAR_COURSE values (55, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "OP��O III", "", 2);
#insert into CURRICULAR_COURSE values (56, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "TRABALHO FINAL DE CURSO II", "", 4);

#-----------------------------
# Data for table 'CURRICULAR_COURSE_SCOPE'
#-----------------------------
;
delete from CURRICULAR_COURSE_SCOPE;
#(ID_INTERNAL, KEY_CURRICULAR_SEMESTER, KEY_CURRICULAR_COURSE, KEY_BRANCH)
;
insert into CURRICULAR_COURSE_SCOPE  values (1, 1, 1, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (2, 1, 2, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (3, 1, 3, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (4, 1, 4, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (5, 1, 5, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (6, 2, 6, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (7, 2, 7, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (8, 2, 8, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (9, 2, 9, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (10, 2, 10, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (11, 3, 11, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (12, 3, 12, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (13, 3, 13, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (14, 3, 14, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (15, 3, 15, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (16, 4, 16, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (17, 4, 17, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (18, 4, 18, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (19, 4, 19, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (20, 4, 20, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (21, 5, 21, 2, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (22, 5, 22, 2, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (23, 5, 23, 2, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (24, 5, 24, 2, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (25, 5, 23, 3, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (26, 5, 24, 3, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (27, 5, 25, 3, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (28, 5, 26, 3, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (29, 6, 27, 2, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (30, 6, 28, 2, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (31, 6, 29, 2, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (32, 6, 30, 2, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (33, 6, 31, 3, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (34, 6, 32, 3, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (35, 6, 33, 3, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (36, 6, 34, 3, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (37, 7, 35, 2, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (38, 7, 26, 2, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (39, 7, 37, 2, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (40, 7, 38, 2, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (41, 7, 37, 3, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (42, 7, 39, 3, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (43, 7, 40, 3, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (44, 7, 41, 3, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (45, 8, 42, 2, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (46, 8, 43, 2, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (47, 8, 44, 2, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (48, 8, 45, 2, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (49, 8, 46, 3, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (50, 8, 47, 3, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (51, 8, 48, 3, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (52, 8, 44, 3, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (53, 5, 50, 2, 0.0, 0.0, 0.0, 0.0, 1);
insert into CURRICULAR_COURSE_SCOPE  values (54, 6, 50, 2, 0.0, 0.0, 0.0, 0.0, 1);
insert into CURRICULAR_COURSE_SCOPE  values (55, 7, 50, 2, 0.0, 0.0, 0.0, 0.0, 1);
insert into CURRICULAR_COURSE_SCOPE  values (56, 8, 50, 2, 0.0, 0.0, 0.0, 0.0, 1);
insert into CURRICULAR_COURSE_SCOPE  values (57, 5, 50, 3, 0.0, 0.0, 0.0, 0.0, 1);
insert into CURRICULAR_COURSE_SCOPE  values (58, 6, 50, 3, 0.0, 0.0, 0.0, 0.0, 1);
insert into CURRICULAR_COURSE_SCOPE  values (59, 7, 50, 3, 0.0, 0.0, 0.0, 0.0, 1);
insert into CURRICULAR_COURSE_SCOPE  values (60, 8, 50, 3, 0.0, 0.0, 0.0, 0.0, 1);
insert into CURRICULAR_COURSE_SCOPE  values (61, 9, 51, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (62, 9, 52, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (63, 9, 53, 1, 0.0, 0.0, 0.0, 0.0, 1);
insert into CURRICULAR_COURSE_SCOPE  values (64, 10, 54, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (65, 10, 55, 1, 0.0, 0.0, 0.0, 0.0, 0);
#insert into CURRICULAR_COURSE_SCOPE  values (66, 10, 56, 1, 0.0, 0.0, 0.0, 0.0, 0);

insert into CURRICULAR_COURSE_SCOPE  values (67, 2, 2, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (68, 2, 3, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (69, 1, 8, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (70, 4, 11, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (71, 3, 16, 1, 0.0, 0.0, 0.0, 0.0, 2);

insert into CURRICULAR_COURSE_SCOPE  values (72, 9, 50, 1, 0.0, 0.0, 0.0, 0.0, 1);
insert into CURRICULAR_COURSE_SCOPE  values (73, 10, 50, 1, 0.0, 0.0, 0.0, 0.0, 1);

insert into CURRICULAR_COURSE_SCOPE  values (74, 10, 53, 1, 0.0, 0.0, 0.0, 0.0, 1);

#-----------------------------
# Data for table 'ENROLMENT'
#-----------------------------
# (ID_INTERNAL, KEY_STUDENT_CURRICULAR_PLAN, KEY_CURRICULAR_COURSE, KEY_EXECUTION_PERIOD, STATE)
;
delete from ENROLMENT;
insert into ENROLMENT values (1, 1, 1, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (2, 1, 2, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (3, 1, 3, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (4, 1, 4, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (5, 1, 5, 1, 1, 'Dominio.Enrolment', null);

#insert into ENROLMENT values (6, 1, 6, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (7, 1, 7, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (8, 1, 8, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (9, 1, 9, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (10, 1, 10, 1, 1, 'Dominio.Enrolment', null);

#insert into ENROLMENT values (11, 1, 11, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (12, 1, 12, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (13, 1, 13, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (14, 1, 14, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (15, 1, 15, 1, 1, 'Dominio.Enrolment', null);

#insert into ENROLMENT values (16, 1, 16, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (17, 1, 17, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (18, 1, 18, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (19, 1, 19, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (20, 1, 20, 1, 1, 'Dominio.Enrolment', null);

#insert into ENROLMENT values (21, 1, 21, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (22, 1, 22, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (23, 1, 23, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (24, 1, 24, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (25, 1, 50, 1, 1, 'Dominio.Enrolment', null);

#insert into ENROLMENT values (26, 1, 27, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (27, 1, 28, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (28, 1, 29, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (29, 1, 30, 1, 1, 'Dominio.Enrolment', null);

#insert into ENROLMENT values (30, 1, 26, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (31, 1, 35, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (32, 1, 37, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (33, 1, 38, 1, 1, 'Dominio.Enrolment', null);

#insert into ENROLMENT values (34, 1, 42, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (35, 1, 43, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (36, 1, 44, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (37, 1, 45, 1, 1, 'Dominio.Enrolment', null);

#insert into ENROLMENT values (38, 1, 51, 1, 1, 'Dominio.EnrolmentInOptionalCurricularCourse', 25);
#insert into ENROLMENT values (39, 1, 52, 1, 1, 'Dominio.EnrolmentInOptionalCurricularCourse', 36);
#insert into ENROLMENT values (40, 1, 53, 1, 1, 'Dominio.Enrolment', null);

#insert into ENROLMENT values (41, 1, 54, 1, 1, 'Dominio.Enrolment', null);
#insert into ENROLMENT values (42, 1, 55, 1, 1, 'Dominio.EnrolmentInOptionalCurricularCourse', 39);


#-----------------------------
# Data for table 'PRECEDENCE'
#-----------------------------
# (ID_INTERNAL, KEY_CURRICULAR_COURSE, ENROLMENT_STEP)
;

delete from PRECEDENCE;
insert into PRECEDENCE values (1, 29, 'offline');
insert into PRECEDENCE values (2, 8, 'offline');
insert into PRECEDENCE values (3, 11, 'offline');
insert into PRECEDENCE values (4, 16, 'offline');
insert into PRECEDENCE values (5, 17, 'offline');
insert into PRECEDENCE values (6, 21, 'offline');
insert into PRECEDENCE values (7, 27, 'offline');
insert into PRECEDENCE values (8, 19, 'offline');
insert into PRECEDENCE values (9, 22, 'offline');
insert into PRECEDENCE values (10, 38, 'offline');
insert into PRECEDENCE values (11, 44, 'offline');
insert into PRECEDENCE values (12, 35, 'offline');
insert into PRECEDENCE values (13, 45, 'offline');
insert into PRECEDENCE values (14, 15, 'offline');
insert into PRECEDENCE values (15, 24, 'offline');
insert into PRECEDENCE values (16, 30, 'offline');
insert into PRECEDENCE values (17, 42, 'offline');
insert into PRECEDENCE values (18, 43, 'offline');
insert into PRECEDENCE values (19, 7, 'offline');
insert into PRECEDENCE values (20, 13, 'offline');
insert into PRECEDENCE values (21, 28, 'offline');
insert into PRECEDENCE values (22, 23, 'offline');
insert into PRECEDENCE values (23, 37, 'offline');
insert into PRECEDENCE values (24, 20, 'offline');
insert into PRECEDENCE values (25, 26, 'offline');
insert into PRECEDENCE values (26, 6, 'offline');
insert into PRECEDENCE values (27, 14, 'offline');
insert into PRECEDENCE values (28, 18, 'offline');
insert into PRECEDENCE values (29, 25, 'offline');
insert into PRECEDENCE values (30, 34, 'offline');
insert into PRECEDENCE values (31, 32, 'offline');
insert into PRECEDENCE values (32, 31, 'offline');
insert into PRECEDENCE values (33, 33, 'offline');
insert into PRECEDENCE values (34, 39, 'offline');
insert into PRECEDENCE values (35, 40, 'offline');
insert into PRECEDENCE values (36, 41, 'offline');
insert into PRECEDENCE values (37, 48, 'offline');
insert into PRECEDENCE values (38, 46, 'offline');
insert into PRECEDENCE values (39, 47, 'offline');

#-----------------------------
# Data for table 'RESTRICTION'
#-----------------------------
# (ID_INTERNAL, CLASS_NAME, KEY_PRECEDENCE, KEY_CURRICULAR_COURSE, NUMBER_OF_CURRICULAR_COURSE_DONE)
;

delete from RESTRICTION;
insert into RESTRICTION values (1, 'Dominio.CurricularCourseDoneRestriction', 1, 12, 0);
insert into RESTRICTION values (2, 'Dominio.CurricularCourseDoneRestriction', 1, 11, 0);
insert into RESTRICTION values (3, 'Dominio.CurricularCourseDoneRestriction', 2, 2, 0);
insert into RESTRICTION values (4, 'Dominio.CurricularCourseDoneRestriction', 3, 8, 0);
insert into RESTRICTION values (5, 'Dominio.CurricularCourseDoneRestriction', 4, 11, 0);
insert into RESTRICTION values (6, 'Dominio.CurricularCourseDoneRestriction', 5, 11, 0);
insert into RESTRICTION values (7, 'Dominio.CurricularCourseDoneRestriction', 6, 12, 0);
insert into RESTRICTION values (8, 'Dominio.CurricularCourseDoneRestriction', 6, 10, 0);
insert into RESTRICTION values (9, 'Dominio.CurricularCourseDoneRestriction', 7, 21, 0);
insert into RESTRICTION values (10, 'Dominio.CurricularCourseDoneRestriction', 8, 8, 0);
insert into RESTRICTION values (11, 'Dominio.CurricularCourseDoneRestriction', 9, 19, 0);
insert into RESTRICTION values (12, 'Dominio.CurricularCourseDoneRestriction', 9, 17, 0);
insert into RESTRICTION values (13, 'Dominio.CurricularCourseDoneRestriction', 10, 22, 0);
insert into RESTRICTION values (14, 'Dominio.CurricularCourseDoneRestriction', 11, 19, 0);
insert into RESTRICTION values (15, 'Dominio.CurricularCourseDoneRestriction', 12, 15, 0);
insert into RESTRICTION values (16, 'Dominio.CurricularCourseDoneRestriction', 12, 14, 0);
insert into RESTRICTION values (17, 'Dominio.CurricularCourseDoneRestriction', 13, 30, 0);
insert into RESTRICTION values (18, 'Dominio.CurricularCourseDoneRestriction', 13, 35, 0);
insert into RESTRICTION values (19, 'Dominio.CurricularCourseDoneRestriction', 14, 6, 0);
insert into RESTRICTION values (20, 'Dominio.CurricularCourseDoneRestriction', 15, 15, 0);
insert into RESTRICTION values (21, 'Dominio.CurricularCourseDoneRestriction', 15, 17, 0);
insert into RESTRICTION values (22, 'Dominio.CurricularCourseDoneRestriction', 16, 24, 0);
insert into RESTRICTION values (23, 'Dominio.CurricularCourseDoneRestriction', 17, 30, 0);
insert into RESTRICTION values (24, 'Dominio.CurricularCourseDoneRestriction', 18, 30, 0);
insert into RESTRICTION values (25, 'Dominio.CurricularCourseDoneRestriction', 19, 4, 0);
insert into RESTRICTION values (26, 'Dominio.CurricularCourseDoneRestriction', 20, 6, 0);
insert into RESTRICTION values (27, 'Dominio.CurricularCourseDoneRestriction', 21, 13, 0);
insert into RESTRICTION values (28, 'Dominio.CurricularCourseDoneRestriction', 22, 13, 0);
insert into RESTRICTION values (29, 'Dominio.CurricularCourseDoneRestriction', 22, 15, 0);
insert into RESTRICTION values (30, 'Dominio.CurricularCourseDoneRestriction', 23, 23, 0);
insert into RESTRICTION values (31, 'Dominio.CurricularCourseDoneRestriction', 24, 14, 0);
insert into RESTRICTION values (32, 'Dominio.CurricularCourseDoneRestriction', 25, 15, 0);
insert into RESTRICTION values (33, 'Dominio.CurricularCourseDoneRestriction', 26, 1, 0);
insert into RESTRICTION values (34, 'Dominio.CurricularCourseDoneRestriction', 27, 6, 0);
insert into RESTRICTION values (35, 'Dominio.CurricularCourseDoneRestriction', 28, 3, 0);
insert into RESTRICTION values (36, 'Dominio.CurricularCourseDoneRestriction', 28, 14, 0);
insert into RESTRICTION values (37, 'Dominio.CurricularCourseDoneRestriction', 29, 17, 0);
insert into RESTRICTION values (38, 'Dominio.CurricularCourseDoneRestriction', 29, 18, 0);
insert into RESTRICTION values (39, 'Dominio.CurricularCourseDoneRestriction', 30, 20, 0);
insert into RESTRICTION values (40, 'Dominio.CurricularCourseDoneRestriction', 31, 14, 0);
insert into RESTRICTION values (41, 'Dominio.CurricularCourseDoneRestriction', 31, 23, 0);
insert into RESTRICTION values (42, 'Dominio.CurricularCourseDoneRestriction', 32, 6, 0);
insert into RESTRICTION values (43, 'Dominio.CurricularCourseDoneRestriction', 33, 6, 0);
insert into RESTRICTION values (44, 'Dominio.CurricularCourseDoneRestriction', 33, 5, 0);
insert into RESTRICTION values (45, 'Dominio.CurricularCourseDoneRestriction', 34, 32, 0);
insert into RESTRICTION values (46, 'Dominio.CurricularCourseDoneRestriction', 35, 34, 0);
insert into RESTRICTION values (47, 'Dominio.CurricularCourseDoneRestriction', 36, 20, 0);
insert into RESTRICTION values (48, 'Dominio.CurricularCourseDoneRestriction', 37, 40, 0);
insert into RESTRICTION values (49, 'Dominio.CurricularCourseDoneRestriction', 37, 32, 0);
insert into RESTRICTION values (50, 'Dominio.CurricularCourseDoneRestriction', 38, 32, 0);
insert into RESTRICTION values (51, 'Dominio.CurricularCourseDoneRestriction', 39, 25, 0);

#-----------------------------
# Data for table 'ENROLMENT_PERIOD'
# (ID_INTERNAL, KEY_DEGREE_CURRICULAR_PLAN, KEY_EXECUTION_PERIOD, START_DATE, END_DATE) 
#-----------------------------
;
DELETE FROM ENROLMENT_PERIOD;
INSERT into ENROLMENT_PERIOD values (1, 1, 2, SYSDATE(), '2010-01-10');

# Isto e para sair
;
DELETE FROM EXECUTION_PERIOD;
INSERT INTO EXECUTION_PERIOD values (1, '1� Semestre', 1, 'NO', 1);
INSERT INTO EXECUTION_PERIOD values (2, '2� Semestre', 1, 'A', 2);
INSERT INTO EXECUTION_PERIOD values (3, '1� Semestre', 2, 'NO', 1);
INSERT INTO EXECUTION_PERIOD values (4, '2� Semestre', 2, 'NO', 2);
INSERT INTO EXECUTION_PERIOD values (5, '1� Semestre', 3, 'NO', 1);
INSERT INTO EXECUTION_PERIOD values (6, '2� Semestre', 3, 'NO', 2);
INSERT INTO EXECUTION_PERIOD values (7, '1� Semestre', 4, 'NO', 1);
INSERT INTO EXECUTION_PERIOD values (8, '2� Semestre', 4, 'NO', 2);
INSERT INTO EXECUTION_PERIOD values (9, '1� Semestre', 5, 'NO', 1);
INSERT INTO EXECUTION_PERIOD values (10, '2� Semestre', 5, 'NO', 2);
INSERT INTO EXECUTION_PERIOD values (11, '1� Semestre', 6, 'NO', 1);
INSERT INTO EXECUTION_PERIOD values (12, '2� Semestre', 6, 'NO', 2);
INSERT INTO EXECUTION_PERIOD values (13, '1� Semestre', 7, 'NO', 1);
INSERT INTO EXECUTION_PERIOD values (14, '2� Semestre', 7, 'NO', 2);
INSERT INTO EXECUTION_PERIOD values (15, '1� Semestre', 8, 'NO', 1);
INSERT INTO EXECUTION_PERIOD values (16, '2� Semestre', 8, 'NO', 2);

DELETE FROM EXECUTION_YEAR;
INSERT INTO EXECUTION_YEAR values (1, '2002/2003','A');
INSERT INTO EXECUTION_YEAR values (2, '2003/2004','NO');
INSERT INTO EXECUTION_YEAR values (3, '2005/2006','NO');
INSERT INTO EXECUTION_YEAR values (4, '2006/2007','NO');
INSERT INTO EXECUTION_YEAR values (5, '2007/2008','NO');
INSERT INTO EXECUTION_YEAR values (6, '2008/2009','NO');
INSERT INTO EXECUTION_YEAR values (7, '2009/2010','NO');
INSERT INTO EXECUTION_YEAR values (8, '2010/2011','NO');
