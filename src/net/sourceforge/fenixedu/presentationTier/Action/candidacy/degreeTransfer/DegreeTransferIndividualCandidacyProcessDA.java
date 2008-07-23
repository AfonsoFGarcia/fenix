package net.sourceforge.fenixedu.presentationTier.Action.candidacy.degreeTransfer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.IndividualCandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/caseHandlingDegreeTransferIndividualCandidacyProcess", module = "academicAdminOffice", formBeanClass = FenixActionForm.class)
@Forwards( {
	@Forward(name = "intro", path = "/candidacy/mainCandidacyProcess.jsp"),
	@Forward(name = "list-allowed-activities", path = "/candidacy/listIndividualCandidacyActivities.jsp"),
	@Forward(name = "prepare-create-new-process", path = "/candidacy/selectPersonForCandidacy.jsp"),
	@Forward(name = "fill-personal-information", path = "/candidacy/fillPersonalInformation.jsp"),
	@Forward(name = "fill-candidacy-information", path = "/candidacy/degreeTransfer/fillCandidacyInformation.jsp"),
	@Forward(name = "prepare-candidacy-payment", path = "/candidacy/candidacyPayment.jsp"),
	@Forward(name = "edit-candidacy-personal-information", path = "/candidacy/editPersonalInformation.jsp"),
	@Forward(name = "edit-candidacy-information", path = "/candidacy/degreeTransfer/editCandidacyInformation.jsp"),
	@Forward(name = "edit-candidacy-curricularCourses-information", path = "/candidacy/degreeTransfer/editCandidacyCurricularCoursesInformation.jsp"),
	@Forward(name = "introduce-candidacy-result", path = "/candidacy/degreeTransfer/introduceCandidacyResult.jsp"),
	@Forward(name = "cancel-candidacy", path = "/candidacy/cancelCandidacy.jsp"),
	@Forward(name = "create-registration", path = "/candidacy/createRegistration.jsp")

})
public class DegreeTransferIndividualCandidacyProcessDA extends IndividualCandidacyProcessDA {

    @Override
    protected Class getParentProcessType() {
	return DegreeTransferCandidacyProcess.class;
    }

    @Override
    protected Class getProcessType() {
	return DegreeTransferIndividualCandidacyProcess.class;
    }
    
    @Override
    protected DegreeTransferCandidacyProcess getParentProcess(HttpServletRequest request) {
        return (DegreeTransferCandidacyProcess) super.getParentProcess(request);
    }
    
    @Override
    protected DegreeTransferIndividualCandidacyProcess getProcess(HttpServletRequest request) {
        return (DegreeTransferIndividualCandidacyProcess) super.getProcess(request);
    }
    
    @Override
    protected DegreeTransferIndividualCandidacyProcessBean getIndividualCandidacyProcessBean() {
        return (DegreeTransferIndividualCandidacyProcessBean) super.getIndividualCandidacyProcessBean();
    }

    @Override
    protected void setStartInformation(ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	final DegreeTransferIndividualCandidacyProcessBean bean = new DegreeTransferIndividualCandidacyProcessBean();
	bean.setCandidacyProcess(getParentProcess(request));
	bean.setChoosePersonBean(new ChoosePersonBean());
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
    }

    @Override
    protected void createCandidacyPrecedentDegreeInformation(IndividualCandidacyProcessWithPrecedentDegreeInformationBean bean,
	    StudentCurricularPlan studentCurricularPlan) {
	super.createCandidacyPrecedentDegreeInformation(bean, studentCurricularPlan);
	bean.getPrecedentDegreeInformation().initCurricularCoursesInformation(studentCurricularPlan);
    }
}
