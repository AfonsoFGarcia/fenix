/*
 * Created on 14/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoGratuityValues;
import DataBeans.InfoPaymentPhase;
import Dominio.CursoExecucao;
import Dominio.GratuitySituation;
import Dominio.GratuityValues;
import Dominio.ICursoExecucao;
import Dominio.IEmployee;
import Dominio.IGratuitySituation;
import Dominio.IGratuityValues;
import Dominio.IPaymentPhase;
import Dominio.IPessoa;
import Dominio.IStudentCurricularPlan;
import Dominio.PaymentPhase;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.masterDegree.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPersistentGratuitySituation;
import ServidorPersistente.IPersistentGratuityValues;
import ServidorPersistente.IPersistentPaymentPhase;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author T�nia Pous�o
 *  
 */
public class InsertGratuityData implements IService
{

	/**
	 * Constructor
	 */
	public InsertGratuityData()
	{
	}

	public Object run(InfoGratuityValues infoGratuityValues) throws FenixServiceException
	{
		if (infoGratuityValues == null)
		{
			throw new FenixServiceException("impossible.insertGratuityValues");
		}
		if (infoGratuityValues.getInfoExecutionDegree() == null
			|| infoGratuityValues.getInfoExecutionDegree().getIdInternal() == null
			|| infoGratuityValues.getInfoExecutionDegree().getIdInternal().intValue() <= 0)
		{
			throw new FenixServiceException("impossible.insertGratuityValues");
		}

		if (infoGratuityValues.getInfoEmployee() == null
			|| infoGratuityValues.getInfoEmployee().getPerson() == null
			|| infoGratuityValues.getInfoEmployee().getPerson().getUsername() == null
			|| infoGratuityValues.getInfoEmployee().getPerson().getUsername().length() <= 0)
		{
			throw new FenixServiceException("impossible.insertGratuityValues");
		}

		ISuportePersistente sp = null;

		Double gratuityValue = validateGratuity(sp, infoGratuityValues);

		try
		{
			sp = SuportePersistenteOJB.getInstance();
			IPersistentGratuityValues persistentGratuityValues = sp.getIPersistentGratuityValues();

			IGratuityValues gratuityValues = new GratuityValues();
			gratuityValues.setIdInternal(infoGratuityValues.getIdInternal());

			ICursoExecucao executionDegree = new CursoExecucao();
			executionDegree.setIdInternal(infoGratuityValues.getInfoExecutionDegree().getIdInternal());
			gratuityValues.setExecutionDegree(executionDegree);

			gratuityValues =
				persistentGratuityValues.readGratuityValuesByExecutionDegree(executionDegree);
			if (gratuityValues == null) //it doesn't exist in database, then write it
			{
				gratuityValues = new GratuityValues();

				//execution Degree
				ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();
				executionDegree = new CursoExecucao();
				executionDegree.setIdInternal(
					infoGratuityValues.getInfoExecutionDegree().getIdInternal());

				executionDegree =
					(ICursoExecucao) persistentExecutionDegree.readByOId(executionDegree, false);
				gratuityValues.setExecutionDegree(executionDegree);

			}

			validatePaymentPhasesWithTransaction(sp, gratuityValues);
			//			validateGratuitySituationWithTransaction(sp, gratuityValues);

			registerWhoAndWhen(sp, infoGratuityValues, gratuityValues);

			gratuityValues.setAnualValue(infoGratuityValues.getAnualValue());
			gratuityValues.setScholarShipValue(infoGratuityValues.getScholarShipValue());
			gratuityValues.setFinalProofValue(infoGratuityValues.getFinalProofValue());
			gratuityValues.setCreditValue(infoGratuityValues.getCreditValue());
			gratuityValues.setCourseValue(infoGratuityValues.getCourseValue());
			gratuityValues.setProofRequestPayment(infoGratuityValues.getProofRequestPayment());
			gratuityValues.setStartPayment(infoGratuityValues.getStartPayment());
			gratuityValues.setEndPayment(infoGratuityValues.getEndPayment());

			//write all payment phases
			writePaymentPhases(sp, infoGratuityValues, gratuityValues);

			//update gratuity values in all student curricular plan that belong to this execution
			// degree
			updateStudentsGratuitySituation(sp, gratuityValues, gratuityValue);
		
			persistentGratuityValues.simpleLockWrite(gratuityValues);
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException("impossible.insertGratuityValues");
		}
		catch (FenixServiceException fenixServiceException)
		{
			throw fenixServiceException;
		}

		return Boolean.TRUE;
	}

