
#
#------------------------------------
# Table structure for table 'SECCAO'
#------------------------------------
drop table if exists SECCAO;
CREATE TABLE SECCAO (
  CODIGO_INTERNO int(11) NOT NULL default '0',
  NOME varchar(100) NOT NULL default '',
  ORDEM int(3) default NULL,
  CHAVE_SECCAO_SUPERIOR int(11) default NULL,
  CHAVE_SITIO int(11) NOT NULL default '0',
  PRIMARY KEY  (CODIGO_INTERNO),
  UNIQUE KEY U1 (NOME,CHAVE_SITIO,CHAVE_SECCAO_SUPERIOR)
) TYPE=InnoDB;
#-----------------------------------
# Table structure for table 'SITIO'
#-----------------------------------
DROP TABLE IF EXISTS SITIO;
CREATE TABLE SITIO (
  CODIGO_INTERNO int(11) NOT NULL default '0',
  NOME varchar(100) NOT NULL default '',
  ANO_CURRICULAR int(1) default NULL,
  SEMESTRE int(1) default NULL,
  CURSO varchar(100) default NULL,
  DEPARTAMENTO varchar(100) default NULL,
  CHAVE_SECCAO_INICIAL int(11) default NULL,
  PRIMARY KEY  (CODIGO_INTERNO),
  UNIQUE KEY U1 (NOME)
) TYPE=InnoDB;