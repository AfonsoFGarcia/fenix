/*
 * Created on Jul 29, 2004
 *
 */
package ServidorAplicacao.Servico.manager.curricularCourseGroupsManagement;

import DataBeans.InfoCurricularCourseGroup;
import DataBeans.InfoCurricularCourseGroupWithInfoBranch;
import Dominio.CurricularCourseGroup;
import Dominio.ICurricularCourseGroup;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseGroup;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Jo�o Mota
 *  
 */
public class ReadCurricularCourseGroup implements IService {

    /**
     *  
     */
    public ReadCurricularCourseGroup() {
    }

    public InfoCurricularCourseGroup run(Integer groupId) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentCurricularCourseGroup persistentCurricularCourseGroup = persistentSuport
                    .getIPersistentCurricularCourseGroup();
            ICurricularCourseGroup group = (ICurricularCourseGroup) persistentCurricularCourseGroup
                    .readByOID(CurricularCourseGroup.class, groupId);
            return InfoCurricularCourseGroupWithInfoBranch.newInfoFromDomain(group);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}