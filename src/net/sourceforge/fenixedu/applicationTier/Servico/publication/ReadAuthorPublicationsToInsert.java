/*
 * Created on Jun 10, 2004
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.constants.publication.PublicationConstants;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoSitePublications;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.publication.Author;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author TJBF & PFON
 *  
 */
public class ReadAuthorPublicationsToInsert implements IService {

    /**
     *  
     */
    public ReadAuthorPublicationsToInsert() {

    }

    /**
     * Executes the service.
     */
    public SiteView run(String user) throws FenixServiceException {
        try {
            InfoSitePublications infoSitePublications = new InfoSitePublications();

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            
            ITeacher teacher = persistentTeacher.readTeacherByUsername(user);
            InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher); 
                //Cloner.copyITeacher2InfoTeacher(teacher);
            infoSitePublications.setInfoTeacher(infoTeacher);

            
            List infoPublications = getInfoPublications(sp, teacher);

            List infoPublicationsDidactic = getInfoPublicationsType(infoPublications,
                    PublicationConstants.DIDATIC);

            List infoPublicationsCientific = getInfoPublicationsType(infoPublications,
                    PublicationConstants.CIENTIFIC);

            infoSitePublications.setInfoDidaticPublications(infoPublicationsDidactic);

            infoSitePublications.setInfoCientificPublications(infoPublicationsCientific);

            return new SiteView(infoSitePublications);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    private List getInfoPublications(ISuportePersistente sp, ITeacher teacher)
            throws ExcepcaoPersistencia {

        IPerson pessoa = teacher.getPerson();
        Integer keyPerson = pessoa.getIdInternal();

        IPersistentAuthor persistentAuthor = sp.getIPersistentAuthor();
        Author author = persistentAuthor.readAuthorByKeyPerson(keyPerson);

        List authorPublications = new ArrayList();
        List infoAuthorPublications = new ArrayList();
        List newAuthorPublications = new ArrayList();
        if (author == null) {
            Author newAuthor = new Author();
            //newAuthor.setKeyPerson(keyPerson);
            newAuthor.setPerson(pessoa);
            persistentAuthor.lockWrite(newAuthor);

        } else {
            authorPublications = author.getPublications();
        }

        List publicationsInTeacherSList = teacher.getTeacherPublications();

        if (authorPublications != null || authorPublications.size() != PublicationConstants.ZERO_VALUE) {
            if ((publicationsInTeacherSList != null) || (publicationsInTeacherSList.size() != 0)) {

                newAuthorPublications = deletePublicationsTeacherInAuthorPublications(
                        authorPublications, publicationsInTeacherSList);
            }
            infoAuthorPublications = (List) CollectionUtils.collect(newAuthorPublications,
                    new Transformer() {
                        public Object transform(Object o) {
                            IPublication publication = (IPublication) o;
                            IPublication publication2 = publication;
                            publication2.setPublicationString(publication.toString());
                            return InfoPublication.newInfoFromDomain(publication2);
                            //return Cloner.copyIPublication2InfoPublication(publication2);
                        }
                    });
        }

        return infoAuthorPublications;
    }

    List getInfoPublicationsType(List infoPublications, Integer typePublication) {

        List newInfoPublications = new ArrayList();

        if (infoPublications != null || infoPublications.size() != PublicationConstants.ZERO_VALUE) {
            Iterator iterator = infoPublications.iterator();
            while (iterator.hasNext()) {
                InfoPublication infoPublication = (InfoPublication) iterator.next();
                if (infoPublication.getDidatic().intValue() == typePublication.intValue()) {
                    newInfoPublications.add(infoPublication);
                }
            }
        }
        return newInfoPublications;
    }

    public List deletePublicationsTeacherInAuthorPublications(List publicationsAuthor,
            List publicationsTeacher) {

        List newPublicationsAuthor = new ArrayList();
        Iterator iteratorTeachers = publicationsTeacher.iterator();
        Iterator iteratorAuthors = publicationsAuthor.iterator();
        Boolean contains = Boolean.FALSE;

        while (iteratorAuthors.hasNext()) {
            IPublication publicationAuthor = (IPublication) iteratorAuthors.next();

            contains = Boolean.FALSE;
            iteratorTeachers = publicationsTeacher.iterator();
            while (iteratorTeachers.hasNext()) {
                IPublication publicationTeacher = (IPublication) iteratorTeachers.next();

                
                if (publicationTeacher.getIdInternal().intValue() == publicationAuthor.getIdInternal()
                        .intValue()) {
                    contains = Boolean.TRUE;
                }
            }
            
            if (contains.booleanValue() == Boolean.FALSE.booleanValue()) {
                newPublicationsAuthor.add(publicationAuthor);
            }

        }

        return newPublicationsAuthor;
    }

}