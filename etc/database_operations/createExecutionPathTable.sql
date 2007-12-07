create table EXECUTION_PATH
  select CONTENT.ID_INTERNAL, CONTENT.ID_INTERNAL as KEY_FUNCTIONALITY, CONTENT.EXECUTION_PATH
  from CONTENT
  where CONTENT.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.functionalities.Functionality';

alter table EXECUTION_PATH
  add primary key (ID_INTERNAL),
  add index (KEY_FUNCTIONALITY),
  add column KEY_ROOT_DOMAIN_OBJECT int(11) NOT NULL default '1',
  type='InnoDB';


alter table CONTENT add column KEY_EXECUTION_PATH_VALUE int(11) default null;
alter table CONTENT add index (KEY_EXECUTION_PATH_VALUE);

update CONTENT C, EXECUTION_PATH EP SET C.KEY_EXECUTION_PATH_VALUE=EP.ID_INTERNAL WHERE EP.KEY_FUNCTIONALITY=C.ID_INTERNAL;
