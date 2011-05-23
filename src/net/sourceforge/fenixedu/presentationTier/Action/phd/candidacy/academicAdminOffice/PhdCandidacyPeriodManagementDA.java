package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.academicAdminOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.candidacy.EPFLPhdCandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.candidacy.InstitutionPhdCandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriodBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdCandidacyPeriodManagement", module = "academicAdminOffice")
@Forwards({
	@Forward(name = "list", path = "/phd/candidacy/academicAdminOffice/periods/list.jsp"),
	@Forward(name = "createPhdCandidacyPeriod", path = "/phd/candidacy/academicAdminOffice/periods/createPhdCandidacyPeriod.jsp"),
	@Forward(name = "editPhdCandidacyPeriod", path = "/phd/candidacy/academicAdminOffice/periods/editPhdCandidacyPeriod.jsp"),
	@Forward(name = "view", path = "/phd/candidacy/academicAdminOffice/periods/view.jsp")
})
public class PhdCandidacyPeriodManagementDA extends FenixDispatchAction {

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("phdCandidacyPeriods", PhdCandidacyPeriod.readPhdCandidacyPeriods());

	return mapping.findForward("list");
    }

    public ActionForward prepareCreatePhdCandidacyPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdCandidacyPeriodBean bean = new PhdCandidacyPeriodBean();
	request.setAttribute("phdCandidacyPeriodBean", bean);

	return mapping.findForward("createPhdCandidacyPeriod");
    }

    public ActionForward createPhdCandidacyPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdCandidacyPeriodBean bean = readPhdCandidacyPeriodBean();

	switch (bean.getType()) {
	case EPFL:
	    EPFLPhdCandidacyPeriod.create(bean);
	    break;
	case INSTITUTION:
	    InstitutionPhdCandidacyPeriod.create(bean);
	    break;
	default:
	    throw new DomainException("error.PhdCandidacyPeriodBean.type.missing");
	}

	return list(mapping, form, request, response);
    }

    public ActionForward createPhdCandidacyPeriodInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("phdCandidacyPeriodBean", readPhdCandidacyPeriodBean());
	request.setAttribute("phdCandidacyPeriod", readPhdCandidacyPeriod(request));

	return mapping.findForward("createPhdCandidacyPeriod");
    }

    public ActionForward prepareEditPhdCandidacyPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdCandidacyPeriod phdCandidacyPeriod = readPhdCandidacyPeriod(request);
	request.setAttribute("phdCandidacyPeriodBean", new PhdCandidacyPeriodBean(phdCandidacyPeriod));
	request.setAttribute("phdCandidacyPeriod", readPhdCandidacyPeriod(request));

	return mapping.findForward("editPhdCandidacyPeriod");
    }

    public ActionForward editPhdCandidacyPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdCandidacyPeriod phdCandidacyPeriod = readPhdCandidacyPeriod(request);
	PhdCandidacyPeriodBean bean = readPhdCandidacyPeriodBean();

	phdCandidacyPeriod.edit(bean.getStart(), bean.getEnd());

	return list(mapping, form, request, response);
    }

    public ActionForward editPhdCandidacyPeriodInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("phdCandidacyPeriodBean", readPhdCandidacyPeriodBean());
	return mapping.findForward("editPhdCandidacyPeriod");
    }

    public ActionForward addPhdProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdCandidacyPeriodBean phdCandidacyPeriodBean = readPhdCandidacyPeriodBean();
	InstitutionPhdCandidacyPeriod phdCandidacyPeriod = (InstitutionPhdCandidacyPeriod) readPhdCandidacyPeriod(request);

	phdCandidacyPeriod.addPhdProgramToPeriod(phdCandidacyPeriodBean.getPhdProgram());

	return prepareEditPhdCandidacyPeriod(mapping, form, request, response);
    }

    public ActionForward removePhdProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	InstitutionPhdCandidacyPeriod phdCandidacyPeriod = (InstitutionPhdCandidacyPeriod) readPhdCandidacyPeriod(request);
	phdCandidacyPeriod.removePhdProgramInPeriod(readPhdProgram(request));

	return prepareEditPhdCandidacyPeriod(mapping, form, request, response);
    }

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	PhdCandidacyPeriod period = readPhdCandidacyPeriod(request);

	request.setAttribute("phdCandidacyPeriod", period);

	return mapping.findForward("view");
    }

    private PhdProgram readPhdProgram(final HttpServletRequest request) {
	return getDomainObject(request, "phdProgramId");
    }

    private PhdCandidacyPeriodBean readPhdCandidacyPeriodBean() {
	return getRenderedObject("phdCandidacyPeriodBean");
    }

    private PhdCandidacyPeriod readPhdCandidacyPeriod(HttpServletRequest request) {
	return getDomainObject(request, "phdCandidacyPeriodId");
    }
}

