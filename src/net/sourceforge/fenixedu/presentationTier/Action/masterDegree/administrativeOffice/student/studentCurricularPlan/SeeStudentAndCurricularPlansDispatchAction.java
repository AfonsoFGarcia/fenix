package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author David Santos 2/Out/2003
 */

public class SeeStudentAndCurricularPlansDispatchAction extends FenixDispatchAction {

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	// request.setAttribute("docIDTypeList",
	// TipoDocumentoIdentificacao.toIntegerArrayList());
	return mapping.findForward("start");
    }

    public ActionForward read(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws FenixActionException, FenixFilterException {

	String idNumber1 = this.getFromRequest("idNumber", request);
	String idType1 = this.getFromRequest("idType", request);
	String studentName1 = this.getFromRequest("studentName", request);
	String studentNumber1 = this.getFromRequest("studentNumber", request);

	String idNumber2 = null;
	IDDocumentType idType2 = null;
	String studentName2 = null;
	Integer studentNumber2 = null;

	if (!idNumber1.equals("")) {
	    idNumber2 = idNumber1;
	}

	if (idType1 != null && !idType1.equals("")) {
	    idType2 = IDDocumentType.valueOf(idType1);
	}

	if (!studentName1.equals("")) {
	    studentName2 = studentName1;
	}

	if (!studentNumber1.equals("")) {
	    try {
		studentNumber2 = new Integer(studentNumber1);
	    } catch (NumberFormatException e) {
		this.saveError(request, "error.numberFormat", "studentNumber");
		return mapping.getInputForward();
	    }
	}

	if (!this.isValid(request, idNumber2, idType2)) {
	    return mapping.getInputForward();
	}

	Object args[] = { studentName2, idNumber2, idType2, studentNumber2 };
	IUserView userView = getUserView(request);

	List studentList = null;
	try {
	    studentList = (ArrayList) ServiceUtils.executeService("ReadStudentsByNameIDnumberIDtypeAndStudentNumber", args);
	    if (studentList != null && !studentList.isEmpty()) {
		this.sort(studentList);
	    }
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}

	if (studentList != null && !studentList.isEmpty()) {
	    request.setAttribute("studentList", studentList);
	} else {
	    this.saveError(request, "error.no.students", "noStudents");
	    return mapping.getInputForward();
	}

	return mapping.findForward("viewStudents");
    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
	String parameterString = request.getParameter(parameter);
	if (parameterString == null) {
	    parameterString = (String) request.getAttribute(parameter);
	}
	return parameterString;
    }

    private static final Comparator<InfoStudent> infoStudentComparator = new Comparator<InfoStudent>() {

	@Override
	public int compare(InfoStudent o1, InfoStudent o2) {
	    final int n = o1.getNumber().compareTo(o2.getNumber());
	    return n == 0 ? o1.getInfoPerson().getNome().compareTo(o2.getInfoPerson().getNome()): n;
	}
	
    };
    private void sort(List listOfInfoStudents) {
	if (listOfInfoStudents != null) {
	    Collections.sort(listOfInfoStudents, infoStudentComparator);
	}
    }

    private boolean isValid(HttpServletRequest request, String idNumber, IDDocumentType idType) {
	boolean result = true;

	if (idNumber != null && idType == null) {
	    this.saveError(request, "error.required.idType", "idType");
	    result = false;
	} else if (idNumber == null && idType != null) {
	    this.saveError(request, "error.required.idNumber", "idNumber");
	    result = false;
	}
	return result;
    }

    private void saveError(HttpServletRequest request, String errorMessageKey, String errorKey) {
	ActionError actionError = new ActionError(errorMessageKey);
	ActionErrors actionErrors = new ActionErrors();
	actionErrors.add(errorKey, actionError);
	saveErrors(request, actionErrors);
    }
}