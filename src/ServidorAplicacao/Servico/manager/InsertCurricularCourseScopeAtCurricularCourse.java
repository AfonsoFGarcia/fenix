/*
 * Created on 21/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourseScope;
import Dominio.Branch;
import Dominio.CurricularCourse;
import Dominio.CurricularCourseScope;
import Dominio.CurricularSemester;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentCurricularSemester;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */
public class InsertCurricularCourseScopeAtCurricularCourse implements IService {

    public InsertCurricularCourseScopeAtCurricularCourse() {
    }

    public void run(InfoCurricularCourseScope infoCurricularCourseScope)
            throws FenixServiceException {
        IBranch branch = null;
        ICurricularSemester curricularSemester = null;
        ICurricularCourseScope curricularCourseScope = new CurricularCourseScope();
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB
                    .getInstance();
            IPersistentCurricularCourseScope persistentCurricularCourseScope = persistentSuport
                    .getIPersistentCurricularCourseScope();
            IPersistentCurricularSemester persistentCurricularSemester = persistentSuport
                    .getIPersistentCurricularSemester();
            IPersistentCurricularCourse persistentCurricularCourse = persistentSuport
                    .getIPersistentCurricularCourse();
            IPersistentBranch persistentBranch = persistentSuport
                    .getIPersistentBranch();

            curricularSemester = (ICurricularSemester) persistentCurricularSemester
                    .readByOID(CurricularSemester.class,
                            infoCurricularCourseScope
                                    .getInfoCurricularSemester()
                                    .getIdInternal());
            if (curricularSemester == null) {
                throw new NonExistingServiceException(
                        "message.non.existing.curricular.semester", null);
            }
            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class,
                            infoCurricularCourseScope.getInfoCurricularCourse()
                                    .getIdInternal());
            if (curricularCourse == null)
                throw new NonExistingServiceException(
                        "message.nonExistingCurricularCourse", null);

            branch = (IBranch) persistentBranch.readByOID(Branch.class,
                    infoCurricularCourseScope.getInfoBranch().getIdInternal());
            if (branch == null)
                throw new NonExistingServiceException(
                        "message.non.existing.branch", null);
            // check that there isn't another scope active with the
            // same
            // curricular course, branch and semester
            ICurricularCourseScope curricularCourseScopeFromDB = null;
            curricularCourseScopeFromDB = persistentCurricularCourseScope
                    .readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranchAndEndDate(
                            curricularCourse, curricularSemester, branch, null);
            if (curricularCourseScopeFromDB != null) {
                throw new ExistingPersistentException();
            }
            persistentCurricularCourseScope
                    .simpleLockWrite(curricularCourseScope);
            curricularCourseScope.setBranch(branch);
            curricularCourseScope.setCurricularCourse(curricularCourse);
            curricularCourseScope.setCurricularSemester(curricularSemester);
            curricularCourseScope.setBeginDate(infoCurricularCourseScope
                    .getBeginDate());
            curricularCourseScope.setEndDate(null);
        } catch (ExistingPersistentException e) {
            //FIXME: remove use of portuguese
            throw new ExistingServiceException("O �mbito com ramo "
                    + branch.getCode() + ", do "
                    + curricularSemester.getCurricularYear().getYear()
                    + "� ano, " + curricularSemester.getSemester()
                    + "� semestre");
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}