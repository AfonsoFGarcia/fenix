select concat('UPDATE CURRICULAR_COURSE SET TYPE = 1 WHERE ID_INTERNAL = ', ID_INTERNAL, ';') as ""
from curricular_course
where (name not like 'Trabalho Final de Curso%' and name not like 'Op��o%')
and key_degree_curricular_plan = 48;
