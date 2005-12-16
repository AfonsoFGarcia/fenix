/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationUtils;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * Base class for authorization issues on credits information edition done by
 * department members.
 * 
 * @author jpvl
 */
public abstract class AbstractTeacherDepartmentAuthorization extends Filtro {

    public void execute(ServiceRequest serviceRequest, ServiceResponse serviceResponse) throws Exception {
        IUserView requester = (IUserView) serviceRequest.getRequester();
        if ((requester == null)
                || !AuthorizationUtils.containsRole(requester.getRoles(),
                        RoleType.DEPARTMENT_CREDITS_MANAGER)) {
            throw new NotAuthorizedException();
        }

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        
        Integer teacherId = getTeacherId(serviceRequest.getServiceParameters().parametersArray(), sp);
        if (teacherId != null) {

            IPessoaPersistente personDAO = sp.getIPessoaPersistente();
            IPerson requesterPerson = personDAO.lerPessoaPorUsername(requester.getUtilizador());
            IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();

            ITeacher teacher = (ITeacher) teacherDAO.readByOID(Teacher.class, teacherId);

            IDepartment teacherDepartment = teacher.getCurrentWorkingDepartment();

            List departmentsWithAccessGranted = requesterPerson.getManageableDepartmentCredits();

            if (!departmentsWithAccessGranted.contains(teacherDepartment)) {
                throw new NotAuthorizedException();
            }
        }

    }

    protected abstract Integer getTeacherId(Object[] arguments, ISuportePersistente sp)
            throws FenixServiceException, ExcepcaoPersistencia;
}