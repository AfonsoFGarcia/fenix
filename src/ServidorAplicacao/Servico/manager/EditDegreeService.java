/*
 * Created on 29/Jul/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoDegree;
import Dominio.ICurso;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author lmac1
 */
public class EditDegreeService implements IServico {

  private static EditDegreeService service = new EditDegreeService();

  /**
   * The singleton access method of this class.
   */
  public static EditDegreeService getService() {
	return service;
  }

  /**
   * The constructor of this class.
   */
  private EditDegreeService () { }

  /**
   * Service name
   */
  public final String getNome() {
	return "EditDegreeService";
  }

  /**
   * Executes the service. Returns the current infodegree.
   */

	public List run(Integer oldDegreeId, InfoDegree newInfoDegree) throws FenixServiceException {
	
		ISuportePersistente persistentSuport = null;	
		ICursoPersistente persistentDegree = null;
		ICurso oldDegree = null;
		String newCode = null;
		String newName = null;
		TipoCurso newType = null;
		
		try {
				persistentSuport = SuportePersistenteOJB.getInstance();
				persistentDegree = persistentSuport.getICursoPersistente();
				oldDegree = persistentDegree.readByIdInternal(oldDegreeId);
				
			System.out.println("AQUI0000000000000000000000000000000"+oldDegreeId);	
			System.out.println("AQUI11111111111111111111111111111"+oldDegree);
			
			
				List degrees = persistentDegree.readAll();
				degrees.remove((ICurso)oldDegree);
			
			
				newCode = newInfoDegree.getSigla();
				newName = newInfoDegree.getNome();
				newType = newInfoDegree.getTipoCurso();
				List errors = new ArrayList(2);
//				if(newCode.compareToIgnoreCase(oldDegree.getSigla())==0 ||(newName.compareToIgnoreCase(oldDegree.getNome())==0 && newType.equals((TipoCurso) oldDegree.getTipoCurso())))
//					errors = null;
//				else
//				{
					int modified = 0;
					Iterator iter = degrees.iterator();
				
					while(iter.hasNext()) {
						ICurso degreeIter = (ICurso) iter.next();
						if(newCode.compareToIgnoreCase(degreeIter.getSigla())==0) {
							modified++;
							errors.add(0, newCode);
				 		}
						if(newName.compareToIgnoreCase(degreeIter.getNome())==0 && newType.equals((TipoCurso) degreeIter.getTipoCurso())) {
						modified++;
						errors.add(1, newName);
						}
					}

System.out.println("AQUI11111111111111111111111111111"+oldDegree);

					oldDegree.setNome(newName);
					oldDegree.setSigla(newCode);
					oldDegree.setTipoCurso(newType);
//				}
				
				return errors;
			
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}