
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

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoEnrolmentInExtraCurricularCourse;
import DataBeans.InfoFinalResult;
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

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * 
 */
public class PrintCertificateDispatchAction extends DispatchAction {

	public ActionForward prepare(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);
//		String[] destination = null;
//		InfoStudent infoStudent = new InfoStudent();
//		InfoDegree infoDegree = new InfoDegree();
//		InfoPerson infoPerson = new InfoPerson();
//		InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
//		InfoEnrolment infoEnrolment = new InfoEnrolment();
//		InfoStudentCurricularPlan newInfoStudentCurricularPlan = new InfoStudentCurricularPlan();
		
		if (session != null) {
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			GestorServicos serviceManager = GestorServicos.manager();	
			
			session.removeAttribute(SessionConstants.MATRICULA);
			session.removeAttribute(SessionConstants.MATRICULA_ENROLMENT);
			session.removeAttribute(SessionConstants.DURATION_DEGREE);
			session.removeAttribute(SessionConstants.ENROLMENT);	
			session.removeAttribute(SessionConstants.ENROLMENT_LIST);	
			session.removeAttribute(SessionConstants.APROVMENT);
			session.removeAttribute(SessionConstants.EXTRA_CURRICULAR_APROVMENT);
			session.removeAttribute(SessionConstants.EXTRA_ENROLMENT_LIST);
			session.removeAttribute(SessionConstants.INFO_FINAL_RESULT);	
			session.removeAttribute(SessionConstants.CONCLUSION_DATE);
			session.removeAttribute(SessionConstants.FINAL_RESULT_SIMPLE);
			session.removeAttribute(SessionConstants.DISCRIMINATED_WITHOUT_AVERAGE);
			session.removeAttribute(SessionConstants.DISCRIMINATED_WITH_AVERAGE);
			session.removeAttribute(SessionConstants.DIPLOMA);
			session.removeAttribute(SessionConstants.DATE);
			
			
			InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) session.getAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN);
			
			String certificate = (String) session.getAttribute(SessionConstants.CERTIFICATE_TYPE);	
		    
			if ((certificate.equals("Matr�cula")) || (certificate.equals("Matr�cula e Inscri��o")) || (certificate.equals("Dura��o do Curso"))){
				if (certificate.equals("Matr�cula"))
							session.setAttribute(SessionConstants.MATRICULA, certificate.toUpperCase());
				if (certificate.equals("Matr�cula e Inscri��o"))
							session.setAttribute(SessionConstants.MATRICULA_ENROLMENT, certificate.toUpperCase());
				if (certificate.equals("Dura��o do Curso")){
					
					if (infoStudentCurricularPlan.getSpecialization().equals(new Specialization(Specialization.MESTRADO))){
							certificate=new String("Matr�cula");
							session.setAttribute(SessionConstants.DURATION_DEGREE, certificate.toUpperCase());
					}
					else{
						ActionErrors errors = new ActionErrors();
						errors.add("AlunoN�oExiste",
									new ActionError("error.invalidStudentType"));
						saveErrors(request, errors);
						return new ActionForward(mapping.getInput());
					}
				}			
				
			}else{ 
				
				//get informations
				List enrolmentList = null;				
				if (certificate.equals("Inscri��o")){
					Object args[] = {infoStudentCurricularPlan, EnrolmentState.ENROLED};
					try {
						enrolmentList = (List) serviceManager.executar(userView, "GetEnrolmentList", args);

					} catch (NonExistingServiceException e) {
						throw new NonExistingActionException("Inscri��o", e);
					}
					if (enrolmentList.size() == 0){
						ActionErrors errors = new ActionErrors();
						errors.add("AlunoN�oExiste",
							new ActionError("error.enrolment.notExist"));
						saveErrors(request, errors);
						return new ActionForward(mapping.getInput());
					}
		
					List normalEnrolment = new ArrayList();
					List extraEnrolment = new ArrayList();
					Object result = null;
					Iterator iterator = enrolmentList.iterator();
					while(iterator.hasNext()) {	
						result = (InfoEnrolment) iterator.next();
						if (result instanceof InfoEnrolmentInExtraCurricularCourse)	
							extraEnrolment.add(result);		
						else
							normalEnrolment.add(result);							 
					}
					if (normalEnrolment.size() != 0)
							session.setAttribute(SessionConstants.ENROLMENT_LIST, normalEnrolment);
					if (extraEnrolment.size() != 0)
							session.setAttribute(SessionConstants.EXTRA_ENROLMENT_LIST, extraEnrolment);		
					session.setAttribute(SessionConstants.ENROLMENT, certificate.toUpperCase());			

				}
				if ((certificate.equals("Aproveitamento")) || (certificate.equals("Aproveitamento de Disciplinas Extra Curricular"))) {
					Object args[] = {infoStudentCurricularPlan, EnrolmentState.APROVED };
					try {
						enrolmentList = (List) serviceManager.executar(userView, "GetEnrolmentList", args);

					} catch (NonExistingServiceException e) {
						throw new NonExistingActionException("Inscri��o", e);
					}
					if (enrolmentList.size() == 0){
						ActionErrors errors = new ActionErrors();
						errors.add("AlunoN�oExiste",
								new ActionError("error.enrolment.notExist"));
						saveErrors(request, errors);
						return new ActionForward(mapping.getInput());
					}
					List normalEnrolment = new ArrayList();
					List extraEnrolment = new ArrayList();
					Object result = null;
					Iterator iterator = enrolmentList.iterator();
					while(iterator.hasNext()) {	
						result = (InfoEnrolment) iterator.next();
						if (result instanceof InfoEnrolmentInExtraCurricularCourse)	
							extraEnrolment.add(result);		
						else
							normalEnrolment.add(result);							 
					}
					
					session.setAttribute(SessionConstants.ENROLMENT_LIST, normalEnrolment);
					session.setAttribute(SessionConstants.EXTRA_ENROLMENT_LIST, extraEnrolment);	
					
					if (certificate.equals("Aproveitamento")){
						if ( normalEnrolment.size() == 0){
							ActionErrors errors = new ActionErrors();
							errors.add("AlunoN�oExiste",
									new ActionError("error.enrolment.notExist"));
							saveErrors(request, errors);
							return new ActionForward(mapping.getInput());
						}
						session.setAttribute(SessionConstants.APROVMENT, certificate.toUpperCase());
					}
				    else						
						if (certificate.equals("Aproveitamento de Disciplinas Extra Curricular")){
							if ( extraEnrolment.size() == 0){
								ActionErrors errors = new ActionErrors();
								errors.add("AlunoN�oExiste",
									new ActionError("error.enrolment.notExist"));
								saveErrors(request, errors);
								return new ActionForward(mapping.getInput());
							}						
							session.setAttribute(SessionConstants.EXTRA_CURRICULAR_APROVMENT, certificate.toUpperCase());
						}
						
				}
				if (infoStudentCurricularPlan.getSpecialization().equals(new Specialization(Specialization.MESTRADO))){
						
						if ((certificate.equals("Fim parte escolar simples")) 
						   || (certificate.equals("Fim parte escolar discriminada sem m�dia")) 
						   || (certificate.equals("Fim parte escolar discriminada com m�dia"))
						   || (certificate.equals("Diploma"))){
								
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
							Object args[] = {infoStudentCurricularPlan, EnrolmentState.APROVED };
							try {
								enrolmentList = (List) serviceManager.executar(userView, "GetEnrolmentList", args);
			
							} catch (NonExistingServiceException e) {
								throw new NonExistingActionException("Inscri��o", e);
							}
							if (enrolmentList.size() == 0){
								ActionErrors errors = new ActionErrors();
								errors.add("AlunoN�oExiste",
											new ActionError("error.enrolment.notExist"));
								saveErrors(request, errors);
								return new ActionForward(mapping.getInput());
							}
							InfoEnrolmentEvaluation infoEnrolmentEvaluation = new InfoEnrolmentEvaluation();
							String conclusionDate = "00/00/00";
							String dataAux = null;					
							Object result = null;
							List normalEnrolment = new ArrayList();
							List extraEnrolment = new ArrayList();
							Iterator iterator = enrolmentList.iterator();
							int i = 0;
							while(iterator.hasNext()) {	
								result = iterator.next();
								infoEnrolmentEvaluation = (InfoEnrolmentEvaluation)(((InfoEnrolment) result).getInfoEvaluations().get(i));	
//								dataAux = DateFormat.getDateInstance().format(infoEnrolmentEvaluation.getExamDate());	
//								if (conclusionDate.compareTo(dataAux) == -1){
//									conclusionDate = dataAux;
//								}		
								if (result instanceof InfoEnrolmentInExtraCurricularCourse)	
									extraEnrolment.add(result);		
								else
									normalEnrolment.add(result);			 
							}			
							
							Object argsAux[] = {infoStudentCurricularPlan};
							Date date = null;
							try {
								date = (Date) serviceManager.executar(userView, "GetEndOfScholarshipDate", argsAux);

							} catch (NonExistingServiceException e) {
								throw new NonExistingActionException("Inscri��o", e);
							}
							
							conclusionDate = DateFormat.getDateInstance().format(date);
							
							
							session.setAttribute(SessionConstants.ENROLMENT_LIST, normalEnrolment);
							session.setAttribute(SessionConstants.EXTRA_ENROLMENT_LIST, extraEnrolment);						
							session.setAttribute(SessionConstants.CONCLUSION_DATE, conclusionDate);	
							session.setAttribute(SessionConstants.INFO_FINAL_RESULT, infoFinalResult);
							
							if (certificate.equals("Fim parte escolar simples"))	
								session.setAttribute(SessionConstants.FINAL_RESULT_SIMPLE, certificate.toUpperCase());
							if (certificate.equals("Fim parte escolar discriminada sem m�dia"))	
								session.setAttribute(SessionConstants.DISCRIMINATED_WITHOUT_AVERAGE, certificate.toUpperCase());
							if (certificate.equals("Fim parte escolar discriminada com m�dia"))	
								session.setAttribute(SessionConstants.DISCRIMINATED_WITH_AVERAGE, certificate.toUpperCase());
							if (certificate.equals("Diploma"))	
								session.setAttribute(SessionConstants.DIPLOMA, certificate.toUpperCase());						
						}
				}
				else{
					ActionErrors errors = new ActionErrors();
					errors.add("AlunoN�oExiste",
								new ActionError("error.invalidStudentType"));
					saveErrors(request, errors);
					return new ActionForward(mapping.getInput());
				}
			}	
			
  			session.setAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN, infoStudentCurricularPlan);
  			
  			
			Locale locale = new Locale("pt", "PT");
			Date date = new Date();
			String formatedDate = "Lisboa, " + DateFormat.getDateInstance(DateFormat.LONG, locale).format(date);

			session.setAttribute(SessionConstants.DATE, formatedDate);
			session.setAttribute(SessionConstants.CERTIFICATE_TYPE, certificate);
			
		
		    
		    return mapping.findForward("PrintReady");
			
		  } else
			throw new Exception();   

	}

	  
}
