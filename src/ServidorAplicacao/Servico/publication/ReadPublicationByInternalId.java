/*
 * Created on 25-Oct-2004
 *
 * @author Carlos Pereira & Francisco Passos
 * 
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ServidorAplicacao.Servico.publication;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoPerson;
import DataBeans.publication.InfoAuthor;
import DataBeans.publication.InfoPublication;
import DataBeans.publication.InfoPublicationType;
import Dominio.IPerson;
import Dominio.publication.IAuthor;
import Dominio.publication.IPublication;
import Dominio.publication.IPublicationAuthor;
import Dominio.publication.IPublicationType;
import Dominio.publication.Publication;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.publication.IPersistentPublication;

/**
 * @author Carlos Pereira & Francisco Passos
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ReadPublicationByInternalId implements IService {

    public InfoPublication run(Integer internalId, IUserView userView)
    		throws FenixServiceException, ExcepcaoPersistencia {
        InfoPublication infoPublication;
        ISuportePersistente sp;

        sp = SuportePersistenteOJB.getInstance();

        IPersistentPublication persistentPublication = sp.getIPersistentPublication();

        IPublication publication = (IPublication) persistentPublication.readByOID(Publication.class,
                internalId);
        infoPublication = InfoPublication.newInfoFromDomain(publication);

        ReadPublicationType readPublicationType = new ReadPublicationType();
        IPublicationType publicationType = readPublicationType.run(publication.getKeyPublicationType());

        final List publicationAuthors = publication.getPublicationAuthors();
        final Map personsMap = generatePersonsMap(publicationAuthors);

        if (publicationType != null) {
            infoPublication.setPublicationType(publicationType.getPublicationType());
            infoPublication.setInfoPublicationType(InfoPublicationType
                    .newInfoFromDomain(publicationType));
            fillInPersonInformation(infoPublication, personsMap);
        }

        return infoPublication;
    }

    /**
     * @param infoPublication
     * @param personsMap
     */
    protected void fillInPersonInformation(InfoPublication infoPublication, final Map personsMap) {
        final List infoAuthors = infoPublication.getInfoPublicationAuthors();
        for (final Iterator iterator = infoAuthors.iterator(); iterator.hasNext();) {
            final InfoAuthor infoAuthor = (InfoAuthor) iterator.next();
            final Integer keyPerson = infoAuthor.getKeyPerson();
            if (keyPerson != null) {
                final IPerson person = (IPerson) personsMap.get(keyPerson);
                final InfoPerson infoPerson = new InfoPerson();
                infoPerson.setIdInternal(person.getIdInternal());
                infoPerson.setNome(person.getNome());
                infoAuthor.setInfoPessoa(infoPerson);
            }
        }
    }

    /**
     * @param publicationAuthors
     * @return
     */
    protected Map generatePersonsMap(final List publicationAuthors) {
        final Map personsMap = new HashMap(publicationAuthors.size());
        for (final Iterator iterator = publicationAuthors.iterator(); iterator.hasNext();) {
            final IPublicationAuthor publicationAuthor = (IPublicationAuthor) iterator.next();
            final IAuthor author = publicationAuthor.getAuthor();
            final IPerson person = author.getPerson();
            if (person != null) {
                personsMap.put(person.getIdInternal(), person);
            }
        }
        return personsMap;
    }

}