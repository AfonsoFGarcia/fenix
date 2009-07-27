package net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.FormationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;

import org.joda.time.LocalDate;

public class SecondCycleIndividualCandidacyProcessBean extends IndividualCandidacyProcessWithPrecedentDegreeInformationBean {

    private DomainReference<Degree> selectedDegree;

    private String professionalStatus;

    private String otherEducation;

    private String istStudentNumber;

    private List<CandidacyProcessDocumentUploadBean> habilitationCertificateList;
    private List<CandidacyProcessDocumentUploadBean> reportOrWorkDocumentList;
    private CandidacyProcessDocumentUploadBean handicapProofDocument;
    private CandidacyProcessDocumentUploadBean curriculumVitaeDocument;

    public SecondCycleIndividualCandidacyProcessBean() {
	setCandidacyDate(new LocalDate());
	setFormationConcludedBeanList(new ArrayList<FormationBean>());
	initializeDocumentUploadBeans();
	setObservations("");
	setIstStudentNumber("");
	setPrecedentDegreeType(PrecedentDegreeType.EXTERNAL_DEGREE);
    }

    public SecondCycleIndividualCandidacyProcessBean(final SecondCycleIndividualCandidacyProcess process) {
	setIndividualCandidacyProcess(process);
	setSelectedDegree(process.getCandidacySelectedDegree());
	setProfessionalStatus(process.getCandidacyProfessionalStatus());
	setOtherEducation(process.getCandidacyOtherEducation());
	setPrecedentDegreeInformation(new CandidacyPrecedentDegreeInformationBean(process
		.getCandidacyPrecedentDegreeInformation()));
	setPrecedentDegreeType(PrecedentDegreeType.valueOf(process.getCandidacyPrecedentDegreeInformation()));
	setCandidacyDate(process.getCandidacyDate());
	initializeFormation(process.getCandidacy().getFormations());
	setObservations(process.getCandidacy().getObservations());
	setIstStudentNumber(process.getCandidacy().getFormerStudentNumber());
    }

    public SecondCycleCandidacyProcess getCandidacyProcess() {
	return (SecondCycleCandidacyProcess) super.getCandidacyProcess();
    }

    public Degree getSelectedDegree() {
	return (this.selectedDegree != null) ? this.selectedDegree.getObject() : null;
    }

    public void setSelectedDegree(Degree selectedDegree) {
	this.selectedDegree = (selectedDegree != null) ? new DomainReference<Degree>(selectedDegree) : null;
    }

    public String getProfessionalStatus() {
	return professionalStatus;
    }

    public void setProfessionalStatus(String professionalStatus) {
	this.professionalStatus = professionalStatus;
    }

    public String getOtherEducation() {
	return otherEducation;
    }

    public void setOtherEducation(String otherEducation) {
	this.otherEducation = otherEducation;
    }

    public boolean hasChoosenPerson() {
	return getChoosePersonBean().hasPerson();
    }

    public void removeChoosePersonBean() {
	setChoosePersonBean(null);
    }

    @Override
    protected double getMinimumEcts(final CycleType cycleType) {
	if (!cycleType.equals(CycleType.FIRST_CYCLE)) {
	    throw new IllegalArgumentException();
	}
	return 150d;
    }

    @Override
    protected List<CycleType> getValidPrecedentCycleTypes() {
	return Collections.singletonList(CycleType.FIRST_CYCLE);
    }

    @Override
    protected boolean isPreBolonhaPrecedentDegreeAllowed() {
	return true;
    }

    public List<CandidacyProcessDocumentUploadBean> getHabilitationCertificateList() {
	return habilitationCertificateList;
    }

    public void setHabilitationCertificateList(List<CandidacyProcessDocumentUploadBean> habilitationCertificateList) {
	this.habilitationCertificateList = habilitationCertificateList;
    }

    public List<CandidacyProcessDocumentUploadBean> getReportOrWorkDocumentList() {
	return reportOrWorkDocumentList;
    }

    public void setReportOrWorkDocumentList(List<CandidacyProcessDocumentUploadBean> reportOrWorkDocumentList) {
	this.reportOrWorkDocumentList = reportOrWorkDocumentList;
    }

    public CandidacyProcessDocumentUploadBean getHandicapProofDocument() {
	return handicapProofDocument;
    }

    public void setHandicapProofDocument(CandidacyProcessDocumentUploadBean handicapProofDocument) {
	this.handicapProofDocument = handicapProofDocument;
    }

    public CandidacyProcessDocumentUploadBean getCurriculumVitaeDocument() {
	return curriculumVitaeDocument;
    }

    public void setCurriculumVitaeDocument(CandidacyProcessDocumentUploadBean curriculumVitaeDocument) {
	this.curriculumVitaeDocument = curriculumVitaeDocument;
    }

    public void addHabilitationCertificateDocument() {
	this.habilitationCertificateList.add(new CandidacyProcessDocumentUploadBean(
		IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT));
    }

    public void removeHabilitationCertificateDocument(final int index) {
	this.habilitationCertificateList.remove(index);
    }

    public void addReportOrWorkDocument() {
	this.reportOrWorkDocumentList.add(new CandidacyProcessDocumentUploadBean(
		IndividualCandidacyDocumentFileType.REPORT_OR_WORK_DOCUMENT));
    }

    public void removeReportOrWorkDocument(final int index) {
	this.reportOrWorkDocumentList.remove(index);
    }

    public String getIstStudentNumber() {
	return this.istStudentNumber;
    }

    public void setIstStudentNumber(String value) {
	this.istStudentNumber = value;
    }

    @Override
    protected void initializeDocumentUploadBeans() {
	setDocumentIdentificationDocument(new CandidacyProcessDocumentUploadBean(
		IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION));
	setPaymentDocument(new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT));
	setVatCatCopyDocument(new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT));

	this.habilitationCertificateList = new ArrayList<CandidacyProcessDocumentUploadBean>();
	addHabilitationCertificateDocument();

	this.reportOrWorkDocumentList = new ArrayList<CandidacyProcessDocumentUploadBean>();
	addReportOrWorkDocument();

	this.handicapProofDocument = new CandidacyProcessDocumentUploadBean(
		IndividualCandidacyDocumentFileType.HANDICAP_PROOF_DOCUMENT);
	this.curriculumVitaeDocument = new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.CV_DOCUMENT);
	setPhotoDocument(new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.PHOTO));
    }
}
