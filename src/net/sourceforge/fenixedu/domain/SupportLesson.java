package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.date.TimePeriod;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerAware;
import org.apache.ojb.broker.PersistenceBrokerException;

/**
 * @author Fernanda Quit�rio 17/10/2003
 * @author jpvl
 */
public class SupportLesson extends SupportLesson_Base implements PersistenceBrokerAware,
        ICreditsEventOriginator {
    private IProfessorship professorship;

    private DiaSemana weekDay;

    public SupportLesson() {
    }

    public SupportLesson(Integer idInternal) {
        setIdInternal(idInternal);
    }

    private boolean elementsAreEqual(Object element1, Object element2) {
        boolean result = false;
        if ((element1 == null && element2 == null)
                || (element1 != null && element2 != null && element1.equals(element2))) {
            result = true;
        }
        return result;
    }

    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof ISupportLesson) {
            ISupportLesson supportLessonsTimetable = (ISupportLesson) arg0;

            if (elementsAreEqual(supportLessonsTimetable.getProfessorship(), this.getProfessorship())
                    && elementsAreEqual(supportLessonsTimetable.getStartTime(), this.getStartTime())
                    && elementsAreEqual(supportLessonsTimetable.getEndTime(), this.getEndTime())
                    && elementsAreEqual(supportLessonsTimetable.getWeekDay(), this.getWeekDay())) {
                result = true;
            }
        }
        return result;
    }

    /**
     * @return Returns the professorship.
     */
    public IProfessorship getProfessorship() {
        return this.professorship;
    }

    /**
     * @return
     */
    public DiaSemana getWeekDay() {
        return weekDay;
    }

    /**
     * @param professorship
     *            The professorship to set.
     */
    public void setProfessorship(IProfessorship professorship) {
        this.professorship = professorship;
    }

    /**
     * @param weekDay
     */
    public void setWeekDay(DiaSemana weekDay) {
        this.weekDay = weekDay;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.ISupportLesson#hours()
     */
    public double hours() {
        TimePeriod timePeriod = new TimePeriod(this.getStartTime(), this.getEndTime());
        return timePeriod.hours().doubleValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#beforeUpdate(org.apache.ojb.broker.PersistenceBroker)
     */
    public void beforeUpdate(PersistenceBroker broker) throws PersistenceBrokerException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterUpdate(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterUpdate(PersistenceBroker broker) throws PersistenceBrokerException {
        notifyTeacher();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#beforeInsert(org.apache.ojb.broker.PersistenceBroker)
     */
    public void beforeInsert(PersistenceBroker broker) throws PersistenceBrokerException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterInsert(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterInsert(PersistenceBroker broker) throws PersistenceBrokerException {
        notifyTeacher();
    }

    /**
     *  
     */
    private void notifyTeacher() {
        ITeacher teacher = this.professorship.getTeacher();
        teacher.notifyCreditsChange(CreditsEvent.SUPPORT_LESSONS, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#beforeDelete(org.apache.ojb.broker.PersistenceBroker)
     */
    public void beforeDelete(PersistenceBroker broker) throws PersistenceBrokerException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterDelete(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterDelete(PersistenceBroker broker) throws PersistenceBrokerException {
        notifyTeacher();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterLookup(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterLookup(PersistenceBroker broker) throws PersistenceBrokerException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.credits.event.ICreditsEventOriginator#belongsToExecutionPeriod(Dominio.IExecutionPeriod)
     */
    public boolean belongsToExecutionPeriod(IExecutionPeriod executionPeriod) {
        return this.getProfessorship().getExecutionCourse().getExecutionPeriod().equals(executionPeriod);
    }

}