
-- -----------------------------
-- Data for table 'SEMINARY'
-- -----------------------------
delete from SEMINARY;
insert into SEMINARY values (1, 'Desenvolvimento Sustent�vel','Ano lectivo de 2003 / 2004',1);

-- -----------------------------
-- Data for table 'SEMINARY_MODALITY'
-- -----------------------------
delete from SEMINARY_MODALITY;
insert into SEMINARY_MODALITY values (1, 'Completa','O aluno frequenta os tr�s Semin�rios com cerca de 50 horas lectivas e elabora um trabalho de grupo de estudo dum caso, que ser� discutido e avaliado. Esta modalidade poder� ser oferecida como conte�do poss�vel para uma disciplina de licenciatura ou de programa de p�s-gradua��o, desde que tal tenha as autoriza��es devidas no respectivo �mbito. Para todos os efeitos formais tudo se passar� como se o aluno tivesse frequentado a disciplina original.');
insert into SEMINARY_MODALITY values (2, 'Semin�rio com Trabalho','O aluno frequenta um semin�rio com cerca de 15 a 17 horas lectivas e elabora um trabalho de grupo de estudo dum caso, que ser� discutido e avaliado. Esta modalidade poder� ser oferecida como conte�do poss�vel de um Semin�rio de p�s-gradua��o ou de parte de uma disciplina de licenciatura ou programa de p�s-gradua��o, desde que tal tenha as autoriza��es devidas no respectivo �mbito. De novo, tudo se passar� formalmente como no contexto da disciplina ou Semin�rio originais.');
insert into SEMINARY_MODALITY values (3, 'Semin�rio','O aluno frequentar� a parte lectiva de um semin�rio, como alternativa a um dos Semin�rios de Humanidades de Engenharia Civil, contando para o portfolio de Engenharia Inform�tica, ou para efeitos semelhantes noutros eventuais cursos.');

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
insert into SEMINARY_CASESTUDY values (201, 'Pol�ticas locais de mobilidade � compara��o entre �vora e Montpellier',NULL,'T.1',2);
insert into SEMINARY_CASESTUDY values (202, 'As promessas da tecnologia � o autom�vel com pilha de combust�vel',NULL,'T.2',2);
insert into SEMINARY_CASESTUDY values (203, 'Sistemas inteligentes de transportes',NULL,'T.3',2);
insert into SEMINARY_CASESTUDY values (204, 'Sistemas de bilh�tica inteligente',NULL,'T.4',2);
insert into SEMINARY_CASESTUDY values (205, 'As TI�s como factor de inclus�o social',NULL,'T.5',2);
insert into SEMINARY_CASESTUDY values (206, 'Estudo da rela��o entre a press�o humana e servi�os de ecossistema em Portugal enquadrado pelo Millenium Ecosystem Assessment',NULL,'T.6',2);
insert into SEMINARY_CASESTUDY values (207, 'Avalia��o do potencial de integra��o de energia solar fotovoltaica em edif�cios em Portugal',NULL,'T.7',2);
insert into SEMINARY_CASESTUDY values (208, 'Edif�cios inteligentes',NULL,'T.8',2);
insert into SEMINARY_CASESTUDY values (209, 'A pegada ecol�gica � a diversidade nacional: compara��o entre o Minho e o Algarve',NULL,'T.9',2);
insert into SEMINARY_CASESTUDY values (210, 'O espa�o urbano do estu�rio do Tejo � liga��es entre as duas margens',NULL,'T.10',2);
insert into SEMINARY_CASESTUDY values (211, 'O mosaico urbano da Cova da Beira',NULL,'T.11',2);
insert into SEMINARY_CASESTUDY values (212, 'A inser��o do porto de Set�bal na cidade',NULL,'T.12',2);
insert into SEMINARY_CASESTUDY values (213, 'Os recursos h�dricos em Portugal. Explora��o sustent�vel',NULL,'T.13',2);
insert into SEMINARY_CASESTUDY values (214, 'Indicadores de desertifica��o. Margem esquerda do Guadiana',NULL,'T.14',2);

