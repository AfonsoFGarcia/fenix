/*
 * Created on 23/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import org.apache.commons.beanutils.BeanUtils;

import DataBeans.InfoObject;
import DataBeans.grant.contract.InfoGrantCostCenter;
import DataBeans.grant.contract.InfoGrantPart;
import DataBeans.grant.contract.InfoGrantProject;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.ITeacher;
import Dominio.Teacher;
import Dominio.grant.contract.GrantCostCenter;
import Dominio.grant.contract.GrantPart;
import Dominio.grant.contract.GrantPaymentEntity;
import Dominio.grant.contract.GrantProject;
import Dominio.grant.contract.IGrantPart;
import Dominio.grant.contract.IGrantPaymentEntity;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.grant.InvalidGrantPaymentEntityException;
import ServidorAplicacao.Servico.exceptions.grant.InvalidPartResponsibleTeacherException;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.grant.IPersistentGrantPart;
import ServidorPersistente.grant.IPersistentGrantPaymentEntity;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantPart extends EditDomainObjectService
{
	/**
	 * The constructor of this class.
	 */
	public EditGrantPart()
	{
	}

	protected IDomainObject clone2DomainObject(InfoObject infoObject)
	{
		return Cloner.copyInfoGrantPart2IGrantPart((InfoGrantPart) infoObject);
	}

	protected IPersistentObject getIPersistentObject(ISuportePersistente sp)
	{
		return sp.getIPersistentGrantPart();
	}

	protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
		throws ExcepcaoPersistencia
	{
		IPersistentGrantPart pgs = sp.getIPersistentGrantPart();
		IGrantPart grantPart = (IGrantPart) domainObject;

		return pgs.readByOID(GrantPart.class, grantPart.getIdInternal());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#doAfterLock(Dominio.IDomainObject,
	 *      DataBeans.InfoObject, ServidorPersistente.ISuportePersistente)
	 */
	protected void doAfterLock(
		IDomainObject domainObjectToLock,
		InfoObject infoObject,
		ISuportePersistente sp)
		throws FenixServiceException
	{
		IGrantPart grantPart = (IGrantPart) domainObjectToLock;
		InfoGrantPart infoGrantPart = (InfoGrantPart) infoObject;
		IGrantPaymentEntity paymentEntity = null;

		//set the payment entity
		try
		{
			IPersistentGrantPaymentEntity pgp = sp.getIPersistentGrantPaymentEntity();

			paymentEntity =
				(IGrantPaymentEntity) pgp.readByOID(
					GrantPaymentEntity.class,
					infoGrantPart.getInfoGrantPaymentEntity().getIdInternal());
			if (paymentEntity != null)
			{
				if (infoGrantPart.getInfoGrantPaymentEntity() instanceof InfoGrantCostCenter)
					grantPart.setGrantPaymentEntity(new GrantCostCenter());
				else if (infoGrantPart.getInfoGrantPaymentEntity() instanceof InfoGrantProject)
					grantPart.setGrantPaymentEntity(new GrantProject());

				BeanUtils.copyProperties(grantPart.getGrantPaymentEntity(), paymentEntity);
			}
			else
				throw new InvalidGrantPaymentEntityException();

			domainObjectToLock = (IDomainObject) grantPart;
		}
		catch (Exception e)
		{
			throw new FenixServiceException(e.getMessage());
		}

		//set the teacher
		try
		{
			ITeacher teacher = new Teacher();

			IPersistentTeacher pt = sp.getIPersistentTeacher();

			teacher = pt.readByNumber(infoGrantPart.getInfoResponsibleTeacher().getTeacherNumber());
			if (teacher != null)
			{
				grantPart.setResponsibleTeacher(new Teacher());
				BeanUtils.copyProperties(grantPart.getResponsibleTeacher(), teacher);
			}
			else
				throw new InvalidPartResponsibleTeacherException();

			domainObjectToLock = (IDomainObject) grantPart;
		}
        catch (InvalidPartResponsibleTeacherException e)
        {
            throw new InvalidPartResponsibleTeacherException();
        }
		catch (Exception e)
		{
			throw new FenixServiceException(e.getMessage());
		}
	}

	public void run(InfoGrantPart infoGrantPart) throws FenixServiceException
	{
		super.run(new Integer(0), infoGrantPart);
	}
}