#-------------------------------------------
# Table structure for CAMPUS
#-------------------------------------------
drop table if exists CAMPUS;
create table CAMPUS(
	ID_INTERNAL int(11) not null auto_increment,
    ACK_OPT_LOCK int(11),
	NAME varchar(50) not null,
	primary key (ID_INTERNAL),
	unique U1 (NAME)
) type=InnoDB;