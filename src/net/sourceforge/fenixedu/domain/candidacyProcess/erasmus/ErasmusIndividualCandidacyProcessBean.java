package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;

import org.joda.time.LocalDate;

public class ErasmusIndividualCandidacyProcessBean extends IndividualCandidacyProcessBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Degree selectedDegree;

    private Set<CurricularCourse> selectedCurricularCourses;

    private ErasmusStudentDataBean erasmusStudentDataBean;

    private boolean toAccessFenix;

    private Boolean validatedByGri;

    private Boolean validatedByErasmusCoordinator;

    private String alertSubject;

    private String alertBody;

    private Boolean createAlert;

    private Boolean sendEmail;

    public ErasmusIndividualCandidacyProcessBean() {
	setCandidacyDate(new LocalDate());
	initializeDocumentUploadBeans();
	setErasmusStudentDataBean(new ErasmusStudentDataBean());
	setSelectedCurricularCourses(new HashSet<CurricularCourse>());

	this.toAccessFenix = false;
    }

    public ErasmusIndividualCandidacyProcessBean(final ErasmusIndividualCandidacyProcess process) {
	setIndividualCandidacyProcess(process);
	setSelectedDegree(process.getCandidacySelectedDegree());
	setSelectedCurricularCourses(new HashSet<CurricularCourse>(process.getCandidacy().getCurricularCoursesSet()));
	setErasmusStudentDataBean(new ErasmusStudentDataBean(process.getCandidacy().getErasmusStudentData()));
	setCandidacyDate(process.getCandidacyDate());

	setValidatedByErasmusCoordinator(process.getValidatedByErasmusCoordinator());
	setValidatedByGri(process.getValidatedByGri());
    }

    @Override
    protected void initializeDocumentUploadBeans() {
	setPhotoDocument(new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.PHOTO));
    }

    public Degree getSelectedDegree() {
	return selectedDegree;
    }

    public void setSelectedDegree(Degree selectedDegree) {
	this.selectedDegree = selectedDegree;
    }

    public Set<CurricularCourse> getSelectedCurricularCourses() {
	return selectedCurricularCourses;
    }

    public void setSelectedCurricularCourses(Set<CurricularCourse> selectedCurricularCourses) {
	this.selectedCurricularCourses = selectedCurricularCourses;
    }

    public ErasmusStudentDataBean getErasmusStudentDataBean() {
	return erasmusStudentDataBean;
    }

    public void setErasmusStudentDataBean(ErasmusStudentDataBean erasmusStudentDataBean) {
	this.erasmusStudentDataBean = erasmusStudentDataBean;
    }

    public void addCurricularCourse(final CurricularCourse curricularCourse) {
	for (CurricularCourse course : this.getSelectedCurricularCourses()) {
	    if (curricularCourse.isEquivalent(course))
		return;
	}

	this.getSelectedCurricularCourses().add(curricularCourse);
    }

    public void removeCurricularCourse(final CurricularCourse curricularCourse) {
	this.getSelectedCurricularCourses().remove(curricularCourse);
    }

    public List<CurricularCourse> getSortedSelectedCurricularCourses() {
	List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>(this.getSelectedCurricularCourses());
	Collections.sort(curricularCourses, CurricularCourse.COMPARATOR_BY_NAME);
	return curricularCourses;
    }

    public boolean isToAccessFenix() {
	return this.toAccessFenix;
    }

    public void willAccessFenix() {
	this.toAccessFenix = true;
    }

    public Boolean getValidatedByGri() {
	return validatedByGri;
    }

    public void setValidatedByGri(Boolean validatedByGri) {
	this.validatedByGri = validatedByGri;
    }

    public Boolean getValidatedByErasmusCoordinator() {
	return validatedByErasmusCoordinator;
    }

    public void setValidatedByErasmusCoordinator(Boolean validatedByErasmusCoordinator) {
	this.validatedByErasmusCoordinator = validatedByErasmusCoordinator;
    }

    public String getAlertSubject() {
	return alertSubject;
    }

    public void setAlertSubject(String alertSubject) {
	this.alertSubject = alertSubject;
    }

    public String getAlertBody() {
	return alertBody;
    }

    public void setAlertBody(String alertBody) {
	this.alertBody = alertBody;
    }

    public Boolean getCreateAlert() {
	return createAlert;
    }

    public void setCreateAlert(Boolean createAlert) {
	this.createAlert = createAlert;
    }

    public Boolean getSendEmail() {
	return sendEmail;
    }

    public void setSendEmail(Boolean sendEmail) {
	this.sendEmail = sendEmail;
    }

}
