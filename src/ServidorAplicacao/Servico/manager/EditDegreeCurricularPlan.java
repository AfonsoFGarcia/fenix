/*
 * Created on 6/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoDegreeCurricularPlan;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */
public class EditDegreeCurricularPlan implements IServico {

	private static EditDegreeCurricularPlan service = new EditDegreeCurricularPlan();

	public static EditDegreeCurricularPlan getService() {
		return service;
	}

	private EditDegreeCurricularPlan() {
	}

	public final String getNome() {
		return "EditDegreeCurricularPlan";
	}

	public void run(InfoDegreeCurricularPlan newInfoDegreeCP) throws FenixServiceException {

		ICurso degree = null;
		IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = null;
		IDegreeCurricularPlan oldDegreeCP = null;
		ICursoPersistente persistentDegree = null;
		String newName = null;

		try {
			
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			persistentDegreeCurricularPlan = persistentSuport.getIPersistentDegreeCurricularPlan();
			oldDegreeCP = (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOId(new DegreeCurricularPlan(newInfoDegreeCP.getIdInternal()), false);

			if(oldDegreeCP == null)
				throw new NonExistingServiceException("message.nonExistingDegreeCurricularPlan", null);
				
			persistentDegree = persistentSuport.getICursoPersistente();
			Integer degreeId = oldDegreeCP.getDegree().getIdInternal();
			degree = persistentDegree.readByIdInternal(degreeId);
			
			if(degree == null)
				throw new NonExistingServiceException("message.nonExistingDegree", null);
				
			newName = newInfoDegreeCP.getName();
						
			oldDegreeCP.setName(newName);
			oldDegreeCP.setDegree(degree);
			oldDegreeCP.setState(newInfoDegreeCP.getState());
			oldDegreeCP.setInitialDate(newInfoDegreeCP.getInitialDate());
			oldDegreeCP.setEndDate(newInfoDegreeCP.getEndDate());
			oldDegreeCP.setDegreeDuration(newInfoDegreeCP.getDegreeDuration());
			oldDegreeCP.setMinimalYearForOptionalCourses(newInfoDegreeCP.getMinimalYearForOptionalCourses());
			oldDegreeCP.setNeededCredits(newInfoDegreeCP.getNeededCredits());
			oldDegreeCP.setMarkType(newInfoDegreeCP.getMarkType());
			oldDegreeCP.setNumerusClausus(newInfoDegreeCP.getNumerusClausus());
				
			persistentDegreeCurricularPlan.write(oldDegreeCP);
			
		} catch (ExistingPersistentException ex) {
			throw new ExistingServiceException(ex);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}