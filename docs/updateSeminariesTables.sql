alter table SEMINARY add enrollment_begin_time time;
alter table SEMINARY add enrollment_begin_date date;
alter table SEMINARY add enrollment_end_time time;
alter table SEMINARY add enrollment_end_date date;
alter table SEMINARY add has_theme int;
alter table SEMINARY add has_case_study int;
alter table SEMINARY_MODALITY drop key name;

update SEMINARY set
	has_theme = 1,
	has_case_study = 1,
	enrollment_begin_date= '2003-9-1',
	enrollment_begin_time = '0:0:0',
	enrollment_end_date = '2003-9-2',
	enrollment_end_time = '23:59:59'
	where id_internal=1;


#select * from teacher,person,role,person_role WHERE
#role.id_internal = 20 AND
#person_role.key_role = role.id_internal AND
#person_role.key_person = person.id_internal AND
#teacher.key_person = person.id_internal




insert into SEMINARY(id_internal,has_theme,has_case_study,name,description,allowed_candidacies_per_student,enrollment_begin_time,enrollment_begin_date,enrollment_end_time,enrollment_end_date,ACK_OPT_LOCK) 
             values (2,0,0,'Inova��o','Semin�rios Sobre Inova��o',1,'0:0:0','2003-1-31','23:59','2003-2-23',1);
insert into SEMINARY_MODALITY(id_internal,name,description,ACK_OPT_LOCK)  values(4,'Completa','O aluno frequenta os Semin�rios com cerca de 45 horas lectivas e elabora um trabalho de grupo de estudo dum caso, que ser� discutido e avaliado. Esta modalidade poder� ser oferecida como conte�do poss�vel para uma disciplina de licenciatura ou de programa de p�s-gradua��o, desde que tal tenha as autoriza��es devidas no respectivo �mbito. Para todos os efeitos formais tudo se passar� como se o aluno tivesse frequentado a disciplina original.',1);
insert into SEMINARY_MODALITY(id_internal,name,description,ACK_OPT_LOCK)  values(5,'Semin�rio','O aluno frequentar� um conjunto de pelo menos 7 sess�es lectivas, como alternativa a um dos Semin�rios   de Humanidades de Engenharia Civil, contando para o portfolio de Engenharia Inform�tica, ou para efeitos semelhantes noutros eventuais cursos.',1);

insert into SEMINARY_CURRICULARCOURSE values (36,2,2819,4,1);
insert into SEMINARY_CURRICULARCOURSE values (37,2,10994,4,1);
insert into SEMINARY_CURRICULARCOURSE values (38,2,3806,4,1);
insert into SEMINARY_CURRICULARCOURSE values (39,2,3338,4,1);
insert into SEMINARY_CURRICULARCOURSE values (40,2,4924,4,1);
insert into SEMINARY_CURRICULARCOURSE values (41,2,4920,5,1);
insert into SEMINARY_CURRICULARCOURSE values (42,2,3633,5,1);
insert into SEMINARY_CURRICULARCOURSE values (43,2,3395,4,1);
insert into SEMINARY_CURRICULARCOURSE values (44,2,4918,4,1);
insert into SEMINARY_CURRICULARCOURSE values (45,2,2951,4,1);
insert into SEMINARY_CURRICULARCOURSE values (46,2,2800,4,1);
insert into SEMINARY_CURRICULARCOURSE values (47,2,2804,4,1);
insert into SEMINARY_CURRICULARCOURSE values (48,2,4747,5,1);
insert into SEMINARY_CURRICULARCOURSE values (49,2,4186,5,1);
insert into SEMINARY_CURRICULARCOURSE values (50,2,11253,4,1);
insert into SEMINARY_CURRICULARCOURSE values (51,2,11254,4,1);
insert into SEMINARY_CURRICULARCOURSE values (52,2,11255,4,1);
insert into SEMINARY_CURRICULARCOURSE values (53,2,11256,4,1);
insert into SEMINARY_CURRICULARCOURSE values (54,2,11257,4,1);
insert into SEMINARY_CURRICULARCOURSE values (55,2,11258,4,1);
insert into SEMINARY_CURRICULARCOURSE values (56,2,11259,4,1);
insert into SEMINARY_CURRICULARCOURSE values (57,2,11260,4,1);
insert into SEMINARY_CURRICULARCOURSE values (58,2,5035,4,1);

insert into PERSON_ROLE values (47000,20,1761,1);
insert into PERSON_ROLE values (47001,20,934,1);
insert into PERSON_ROLE values (47002,20,291,1);
insert into PERSON_ROLE values (47003,20,418,1);