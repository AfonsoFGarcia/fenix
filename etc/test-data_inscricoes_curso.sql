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

-- -----------------------------
-- Data for table 'DEPARTMENT'
-- (ID_INTERNAL, NAME, CODE)
-- -----------------------------
delete from DEPARTMENT;
insert into DEPARTMENT values (1, 'Departamento de Engenharia Quimica', 'DEQ');

-- -----------------------------
-- Data for table 'DEPARTMENT_COURSE'
-- -----------------------------
-- (ID_INTERNAL, CODE, NAME, KEY_DEPARTMENT)
delete from DEPARTMENT_COURSE;
insert into DEPARTMENT_COURSE values (1, 'Disciplina Departamento', 'DD', 1);

-- -----------------------------
-- Data for table 'BRANCH'
-- (ID_INTERNAL, BRANCH_CODE, BRANCH_NAME)
-- -----------------------------
delete from BRANCH;
insert into BRANCH values (1, '', '');

-- -----------------------------
-- Data for table 'DEGREE'
-- (ID_INTERNAL, CODE, NAME, TYPE_DEGREE)
-- -----------------------------
delete from DEGREE;
insert into DEGREE values (1, 'LEQ', 'Licenciatura em Engenharia Quimica', 1);

-- -----------------------------
-- Data for table 'DEGREE_CURRICULAR_PLAN'
-- (ID_INTERNAL, NAME, KEY_DEGREE, STATE, INITIAL_DATE, END_DATE)
-- -----------------------------
delete from DEGREE_CURRICULAR_PLAN;
insert into DEGREE_CURRICULAR_PLAN values (1, 'LEQ-2003', 1, 1, '0000-00-00', '0000-00-00');

-- -----------------------------
-- Data for table 'STUDENT_CURRICULAR_PLAN'
-- (ID_INTERNAL, KEY_STUDENT, KEY_DEGREE_CURRICULAR_PLAN, CURRENT_STATE, START_DATE, KEY_BRANCH)
-- -----------------------------
delete from STUDENT_CURRICULAR_PLAN;
insert into STUDENT_CURRICULAR_PLAN values (1, 6, 1, 1, '0000-00-00', 1);

