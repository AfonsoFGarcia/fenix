/*
 * Created on 10/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import Dominio.GratuitySituation;
import Dominio.IGratuitySituation;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGratuitySituation;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;


/**
 * @author T�nia Pous�o
 *
 */
/**
 * @author T�nia Pous�o
 *
 */
public class DeleteGratuitySituationById implements IServico
{

    private static DeleteGratuitySituationById servico = new DeleteGratuitySituationById();

    /**
	 * The singleton access method of this class.
	 */
    public static DeleteGratuitySituationById getService()
    {
        return servico;
    }

    /**
	 * The actor of this class.
	 */
    private DeleteGratuitySituationById()
    {
    }

    /**
	 * Returns The Service Name
	 */

    public final String getNome()
    {
        return "DeleteGratuitySituationById";
    }

    /*
     * This clean the exemption's value, but not delete the object.
     * 
     */
    public Boolean run(Integer gratuitySituationID) throws FenixServiceException
    {
    	ISuportePersistente sp = null;
    	
    	try
		{
			sp = SuportePersistenteOJB.getInstance();
			IPersistentGratuitySituation persistentGratuitySituation = sp.getIPersistentGratuitySituation();
			
			IGratuitySituation gratuitySituation = (IGratuitySituation) persistentGratuitySituation.readByOID(GratuitySituation.class,gratuitySituationID, true);
			if(gratuitySituation == null){
				return Boolean.TRUE;
			}			
			
					
			gratuitySituation.setExemptionPercentage(null);
			gratuitySituation.setExemptionType(null);
			gratuitySituation.setExemptionDescription(null);
		}
		catch (ExcepcaoPersistencia e)
		{
			
			throw new FenixServiceException(e);
		}
    		    	
		return Boolean.TRUE;
    }
}
