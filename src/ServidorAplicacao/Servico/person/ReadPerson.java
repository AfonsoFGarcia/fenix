/*
 * Created on 19/01/2004
 * 
 */
package ServidorAplicacao.Servico.person;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoObject;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.IPessoa;
import Dominio.Pessoa;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * @author  Barbosa
 * @author  Pica
 *  
 */
public class ReadPerson extends ReadDomainObjectService implements IService
{
    /**
     * The constructor of this class.
     */
    public ReadPerson()
    {
    }
    
    /* (non-Javadoc)
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        return sp.getIPessoaPersistente();
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#clone2InfoObject(Dominio.IDomainObject)
     */
    protected InfoObject clone2InfoObject(IDomainObject domainObject)
    {
        return Cloner.copyIPerson2InfoPerson((IPessoa) domainObject);
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass()
    {
        return Pessoa.class;
    }
}