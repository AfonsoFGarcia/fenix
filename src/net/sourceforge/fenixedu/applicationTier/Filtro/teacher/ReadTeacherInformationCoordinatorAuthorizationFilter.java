/*
 * Created on Dec 16, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationUtils;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadTeacherInformationCoordinatorAuthorizationFilter extends AuthorizationByRoleFilter {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest arg0, ServiceResponse arg1) throws FilterException, Exception {
        IUserView id = (IUserView) arg0.getRequester();
        Object[] arguments = arg0.getArguments();
        if (((id != null && id.getRoles() != null && !AuthorizationUtils.containsRole(id.getRoles(),
                getRoleType())))
                || (id == null)
                || (id.getRoles() == null)
                || !verifyCondition(id, (String) arguments[0])) {
            throw new NotAuthorizedFilterException();
        }
    }

    protected boolean verifyCondition(IUserView id, String user) {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
            IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();

            Teacher teacher = persistentTeacher.readTeacherByUsername(user);
            Teacher coordinator = persistentTeacher.readTeacherByUsername(id.getUtilizador());

            List<ExecutionDegree> executionDegrees = persistentExecutionDegree
                    .readByTeacher(coordinator.getIdInternal());
            List<Integer> degreeCurricularPlanIDs = getDegreeCurricularPlanIDs(executionDegrees);
            Integer executionYearID = (!degreeCurricularPlanIDs.isEmpty()) ? executionDegrees
                    .get(0).getExecutionYear().getIdInternal()
                    : null;

            List professorships = persistentProfessorship.readByDegreeCurricularPlansAndExecutionYear(
                    degreeCurricularPlanIDs, executionYearID);
            Iterator iter = professorships.iterator();
            while (iter.hasNext()) {
                Professorship professorship = (Professorship) iter.next();
                if (professorship.getTeacher().equals(teacher))
                    return true;
            }
            return false;
        } catch (ExcepcaoPersistencia e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private List<Integer> getDegreeCurricularPlanIDs(final List<ExecutionDegree> executionDegrees) {
        final List<Integer> result = new ArrayList<Integer>();
        for (final ExecutionDegree executionDegree : executionDegrees) {
            result.add(executionDegree.getDegreeCurricularPlan().getIdInternal());
        }
        return result;
    }
}