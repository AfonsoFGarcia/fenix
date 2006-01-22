/*
 * Created on Nov 15, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.student;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AccessControlFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationUtils;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutor;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author Andr� Fernandes / Jo�o Brito
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StudentTutorAuthorizationFilter extends AccessControlFilter
{
    
        public StudentTutorAuthorizationFilter()
        {    
        }
        
        /* (non-Javadoc)
         * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest, pt.utl.ist.berserk.ServiceResponse)
         */
        public void execute(ServiceRequest request, ServiceResponse response)
                throws FilterException, Exception
        {
            IUserView id = (IUserView) request.getRequester();
            String messageException;

            if (id == null || id.getRoles() == null
                    || !AuthorizationUtils.containsRole(id.getRoles(), getRoleType()))
            {
                throw new NotAuthorizedFilterException();
            }
            messageException = studentTutor(id, request.getServiceParameters().parametersArray());
            if (messageException != null)
                throw new NotAuthorizedFilterException(messageException);

        }

        /*
        (String username, StudentCurricularPlanIDDomainType curricularPlanID, EnrollmentStateSelectionType criterio)
        */
        //devolve null se tudo OK
        // noAuthorization se algum prob
        private String studentTutor(IUserView id, Object[] arguments)
    	{
    	    try 
    	    {
    	        String username = (String)arguments[0];

    	        Teacher teacher = persistentSupport.getIPersistentTeacher().readTeacherByUsername(id.getUtilizador());
    	        
    	        Student student = persistentSupport.getIPersistentStudent().readByUsername(username);
    	        if (student == null) 
		        {
		            return "noAuthorization";
		        }
    	        
    	        List allStudents = student.getPerson().getStudents();
    	        if (allStudents == null || allStudents.isEmpty()) 
		        {
		            return "noAuthorization";
		        }
    	        
		        if (teacher == null) 
		        {
		            return "noAuthorization";
		        }

		        if (!verifyStudentTutor(teacher, allStudents)) {
		            return "error.enrollment.notStudentTutor+" + student.getNumber().toString();
		        }
    	    }
    	    catch (Exception exception)
    	    {
    	        exception.printStackTrace();
    	        return "noAuthorization";
    	    }
    	    return null;
    	}
    	
        /*
         * devolve true se teacher for tutor de algum dos students da lista
         */
        private boolean verifyStudentTutor(Teacher teacher, List students) throws ExcepcaoPersistencia
        {
            Student student;
            Tutor tutor;
            
            for (Iterator it = students.iterator(); it.hasNext();)
            {
                student = (Student)it.next();
                tutor = persistentSupport.getIPersistentTutor().readTutorByTeacherAndStudent(teacher, student);

                if (tutor != null)
                    return true;
            }
            return false;
        }
        
    	protected RoleType getRoleType()
    	{
    	    return RoleType.TEACHER;
    	}

}
