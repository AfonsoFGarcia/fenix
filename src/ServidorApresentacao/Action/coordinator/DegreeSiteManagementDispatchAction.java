package ServidorApresentacao.Action.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoDegreeInfo;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;

/**
 * @author T�nia Pous�o Created on 31/Out/2003
 */
public class DegreeSiteManagementDispatchAction extends FenixDispatchAction {

	public ActionForward subMenu(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		return mapping.findForward("degreeSiteMenu");
	}

	public ActionForward viewInformation(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		System.out.println("--->Entrou viewInformation...");
		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute("UserView");

		Integer infoExecutionDegreeId = getFromRequest("infoExecutionDegreeId", request);
		request.setAttribute("infoExecutionDegreeId", infoExecutionDegreeId);

		Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
		request.setAttribute("inEnglish", inEnglish);

		
		Object[] args = { infoExecutionDegreeId };

		GestorServicos gestorServicos = GestorServicos.manager();

		InfoDegreeInfo infoDegreeInfo = null;
		Integer infoDegreeInfoId = new Integer(0);
		try {
			infoDegreeInfo = (InfoDegreeInfo) gestorServicos.executar(userView, "ReadDegreeInfoByExecutionDegree", args);
		} catch (FenixServiceException e) {
			e.printStackTrace();
			throw new FenixActionException(e);
		}

		DynaActionForm degreeInfoForm = (DynaActionForm) form;

		if (infoDegreeInfo != null) {
			fillForm(infoDegreeInfo, degreeInfoForm);

			infoDegreeInfoId = infoDegreeInfo.getIdInternal();
		}

		request.setAttribute("infoDegreeInfoId", infoDegreeInfoId);

		System.out.println("--->Saiu viewInformation...");
		return mapping.findForward("viewInformation");
	}

	public ActionForward editDegreeInformation(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		System.out.println("--->Entrou editDegreeInfomation...");
		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute("UserView");

		Integer infoExecutionDegreeId = getFromRequest("infoExecutionDegreeId", request);
		request.setAttribute("infoExecutionDegreeId", infoExecutionDegreeId);

		Integer infoDegreeInfoId = getFromRequest("infoDegreeInfoId", request);
		request.setAttribute("infoDegreeInfoId", infoDegreeInfoId);

		DynaActionForm degreeInfoForm = (DynaActionForm) form;
		InfoDegreeInfo infoDegreeInfo = new InfoDegreeInfo();
		infoDegreeInfo.setIdInternal(infoDegreeInfoId);
		fillInfoDegreeInfo(degreeInfoForm, infoDegreeInfo);

		Object[] args = { infoExecutionDegreeId, infoDegreeInfoId, infoDegreeInfo };

		GestorServicos gestorServicos = GestorServicos.manager();

		try {
			gestorServicos.executar(userView, "EditDegreeInfoByExecutionDegree", args);
		} catch (FenixServiceException e) {
			e.printStackTrace();
			throw new FenixActionException(e);
		}

		System.out.println("--->Saiu editDegreeInfomation...");
		return mapping.findForward("degreeSiteMenu");
	}

	private void fillForm(InfoDegreeInfo infoDegreeInfo, DynaActionForm degreeInfoForm) {
		degreeInfoForm.set("objectives", infoDegreeInfo.getObjectives());
		degreeInfoForm.set("history", infoDegreeInfo.getHistory());
		degreeInfoForm.set("professionalExits", infoDegreeInfo.getProfessionalExits());
		degreeInfoForm.set("additionalInfo", infoDegreeInfo.getAdditionalInfo());
		degreeInfoForm.set("links", infoDegreeInfo.getLinks());
		degreeInfoForm.set("testIngression", infoDegreeInfo.getTestIngression());
		degreeInfoForm.set("driftsInitial", infoDegreeInfo.getDriftsInitial());
		degreeInfoForm.set("driftsFirst", infoDegreeInfo.getDriftsFirst());
		degreeInfoForm.set("driftsSecond", infoDegreeInfo.getDriftsSecond());
		degreeInfoForm.set("classifications", infoDegreeInfo.getClassifications());
		degreeInfoForm.set("markMin", infoDegreeInfo.getMarkMin());
		degreeInfoForm.set("markMax", infoDegreeInfo.getMarkMax());
		degreeInfoForm.set("markAverage", infoDegreeInfo.getMarkAverage());
		degreeInfoForm.set("lastModificationDate", infoDegreeInfo.getLastModificationDate().toString());

		degreeInfoForm.set("objectivesEn", infoDegreeInfo.getObjectivesEn());
		degreeInfoForm.set("historyEn", infoDegreeInfo.getHistoryEn());
		degreeInfoForm.set("professionalExitsEn", infoDegreeInfo.getProfessionalExitsEn());
		degreeInfoForm.set("additionalInfoEn", infoDegreeInfo.getAdditionalInfoEn());
		degreeInfoForm.set("linksEn", infoDegreeInfo.getLinksEn());
		degreeInfoForm.set("testIngressionEn", infoDegreeInfo.getTestIngressionEn());
		degreeInfoForm.set("classificationsEn", infoDegreeInfo.getClassificationsEn());
	}

