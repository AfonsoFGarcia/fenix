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

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.IEnrollment;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadStudentListByCurricularCourse implements IServico {

    private static ReadStudentListByCurricularCourse servico = new ReadStudentListByCurricularCourse();

    /**
     * The singleton access method of this class.
     */
    public static ReadStudentListByCurricularCourse getService() {
        return servico;
    }

    /**
     * The actor of this class.
     */
    private ReadStudentListByCurricularCourse() {
    }

    /**
     * Returns The Service Name
     */

    public final String getNome() {
        return "ReadStudentListByCurricularCourse";
    }

    public List run(IUserView userView, Integer curricularCourseID,
            String executionYear) throws ExcepcaoInexistente,
            FenixServiceException {

        ISuportePersistente sp = null;

        List enrolmentList = null;

        ICurricularCourse curricularCourse = null;
        try {
            sp = SuportePersistenteOJB.getInstance();

            // Read the Students

            curricularCourse = (ICurricularCourse) sp
                    .getIPersistentCurricularCourse().readByOID(
                            CurricularCourse.class, curricularCourseID);

            if (executionYear != null) {
                enrolmentList = sp.getIPersistentEnrolment()
                        .readByCurricularCourseAndYear(curricularCourse,
                                executionYear);
            } else {
                enrolmentList = sp.getIPersistentEnrolment()
                        .readByCurricularCourse(curricularCourse);
            }

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException(
                    "Persistence layer error");
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
    private List cleanList(List studentCurricularPlans, IUserView userView)
            throws FenixServiceException {
        List result = new ArrayList();
        Integer numberAux = null;

        BeanComparator numberComparator = new BeanComparator(
                "studentCurricularPlan.student.number");
        Collections.sort(studentCurricularPlans, numberComparator);

        Iterator iterator = studentCurricularPlans.iterator();
        while (iterator.hasNext()) {
            IEnrollment enrolment = (IEnrollment) iterator.next();

            if ((numberAux == null)
                    || (numberAux.intValue() != enrolment
                            .getStudentCurricularPlan().getStudent()
                            .getNumber().intValue())) {
                numberAux = enrolment.getStudentCurricularPlan().getStudent()
                        .getNumber();

                Object args[] = { enrolment };
                InfoEnrolmentEvaluation infoEnrolmentEvaluation = (InfoEnrolmentEvaluation) ServiceManagerServiceFactory
                        .executeService(userView, "GetEnrolmentGrade", args);

                InfoEnrolment infoEnrolment = Cloner
                        .copyIEnrolment2InfoEnrolment(enrolment);
                infoEnrolment
                        .setInfoEnrolmentEvaluation(infoEnrolmentEvaluation);

                result.add(infoEnrolment);
            }
        }

        return result;
    }

}