-- -----------------------------
-- Data for table 'CURRICULAR_COURSE'
-- (ID_INTERNAL, KEY_DEPARTMENT_COURSE, KEY_DEGREE_CURRICULAR_PLAN, CREDITS, THEORETICAL_HOURS, PRATICAL_HOURS, THEO_PRAT_HOURS, LAB_HOURS, NAME, CODE, TYPE)
-- -----------------------------
-- PRIMEIRO ANO, PRIMEIRO SEMESTRE:
delete from CURRICULAR_COURSE;
insert into CURRICULAR_COURSE values (1, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "INTRODU��O � QU�MICA-F�SICA", "D3", 1);
insert into CURRICULAR_COURSE values (2, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "AN�LISE MATEM�TICA I", "PY", 1);
insert into CURRICULAR_COURSE values (3, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "�LGEBRA LINEAR", "QN", 1);
insert into CURRICULAR_COURSE values (4, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "COMPUTA��O E PROGRAMA��O", "AZ9", 1);
insert into CURRICULAR_COURSE values (5, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "INTRODU��O � LIGA��O QU�MICA", "GU", 1);
insert into CURRICULAR_COURSE values (6, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "LABORAT�RIO DE QU�MICA GERAL I", "AGU", 1);
-- PRIMEIRO ANO, SEGUNDO SEMESTRE:
insert into CURRICULAR_COURSE values (7, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "QU�MICA ORG�NICA I", "HU", 1);
insert into CURRICULAR_COURSE values (8, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "MEC�NICA E ONDAS", "AZH", 1);
insert into CURRICULAR_COURSE values (9, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "AN�LISE MATEM�TICA II", "P5", 1);
insert into CURRICULAR_COURSE values (10, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "QU�MICA DAS SOLU��ES AQUOSAS", "AGW", 1);
insert into CURRICULAR_COURSE values (11, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "LABORAT�RIO DE QU�MICA GERAL II", "AGX", 1);
insert into CURRICULAR_COURSE values (12, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "PRINC�PIOS B�SICOS DE ENGENHARIA DE PROCESSOS", "AH2", 1);
-- SEGUNDO ANO, PRIMEIRO SEMESTRE:
insert into CURRICULAR_COURSE values (13, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "AN�LISE MATEM�TICA III", "UN", 1);
insert into CURRICULAR_COURSE values (14, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "TERMODIN�MICA QU�MICA", "LP", 1);
insert into CURRICULAR_COURSE values (15, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "PROBABILIDADES E ESTAT�STICA", "SF", 1);
insert into CURRICULAR_COURSE values (16, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "LABORAT�RIO DE QU�MICA ORG�NICA", "AIF", 1);
insert into CURRICULAR_COURSE values (17, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "QU�MICA ORG�NICA II", "AJM", 1);
insert into CURRICULAR_COURSE values (18, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "ELECTROMAGNETISMO E �PTICA", "AZI", 1);
-- SEGUNDO ANO, SEGUNDO SEMESTRE:
insert into CURRICULAR_COURSE values (19, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "FEN�MENOS DE TRANSFER�NCIA I", "C4", 1);
insert into CURRICULAR_COURSE values (20, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "AN�LISE MATEM�TICA IV", "U8", 1);
insert into CURRICULAR_COURSE values (21, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "QU�MICA F�SICA", "XW", 1);
insert into CURRICULAR_COURSE values (22, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "TERMODIN�MICA DE ENGENHARIA QU�MICA", "AII", 1);
insert into CURRICULAR_COURSE values (23, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "PROCESSOS DE ENGENHARIA QU�MICA I", "AIK", 1);
insert into CURRICULAR_COURSE values (24, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "LABORAT�RIO DE ENGENHARIA QU�MICA I", "AJL", 1);
-- TERCEIRO ANO, PRIMEIRO SEMESTRE:
insert into CURRICULAR_COURSE values (25, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "AN�LISE QU�MICA", "AN", 1);
insert into CURRICULAR_COURSE values (26, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "FEN�MENOS DE TRANSFER�NCIA II", "C5", 1);
insert into CURRICULAR_COURSE values (27, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "AN�LISE E SIMULA��O NUM�RICA", "AZ7", 1);
insert into CURRICULAR_COURSE values (28, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "PROCESSOS DE ENGENHARIA QU�MICA II", "AL6", 1);
insert into CURRICULAR_COURSE values (29, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "LABORAT�RIO DE ENGENHARIA QU�MICA II", "AL7", 1);
insert into CURRICULAR_COURSE values (30, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "MATERIAIS E CORROS�O", "AMD", 1);
-- TERCEIRO ANO, SEGUNDO SEMESTRE:
insert into CURRICULAR_COURSE values (31, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "OPERA��ES EM SISTEMAS MULTIF�SICOS", "AL8", 1);
insert into CURRICULAR_COURSE values (32, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "LABORAT�RIO DE ENGENHARIA QU�MICA III", "AL9", 1);
insert into CURRICULAR_COURSE values (33, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "ENGENHARIA DAS REAC��ES I", "AME", 1);
insert into CURRICULAR_COURSE values (34, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "FEN�MENOS DE TRANSFER�NCIA III", "AMG", 1);
insert into CURRICULAR_COURSE values (35, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "PROCESSOS DE SEPARA��O I", "AMH", 1);
insert into CURRICULAR_COURSE values (36, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "FUNDAMENTOS DE GEST�O", "AX2", 1);
-- QUARTO ANO, PRIMEIRO SEMESTRE:
insert into CURRICULAR_COURSE values (37, 1, 1, 4.0, 3.0, 2.0, 0.0, 2.0, "OPTIMIZA��O DE PROCESSOS", "7W", 1);
insert into CURRICULAR_COURSE values (38, 1, 1, 4.0, 3.0, 0.0, 2.0, 2.0, "CONTROLO E INSTRUMENTA��O DE PROCESSOS", "APK", 1);
insert into CURRICULAR_COURSE values (39, 1, 1, 4.0, 3.0, 1.0, 2.0, 2.0, "LABORAT�RIOS DE ENGENHARIA QU�MICA IV", "APL", 1);
insert into CURRICULAR_COURSE values (40, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "ENGENHARIA DAS REAC��ES II", "AR7", 1);
insert into CURRICULAR_COURSE values (41, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "PROCESSOS DE SEPARA��O II", "AR8", 1);
insert into CURRICULAR_COURSE values (42, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "OP��O I", "", 2);
-- QUARTO ANO, SEGUNDO SEMESTRE:
insert into CURRICULAR_COURSE values (43, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "ENGENHARIA QU�MICA INTEGRADA I", "APM", 1);
insert into CURRICULAR_COURSE values (44, 1, 1, 4.0, 3.0, 0.0, 0.0, 0.0, "S�NTESE E INTEGRA��O DE PROCESSOS", "APO", 1);
insert into CURRICULAR_COURSE values (45, 1, 1, 4.0, 3.0, 0.0, 3.0, 0.0, "LABORAT�RIOS DE ENGENHARIA QU�MICA V", "APP", 1);
insert into CURRICULAR_COURSE values (46, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "TECNOLOGIA AMBIENTAL", "APQ", 1);
insert into CURRICULAR_COURSE values (47, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "DIMENSIONAMENTO E OPTIMIZA��O DE EQUIPAMENTOS E UTILIDADES", "APN", 1);
insert into CURRICULAR_COURSE values (48, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "OP��O II", "", 2);
-- QUINTO ANO, PRIMEIRO SEMESTRE:
insert into CURRICULAR_COURSE values (49, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "PROJECTO DE IND�STRIAS QU�MICAS", "AB3", 1);
insert into CURRICULAR_COURSE values (50, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "ENGENHARIA QU�MICA INTEGRADA II", "AV6", 1);
insert into CURRICULAR_COURSE values (51, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "OP��O III", "", 2);
-- QUINTO ANO, SEGUNDO SEMESTRE:
insert into CURRICULAR_COURSE values (52, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "EST�GIO", "AV2", 1);
-- QUARTO ANO, PRIMEIRO SEMESTRE, OP��ES:
insert into CURRICULAR_COURSE values (53, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "RISCOS NATURAIS E TECNOL�GICOS", "AH0", 1);
insert into CURRICULAR_COURSE values (54, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "GEST�O PELA QUALIDADE TOTAL", "ALE", 1);
insert into CURRICULAR_COURSE values (55, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "AN�LISES INDUSTRIAIS E CONTROLO", "AV7", 1);
insert into CURRICULAR_COURSE values (56, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "LIMITES DA CI�NCIA", "AXM", 1);
insert into CURRICULAR_COURSE values (57, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "BIOTECNOLOGIA", "AB6", 1);
insert into CURRICULAR_COURSE values (58, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "QU�MICA INDUSTRIAL", "AOZ", 1);
insert into CURRICULAR_COURSE values (59, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "ESTIMATIVA DE PROPRIEDADES PARA ENGENHARIA DE PROCESSOS", "", 1);
-- QUARTO ANO, SEGUNDO SEMESTRE, OP��ES:
insert into CURRICULAR_COURSE values (60, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "INVESTIGA��O OPERACIONAL", "EA", 1);
insert into CURRICULAR_COURSE values (61, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "PREVIS�O DE PROPRIEDADES", "ZC", 1);
insert into CURRICULAR_COURSE values (62, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "TRATAMENTO DE EFLUENTES GASOSOS", "AEV", 1);
insert into CURRICULAR_COURSE values (63, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "ESTUDOS DE CI�NCIA:ARTE,TECNOLOGIA E SOCIEDADE", "AP9", 1);
insert into CURRICULAR_COURSE values (64, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "GEST�O, TRATAMENTO E VALORIZA��O DE RES�DUOS", "APR", 1);
insert into CURRICULAR_COURSE values (65, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "SUPERVIS�O E DIAGN�STICO DE PROCESSOS", "APU", 1);
insert into CURRICULAR_COURSE values (66, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "GEST�O DA PRODU��O E DAS OPERA��ES", "APW", 1);
insert into CURRICULAR_COURSE values (67, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "SISTEMAS DE GEST�O AMBIENTAL", "AV8", 1);
insert into CURRICULAR_COURSE values (68, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "MODELA��O E OPTIMIZA��O DE SISTEMAS DIN�MICOS", "AX3", 1);
insert into CURRICULAR_COURSE values (69, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "CI�NCIA E TECNOLOGIA DE POL�MEROS", "AXN", 1);
-- QUINTO ANO, PRIMEIRO SEMESTRE, OP��ES:
insert into CURRICULAR_COURSE values (70, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "TECNOLOGIA ALIMENTAR", "AI", 1);
insert into CURRICULAR_COURSE values (71, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "REFINA��O DE PETR�LEOS E PETROQU�MICA", "IF", 1);
insert into CURRICULAR_COURSE values (72, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "MODELOS MULTICRIT�RIO DE APOIO � DECIS�O", "AJA", 1);
insert into CURRICULAR_COURSE values (73, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "SEGURAN�A E HIGIENE INDUSTRIAL", "APB", 1);
insert into CURRICULAR_COURSE values (74, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "CONTROLO DE QUALIDADE", "APS", 1);
insert into CURRICULAR_COURSE values (75, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "CONTROLO AVAN�ADO DE PROCESSOS", "APT", 1);
insert into CURRICULAR_COURSE values (76, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "CARACTER�STICAS E TRATAMENTO DE �GUAS", "AET", 1);
insert into CURRICULAR_COURSE values (77, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "ENGENHARIA DE SUPERF�CIES", "APY", 1);

