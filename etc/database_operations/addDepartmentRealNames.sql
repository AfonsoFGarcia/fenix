alter table DEPARTMENT add column REAL_NAME varchar(100) default '';

update DEPARTMENT set DEPARTMENT.NAME = 'SECCAO AUTONOMA DE ENGENHARIA NAVAL' where DEPARTMENT.NAME = 'SEC. AUTONOMA DE ENGENHARIA NAVAL';

update DEPARTMENT set REAL_NAME = 'Departamento de F�sica (DF)' where DEPARTMENT.NAME = 'DEPARTAMENTO DE FISICA';
update DEPARTMENT set REAL_NAME = 'Departamento de Engenharia Electrot�cnica e de Computadores (DEEC)' where DEPARTMENT.NAME = 'DEP. ENG. ELECT. E COMPUTADORES';
update DEPARTMENT set REAL_NAME = 'Departamento de Matem�tica (DM)' where DEPARTMENT.NAME = 'DEPARTAMENTO DE MATEMATICA';
update DEPARTMENT set REAL_NAME = 'Departamento de Engenharia Qu�mica (DQ)' where DEPARTMENT.NAME = 'DEPARTAMENTO DE ENGENHARIA QUIMICA';
update DEPARTMENT set REAL_NAME = 'Departamento de Engenharia Mec�nica (DEM)' where DEPARTMENT.NAME = 'DEP. DE ENGENHARIA MECANICA';
update DEPARTMENT set REAL_NAME = 'Departamento de Engenharia de Materiais (DEMAT)' where DEPARTMENT.NAME = 'DEPART. DE ENGENHARIA DE MATERIAIS';
update DEPARTMENT set REAL_NAME = 'Departamento de Engenharia Civil e Arquitectura (DECivil)' where DEPARTMENT.NAME = 'DEPARTAMENTO DE ENGENHARIA CIVIL';
update DEPARTMENT set REAL_NAME = 'Departamento de Engenharia de Minas e Georrecursos (DEMG)' where DEPARTMENT.NAME = 'DEPARTAMENTO DE ENGENHARIA MINAS';
update DEPARTMENT set REAL_NAME = 'Departamento de Engenharia Inform�tica (DEI)' where DEPARTMENT.NAME = 'DEPARTAMENTO DE ENG. INFORMATICA';
update DEPARTMENT set REAL_NAME = 'Departamento de Engenharia e Gest�o (DEG)' where DEPARTMENT.NAME = 'DEPARTAMENTO DE ENGENHARIA E GESTAO';
update DEPARTMENT set REAL_NAME = 'Sec��o Aut�noma de Engenharia Naval (SAEN)' where DEPARTMENT.NAME = 'SEC. AUTONOMA DE ENGENHARIA NAVAL';