/*
 * Created on 25/Set/2003, 16:56:39
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import Dominio.Seminaries.Candidacy;
import Dominio.Seminaries.ICandidacy;
import ServidorAplicacao.IServico;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryCandidacy;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 25/Set/2003, 16:56:39
 * 
 */
public class ChangeCandidacyApprovanceStatus implements IServico
{
	private static ChangeCandidacyApprovanceStatus service= new ChangeCandidacyApprovanceStatus();
	/**
	 * The singleton access method of this class.
	 **/
	public static ChangeCandidacyApprovanceStatus getService()
	{
		return service;
	}
	/**
	 * The actor of this class.
	 **/
	private ChangeCandidacyApprovanceStatus()
	{
	}
	/**
	 * Returns The Service Name */
	public final String getNome()
	{
		return "Seminaries.ChangeCandidacyApprovanceStatus";
	}
	public void run(List candidaciesIDs) throws BDException
	{
		List seminariesInfo= new LinkedList();
		try
		{
			ISuportePersistente persistenceSupport= SuportePersistenteOJB.getInstance();
			IPersistentSeminaryCandidacy persistentCandidacy= persistenceSupport.getIPersistentSeminaryCandidacy();
			for (Iterator iterator= candidaciesIDs.iterator(); iterator.hasNext();)
			{
				ICandidacy candidacy=
					(ICandidacy) persistentCandidacy.readByOID(Candidacy.class, (Integer) iterator.next());
				persistentCandidacy.lockWrite(candidacy);
				candidacy.setApproved(new Boolean(!candidacy.getApproved().booleanValue()));
			}
		}
		catch (ExcepcaoPersistencia ex)
		{
			throw new BDException(
				"Got an error while trying to change the approved status from a list of candidacies",
				ex);
		}
	}
}
