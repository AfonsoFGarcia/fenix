package net.sourceforge.fenixedu.presentationTier.Action.gep.inquiries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries.DefineInquiryResponsePeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponsePeriod;
import net.sourceforge.fenixedu.domain.inquiries.teacher.InquiryResponsePeriodType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.security.UserView;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

public class DefineResponsePeriodsDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
	final String executionPeriodIDString = dynaActionForm.getString("executionPeriodID");
	final Integer executionPeriodID = validInteger(executionPeriodIDString) ? Integer.valueOf(executionPeriodIDString) : null;
	final String periodTypeString = dynaActionForm.getString("periodType");
	final InquiryResponsePeriodType periodType = StringUtils.isEmpty(periodTypeString) ? InquiryResponsePeriodType.STUDENT
		: InquiryResponsePeriodType.valueOf(periodTypeString);

	final List<ExecutionSemester> executionSemesters = new ArrayList<ExecutionSemester>(rootDomainObject
		.getExecutionPeriodsSet());
	Collections.sort(executionSemesters, new ReverseComparator(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR));

	ExecutionSemester selectedExecutionPeriod = null;
	final List<LabelValueBean> executionPeriodLVBs = new ArrayList<LabelValueBean>();

	for (final ExecutionSemester executionSemester : executionSemesters) {
	    final String label = executionSemester.getName() + " " + executionSemester.getExecutionYear().getYear();
	    executionPeriodLVBs.add(new LabelValueBean(label, executionSemester.getIdInternal().toString()));

	    if ((executionPeriodID == null && executionSemester.isCurrent())
		    || (executionPeriodID != null && executionSemester.getIdInternal().equals(executionPeriodID))) {
		selectedExecutionPeriod = executionSemester;
		dynaActionForm.set("executionPeriodID", selectedExecutionPeriod.getIdInternal().toString());
		InquiryResponsePeriod inquiryResponsePeriod = selectedExecutionPeriod.getInquiryResponsePeriod(periodType);
		if (inquiryResponsePeriod != null) {
		    request.setAttribute("inquiryResponsePeriod", inquiryResponsePeriod);
		}
	    }
	}

	request.setAttribute("executionPeriodLVBs", executionPeriodLVBs);
	request.setAttribute("periodType", periodType);

	request.setAttribute("selectedExecutionPeriod", selectedExecutionPeriod);

	return mapping.findForward("showForm");
    }

    public ActionForward define(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final IUserView userView = UserView.getUser();

	final DynaActionForm dynaActionForm = (DynaActionForm) actionForm;

	final String executionPeriodIDString = dynaActionForm.getString("executionPeriodID");
	final String inquiryResponseBeginString = dynaActionForm.getString("inquiryResponseBegin");
	final String inquiryResponseEndString = dynaActionForm.getString("inquiryResponseEnd");

	final Integer executionPeriodID = validInteger(executionPeriodIDString) ? Integer.valueOf(executionPeriodIDString) : null;
	final Date inquiryResponseBegin = (inquiryResponseBeginString != null && inquiryResponseBeginString.length() > 0) ? DateFormatUtil
		.parse("dd/MM/yyyy HH:mm", inquiryResponseBeginString + ":00")
		: null;
	final Date inquiryResponseEnd = (inquiryResponseEndString != null && inquiryResponseEndString.length() > 0) ? DateFormatUtil
		.parse("dd/MM/yyyy HH:mm", inquiryResponseEndString + ":00")
		: null;

	DefineInquiryResponsePeriod.run(executionPeriodID, inquiryResponseBegin, inquiryResponseEnd);

	final ActionMessages actionMessages = new ActionMessages();
	actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.inquiry.response.period.defined"));
	saveMessages(request, actionMessages);

	return prepare(mapping, actionForm, request, response);
    }

    private boolean validInteger(final String executionPeriodIDString) {
	return executionPeriodIDString != null && executionPeriodIDString.length() > 0
		&& StringUtils.isNumeric(executionPeriodIDString);
    }

}