	private Double validateGratuity(ISuportePersistente sp, InfoGratuityValues infoGratuityValues)
		throws FenixServiceException
	{
		//find the gratuity's value
		Double gratuityValue = null;

		if (infoGratuityValues.getAnualValue() != null)
		{
			gratuityValue = infoGratuityValues.getAnualValue();
		}
		else
		{
			if (infoGratuityValues.getProofRequestPayment() != null
				&& infoGratuityValues.getProofRequestPayment().equals(Boolean.TRUE))
			{
				gratuityValue = infoGratuityValues.getScholarShipValue();
			}
			else
			{
				gratuityValue =
					new Double(
						infoGratuityValues.getScholarShipValue().doubleValue()
							+ infoGratuityValues.getFinalProofValue().doubleValue());
			}
		}

		List paymentPhasesList = infoGratuityValues.getInfoPaymentPhases();
		if (paymentPhasesList != null && paymentPhasesList.size() > 0)
		{
			//verify if total of all payment phases isn't greater then anual value
			ListIterator iterator = paymentPhasesList.listIterator();
			double totalValuePaymentPhases = 0;
			while (iterator.hasNext())
			{
				InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) iterator.next();
				totalValuePaymentPhases += infoPaymentPhase.getValue().floatValue();
			}
			if (totalValuePaymentPhases > gratuityValue.doubleValue())
			{
				throw new FenixServiceException("error.masterDegree.gatuyiuty.totalValuePaymentPhases");
			}
			//verify if all payment phases's dates are correct
			validateDatesOfPaymentPhases(paymentPhasesList);

			//registration Payment
			if (infoGratuityValues.getRegistrationPayment() != null
				&& infoGratuityValues.getRegistrationPayment().equals(Boolean.TRUE))
			{
				iterator = paymentPhasesList.listIterator();
				InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) iterator.next();
				infoPaymentPhase.setDescription(SessionConstants.REGISTRATION_PAYMENT_KEY);
			}
		}
		return gratuityValue;
	}

	private void validateDatesOfPaymentPhases(List paymentPhasesList) throws FenixServiceException
	{
		Collections.sort(paymentPhasesList, new BeanComparator("endDate"));

		ListIterator iterator = paymentPhasesList.listIterator();
		while (iterator.hasNext())
		{
			InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) iterator.next();

			if (iterator.hasNext())
			{
				InfoPaymentPhase infoPaymentPhase2Compare = (InfoPaymentPhase) iterator.next();
				if ((infoPaymentPhase2Compare.getStartDate() != null
					&& !infoPaymentPhase.getEndDate().before(infoPaymentPhase2Compare.getStartDate()))
					|| (infoPaymentPhase2Compare.getStartDate() == null
						&& !infoPaymentPhase.getEndDate().before(infoPaymentPhase2Compare.getEndDate())))
				{
					throw new FenixServiceException("error.impossible.paymentPhaseWithWrongDates");
				}
				iterator.previous();
			}
		}
	}

	private void validatePaymentPhasesWithTransaction(
		ISuportePersistente sp,
		IGratuityValues gratuityValues)
		throws FenixServiceException
	{
		//verify if any payment phase has any transaction associated
		if (gratuityValues.getPaymentPhaseList() != null
			&& gratuityValues.getPaymentPhaseList().size() > 0)
		{
			ListIterator iterator = gratuityValues.getPaymentPhaseList().listIterator();
			while (iterator.hasNext())
			{
				IPaymentPhase paymentPhase = (IPaymentPhase) iterator.next();

				if (paymentPhase.getTransactionList() != null
					&& paymentPhase.getTransactionList().size() > 0)
				{
					throw new FenixServiceException("error.impossible.paymentPhaseWithTransactional");
				}
			}
		}
	}

	//	private void validateGratuitySituationWithTransaction(
	//		ISuportePersistente sp,
	//		IGratuityValues gratuityValues)
	//		throws FenixServiceException
	//	{
	//		//verify if gratuity has any transaction associated
	//		IGratuityTransaction persistentGratuityTransaction = sp.getIPersistentGratuityTransaction();
	//
	//		List gratuityTransactions = persistentGratuityTransaction.readByGratuityValues(gratuityValues);
	//
	//		if(gratuityTransactions != null && gratuityTransactions.size() > 0){
	//					throw new FenixServiceException("error.impossible.gratuityWithTransactions");
	//		}
	//
	//	}

	private void registerWhoAndWhen(
		ISuportePersistente sp,
		InfoGratuityValues infoGratuityValues,
		IGratuityValues gratuityValues)
		throws ExcepcaoPersistencia
	{
		//employee who made register
		IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
		IPessoa person =
			persistentPerson.lerPessoaPorUsername(
				infoGratuityValues.getInfoEmployee().getPerson().getUsername());
		if (person != null)
		{
			IPersistentEmployee persistentEmployee = sp.getIPersistentEmployee();
			IEmployee employee = persistentEmployee.readByPerson(person.getIdInternal().intValue());
			gratuityValues.setEmployee(employee);
		}

		Calendar now = Calendar.getInstance();
		gratuityValues.setWhen(now.getTime());
	}

	private void writePaymentPhases(
		ISuportePersistente sp,
		InfoGratuityValues infoGratuityValues,
		IGratuityValues gratuityValues)
		throws FenixServiceException
	{
		IPersistentPaymentPhase persistentPaymentPhase = sp.getIPersistentPaymentPhase();

		try
		{
			if (gratuityValues.getPaymentPhaseList() != null
				&& gratuityValues.getPaymentPhaseList().size() > 0)
			{
				persistentPaymentPhase.deletePaymentPhasesOfThisGratuity(gratuityValues.getIdInternal());
			}

			if (infoGratuityValues.getInfoPaymentPhases() != null
				&& infoGratuityValues.getInfoPaymentPhases().size() > 0)
			{
				ListIterator iterator = infoGratuityValues.getInfoPaymentPhases().listIterator();
				while (iterator.hasNext())
				{
					InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) iterator.next();

					IPaymentPhase paymentPhase = new PaymentPhase();
					paymentPhase.setStartDate(infoPaymentPhase.getStartDate());
					paymentPhase.setEndDate(infoPaymentPhase.getEndDate());
					paymentPhase.setValue(infoPaymentPhase.getValue());
					if (infoPaymentPhase.getDescription() == null)
					{
						infoPaymentPhase.setDescription(String.valueOf(iterator.previousIndex()));
					}
					paymentPhase.setDescription(infoPaymentPhase.getDescription());

					paymentPhase.setGratuityValues(gratuityValues);

					persistentPaymentPhase.simpleLockWrite(paymentPhase);
				}
			}
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException("impossible.insertGratuityValues");
		}
	}

	private void updateStudentsGratuitySituation(
		ISuportePersistente sp,
		IGratuityValues gratuityValues,
		Double gratuityValue)
		throws FenixServiceException
	{
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
			sp.getIStudentCurricularPlanPersistente();
		IPersistentGratuitySituation persistentGratuitySituation = sp.getIPersistentGratuitySituation();

		List studentCurricularPlanList = null;
		try
		{
			studentCurricularPlanList =
				persistentStudentCurricularPlan.readByDegreeCurricularPlan(
					gratuityValues.getExecutionDegree().getCurricularPlan());

			if (studentCurricularPlanList != null && studentCurricularPlanList.size() > 0)
			{
				ListIterator iterator = studentCurricularPlanList.listIterator();
				//for each student curricular plan update the correspondent gratuity situatuion
				//with the gratuity values key
				while (iterator.hasNext())
				{
					IStudentCurricularPlan studentCurricularPlan =
						(IStudentCurricularPlan) iterator.next();

					IGratuitySituation gratuitySituation =
						persistentGratuitySituation
							.readGratuitySituatuionByStudentCurricularPlanAndGratuityValues(
							studentCurricularPlan,
							gratuityValues);

					if (gratuitySituation == null)
					{
						gratuitySituation = new GratuitySituation();
						gratuitySituation.setStudentCurricularPlan(studentCurricularPlan);
						gratuitySituation.setGratuityValues(gratuityValues);
						persistentGratuitySituation.simpleLockWrite(gratuitySituation);
					}
				}
			}
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException("impossible.insertGratuityValues");
		}
	}
}

