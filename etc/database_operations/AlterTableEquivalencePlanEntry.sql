alter table EQUIVALENCE_PLAN_ENTRY add column KEY_PREVIOUS_COURSE_GROUP_FOR_NEW_CURRICULAR_COURSES int(11) null;
alter table EQUIVALENCE_PLAN_ENTRY drop column KEY_NEW_CURRICULAR_COURSE;
alter table EQUIVALENCE_PLAN_ENTRY add column NEW_CURRICULAR_COURSES_OPERATOR varchar(255) not null;
