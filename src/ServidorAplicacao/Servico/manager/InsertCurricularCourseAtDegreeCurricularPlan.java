/*
 * Created on 8/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;

import DataBeans.InfoCurricularCourse;
import Dominio.CurricularCourse;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */

public class InsertCurricularCourseAtDegreeCurricularPlan implements IServico {

	private static InsertCurricularCourseAtDegreeCurricularPlan service = new InsertCurricularCourseAtDegreeCurricularPlan();

	public static InsertCurricularCourseAtDegreeCurricularPlan getService() {
		return service;
	}

	private InsertCurricularCourseAtDegreeCurricularPlan() {
	}

	public final String getNome() {
		return "InsertCurricularCourseAtDegreeCurricularPlan";
	}
	

	public void run(InfoCurricularCourse infoCurricularCourse) throws FenixServiceException {
	
		try {
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
				
				Integer degreeCurricularPlanId = infoCurricularCourse.getInfoDegreeCurricularPlan().getIdInternal();
				
				IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport.getIPersistentDegreeCurricularPlan();
				IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOId(new DegreeCurricularPlan(degreeCurricularPlanId), false);
				
				String name = infoCurricularCourse.getName();
				String code = infoCurricularCourse.getCode();
				
				IPersistentCurricularCourse persistentCurricularCourse = persistentSuport.getIPersistentCurricularCourse();
				
				ICurricularCourse curricularCourse = new CurricularCourse();
				curricularCourse.setBasic(infoCurricularCourse.getBasic());		
				curricularCourse.setCode(code);
				curricularCourse.setDegreeCurricularPlan(degreeCurricularPlan);
				curricularCourse.setMandatory(infoCurricularCourse.getMandatory());
				curricularCourse.setName(name);
				curricularCourse.setType(infoCurricularCourse.getType());
				curricularCourse.setAssociatedExecutionCourses(new ArrayList());							

				persistentCurricularCourse.lockWrite(curricularCourse);
					
		} catch(ExistingPersistentException existingException) {
			throw new ExistingServiceException("A disciplina curricular " + infoCurricularCourse.getName() + ", com c�digo " + infoCurricularCourse.getCode(), existingException); 
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}