//private void updateStudentsGratuitySituation(ISuportePersistente sp, IGratuityValues gratuityValues,
// Double gratuityValue)
//throws FenixServiceException
//{
//	IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
//	sp.getIStudentCurricularPlanPersistente();
//	IPersistentGratuitySituation persistentGratuitySituation = sp.getIPersistentGratuitySituation();
//
//	List studentCurricularPlanList = null;
//	List gratuitySituationList = null;
//	try
//	{
//		//find all students curricular plan with the degree curricular plan that belongs to this
//		// execution degree
//
//		// gratuitySituationList =
//		// persistentGratuitySituation.readGratuitySituationsByDegreeCurricularPlan(
//		// gratuityValues.getExecutionDegree().getCurricularPlan());
//		//
//		// Iterator iterGratuitySituation = gratuitySituationList.iterator();
//		// while (iterGratuitySituation.hasNext())
//		// {
//		// IGratuitySituation gratuitySituation = (IGratuitySituation) iterGratuitySituation.next();
//		// // it would be useful to create strategy for these if's
//		// // because in future there will be also licenciaturas e doutoramentos
//		// if (gratuitySituation.getStudentCurricularPlan().getSpecialization() != null
//		// && (gratuitySituation
//		// .getStudentCurricularPlan()
//		// .getSpecialization()
//		// .equals(Specialization.MESTRADO_TYPE)
//		// || gratuitySituation.getStudentCurricularPlan().getSpecialization().equals(
//		// Specialization.INTEGRADO_TYPE)))
//		// {
//		// gratuitySituation.setPayedValue(new Double(0));
//		// gratuitySituation.setRemainingValue(gratuityValues.getAnualValue());
//		// }
//		// else if (
//		// gratuitySituation.getStudentCurricularPlan().getSpecialization() != null
//		// && (gratuitySituation
//		// .getStudentCurricularPlan()
//		// .getSpecialization()
//		// .equals(Specialization.ESPECIALIZACAO_TYPE)))
//		// {
//		// if (gratuitySituation.getStudentCurricularPlan().getEnrolments() != null
//		// && gratuitySituation.getStudentCurricularPlan().getEnrolments().size() > 0)
//		// {
//		// gratuitySituation.setPayedValue(new Double(0));
//		// if (gratuityValues.getCourseValue() != null)
//		// {
//		// gratuitySituation.setRemainingValue(
//		// new Double(
//		// gratuityValues.getCourseValue().doubleValue()
//		// * gratuitySituation
//		// .getStudentCurricularPlan()
//		// .getEnrolments()
//		// .size()));
//		// }
//		// else
//		// {
//		// double totalToPay = 0;
//		// Iterator iterCourse =
//		// gratuitySituation.getStudentCurricularPlan().getEnrolments().iterator();
//		// while (iterCourse.hasNext())
//		// {
//		// IEnrolment enrolment = (IEnrolment) iterCourse.next();
//		// totalToPay
//		// += enrolment.getCurricularCourseScope().getCredits().doubleValue()
//		// * gratuityValues.getCreditValue().doubleValue();
//		// }
//		// gratuitySituation.setRemainingValue(new Double(totalToPay));
//		// }
//		// } else {
//		// gratuitySituation.setPayedValue(new Double(0));
//		// gratuitySituation.setRemainingValue(gratuityValue);
//		// }
//		// }
//		// persistentGratuitySituation.simpleLockWrite(gratuitySituation);
//		// }
//
//		studentCurricularPlanList =
//		persistentStudentCurricularPlan.readByDegreeCurricularPlan(
//				gratuityValues.getExecutionDegree().getCurricularPlan());
//
//		if (studentCurricularPlanList != null && studentCurricularPlanList.size() > 0)
//		{
//			ListIterator iterator = studentCurricularPlanList.listIterator();
//			//for each student curricular plan update the correspondent gratuity situatuion
//			//with the gratuity values key
//			while (iterator.hasNext())
//			{
//				IStudentCurricularPlan studentCurricularPlan =
//				(IStudentCurricularPlan) iterator.next();
//
//				IGratuitySituation gratuitySituation =
//				persistentGratuitySituation.readGratuitySituatuionByStudentCurricularPlan(
//						studentCurricularPlan);
//
//				if (gratuitySituation == null)
//				{
//					gratuitySituation = new GratuitySituation();
//					gratuitySituation.setStudentCurricularPlan(studentCurricularPlan);
//				}
//				if (gratuitySituation.getGratuityValues() == null
//						|| (gratuityValues.getIdInternal() != null
//								&& !gratuitySituation.getGratuityValues().getIdInternal().equals(
//										gratuityValues.getIdInternal())))
//				{
//					gratuitySituation.setGratuityValues(gratuityValues);
//				}
//
//				// it would be useful to create strategy for these if's
//				// because in future there will be also licenciaturas e doutoramentos
//				if (studentCurricularPlan.getSpecialization() != null
//						&& (studentCurricularPlan.getSpecialization().equals(Specialization.MESTRADO_TYPE)
//								|| studentCurricularPlan.getSpecialization().equals(
//										Specialization.INTEGRADO_TYPE)))
//				{
//					gratuitySituation.setPayedValue(new Double(0));
//					gratuitySituation.setRemainingValue(gratuityValue);
//				}
//				else if (
//						studentCurricularPlan.getSpecialization() != null
//						&& (studentCurricularPlan
//								.getSpecialization()
//								.equals(Specialization.ESPECIALIZACAO_TYPE)))
//				{
//					if (studentCurricularPlan.getEnrolments() != null
//							&& studentCurricularPlan.getEnrolments().size() > 0)
//					{
//						gratuitySituation.setPayedValue(new Double(0));
//						if (gratuityValues.getCourseValue() != null)
//						{
//							gratuitySituation.setRemainingValue(
//									new Double(
//											gratuityValues.getCourseValue().doubleValue()
//											* studentCurricularPlan.getEnrolments().size()));
//						}
//						else
//						{
//							double totalToPay = 0;
//							Iterator iterCourse = studentCurricularPlan.getEnrolments().iterator();
//							while (iterCourse.hasNext())
//							{
//								IEnrolment enrolment = (IEnrolment) iterCourse.next();
//								if(enrolment.getCurricularCourseScope().getCredits() != null){
//									totalToPay
//									+= enrolment.getCurricularCourseScope().getCredits().doubleValue()
//									* gratuityValues.getCreditValue().doubleValue();
//								}
//							}
//							gratuitySituation.setRemainingValue(new Double(totalToPay));
//						}
//					} else {
//						gratuitySituation.setPayedValue(new Double(0));
//						gratuitySituation.setRemainingValue(gratuityValue);
//					}
//				}
//				persistentGratuitySituation.simpleLockWrite(gratuitySituation);
//			}
//		}
//	}
//	catch (ExcepcaoPersistencia e)
//	{
//		e.printStackTrace();
//		throw new FenixServiceException("impossible.insertGratuityValues");
//	}
//}
