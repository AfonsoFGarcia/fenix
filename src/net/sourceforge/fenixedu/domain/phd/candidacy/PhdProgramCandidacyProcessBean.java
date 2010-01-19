package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.QualificationBean;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramCollaborationType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipantBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgramFocusArea;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.LocalDate;

public class PhdProgramCandidacyProcessBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private PersonBean personBean;

    private LocalDate candidacyDate;

    private DomainReference<PhdProgram> program;

    private DomainReference<ExecutionYear> executionYear;

    private DomainReference<Degree> degree;

    private String thesisTitle;

    private PhdIndividualProgramCollaborationType collaborationType;

    private String otherCollaborationType;

    private ChoosePersonBean choosePersonBean;

    private String email;

    private String captcha;

    private String institutionId;

    private PhdProgramCandidacyProcessState state = PhdProgramCandidacyProcessState.STAND_BY_WITH_MISSING_INFORMATION;

    private DomainReference<PhdProgramPublicCandidacyHashCode> candidacyHashCode;

    private DomainReference<PhdProgramFocusArea> focusArea;

    private List<PhdParticipantBean> guidings;

    private List<QualificationBean> qualifications;

    private List<PhdCandidacyRefereeBean> candidacyReferees;

    private PhdProgramDocumentUploadBean curriculumVitae;

    private PhdProgramDocumentUploadBean identificationDocument;

    private PhdProgramDocumentUploadBean motivationLetter;

    private PhdProgramDocumentUploadBean socialSecurityDocument;

    private PhdProgramDocumentUploadBean researchPlan;

    private PhdProgramDocumentUploadBean dissertationOrFinalWorkDocument;

    private List<PhdProgramDocumentUploadBean> habilitationCertificateDocuments;

    private List<PhdProgramDocumentUploadBean> phdGuidingLetters;

    public PhdProgramCandidacyProcessBean() {
	setCandidacyDate(new LocalDate());
    }

    public LocalDate getCandidacyDate() {
	return candidacyDate;
    }

    public void setCandidacyDate(LocalDate candidacyDate) {
	this.candidacyDate = candidacyDate;
    }

    public PhdProgram getProgram() {
	return (this.program != null) ? this.program.getObject() : null;
    }

    public void setProgram(PhdProgram program) {
	this.program = (program != null) ? new DomainReference<PhdProgram>(program) : null;
    }

    public PersonBean getPersonBean() {
	return personBean;
    }

    public void setPersonBean(PersonBean personBean) {
	this.personBean = personBean;
    }

    public ExecutionYear getExecutionYear() {
	return (this.executionYear != null) ? this.executionYear.getObject() : null;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = (executionYear != null) ? new DomainReference<ExecutionYear>(executionYear) : null;
    }

    public Person getOrCreatePersonFromBean() {
	if (!getPersonBean().hasPerson()) {
	    Person person = new Person(getPersonBean());
	    getPersonBean().setPerson(person);
	    return person;
	}

	if (getPersonBean().getPerson().hasRole(RoleType.EMPLOYEE)) {
	    return getPersonBean().getPerson();
	}

	return getPersonBean().getPerson().edit(personBean);
    }

    public Degree getDegree() {
	return (this.degree != null) ? this.degree.getObject() : null;
    }

    public boolean hasDegree() {
	return getDegree() != null;
    }

    public void setDegree(Degree degree) {
	this.degree = (degree != null) ? new DomainReference<Degree>(degree) : null;
    }

    public ExecutionDegree getExecutionDegree() {
	return hasDegree() ? null : getDegree().getLastActiveDegreeCurricularPlan().getExecutionDegreeByAcademicInterval(
		getExecutionYear().getAcademicInterval());
    }

    public String getThesisTitle() {
	return thesisTitle;
    }

    public void setThesisTitle(String thesisTitle) {
	this.thesisTitle = thesisTitle;
    }

    public PhdIndividualProgramCollaborationType getCollaborationType() {
	return collaborationType;
    }

    public void setCollaborationType(PhdIndividualProgramCollaborationType collaborationType) {
	this.collaborationType = collaborationType;
    }

    public boolean hasCollaborationType() {
	return getCollaborationType() != null;
    }

    public String getOtherCollaborationType() {
	return otherCollaborationType;
    }

    public void setOtherCollaborationType(String otherCollaborationType) {
	this.otherCollaborationType = otherCollaborationType;
    }

    public ChoosePersonBean getChoosePersonBean() {
	return choosePersonBean;
    }

    public void setChoosePersonBean(ChoosePersonBean choosePersonBean) {
	this.choosePersonBean = choosePersonBean;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getCaptcha() {
	return captcha;
    }

    public void setCaptcha(String captcha) {
	this.captcha = captcha;
    }

    public String getInstitutionId() {
	return institutionId;
    }

    public void setInstitutionId(String institutionId) {
	this.institutionId = institutionId;
    }

    public boolean hasInstitutionId() {
	return !StringUtils.isEmpty(this.institutionId);
    }

    public PhdProgramCandidacyProcessState getState() {
	return state;
    }

    public void setState(PhdProgramCandidacyProcessState state) {
	this.state = state;
    }

    public PhdProgramPublicCandidacyHashCode getCandidacyHashCode() {
	return (this.candidacyHashCode != null) ? this.candidacyHashCode.getObject() : null;
    }

    public void setCandidacyHashCode(final PhdProgramPublicCandidacyHashCode candidacyHashCode) {
	this.candidacyHashCode = (candidacyHashCode != null) ? new DomainReference<PhdProgramPublicCandidacyHashCode>(
		candidacyHashCode) : null;
    }

    public PhdIndividualProgramProcess getIndividualProgramProcess() {
	return getCandidacyHashCode().getIndividualProgramProcess();
    }

    public boolean hasCandidacyHashCode() {
	return getCandidacyHashCode() != null;
    }

    public PhdProgramFocusArea getFocusArea() {
	return (this.focusArea != null) ? this.focusArea.getObject() : null;
    }

    public void setFocusArea(final PhdProgramFocusArea focusArea) {
	this.focusArea = (focusArea != null) ? new DomainReference<PhdProgramFocusArea>(focusArea) : null;
    }

    public List<PhdParticipantBean> getGuidings() {
	return guidings;
    }

    public void setGuidings(List<PhdParticipantBean> guidings) {
	this.guidings = guidings;
    }

    public boolean hasAnyGuiding() {
	return this.guidings != null && !this.guidings.isEmpty();
    }

    public void addGuiding(final PhdParticipantBean guiding) {
	this.guidings.add(guiding);
    }

    public void removeGuiding(int index) {
	this.guidings.remove(index);
    }

    public List<QualificationBean> getQualifications() {
	return qualifications;
    }

    public void setQualifications(List<QualificationBean> qualifications) {
	this.qualifications = qualifications;
    }

    public void addQualification(final QualificationBean qualification) {
	this.qualifications.add(qualification);
    }

    public void removeQualification(int index) {
	this.qualifications.remove(index);
    }

    public boolean hasAnyQualification() {
	return this.qualifications != null && !this.qualifications.isEmpty();
    }

    public void sortQualificationsByAttendedEnd() {
	Collections.sort(this.qualifications, QualificationBean.COMPARATOR_BY_MOST_RECENT_ATTENDED_END);
    }

    public List<PhdCandidacyRefereeBean> getCandidacyReferees() {
	return candidacyReferees;
    }

    public void setCandidacyReferees(List<PhdCandidacyRefereeBean> candidacyReferees) {
	this.candidacyReferees = candidacyReferees;
    }

    public void addCandidacyReferee(PhdCandidacyRefereeBean phdCandidacyRefereeBean) {
	this.candidacyReferees.add(phdCandidacyRefereeBean);
    }

    public void removeCandidacyReferee(int index) {
	this.candidacyReferees.remove(index);
    }

    public boolean hasAnyCandidacyReferee() {
	return this.candidacyReferees != null && !this.candidacyReferees.isEmpty();
    }

    public void clearPerson() {
	getPersonBean().setPerson(null);
    }

    public PhdProgramDocumentUploadBean getCurriculumVitae() {
	return curriculumVitae;
    }

    public void setCurriculumVitae(PhdProgramDocumentUploadBean curriculumVitae) {
	this.curriculumVitae = curriculumVitae;
    }

    public PhdProgramDocumentUploadBean getIdentificationDocument() {
	return identificationDocument;
    }

    public void setIdentificationDocument(PhdProgramDocumentUploadBean identificationDocument) {
	this.identificationDocument = identificationDocument;
    }

    public PhdProgramDocumentUploadBean getMotivationLetter() {
	return motivationLetter;
    }

    public void setMotivationLetter(PhdProgramDocumentUploadBean motivationLetter) {
	this.motivationLetter = motivationLetter;
    }

    public PhdProgramDocumentUploadBean getSocialSecurityDocument() {
	return socialSecurityDocument;
    }

    public void setSocialSecurityDocument(PhdProgramDocumentUploadBean socialSecurityDocument) {
	this.socialSecurityDocument = socialSecurityDocument;
    }

    public PhdProgramDocumentUploadBean getResearchPlan() {
	return researchPlan;
    }

    public void setResearchPlan(PhdProgramDocumentUploadBean researchPlan) {
	this.researchPlan = researchPlan;
    }

    public PhdProgramDocumentUploadBean getDissertationOrFinalWorkDocument() {
	return dissertationOrFinalWorkDocument;
    }

    public void setDissertationOrFinalWorkDocument(PhdProgramDocumentUploadBean dissertationOrFinalWorkDocument) {
	this.dissertationOrFinalWorkDocument = dissertationOrFinalWorkDocument;
    }

    public List<PhdProgramDocumentUploadBean> getHabilitationCertificateDocuments() {
	return habilitationCertificateDocuments;
    }

    public void setHabilitationCertificateDocuments(List<PhdProgramDocumentUploadBean> habilitationCertificateDocuments) {
	this.habilitationCertificateDocuments = habilitationCertificateDocuments;
    }

    public void addHabilitationCertificateDocument(PhdProgramDocumentUploadBean document) {
	this.habilitationCertificateDocuments.add(document);
    }

    public void removeHabilitationCertificateDocument(int index) {
	this.habilitationCertificateDocuments.remove(index);
    }

    public void removeHabilitationCertificateDocumentFiles() {
	for (final PhdProgramDocumentUploadBean bean : getHabilitationCertificateDocuments()) {
	    bean.removeFile();
	}
    }

    public List<PhdProgramDocumentUploadBean> getPhdGuidingLetters() {
	return phdGuidingLetters;
    }

    public void setPhdGuidingLetters(List<PhdProgramDocumentUploadBean> phdGuidingLetters) {
	this.phdGuidingLetters = phdGuidingLetters;
    }

    public void removePhdGuidingLetters() {
	for (final PhdProgramDocumentUploadBean bean : getPhdGuidingLetters()) {
	    bean.removeFile();
	}
    }

    public List<PhdProgramDocumentUploadBean> getAllDocuments() {
	final List<PhdProgramDocumentUploadBean> result = new ArrayList<PhdProgramDocumentUploadBean>();

	result.add(getCurriculumVitae());
	result.add(getIdentificationDocument());
	result.add(getMotivationLetter());

	if (getSocialSecurityDocument().hasAnyInformation()) {
	    result.add(getSocialSecurityDocument());
	}

	if (getResearchPlan().hasAnyInformation()) {
	    result.add(getResearchPlan());
	}

	if (getDissertationOrFinalWorkDocument().hasAnyInformation()) {
	    result.add(getDissertationOrFinalWorkDocument());
	}

	for (final PhdProgramDocumentUploadBean bean : getHabilitationCertificateDocuments()) {
	    if (bean.hasAnyInformation()) {
		result.add(bean);
	    }
	}

	for (final PhdProgramDocumentUploadBean bean : getPhdGuidingLetters()) {
	    if (bean.hasAnyInformation()) {
		result.add(bean);
	    }
	}

	return result;
    }

}
