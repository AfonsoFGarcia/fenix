/*
 * Created on Nov 12, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.student;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AccessControlFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationUtils;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.RoleType;
import net.sourceforge.fenixedu.util.StudentCurricularPlanIDDomainType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author Andr� Fernandes / Jo�o Brito
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StudentCurricularPlanOwnerAuthorizationFilter extends
        AccessControlFilter
{
    public StudentCurricularPlanOwnerAuthorizationFilter()
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
        messageException = curricularPlanOwner(id, request.getServiceParameters().parametersArray());
        if (messageException != null)
            throw new NotAuthorizedFilterException(messageException);
    }

    /*
    (String username, StudentCurricularPlanIDDomainType curricularPlanID, EnrollmentStateSelectionType criterio)
    */
    //devolve null se tudo OK
    // noAuthorization se algum prob
    private String curricularPlanOwner(IUserView id, Object[] arguments)
	{
	    IStudentCurricularPlan studentCurricularPlan;
	    StudentCurricularPlanIDDomainType scpId = (StudentCurricularPlanIDDomainType)arguments[1];
	    Integer studentCurricularPlanID;
	    
	    
	    // permitir ler se o ID nao tiver sido especificado
	    if (scpId.isAll() || scpId.isNewest())
	        return null;
	    studentCurricularPlanID = scpId.getId();
	    
	    
	    try {
	        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
	        IPersistentStudentCurricularPlan persistentStudentCurricularPlan = sp
	                .getIStudentCurricularPlanPersistente();
	
	        studentCurricularPlan = (IStudentCurricularPlan) persistentStudentCurricularPlan.readByOID(
	                StudentCurricularPlan.class, studentCurricularPlanID);
	        if (studentCurricularPlan == null)
	        {
	            return "noAuthorization";
	        }
	        if (!studentCurricularPlan.getStudent().getPerson().getUsername().equals(id.getUtilizador())) 
	        {
	            return "noAuthorization";
	        }
	    }
	    catch (Exception exception)
	    {
	        exception.printStackTrace();
	        return "noAuthorization";
	    }
	    return null;
	}
	
	protected RoleType getRoleType()
	{
	    return RoleType.STUDENT;
	}
}