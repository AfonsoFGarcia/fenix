/*
 * Created on 10/Set/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoStudentTestLog;
import DataBeans.util.Cloner;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IStudent;
import Dominio.IStudentTestLog;
import Dominio.Student;
import Dominio.StudentTestLog;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTestLog implements IServico {

	private static ReadStudentTestLog service = new ReadStudentTestLog();

	public static ReadStudentTestLog getService() {
		return service;
	}

	public String getNome() {
		return "ReadStudentTestLog";
	}

	public List run(
		Integer executionCourseId,
		Integer distributedTestId,
		Integer studentId)
		throws FenixServiceException {

		ISuportePersistente persistentSuport;
		List infoStudentTestLogList = new ArrayList();
		
		try {
			persistentSuport = SuportePersistenteOJB.getInstance();
			IStudent student = new Student(studentId);
			student =
				(IStudent) persistentSuport.getIPersistentStudent().readByOId(
					student,
					false);
			if (student == null)
				throw new FenixServiceException();
			IDistributedTest distributedTest =
				new DistributedTest(distributedTestId);
			if (distributedTest == null)
				throw new FenixServiceException();
			distributedTest =
				(IDistributedTest) persistentSuport
					.getIPersistentDistributedTest()
					.readByOId(
					distributedTest,
					false);

			List studentTestLogList = persistentSuport.getIPersistentStudentTestLog().readByStudentAndDistributedTest(student, distributedTest);
			Iterator it = studentTestLogList.iterator();
			while (it.hasNext()) {
				IStudentTestLog studentTestLog = (StudentTestLog)it.next();
				InfoStudentTestLog infoStudentTestLog = Cloner.copyIStudentTestLog2InfoStudentTestLog(studentTestLog);

				String[] eventTokens = infoStudentTestLog.getEvent().split(";");
				List eventList = new ArrayList();
				for(int i=0; i<eventTokens.length; i++)
					eventList.add(eventTokens[i]);

				infoStudentTestLog.setEventList(eventList);
				infoStudentTestLogList.add(infoStudentTestLog);
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return infoStudentTestLogList;
	}

}
