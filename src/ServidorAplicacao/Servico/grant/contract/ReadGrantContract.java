/*
 * Created on 18/12/2003
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoObject;
import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.grant.contract.InfoGrantContractWithGrantOwnerAndGrantType;
import DataBeans.grant.contract.InfoGrantOrientationTeacher;
import DataBeans.grant.contract.InfoGrantOrientationTeacherWithTeacherAndGrantContract;
import Dominio.IDomainObject;
import Dominio.grant.contract.GrantContract;
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantOrientationTeacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantOrientationTeacher;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadGrantContract extends ReadDomainObjectService implements IService {
    public ReadGrantContract() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantContract();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#clone2InfoObject(Dominio.IDomainObject)
     */
    protected InfoObject clone2InfoObject(IDomainObject domainObject) {
        return InfoGrantContractWithGrantOwnerAndGrantType
                .newInfoFromDomain((IGrantContract) domainObject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass() {
        return GrantContract.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#run(java.lang.Integer)
     */
    public InfoObject run(Integer objectId) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentGrantOrientationTeacher pgot = sp.getIPersistentGrantOrientationTeacher();

            InfoGrantContract infoGrantContract = (InfoGrantContract) super.run(objectId);
            IGrantContract contract = InfoGrantContractWithGrantOwnerAndGrantType
                    .newDomainFromInfo(infoGrantContract);
           

            //get the GrantOrientationTeacher for the contract
            IGrantOrientationTeacher orientationTeacher = pgot
                    .readActualGrantOrientationTeacherByContract(contract, new Integer(0));
          
            InfoGrantOrientationTeacher infoOrientationTeacher = InfoGrantOrientationTeacherWithTeacherAndGrantContract
                    .newInfoFromDomain(orientationTeacher);
            infoGrantContract.setGrantOrientationTeacherInfo(infoOrientationTeacher);

            return infoGrantContract;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }
    }
}