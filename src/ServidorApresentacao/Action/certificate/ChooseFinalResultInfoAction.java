
package ServidorApresentacao.Action.certificate;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoFinalResult;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.FinalResulUnreachedActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.EnrolmentState;
import Util.Specialization;
import Util.TipoCurso;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 */
public class ChooseFinalResultInfoAction extends DispatchAction {

	public ActionForward prepare(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);
		DynaActionForm chooseDeclaration = (DynaActionForm) form;


		if (session != null) {
			
			session.removeAttribute(SessionConstants.SPECIALIZATIONS);
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			ArrayList specializations = Specialization.toArrayList();
			session.setAttribute(SessionConstants.SPECIALIZATIONS, specializations);
					
			return mapping.findForward("PrepareReady");
		  } else
			throw new Exception();   

	}




	public ActionForward choose(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);

		if (session != null) {

			DynaActionForm chooseDeclaration = (DynaActionForm) form;
			
			GestorServicos serviceManager = GestorServicos.manager();	
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			//remove sessions variables
			session.removeAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN);
			session.removeAttribute(SessionConstants.DEGREE_TYPE);
			session.removeAttribute(SessionConstants.DATE);
			session.removeAttribute(SessionConstants.INFO_FINAL_RESULT);
			session.removeAttribute(SessionConstants.ENROLMENT_LIST);
			session.removeAttribute(SessionConstants.INFO_EXECUTION_YEAR);
			session.removeAttribute(SessionConstants.CONCLUSION_DATE);
			

			
			// Get request Information
			Integer requesterNumber = new Integer((String) chooseDeclaration.get("requesterNumber"));
     		String graduationType = (String) chooseDeclaration.get("graduationType");
   
		

			// inputs
			InfoStudent infoStudent = new InfoStudent();
			infoStudent.setNumber(requesterNumber);
			infoStudent.setDegreeType(new TipoCurso(TipoCurso.MESTRADO));
			session.setAttribute(SessionConstants.DEGREE_TYPE, infoStudent.getDegreeType());
			
	        
	        // output
			InfoStudentCurricularPlan infoStudentCurricularPlan = null;
			InfoExecutionYear infoExecutionYear = null;
			List enrolmentList = null;
			
			try {
				Object args[] = {infoStudent, new Specialization(graduationType)};
				infoStudentCurricularPlan = (InfoStudentCurricularPlan) serviceManager.executar(userView, "CreateDeclaration", args);

			} catch (NonExistingServiceException e) {
				throw new NonExistingActionException("O Aluno", e);
			}

			if (infoStudentCurricularPlan == null){
				throw new NonExistingActionException("O Aluno");
			}
			else {		
				InfoFinalResult infoFinalResult = null;
				try {	
					Object args[] = {infoStudentCurricularPlan};	
					infoFinalResult =  (InfoFinalResult) serviceManager.executar(userView, "FinalResult", args);
				}catch (FenixServiceException e){
					throw new FenixServiceException ("");	
				}
				if (infoFinalResult == null){
					throw new FinalResulUnreachedActionException("");
				}
				else {	
					try {
						Object args[] = {infoStudentCurricularPlan, EnrolmentState.APROVED};
						enrolmentList = (List) serviceManager.executar(userView, "GetEnrolmentList", args);
	
					} catch (NonExistingServiceException e) {
						throw new NonExistingActionException("Inscri��o", e);
					}

				
					if (enrolmentList.size() == 0){
						throw new NonExistingActionException("Inscri��o em Disciplinas");
					}
					else {
						//check the last exam date
						InfoEnrolmentEvaluation infoEnrolmentEvaluation = new InfoEnrolmentEvaluation();
						String conclusionDate = "00/00/00";
						String dataAux = null;					
						Object result = null;
						Iterator iterator = enrolmentList.iterator();
						int i = 0;
						while(iterator.hasNext()) {	
							result = iterator.next();
							infoEnrolmentEvaluation = (InfoEnrolmentEvaluation) (((InfoEnrolment) result).getInfoEvaluations().get(i));	
							dataAux = DateFormat.getDateInstance().format(infoEnrolmentEvaluation.getExamDate());	
							if (conclusionDate.compareTo(dataAux) == -1){
								conclusionDate = dataAux;
							}					 
						}			
						session.setAttribute(SessionConstants.CONCLUSION_DATE, conclusionDate);	
						try {
							infoExecutionYear = (InfoExecutionYear) serviceManager.executar(userView, "ReadCurrentExecutionYear", null);
		
						} catch (RuntimeException e) {
							throw new RuntimeException("Error", e);
						}
						Locale locale = new Locale("pt", "PT");
						Date date = new Date();
						String formatedDate = "Lisboa, " + DateFormat.getDateInstance(DateFormat.LONG, locale).format(date);
						session.setAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN, infoStudentCurricularPlan);		
						session.setAttribute(SessionConstants.DATE, formatedDate);			
						session.setAttribute(SessionConstants.INFO_EXECUTION_YEAR, infoExecutionYear);	
						session.setAttribute(SessionConstants.ENROLMENT_LIST, enrolmentList);	
						session.setAttribute(SessionConstants.INFO_FINAL_RESULT, infoFinalResult);	

						return mapping.findForward("ChooseSuccess"); 
				 
				    }
				}
			}
		
		  }
		  else
		  	throw new Exception();   
	  }	  
	}

	  

