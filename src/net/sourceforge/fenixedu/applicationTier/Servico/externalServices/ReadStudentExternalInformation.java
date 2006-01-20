/*
 * Created on 2:40:27 PM,Mar 11, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.externalServices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.GetEnrolmentGrade;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalAdressInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalCitizenshipInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalCurricularCourseInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalDegreeBranchInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalDegreeCurricularPlanInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalEnrollmentInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalIdentificationInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalPersonInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoStudentExternalInformation;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz </a>
 * 
 * Created at 2:40:27 PM, Mar 11, 2005
 */
public class ReadStudentExternalInformation extends Service {

	public Collection run(String username) throws ExcepcaoPersistencia,
			FenixServiceException {
		Collection result = new ArrayList();
		Person person = persistentSupport.getIPessoaPersistente().lerPessoaPorUsername(username);
		Collection students = person.getStudents();
		for (Iterator iter = students.iterator(); iter.hasNext();) {
			InfoStudentExternalInformation info = new InfoStudentExternalInformation();
			Student student = (Student) iter.next();

			info.setPerson(this.buildExternalPersonInfo(person));
			info.setDegree(this.buildExternalDegreeCurricularPlanInfo(student));
			info.setCourses(this.buildExternalEnrollmentsInfo(student));
			info.setAvailableRemainingCourses(this
					.buildAvailableRemainingCourses(student));
			info.setNumber(student.getNumber().toString());

			result.add(info);
		}

		return result;
	}

	private Collection buildAvailableRemainingCourses(final Student student) {

		Collection allCourses = student.getActiveStudentCurricularPlan().getDegreeCurricularPlan().getCurricularCourses();
		Collection available = CollectionUtils.select(allCourses,
				new Predicate() {
					public boolean evaluate(Object arg0) {
						boolean availableToEnroll = true;
						boolean atLeastOnOpenScope = false;
						CurricularCourse course = (CurricularCourse) arg0;
						for (CurricularCourseScope scope : course.getScopes()) {
							atLeastOnOpenScope |= scope.isActive()
									.booleanValue();
						}

						availableToEnroll &= atLeastOnOpenScope;
						availableToEnroll &= !student
								.getActiveStudentCurricularPlan()
								.isCurricularCourseApproved(course);
						return availableToEnroll;
					}
				}

		);

		Collection availableInfos = CollectionUtils.collect(available,
				new Transformer() {
					public Object transform(Object arg0) {
						CurricularCourse course = (CurricularCourse) arg0;
						InfoExternalCurricularCourseInfo info = InfoExternalCurricularCourseInfo
								.newFromDomain(course);
						return info;

					}
				}

		);
		return availableInfos;
	}

	/**
	 * @param student
	 * @return
	 * @throws FenixServiceException
	 * @throws ExcepcaoPersistencia 
	 */
	private Collection buildExternalEnrollmentsInfo(Student student)
			throws FenixServiceException, ExcepcaoPersistencia {
		Collection enrollments = new ArrayList();
		Collection curricularPlans = student.getStudentCurricularPlans();
		for (Iterator iter = curricularPlans.iterator(); iter.hasNext();) {
			StudentCurricularPlan curricularPlan = (StudentCurricularPlan) iter.next();
			for (Iterator iterEnrolments = curricularPlan.getEnrolments().iterator(); iterEnrolments.hasNext();) {
				Enrolment enrollment = (Enrolment) iterEnrolments.next();
				if (enrollment.getEnrollmentState().equals(
						EnrollmentState.APROVED)) {
					InfoExternalEnrollmentInfo info = InfoExternalEnrollmentInfo
							.newFromEnrollment(enrollment);

					GetEnrolmentGrade getEnrollmentGrade = new GetEnrolmentGrade();
					InfoEnrolmentEvaluation infoEnrollmentEvaluation = getEnrollmentGrade
							.run(enrollment);
					info.setFinalGrade(infoEnrollmentEvaluation.getGrade());
					enrollments.add(info);
				}
			}
		}

		return enrollments;
	}