	private void fillInfoDegreeInfo(DynaActionForm degreeInfoForm, InfoDegreeInfo infoDegreeInfo) {
		infoDegreeInfo.setObjectives((String) degreeInfoForm.get("objectives"));
		infoDegreeInfo.setHistory((String) degreeInfoForm.get("history"));
		infoDegreeInfo.setProfessionalExits((String) degreeInfoForm.get("professionalExits"));
		infoDegreeInfo.setAdditionalInfo((String) degreeInfoForm.get("additionalInfo"));
		infoDegreeInfo.setLinks((String) degreeInfoForm.get("links"));
		infoDegreeInfo.setTestIngression((String) degreeInfoForm.get("testIngression"));
		infoDegreeInfo.setDriftsInitial((Integer) degreeInfoForm.get("driftsInitial"));
		infoDegreeInfo.setDriftsFirst((Integer) degreeInfoForm.get("driftsFirst"));
		infoDegreeInfo.setDriftsSecond((Integer) degreeInfoForm.get("driftsSecond"));
		infoDegreeInfo.setClassifications((String) degreeInfoForm.get("classifications"));
		infoDegreeInfo.setMarkMin((Double) degreeInfoForm.get("markMin"));
		infoDegreeInfo.setMarkMax((Double) degreeInfoForm.get("markMax"));
		infoDegreeInfo.setMarkAverage((Double) degreeInfoForm.get("markAverage"));

		//information in english
		infoDegreeInfo.setObjectivesEn((String) degreeInfoForm.get("objectivesEn"));
		infoDegreeInfo.setHistoryEn((String) degreeInfoForm.get("historyEn"));
		infoDegreeInfo.setProfessionalExitsEn((String) degreeInfoForm.get("professionalExitsEn"));
		infoDegreeInfo.setAdditionalInfoEn((String) degreeInfoForm.get("additionalInfoEn"));
		infoDegreeInfo.setLinksEn((String) degreeInfoForm.get("linksEn"));
		infoDegreeInfo.setTestIngressionEn((String) degreeInfoForm.get("testIngressionEn"));
		infoDegreeInfo.setClassificationsEn((String) degreeInfoForm.get("classificationsEn"));
	}

	public ActionForward viewHistoric(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		return mapping.findForward("viewHistoric");
	}

	public ActionForward viewDegreeSite(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		return mapping.findForward("viewSite");
	}

	private Integer getFromRequest(String parameter, HttpServletRequest request) {
		Integer parameterCode = null;
		String parameterCodeString = request.getParameter(parameter);
		if (parameterCodeString == null) {
			parameterCodeString = (String) request.getAttribute(parameter);
		}
		if (parameterCodeString != null) {
			parameterCode = new Integer(parameterCodeString);
		}
		return parameterCode;
	}

	private Boolean getFromRequestBoolean(String parameter, HttpServletRequest request) {
		Boolean parameterBoolean = null;

		String parameterCodeString = request.getParameter(parameter);
		if (parameterCodeString == null) {
			parameterCodeString = (String) request.getAttribute(parameter);
		}
		if (parameterCodeString != null) {
			parameterBoolean = new Boolean(parameterCodeString);
		}

		return parameterBoolean;
	}
}
