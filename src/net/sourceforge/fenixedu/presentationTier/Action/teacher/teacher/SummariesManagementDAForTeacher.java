package net.sourceforge.fenixedu.presentationTier.Action.teacher.teacher;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		module = "teacher",
		path = "/summariesManagement",
		attribute = "summariesManagementForm",
		formBean = "summariesManagementForm",
		scope = "request",
		parameter = "method")
@Forwards(value = { @Forward(name = "prepareInsertSummary", path = "prepare-insert-summary"),
		@Forward(name = "prepareShowSummaries", path = "prepare-show-summaries"),
		@Forward(name = "showSummariesCalendar", path = "show-summaries-calendar"),
		@Forward(name = "prepareInsertComplexSummary", path = "prepare-insert-complex-summary") })
public class SummariesManagementDAForTeacher extends
		net.sourceforge.fenixedu.presentationTier.Action.teacher.SummariesManagementDA {
}