/*
 * Created on Dec 21, 2003
 *  
 */
package ServidorAplicacao.Filtro.coordinator;

import java.util.List;

import Dominio.ExecutionDegree;
import Dominio.IExecutionDegree;
import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.framework.DomainObjectAuthorizationFilter;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ExecutionDegreeCoordinatorAuthorizationFilter extends DomainObjectAuthorizationFilter {
    /**
     *  
     */
    public ExecutionDegreeCoordinatorAuthorizationFilter() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.framework.DomainObjectAuthorizationFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.framework.DomainObjectAuthorizationFilter#verifyCondition(ServidorAplicacao.IUserView,
     *      java.lang.Integer)
     */
    protected boolean verifyCondition(IUserView id, Integer objectId) {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

            IExecutionDegree executionDegree = (IExecutionDegree) persistentExecutionDegree.readByOID(
                    ExecutionDegree.class, objectId);
            ITeacher coordinator = persistentTeacher.readTeacherByUsername(id.getUtilizador());
            List executionDegrees = persistentExecutionDegree.readByTeacher(coordinator);

            return executionDegrees.contains(executionDegree);
        } catch (ExcepcaoPersistencia e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}