insert into SEMINARY_CASESTUDY values (301, 'Estrat�gias de optimiza��o dos processos de recupera��o e reciclagem de metais ferrosos e n�o ferrosos provenientes de VFV (ve�culos em fim de vida), REEE (res�duos de equipamentos el�ctricos e electr�nicos) e outros res�duos fragmentados',NULL,'SP.1',3);
insert into SEMINARY_CASESTUDY values (302, 'Estrat�gias para o desenvolvimento da ind�stria fotovoltaica em Portugal',NULL,'SP.2',3);
insert into SEMINARY_CASESTUDY values (303, 'Estrat�gias de reciclagem para pol�meros e materiais comp�sitos indiferenciados',NULL,'SP.3',3);
insert into SEMINARY_CASESTUDY values (304, 'Estrat�gias de desenvolvimentos da gest�o de res�duos s�lidos em Portugal � o contributo de uma Bolsa de Res�duos',NULL,'SP.4',3);
insert into SEMINARY_CASESTUDY values (305, 'Sistema de informa��o para apoio � gest�o de VFV em Portugal',NULL,'SP.5',3);
insert into SEMINARY_CASESTUDY values (306, 'Potencialidades e limita��es da separa��o mec�nica de res�duos s�lidos urbanos (RSU)',NULL,'SP.6',3);
insert into SEMINARY_CASESTUDY values (307, 'O sistema ponto verde � balan�o.',NULL,'SP.7',3);
insert into SEMINARY_CASESTUDY values (308, 'O hidrog�nio como vector energ�tico',NULL,'SP.8',3);
insert into SEMINARY_CASESTUDY values (309, 'Certificados verdes. Perspectiva de impacto futuro.',NULL,'SP.9',3);
insert into SEMINARY_CASESTUDY values (310, 'Emiss�es na ind�stria cimenteira-perspectivas futuras',NULL,'SP.10',3);
insert into SEMINARY_CASESTUDY values (311, 'O ciclo da floresta em Portugal',NULL,'SP.11',3);
insert into SEMINARY_CASESTUDY values (312, 'Impacte socio-econ�mico e ambiental da Fus�o Nuclear',NULL,'SP.12',3);
insert into SEMINARY_CASESTUDY values (313, 'Tratamento de res�duos e produ��o de energia.',NULL,'SP.13',3);


-- -----------------------------
-- Data for table 'SEMINARY_CURRICULARCOURSE'
-- -----------------------------

insert into CURRICULAR_COURSE (id_internal,name, code, key_degree_curricular_plan) values
	(4918,"COMPLEMENTOS DE ENGENHARIA NAVAL II","A2J",113),
	(4919,"COMPLEMENTOS DE ENGENHARIA NAVAL I","A2I",113),
	(4920,"Semin�rios em Humanidades","##1",51),
	(4921,"Op��o Livre","##2",48),
	(4922,"DESENVOLVIMENTO SUSTENT�VEL","B1S",108),
	(4923,"DESENVOLVIMENTO SUSTENT�VEL DO DEG","##3",88),
	(4924,"Op��o","##4",91),
	(4925,"Desenvolvimento Sustent�vel e de Inova��o","##5",119),
	(4926,"Op��o (2� Semestre)","##6",127);

delete from SEMINARY_CURRICULARCOURSE;
insert into SEMINARY_CURRICULARCOURSE values (1,1,3842,2); 
insert into SEMINARY_CURRICULARCOURSE values (2,1,3841,2);
insert into SEMINARY_CURRICULARCOURSE values (3,1,3815,2);
insert into SEMINARY_CURRICULARCOURSE values (4,1,3769,2);
insert into SEMINARY_CURRICULARCOURSE values (5,1,3528,2);
insert into SEMINARY_CURRICULARCOURSE values (6,1,3418,2);
insert into SEMINARY_CURRICULARCOURSE values (8,1,2939,1);
insert into SEMINARY_CURRICULARCOURSE values (9,1,2859,1);
insert into SEMINARY_CURRICULARCOURSE values (11,1,3078,1);
insert into SEMINARY_CURRICULARCOURSE values (15,1,3631,3);
insert into SEMINARY_CURRICULARCOURSE values (16,1,3080,2);
insert into SEMINARY_CURRICULARCOURSE values (17,1,2673,1);
insert into SEMINARY_CURRICULARCOURSE values (18,1,3476,1);
insert into SEMINARY_CURRICULARCOURSE values (20,1,3376,1);
insert into SEMINARY_CURRICULARCOURSE values (22,1,4464,2);
insert into SEMINARY_CURRICULARCOURSE values (23,1,4489,1);
insert into SEMINARY_CURRICULARCOURSE values (25,1,4749,2);
insert into SEMINARY_CURRICULARCOURSE values (29,1,4672,1);

insert into SEMINARY_CURRICULARCOURSE values (7,1,4918,1);
insert into SEMINARY_CURRICULARCOURSE values (33,1,4919,1);
insert into SEMINARY_CURRICULARCOURSE values (10,1,4920,2);
insert into SEMINARY_CURRICULARCOURSE values (12,1,4921,1);
insert into SEMINARY_CURRICULARCOURSE values (13,1,4922,1);
insert into SEMINARY_CURRICULARCOURSE values (14,1,4923,1);
insert into SEMINARY_CURRICULARCOURSE values (19,1,4924,1);
insert into SEMINARY_CURRICULARCOURSE values (24,1,4925,1);
insert into SEMINARY_CURRICULARCOURSE values (26,1,4558,2);
insert into SEMINARY_CURRICULARCOURSE values (27,1,4552,1);
insert into SEMINARY_CURRICULARCOURSE values (28,1,4655,1);
insert into SEMINARY_CURRICULARCOURSE values (31,1,4658,1);
insert into SEMINARY_CURRICULARCOURSE values (32,1,4651,1);
insert into SEMINARY_CURRICULARCOURSE values (30,1,4926,1);



