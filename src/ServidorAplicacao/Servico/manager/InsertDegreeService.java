/*
 * Created on 30/Mai/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoDegree;
import Dominio.Curso;
import Dominio.ICurso;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class InsertDegreeService implements IServico {

	private static InsertDegreeService service = new InsertDegreeService();

	public static InsertDegreeService getService() {
		return service;
	}

	private InsertDegreeService() {
	}

	public final String getNome() {
		return "InsertDegreeService";
	}
	

	public List run(InfoDegree infoDegree) throws FenixServiceException {

		ICurso degree = null;
		ICursoPersistente persistentDegree = null;
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();

			persistentDegree = persistentSuport.getICursoPersistente();
			List persistentDegrees = persistentSuport.getICursoPersistente().readAll();

			Iterator iter = persistentDegrees.iterator();
			
			String code = infoDegree.getSigla();
			String name = infoDegree.getNome();
			List errors = new ArrayList();
			errors.add(null);
			errors.add(null);
			
			
			
			while(iter.hasNext()){
				ICurso degreeIter = (ICurso) iter.next();
				if(code.compareToIgnoreCase(degreeIter.getSigla())==0)
				{	
		        	errors.add(0, code);
				}
				if(name.compareToIgnoreCase(degreeIter.getNome())==0)
					errors.add(1, name);
			}

			List errors2 = new ArrayList();
			errors2.add(null);
			errors2.add(null);

			if(errors2.equals((List)errors)){
				errors = null;
				degree = new Curso(
							code,
							name,
							infoDegree.getTipoCurso());
	
				persistentDegree.lockWrite(degree);
			}
			return errors;
			
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {

			throw new FenixServiceException(excepcaoPersistencia);
		}

		
	}
}