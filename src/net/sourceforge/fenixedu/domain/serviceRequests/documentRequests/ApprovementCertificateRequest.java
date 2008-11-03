package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;

import org.joda.time.DateTime;

public class ApprovementCertificateRequest extends ApprovementCertificateRequest_Base {

    protected ApprovementCertificateRequest() {
	super();
    }

    public ApprovementCertificateRequest(final DocumentRequestCreateBean bean) {
	this();
	super.init(bean);

	checkParameters(bean);
	super.setMobilityProgram(bean.getMobilityProgram());
    }

    @Override
    final protected void checkParameters(final DocumentRequestCreateBean bean) {
	if (bean.getMobilityProgram() == null && hasAnyExternalEntriesToReport()) {
	    throw new DomainException("ApprovementCertificateRequest.mobility.program.cannot.be.null");
	}
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
	super.internalChangeState(academicServiceRequestBean);

	if (academicServiceRequestBean.isToProcess()) {

	    if (getEntriesToReport().isEmpty()) {
		throw new DomainException("ApprovementCertificateRequest.registration.without.approvements");
	    }

	    if (getRegistration().isConcluded()) {
		throw new DomainException("ApprovementCertificateRequest.registration.is.concluded");
	    }

	    if (getMobilityProgram() == null && hasAnyExternalEntriesToReport()) {
		throw new DomainException("ApprovementCertificateRequest.mobility.program.cannot.be.null");
	    }

	    // FIXME For now, the following conditions are only valid for 5year
	    // degrees
	    if (getRegistration().getDegreeType().getYears() == 5 && getDocumentPurposeType() == DocumentPurposeType.PROFESSIONAL) {

		int curricularYear = getRegistration().getCurricularYear();

		if (curricularYear <= 3) {
		    throw new DomainException("ApprovementCertificateRequest.registration.hasnt.finished.third.year");
		}
	    }
	}

	if (academicServiceRequestBean.isToConclude()) {
	    super.setNumberOfUnits(calculateNumberOfUnits());
	}
    }

    @Override
    final public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.APPROVEMENT_CERTIFICATE;
    }

    @Override
    final public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    final public EventType getEventType() {
	return EventType.APPROVEMENT_CERTIFICATE_REQUEST;
    }

    @Override
    final public Integer getNumberOfUnits() {
	final Integer res = super.getNumberOfUnits();
	return res == null ? calculateNumberOfUnits() : res;
    }

    private int calculateNumberOfUnits() {
	return getEntriesToReport().size() + getExtraCurricularEntriesToReport().size() + getPropaedeuticEntriesToReport().size();
    }

    @Override
    final public void setNumberOfUnits(final Integer numberOfUnits) {
	throw new DomainException("error.ApprovementCertificateRequest.cannot.modify.numberOfUnits");
    }

    @Override
    public boolean isToPrint() {
	final Integer units = super.getNumberOfUnits();
	return !hasConcluded() || (units != null && units.intValue() == calculateNumberOfUnits());
    }

    final private Collection<ICurriculumEntry> getEntriesToReport() {
	final HashSet<ICurriculumEntry> result = new HashSet<ICurriculumEntry>();

	final Registration registration = getRegistration();
	ICurriculum curriculum;
	if (registration.isBolonha()) {
	    for (final CycleCurriculumGroup cycle : registration.getLastStudentCurricularPlan().getInternalCycleCurriculumGrops()) {
		if (cycle.hasAnyApprovedCurriculumLines() && !cycle.isConclusionProcessed()) {
		    curriculum = cycle.getCurriculum(getFilteringDate());
		    filterEntries(result, curriculum);
		}
	    }
	} else {
	    curriculum = getRegistration().getCurriculum(getFilteringDate());
	    filterEntries(result, curriculum);
	}

	return result;
    }

    public DateTime getFilteringDate() {
	return hasConcluded() ? getRequestConclusionDate() : new DateTime();
    }

    static final public void filterEntries(final Collection<ICurriculumEntry> result, final ICurriculum curriculum) {

	for (final ICurriculumEntry entry : curriculum.getCurriculumEntries()) {
	    if (entry instanceof Dismissal) {
		final Dismissal dismissal = (Dismissal) entry;
		if (dismissal.getCredits().isEquivalence()
			|| (dismissal.isCreditsDismissal() && !dismissal.getCredits().isSubstitution())) {
		    continue;
		}
	    } else if (entry instanceof ExternalEnrolment) {
		final ExternalEnrolment externalEnrolment = (ExternalEnrolment) entry;
		if (externalEnrolment.hasExecutionPeriod() && !externalEnrolment.isResultOfMobility()) {
		    continue;
		}
	    }

	    result.add(entry);
	}
    }

    final public Collection<ICurriculumEntry> getExtraCurricularEntriesToReport() {
	final Collection<ICurriculumEntry> result = new HashSet<ICurriculumEntry>();

	for (final CurriculumLine curriculumLine : getRegistration().getExtraCurricularCurriculumLines()) {
	    if (curriculumLine.isApproved()) {
		if (curriculumLine.isEnrolment()) {
		    result.add((IEnrolment) curriculumLine);
		} else if (curriculumLine.isDismissal() && ((Dismissal) curriculumLine).getCredits().isSubstitution()) {
		    result.addAll(((Dismissal) curriculumLine).getSourceIEnrolments());
		}
	    }
	}

	for (final ExternalCurriculumGroup group : getRegistration().getLastStudentCurricularPlan().getExternalCurriculumGroups()) {
	    filterEntries(result, group.getCurriculumInAdvance(getFilteringDate()));
	}

	return result;
    }

    final public Collection<ICurriculumEntry> getPropaedeuticEntriesToReport() {
	final Collection<ICurriculumEntry> result = new HashSet<ICurriculumEntry>();

	// TODO report propaedeutic curriculum lines identically to extra
	// curricular ones
	// must run migration script first!! talk to lmre and naat
	result.addAll(getRegistration().getPropaedeuticEnrolments());

	return result;
    }

    final public boolean hasAnyExternalEntriesToReport() {
	for (final ICurriculumEntry entry : getEntriesToReport()) {
	    if (entry instanceof ExternalEnrolment) {
		return true;
	    }
	}

	for (final ICurriculumEntry entry : getExtraCurricularEntriesToReport()) {
	    if (entry instanceof ExternalEnrolment) {
		return true;
	    }
	}

	for (final ICurriculumEntry entry : getPropaedeuticEntriesToReport()) {
	    if (entry instanceof ExternalEnrolment) {
		return true;
	    }
	}

	return false;
    }

    @Override
    public boolean hasPersonalInfo() {
	return true;
    }

}
