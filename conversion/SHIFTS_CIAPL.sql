truncate ciapl.SHIFT;

insert into ciapl.SHIFT (ID_INTERNAL, NAME, KEY_EXECUTION_COURSE, TYPE, CAPACITY)
select t.codigoInterno, t.nome, t.chaveDisciplinaExecucao, 1, lotacao
from fenix.turno t, ciapl.EXECUTION_COURSE ec
where tipo = "Te�rico" and ec.ID_INTERNAL = t.chaveDisciplinaExecucao;

insert into ciapl.SHIFT (ID_INTERNAL, NAME, KEY_EXECUTION_COURSE, TYPE, CAPACITY)
select t.codigoInterno, t.nome, t.chaveDisciplinaExecucao, 2, lotacao
from fenix.turno t, ciapl.EXECUTION_COURSE ec
where tipo = "Pr�tico" and ec.ID_INTERNAL = t.chaveDisciplinaExecucao;

insert into ciapl.SHIFT (ID_INTERNAL, NAME, KEY_EXECUTION_COURSE, TYPE, CAPACITY)
select t.codigoInterno, t.nome, t.chaveDisciplinaExecucao, 3, lotacao
from fenix.turno t, ciapl.EXECUTION_COURSE ec
where tipo = "Te�rico-Pr�tico" and ec.ID_INTERNAL = t.chaveDisciplinaExecucao;

insert into ciapl.SHIFT (ID_INTERNAL, NAME, KEY_EXECUTION_COURSE, TYPE, CAPACITY)
select t.codigoInterno, t.nome, t.chaveDisciplinaExecucao, 4, lotacao
from fenix.turno t, ciapl.EXECUTION_COURSE ec
where tipo = "Laboratorial" and ec.ID_INTERNAL = t.chaveDisciplinaExecucao;


