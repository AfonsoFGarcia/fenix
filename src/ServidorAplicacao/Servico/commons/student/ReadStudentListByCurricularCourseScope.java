/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package ServidorAplicacao.Servico.commons.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import Dominio.CurricularCourseScope;
import Dominio.ICurricularCourseScope;
import Dominio.IEnrollment;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import framework.factory.ServiceManagerServiceFactory;

public class ReadStudentListByCurricularCourseScope implements IService {

    /**
     * The actor of this class.
     */
    public ReadStudentListByCurricularCourseScope() {
    }

    public List run(IUserView userView, Integer curricularCourseScopeID) throws ExcepcaoInexistente,
            FenixServiceException {

        ISuportePersistente sp = null;

        List enrolmentList = null;

        ICurricularCourseScope curricularCourseScope = null;
        try {
            sp = SuportePersistenteOJB.getInstance();

            // Read the Students

            curricularCourseScope = (ICurricularCourseScope) sp.getIPersistentCurricularCourseScope()
                    .readByOID(CurricularCourseScope.class, curricularCourseScopeID);

            //enrolmentList =
            // sp.getIPersistentEnrolment().readByCurricularCourseScope(curricularCourseScope);
            enrolmentList = sp.getIPersistentEnrolment().readByCurricularCourse(
                    curricularCourseScope.getCurricularCourse());

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if ((enrolmentList == null) || (enrolmentList.size() == 0)) {
            throw new NonExistingServiceException();
        }

        return cleanList(enrolmentList, userView);
    }

    /**
     * @param studentCurricularPlans
     * @return A list of Student curricular Plans without the duplicates
     */
    private List cleanList(List studentCurricularPlans, IUserView userView) throws FenixServiceException {
        List result = new ArrayList();
        Integer numberAux = null;

        BeanComparator numberComparator = new BeanComparator("studentCurricularPlan.student.number");
        Collections.sort(studentCurricularPlans, numberComparator);

        Iterator iterator = studentCurricularPlans.iterator();
        while (iterator.hasNext()) {
            IEnrollment enrolment = (IEnrollment) iterator.next();

            if ((numberAux == null)
                    || (numberAux.intValue() != enrolment.getStudentCurricularPlan().getStudent()
                            .getNumber().intValue())) {
                numberAux = enrolment.getStudentCurricularPlan().getStudent().getNumber();

                Object args[] = { enrolment };
                InfoEnrolmentEvaluation infoEnrolmentEvaluation = (InfoEnrolmentEvaluation) ServiceManagerServiceFactory
                        .executeService(userView, "GetEnrolmentGrade", args);

                InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod
                        .newInfoFromDomain(enrolment);
                infoEnrolment.setInfoEnrolmentEvaluation(infoEnrolmentEvaluation);

                result.add(infoEnrolment);
            }
        }

        return result;
    }

}