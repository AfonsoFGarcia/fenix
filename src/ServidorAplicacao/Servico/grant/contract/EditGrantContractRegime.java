/*
 * Created on Jan 24, 2004
 */
package ServidorAplicacao.Servico.grant.contract;

import DataBeans.InfoObject;
import DataBeans.InfoTeacher;
import DataBeans.grant.contract.InfoGrantContractRegime;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.ITeacher;
import Dominio.grant.contract.GrantContract;
import Dominio.grant.contract.GrantContractRegime;
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantContractRegime;
import Dominio.grant.contract.IGrantOrientationTeacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.grant.IPersistentGrantContractRegime;
import ServidorPersistente.grant.IPersistentGrantOrientationTeacher;

/**
 * @author pica
 * @author barbosa
 */
public class EditGrantContractRegime extends EditDomainObjectService
{
    /**
     * The constructor of this class.
     */
    public EditGrantContractRegime()
    {
    }

    protected IDomainObject clone2DomainObject(InfoObject infoObject)
    {
    	return copyInfoGrantContractRegime2IGrantContractRegime((InfoGrantContractRegime) infoObject); 
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp)
    {
        return sp.getIPersistentGrantContractRegime();
    }

    protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
    throws ExcepcaoPersistencia
    {
        IPersistentGrantContractRegime persistentGrantContractRegime = sp.getIPersistentGrantContractRegime();
        IGrantContractRegime grantContractRegime = (IGrantContractRegime) domainObject;

        return persistentGrantContractRegime.readByOID(GrantContractRegime.class, grantContractRegime.getIdInternal());
    }
    
    
    
    public void run(InfoGrantContractRegime infoGrantContractRegime) throws FenixServiceException
	{
		super.run(new Integer(0), infoGrantContractRegime);
	}

	private IGrantContractRegime copyInfoGrantContractRegime2IGrantContractRegime(InfoGrantContractRegime infoGrantContractRegime)
	{
		IGrantContractRegime grantContractRegime = null;
		if (infoGrantContractRegime != null)
		{
			grantContractRegime = new GrantContractRegime();
			grantContractRegime.setIdInternal(infoGrantContractRegime.getIdInternal());
			grantContractRegime.setState(infoGrantContractRegime.getState());
			grantContractRegime.setDateBeginContract(infoGrantContractRegime.getDateBeginContract());
			grantContractRegime.setDateEndContract(infoGrantContractRegime.getDateEndContract());
			grantContractRegime.setDateSendDispatchCC(infoGrantContractRegime.getDateSendDispatchCC());
			grantContractRegime.setDateSendDispatchCD(infoGrantContractRegime.getDateSendDispatchCD());
			grantContractRegime.setDateDispatchCC(infoGrantContractRegime.getDateDispatchCC());
			grantContractRegime.setDateDispatchCD(infoGrantContractRegime.getDateDispatchCD());
			grantContractRegime.setDateAcceptTerm(infoGrantContractRegime.getDateAcceptTerm());

			if(infoGrantContractRegime.getInfoTeacher() != null)
			{
				ITeacher teacher = null;
				teacher = Cloner.copyInfoTeacher2Teacher(infoGrantContractRegime.getInfoTeacher());
				grantContractRegime.setTeacher(teacher);
			}
			
			IGrantContract grantContract = null;
			grantContract = Cloner.copyInfoGrantContract2IGrantContract(infoGrantContractRegime.getInfoGrantContract());
			grantContractRegime.setGrantContract(grantContract);
		}
		return grantContractRegime;
	}
    
	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#doAfterLock(Dominio.IDomainObject, DataBeans.InfoObject, ServidorPersistente.ISuportePersistente)
	 */
	protected void doAfterLock(IDomainObject domainObjectLocked, InfoObject infoObject,
			ISuportePersistente sp) throws FenixServiceException
	{
		InfoGrantContractRegime infoGrantContractRegime = (InfoGrantContractRegime) infoObject;

		if(infoGrantContractRegime.getState().equals(new Integer(1))) //Active Contract Regime
		{
			IGrantContractRegime grantContractRegime = (IGrantContractRegime) domainObjectLocked;
			
			//Set the correct grant orientation teacher
			try
			{
				IPersistentGrantOrientationTeacher persistentGrantOrientationTeacher = sp.getIPersistentGrantOrientationTeacher();
				IGrantContract grantContract = new GrantContract();
				grantContract.setIdInternal(infoGrantContractRegime.getInfoGrantContract().getIdInternal());
				IGrantOrientationTeacher grantOrientationTeacher = persistentGrantOrientationTeacher.readActualGrantOrientationTeacherByContract(grantContract, new Integer(0));
				InfoTeacher infoTeacher = null;
				
				persistentGrantOrientationTeacher.simpleLockWrite(grantOrientationTeacher);
				//If grantOrientationTeacher is filled in grantContractRegime
				if(infoGrantContractRegime.getInfoTeacher() != null)
				{
					//Update grant orientation teacher of contract
					grantOrientationTeacher.setOrientationTeacher(Cloner.copyInfoTeacher2Teacher(infoGrantContractRegime.getInfoTeacher()));
				}
				else //if grantOrientationTeacher is not filled in grantContractRegime
				{
					//Copy old values from grant orientation teacher do grant contract regime
					grantContractRegime.setTeacher(grantOrientationTeacher.getOrientationTeacher());
				}
				grantOrientationTeacher.setBeginDate(infoGrantContractRegime.getDateBeginContract());
				grantOrientationTeacher.setEndDate(infoGrantContractRegime.getDateEndContract());

			}
			catch (ExcepcaoPersistencia persistentException)
			{
				throw new FenixServiceException(persistentException.getMessage());
			}
		}
		
	}
}
