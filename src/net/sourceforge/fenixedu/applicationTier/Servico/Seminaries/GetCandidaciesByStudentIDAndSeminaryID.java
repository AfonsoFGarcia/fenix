/*
 * Created on 5/Ago/2003, 19:44:39
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacy;
import net.sourceforge.fenixedu.domain.Seminaries.ICandidacy;
import net.sourceforge.fenixedu.domain.Seminaries.ISeminary;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCandidacy;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 5/Ago/2003, 19:44:39
 *  
 */
public class GetCandidaciesByStudentIDAndSeminaryID implements IService {

    public GetCandidaciesByStudentIDAndSeminaryID() {
    }

    public List run(Integer studentID, Integer seminaryID) throws BDException {
        List candidaciesInfo = new LinkedList();
        try {
            ISuportePersistente persistenceSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentSeminaryCandidacy persistentSeminaryCandidacy = persistenceSupport
                    .getIPersistentSeminaryCandidacy();
            List candidacies = persistentSeminaryCandidacy.readByStudentIDAndSeminaryID(studentID,
                    seminaryID);
            for (Iterator iterator = candidacies.iterator(); iterator.hasNext();) {
                ICandidacy candidacy = (ICandidacy) iterator.next();
                InfoCandidacy infoCandidacy = InfoCandidacy.newInfoFromDomain(candidacy);
                ISeminary seminary = candidacy.getSeminary();
                infoCandidacy.setSeminaryName(seminary.getName());
                candidaciesInfo.add(infoCandidacy);
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new BDException(
                    "Got an error while trying to retrieve multiple candidacies from the database", ex);
        }
        return candidaciesInfo;
    }
}