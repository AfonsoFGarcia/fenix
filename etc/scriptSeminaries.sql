-- -----------------------------
-- Data for table 'SEMINARY'
-- -----------------------------
delete from SEMINARY;
insert into SEMINARY values (1, 'Desenvolvimento Sustentavel','Ano lectivo de 2003 / 2004',1);
insert into SEMINARY values (2, 'Clonagem humana a altas temperaturas','Ano 2042, orador: Gon�alo Mengel Luiz',3);
insert into SEMINARY values (3, 'Pontes e T�neis','Ano 1940, orador: Hintze Ribeiro',2);

-- -----------------------------
-- Data for table 'SEMINARY_MODALITY'
-- -----------------------------
delete from SEMINARY_MODALITY;
insert into SEMINARY_MODALITY values (1, 'Completa','O aluno frequenta os tr�s Semin�rios com cerca de 50 horas lectivas e elabora um trabalho de grupo de estudo dum caso, que ser� discutido e avaliado. Esta modalidade poder� ser oferecida como conte�do poss�vel para uma disciplina de licenciatura ou de programa de p�s-gradua��o, desde que tal tenha as autoriza��es devidas no respectivo �mbito. Para todos os efeitos formais tudo se passar� como se o aluno tivesse frequentado a disciplina original.');
-- -----------------------------
-- Data for table 'SEMINARY_THEME'
-- -----------------------------
delete from SEMINARY_THEME;
insert into SEMINARY_THEME values (1, 'O Mar','M','');
insert into SEMINARY_THEME values (2, 'O Sistema Produtivo','SP','');
insert into SEMINARY_THEME values (3, 'O Territorio','T','');

-- -----------------------------
-- Data for table 'SEMINARY_CASESTUDY'
-- -----------------------------
delete from SEMINARY_CASESTUDY;
insert into SEMINARY_CASESTUDY values (101, 'A evolu��o das capturas e frota de pesca em Portugal � stocks e sustentabilidade',NULL,'M.1',1);
insert into SEMINARY_CASESTUDY values (102, 'Modeliza��o de recursos pisc�colas',NULL,'M.2',1);
insert into SEMINARY_CASESTUDY values (103, 'Aplicabilidade das auto-estradas mar�timas no com�rcio externo portugu�s',NULL,'M.3',1);
insert into SEMINARY_CASESTUDY values (104, 'Perspectivas de desenvolvimento do transporte mar�timo de curta dist�ncia em Portugal',NULL,'M.4',1);
insert into SEMINARY_CASESTUDY values (105, 'Marina de Lisboa � America�s Cup: avalia��o do potencial',NULL,'M.5',1);
insert into SEMINARY_CASESTUDY values (106, 'Modeliza��o sustent�vel do sector mar�timo tur�stico',NULL,'M.6',1);
insert into SEMINARY_CASESTUDY values (107, 'Modela��o e monitoriza��o na gest�o do ambiente marinho',NULL,'M.7',1);
insert into SEMINARY_CASESTUDY values (108, 'Eutrofiza��o na costa portuguesa',NULL,'M.8',1);
insert into SEMINARY_CASESTUDY values (109, 'Estrat�gias de gest�o da linha de costa portuguesa.',NULL,'M.9',1);
insert into SEMINARY_CASESTUDY values (110, 'Avalia��o dos riscos de polui��o por hidrocarbonetos nas costas portuguesas.',NULL,'M.10',1);
insert into SEMINARY_CASESTUDY values (111, 'Estrat�gia de combate � polui��o por hidrocarbonetos.',NULL,'M.11',1);
insert into SEMINARY_CASESTUDY values (112, 'Business Inteligence na �rea do controlo ambiental e seguran�a mar�tima',NULL,'M.12',1);
insert into SEMINARY_CASESTUDY values (113, 'Tecnologias para inspec��o de estruturas costeiras',NULL,'M.13',1);
insert into SEMINARY_CASESTUDY values (114, 'Tecnologias para estudo e explora��o do mar profundo',NULL,'M.14',1);
insert into SEMINARY_CASESTUDY values (115, 'Barreiras e potencial da energia das ondas em Portugal',NULL,'M.15',1);
insert into SEMINARY_CASESTUDY values (4, 'Caso 1 Tema 2',NULL,'T.1',2);
insert into SEMINARY_CASESTUDY values (5, 'Caso 2 Tema 2',NULL,'T.2',2);
insert into SEMINARY_CASESTUDY values (6, 'Caso 3 Tema 2',NULL,'T.3',2);
insert into SEMINARY_CASESTUDY values (7, 'Caso 1 Tema 3',NULL,'T.1',3);
insert into SEMINARY_CASESTUDY values (8, 'Caso 2 Tema 3',NULL,'T.2',3);
insert into SEMINARY_CASESTUDY values (9, 'Caso 3 Tema 3',NULL,'T.3',3);


-- -----------------------------
-- Data for table 'SEMINARY_CURRICULARCOURSE'
-- -----------------------------
delete from SEMINARY_CURRICULARCOURSE;
insert into SEMINARY_CURRICULARCOURSE values (1,1,666,1);
insert into SEMINARY_CURRICULARCOURSE values (2,1,666,2);
insert into SEMINARY_CURRICULARCOURSE values (3,1,666,3);
insert into SEMINARY_CURRICULARCOURSE values (4,1,701,1);
insert into SEMINARY_CURRICULARCOURSE values (5,1,701,2);

-- -----------------------------
-- Data for table 'EQUIVALENCY_THEME'
-- -----------------------------
delete from EQUIVALENCY_THEME;
insert into EQUIVALENCY_THEME values (1,1);
insert into EQUIVALENCY_THEME values (1,2);
insert into EQUIVALENCY_THEME values (1,3);
insert into EQUIVALENCY_THEME values (2,1);
insert into EQUIVALENCY_THEME values (2,2);
insert into EQUIVALENCY_THEME values (2,3);
insert into EQUIVALENCY_THEME values (2,4);
insert into EQUIVALENCY_THEME values (2,5);
insert into EQUIVALENCY_THEME values (3,1);
insert into EQUIVALENCY_THEME values (3,3);