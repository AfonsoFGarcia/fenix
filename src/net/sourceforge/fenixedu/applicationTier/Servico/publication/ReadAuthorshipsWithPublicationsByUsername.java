package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoSitePublications;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.research.result.Authorship;
import net.sourceforge.fenixedu.domain.research.result.Publication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadAuthorshipsWithPublicationsByUsername extends Service {

	public SiteView run(String user) throws FenixServiceException, ExcepcaoPersistencia {
		Person person = Person.readPersonByUsername(user);
		Teacher teacher = Teacher.readTeacherByUsername(user);

		InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher);
		List<InfoPublication> infoPublications = new ArrayList<InfoPublication>();
		/*List<InfoPublication> infoPublications = new ArrayList<InfoPublication>(person
				.getPersonAuthorshipsCount());*/

		for (Authorship authorship : person.getPersonAuthorshipsWithPublications()) {
			infoPublications.add(InfoPublication.newInfoFromDomain((Publication)authorship.getResult()));
		}

		InfoSitePublications infoSitePublications = new InfoSitePublications();
		infoSitePublications.setInfoTeacher(infoTeacher);
		infoSitePublications.setInfoPublications(infoPublications);

		return new SiteView(infoSitePublications);
	}

}