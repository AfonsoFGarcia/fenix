/*
 * Created on May 10, 2005
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.PeriodState;

/**
 * @author mrsp and jdnf
 */
public class ExecutionPeriodVO extends VersionedObjectsBase implements IPersistentExecutionPeriod {

    public ExecutionPeriod readByNameAndExecutionYear(String executionPeriodName, String year)
            throws ExcepcaoPersistencia {
        Collection executionPeriods = readAll(ExecutionPeriod.class);
        for (Iterator iter = executionPeriods.iterator(); iter.hasNext();) {
            ExecutionPeriod element = (ExecutionPeriod) iter.next();
            if ((element.getExecutionYear().getYear().equals(year))
                    && (element.getName().equals(executionPeriodName)))
                return element;
        }
        return null;
    }

    public ExecutionPeriod readBySemesterAndExecutionYear(Integer semester, String year)
            throws ExcepcaoPersistencia {
        if (year == null)
            return null;
        Collection executionPeriods = readAll(ExecutionPeriod.class);
        for (Iterator iter = executionPeriods.iterator(); iter.hasNext();) {
            ExecutionPeriod element = (ExecutionPeriod) iter.next();
            if ((element.getExecutionYear().getYear().equals(year))
                    && (element.getSemester().equals(semester)))
                return element;
        }
        return null;
    }

    public List readNotClosedPublicExecutionPeriods() throws ExcepcaoPersistencia {
        Collection executionPeriods = readAll(ExecutionPeriod.class);
        List executionPeriodsAux = new ArrayList();

        for (Iterator iter = executionPeriods.iterator(); iter.hasNext();) {
            ExecutionPeriod element = (ExecutionPeriod) iter.next();
            if ((element.getSemester().compareTo(new Integer(0)) > 0)
                    && (!element.getState().equals(PeriodState.CLOSED))
                    && (!element.getState().equals(PeriodState.NOT_OPEN)))
                executionPeriodsAux.add(element);
        }
        return executionPeriodsAux;
    }

    public List readExecutionPeriodsInTimePeriod(Date start, Date end) throws ExcepcaoPersistencia {
        Collection executionPeriods = readAll(ExecutionPeriod.class);
        List executionPeriodsAux = new ArrayList();

        for (Iterator iter = executionPeriods.iterator(); iter.hasNext();) {
            ExecutionPeriod element = (ExecutionPeriod) iter.next();
            if (element.getBeginDate().before(end) && element.getEndDate().after(start))
                executionPeriodsAux.add(element);
        }
        return executionPeriodsAux;
    }

    public List readNotClosedPublicExecutionPeriodsByExecutionYears(Integer executionYearID)
            throws ExcepcaoPersistencia {
        Collection executionPeriods = readAll(ExecutionPeriod.class);
        List executionPeriodsAux = new ArrayList();

        for (Iterator iter = executionPeriods.iterator(); iter.hasNext();) {
            ExecutionPeriod element = (ExecutionPeriod) iter.next();
            if ((element.getExecutionYear().getIdInternal().equals(executionYearID))
                    && (!element.getState().equals(PeriodState.CLOSED))
                    && (!element.getState().equals(PeriodState.NOT_OPEN))
                    && (element.getSemester().compareTo(new Integer(0)) > 0))
                executionPeriodsAux.add(element);
        }
        return executionPeriodsAux;
    }
}
