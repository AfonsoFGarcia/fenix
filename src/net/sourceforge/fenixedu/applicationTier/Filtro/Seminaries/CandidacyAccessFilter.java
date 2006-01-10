/*
 * Created on 26/Ago/2003, 13:35:57
 * 
 * By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.Seminaries;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationUtils;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Seminaries.Candidacy;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCandidacy;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 26/Ago/2003, 13:35:57
 *  
 */
public class CandidacyAccessFilter extends Filtro {
    public CandidacyAccessFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws FilterException,
            Exception {
        IUserView id = getRemoteUser(request);
        Object[] argumentos = getServiceCallArguments(request);

        if ((!this.checkCandidacyOwnership(id, argumentos))
                && (!this.checkCoordinatorRole(id, argumentos)))
            throw new NotAuthorizedException();

    }

    boolean checkCoordinatorRole(IUserView id, Object[] arguments) throws Exception {
        boolean result = true;
        //        Collection roles = id.getRoles();
        //        Iterator iter = roles.iterator();
        //        while (iter.hasNext())
        //        {
        //            InfoRole role = (InfoRole) iter.next();
        //        }
        if (((id != null && id.getRoles() != null && !AuthorizationUtils.containsRole(id.getRoles(),
                getRoleType())))
                || (id == null) || (id.getRoles() == null)) {
            result = false;
        }
        return result;
    }

    boolean checkCandidacyOwnership(IUserView id, Object[] arguments) throws Exception {
        boolean result = true;
        Integer candidacyID = (Integer) arguments[0];
        ISuportePersistente persistenceSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentSeminaryCandidacy persistentCandidacy = persistenceSupport
                .getIPersistentSeminaryCandidacy();
        //
        Student student = persistenceSupport.getIPersistentStudent().readByUsername(id.getUtilizador());
        if (student != null) {
            Candidacy candidacy = (Candidacy) persistentCandidacy.readByOID(Candidacy.class,
                    candidacyID);
            //
            if ((candidacy != null)
                    && (candidacy.getStudent().getIdInternal().intValue() != student.getIdInternal()
                            .intValue()))
                result = false;
        } else {
            result = false;
        }
        return result;
    }

    private RoleType getRoleType() {
        return RoleType.SEMINARIES_COORDINATOR;
    }
}