package ServidorAplicacao.Servico.commons.student;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.util.Cloner;
import Dominio.IEmployee;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetEnrolmentGrade implements IServico {

	private static GetEnrolmentGrade servico = new GetEnrolmentGrade();
	
	/**
	 * The singleton access method of this class.
	 **/
	public static GetEnrolmentGrade getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private GetEnrolmentGrade() {
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "GetEnrolmentGrade";
	}

	public InfoEnrolmentEvaluation run(IEnrolment enrolment) throws FenixServiceException
	{
		List enrolmentEvaluations = enrolment.getEvaluations();

		if ((enrolment == null) || (enrolment.getEvaluations() == null) || (enrolment.getEvaluations().size() == 0)) {
			return null;
		} else
		{
			// This sorts the list ascendingly so we need to reverse it to get the first object.
			Collections.sort(enrolmentEvaluations,new BeanComparator("when"));
			Collections.reverse(enrolmentEvaluations);
			
			try
			{
				return getInfoLatestEvaluation((IEnrolmentEvaluation) enrolmentEvaluations.get(0));
			} catch (ExcepcaoPersistencia e)
			{
				e.printStackTrace();
				throw new FenixServiceException(e);
			}
		}
	}

	private InfoEnrolmentEvaluation getInfoLatestEvaluation(IEnrolmentEvaluation latestEvaluation) throws ExcepcaoPersistencia {
		InfoEnrolmentEvaluation infolatestEvaluation = Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(latestEvaluation);
		if (latestEvaluation.getEmployee() != null) {
			if (String.valueOf(latestEvaluation.getEmployee().getIdInternal()) != null
				|| String.valueOf(latestEvaluation.getEmployee().getIdInternal()).length() > 0) {
				IEmployee employee = readEmployee(latestEvaluation.getEmployee().getIdInternal().intValue());
                latestEvaluation.setEmployee(employee);
                infolatestEvaluation.setInfoEmployee(Cloner.copyIPerson2InfoPerson(employee.getPerson()));
			}
		}
		return infolatestEvaluation;
	}

	private IEmployee readEmployee(int id) throws ExcepcaoPersistencia {
		IEmployee employee = null;
		IPersistentEmployee persistentEmployee;
		try {
			persistentEmployee = SuportePersistenteOJB.getInstance().getIPersistentEmployee();
			employee = persistentEmployee.readByIdInternal(id);
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw e;
		}
		return employee;
	}

}