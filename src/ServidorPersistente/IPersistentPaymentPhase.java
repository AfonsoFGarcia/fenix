/*
 * Created on 6/Jan/2004
 *
 */
package ServidorPersistente;


/**
 * @author T�nia Pous�o
 *
 */
public interface IPersistentPaymentPhase extends IPersistentObject
{
	public void deletePaymentPhasesOfThisGratuity(Integer gratuityValuesID) throws ExcepcaoPersistencia;
}
