package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.listings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.ExecutionPeriod;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadStudentsByExecutionDegreeAndExecutionYear implements IServico {

	private static ReadStudentsByExecutionDegreeAndExecutionYear servico = new ReadStudentsByExecutionDegreeAndExecutionYear();

	/**
	 * The singleton access method of this class.
	 **/
	public static ReadStudentsByExecutionDegreeAndExecutionYear getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadStudentsByExecutionDegreeAndExecutionYear() {
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ReadStudentsByExecutionDegreeAndExecutionYear";
	}

	public List run(String executionDegreeString, String executionYearString) throws FenixServiceException {
		ISuportePersistente sp = null;
		List distinctEnrolments = new ArrayList();
			try {
				sp = SuportePersistenteOJB.getInstance();
		
				//Get the Actual Execution Year
				IExecutionYear executionYear = null;
				executionYear = sp.getIPersistentExecutionYear().readExecutionYearByName(executionYearString);
//System.out.println("=================ano: " + executionYear);		

				// Get the Actual Execution Degree
				ICursoExecucao executionDegree = null;
				executionDegree = sp.getICursoExecucaoPersistente().readByDegreeCodeAndExecutionYear(executionDegreeString, executionYear);
//System.out.println("=================curso: " + executionDegree);		

				// Get the Degree Curricular Plan
				IDegreeCurricularPlan degreeCurricularPlan = null;
				degreeCurricularPlan = executionDegree.getCurricularPlan();
//System.out.println("=================degreeCurrPlan: " + degreeCurricularPlan);				

				//Get the List of Student Curricular Plans from the Degree Curricular Plan
				List studentCurricularPlanList = null;
				studentCurricularPlanList = sp.getIStudentCurricularPlanPersistente().readByDegreeCurricularPlan(degreeCurricularPlan);
//System.out.println("=================a lista dos Student Curricular Plans tem tamanho " + studentCurricularPlanList.size());
				if(studentCurricularPlanList == null || studentCurricularPlanList.size() == 0){
					throw new NonExistingServiceException();
				}

				// Get the execution periods
				IExecutionPeriod executionPeriod1 = new ExecutionPeriod("1 Semestre", executionYear);
				IExecutionPeriod executionPeriod2 = new ExecutionPeriod("2 Semestre", executionYear);

				// Get the list of Enrolments for the studentCurricularPlanList -> enrolmentsList
				Iterator iterator = studentCurricularPlanList.iterator();
				final List enrolmentsList = new ArrayList();
				List enrolments1 = new ArrayList();
				List enrolments2 = new ArrayList();
//System.out.println("=================vou entrar no ciclo");
				while(iterator.hasNext()) {	
					
					// Each Student Curricular Plan 
					IStudentCurricularPlan singleSCP = (IStudentCurricularPlan) iterator.next(); 
					
					// Read the enrolments for the first executionPeriod
					enrolments1 = sp.getIPersistentEnrolment().readAllEnrolmentsByStudentCurricularPlanAndExecutionPeriod(singleSCP, executionPeriod1); 
					enrolmentsList.addAll(enrolments1);
//System.out.println("=================tenho os do 1o");
					// Read the enrolments for the second executionPeriod
					enrolments2 = sp.getIPersistentEnrolment().readAllEnrolmentsByStudentCurricularPlanAndExecutionPeriod(singleSCP, executionPeriod2);
					enrolmentsList.addAll(enrolments2);
//System.out.println("=================tenho os do 2o");
				}
//System.out.println("================= a grande lista: size " + enrolmentsList.size());
				
//System.out.println("=================vou filtrar...");
			
			
					//Now it is necessary to filter the list 'enrolmentsList' for distinct entries
					distinctEnrolments = (List) CollectionUtils.select(enrolmentsList, new Predicate (){
						private List auxList = new ArrayList();
						public boolean evaluate(Object arg0) {
							IEnrolment enrolment = (IEnrolment) arg0;
							if (!auxList.contains(enrolment)){
								auxList.add(enrolment);
								return true;
							}else
								return false;
					}});

//System.out.println("================= a lista filtrada" + distinctEnrolments);
System.out.println("=================a lista filtrada tem tamanho: " + distinctEnrolments.size());

//	FALTA SELECCIONAR OS ESTUDANTES CORRESPONDENTES AO RESULTADO DA LISTA FILTRADA. TAREFA ADIADA. NOVA VERSAO EM CURSO...
			

			} catch (ExcepcaoPersistencia ex) {
				FenixServiceException newEx = new FenixServiceException("Persistence layer error");
				newEx.fillInStackTrace();
				throw newEx;
			} 

		return distinctEnrolments;	
	}
	
}
