UPDATE `DEPARTMENT`
	SET `REAL_NAME_EN` = 'Department of Chemical and Biological Engineering (DEQB)'
	WHERE `REAL_NAME`  = 'Departamento de Engenharia Qu�mica e Biol�gica (DEQB)';
UPDATE `DEPARTMENT`
	SET `REAL_NAME_EN` = 'Department of Civil Engineering and Architecture (DECivil)'
	WHERE `REAL_NAME`  = 'Departamento de Engenharia Civil e Arquitectura (DECivil)';
UPDATE `DEPARTMENT`
	SET `REAL_NAME_EN` = 'Mechanical Engineering Department (DEM)'
	WHERE `REAL_NAME`  = 'Departamento de Engenharia Mec�nica (DEM)';
UPDATE `DEPARTMENT`
	SET `REAL_NAME_EN` = 'Departamento de F�sica (DF)'
	WHERE `REAL_NAME`  = 'Department of Physics (DF)';
UPDATE `DEPARTMENT`
	SET `REAL_NAME_EN` = 'Department of Mining and Earth Resources (DEMG)'
	WHERE `REAL_NAME`  = 'Departamento de Engenharia de Minas e Georrecursos (DEMG)';
UPDATE `DEPARTMENT`
	SET `REAL_NAME_EN` = 'Department of Materials Engineering (DEMAT)'
	WHERE `REAL_NAME`  = 'Departamento de Engenharia de Materiais (DEMAT)';

ALTER TABLE `PARTY` ADD COLUMN `NAME_EN` VARCHAR(255) DEFAULT NULL AFTER `NAME`;

UPDATE PARTY 
	SET NAME_EN = 'Hydraulics and Water and Environmental Resources Section'
	WHERE NAME  = 'Sec��o de Hidraulica e Recursos H�dricos e Ambientais';
UPDATE PARTY 
	SET NAME_EN = 'Structural Mechanics and Structures Section'
	WHERE NAME  = 'Sec��o de Mec�nica Estrutural e Estruturas';
UPDATE PARTY 
	SET NAME_EN = 'Systems and Aided Design Section'
	WHERE NAME  = 'Sec��o de Sistemas de Apoio ao Projecto';
UPDATE PARTY
	SET NAME_EN = 'Urban Planning, Transportation and Systems Section'
	WHERE NAME  = 'Sec��o de Urbanismo, Transportes, Vias e Sistemas';
UPDATE PARTY
	SET NAME_EN = 'Architecture Section'
	WHERE NAME  = 'Seccao de Arquitectura';
UPDATE PARTY
	SET NAME_EN = 'Construction Section' 
	WHERE NAME  = 'Seccao de Construcao';
UPDATE PARTY
	SET NAME_EN = 'Geotechnics Section' 
	WHERE NAME  = 'Seccao de Geotecnia';
UPDATE PARTY
	SET NAME_EN = 'Computers Scientific Area' 
	WHERE NAME  = '�rea Cient�fica de Computadores';
UPDATE PARTY
	SET NAME_EN = 'Electronics Scientific Area' 
	WHERE NAME  = '�rea Cient�fica de Electr�nica';
UPDATE PARTY
	SET NAME_EN = 'Energy Scientific Area' 
	WHERE NAME  = '�rea Cient�fica de Energia';
UPDATE PARTY
	SET NAME_EN = 'Systems, Decision and Control Scientific Area' 
	WHERE NAME  = '�rea Cient�fica de Sistemas, Decis�o e Controlo';
UPDATE PARTY
	SET NAME_EN = 'Telecommunications Scientific Area' 
	WHERE NAME  = '�rea Cient�fica de Telecomunica��es';
UPDATE PARTY
	SET NAME_EN = 'Mineralogy and Petrology Laboratory' 
	WHERE NAME  = 'Laborat�rio de Mineralogia e Petrologia';
UPDATE PARTY
	SET NAME_EN = 'Mining Exploitation Section' 
	WHERE NAME  = 'Sec��o de Explora��o de Minas';
UPDATE PARTY
	SET NAME_EN = 'Minerallurgy and Mining Planning Section' 
	WHERE NAME  = 'Laborat�rio de Mineralurgia e Planeamento Mineiro';
UPDATE PARTY 
	SET NAME_EN = 'Applied Geology Laboratory' 
	WHERE NAME  = 'Laborat�rio de Geologia Aplicada';
UPDATE PARTY 
	SET NAME_EN = 'Mechanical Design Section' 
	WHERE NAME  = 'Sec��o de Projecto Mec�nico';
UPDATE PARTY 
	SET NAME_EN = 'Systems Section' 
	WHERE NAME  = 'Sec��o de Sistemas';
UPDATE PARTY 
	SET NAME_EN = 'Mechanics Technology Section' 
	WHERE NAME  = 'Sec��o de Tecnologia Mec�nica';
UPDATE PARTY 
	SET NAME_EN = 'Thermofluids and Energy Section' 
	WHERE NAME  = 'Sec��o de Termofluidos e Energia';
UPDATE PARTY 
	SET NAME_EN = 'Environment and Energy Section' 
	WHERE NAME  = 'Sec��o de Ambiente e Energia';
UPDATE PARTY 
	SET NAME_EN = 'Aerospace Mechanics Section' 
	WHERE NAME  = 'Sec��o de Mec�nica Aeroespacial';
UPDATE PARTY 
	SET NAME_EN = 'Algebra and Mathematical Analysis Section' 
	WHERE NAME  = 'Sec��o de �lgebra e An�lise';
UPDATE PARTY
	SET NAME_EN = 'Computation Science Section'
	WHERE NAME  = 'Sec��o de Ci�ncia da Computa��o';
UPDATE PARTY
	SET NAME_EN = 'Statistics and Applications Section'
	WHERE NAME  = 'Sec��o de Estat�stica e Aplica��es';
UPDATE PARTY
	SET NAME_EN = 'Applied Mathematics and Numerical Analysis Section'
	WHERE NAME  = 'Sec��o de Matem�tica Aplicada e An�lise Num�rica';