-- -----------------------------
-- Data for table 'CURRICULAR_COURSE_SCOPE'
-- (ID_INTERNAL, KEY_CURRICULAR_SEMESTER, KEY_CURRICULAR_COURSE, KEY_BRANCH, THEORETICAL_HOURS, PRATICAL_HOURS, THEO_PRAT_HOURS, LAB_HOURS, SCOPE_TYPE)
-- -----------------------------
delete from CURRICULAR_COURSE_SCOPE;
insert into CURRICULAR_COURSE_SCOPE  values (1, 1, 1, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (2, 1, 2, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (3, 1, 3, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (4, 1, 4, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (5, 1, 5, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (6, 1, 6, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (7, 2, 7, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (8, 2, 8, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (9, 2, 9, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (10, 2, 10, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (11, 2, 11, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (12, 2, 12, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (13, 3, 13, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (14, 3, 14, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (15, 3, 15, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (16, 3, 16, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (17, 3, 17, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (18, 3, 18, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (19, 4, 19, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (20, 4, 20, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (21, 4, 21, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (22, 4, 22, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (23, 4, 23, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (24, 4, 24, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (25, 5, 25, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (26, 5, 26, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (27, 5, 27, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (28, 5, 28, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (29, 5, 29, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (30, 5, 30, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (31, 6, 31, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (32, 6, 32, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (33, 6, 33, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (34, 6, 34, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (35, 6, 35, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (36, 6, 36, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (37, 7, 37, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (38, 7, 38, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (39, 7, 39, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (40, 7, 40, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (41, 7, 41, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (42, 7, 42, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (43, 8, 43, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (44, 8, 44, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (45, 8, 45, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (46, 8, 46, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (47, 8, 47, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (48, 8, 48, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (49, 9, 49, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (50, 9, 50, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (51, 9, 51, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (52, 10, 52, 1, 0.0, 0.0, 0.0, 0.0, 0);

-- bianuais matem�ticas
insert into CURRICULAR_COURSE_SCOPE  values (53, 2, 2, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (54, 2, 3, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (55, 1, 9, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (56, 4, 13, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (57, 3, 20, 1, 0.0, 0.0, 0.0, 0.0, 2);

-- bianuais quimica
insert into CURRICULAR_COURSE_SCOPE  values (58, 2, 1, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (59, 1, 10, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (60, 1, 7, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (61, 4, 17, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (62, 4, 14, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (63, 3, 22, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (64, 3, 19, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (65, 6, 26, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (66, 5, 34, 1, 0.0, 0.0, 0.0, 0.0, 0);

-- -----------------------------
-- Data for table 'POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE'
-- (ID_INTERNAL, KEY_POSSIBLE_CURRICULAR_COURSE, KEY_OPTIONAL_CURRICULAR_COURSE)
-- -----------------------------
delete from POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE;
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (1, 53, 42);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (2, 54, 42);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (3, 55, 42);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (4, 56, 42);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (5, 57, 42);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (6, 58, 42);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (7, 59, 42);

insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (8, 60, 48);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (9, 61, 48);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (10, 62, 48);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (11, 63, 48);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (12, 64, 48);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (13, 65, 48);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (14, 66, 48);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (15, 67, 48);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (16, 68, 48);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (17, 69, 48);

insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (18, 70, 51);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (19, 71, 51);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (20, 72, 51);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (21, 73, 51);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (22, 74, 51);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (23, 75, 51);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (24, 76, 51);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (25, 77, 51);

-- -----------------------------
-- Data for table 'ENROLMENT'
-- (ID_INTERNAL, KEY_STUDENT_CURRICULAR_PLAN, KEY_CURRICULAR_COURSE, KEY_EXECUTION_PERIOD, STATE)
-- -----------------------------
delete from ENROLMENT;
insert into ENROLMENT values (1, 1, 1, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (2, 1, 2, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (3, 1, 3, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (4, 1, 4, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (5, 1, 5, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (6, 1, 6, 1, 1, 'Dominio.Enrolment', null);

insert into ENROLMENT values (7, 1, 7, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (8, 1, 8, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (9, 1, 9, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (10, 1, 10, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (11, 1, 11, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (12, 1, 12, 1, 1, 'Dominio.Enrolment', null);

insert into ENROLMENT values (13, 1, 13, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (14, 1, 14, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (15, 1, 15, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (16, 1, 16, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (17, 1, 17, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (18, 1, 18, 1, 1, 'Dominio.Enrolment', null);

insert into ENROLMENT values (19, 1, 19, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (20, 1, 20, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (21, 1, 21, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (22, 1, 22, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (23, 1, 23, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (24, 1, 24, 1, 1, 'Dominio.Enrolment', null);

insert into ENROLMENT values (25, 1, 25, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (26, 1, 26, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (27, 1, 27, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (28, 1, 28, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (29, 1, 29, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (30, 1, 30, 1, 1, 'Dominio.Enrolment', null);

insert into ENROLMENT values (31, 1, 31, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (32, 1, 32, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (33, 1, 33, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (34, 1, 34, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (35, 1, 35, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (36, 1, 36, 1, 1, 'Dominio.Enrolment', null);

-- insert into ENROLMENT values (37, 1, 37, 1, 1, 'Dominio.Enrolment', null);
-- insert into ENROLMENT values (38, 1, 38, 1, 1, 'Dominio.Enrolment', null);
-- insert into ENROLMENT values (39, 1, 39, 1, 1, 'Dominio.Enrolment', null);
-- insert into ENROLMENT values (40, 1, 40, 1, 1, 'Dominio.Enrolment', null);
-- insert into ENROLMENT values (41, 1, 41, 1, 1, 'Dominio.Enrolment', null);
-- insert into ENROLMENT values (42, 1, 42, 1, 1, 'Dominio.Enrolment', null);

-- insert into ENROLMENT values (43, 1, 43, 1, 1, 'Dominio.Enrolment', null);
-- insert into ENROLMENT values (44, 1, 44, 1, 1, 'Dominio.Enrolment', null);
-- insert into ENROLMENT values (45, 1, 45, 1, 1, 'Dominio.Enrolment', null);
-- insert into ENROLMENT values (46, 1, 46, 1, 1, 'Dominio.Enrolment', null);
-- insert into ENROLMENT values (47, 1, 47, 1, 1, 'Dominio.Enrolment', null);
-- insert into ENROLMENT values (48, 1, 48, 1, 1, 'Dominio.Enrolment', null);

-- -----------------------------
-- Data for table 'DEGREE_ENROLMENT_INFO'
-- (ID_INTERNAL, KEY_DEGREE_CURRICULAR_PLAN, DEGREE_DURATION, MINIMAL_YEAR_FOR_OPTIONAL_COURSES, END_DATE) 
-- -----------------------------
delete from DEGREE_CURRICULAR_PLAN_ENROLMENT_INFO;
insert into DEGREE_CURRICULAR_PLAN_ENROLMENT_INFO values (1, 1, 5, 3);

-- -----------------------------
-- Data for table 'STUDENT_GROUP_INFO'
-- (ID_INTERNAL, STUDENT_TYPE, MIN_COURSES_TO_ENROL, MAX_COURSES_TO_ENROL, MAX_NAC_TO_ENROL) 
-- -----------------------------
delete from STUDENT_GROUP_INFO;
insert into STUDENT_GROUP_INFO values (1, 1, 3, 7, 10);

-- -----------------------------
-- Data for table 'ENROLMENT_PERIOD'
-- (ID_INTERNAL, KEY_DEGREE_CURRICULAR_PLAN, KEY_EXECUTION_PERIOD, START_DATE, END_DATE) 
-- -----------------------------
delete from ENROLMENT_PERIOD;
insert into ENROLMENT_PERIOD values (1, 1, 1, SYSDATE(), '2010-01-10');

-- Isto e para sair
delete from EXECUTION_PERIOD;
insert into EXECUTION_PERIOD values (1, '1� Semestre', 1, 'A', 1);
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
insert into EXECUTION_YEAR values (1, '2002/2003','A');
insert into EXECUTION_YEAR values (2, '2003/2004','NO');
insert into EXECUTION_YEAR values (3, '2005/2006','NO');
insert into EXECUTION_YEAR values (4, '2006/2007','NO');
insert into EXECUTION_YEAR values (5, '2007/2008','NO');
insert into EXECUTION_YEAR values (6, '2008/2009','NO');
insert into EXECUTION_YEAR values (7, '2009/2010','NO');
insert into EXECUTION_YEAR values (8, '2010/2011','NO');

-- para sair at� aqui
