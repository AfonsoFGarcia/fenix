/*
 * Created on 12/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import DataBeans.ISiteComponent;
import DataBeans.InfoSiteStudentGroup;
import DataBeans.InfoSiteStudentInformation;
import Dominio.DisciplinaExecucao;
import Dominio.GroupProperties;
import Dominio.IDisciplinaExecucao;
import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author ansr and scpo
 *
 */
public class PrepareCreateStudentGroup implements IServico {

	private static PrepareCreateStudentGroup service = new PrepareCreateStudentGroup();

	/**
		* The singleton access method of this class.
		*/
	public static PrepareCreateStudentGroup getService() {
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private PrepareCreateStudentGroup() {
	}
	/**
	 * The name of the service
	 */
	public final String getNome() {
		return "PrepareCreateStudentGroup";
	}

	/**
	 * Executes the service.
	 */

	public ISiteComponent run(Integer executionCourseCode, Integer groupPropertiesCode) throws FenixServiceException {

		IFrequentaPersistente persistentAttend = null;
		IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
		
		IPersistentStudentGroup persistentStudentGroup = null;
		IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
		List frequentas = new ArrayList();
		
		List infoStudentInformationList = new ArrayList();
		InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
		Integer groupNumber=null;
		try {

			ISuportePersistente ps = SuportePersistenteOJB.getInstance();
			persistentExecutionCourse = ps.getIDisciplinaExecucaoPersistente();
			persistentAttend = ps.getIFrequentaPersistente();
			persistentStudentGroup = ps.getIPersistentStudentGroup();
			persistentStudentGroupAttend = ps.getIPersistentStudentGroupAttend();

			IDisciplinaExecucao executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(new DisciplinaExecucao(executionCourseCode), false);
			IGroupProperties groupProperties =
				(IGroupProperties) ps.getIPersistentGroupProperties().readByOId(new GroupProperties(groupPropertiesCode), false);

			frequentas = persistentAttend.readByExecutionCourse(executionCourse);

			List allStudentsGroups = persistentStudentGroup.readAllStudentGroupByGroupProperties(groupProperties);
			groupNumber = new Integer(1);
			
			if (allStudentsGroups.size() != 0) {
				Collections.sort(allStudentsGroups, new BeanComparator("groupNumber"));
				Integer lastGroupNumber = ((IStudentGroup) allStudentsGroups.get(allStudentsGroups.size() - 1)).getGroupNumber();
				groupNumber = new Integer(lastGroupNumber.intValue() + 1);

			}

			
			Iterator iterator = allStudentsGroups.iterator();
			List allStudentGroupAttend;

			while (iterator.hasNext()) {

				allStudentGroupAttend = persistentStudentGroupAttend.readAllByStudentGroup((IStudentGroup) iterator.next());
				Iterator iterator2 = allStudentGroupAttend.iterator();
				IFrequenta frequenta = null;
				while (iterator2.hasNext()) {
					frequenta = ((IStudentGroupAttend) iterator2.next()).getAttend();
					if (frequentas.contains(frequenta)) {
						frequentas.remove(frequenta);
					}

				}
			}

			IStudent student = null;
			Iterator iterator3 = frequentas.iterator();
			
			while (iterator3.hasNext()) {
				student = ((IFrequenta) iterator3.next()).getAluno();
				InfoSiteStudentInformation infoSiteStudentInformation = new InfoSiteStudentInformation();
				
				infoSiteStudentInformation.setEmail(student.getPerson().getEmail());
				infoSiteStudentInformation.setName(student.getPerson().getNome());
				infoSiteStudentInformation.setNumber(student.getNumber());
				infoSiteStudentInformation.setUsername(student.getPerson().getUsername());
				infoStudentInformationList.add(infoSiteStudentInformation);
			}

		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}
		Collections.sort(infoStudentInformationList, new BeanComparator("number"));
				
		infoSiteStudentGroup.setInfoSiteStudentInformationList(infoStudentInformationList);
		infoSiteStudentGroup.setNrOfElements(groupNumber);
		
		return infoSiteStudentGroup;

	}
}
