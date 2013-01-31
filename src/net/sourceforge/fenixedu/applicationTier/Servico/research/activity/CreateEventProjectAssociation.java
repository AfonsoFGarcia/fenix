package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.EventProjectAssociationFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.EventProjectAssociationSimpleCreationBean;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.project.Project;
import net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateEventProjectAssociation extends FenixService {

	/**
	 * Service responsible for creating an association between a project and an
	 * event
	 * 
	 * @param bean
	 *            - Bean responsible for carrying the information from the
	 *            presentation to the services layer
	 * @param eventId
	 *            - the identifier of the ResearchEvent for whom the association
	 *            is being created
	 * @return the newly created ProjectEventAssociation
	 * @throws ExcepcaoPersistencia
	 * @throws FenixServiceException
	 *             - In case the project doesn't exist.
	 */
	@Checked("ResultPredicates.author")
	@Service
	public static ProjectEventAssociation run(EventProjectAssociationSimpleCreationBean bean, Integer eventId)
			throws FenixServiceException {
		ProjectEventAssociation association = null;
		final EventEdition event = rootDomainObject.readEventEditionByOID(eventId);
		if (event == null) {
			throw new FenixServiceException();
		}

		association = new ProjectEventAssociation();
		association.setEventEdition(event);
		association.setProject(bean.getProject());
		association.setRole(bean.getRole());
		return association;
	}

	/**
	 * Service responsible for creating an association between a project and an
	 * event
	 * 
	 * @param bean
	 *            - Bean responsible for carrying the information from the
	 *            presentation to the services layer
	 * @param eventId
	 *            - the identifier of the ResearchEvent for whom the association
	 *            is being created
	 * @return the newly created ProjectEventAssociation
	 * @throws ExcepcaoPersistencia
	 * @throws FenixServiceException
	 *             - In case the project doesn't exist.
	 */
	@Checked("ResultPredicates.author")
	@Service
	public static ProjectEventAssociation run(EventProjectAssociationFullCreationBean bean, Integer eventId)
			throws FenixServiceException {
		final ProjectEventAssociation association;

		final EventEdition event = rootDomainObject.readEventEditionByOID(eventId);
		if (event == null) {
			throw new FenixServiceException();
		}

		final Project project = new Project(bean.getProjectTitle(), bean.getProjectType());

		// Insert this line when inner enums are supported by the domain factory
		// participation = new ProjectParticipation(project,
		// externalPerson.getPerson(), bean.getRole());
		association = new ProjectEventAssociation();
		association.setProject(project);
		association.setEventEdition(event);
		association.setRole(bean.getRole());

		return association;
	}
}