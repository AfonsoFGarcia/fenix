package net.sourceforge.fenixedu.domain.administrativeOffice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.EmptyDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.PermissionType;
import net.sourceforge.fenixedu.domain.accessControl.academicAdminOffice.AdministrativeOfficePermission;
import net.sourceforge.fenixedu.domain.accessControl.academicAdminOffice.AdministrativeOfficePermissionGroup;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.AdministrativeOfficeUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestYear;
import net.sourceforge.fenixedu.domain.serviceRequests.RectorateSubmissionBatch;
import net.sourceforge.fenixedu.domain.serviceRequests.RectorateSubmissionState;
import net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.space.Campus;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class AdministrativeOffice extends AdministrativeOffice_Base {

    private AdministrativeOffice() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public AdministrativeOffice(AdministrativeOfficeType administrativeOfficeType) {
	this();
	init(administrativeOfficeType);
    }

    private void checkParameters(AdministrativeOfficeType administrativeOfficeType) {
	if (administrativeOfficeType == null) {
	    throw new DomainException("error.administrativeOffice.AdministrativeOffice.administrativeOfficeType.cannot.be.null");
	}
	checkIfExistsAdministrativeOfficeForType(administrativeOfficeType);
    }

    private void checkIfExistsAdministrativeOfficeForType(AdministrativeOfficeType administrativeOfficeType) {
	for (final AdministrativeOffice administrativeOffice : RootDomainObject.getInstance().getAdministrativeOffices()) {
	    if (administrativeOffice.getAdministrativeOfficeType() == administrativeOfficeType) {
		throw new DomainException(
			"error.administrativeOffice.AdministrativeOffice.already.exists.with.administrativeOfficeType");
	    }
	}
    }

    protected void init(AdministrativeOfficeType administrativeOfficeType) {
	checkParameters(administrativeOfficeType);
	super.setAdministrativeOfficeType(administrativeOfficeType);
    }

    @Override
    public void setAdministrativeOfficeType(AdministrativeOfficeType administrativeOfficeType) {
	throw new DomainException("error.administrativeOffice.AdministrativeOffice.cannot.modify.administrativeOfficeType");
    }

    @Override
    public void setUnit(AdministrativeOfficeUnit unit) {
	throw new DomainException("error.administrativeOffice.AdministrativeOffice.cannot.modify.unit");
    }

    public Collection<AcademicServiceRequest> searchRegistrationAcademicServiceRequests(final Integer year,
	    final AcademicServiceRequestSituationType situationType, final Campus campus) {

	final Collection<AcademicServiceRequest> result = new HashSet<AcademicServiceRequest>();

	for (final AcademicServiceRequest request : filterRequests(year)) {
	    if (year != null && request.getAdministrativeOffice() != this) {
		continue;
	    }

	    if (!request.isRequestForRegistration()) {
		continue;
	    }

	    final RegistrationAcademicServiceRequest registrationRequest = (RegistrationAcademicServiceRequest) request;

	    if (situationType != null && registrationRequest.getAcademicServiceRequestSituationType() != situationType) {
		continue;
	    }

	    if (campus != null && registrationRequest.isDocumentRequest()) {
		final DocumentRequest documentRequest = (DocumentRequest) registrationRequest;
		final Campus documentCampus = documentRequest.getCampus();

		if (documentCampus != null && !documentCampus.equals(campus)) {
		    continue;
		}
	    }

	    result.add(registrationRequest);
	}

	return result;
    }

    public Collection<AcademicServiceRequest> searchRegistrationAcademicServiceRequests(final LocalDate begin,
	    final LocalDate end, final AcademicServiceRequestSituationType situationType, final Campus campus) {

	if (end == null) {
	    throw new DomainException("error.AdministrativeOffice.no.endDate");
	}
	if (end.isBefore(begin)) {
	    throw new DomainException("error.AdministrativeOffice.end.before.begin");
	}
	if (situationType == null) {
	    throw new DomainException("error.AdministrativeOffice.empty.situationType");
	}

	final Collection<AcademicServiceRequest> toInspect = new HashSet<AcademicServiceRequest>();
	final int beginYear = ExecutionYear.readByDateTime(begin).getBeginDateYearMonthDay().getYear();
	final int endYear = ExecutionYear.readByDateTime(end).getEndDateYearMonthDay().getYear();
	for (int iter = beginYear; iter <= endYear; iter++) {
	    toInspect.addAll(filterRequests(iter));
	}

	final Collection<AcademicServiceRequest> result = new HashSet<AcademicServiceRequest>();

	for (final AcademicServiceRequest request : toInspect) {
	    if (!request.isRequestForRegistration()) {
		continue;
	    }

	    final RegistrationAcademicServiceRequest registrationRequest = (RegistrationAcademicServiceRequest) request;

	    if (situationType != null && registrationRequest.getAcademicServiceRequestSituationType() != situationType) {
		continue;
	    }

	    final LocalDate date = registrationRequest.getSituationByType(situationType).getSituationDate().toLocalDate();
	    if (begin.isAfter(date) || end.isBefore(date)) {
		continue;
	    }

	    if (campus != null && registrationRequest.isDocumentRequest()) {
		final DocumentRequest documentRequest = (DocumentRequest) registrationRequest;
		final Campus documentCampus = documentRequest.getCampus();

		if (documentCampus != null && !documentCampus.equals(campus)) {
		    continue;
		}
	    }

	    result.add(registrationRequest);
	}

	return result;
    }

    private Collection<AcademicServiceRequest> filterRequests(final Integer year) {
	if (year == null) {
	    return getAcademicServiceRequestsSet();
	} else {
	    AcademicServiceRequestYear readByYear = AcademicServiceRequestYear.readByYear(year.intValue());
	    return readByYear == null ? Collections.EMPTY_SET : readByYear.getAcademicServiceRequestsSet();
	}
    }

    public List<AcademicServiceRequest> getNewAcademicServiceRequests() {

	final List<AcademicServiceRequest> result = new ArrayList<AcademicServiceRequest>();
	for (final AcademicServiceRequest academicServiceRequest : getAcademicServiceRequests()) {
	    if (academicServiceRequest.isNewRequest()) {
		result.add(academicServiceRequest);
	    }
	}
	return result;
    }

    // static methods
    public static AdministrativeOffice readByAdministrativeOfficeType(AdministrativeOfficeType administrativeOfficeType) {

	for (final AdministrativeOffice administrativeOffice : RootDomainObject.getInstance().getAdministrativeOffices()) {

	    if (administrativeOffice.getAdministrativeOfficeType() == administrativeOfficeType) {
		return administrativeOffice;
	    }

	}
	return null;

    }

    public static AdministrativeOffice getResponsibleAdministrativeOffice(Degree degree) {
	return readByAdministrativeOfficeType(degree.getDegreeType().getAdministrativeOfficeType());
    }

    public Collection<DegreeType> getAdministratedDegreeTypes() {
	Collection<DegreeType> result = new HashSet<DegreeType>();

	for (final DegreeType degreeType : DegreeType.NOT_EMPTY_VALUES) {
	    if (degreeType.getAdministrativeOfficeType() == getAdministrativeOfficeType()) {
		result.add(degreeType);
	    }
	}

	return result;
    }

    public Collection<CycleType> getAdministratedCycleTypes() {
	Collection<CycleType> result = new HashSet<CycleType>();

	for (final DegreeType degreeType : getAdministratedDegreeTypes()) {
	    result.addAll(degreeType.getCycleTypes());
	}

	return result;
    }

    public Set<Degree> getAdministratedDegrees() {
	final Set<Degree> result = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
	for (Degree degree : Degree.readNotEmptyDegrees()) {
	    final DegreeType degreeType = degree.getDegreeType();
	    if (degreeType.getAdministrativeOfficeType().equals(this.getAdministrativeOfficeType())) {
		result.add(degree);
	    }
	}

	return result;
    }

    public Set<Degree> getAdministratedDegreesForMarkSheets() {
	final Set<Degree> result = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
	for (Degree degree : Degree.readNotEmptyDegrees()) {
	    final DegreeType degreeType = degree.getDegreeType();
	    if (degreeType.getAdministrativeOfficeType().equals(this.getAdministrativeOfficeType())
		    && !degreeType.equals(DegreeType.MASTER_DEGREE)
		    && !degreeType.equals(DegreeType.BOLONHA_SPECIALIZATION_DEGREE)) {
		result.add(degree);
	    }
	}

	return result;
    }

    public List<Degree> getAdministratedDegreesForStudentCreationWithoutCandidacy() {
	final List<Degree> result = new ArrayList<Degree>();
	for (Degree degree : Degree.readNotEmptyDegrees()) {
	    final DegreeType degreeType = degree.getDegreeType();
	    if (degreeType.getAdministrativeOfficeType().equals(this.getAdministrativeOfficeType())
		    && degreeType.canCreateStudent() && !degreeType.canCreateStudentOnlyWithCandidacy()) {
		result.add(degree);
	    }
	}

	final EmptyDegree emptyDegree = EmptyDegree.getInstance();
	if (emptyDegree.getDegreeType().getAdministrativeOfficeType().getAdministrativeOffice() == this) {
	    result.add(emptyDegree);
	}

	Collections.sort(result, Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);

	return result;
    }

    public void delete() {
	checkRulesToDelete();

	removeUnit();
	removeServiceAgreementTemplate();
	removeRootDomainObject();
	deleteDomainObject();
    }

    private void checkRulesToDelete() {
	if (hasAnyAcademicServiceRequests()) {
	    throw new DomainException("error.AdministrativeOffice.cannot.delete");
	}
    }

    public boolean isDegree() {
	return getAdministrativeOfficeType().equals(AdministrativeOfficeType.DEGREE);
    }

    public boolean isMasterDegree() {
	return getAdministrativeOfficeType() == AdministrativeOfficeType.MASTER_DEGREE;
    }

    private AdministrativeOfficePermissionGroup getAdministrativeOfficePermissionGroup(final Campus campus) {
	for (final AdministrativeOfficePermissionGroup group : getAdministrativeOfficePermissionGroupsSet()) {
	    if (group.hasCampus(campus)) {
		return group;
	    }
	}
	return null;
    }

    private boolean hasAdministrativeOfficePermissionGroup(final Campus campus) {
	return getAdministrativeOfficePermissionGroup(campus) != null;
    }

    public List<AdministrativeOfficePermission> getPermissions(final Campus campus) {
	return hasAdministrativeOfficePermissionGroup(campus) ? getAdministrativeOfficePermissionGroup(campus)
		.getAdministrativeOfficePermissions() : Collections.EMPTY_LIST;
    }

    public AdministrativeOfficePermission getPermission(final PermissionType permissionType, final Campus campus) {
	return hasAdministrativeOfficePermissionGroup(campus) ? getAdministrativeOfficePermissionGroup(campus).getPermission(
		permissionType) : null;
    }

    @Service
    @Checked("PermissionPredicates.CREATE_PERMISSION_MEMBERS_GROUP")
    public void createAdministrativeOfficePermission(final PermissionType permissionType, final Campus campus) {
	if (!hasAdministrativeOfficePermissionGroup(campus)) {
	    new AdministrativeOfficePermissionGroup(this, campus);
	}
	getAdministrativeOfficePermissionGroup(campus).createPermissionForType(permissionType);
    }

    public boolean hasPermission(final PermissionType permissionType, final Campus campus) {
	return hasAdministrativeOfficePermissionGroup(campus)
		&& getAdministrativeOfficePermissionGroup(campus).hasPermission(permissionType);
    }

    public RectorateSubmissionBatch getCurrentRectorateSubmissionBatch() {
	DateTime last = null;
	RectorateSubmissionBatch current = null;
	for (RectorateSubmissionBatch bag : getRectorateSubmissionBatchSet()) {
	    if (last == null || bag.getCreation().isAfter(last)) {
		last = bag.getCreation();
		current = bag;
	    }
	}
	return current;
    }

    public Set<RectorateSubmissionBatch> getRectorateSubmissionBatchesByState(RectorateSubmissionState state) {
	Set<RectorateSubmissionBatch> result = new HashSet<RectorateSubmissionBatch>();
	for (RectorateSubmissionBatch batch : getRectorateSubmissionBatchSet()) {
	    if (batch.getState().equals(state)) {
		result.add(batch);
	    }
	}
	return result;
    }
}
