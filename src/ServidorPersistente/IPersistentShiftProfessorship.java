/*
 * Created on 19/Mai/2003 by jpvl
 *
 */
package ServidorPersistente;

import java.util.Date;
import java.util.List;

import Dominio.IExecutionPeriod;
import Dominio.IProfessorship;
import Dominio.IShiftProfessorship;
import Dominio.ITeacher;
import Dominio.IShift;
import Util.DiaSemana;
import Util.TipoCurso;

/**
 * @author jpvl
 */
public interface IPersistentShiftProfessorship extends IPersistentObject {
    IShiftProfessorship readByUnique(IShiftProfessorship teacherShiftPercentage)
            throws ExcepcaoPersistencia;

    void delete(IShiftProfessorship teacherShiftPercentage) throws ExcepcaoPersistencia;

    IShiftProfessorship readByProfessorshipAndShift(IProfessorship professorship, IShift shift)
            throws ExcepcaoPersistencia;

    List readOverlappingPeriod(ITeacher teacher, IExecutionPeriod executionPeriod, DiaSemana weekDay,
            Date startTime, Date endTime) throws ExcepcaoPersistencia;

    List readByTeacherAndExecutionPeriodAndDegreeType(ITeacher teacher,
            IExecutionPeriod executionPeriod, TipoCurso curso) throws ExcepcaoPersistencia;

    List readByProfessorship(IProfessorship professorship) throws ExcepcaoPersistencia;

    /**
     * @param executionPeriod
     * @return
     */
    List readByExecutionPeriod(IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

    List readByTeacherAndExecutionPeriodWithDifferentIds(ITeacher teacher, IExecutionPeriod period,
            List shiftProfessorShipsIds) throws ExcepcaoPersistencia;

    List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod period)
            throws ExcepcaoPersistencia;

    List readByShift(IShift shift) throws ExcepcaoPersistencia;
}