/*
 * Created on 21/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBranch;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularSemester;

/**
 * @author lmac1
 */
public class InsertCurricularCourseScopeAtCurricularCourse extends Service {

    public void run(InfoCurricularCourseScope infoCurricularCourseScope) throws FenixServiceException, ExcepcaoPersistencia {
        Branch branch = null;
        CurricularSemester curricularSemester = null;
        try {
            IPersistentCurricularSemester persistentCurricularSemester = persistentSupport.getIPersistentCurricularSemester();
            IPersistentCurricularCourse persistentCurricularCourse = persistentSupport.getIPersistentCurricularCourse();
            IPersistentBranch persistentBranch = persistentSupport.getIPersistentBranch();

			
			
            curricularSemester = (CurricularSemester) persistentCurricularSemester.readByOID(
                    CurricularSemester.class, infoCurricularCourseScope.getInfoCurricularSemester().getIdInternal());
            if (curricularSemester == null)
                throw new NonExistingServiceException("message.non.existing.curricular.semester", null);
         
			
            
			CurricularCourse curricularCourse = (CurricularCourse) persistentCurricularCourse.readByOID(
					CurricularCourse.class, infoCurricularCourseScope.getInfoCurricularCourse().getIdInternal());
            if (curricularCourse == null)
                throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);

			
			
            branch = (Branch) persistentBranch.readByOID(
					Branch.class, infoCurricularCourseScope.getInfoBranch().getIdInternal());
            if (branch == null)
                throw new NonExistingServiceException("message.non.existing.branch", null);
			
			DomainFactory.makeCurricularCourseScope(branch, curricularCourse, curricularSemester, infoCurricularCourseScope.getBeginDate(),
										infoCurricularCourseScope.getEndDate(), infoCurricularCourseScope.getAnotation());
			
        } catch (RuntimeException e) {
            throw new ExistingServiceException("O �mbito pertencente ao ramo " + branch.getCode() + ", no "
                    + curricularSemester.getCurricularYear().getYear() + "� ano,  "
                    + curricularSemester.getSemester() + "� semestre");
        }
    }
}