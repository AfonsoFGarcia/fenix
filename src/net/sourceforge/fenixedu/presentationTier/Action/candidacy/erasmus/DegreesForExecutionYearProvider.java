/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreesForExecutionYearProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	final SortedSet<Degree> result = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
	final DegreeCourseInformationBean chooseDegreeBean = (DegreeCourseInformationBean) source;

	for (final Degree degree : Degree.readAllByDegreeType(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE,
		DegreeType.BOLONHA_MASTER_DEGREE)) {
	    if (matchesExecutionYear(degree, chooseDegreeBean.getExecutionYear())) {
		result.add(degree);
	    }
	}

	return result;
    }

    private boolean matchesExecutionYear(Degree degree, ExecutionYear executionYear) {
	for (final ExecutionDegree executionDegree : executionYear.getExecutionDegrees()) {
	    if (executionDegree.getDegree() == degree) {
		return true;
	    }
	}

	return false;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}