package ServidorAplicacao.Servico.manager;

import java.util.Calendar;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourseScope;
import Dominio.CurricularCourseScope;
import Dominio.ICurricularCourseScope;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quit�rio 28/10/2003
 *  
 */
public class EndCurricularCourseScope implements IService {

    public void run(InfoCurricularCourseScope newInfoCurricularCourseScope) throws FenixServiceException {

        ICurricularCourseScope oldCurricularCourseScope = null;
        try {
            ISuportePersistente ps = SuportePersistenteOJB.getInstance();
            IPersistentCurricularCourseScope persistentCurricularCourseScope = ps
                    .getIPersistentCurricularCourseScope();

            if (!newInfoCurricularCourseScope.getEndDate().after(
                    newInfoCurricularCourseScope.getBeginDate())) {
                throw new InvalidArgumentsServiceException();
            }

            if (newInfoCurricularCourseScope.getEndDate().after(Calendar.getInstance())) {
                throw new InvalidSituationServiceException();
            }

            oldCurricularCourseScope = (ICurricularCourseScope) persistentCurricularCourseScope
                    .readByOID(CurricularCourseScope.class,
                            newInfoCurricularCourseScope.getIdInternal(), true);

            if (oldCurricularCourseScope == null) {
                throw new NonExistingServiceException("message.non.existing.curricular.course.scope",
                        null);
            }
            oldCurricularCourseScope.setEndDate(newInfoCurricularCourseScope.getEndDate());

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}