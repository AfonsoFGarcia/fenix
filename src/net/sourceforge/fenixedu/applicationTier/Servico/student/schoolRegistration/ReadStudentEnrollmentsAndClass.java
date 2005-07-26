/*
 * Created on Jul 28, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.schoolRegistration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithInfoCurricularCourse;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class ReadStudentEnrollmentsAndClass implements IService {

    public List run(IUserView userView) throws ExcepcaoPersistencia {

        ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentStudentCurricularPlan pSCP = suportePersistente
                .getIStudentCurricularPlanPersistente();
        IPersistentExecutionPeriod persistentExecutionPeriod = suportePersistente
                .getIPersistentExecutionPeriod();

        String user = userView.getUtilizador();
        Integer studentNumber = new Integer(user.substring(1));

        IStudentCurricularPlan scp = pSCP.readActiveStudentCurricularPlan(studentNumber,
                DegreeType.DEGREE);
        List studentEnrollments = scp.getEnrolments();
        IExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
        
        List studentShifts = new ArrayList();
        List<IShift> shifts = scp.getStudent().getShifts();
        for (IShift shift : shifts) {
            if (shift.getDisciplinaExecucao().getExecutionPeriod().equals(executionPeriod)) {
                studentShifts.add(shift);
            }
        }
        
        List filteredStudentShifts = filterStudentShifts(studentShifts);

        InfoClass infoClass = getClass(filteredStudentShifts, scp.getDegreeCurricularPlan().getDegree()
                .getNome());
        List infoEnrollments = new ArrayList();

        for (int iterator = 0; iterator < studentEnrollments.size(); iterator++) {

            IEnrolment enrollment = (IEnrolment) studentEnrollments.get(iterator);

            InfoEnrolment infoEnrollment = InfoEnrolmentWithInfoCurricularCourse
                    .newInfoFromDomain(enrollment);
            infoEnrollments.add(infoEnrollment);
        }

        List result = new ArrayList();
        result.add(infoEnrollments);
        result.add(infoClass);
        result.add(scp.getDegreeCurricularPlan().getDegree().getNome());

        return result;
    }

    /**
     * @param studentShifts
     * @return
     */
    private InfoClass getClass(List studentShifts, String degreeName) {

        List classesName = new ArrayList();
        InfoClass infoClass = new InfoClass();
        for (int iter = 0; iter < studentShifts.size(); iter++) {
            IShift shift = (IShift) studentShifts.get(0);
            List classes = shift.getAssociatedClasses();
            if (classes.size() == 1) {
                ISchoolClass klass = (ISchoolClass) classes.get(0);
                infoClass.setNome(klass.getNome());
                return infoClass;
            }

            for (int j = 0; j < classes.size(); j++) {
                ISchoolClass klass = (ISchoolClass) classes.get(j);
                if (degreeName.equals(klass.getExecutionDegree().getDegreeCurricularPlan().getDegree()
                        .getNome())) {
                    classesName.add(klass.getNome());
                }
            }

        }
        String className = getMaxOcurrenceElement(classesName);
        infoClass.setNome(className);
        return infoClass;
    }

    /**
     * @param studentShifts
     * @return
     */
    private List filterStudentShifts(List studentShifts) {
        List filteredStudentShifts = (List) CollectionUtils.select(studentShifts, new Predicate() {
            List validTypes = Arrays.asList(new ShiftType[] { ShiftType.PRATICA,
                    ShiftType.TEORICO_PRATICA });

            public boolean evaluate(Object input) {
                IShift shift = (IShift) input;
                return validTypes.contains(shift.getTipo());
            }
        });

        return filteredStudentShifts;
    }

    private String getMaxOcurrenceElement(List classes) {

        int maxNumberOfOcurrencies = 0;
        String resultElement = null;
        for (int iter = 0; iter < classes.size(); iter++) {
            String element = (String) classes.get(iter);
            int numberOfOcurrencis = CollectionUtils.cardinality(element, classes);
            if (numberOfOcurrencis > maxNumberOfOcurrencies)
                resultElement = element;
        }
        return resultElement;
    }
}

