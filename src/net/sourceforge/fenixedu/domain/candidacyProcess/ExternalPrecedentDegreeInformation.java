package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.LocalDate;

public class ExternalPrecedentDegreeInformation extends ExternalPrecedentDegreeInformation_Base {

    ExternalPrecedentDegreeInformation() {
	super();
    }

    public ExternalPrecedentDegreeInformation(final IndividualCandidacy candidacy, final String degreeDesignation,
	    final LocalDate conclusionDate, final Unit institution, final String conclusionGrade, Country country) {
	this();
	checkParameters(candidacy, degreeDesignation, institution, conclusionGrade);
	setCandidacy(candidacy);
	setDegreeDesignation(degreeDesignation);
	setConclusionDate(conclusionDate);
	setInstitution(institution);
	setConclusionGrade(conclusionGrade);
	setCountry(country);
    }

    private void checkParameters(final IndividualCandidacy candidacy, final String degreeDesignation, final Unit institution,
	    final String conclusionGrade) {

	if (candidacy == null) {
	    throw new DomainException("error.ExternalPrecedentDegreeInformation.invalid.candidacy");
	}

	if (degreeDesignation == null || degreeDesignation.length() == 0) {
	    throw new DomainException("error.ExternalPrecedentDegreeInformation.invalid.degreeDesignation");
	}

	if (institution == null) {
	    throw new DomainException("error.ExternalPrecedentDegreeInformation.invalid.institution");
	}
    }

    @Override
    public boolean isExternal() {
	return true;
    }

    @Override
    public void edit(final CandidacyPrecedentDegreeInformationBean bean) {
	checkParameters(getCandidacy(), bean.getDegreeDesignation(), bean.getInstitution(), bean.getConclusionGrade());
	
	setDegreeDesignation(bean.getDegreeDesignation());
	setConclusionDate(bean.getConclusionDate());
	setInstitution(bean.getInstitution());
	setConclusionGrade(bean.getConclusionGrade());
    }

    public void init(final Integer numberOfApprovedCurricularCourses, final BigDecimal gradeSum, final BigDecimal approvedEcts,
	    final BigDecimal enroledEcts) {
	checkParameters(numberOfApprovedCurricularCourses, gradeSum, approvedEcts, enroledEcts);
	setNumberOfApprovedCurricularCourses(numberOfApprovedCurricularCourses);
	setGradeSum(gradeSum);
	setApprovedEcts(approvedEcts);
	setEnroledEcts(enroledEcts);
    }

    private void checkParameters(final Integer numberOfApprovedCurricularCourses, final BigDecimal gradeSum,
	    final BigDecimal approvedEcts, final BigDecimal enroledEcts) {
	if (numberOfApprovedCurricularCourses != null && numberOfApprovedCurricularCourses.intValue() == 0) {
	    throw new DomainException("error.ExternalPrecedentDegreeInformation.invalid.numberOfApprovedCurricularCourses");
	}
	checkBigDecimal(gradeSum, "gradeSum");
	checkBigDecimal(approvedEcts, "approvedEcts");
	checkBigDecimal(enroledEcts, "enroledEcts");
    }

    private void checkBigDecimal(final BigDecimal value, final String property) {
	if (value != null && value.signum() == 0) {
	    throw new DomainException("error.ExternalPrecedentDegreeInformation.invalid." + property);
	}
    }

    @Override
    public void editCurricularCoursesInformation(final CandidacyPrecedentDegreeInformationBean information) {
	init(information.getNumberOfApprovedCurricularCourses(), information.getGradeSum(), information.getApprovedEcts(),
		information.getEnroledEcts());
    }

    @Override
    public void fill(CandidacyInformationBean bean) {
	super.fill(bean);
	bean.setCountryWhereFinishedPrecedentDegree(getCountry());
    }

    @Override
    public void edit(final CandidacyInformationBean bean) {
	super.edit(bean);
	
	setConclusionYear(bean.getConclusionYear());
	setConclusionGrade(bean.getConclusionGrade());
	setDegreeDesignation(bean.getDegreeDesignation());
	setInstitution(getOrCreateInstitution(bean));
	setCountry(bean.getCountryWhereFinishedPrecedentDegree());
    }

    @Override
    public void editMissingInformation(final CandidacyInformationBean bean) {
	super.editMissingInformation(bean);

	setConclusionYear(hasConclusionYear() ? getConclusionYear() : bean.getConclusionYear());
	setConclusionGrade(hasConclusionGrade() ? getConclusionGrade() : bean.getConclusionGrade());
	setDegreeDesignation(hasDegreeDesignation() ? getDegreeDesignation() : bean.getDegreeDesignation());
	setInstitution(hasInstitution() ? getInstitution() : getOrCreateInstitution(bean));
	setCountry(hasCountry() ? getCountry() : bean.getCountryWhereFinishedPrecedentDegree());
    }

    private Unit getOrCreateInstitution(final CandidacyInformationBean bean) {
	if (bean.getInstitution() != null) {
	    return bean.getInstitution();
	}

	if (bean.getInstitutionName() == null || bean.getInstitutionName().isEmpty()) {
	    throw new DomainException("error.ExternalPrecedentDegreeCandidacy.invalid.institution.name");
	}

	final Unit unit = Unit.findFirstExternalUnitByName(bean.getInstitutionName());
	return (unit != null) ? unit : Unit.createNewNoOfficialExternalInstitution(bean.getInstitutionName());
    }

    @Override
    public Integer getConclusionYear() {
	if (super.getConclusionYear() != null) {
	    return super.getConclusionYear();
	}
	return hasConclusionDate() ? getConclusionDate().getYear() : null;
    }

    private boolean hasConclusionYear() {
	return getConclusionYear() != null;
    }

    private boolean hasConclusionGrade() {
	return getConclusionGrade() != null && !getConclusionGrade().isEmpty();
    }

    private boolean hasDegreeDesignation() {
	return getDegreeDesignation() != null && !getDegreeDesignation().isEmpty();
    }

}
