/*
 * Created on 21/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoGuide;
import DataBeans.InfoGuideEntry;
import DataBeans.util.Cloner;
import Dominio.Contributor;
import Dominio.ExecutionYear;
import Dominio.Guide;
import Dominio.IContributor;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.IGuide;
import Dominio.IMasterDegreeCandidate;
import Dominio.IPrice;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NoActiveStudentCurricularPlanServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingContributorServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.DocumentType;
import Util.GraduationType;
import Util.GuideRequester;
import Util.Specialization;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class PrepareCreateGuide implements IServico {

	private static PrepareCreateGuide servico = new PrepareCreateGuide();

	/**
	 * The singleton access method of this class.
	 **/
	public static PrepareCreateGuide getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private PrepareCreateGuide() {
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "PrepareCreateGuide";
	}

	public InfoGuide run(
		String graduationType,
		InfoExecutionDegree infoExecutionDegree,
		Integer number,
		String requesterType,
		Integer contributorNumber,
		String contributorName,
		String contributorAddress)
		throws FenixServiceException {

		ISuportePersistente sp = null;
		IContributor contributor = null;
		IMasterDegreeCandidate masterDegreeCandidate = null;
		IGuide guide = new Guide();
		InfoGuide infoGuide = new InfoGuide();

		//	Read the Contributor
		try {
			sp = SuportePersistenteOJB.getInstance();
			contributor = sp.getIPersistentContributor().readByContributorNumber(contributorNumber);
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error", ex);
			throw newEx;
		}

		if ((contributor == null)
			&& ((contributorAddress == null)
				|| (contributorAddress.length() == 0)
				|| (contributorName.length() == 0)
				|| (contributorName == null))) {
			throw new NonExistingContributorServiceException();
		}

		if ((contributor == null)
			&& (contributorAddress != null)
			&& (contributorAddress.length() != 0)
			&& (contributorName.length() != 0)
			&& (contributorName != null)) {
			// Create the Contributor
			contributor = new Contributor();
			contributor.setContributorNumber(contributorNumber);
			contributor.setContributorAddress(contributorAddress);
			contributor.setContributorName(contributorName);
			try {
				sp.getIPersistentContributor().write(contributor);
			} catch (ExistingPersistentException e) {
				throw new ExistingServiceException(e);
			} catch (ExcepcaoPersistencia e) {
				throw new FenixServiceException(e);
			}
		} else if (
			(contributor != null)
				&& (contributorAddress != null)
				&& (contributorAddress.length() != 0)
				&& (contributorName.length() != 0)
				&& (contributorName != null)) {
			throw new ExistingServiceException();
		}

		Integer year = null;
		Calendar calendar = Calendar.getInstance();
		year = new Integer(calendar.get(Calendar.YEAR));

		//	Check if the Requester is a Candidate
		ICursoExecucao executionDegree = null;
		if (requesterType.equals(GuideRequester.CANDIDATE_STRING)) {

			try {
				IExecutionYear executionYear =
					new ExecutionYear(infoExecutionDegree.getInfoExecutionYear().getYear());
				executionDegree =
					sp
						.getICursoExecucaoPersistente()
						.readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
						infoExecutionDegree
							.getInfoDegreeCurricularPlan()
							.getInfoDegree()
							.getSigla(),
						infoExecutionDegree.getInfoDegreeCurricularPlan().getName(),
						executionYear);
				masterDegreeCandidate =
					sp
						.getIPersistentMasterDegreeCandidate()
						.readByNumberAndExecutionDegreeAndSpecialization(
						number,
						executionDegree,
						new Specialization(graduationType));
			} catch (ExcepcaoPersistencia ex) {
				FenixServiceException newEx =
					new FenixServiceException("Persistence layer error", ex);
				throw newEx;
			}

			// Check if the Candidate Exists

			if (masterDegreeCandidate == null)
				throw new NonExistingServiceException("O Candidato", null);

			// Get the price for the Candidate Application

			IPrice price = null;

			try {
				price =
					sp.getIPersistentPrice().readByGraduationTypeAndDocumentTypeAndDescription(
						GraduationType.MASTER_DEGREE_TYPE,
						DocumentType.APPLICATION_EMOLUMENT_TYPE,
						graduationType);
			} catch (ExcepcaoPersistencia ex) {
				FenixServiceException newEx = new FenixServiceException("Persistence layer error");
				newEx.fillInStackTrace();
				throw newEx;
			}

			if (price == null) {
				throw new FenixServiceException("Unkown Application Price");
			}

			guide.setContributor(contributor);
			guide.setPerson(masterDegreeCandidate.getPerson());
			guide.setYear(year);
			guide.setTotal(price.getPrice());

			guide.setCreationDate(calendar.getTime());
			guide.setVersion(new Integer(1));
			guide.setExecutionDegree(executionDegree);

			infoGuide = Cloner.copyIGuide2InfoGuide(guide);

			InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
			infoGuideEntry.setDescription(price.getDescription());
			infoGuideEntry.setDocumentType(price.getDocumentType());
			infoGuideEntry.setGraduationType(price.getGraduationType());
			infoGuideEntry.setInfoGuide(infoGuide);
			infoGuideEntry.setPrice(price.getPrice());
			infoGuideEntry.setQuantity(new Integer(1));

			List infoGuideEntries = new ArrayList();
			infoGuideEntries.add(infoGuideEntry);

			infoGuide.setInfoGuideEntries(infoGuideEntries);
			infoGuide.setGuideRequester(GuideRequester.CANDIDATE_TYPE);

		}

		if (requesterType.equals(GuideRequester.STUDENT_STRING)) {
			IStudent student = null;
			IStudentCurricularPlan studentCurricularPlan = null;
			try {
				student = sp.getIPersistentStudent().readByNumero(number, TipoCurso.MESTRADO_OBJ);
				if (student == null)
					throw new NonExistingServiceException("O Aluno", null);

				studentCurricularPlan =
					sp.getIStudentCurricularPlanPersistente().readActiveStudentCurricularPlan(
						student.getNumber(),
						TipoCurso.MESTRADO_OBJ);

			} catch (ExcepcaoPersistencia ex) {

				FenixServiceException newEx = new FenixServiceException("Persistence layer error");
				newEx.fillInStackTrace();
				throw newEx;
			}

			if (studentCurricularPlan == null) {
				throw new NoActiveStudentCurricularPlanServiceException();
			}

			try {
				executionDegree =
					sp.getICursoExecucaoPersistente().readByDegreeCodeAndDegreeCurricularPlanName(
						studentCurricularPlan.getDegreeCurricularPlan().getDegree().getSigla(),
						studentCurricularPlan.getDegreeCurricularPlan().getName());

			} catch (ExcepcaoPersistencia ex) {
				FenixServiceException newEx = new FenixServiceException("Persistence layer error");
				newEx.fillInStackTrace();
				throw newEx;
			}

			// Check if the Candidate Exists

			if (student == null)
				throw new NonExistingServiceException("O Aluno", null);

			guide.setContributor(contributor);
			guide.setPerson(student.getPerson());
			guide.setYear(year);

			guide.setCreationDate(calendar.getTime());
			guide.setVersion(new Integer(1));

			guide.setExecutionDegree(executionDegree);

			infoGuide = Cloner.copyIGuide2InfoGuide(guide);

			infoGuide.setInfoGuideEntries(new ArrayList());
			infoGuide.setGuideRequester(GuideRequester.STUDENT_TYPE);
		}

		return infoGuide;
	}

}
