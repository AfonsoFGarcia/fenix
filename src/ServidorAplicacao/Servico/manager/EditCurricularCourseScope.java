/*
 * Created on 21/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourseScope;
import Dominio.Branch;
import Dominio.CurricularCourseScope;
import Dominio.CurricularSemester;
import Dominio.IBranch;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentCurricularSemester;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */
public class EditCurricularCourseScope implements IService {

    public void run(InfoCurricularCourseScope newInfoCurricularCourseScope) throws FenixServiceException {

        ICurricularCourseScope oldCurricularCourseScope = null;
        ICurricularSemester newCurricularSemester = null;
        IBranch newBranch = null;

        try {
            ISuportePersistente ps = SuportePersistenteOJB.getInstance();
            IPersistentBranch persistentBranch = ps.getIPersistentBranch();
            IPersistentCurricularSemester persistentCurricularSemester = ps
                    .getIPersistentCurricularSemester();
            IPersistentCurricularCourseScope persistentCurricularCourseScope = ps
                    .getIPersistentCurricularCourseScope();

            Integer branchId = newInfoCurricularCourseScope.getInfoBranch().getIdInternal();

            newBranch = (IBranch) persistentBranch.readByOID(Branch.class, branchId);

            if (newBranch == null) {
                throw new NonExistingServiceException("message.non.existing.branch", null);
            }

            Integer curricularSemesterId = newInfoCurricularCourseScope.getInfoCurricularSemester()
                    .getIdInternal();

            newCurricularSemester = (ICurricularSemester) persistentCurricularSemester.readByOID(
                    CurricularSemester.class, curricularSemesterId);

            if (newCurricularSemester == null) {
                throw new NonExistingServiceException("message.non.existing.curricular.semester", null);
            }

            oldCurricularCourseScope = (ICurricularCourseScope) persistentCurricularCourseScope
                    .readByOID(CurricularCourseScope.class,
                            newInfoCurricularCourseScope.getIdInternal(), true);

            if (oldCurricularCourseScope == null) {
                throw new NonExistingServiceException("message.non.existing.curricular.course.scope",
                        null);
            }

            oldCurricularCourseScope.setBranch(newBranch);
            //it already includes the curricular year
            oldCurricularCourseScope.setCurricularSemester(newCurricularSemester);
            oldCurricularCourseScope.setBeginDate(newInfoCurricularCourseScope.getBeginDate());
            oldCurricularCourseScope.setEndDate(null);

        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException("O �mbito com ramo " + newBranch.getCode() + ", do "
                    + newCurricularSemester.getCurricularYear().getYear() + "� ano, "
                    + newCurricularSemester.getSemester() + "� semestre");
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}