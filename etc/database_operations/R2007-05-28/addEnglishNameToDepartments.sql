ALTER TABLE `DEPARTMENT`
	ADD COLUMN `REAL_NAME_EN` VARCHAR(100)  NOT NULL AFTER `COMPETENCE_COURSE_MEMBERS_GROUP`;
	
UPDATE `DEPARTMENT`
	SET `REAL_NAME_EN` = 'Department of Electrical and Computer Engineering (DEEC)'
	WHERE `REAL_NAME`  = 'Departamento de Engenharia Electrot�cnica e de Computadores (DEEC)';
UPDATE `DEPARTMENT`
	SET `REAL_NAME_EN` = 'Departamento de Engenharia Qu�mica e Biol�gica (DEQB)'
	WHERE `REAL_NAME`  = 'Departamento de Engenharia Qu�mica e Biol�gica (DEQB)';
UPDATE `DEPARTMENT`
	SET `REAL_NAME_EN` = 'Departamento de Engenharia Civil e Arquitectura (DECivil)'
	WHERE `REAL_NAME`  = 'Departamento de Engenharia Civil e Arquitectura (DECivil)';
UPDATE `DEPARTMENT`
	SET `REAL_NAME_EN` = 'Mechanical Engineering Department (DEM)'
	WHERE `REAL_NAME`  = 'Departamento de Engenharia Mec�nica (DEM)';
UPDATE `DEPARTMENT`
	SET `REAL_NAME_EN` = 'Departamento de F�sica (DF)'
	WHERE `REAL_NAME`  = 'Departamento de F�sica (DF)';
UPDATE `DEPARTMENT`
	SET `REAL_NAME_EN` = 'Departamento de Engenharia de Minas e Georrecursos (DEMG)'
	WHERE `REAL_NAME`  = 'Departamento de Engenharia de Minas e Georrecursos (DEMG)';
UPDATE `DEPARTMENT`
	SET `REAL_NAME_EN` = 'Mathematics Department (DM)'
	WHERE `REAL_NAME`  = 'Departamento de Matem�tica (DM)';
UPDATE `DEPARTMENT`
	SET `REAL_NAME_EN` = 'Departamento de Engenharia de Materiais (DEMAT)'
	WHERE `REAL_NAME`  = 'Departamento de Engenharia de Materiais (DEMAT)';
UPDATE `DEPARTMENT`
	SET `REAL_NAME_EN` = 'Department of Information Systems and Computer Engineering (DEI)'
	WHERE `REAL_NAME`  = 'Departamento de Engenharia Inform�tica (DEI)';
UPDATE `DEPARTMENT`
	SET `REAL_NAME_EN` = 'Departament of Engineering and Management (DEG)'
	WHERE `REAL_NAME`  = 'Departamento de Engenharia e Gest�o (DEG)';
UPDATE `DEPARTMENT`
	SET `REAL_NAME_EN` = 'Naval Architecture and Marine Engineering (SAEN)'
	WHERE `REAL_NAME`  = 'Sec��o Aut�noma de Engenharia Naval (SAEN)';
