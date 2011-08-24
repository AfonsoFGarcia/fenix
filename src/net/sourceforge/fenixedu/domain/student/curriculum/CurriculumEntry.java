package net.sourceforge.fenixedu.domain.student.curriculum;

import java.io.Serializable;
import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grade;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

abstract public class CurriculumEntry implements Serializable, ICurriculumEntry {

    public boolean isNotInDegreeCurriculumEnrolmentEntry() {
	return false;
    }

    final public boolean getIsNotInDegreeCurriculumEnrolmentEntry() {
	return isNotInDegreeCurriculumEnrolmentEntry();
    }

    protected double ectsCredits(final CurricularCourse curricularCourse) {
	final double ectsCredits = curricularCourse.getEctsCredits().doubleValue();
	return ectsCredits == 0 ? 6.0 : ectsCredits;
    }

    public BigDecimal getWeigthTimesGrade() {
	final String grade = getGradeValue();
	return StringUtils.isNumeric(grade) ? getWeigthForCurriculum().multiply(BigDecimal.valueOf(Double.valueOf(grade))) : null;
    }

    abstract public Grade getGrade();

    public String getGradeValue() {
	return getGrade() == null ? null : getGrade().getValue();
    }

    public ExecutionSemester getExecutionPeriod() {
	return null;
    }

    final public boolean hasExecutionPeriod() {
	return getExecutionPeriod() != null;
    }

    final public ExecutionYear getExecutionYear() {
	return getExecutionPeriod() == null ? null : getExecutionPeriod().getExecutionYear();
    }

    final public String getCode() {
	return null;
    }

    final public MultiLanguageString getName() {
	return new MultiLanguageString();
    }

    @Override
    public MultiLanguageString getPresentationName() {
	return getName();
    }
}
