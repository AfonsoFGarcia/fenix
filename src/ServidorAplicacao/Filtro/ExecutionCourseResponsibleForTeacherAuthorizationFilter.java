/*
 * Created on 19/Mai/2003
 *
 * 
 */
package ServidorAplicacao.Filtro;

import java.util.Collection;
import java.util.Iterator;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoRole;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.NotAuthorizedException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author Jo�o Mota
 *
 */
public class ExecutionCourseResponsibleForTeacherAuthorizationFilter
	extends AuthorizationByRoleFilter {


		public final static ExecutionCourseResponsibleForTeacherAuthorizationFilter instance = new ExecutionCourseResponsibleForTeacherAuthorizationFilter();

		/**
		 * The singleton access method of this class.
		 *
		 * @return Returns the instance of this class responsible for the
		 * authorization access to services.
		 **/
		public static Filtro getInstance() {
		  return instance;
		}
	/* (non-Javadoc)
	 * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
	 */
	protected RoleType getRoleType() {
		return RoleType.TEACHER;
	}

	public void preFiltragem(
				IUserView id,
				IServico servico,
				Object[] argumentos)
				throws Exception {
				if (((id != null
					&& id.getRoles() != null
					&& !containsRole(id.getRoles())
					&& !isResponsibleForExecutionCourse(id,argumentos)))
					|| (id == null)
					|| (id.getRoles() == null)) {
					throw new NotAuthorizedException();
				}

			}


		/**
			 * @param collection
			 * @return boolean
			 */
			private boolean containsRole(Collection roles) {
				Iterator rolesIterator = roles.iterator();
				while (rolesIterator.hasNext()) {
					InfoRole infoRole = (InfoRole) rolesIterator.next();
					if (infoRole.getRoleType().equals(getRoleType()))
						return true;	
				}
				return false;
			}
	/**
	 * @param id
	 * @param argumentos
	 * @return
	 */
	private boolean isResponsibleForExecutionCourse(IUserView id, Object[] argumentos) {
		InfoExecutionCourse infoExecutionCourse =(InfoExecutionCourse) argumentos[0];
		IDisciplinaExecucao executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
		ISuportePersistente sp;
		IResponsibleFor responsibleFor =null;
		try {
			sp = SuportePersistenteOJB.getInstance();
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			ITeacher teacher=persistentTeacher.readTeacherByUsernamePB(id.getUtilizador());
			IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
			responsibleFor =persistentResponsibleFor.readByTeacherAndExecutionCoursePB(teacher,executionCourse);
			
		} catch (ExcepcaoPersistencia e) {
			return false;
		}
		if (responsibleFor==null){
			return false;
		}
		else {
			return true;
		}
		
	}

}
