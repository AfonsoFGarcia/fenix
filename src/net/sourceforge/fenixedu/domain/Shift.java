package net.sourceforge.fenixedu.domain;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Shift extends Shift_Base {

    public String toString() {
        String result = "[TURNO";
        result += ", codigoInterno=" + this.getIdInternal();
        result += ", nome=" + getNome();
        result += ", tipo=" + getTipo();
        result += ", lotacao=" + getLotacao();
        result += ", chaveDisciplinaExecucao=" + getChaveDisciplinaExecucao();
        result += "]";
        return result;
    }

    public double hours() {
        double hours = 0;
        List lessons = this.getAssociatedLessons();
        for (int i = 0; i < lessons.size(); i++) {
            ILesson lesson = (ILesson) lessons.get(i);
            hours += lesson.hours();
        }
        return hours;
    }

    public void associateSchoolClass(ISchoolClass schoolClass) {
        if (schoolClass == null) {
            throw new NullPointerException();
        }
        if (!this.getAssociatedClasses().contains(schoolClass)) {
            this.getAssociatedClasses().add(schoolClass);
        }
        if (!schoolClass.getAssociatedShifts().contains(this)) {
            schoolClass.getAssociatedShifts().add(this);
        }
    }

    public void transferSummary(ISummary summary, Date summaryDate, Date summaryHour, IRoom room) {
        checkIfSummaryExistFor(summaryDate, summaryHour);
        summary.modifyShift(this, summaryDate, summaryHour, room);
    }

    private void checkIfSummaryExistFor(final Date summaryDate, final Date summaryHour) {
        final Iterator associatedSummaries = getAssociatedSummariesIterator();
        while (associatedSummaries.hasNext()) {
            ISummary summary = (ISummary) associatedSummaries.next();
            if (summary.getSummaryDate().equals(summaryDate)
                    && summary.getSummaryHour().equals(summaryHour)) {
                throw new DomainException(this.getClass().getName(), "error.summary.already.exists");
            }
        }
        return;
    }

}
