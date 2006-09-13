package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.SummariesManagementBean;
import net.sourceforge.fenixedu.dataTransferObject.SummariesManagementBean.SummaryType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.YearMonthDayConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.joda.time.YearMonthDay;

public class PossibleDatesToSummariesManagementProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	
	SummariesManagementBean bean = (SummariesManagementBean) source;
	Lesson lesson = bean.getLesson();
	Shift shift = bean.getShift();
	SummaryType summaryType = bean.getSummaryType();
	Summary summary = bean.getSummary();
	List<YearMonthDay> possibleSummaryDates = new ArrayList<YearMonthDay>();
	
	if (summaryType != null && summaryType.equals(SummaryType.NORMAL_SUMMARY)) {
	    if (lesson != null) {
		possibleSummaryDates.addAll(lesson.getPossibleDatesToInsertSummary());
	    }
	    //Show SummaryDate when edit summary
	    if (summary != null && shift != null && lesson != null && summary.getShift().equals(shift)
		    && summary.getLesson().equals(lesson)) {
		possibleSummaryDates.add(summary.getSummaryDateYearMonthDay());
	    }
	    Collections.reverse(possibleSummaryDates);
	}
	return possibleSummaryDates;
    }

    public Converter getConverter() {
	return new YearMonthDayConverter();
    }
}
