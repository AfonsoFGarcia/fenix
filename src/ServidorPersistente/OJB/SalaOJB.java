/*
 * SalaOJB.java
 * 
 * Created on 21 de Agosto de 2002, 16:36
 */

package ServidorPersistente.OJB;

/**
 * @author ars
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.Aula;
import Dominio.Exam;
import Dominio.IExecutionCourse;
import Dominio.IExam;
import Dominio.ISala;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.Sala;
import Dominio.TurmaTurno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import ServidorPersistente.exceptions.notAuthorizedPersistentDeleteException;
import Util.TipoSala;

public class SalaOJB extends ObjectFenixOJB implements ISalaPersistente
{

    public ISala readByName(String nome) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("nome", nome);
        return (ISala) queryObject(Sala.class, crit);

    }

    public void lockWrite(ISala roomToWrite) throws ExcepcaoPersistencia, ExistingPersistentException
    {

        ISala roomFromDB = null;

        // If there is nothing to write, simply return.
        if (roomToWrite == null)
            return;

        // Read room from database.
        roomFromDB = this.readByName(roomToWrite.getNome());

        // If room is not in database, then write it.
        if (roomFromDB == null)
            super.lockWrite(roomToWrite);
        // else If the room is mapped to the database, then write any existing
        // changes.
        else if (
            (roomToWrite instanceof Sala)
                && ((Sala) roomFromDB).getIdInternal().equals(((Sala) roomToWrite).getIdInternal()))
        {
            super.lockWrite(roomToWrite);
            // else Throw an already existing exception
        }
        else
            throw new ExistingPersistentException();
    }

    public void delete(ISala sala) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("sala.nome", sala.getNome());

        List result = queryList(Aula.class, crit);
        if (result.size() != 0)
        {
            throw new notAuthorizedPersistentDeleteException("Cannot delete rooms with classes");
        }
        else
        {
            super.delete(sala);
        }

    }

    public List readAll() throws ExcepcaoPersistencia
    {
        return queryList(Sala.class, new Criteria());
    }

    /**
     * Reads all salas that with certains properties. The properties are
     * specified by the arguments of this method. If an argument is null, then
     * the sala can have any value concerning that argument. In what concerns
     * the capacidadeNormal and capacidadeExame, this two arguments specify the
     * minimal value that a sala can have in order to be selected.
     * 
     * @return a list with all salas that satisfy the conditions specified by
     *         the non-null arguments.
     */
    public List readSalas(
        String nome,
        String edificio,
        Integer piso,
        Integer tipo,
        Integer capacidadeNormal,
        Integer capacidadeExame)
        throws ExcepcaoPersistencia
    {

        if (nome == null
            && edificio == null
            && piso == null
            && tipo == null
            && capacidadeExame == null
            && capacidadeNormal == null)
        {
            return readAll();
        }

        try
        {
            StringBuffer oqlQuery = new StringBuffer("select sala from ");
            boolean hasPrevious = false;

            oqlQuery.append(Sala.class.getName()).append(" where ");
            if (nome != null)
            {
                hasPrevious = true;
                oqlQuery.append("nome = \"").append(nome).append("\"");
            }

            if (edificio != null)
            {
                if (hasPrevious)
                    oqlQuery.append(" and ");
                else
                    hasPrevious = true;

                oqlQuery.append(" edificio = \"").append(edificio).append("\"");
            }

            if (piso != null)
            {
                if (hasPrevious)
                    oqlQuery.append(" and ");
                else
                    hasPrevious = true;

                oqlQuery.append(" piso = \"").append(piso).append("\"");
            }

            if (tipo != null)
            {
                if (hasPrevious)
                    oqlQuery.append(" and ");
                else
                    hasPrevious = true;

                oqlQuery.append(" tipo = \"").append(tipo).append("\"");
            }

            if (capacidadeNormal != null)
            {
                if (hasPrevious)
                    oqlQuery.append(" and ");
                else
                    hasPrevious = true;

                oqlQuery.append(" capacidadeNormal > \"").append(
                    capacidadeNormal.intValue() - 1).append(
                    "\"");
            }

            if (capacidadeExame != null)
            {
                if (hasPrevious)
                    oqlQuery.append(" and ");
                else
                    hasPrevious = true;

                oqlQuery.append(" capacidadeExame > \"").append(capacidadeExame.intValue() - 1).append(
                    "\"");
            }

            query.create(oqlQuery.toString());
            List result = (List) query.execute();
            lockRead(result);
            return result;
        }
        catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public List readAvailableRooms(IExam exam) throws ExcepcaoPersistencia
    {
        List availableRooms = null;

        IExam examFromDB = (IExam) readByOId(exam, false);
        if (examFromDB != null)
        {
            Criteria crit = new Criteria();
            crit.addNotEqualTo("idInternal", examFromDB.getIdInternal());
            crit.addEqualTo("day", exam.getDay());
            crit.addEqualTo("beginning", exam.getBeginning());
            crit.addEqualTo(
                "associatedExecutionCourses.executionPeriod.name",
                ((IExecutionCourse) examFromDB.getAssociatedExecutionCourses().get(0))
                    .getExecutionPeriod()
                    .getName());
            crit.addEqualTo(
                "associatedExecutionCourses.executionPeriod.executionYear.year",
                ((IExecutionCourse) examFromDB.getAssociatedExecutionCourses().get(0))
                    .getExecutionPeriod()
                    .getExecutionYear()
                    .getYear());
            List otherExams = queryList(Exam.class, crit);

            List occupiedRooms = new ArrayList();
            for (int i = 0; i < otherExams.size(); i++)
            {
                IExam someOtherExam = (IExam) otherExams.get(i);
                occupiedRooms.addAll(someOtherExam.getAssociatedRooms());
            }
            Criteria crit2 = new Criteria();
            crit2.addNotEqualTo("tipo", new TipoSala(TipoSala.LABORATORIO));
            List allExamRooms = queryList(Sala.class, crit2);
            availableRooms = (List) CollectionUtils.subtract(allExamRooms, occupiedRooms);

        }
        return availableRooms;
    }

    public List readForRoomReservation() throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addNotEqualTo("tipo", new TipoSala(TipoSala.LABORATORIO));
        criteria.addNotLike("edificio", "Tagus%");
        criteria.addNotLike("edificio", "Local%");
        return queryList(Sala.class, criteria);
    }

    public List readByPavillion(String pavillion) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("edificio", pavillion);
        return queryList(Sala.class, criteria);
    }
    /**
     * Returns a class list
     * 
     * @see ServidorPersistente.ITurmaTurnoPersistente#readByClass(ITurma)
     */
    public List readByShift(ITurno group) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("turno.idInternal", group.getIdInternal());

        List result = queryList(TurmaTurno.class, crit);

        List classList = new ArrayList();
        Iterator resultIterator = result.iterator();
        while (resultIterator.hasNext())
        {
            ITurmaTurno classShift = (ITurmaTurno) resultIterator.next();
            classList.add(classShift.getTurma());
        }
        return classList;

    }

}
