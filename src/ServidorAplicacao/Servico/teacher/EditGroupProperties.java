/*
 * Created on 17/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoGroupProperties;
import Dominio.DisciplinaExecucao;
import Dominio.GroupProperties;
import Dominio.IDisciplinaExecucao;
import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonValidChangeServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 */
public class EditGroupProperties implements IServico {

	private static EditGroupProperties service = new EditGroupProperties();

	/**
	* The singleton access method of this class.
	**/

	public static EditGroupProperties getService() {
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private EditGroupProperties() {
	}
	/**
	 * The name of the service
	 */
	public final String getNome() {
		return "EditGroupProperties";
	}

	private boolean checkIfAlreadyExists(InfoGroupProperties infoGroupProperties, IGroupProperties groupProperties)
		throws FenixServiceException {

		IPersistentStudentGroup persistentStudentGroup = null;
		IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
		IGroupProperties existingGroupProperties = null;
		try {
			ISuportePersistente ps = SuportePersistenteOJB.getInstance();
			IPersistentGroupProperties persistentGroupProperties = ps.getIPersistentGroupProperties();
			if (!infoGroupProperties.getName().equals(groupProperties.getName())) {
				persistentGroupProperties = ps.getIPersistentGroupProperties();
				existingGroupProperties =
					persistentGroupProperties.readGroupPropertiesByExecutionCourseAndName(
						groupProperties.getExecutionCourse(),
						infoGroupProperties.getName());
				if (existingGroupProperties != null)
					throw new ExistingServiceException();
			}

		} catch (ExcepcaoPersistencia excepcaoPersistencia) {

			throw new FenixServiceException(excepcaoPersistencia);
		}
		return true;
	}

	private boolean checkIfIsPossibleToEdit(InfoGroupProperties infoGroupProperties, IGroupProperties groupProperties)
		throws FenixServiceException {

		IPersistentStudentGroup persistentStudentGroup = null;
		IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
		IGroupProperties existingGroupProperties = null;
		try {
			ISuportePersistente ps = SuportePersistenteOJB.getInstance();
			IPersistentGroupProperties persistentGroupProperties = ps.getIPersistentGroupProperties();

			persistentStudentGroup = ps.getIPersistentStudentGroup();
			persistentStudentGroupAttend = ps.getIPersistentStudentGroupAttend();
			List allStudentsGroup = persistentStudentGroup.readAllStudentGroupByGroupProperties(groupProperties);

			Integer groupMaximumNumber = infoGroupProperties.getGroupMaximumNumber();
			Integer maximumCapacity = infoGroupProperties.getMaximumCapacity();
			Integer minimumCapacity = infoGroupProperties.getMinimumCapacity();

			if (groupMaximumNumber != null) {
				ITurno shift = null;
				List shiftsInternalList = new ArrayList();
				Iterator iterator = allStudentsGroup.iterator();

				while (iterator.hasNext()) {
					shift = ((IStudentGroup) iterator.next()).getShift();
					if (!shiftsInternalList.contains(shift))
						shiftsInternalList.add(shift);
				}

				Iterator iterator2 = shiftsInternalList.iterator();
				List studentGroupsList = null;
				shift = null;

				while (iterator2.hasNext()) {
					shift = (ITurno) iterator2.next();
					studentGroupsList = persistentStudentGroup.readAllStudentGroupByGroupPropertiesAndShift(groupProperties, shift);
					if (studentGroupsList.size() > groupMaximumNumber.intValue()) {
						throw new NonValidChangeServiceException();
					}

				}

			}

			if (maximumCapacity == null && minimumCapacity == null) {
				return true;
			}

			Iterator iterGroups = allStudentsGroup.iterator();
			List allStudents = null;
			int size;
			while (iterGroups.hasNext()) {
				allStudents = new ArrayList();

				IStudentGroup studentGroup = (IStudentGroup) iterGroups.next();
				allStudents = persistentStudentGroupAttend.readAllByStudentGroup(studentGroup);
				size = allStudents.size();

				if (maximumCapacity == null && minimumCapacity != null) {
					if (size < minimumCapacity.intValue()) {
						throw new NonValidChangeServiceException();
					}
				} else if (minimumCapacity == null && maximumCapacity != null) {
					if (size > maximumCapacity.intValue()) {
						throw new NonValidChangeServiceException();
					}
				} else if (size > maximumCapacity.intValue() || size < minimumCapacity.intValue()) {
					throw new NonValidChangeServiceException();
				}
			}

		} catch (ExcepcaoPersistencia excepcaoPersistencia) {

			throw new FenixServiceException(excepcaoPersistencia);
		}
		return true;
	}

	/**
	 * Executes the service.
	 */

	public boolean run(Integer objectCode, InfoGroupProperties infoGroupProperties) throws FenixServiceException {

		boolean result = false;
		try {
			ISuportePersistente ps = SuportePersistenteOJB.getInstance();

			IPersistentGroupProperties persistentGroupProperties = ps.getIPersistentGroupProperties();
			IDisciplinaExecucaoPersistente persistentExecutionCourse = ps.getIDisciplinaExecucaoPersistente();

			IGroupProperties groupProperties = new GroupProperties(infoGroupProperties.getIdInternal());
			groupProperties = (IGroupProperties) persistentGroupProperties.readByOId(groupProperties, false);

			if (checkIfAlreadyExists(infoGroupProperties, groupProperties))
				if (checkIfIsPossibleToEdit(infoGroupProperties, groupProperties)) {
					IDisciplinaExecucao executionCourse =
						(IDisciplinaExecucao) persistentExecutionCourse.readByOId(new DisciplinaExecucao(objectCode), false);

					groupProperties.setEnrolmentBeginDay(infoGroupProperties.getEnrolmentBeginDay());
					groupProperties.setEnrolmentEndDay(infoGroupProperties.getEnrolmentEndDay());
					groupProperties.setEnrolmentPolicy(infoGroupProperties.getEnrolmentPolicy());
					groupProperties.setExecutionCourse(executionCourse);
					groupProperties.setGroupMaximumNumber(infoGroupProperties.getGroupMaximumNumber());
					groupProperties.setIdealCapacity(infoGroupProperties.getIdealCapacity());
					groupProperties.setIdInternal(infoGroupProperties.getIdInternal());
					groupProperties.setMaximumCapacity(infoGroupProperties.getMaximumCapacity());
					groupProperties.setMinimumCapacity(infoGroupProperties.getMinimumCapacity());

					groupProperties.setName(infoGroupProperties.getName());
					groupProperties.setProjectDescription(infoGroupProperties.getProjectDescription());
					groupProperties.setShiftType(infoGroupProperties.getShiftType());
					persistentGroupProperties.lockWrite(groupProperties);
				}

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return true;

	}
}