/*
 * Created on 13/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.student;

import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author Luis Cruz
 */
public class StudentIsUserFilter extends Filtro {

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest serviceRequest, ServiceResponse serviceResponse)
            throws FilterException, Exception {
        Integer studentId = (Integer) serviceRequest.getArguments()[0];
        IUserView userView = getRemoteUser(serviceRequest);

        ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();

        IStudent student = (IStudent) persistentSupport.getIPersistentStudent().readByOID(Student.class,
                studentId);
        if (student == null || userView == null
                || !userView.getUtilizador().equals(student.getPerson().getUsername())) {
            throw new NotAuthorizedFilterException();
        }

    }

}