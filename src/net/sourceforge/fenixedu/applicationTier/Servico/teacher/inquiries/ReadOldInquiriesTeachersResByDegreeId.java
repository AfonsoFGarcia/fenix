/*
 * Created on Feb 4, 2005
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.inquiries;

import java.util.List;

import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesTeachersRes;

import net.sourceforge.fenixedu.commons.CollectionUtils;

/**
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class ReadOldInquiriesTeachersResByDegreeId implements IServico {

    private static ReadOldInquiriesTeachersResByDegreeId service = new ReadOldInquiriesTeachersResByDegreeId();
    
    private ReadOldInquiriesTeachersResByDegreeId() {
    }
    
    public String getNome() {
        return "ReadOldInquiriesTeachersResByDegreeId";
    }

    /**
     * @return Returns the service.
     */
    public static ReadOldInquiriesTeachersResByDegreeId getService() {
        return service;
    }
    
    public List run(Integer degreeId) throws FenixServiceException {
        List oldInquiriesTeachersResList = null;

        try {
            if (degreeId == null) {
                throw new FenixServiceException("nullDegreeId");
            }
            SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
            IPersistentOldInquiriesTeachersRes poits = sp.getIPersistentOldInquiriesTeachersRes();
        
            oldInquiriesTeachersResList = poits.readByDegreeId(degreeId);
            
            CollectionUtils.transform(oldInquiriesTeachersResList,new Transformer(){

                public Object transform(Object oldInquiriesTeachersRes) {
                    InfoOldInquiriesTeachersRes ioits = new InfoOldInquiriesTeachersRes();
                    try {
                        ioits.copyFromDomain((IOldInquiriesTeachersRes) oldInquiriesTeachersRes);

                    } catch (Exception ex) {
                    }
                                        
                    return ioits;
                }
             	});
                            
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }

        return oldInquiriesTeachersResList;
    }  
    
}