-- -----------------------------
-- Data for table 'EQUIVALENCY_THEME'
-- -----------------------------
delete from EQUIVALENCY_THEME;
insert into EQUIVALENCY_THEME values (1,1);
insert into EQUIVALENCY_THEME values (2,1);
insert into EQUIVALENCY_THEME values (3,1);
insert into EQUIVALENCY_THEME values (1,2);
insert into EQUIVALENCY_THEME values (2,2);
insert into EQUIVALENCY_THEME values (3,2);
insert into EQUIVALENCY_THEME values (1,3);
insert into EQUIVALENCY_THEME values (2,3);
insert into EQUIVALENCY_THEME values (3,3);
insert into EQUIVALENCY_THEME values (2,4);
insert into EQUIVALENCY_THEME values (2,5);
insert into EQUIVALENCY_THEME values (1,6);
insert into EQUIVALENCY_THEME values (2,6);
insert into EQUIVALENCY_THEME values (3,6);
insert into EQUIVALENCY_THEME values (1,8);
insert into EQUIVALENCY_THEME values (2,8);
insert into EQUIVALENCY_THEME values (3,8);
insert into EQUIVALENCY_THEME values (1,9);
insert into EQUIVALENCY_THEME values (2,9);
insert into EQUIVALENCY_THEME values (3,9);
insert into EQUIVALENCY_THEME values (1,11);
insert into EQUIVALENCY_THEME values (2,11);
insert into EQUIVALENCY_THEME values (3,11);
insert into EQUIVALENCY_THEME values (1,15);
insert into EQUIVALENCY_THEME values (2,15);
insert into EQUIVALENCY_THEME values (3,15);
insert into EQUIVALENCY_THEME values (1,16);
insert into EQUIVALENCY_THEME values (2,16);
insert into EQUIVALENCY_THEME values (3,16);
insert into EQUIVALENCY_THEME values (1,17);
insert into EQUIVALENCY_THEME values (2,17);
insert into EQUIVALENCY_THEME values (3,17);
insert into EQUIVALENCY_THEME values (1,18);
insert into EQUIVALENCY_THEME values (2,18);
insert into EQUIVALENCY_THEME values (3,18);
insert into EQUIVALENCY_THEME values (1,20);
insert into EQUIVALENCY_THEME values (2,20);
insert into EQUIVALENCY_THEME values (3,20);
insert into EQUIVALENCY_THEME values (1,25);
insert into EQUIVALENCY_THEME values (3,25);
insert into EQUIVALENCY_THEME values (1,29);
insert into EQUIVALENCY_THEME values (2,29);
insert into EQUIVALENCY_THEME values (3,29);
insert into EQUIVALENCY_THEME values (1,7);
insert into EQUIVALENCY_THEME values (2,7);
insert into EQUIVALENCY_THEME values (3,7);
insert into EQUIVALENCY_THEME values (1,33);
insert into EQUIVALENCY_THEME values (2,33);
insert into EQUIVALENCY_THEME values (3,33);
insert into EQUIVALENCY_THEME values (1,10);
insert into EQUIVALENCY_THEME values (2,10);
insert into EQUIVALENCY_THEME values (3,10);
insert into EQUIVALENCY_THEME values (1,12);
insert into EQUIVALENCY_THEME values (2,12);
insert into EQUIVALENCY_THEME values (3,12);
insert into EQUIVALENCY_THEME values (1,13);
insert into EQUIVALENCY_THEME values (2,13);
insert into EQUIVALENCY_THEME values (3,13);
insert into EQUIVALENCY_THEME values (1,14);
insert into EQUIVALENCY_THEME values (2,14);
insert into EQUIVALENCY_THEME values (3,14);
insert into EQUIVALENCY_THEME values (1,19);
insert into EQUIVALENCY_THEME values (2,19);
insert into EQUIVALENCY_THEME values (3,19);
insert into EQUIVALENCY_THEME values (1,24);
insert into EQUIVALENCY_THEME values (2,24);
insert into EQUIVALENCY_THEME values (3,24);
insert into EQUIVALENCY_THEME values (1,26);
insert into EQUIVALENCY_THEME values (2,26);
insert into EQUIVALENCY_THEME values (3,26);
insert into EQUIVALENCY_THEME values (1,27);
insert into EQUIVALENCY_THEME values (2,27);
insert into EQUIVALENCY_THEME values (3,27);
insert into EQUIVALENCY_THEME values (1,28);
insert into EQUIVALENCY_THEME values (2,28);
insert into EQUIVALENCY_THEME values (3,28);
insert into EQUIVALENCY_THEME values (1,31);
insert into EQUIVALENCY_THEME values (2,31);
insert into EQUIVALENCY_THEME values (3,31);
insert into EQUIVALENCY_THEME values (1,32);
insert into EQUIVALENCY_THEME values (2,32);
insert into EQUIVALENCY_THEME values (3,32);
insert into EQUIVALENCY_THEME values (1,30);
insert into EQUIVALENCY_THEME values (2,30);
insert into EQUIVALENCY_THEME values (3,30);


#47676 - ambiente
#47629 - aero
#49921 - quimica