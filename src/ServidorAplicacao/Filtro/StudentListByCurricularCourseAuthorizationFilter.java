package ServidorAplicacao.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

import DataBeans.InfoRole;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.ICursoExecucao;
import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class StudentListByCurricularCourseAuthorizationFilter extends AccessControlFilter
{

    /**
	 *  
	 */
    public StudentListByCurricularCourseAuthorizationFilter()
    {
        super();
    }

    /**
	 * @param collection
	 * @return boolean
	 */
    private boolean containsRole(Collection roles)
    {
        CollectionUtils.intersection(roles, getNeededRoles());

        if (roles.size() != 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    /*
	 * (non-Javadoc)
	 * 
	 * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
	 *          pt.utl.ist.berserk.ServiceResponse)
	 */
    public void execute(ServiceRequest request, ServiceResponse response)
            throws FilterException, Exception
    {
        IUserView id = (IUserView) request.getRequester();
        if ((id != null && id.getRoles() != null && !containsRole(id.getRoles()))
                || (id != null && id.getRoles() != null && !hasPrivilege(id, request.getArguments()))
                || (id == null) || (id.getRoles() == null))
        {
            throw new NotAuthorizedException();
        }
    }

    /**
	 * @return The Needed Roles to Execute The Service
	 */
    private Collection getNeededRoles()
    {
        List roles = new ArrayList();

        InfoRole infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.COORDINATOR);
        roles.add(infoRole);

        return roles;
    }

    private List getRoleList(List roles)
    {
        List result = new ArrayList();
        Iterator iterator = roles.iterator();
        while (iterator.hasNext())
        {
            result.add(((InfoRole) iterator.next()).getRoleType());
        }

        return result;
    }

    /**
	 * @param id
	 * @param argumentos
	 * @return
	 */
    private boolean hasPrivilege(IUserView id, Object[] arguments)
    {

        List roles = getRoleList((List) id.getRoles());
        CollectionUtils.intersection(roles, getNeededRoles());

        Integer curricularCourseID = (Integer) arguments[1];

        ICurricularCourse curricularCourse = null;

        // Read The DegreeCurricularPlan
        try
        {
            IPersistentCurricularCourse persistentCurricularCourse = SuportePersistenteOJB.getInstance()
                    .getIPersistentCurricularCourse();
            ICurricularCourse curricularCourseTemp = new CurricularCourse();
            curricularCourseTemp.setIdInternal(curricularCourseID);

            curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(
                    curricularCourseTemp, false);
        }
        catch (Exception e)
        {
            return false;
        }

        if (curricularCourse == null)
        {
            return false;
        }

        List roleTemp = new ArrayList();
        roleTemp.add(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        if (CollectionUtils.containsAny(roles, roleTemp))
        {
            if (curricularCourse.getDegreeCurricularPlan().getDegree().getTipoCurso()
                    .equals(TipoCurso.MESTRADO_OBJ))
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        roleTemp = new ArrayList();
        roleTemp.add(RoleType.COORDINATOR);
        if (CollectionUtils.containsAny(roles, roleTemp))
        {

            ITeacher teacher = null;
            // Read The ExecutionDegree
            try
            {
                ICursoExecucaoPersistente persistentExecutionDegree = SuportePersistenteOJB
                        .getInstance().getICursoExecucaoPersistente();

                List executionDegrees = persistentExecutionDegree.readByDegreeCurricularPlan(
                        curricularCourse.getDegreeCurricularPlan());
                if (executionDegrees == null)
                {
                    return false;
                }
                // IMPORTANT: It's assumed that the coordinator for a Degree is ALWAYS the same
                teacher = ((ICursoExecucao) executionDegrees.get(0)).getCoordinator();

                if (teacher == null)
                {
                    return false;
                }

                if (id.getUtilizador().equals(teacher.getPerson().getUsername()))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            catch (Exception e)
            {
                return false;
            }
        }
        return false;
    }
}