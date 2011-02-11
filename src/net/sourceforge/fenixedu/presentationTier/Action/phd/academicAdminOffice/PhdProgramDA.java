package net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.PhdProgramContextPeriod;
import net.sourceforge.fenixedu.domain.phd.PhdProgramContextPeriodBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdProgram", module = "academicAdminOffice")
@Forwards({
	@Forward(name = "listPhdProgramForPeriods", path = "/phd/academicAdminOffice/periods/phdProgram/listPhdProgramForPeriods.jsp"),
	@Forward(name = "viewPhdProgramPeriods", path = "/phd/academicAdminOffice/periods/phdProgram/viewPhdProgramPeriods.jsp"),
	@Forward(name = "addPhdProgramPeriod", path = "/phd/academicAdminOffice/periods/phdProgram/addPhdProgramPeriod.jsp") })
public class PhdProgramDA extends FenixDispatchAction {

    // Phd Program Management Periods

    public ActionForward listPhdProgramForPeriods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("phdPrograms", RootDomainObject.getInstance().getPhdPrograms());
	return mapping.findForward("listPhdProgramForPeriods");
    }

    public ActionForward viewPhdProgramPeriods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdProgram phdProgram = (PhdProgram) getDomainObject(request, "phdProgramId");

	request.setAttribute("phdProgram", phdProgram);
	return mapping.findForward("viewPhdProgramPeriods");
    }

    public ActionForward removePhdProgramPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdProgramContextPeriod period = getDomainObject(request, "phdProgramContextPeriodId");
	period.deletePeriod();

	return viewPhdProgramPeriods(mapping, form, request, response);
    }

    public ActionForward prepareAddPhdProgramPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdProgram phdProgram = (PhdProgram) getDomainObject(request, "phdProgramId");
	PhdProgramContextPeriodBean bean = new PhdProgramContextPeriodBean(phdProgram);

	request.setAttribute("phdProgram", phdProgram);
	request.setAttribute("phdProgramContextPeriodBean", bean);

	return mapping.findForward("addPhdProgramPeriod");
    }

    public ActionForward addPhdProgramPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdProgram phdProgram = getDomainObject(request, "phdProgramId");
	PhdProgramContextPeriodBean bean = getRenderedObject("phdProgramContextPeriodBean");
	phdProgram.create(bean);

	return viewPhdProgramPeriods(mapping, form, request, response);
    }

    public ActionForward addPhdProgramPeriodInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdProgram phdProgram = getDomainObject(request, "phdProgramId");
	PhdProgramContextPeriodBean bean = getRenderedObject("phdProgramContextPeriodBean");

	request.setAttribute("phdProgram", phdProgram);
	request.setAttribute("phdProgramContextPeriodBean", bean);

	return mapping.findForward("addPhdProgramPeriod");
    }

    // End of Phd Program Management Periods

}