	/**
	 * @param student
	 * @return
	 */
	private InfoExternalDegreeCurricularPlanInfo buildExternalDegreeCurricularPlanInfo(
			Student student) {
		InfoExternalDegreeCurricularPlanInfo info = new InfoExternalDegreeCurricularPlanInfo();
		DegreeCurricularPlan degreeCurricularPlan = student
				.getActiveStudentCurricularPlan().getDegreeCurricularPlan();

		info.setName(degreeCurricularPlan.getName());
		info.setCode(degreeCurricularPlan.getDegree().getIdInternal()
				.toString());
		info.setBranch(this.buildExternalDegreeBranchInfo(student));

		Collection courses = student.getActiveStudentCurricularPlan()
				.getDegreeCurricularPlan().getCurricularCourses();
		for (Iterator iter = courses.iterator(); iter.hasNext();) {
			CurricularCourse curricularCourse = (CurricularCourse) iter
					.next();
			info.addCourse(InfoExternalCurricularCourseInfo
					.newFromDomain(curricularCourse));
		}

		return info;
	}

	/**
	 * @param student
	 * @return
	 */
	private InfoExternalDegreeBranchInfo buildExternalDegreeBranchInfo(
			Student student) {
		InfoExternalDegreeBranchInfo info = new InfoExternalDegreeBranchInfo();
		if (student.getActiveStudentCurricularPlan().getBranch() != null) {
			info.setName(student.getActiveStudentCurricularPlan().getBranch()
					.getName());
			info.setCode(student.getActiveStudentCurricularPlan().getBranch()
					.getCode());
		}

		return info;
	}

	/**
	 * @param infoPerson
	 * @return
	 */
	private InfoExternalPersonInfo buildExternalPersonInfo(Person person) {
		InfoExternalPersonInfo info = new InfoExternalPersonInfo();
		info.setAddress(this.buildInfoExternalAdressInfo(person));
		info.setBirthday(person.getNascimento().toString());
		info.setCelularPhone(person.getTelemovel());
		info.setCitizenship(this.builsExternalCitizenshipInfo(person));
		info.setEmail(person.getEmail());
		info.setFiscalNumber(person.getNumContribuinte());
		info.setIdentification(this.buildExternalIdentificationInfo(person));
		info.setName(person.getNome());
		info.setNationality(person.getNacionalidade());
		info.setPhone(person.getTelefone());
		info.setSex(person.getGender().toString());

		return info;
	}

	/**
	 * @param infoPerson
	 * @return
	 */
	private InfoExternalIdentificationInfo buildExternalIdentificationInfo(
			Person person) {
		InfoExternalIdentificationInfo info = new InfoExternalIdentificationInfo();
		info.setDocumentType(person.getIdDocumentType().toString());
		info.setNumber(person.getNumeroDocumentoIdentificacao());
		if (person.getDataEmissaoDocumentoIdentificacao() != null) {
			info.setEmitionDate(person.getDataEmissaoDocumentoIdentificacao()
					.toString());
		}
		if (person.getDataValidadeDocumentoIdentificacao() != null) {
			info.setExpiryDate(person.getDataValidadeDocumentoIdentificacao()
					.toString());
		}
		if (person.getLocalEmissaoDocumentoIdentificacao() != null) {
			info
					.setEmitionLocal(person
							.getLocalEmissaoDocumentoIdentificacao());
		}

		return info;
	}

	/**
	 * @param infoPerson
	 * @return
	 */
	private InfoExternalCitizenshipInfo builsExternalCitizenshipInfo(
			Person person) {
		InfoExternalCitizenshipInfo info = new InfoExternalCitizenshipInfo();
		info.setArea(person.getFreguesiaNaturalidade());
		info.setCounty(person.getConcelhoNaturalidade());

		return info;
	}

	/**
	 * @param infoPerson
	 * @return
	 */
	private InfoExternalAdressInfo buildInfoExternalAdressInfo(Person person) {
		InfoExternalAdressInfo info = new InfoExternalAdressInfo();
		info.setPostalCode(person.getCodigoPostal());
		info.setStreet(person.getMorada());
		info.setTown(person.getLocalidade());

		return info;
	}
}