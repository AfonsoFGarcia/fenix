/*
 * Created on 2003/12/04
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IPessoa;
import Dominio.IRole;
import Dominio.Role;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 */

public class SetPersonRoles implements IService {

    public Boolean run(String username, List roleOIDs) throws FenixServiceException {
        Boolean result = new Boolean(false);
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPessoa person = persistentSuport.getIPessoaPersistente().lerPessoaPorUsername(username);
            persistentSuport.getIPessoaPersistente().simpleLockWrite(person);
            person.setPersonRoles(new ArrayList());
            for (int i = 0; i < roleOIDs.size(); i++) {
                IRole role = (IRole) persistentSuport.getIPessoaPersistente().readByOID(Role.class,
                        ((Integer) roleOIDs.get(i)));
                person.getPersonRoles().add(role);
            }
            result = new Boolean(true);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return result;
    }
}