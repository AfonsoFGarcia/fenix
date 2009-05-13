package net.sourceforge.fenixedu.presentationTier.Action.certificate;

import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis.ReadActiveMasterDegreeProofVersionByStudentCurricularPlan;

import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis.ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurrentExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ScholarshipNotFinishedServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeProofVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.masterDegree.DocumentReason;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ScholarshipNotFinishedActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.util.CertificateList;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 */
public class ChooseCertificateInfoAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	return mapping.findForward("PrepareReady");
    }

    public ActionForward chooseStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Integer number = new Integer((String) ((DynaActionForm) form).get("requesterNumber"));
	request.setAttribute("registrations", Registration.readByNumberAndDegreeType(number, DegreeType.MASTER_DEGREE));

	request.setAttribute(PresentationConstants.DOCUMENT_REASON, DocumentReason.values());
	request.setAttribute(PresentationConstants.CERTIFICATE_LIST, new CertificateList().toArrayList());

	return mapping.findForward("ChooseStudentCurricularPlan");
    }

    public ActionForward chooseFinal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	IUserView userView = getUserView(request);

	DynaActionForm chooseDeclaration = (DynaActionForm) form;

	// Get the Information
	String certificateString = (String) chooseDeclaration.get("certificateList");
	String[] destination = (String[]) chooseDeclaration.get("destination");
	Integer studentCurricularPlanID = (Integer) chooseDeclaration.get("studentCurricularPlanID");

	if (destination.length != 0) {
	    request.setAttribute(PresentationConstants.DOCUMENT_REASON_LIST, destination);
	}

	InfoStudentCurricularPlan infoStudentCurricularPlan = InfoStudentCurricularPlan.newInfoFromDomain(rootDomainObject
		.readStudentCurricularPlanByOID(studentCurricularPlanID));

	int initialYear = infoStudentCurricularPlan.getInfoDegreeCurricularPlan().getInitialDate().getYear() + 1900;
	String initialExecutionYear = initialYear + "/" + ++initialYear;
	request.setAttribute("initialExecutionYear", initialExecutionYear);

	InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = null;
	InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = null;
	InfoExecutionYear infoExecutionYear = null;

	try {
	    if (certificateString.equals("Fim de curso de Mestrado discriminada com m�dia")
		    || certificateString.equals("Fim de curso de Mestrado simples") || certificateString.equals("Carta de Curso")) {

		try {
		    infoMasterDegreeThesisDataVersion = (InfoMasterDegreeThesisDataVersion) ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan.run(infoStudentCurricularPlan);
		} catch (NonExistingServiceException e) {
		    throw new NonExistingActionException("O registo da tese ", e);

		}

		/* get master degree proof */

		try {
		    infoMasterDegreeProofVersion = (InfoMasterDegreeProofVersion) ReadActiveMasterDegreeProofVersionByStudentCurricularPlan.run(studentCurricularPlanID);
		} catch (NonExistingServiceException e) {
		    throw new NonExistingActionException("O registo da tese ", e);
		} catch (ScholarshipNotFinishedServiceException e) {
		    throw new ScholarshipNotFinishedActionException("", e);
		}

	    }

	    infoExecutionYear = (InfoExecutionYear) ReadCurrentExecutionYear.run();

	} catch (RuntimeException e) {
	    throw new RuntimeException("Error", e);
	}

	Locale locale = new Locale("pt", "PT");
	Date date = new Date();
	String formatedDate = "Lisboa, " + DateFormat.getDateInstance(DateFormat.LONG, locale).format(date);
	request.setAttribute(PresentationConstants.DATE, formatedDate);

	request.setAttribute(PresentationConstants.CERTIFICATE_TYPE, certificateString);
	request.setAttribute(PresentationConstants.MASTER_DEGREE_THESIS_DATA_VERSION, infoMasterDegreeThesisDataVersion);
	request.setAttribute(PresentationConstants.MASTER_DEGREE_PROOF_HISTORY, infoMasterDegreeProofVersion);
	request.setAttribute(PresentationConstants.INFO_EXECUTION_YEAR, infoExecutionYear);
	request.setAttribute(PresentationConstants.INFO_STUDENT_CURRICULAR_PLAN, infoStudentCurricularPlan);

	return mapping.findForward("ChooseSuccess");

    }

}