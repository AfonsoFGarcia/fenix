package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;

public abstract class AcademicServiceRequest extends AcademicServiceRequest_Base {

    protected AcademicServiceRequest() {
        super();
        super.setRootDomainObject(RootDomainObject.getInstance());
        super.setOjbConcreteClass(this.getClass().getName());
        super.setCreationDate(new DateTime());
    }

    protected AcademicServiceRequest(StudentCurricularPlan studentCurricularPlan,
            AdministrativeOffice administrativeOffice) {
        this();
        init(studentCurricularPlan, administrativeOffice);
    }

    private void checkParameters(StudentCurricularPlan studentCurricularPlan,
            AdministrativeOffice administrativeOffice) {
        if (studentCurricularPlan == null) {
            throw new DomainException(
                    "error.serviceRequests.AcademicServiceRequest.studentCurricularPlan.cannot.be.null");
        }
        if (administrativeOffice == null) {
            throw new DomainException(
                    "error.serviceRequests.AcademicServiceRequest.administrativeOffice.cannot.be.null");
        }

    }

    protected void init(StudentCurricularPlan studentCurricularPlan,
            AdministrativeOffice administrativeOffice) {
        checkParameters(studentCurricularPlan, administrativeOffice);
        super.setAdministrativeOffice(administrativeOffice);
        super.setStudentCurricularPlan(studentCurricularPlan);
    }

    @Override
    public void setAdministrativeOffice(AdministrativeOffice administrativeOffice) {
        throw new DomainException(
                "error.serviceRequests.AcademicServiceRequest.cannot.modify.administrativeOffice");
    }

    @Override
    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
        throw new DomainException(
                "error.serviceRequests.AcademicServiceRequest.cannot.modify.studentCurricularPlan");
    }

    @Override
    public void setCreationDate(DateTime creationDate) {
        throw new DomainException(
                "error.serviceRequests.AcademicServiceRequest.cannot.modify.creationDate");
    }

    @Override
    public void addAcademicServiceRequestSituations(
            AcademicServiceRequestSituation academicServiceRequestSituation) {
        throw new DomainException(
                "error.serviceRequests.AcademicServiceRequest.cannot.add.academicServiceRequestSituation");
    }

    @Override
    public List<AcademicServiceRequestSituation> getAcademicServiceRequestSituations() {
        return Collections.unmodifiableList(super.getAcademicServiceRequestSituations());
    }

    @Override
    public Set<AcademicServiceRequestSituation> getAcademicServiceRequestSituationsSet() {
        return Collections.unmodifiableSet(super.getAcademicServiceRequestSituationsSet());
    }

    @Override
    public Iterator<AcademicServiceRequestSituation> getAcademicServiceRequestSituationsIterator() {
        return getAcademicServiceRequestSituationsSet().iterator();
    }

    @Override
    public void removeAcademicServiceRequestSituations(
            AcademicServiceRequestSituation academicServiceRequestSituation) {
        throw new DomainException(
                "error.serviceRequests.AcademicServiceRequest.cannot.remove.academicServiceRequestSituation");
    }

    public boolean isNewRequest() {
        return !hasAnyAcademicServiceRequestSituations();
    }

    public AcademicServiceRequestSituation createSituation(
            AcademicServiceRequestSituationType academicServiceRequestSituationType, Employee employee) {
        return new AcademicServiceRequestSituation(this, academicServiceRequestSituationType, employee);
    }

    public AcademicServiceRequestSituation getActiveSituation() {
        return (!getAcademicServiceRequestSituations().isEmpty()) ? (AcademicServiceRequestSituation) Collections
                .max(getAcademicServiceRequestSituations(), new BeanComparator("creationDate"))
                : null;
    }

    public AcademicServiceRequestSituationType getAcademicServiceRequestSituationType() {
        return (getActiveSituation() != null) ? getActiveSituation()
                .getAcademicServiceRequestSituationType() : null;
    }

    protected void edit(AcademicServiceRequestSituationType academicServiceRequestSituationType,
            Employee employee, String justification) {

        if (getAcademicServiceRequestSituationType() != academicServiceRequestSituationType) {
            checkRulesToChangeState(academicServiceRequestSituationType);

            internalChangeState(academicServiceRequestSituationType);

            new AcademicServiceRequestSituation(this, academicServiceRequestSituationType, employee,
                    justification);
        } else {
            getActiveSituation().edit(employee, justification);
        }

    }

    private void checkRulesToChangeState(
            AcademicServiceRequestSituationType academicServiceRequestSituationType) {

        AcademicServiceRequestSituationType[] acceptedTypes;
        if (getActiveSituation() != null) {
            switch (getActiveSituation().getAcademicServiceRequestSituationType()) {
            case PROCESSING:
                acceptedTypes = new AcademicServiceRequestSituationType[] {
                        AcademicServiceRequestSituationType.CANCELLED,
                        AcademicServiceRequestSituationType.REJECTED,
                        AcademicServiceRequestSituationType.CONCLUDED };
                break;
            case CONCLUDED:
                acceptedTypes = new AcademicServiceRequestSituationType[] { AcademicServiceRequestSituationType.DELIVERED };
                break;
            default:
                acceptedTypes = new AcademicServiceRequestSituationType[] {};
                break;
            }

            if (!Arrays.asList(acceptedTypes).contains(academicServiceRequestSituationType)) {
                final LabelFormatter sourceLabelFormatter = new LabelFormatter().appendLabel(
                        getActiveSituation().getAcademicServiceRequestSituationType().name(), "enum");
                final LabelFormatter targetLabelFormatter = new LabelFormatter().appendLabel(
                        academicServiceRequestSituationType.name(), "enum");

                throw new DomainExceptionWithLabelFormatter(
                        "error.serviceRequests.AcademicServiceRequest.cannot.change.from.source.state.to.target.state",
                        sourceLabelFormatter, targetLabelFormatter);
            }
        } else {
            acceptedTypes = new AcademicServiceRequestSituationType[] {
                    AcademicServiceRequestSituationType.CANCELLED,
                    AcademicServiceRequestSituationType.PROCESSING };
            if (!Arrays.asList(acceptedTypes).contains(academicServiceRequestSituationType)) {
                final LabelFormatter labelFormatter = new LabelFormatter().appendLabel(
                        academicServiceRequestSituationType.name(), "enum");

                throw new DomainExceptionWithLabelFormatter(
                        "error.serviceRequests.AcademicServiceRequest.cannot.change.state.",
                        labelFormatter);

            }
        }

    }

    protected void internalChangeState(
            AcademicServiceRequestSituationType academicServiceRequestSituationType) {
        // nothing to be done
    }

    public Student getStudent() {
        return getStudentCurricularPlan().getStudent();
    }

}
