/*
 * Created on 07/Set/2003
 *
 */
package ServidorAplicacao.Servico.student;

import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.ITurno;
import Dominio.StudentGroup;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidChangeServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 */

public class EditGroupShift implements IServico {

	private IPersistentStudentGroup persistentStudentGroup = null;

	private static EditGroupShift service = new EditGroupShift();

	/**
		* The singleton access method of this class.
		*/
	public static EditGroupShift getService() {
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private EditGroupShift() {
	}
	/**
	 * The name of the service
	 */
	public final String getNome() {
		return "EditGroupShift";
	}

	/**
	 * Executes the service.
	 */

	public boolean run(Integer studentGroupCode, Integer newShiftCode, String username) throws FenixServiceException {

		ITurnoPersistente persistentShift = null;
		IPersistentStudentGroup persistentStudentGroup = null;
			

		try {
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IPersistentStudentGroupAttend persistentStudentGroupAttend = persistentSupport.getIPersistentStudentGroupAttend();
		
			persistentShift = persistentSupport.getITurnoPersistente();
			ITurno shift = (ITurno) persistentShift.readByOId(new Turno(newShiftCode), false);

			persistentStudentGroup = persistentSupport.getIPersistentStudentGroup();
			IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup.readByOId(new StudentGroup(studentGroupCode), false);
			IStudent student = (IStudent) persistentSupport.getIPersistentStudent().readByUsername(username);
			
			if (studentGroup == null)
				throw new InvalidArgumentsServiceException();

			IFrequenta attend =(IFrequenta) persistentSupport.getIFrequentaPersistente().readByAlunoAndDisciplinaExecucao(
					student,
					studentGroup.getGroupProperties().getExecutionCourse());
			IStudentGroupAttend studentGroupAttend = persistentStudentGroupAttend.readBy(studentGroup, attend);
			
			
			if (studentGroupAttend == null)
				throw new InvalidSituationServiceException();

			IGroupProperties groupProperties = studentGroup.getGroupProperties();
			IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
			IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);
			
			boolean result = strategy.checkNumberOfGroups(groupProperties, shift);
			if(!result)
				throw new InvalidChangeServiceException();
			
			studentGroup.setShift(shift);
			persistentStudentGroup.lockWrite(studentGroup);

		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}

		return true;
	}
}
