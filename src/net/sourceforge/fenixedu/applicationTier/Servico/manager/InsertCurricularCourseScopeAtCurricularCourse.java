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
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author lmac1
 */
public class InsertCurricularCourseScopeAtCurricularCourse extends Service {

    public void run(InfoCurricularCourseScope infoCurricularCourseScope) throws FenixServiceException, ExcepcaoPersistencia {
        Branch branch = null;
        CurricularSemester curricularSemester = null;
        try {
            curricularSemester = rootDomainObject.readCurricularSemesterByOID(infoCurricularCourseScope.getInfoCurricularSemester().getIdInternal());
            if (curricularSemester == null)
                throw new NonExistingServiceException("message.non.existing.curricular.semester", null);
         
			CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(infoCurricularCourseScope.getInfoCurricularCourse().getIdInternal());
            if (curricularCourse == null)
                throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);

            branch = rootDomainObject.readBranchByOID(infoCurricularCourseScope.getInfoBranch().getIdInternal());
            if (branch == null)
                throw new NonExistingServiceException("message.non.existing.branch", null);
			
			new CurricularCourseScope(branch, curricularCourse, curricularSemester, infoCurricularCourseScope.getBeginDate(),
										infoCurricularCourseScope.getEndDate(), infoCurricularCourseScope.getAnotation());
			
        } catch (RuntimeException e) {
            throw new ExistingServiceException("O �mbito pertencente ao ramo " + branch.getCode() + ", no "
                    + curricularSemester.getCurricularYear().getYear() + "� ano,  "
                    + curricularSemester.getSemester() + "� semestre");
        }
    }
    
}
