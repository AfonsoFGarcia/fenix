/*
 * Created on 29/Jul/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.List;

import DataBeans.InfoDegree;
import Dominio.ICurso;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoCurso;

/**
 * @author lmac1
 */
public class EditDegree implements IServico {

  private static EditDegree service = new EditDegree();

  /**
   * The singleton access method of this class.
   */
  public static EditDegree getService() {
	return service;
  }

  /**
   * The constructor of this class.
   */
  private EditDegree () { }

  /**
   * Service name
   */
  public final String getNome() {
	return "EditDegree";
  }

  /**
   * Executes the service. Returns the current infodegree.
   */

	public void run(Integer oldDegreeId, InfoDegree newInfoDegree) throws FenixServiceException {
	
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
			
				List degrees = persistentDegree.readAll();
				degrees.remove((ICurso)oldDegree);
			
			
				newCode = newInfoDegree.getSigla();
				newName = newInfoDegree.getNome();
				newType = newInfoDegree.getTipoCurso();
//				List errors = new ArrayList(3);
//				errors.add(null);
//			    errors.add(null);
//			errors.add(null);
//				if(newCode.compareToIgnoreCase(oldDegree.getSigla())!=0 || (newName.compareToIgnoreCase(oldDegree.getNome())!=0 || newType.equals((TipoCurso) oldDegree.getTipoCurso())))
//				

//				{
//					int modified = 0;
//					Iterator iter = degrees.iterator();
//				
//					while(iter.hasNext()) {
//						ICurso degreeIter = (ICurso) iter.next();
//						if(newCode.compareToIgnoreCase(degreeIter.getSigla())==0) {
//							modified++;
////							errors.set(0, newCode);
//				 		}
//						if(newName.compareToIgnoreCase(degreeIter.getNome())==0 && newType.equals((TipoCurso) degreeIter.getTipoCurso())) {
//						modified++;
////						errors.set(1, newType.toString());
////						errors.set(2, newName);
//						}
//					}
//					if(modified == 0) {
//						persistentDegree.simpleLockWrite(oldDegree);
//						oldDegree.setNome(newName);
//						oldDegree.setSigla(newCode);
//						oldDegree.setTipoCurso(newType);
//					}
//				}
				
//				return errors;
			if(oldDegree != null) {

				try {
					persistentDegree.lockWrite(oldDegree);
					oldDegree.setNome(newName);
					oldDegree.setSigla(newCode);
					oldDegree.setTipoCurso(newType);
				
					} catch (ExistingPersistentException ex) {
						throw new ExistingServiceException("O com esses nome,sigla e tipo", ex);
					}
				}
		
		
			
			
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}