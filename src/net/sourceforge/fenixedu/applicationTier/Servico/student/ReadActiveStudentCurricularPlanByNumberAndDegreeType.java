package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

/*
 * Created on 24/Set/2003, 11:26:29
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 24/Set/2003, 11:26:29
 * 
 */
public class ReadActiveStudentCurricularPlanByNumberAndDegreeType extends FenixService {

    @Service
    public static InfoStudentCurricularPlan run(Integer studentNumber, DegreeType degreeType) {
        Registration registration = Registration.readStudentByNumberAndDegreeType(studentNumber, degreeType);
        if (registration == null) {
            return null;
        }
        StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
        if (studentCurricularPlan != null) {
            return InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan);
        }
        return null;
    }

}