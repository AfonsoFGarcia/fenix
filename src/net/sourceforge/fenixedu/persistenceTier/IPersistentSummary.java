package net.sourceforge.fenixedu.persistenceTier;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.ISummary;

public interface IPersistentSummary extends IPersistentObject {
   
    public List readByExecutionCourse(Integer executionCourseID) throws ExcepcaoPersistencia;

    public List readByExecutionCourseShifts(Integer executionCourseID)
            throws ExcepcaoPersistencia;

    public List readByExecutionCourseAndType(Integer executionCourseID, net.sourceforge.fenixedu.domain.ShiftType summaryType)
            throws ExcepcaoPersistencia;

    public List readByExecutionCourseShiftsAndTypeLesson(Integer executionCourseID,
            net.sourceforge.fenixedu.domain.ShiftType summaryType) throws ExcepcaoPersistencia;

    public List readByShift(Integer executionCourseID, Integer shiftID) throws ExcepcaoPersistencia;

    public List<ISummary> readByTeacher(Integer executionCourseID, Integer teacherNumber)
            throws ExcepcaoPersistencia;

    public List readByOtherTeachers(Integer executionCourseID) throws ExcepcaoPersistencia;

    public ISummary readSummaryByUnique(Integer shiftID, Date summaryDate, Date summaryHour)
            throws ExcepcaoPersistencia;

}