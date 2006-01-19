package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranchAndInfoDegreeCurricularPlanAndDegree;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/*
 * Created on 24/Set/2003, 11:26:29
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 24/Set/2003, 11:26:29
 * 
 */
public class ReadActiveStudentCurricularPlanByNumberAndDegreeType extends Service {

    public InfoStudentCurricularPlan run(Integer studentNumber, DegreeType degreeType)
            throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final StudentCurricularPlan studentCurricularPlan = sp.getIStudentCurricularPlanPersistente()
                .readActiveByStudentNumberAndDegreeType(studentNumber, degreeType);

        if (studentCurricularPlan != null) {
            return InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranchAndInfoDegreeCurricularPlanAndDegree
                    .newInfoFromDomain(studentCurricularPlan);
        }
        return null;
    }

}