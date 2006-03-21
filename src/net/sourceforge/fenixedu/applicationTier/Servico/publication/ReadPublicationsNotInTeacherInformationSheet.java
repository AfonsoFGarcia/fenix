package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoSitePublications;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.research.result.Authorship;
import net.sourceforge.fenixedu.domain.research.result.Publication;
import net.sourceforge.fenixedu.domain.research.result.PublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;


public class ReadPublicationsNotInTeacherInformationSheet extends Service {

    public SiteView run(String user) throws ExcepcaoPersistencia {
        InfoSitePublications infoSitePublications = new InfoSitePublications();
        
        Teacher teacher = Teacher.readTeacherByUsername(user);

        List<Publication> publicationsInTeacherSheet = new ArrayList<Publication>(teacher.getTeacherPublicationsCount());
        for(PublicationTeacher publicationTeacher : teacher.getTeacherPublications()) {
            publicationsInTeacherSheet.add(publicationTeacher.getPublication());
        }
        
        List<InfoPublication> infoPublications = new ArrayList<InfoPublication>();
        for(Authorship authorship : teacher.getPerson().getPersonAuthorshipsWithPublications()) {
            Publication publication = (Publication)authorship.getResult();
            if (!publicationsInTeacherSheet.contains(publication)) {
                infoPublications.add(InfoPublication.newInfoFromDomain(publication));
            }
        }
        
        InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher); 
        infoSitePublications.setInfoTeacher(infoTeacher);
        infoSitePublications.setInfoPublications(infoPublications);

        return new SiteView(infoSitePublications);
    }
}