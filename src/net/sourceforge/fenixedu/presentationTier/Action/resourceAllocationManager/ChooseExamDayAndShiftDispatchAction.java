/**
 * Project Sop 
 * 
 * Package presentationTier.Action.sop
 * 
 * Created on 2003/03/19
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.Util;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ChooseExamDayAndShiftDispatchAction extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	String nextPage = request.getParameter(PresentationConstants.NEXT_PAGE);
	if (nextPage != null)
	    request.setAttribute("nextPage", nextPage);

	List horas = Util.getExamShifts();
	request.setAttribute(PresentationConstants.LABLELIST_HOURS, horas);

	List daysOfMonth = Util.getDaysOfMonth();
	request.setAttribute(PresentationConstants.LABLELIST_DAYSOFMONTH, daysOfMonth);

	List monthsOfYear = Util.getMonthsOfYear();
	request.setAttribute(PresentationConstants.LABLELIST_MONTHSOFYEAR, monthsOfYear);

	List years = Util.getYears();
	request.setAttribute(PresentationConstants.LABLELIST_YEARS, years);

	return mapping.findForward("Show Choose Form");

    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	DynaValidatorForm chooseDayAndShiftForm = (DynaValidatorForm) form;

	Integer day = new Integer((String) chooseDayAndShiftForm.get("day"));
	Integer month = new Integer((String) chooseDayAndShiftForm.get("month"));
	Integer year = new Integer((String) chooseDayAndShiftForm.get("year"));
	Integer beginning = new Integer((String) chooseDayAndShiftForm.get("beginning"));

	Calendar examDateAndTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"), new Locale("pt", "PT"));
	examDateAndTime.set(Calendar.YEAR, year.intValue());
	examDateAndTime.set(Calendar.MONTH, month.intValue());
	examDateAndTime.set(Calendar.DAY_OF_MONTH, day.intValue());
	examDateAndTime.set(Calendar.HOUR_OF_DAY, beginning.intValue());
	examDateAndTime.set(Calendar.MINUTE, 0);
	examDateAndTime.set(Calendar.SECOND, 0);
	examDateAndTime.set(Calendar.MILLISECOND, 0);

	request.setAttribute(PresentationConstants.EXAM_DATEANDTIME_STR, "" + year + "/" + (month.intValue() + 1) + "/" + day
		+ "  �s  " + beginning + " horas");

	request.removeAttribute(PresentationConstants.EXAM_DATEANDTIME);
	request.setAttribute(PresentationConstants.EXAM_DATEANDTIME, examDateAndTime);

	String nextPage = request.getParameter(PresentationConstants.NEXT_PAGE);
	return mapping.findForward(nextPage);

    }

}