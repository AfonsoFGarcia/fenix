----------------------------
-- Table structure for CREDIT_LINE
-- Types : Sab�tica, Outro, Dispensa de Servi�o Docente, Cargos de Gest�o
----------------------------
drop table if exists CREDIT_LINE;
create table CREDIT_LINE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_TEACHER int(11) not null,
   CREDITS float not null,
	 TYPE int(11) not null,
   EXPLANATION varchar(250) not null,
   START_DATE date not null,
   END_DATE date not null,
   primary key (ID_INTERNAL),
)type=InnoDB;
