package ServidorApresentacao.Action.certificate;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoEnrolmentInExtraCurricularCourse;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoFinalResult;
import DataBeans.InfoMasterDegreeProofVersion;
import DataBeans.InfoMasterDegreeThesisDataVersion;
import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.Data;
import Util.EnrollmentState;
import Util.MasterDegreeClassification;
import Util.Specialization;
import framework.factory.ServiceManagerServiceFactory;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 *  
 */
public class PrintCertificateDispatchAction extends DispatchAction {
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        //		String[] destination = null;
        //		InfoStudent infoStudent = new InfoStudent();
        //		InfoDegree infoDegree = new InfoDegree();
        //		InfoPerson infoPerson = new InfoPerson();
        //		InfoDegreeCurricularPlan infoDegreeCurricularPlan = new
        // InfoDegreeCurricularPlan();
        //		InfoEnrolment infoEnrolment = new InfoEnrolment();
        //		InfoStudentCurricularPlan newInfoStudentCurricularPlan = new
        // InfoStudentCurricularPlan();
        if (session != null) {
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
            session.removeAttribute(SessionConstants.MATRICULA);
            session.removeAttribute(SessionConstants.MATRICULA_ENROLMENT);
            session.removeAttribute(SessionConstants.DURATION_DEGREE);
            session.removeAttribute(SessionConstants.ENROLMENT);
            session.removeAttribute(SessionConstants.ENROLMENT_LIST);
            session.removeAttribute(SessionConstants.APROVMENT);
            session.removeAttribute(SessionConstants.EXTRA_CURRICULAR_APROVMENT);
            session.removeAttribute(SessionConstants.EXTRA_ENROLMENT_LIST);
            session.removeAttribute(SessionConstants.INFO_FINAL_RESULT);
            session.removeAttribute(SessionConstants.FINAL_RESULT);
            session.removeAttribute(SessionConstants.CONCLUSION_DATE);
            session.removeAttribute(SessionConstants.FINAL_RESULT_SIMPLE);
            session.removeAttribute(SessionConstants.FINAL_RESULT_DEGREE_SIMPLE);
            session.removeAttribute(SessionConstants.DISCRIMINATED_WITHOUT_AVERAGE);
            session.removeAttribute(SessionConstants.DISCRIMINATED_WITH_AVERAGE);
            session.removeAttribute(SessionConstants.FINAL_DEGREE_DISCRIMINATED_WITH_AVERAGE);
            session.removeAttribute(SessionConstants.DIPLOMA);
            session.removeAttribute(SessionConstants.DATE);
            session.removeAttribute("notString");
            InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) session
                    .getAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN);
            InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = (InfoMasterDegreeThesisDataVersion) session
                    .getAttribute(SessionConstants.MASTER_DEGREE_THESIS_DATA_VERSION);
            InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = (InfoMasterDegreeProofVersion) session
                    .getAttribute(SessionConstants.MASTER_DEGREE_PROOF_HISTORY);
            String certificate = (String) session.getAttribute(SessionConstants.CERTIFICATE_TYPE);
            String anoLectivo = new String();
            InfoExecutionYear infoExecutionYear = null;
            try {
                infoExecutionYear = (InfoExecutionYear) ServiceManagerServiceFactory.executeService(
                        userView, "ReadCurrentExecutionYear", null);
            } catch (RuntimeException e) {
                throw new RuntimeException("Error", e);
            }
            if ((certificate.equals("Matr�cula")) || (certificate.equals("Matr�cula e Inscri��o"))
                    || (certificate.equals("Dura��o do Curso"))) {
                List enrolmentList = null;
                Object args[] = { infoStudentCurricularPlan.getIdInternal() };
                try {
                    enrolmentList = (List) ServiceManagerServiceFactory.executeService(userView,
                            "GetEnrolmentList", args);
                } catch (NonExistingServiceException e) {
                    throw new NonExistingActionException("Inscri��o", e);
                }
                if (enrolmentList.size() == 0)
                    anoLectivo = infoExecutionYear.getYear();
                else
                    anoLectivo = ((InfoEnrolment) enrolmentList.get(0)).getInfoExecutionPeriod()
                            .getInfoExecutionYear().getYear();
                if (certificate.equals("Matr�cula"))
                    session.setAttribute(SessionConstants.MATRICULA, certificate.toUpperCase());
                if (certificate.equals("Matr�cula e Inscri��o"))
                    session
                            .setAttribute(SessionConstants.MATRICULA_ENROLMENT, certificate
                                    .toUpperCase());
                if (certificate.equals("Dura��o do Curso")) {
                    if (infoStudentCurricularPlan.getSpecialization().equals(
                            new Specialization(Specialization.MESTRADO))) {
                        certificate = new String("Matr�cula");
                        session
                                .setAttribute(SessionConstants.DURATION_DEGREE, certificate
                                        .toUpperCase());
                    } else {
                        ActionErrors errors = new ActionErrors();
                        errors.add("AlunoN�oExiste", new ActionError("error.invalidStudentType"));
                        saveErrors(request, errors);
                        return new ActionForward(mapping.getInput());
                    }
                }
            } else {
                //get informations
                List enrolmentList = null;
                if (certificate.equals("Inscri��o")) {
                    //					, EnrolmentState.ENROLED
                    Object args[] = { infoStudentCurricularPlan.getIdInternal() };
                    try {
                        enrolmentList = (List) ServiceManagerServiceFactory.executeService(userView,
                                "GetEnrolmentList", args);
                    } catch (NonExistingServiceException e) {
                        throw new NonExistingActionException("Inscri��o", e);
                    }
                    if (enrolmentList.size() == 0) {
                        ActionErrors errors = new ActionErrors();
                        errors.add("AlunoN�oExiste", new ActionError("error.enrolment.notExist"));
                        saveErrors(request, errors);
                        return new ActionForward(mapping.getInput());
                    }
                    List normalEnrolment = new ArrayList();
                    List extraEnrolment = new ArrayList();
                    //		InfoEnrolment infoEnrolment = new InfoEnrolment();
                    Object result = null;
                    Iterator iterator = enrolmentList.iterator();
                    while (iterator.hasNext()) {
                        result = iterator.next();
                        InfoEnrolment infoEnrolment2 = (InfoEnrolment) result;
                        List aux = infoEnrolment2.getInfoEvaluations();
                        if (aux != null && aux.size() > 0) {
                            if (aux.size() > 1) {
                                BeanComparator dateComparator = new BeanComparator("when");
                                Collections.sort(aux, dateComparator);
                                Collections.reverse(aux);
                            }
                            InfoEnrolmentEvaluation latestEvaluation = (InfoEnrolmentEvaluation) aux
                                    .get(0);
                            infoEnrolment2.setInfoEnrolmentEvaluation(latestEvaluation);
                        }
                        if (result instanceof InfoEnrolmentInExtraCurricularCourse) {
                            extraEnrolment.add(infoEnrolment2);
                            anoLectivo = ((InfoEnrolment) extraEnrolment.get(0))
                                    .getInfoExecutionPeriod().getInfoExecutionYear().getYear();
                        } else {
                            normalEnrolment.add(infoEnrolment2);
                            anoLectivo = ((InfoEnrolment) normalEnrolment.get(0))
                                    .getInfoExecutionPeriod().getInfoExecutionYear().getYear();
                        }
                    }
                    if (normalEnrolment.size() != 0)
                        session.setAttribute(SessionConstants.ENROLMENT_LIST, normalEnrolment);
                    if (extraEnrolment.size() != 0)
                        session.setAttribute(SessionConstants.EXTRA_ENROLMENT_LIST, extraEnrolment);
                    session.setAttribute(SessionConstants.ENROLMENT, certificate.toUpperCase());
                } else {
                    if ((certificate.equals("Aproveitamento"))
                            || (certificate.equals("Aproveitamento de Disciplinas Extra Curricular"))) {
                        Object args[] = { infoStudentCurricularPlan.getIdInternal(),
                                EnrollmentState.APROVED };
                        try {
                            enrolmentList = (List) ServiceManagerServiceFactory.executeService(userView,
                                    "GetEnrolmentList", args);
                        } catch (NonExistingServiceException e) {
                            throw new NonExistingActionException("Inscri��o", e);
                        }
                        if (enrolmentList.size() == 0) {
                            ActionErrors errors = new ActionErrors();
                            errors.add("AlunoN�oExiste", new ActionError("error.enrolment.notExist"));
                            saveErrors(request, errors);
                            return new ActionForward(mapping.getInput());
                        }
                        List normalEnrolment = new ArrayList();
                        List extraEnrolment = new ArrayList();
                        //	InfoEnrolment infoEnrolment = new InfoEnrolment();
                        Object result = null;
                        Iterator iterator = enrolmentList.iterator();
                        while (iterator.hasNext()) {
                            result = iterator.next();
                            InfoEnrolment infoEnrolment2 = (InfoEnrolment) result;
                            List aux = infoEnrolment2.getInfoEvaluations();
                            if (aux != null && aux.size() > 0) {
                                if (aux.size() > 1) {
                                    BeanComparator dateComparator = new BeanComparator("when");
                                    Collections.sort(aux, dateComparator);
                                    Collections.reverse(aux);
                                }
                                InfoEnrolmentEvaluation latestEvaluation = (InfoEnrolmentEvaluation) aux
                                        .get(0);
                                infoEnrolment2.setInfoEnrolmentEvaluation(latestEvaluation);
                            }
                            if (result instanceof InfoEnrolmentInExtraCurricularCourse) {
                                extraEnrolment.add(infoEnrolment2);
                                anoLectivo = ((InfoEnrolment) extraEnrolment.get(0))
                                        .getInfoExecutionPeriod().getInfoExecutionYear().getYear();
                            } else {
                                normalEnrolment.add(infoEnrolment2);
                                anoLectivo = ((InfoEnrolment) normalEnrolment.get(0))
                                        .getInfoExecutionPeriod().getInfoExecutionYear().getYear();
                            }
                        }
                        //					Object result = null;
                        //					Iterator iterator = enrolmentList.iterator();
                        //					while(iterator.hasNext()) {
                        //						result = (InfoEnrolment) iterator.next();
                        //						if (result instanceof
                        // InfoEnrolmentInExtraCurricularCourse)
                        //							extraEnrolment.add(result);
                        //						else
                        //							normalEnrolment.add(result);
                        //					}
                        session.setAttribute(SessionConstants.ENROLMENT_LIST, normalEnrolment);
                        session.setAttribute(SessionConstants.EXTRA_ENROLMENT_LIST, extraEnrolment);
                        if (certificate.equals("Aproveitamento")) {
                            if (normalEnrolment.size() == 0) {
                                ActionErrors errors = new ActionErrors();
                                errors
                                        .add("AlunoN�oExiste", new ActionError(
                                                "error.enrolment.notExist"));
                                saveErrors(request, errors);
                                return new ActionForward(mapping.getInput());
                            }
                            session.setAttribute(SessionConstants.APROVMENT, certificate.toUpperCase());
                        } else if (certificate.equals("Aproveitamento de Disciplinas Extra Curricular")) {
                            if (extraEnrolment.size() == 0) {
                                ActionErrors errors = new ActionErrors();
                                errors
                                        .add("AlunoN�oExiste", new ActionError(
                                                "error.enrolment.notExist"));
                                saveErrors(request, errors);
                                return new ActionForward(mapping.getInput());
                            }
                            session.setAttribute(SessionConstants.EXTRA_CURRICULAR_APROVMENT,
                                    certificate.toUpperCase());
                        }
                    } else {
                        if (infoStudentCurricularPlan.getSpecialization().equals(
                                new Specialization(Specialization.MESTRADO))) {
                            if ((certificate.equals("Fim parte escolar simples"))
                                    || (certificate.equals("Fim parte escolar discriminada sem m�dia"))
                                    || (certificate.equals("Fim parte escolar discriminada com m�dia"))
                                    || (certificate.equals("Diploma"))
                                    || (certificate
                                            .equals("Fim de curso de Mestrado discriminada com m�dia"))) {
                                InfoFinalResult infoFinalResult = null;
                                try {
                                    Object args[] = { infoStudentCurricularPlan };
                                    infoFinalResult = (InfoFinalResult) ServiceManagerServiceFactory
                                            .executeService(userView, "FinalResult", args);
                                } catch (FenixServiceException e) {
                                    throw new FenixServiceException("");
                                }
                                if (infoFinalResult == null) {
                                    ActionErrors actionErrors = new ActionErrors();
                                    actionErrors.add("FinalResultError", new ActionError(
                                            "error.exception.unreached"));
                                    saveErrors(request, actionErrors);
                                    return mapping.findForward("insucesso");
                                    //								throw new
                                    // FinalResulUnreachedActionException("aqui");
                                }
                                Object args[] = { infoStudentCurricularPlan.getIdInternal(),
                                        EnrollmentState.APROVED };
                                try {
                                    enrolmentList = (List) ServiceManagerServiceFactory.executeService(
                                            userView, "GetEnrolmentList", args);
                                } catch (NonExistingServiceException e) {
                                    throw new NonExistingActionException("Inscri��o", e);
                                }
                                if (enrolmentList.size() == 0) {
                                    ActionErrors errors = new ActionErrors();
                                    errors.add("AlunoN�oExiste", new ActionError(
                                            "error.enrolment.notExist"));
                                    saveErrors(request, errors);
                                    return new ActionForward(mapping.getInput());
                                }
                                //		InfoEnrolmentEvaluation
                                // infoEnrolmentEvaluation = new
                                // InfoEnrolmentEvaluation();
                                String conclusionDate = null;
                                Date endOfScholarshipDate = null;
                                try {
                                    Object argsTemp[] = { infoStudentCurricularPlan };
                                    endOfScholarshipDate = (Date) ServiceManagerServiceFactory
                                            .executeService(userView, "GetEndOfScholarshipDate",
                                                    argsTemp);
                                } catch (FenixServiceException e) {
                                    throw new FenixActionException(e);
                                }
                                conclusionDate = Data.format2DayMonthYear(endOfScholarshipDate, "-");
                                //			String dataAux = null;
                                //							Object result = null;
                                List normalEnrolment = new ArrayList();
                                List extraEnrolment = new ArrayList();
                                //		InfoEnrolment infoEnrolment = new
                                // InfoEnrolment();
                                Object result = null;
                                Iterator iterator = enrolmentList.iterator();
                                while (iterator.hasNext()) {
                                    result = iterator.next();
                                    InfoEnrolment infoEnrolment2 = (InfoEnrolment) result;
                                    List aux = infoEnrolment2.getInfoEvaluations();
                                    if (aux != null && aux.size() > 0) {
                                        if (aux.size() > 1) {
                                            BeanComparator dateComparator = new BeanComparator("when");
                                            Collections.sort(aux, dateComparator);
                                            Collections.reverse(aux);
                                        }
                                        InfoEnrolmentEvaluation latestEvaluation = (InfoEnrolmentEvaluation) aux
                                                .get(0);
                                        infoEnrolment2.setInfoEnrolmentEvaluation(latestEvaluation);
                                    }
                                    if (result instanceof InfoEnrolmentInExtraCurricularCourse) {
                                        extraEnrolment.add(infoEnrolment2);
                                        anoLectivo = ((InfoEnrolment) extraEnrolment.get(0))
                                                .getInfoExecutionPeriod().getInfoExecutionYear()
                                                .getYear();
                                    } else {
                                        normalEnrolment.add(infoEnrolment2);
                                        anoLectivo = ((InfoEnrolment) normalEnrolment.get(0))
                                                .getInfoExecutionPeriod().getInfoExecutionYear()
                                                .getYear();
                                    }
                                }
                                //Iterator iterator = enrolmentList.iterator();
                                //							int i = 0;
                                //							while(iterator.hasNext()) {
                                //								result = iterator.next();
                                //								infoEnrolmentEvaluation =
                                // (InfoEnrolmentEvaluation)(((InfoEnrolment)
                                // result).getInfoEvaluations().get(i));
                                //// dataAux =
                                // DateFormat.getDateInstance().format(infoEnrolmentEvaluation.getExamDate());
                                //// if (conclusionDate.compareTo(dataAux) ==
                                // -1){
                                //// conclusionDate = dataAux;
                                //// }
                                //								if (result instanceof
                                // InfoEnrolmentInExtraCurricularCourse)
                                //									extraEnrolment.add(result);
                                //								else
                                //									normalEnrolment.add(result);
                                //							}
                                //							
                                Object argsAux[] = { infoStudentCurricularPlan };
                                Date date = null;
                                try {
                                    date = (Date) ServiceManagerServiceFactory.executeService(userView,
                                            "GetEndOfScholarshipDate", argsAux);
                                } catch (NonExistingServiceException e) {
                                    throw new NonExistingActionException("Inscri��o", e);
                                }
                                conclusionDate = DateFormat.getDateInstance().format(date);
                                session.setAttribute(SessionConstants.ENROLMENT_LIST, normalEnrolment);
                                session.setAttribute(SessionConstants.EXTRA_ENROLMENT_LIST,
                                        extraEnrolment);
                                session.setAttribute(SessionConstants.CONCLUSION_DATE, conclusionDate);
                                session
                                        .setAttribute(SessionConstants.INFO_FINAL_RESULT,
                                                infoFinalResult);
                                session.setAttribute(SessionConstants.MASTER_DEGREE_THESIS_DATA_VERSION,
                                        infoMasterDegreeThesisDataVersion);
                                //  							
                                if (certificate.equals("Fim parte escolar simples"))
                                    session.setAttribute(SessionConstants.FINAL_RESULT_SIMPLE,
                                            certificate.toUpperCase());
                                if (certificate.equals("Fim parte escolar discriminada sem m�dia"))
                                    session.setAttribute(SessionConstants.DISCRIMINATED_WITHOUT_AVERAGE,
                                            certificate.toUpperCase());
                                if (certificate.equals("Fim parte escolar discriminada com m�dia"))
                                    session.setAttribute(SessionConstants.DISCRIMINATED_WITH_AVERAGE,
                                            certificate.toUpperCase());
                                if (certificate.equals("Diploma"))
                                    session.setAttribute(SessionConstants.DIPLOMA, certificate
                                            .toUpperCase());
                                if (certificate
                                        .equals("Fim de curso de Mestrado discriminada com m�dia"))

                                {
                                    if (infoMasterDegreeProofVersion != null) {
                                        if (infoMasterDegreeProofVersion.getFinalResult().getValue() != 3) {
                                            String notString = " ";
                                            if (infoMasterDegreeProofVersion.getFinalResult().getValue() == 2)
                                                notString = " n�o ";
                                            session.setAttribute("notString", notString);
                                            session
                                                    .setAttribute(
                                                            SessionConstants.FINAL_DEGREE_DISCRIMINATED_WITH_AVERAGE,
                                                            certificate.toUpperCase());
                                            String dataFinal = DateFormat.getDateInstance().format(
                                                    infoMasterDegreeProofVersion.getProofDate());
                                            session.setAttribute(SessionConstants.CONCLUSION_DATE,
                                                    dataFinal);
                                            int value = infoMasterDegreeProofVersion.getFinalResult()
                                                    .getValue();
                                            session.setAttribute(SessionConstants.FINAL_RESULT,
                                                    MasterDegreeClassification
                                                    //												infoMasterDegreeProofVersion
                                                            //													.getFinalResult()
                                                            .getClassificationString(value));
                                        } else {
                                            ActionErrors errors = new ActionErrors();
                                            errors.add("AlunoN�oExiste", new ActionError(
                                                    "error.invalidStudentType"));
                                            saveErrors(request, errors);
                                            return new ActionForward(mapping.getInput());
                                        }
                                    }
                                }
                            } else {
                                if ((certificate.equals("Fim de curso de Mestrado simples"))) {
                                    if (infoMasterDegreeProofVersion.getFinalResult().getValue() != 3) {
                                        session.setAttribute(
                                                SessionConstants.FINAL_RESULT_DEGREE_SIMPLE, certificate
                                                        .toUpperCase());
                                        session.setAttribute(
                                                SessionConstants.MASTER_DEGREE_THESIS_HISTORY,
                                                infoMasterDegreeProofVersion);
                                        String dataFinal = DateFormat.getDateInstance().format(
                                                infoMasterDegreeProofVersion.getProofDate());
                                        session
                                                .setAttribute(SessionConstants.CONCLUSION_DATE,
                                                        dataFinal);
                                        int value = infoMasterDegreeProofVersion.getFinalResult()
                                                .getValue();
                                        String notString = " ";
                                        if (value == 2)
                                            notString = " n�o ";
                                        session.setAttribute("notString", notString);
                                        session.setAttribute(SessionConstants.FINAL_RESULT,
                                                MasterDegreeClassification
                                                //											infoMasterDegreeProofVersion
                                                        //												.getFinalResult()
                                                        .getClassificationString(value));
                                        session.setAttribute(
                                                SessionConstants.MASTER_DEGREE_THESIS_DATA_VERSION,
                                                infoMasterDegreeThesisDataVersion);
                                    } else {
                                        ActionErrors errors = new ActionErrors();
                                        errors.add("AlunoN�oExiste", new ActionError(
                                                "error.invalidStudentConclusion"));
                                        saveErrors(request, errors);
                                        return new ActionForward(mapping.getInput());
                                    }
                                }
                            }
                        } else {
                            ActionErrors errors = new ActionErrors();
                            errors.add("AlunoN�oExiste", new ActionError("error.invalidStudentType"));
                            saveErrors(request, errors);
                            return new ActionForward(mapping.getInput());
                        }
                    }
                }
            }
            session.setAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN,
                    infoStudentCurricularPlan);
            Locale locale = new Locale("pt", "PT");
            Date date = new Date();
            String formatedDate = "Lisboa, "
                    + DateFormat.getDateInstance(DateFormat.LONG, locale).format(date);
            request.setAttribute("anoLectivo", anoLectivo);
            session.setAttribute(SessionConstants.DATE, formatedDate);
            session.setAttribute(SessionConstants.CERTIFICATE_TYPE, certificate);
            return mapping.findForward("PrintReady");
        }

        throw new Exception();
    }
}