#-----------------------------
# Data for table 'MASTER_DEGREE_CANDIDATE'
#-----------------------------
DELETE FROM MASTER_DEGREE_CANDIDATE;
DELETE FROM MASTER_DEGREE_CANDIDATE;
INSERT INTO MASTER_DEGREE_CANDIDATE VALUES (1, 6, 10, 1, 1, 'Informatica', 'IST', 2000, 14.99);
INSERT INTO MASTER_DEGREE_CANDIDATE VALUES (2, 7, 10, 2, 2, 'Informatica 2', 'IST 2', 2001, 13.99);


#-----------------------------
# Data for table 'CANDIDATE_SITUATION'
#-----------------------------
DELETE FROM CANDIDATE_SITUATION;
DELETE FROM CANDIDATE_SITUATION;
INSERT INTO CANDIDATE_SITUATION VALUES (1, '2002-11-17', 'Nothing', 0, 1, 1);
INSERT INTO CANDIDATE_SITUATION VALUES (2, '2002-11-17', 'Nothing', 1, 2, 1);
INSERT INTO CANDIDATE_SITUATION VALUES (3, '2002-11-20', 'Nothing', 1, 1, 2);


#-----------------------------
# Data for table 'CONTRIBUTOR'
#-----------------------------
DELETE FROM CONTRIBUTOR;
DELETE FROM CONTRIBUTOR;
INSERT INTO CONTRIBUTOR values (1, 123, 'Nome1', 'Morada1');
INSERT INTO CONTRIBUTOR values (2, 456, 'Nome2', 'Morada2');

#-----------------------------
# Data for table 'GUIDE'
#-----------------------------
DELETE FROM GUIDE;
DELETE FROM GUIDE;
INSERT INTO GUIDE values (1, 1, 2003, 1, 6, 600.04, 'guia1', 1, 10, 1, '2003-4-4', 1);
INSERT INTO GUIDE values (2, 2, 2003, 1, 6, 400.04, 'guia2', 1, 10, 2, '2003-4-3', 1);
INSERT INTO GUIDE values (3, 1, 2002, 2, 6, 200.04, 'guia3', 1, 10, 1, '2003-4-1', 1);


#-----------------------------
# Data for table 'GUIDE_ENTRY'
#-----------------------------
DELETE FROM GUIDE_ENTRY;
DELETE FROM GUIDE_ENTRY;
INSERT INTO GUIDE_ENTRY values (1, 1, 1, 2, 'Conclus�o', 10.02 , 1);
INSERT INTO GUIDE_ENTRY values (2, 1, 1, 6, 'Atraso no Pagamento', 15, 2);
INSERT INTO GUIDE_ENTRY values (3, 1, 1, 7, 'de vida', 10.00, 1);

INSERT INTO GUIDE_ENTRY values (4, 2, 1, 1, 'desc1', 1, 1);
INSERT INTO GUIDE_ENTRY values (5, 2, 1, 2, 'desc2', 12, 2);


INSERT INTO GUIDE_ENTRY values (6, 3, 2, 3, 'desc3', 33, 1);
INSERT INTO GUIDE_ENTRY values (7, 3, 2, 5, 'desc5', 4, 3);


#-----------------------------
# Data for table 'GUIDE_SITUATION'
#-----------------------------
DELETE FROM GUIDE_SITUATION;
DELETE FROM GUIDE_SITUATION;
INSERT INTO GUIDE_SITUATION values (1, 1, 1, '2003-3-12', 'nao pago', 0);
INSERT INTO GUIDE_SITUATION values (2, 1, 2, '2003-10-6', 'pago', 1);
INSERT INTO GUIDE_SITUATION values (3, 2, 3, '2003-5-4', 'anulado', 1);


#-----------------------------
# Data for table 'PRICE'
#-----------------------------
DELETE FROM PRICE;
DELETE FROM PRICE;
INSERT INTO PRICE values (1, 1, 1, 'De conclus�o de mestrado', '11.47');
INSERT INTO PRICE values (2, 1, 2, 'Doc2', '12.45');
INSERT INTO PRICE values (3, 1, 3, 'Doc3', '13.47');
INSERT INTO PRICE values (4, 2, 4, 'Mestrado', '75');
INSERT INTO PRICE values (5, 2, 4, 'Integrado', '75');
INSERT INTO PRICE values (6, 2, 4, 'Especializa��o', '75');

