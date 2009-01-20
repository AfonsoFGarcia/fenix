package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.MobilityProgram;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

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
	super.setIgnoreExternalEntries(bean.isIgnoreExternalEntries());

	if (isEmployee()) {
	    if (getEntriesToReport().isEmpty()) {
		throw new DomainException("ApprovementCertificateRequest.registration.without.approvements");
	    }

	    if (getRegistration().isConcluded()) {
		throw new DomainException("ApprovementCertificateRequest.registration.is.concluded");
	    }
	}
    }

    private boolean isEmployee() {
	final Person person = AccessControl.getPerson();
	return person != null && person.hasEmployee();
    }

    @Override
    final protected void checkParameters(final DocumentRequestCreateBean bean) {
	if (bean.getMobilityProgram() != null && bean.isIgnoreExternalEntries()) {
	    throw new DomainException("ApprovementCertificateRequest.cannot.ignore.external.entries.within.a.mobility.program");
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
    public void setMobilityProgram(MobilityProgram mobilityProgram) {
	throw new DomainException("error.ApprovementCertificateRequest.cannot.modify");
    }

    @Override
    public void setIgnoreExternalEntries(Boolean ignoreExternalEntries) {
	throw new DomainException("error.ApprovementCertificateRequest.cannot.modify");
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
		    filterEntries(result, this, curriculum);
		}
	    }
	} else {
	    curriculum = getRegistration().getCurriculum(getFilteringDate());
	    filterEntries(result, this, curriculum);
	}

	return result;
    }

    public DateTime getFilteringDate() {
	return hasConcluded() ? getRequestConclusionDate() : new DateTime();
    }

    static final public void filterEntries(final Collection<ICurriculumEntry> result,
	    final ApprovementCertificateRequest request, final ICurriculum curriculum) {
	for (final ICurriculumEntry entry : curriculum.getCurriculumEntries()) {
	    if (entry instanceof Dismissal) {
		final Dismissal dismissal = (Dismissal) entry;
		if (dismissal.getCredits().isEquivalence()
			|| (dismissal.isCreditsDismissal() && !dismissal.getCredits().isSubstitution())) {
		    continue;
		}
	    } else if (entry instanceof ExternalEnrolment && request.getIgnoreExternalEntries()) {
		continue;
	    }

	    result.add(entry);
	}
    }

    final public Collection<ICurriculumEntry> getExtraCurricularEntriesToReport() {
	final Collection<ICurriculumEntry> result = new HashSet<ICurriculumEntry>();

	reportApprovedCurriculumLines(result, getRegistration().getExtraCurricularCurriculumLines());
	reportExternalGroups(result);

	return result;
    }

    private void reportApprovedCurriculumLines(final Collection<ICurriculumEntry> result, final Collection<CurriculumLine> lines) {
	for (final CurriculumLine line : lines) {
	    if (line.isApproved()) {
		if (line.isEnrolment()) {
		    result.add((IEnrolment) line);
		} else if (line.isDismissal() && ((Dismissal) line).getCredits().isSubstitution()) {
		    result.addAll(((Dismissal) line).getSourceIEnrolments());
		}
	    }
	}
    }

    private void reportExternalGroups(final Collection<ICurriculumEntry> result) {
	for (final ExternalCurriculumGroup group : getRegistration().getLastStudentCurricularPlan().getExternalCurriculumGroups()) {
	    filterEntries(result, this, group.getCurriculumInAdvance(getFilteringDate()));
	}
    }

    final public Collection<ICurriculumEntry> getPropaedeuticEntriesToReport() {
	final Collection<ICurriculumEntry> result = new HashSet<ICurriculumEntry>();

	reportApprovedCurriculumLines(result, getRegistration().getPropaedeuticCurriculumLines());

	return result;
    }

    @Override
    public boolean hasPersonalInfo() {
	return true;
    }

}
