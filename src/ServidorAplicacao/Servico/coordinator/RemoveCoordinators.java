/*
 * Created on 30/Oct/2003
 *
 */
package ServidorAplicacao.Servico.coordinator;

import java.util.Iterator;
import java.util.List;

import Dominio.Coordinator;
import Dominio.ICoordinator;
import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.PersonRole;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCoordinator;
import ServidorPersistente.IPersistentPersonRole;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author Jo�o Mota
 * 30/Oct/2003
 *
 */
public class RemoveCoordinators implements IServico {

	private static RemoveCoordinators service = new RemoveCoordinators();

	public static RemoveCoordinators getService() {

		return service;
	}

	private RemoveCoordinators() {

	}

	public final String getNome() {

		return "RemoveCoordinators";
	}

	public Boolean run(Integer executionDegreeId,List coordinatorsIds) throws FenixServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();
			IPersistentPersonRole persistentPersonRole = sp.getIPersistentPersonRole();
			Iterator iter = coordinatorsIds.iterator();
			while (iter.hasNext()) {
				ICoordinator coordinator = new Coordinator((Integer) iter.next());
				coordinator = (ICoordinator) persistentCoordinator.readByOId(coordinator, true);
				if (coordinator != null) {
					IPessoa person = coordinator.getTeacher().getPerson();
					IPersonRole personRole =
						persistentPersonRole.readByPersonAndRole(
							person,
							sp.getIPersistentRole().readByRoleType(RoleType.COORDINATOR));
					if (personRole != null) {
						persistentPersonRole.deleteByOID(
							PersonRole.class,
							personRole.getIdInternal());
					}
					persistentCoordinator.deleteByOID(
						Coordinator.class,
						coordinator.getIdInternal());
				}
			}

			return new Boolean(true);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

	}
}
