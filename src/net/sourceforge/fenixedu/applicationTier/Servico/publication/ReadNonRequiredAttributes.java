/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.publication.IAttribute;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author TJBF
 * @author PFON
 *  
 */
public class ReadNonRequiredAttributes implements IServico {
    private static ReadNonRequiredAttributes service = new ReadNonRequiredAttributes();

    /**
     *  
     */
    private ReadNonRequiredAttributes() {

    }

    public static ReadNonRequiredAttributes getService() {

        return service;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadNonRequiredAttributes";
    }

    public List run(String user, int publicationTypeId) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentPublicationType persistentPublicationType = persistentSuport
                    .getIPersistentPublicationType();
            PublicationType publicationType = (PublicationType) persistentPublicationType.readByOID(
                    PublicationType.class, new Integer(publicationTypeId));

            List nonRequiredAttributeList = publicationType.getNonRequiredAttributes();

            List result = (List) CollectionUtils.collect(nonRequiredAttributeList, new Transformer() {
                public Object transform(Object o) {
                    IAttribute publicationAttribute = (IAttribute) o;
                    return Cloner.copyIAttribute2InfoAttribute(publicationAttribute);
                }
            });

            return result;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}