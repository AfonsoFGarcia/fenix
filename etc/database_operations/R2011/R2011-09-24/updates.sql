create table `TASK_LOG` (`SUCCESSFUL` tinyint(1), `TASK_END` timestamp NULL default NULL, `OID` bigint unsigned, `OID_TASK` bigint unsigned, `TASK_START` timestamp NULL default NULL, `OUTPUT` text, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_TASK)) ENGINE=InnoDB, character set latin1;
create table `SCHEDULER_SYSTEM` (`OID` bigint unsigned, `OID_PENDING_TASK` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID)) ENGINE=InnoDB, character set latin1;
create table `TASK` (`OID_PREVIOUS_TASK` bigint unsigned, `OID_NEXT_TASK` bigint unsigned, `OID` bigint unsigned, `OID_SCHEDULER_SYSTEM` bigint unsigned, `OID_SCHEDULER_SYSTEM_FROM_PENDING_TASK` bigint unsigned, `LAST_RUN` timestamp NULL default NULL, `OJB_CONCRETE_CLASS` varchar(255) NOT NULL DEFAULT '', `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_SCHEDULER_SYSTEM)) ENGINE=InnoDB, character set latin1;
create table `TASK_SCHEDULE` (`OID` bigint unsigned, `OID_TASK` bigint unsigned, `DAY` int(11), `HOUR` int(11), `MINUTE` int(11), `DAYOFWEEK` int(11), `MONTH` int(11), `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_TASK)) ENGINE=InnoDB, character set latin1;

alter table TASK add column LAST_RUN_START timestamp NULL DEFAULT NULL;
alter table TASK add column LAST_RUN_END timestamp NULL DEFAULT NULL;

