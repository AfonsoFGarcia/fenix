
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.certificate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.IPrice;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.GraduationType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCertificateList implements IServico {

	private static ReadCertificateList servico = new ReadCertificateList();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadCertificateList getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadCertificateList() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ReadCertificateList";
	}

	
	public List run(GraduationType graduationType, List types) throws FenixServiceException {

			ISuportePersistente sp = null;
			List certificates = null;
			
		System.out.println("lista vazia..." + types.size());
			
			try {
				sp = SuportePersistenteOJB.getInstance();
		
				// Read the certificates
			
				certificates = sp.getIPersistentPrice().readByGraduationTypeAndDocumentType(graduationType, types);		
			} catch (ExcepcaoPersistencia ex) {
				FenixServiceException newEx = new FenixServiceException("Persistence layer error");
				newEx.fillInStackTrace();
				throw newEx;
			} 
System.out.println("lista vazia..." + certificates.size());
			if (certificates == null) 
				throw new ExcepcaoInexistente("No Certificates Found !!");
		
			List result = new ArrayList();
			Iterator iterator = certificates.iterator();
		
			while(iterator.hasNext()) {	
				IPrice price = (IPrice) iterator.next(); 
				result.add(Cloner.copyIPrice2InfoPrice(price));
			}
			return result;		
	}
	
}
