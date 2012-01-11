package net.sourceforge.fenixedu.domain.candidacy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DistrictSubdivision;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.domain.GrantOwnerType;
import net.sourceforge.fenixedu.domain.ProfessionType;
import net.sourceforge.fenixedu.domain.ProfessionalSituationConditionType;
import net.sourceforge.fenixedu.domain.SchoolLevelType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.raides.DegreeDesignation;
import net.sourceforge.fenixedu.domain.student.PersonalIngressionData;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.StringUtils;
import pt.ist.fenixWebFramework.services.Service;

public class PersonalInformationBean implements Serializable {

    static public Comparator<PersonalInformationBean> COMPARATOR_BY_DESCRIPTION = new Comparator<PersonalInformationBean>() {

	@Override
	public int compare(PersonalInformationBean o1, PersonalInformationBean o2) {
	    return o1.getDescription().compareTo(o2.getDescription());
	}
    };

    static private final long serialVersionUID = 1144682974757187722L;

    private PhdIndividualProgramProcess phdIndividualProgramProcess;

    private Registration registration;

    private Country countryOfResidence;

    private DistrictSubdivision districtSubdivisionOfResidence;

    private Boolean dislocatedFromPermanentResidence;

    private DistrictSubdivision schoolTimeDistrictSubdivisionOfResidence;

    private GrantOwnerType grantOwnerType;

    private Unit grantOwnerProvider;

    private String grantOwnerProviderName;

    private AcademicalInstitutionType highSchoolType;

    private MaritalStatus maritalStatus;

    private ProfessionType professionType;

    private ProfessionalSituationConditionType professionalCondition;

    private SchoolLevelType motherSchoolLevel;

    private ProfessionType motherProfessionType;

    private ProfessionalSituationConditionType motherProfessionalCondition;

    private SchoolLevelType fatherSchoolLevel;

    private ProfessionType fatherProfessionType;

    private ProfessionalSituationConditionType fatherProfessionalCondition;

    private String conclusionGrade;

    private Integer conclusionYear;

    private Unit institution;

    private String institutionName;

    private String degreeDesignation;

    private DegreeDesignation raidesDegreeDesignation;

    private Country countryWhereFinishedPrecedentDegree;

    private SchoolLevelType schoolLevel;

    private String otherSchoolLevel;

    private final Collection<File> documentFiles = new ArrayList<File>();

    public PersonalInformationBean(Registration registration) {
	setRegistration(registration);
    }

    public PersonalInformationBean(PhdIndividualProgramProcess PhdProcess) {
	setPhdIndividualProgramProcess(PhdProcess);
    }

    public PersonalInformationBean() {
    }

    public Registration getRegistration() {
	return this.registration;
    }

    public void setRegistration(Registration registration) {
	this.registration = registration;
    }

    public boolean hasRegistration() {
	return getRegistration() != null;
    }

    public PhdIndividualProgramProcess getPhdIndividualProgramProcess() {
	return phdIndividualProgramProcess;
    }

    public void setPhdIndividualProgramProcess(PhdIndividualProgramProcess phdIndividualProgramProcess) {
	this.phdIndividualProgramProcess = phdIndividualProgramProcess;
    }

    public boolean hasPhdIndividualProgramProcess() {
	return getPhdIndividualProgramProcess() != null;
    }

    public Country getCountryOfResidence() {
	return this.countryOfResidence;
    }

    public void setCountryOfResidence(Country country) {
	this.countryOfResidence = country;
    }

    public DistrictSubdivision getDistrictSubdivisionOfResidence() {
	return this.districtSubdivisionOfResidence;
    }

    public void setDistrictSubdivisionOfResidence(DistrictSubdivision districtSubdivision) {
	this.districtSubdivisionOfResidence = districtSubdivision;
    }

    public Boolean getDislocatedFromPermanentResidence() {
	return dislocatedFromPermanentResidence;
    }

    public void setDislocatedFromPermanentResidence(Boolean dislocatedFromPermanentResidence) {
	this.dislocatedFromPermanentResidence = dislocatedFromPermanentResidence;
    }

    public DistrictSubdivision getSchoolTimeDistrictSubdivisionOfResidence() {
	return this.schoolTimeDistrictSubdivisionOfResidence;
    }

    public void setSchoolTimeDistrictSubdivisionOfResidence(DistrictSubdivision districtSubdivision) {
	this.schoolTimeDistrictSubdivisionOfResidence = districtSubdivision;
    }

    public GrantOwnerType getGrantOwnerType() {
	return grantOwnerType;
    }

    public void setGrantOwnerType(GrantOwnerType grantOwnerType) {
	this.grantOwnerType = grantOwnerType;
    }

    public Unit getGrantOwnerProvider() {
	return this.grantOwnerProvider;
    }

    public void setGrantOwnerProvider(Unit grantOwnerProvider) {
	this.grantOwnerProvider = grantOwnerProvider;
    }

    public String getGrantOwnerProviderName() {
	return grantOwnerProviderName;
    }

    public void setGrantOwnerProviderName(String grantOwnerProviderName) {
	this.grantOwnerProviderName = grantOwnerProviderName;
    }

    public UnitName getGrantOwnerProviderUnitName() {
	return (grantOwnerProvider == null) ? null : grantOwnerProvider.getUnitName();
    }

    public void setGrantOwnerProviderUnitName(UnitName grantOwnerProviderUnitName) {
	this.grantOwnerProvider = (grantOwnerProviderUnitName == null) ? null : grantOwnerProviderUnitName.getUnit();
    }

    public AcademicalInstitutionType getHighSchoolType() {
	if ((getSchoolLevel() != null) && (getSchoolLevel().isHighSchoolOrEquivalent())) {
	    return highSchoolType;
	}
	return null;
    }

    public void setHighSchoolType(AcademicalInstitutionType highSchoolType) {
	this.highSchoolType = highSchoolType;
    }

    public MaritalStatus getMaritalStatus() {
	return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
	this.maritalStatus = (maritalStatus == MaritalStatus.UNKNOWN ? null : maritalStatus);
    }

    public boolean hasMaritalStatus() {
	return getMaritalStatus() != null;
    }

    public ProfessionType getProfessionType() {
	return professionType;
    }

    public void setProfessionType(ProfessionType professionType) {
	this.professionType = professionType;
    }

    public boolean hasProfessionType() {
	return getProfessionType() != null;
    }

    public ProfessionalSituationConditionType getProfessionalCondition() {
	return professionalCondition;
    }

    public void setProfessionalCondition(ProfessionalSituationConditionType professionalCondition) {
	this.professionalCondition = professionalCondition;
    }

    public boolean hasProfessionalCondition() {
	return getProfessionalCondition() != null;
    }

    public SchoolLevelType getMotherSchoolLevel() {
	return motherSchoolLevel;
    }

    public void setMotherSchoolLevel(SchoolLevelType motherSchoolLevel) {
	this.motherSchoolLevel = motherSchoolLevel;
    }

    public boolean hasMotherSchoolLevel() {
	return getMotherSchoolLevel() != null;
    }

    public ProfessionType getMotherProfessionType() {
	return motherProfessionType;
    }

    public void setMotherProfessionType(ProfessionType motherProfessionType) {
	this.motherProfessionType = motherProfessionType;
    }

    public boolean hasMotherProfessionType() {
	return getMotherProfessionType() != null;
    }

    public ProfessionalSituationConditionType getMotherProfessionalCondition() {
	return motherProfessionalCondition;
    }

    public void setMotherProfessionalCondition(ProfessionalSituationConditionType motherProfessionalCondition) {
	this.motherProfessionalCondition = motherProfessionalCondition;
    }

    public boolean hasMotherProfessionalCondition() {
	return getMotherProfessionalCondition() != null;
    }

    public SchoolLevelType getFatherSchoolLevel() {
	return fatherSchoolLevel;
    }

    public void setFatherSchoolLevel(SchoolLevelType fatherSchoolLevel) {
	this.fatherSchoolLevel = fatherSchoolLevel;
    }

    public boolean hasFatherSchoolLevel() {
	return getFatherSchoolLevel() != null;
    }

    public ProfessionType getFatherProfessionType() {
	return fatherProfessionType;
    }

    public void setFatherProfessionType(ProfessionType fatherProfessionType) {
	this.fatherProfessionType = fatherProfessionType;
    }

    public boolean hasFatherProfessionType() {
	return getFatherProfessionType() != null;
    }

    public ProfessionalSituationConditionType getFatherProfessionalCondition() {
	return fatherProfessionalCondition;
    }

    public void setFatherProfessionalCondition(ProfessionalSituationConditionType fatherProfessionalCondition) {
	this.fatherProfessionalCondition = fatherProfessionalCondition;
    }

    public boolean hasFatherProfessionalCondition() {
	return getFatherProfessionalCondition() != null;
    }

    public String getConclusionGrade() {
	return conclusionGrade;
    }

    public void setConclusionGrade(String conclusionGrade) {
	this.conclusionGrade = conclusionGrade;
    }

    public Integer getConclusionYear() {
	return conclusionYear;
    }

    public void setConclusionYear(Integer conclusionYear) {
	this.conclusionYear = conclusionYear;
    }

    public Unit getInstitution() {
	return this.institution;
    }

    public void setInstitution(Unit institution) {
	this.institution = institution;
    }

    public String getInstitutionName() {
	return institutionName;
    }

    public void setInstitutionName(String institutionName) {
	this.institutionName = institutionName;
    }

    public UnitName getInstitutionUnitName() {
	return (institution == null) ? null : institution.getUnitName();
    }

    public void setInstitutionUnitName(UnitName institutionUnitName) {
	this.institution = (institutionUnitName == null) ? null : institutionUnitName.getUnit();
    }

    public String getDegreeDesignation() {
	if (getSchoolLevel() != null) {
	    return getSchoolLevel().isHigherEducation() && getRaidesDegreeDesignation() != null ? getRaidesDegreeDesignation()
		    .getDescription() : degreeDesignation;
	}
	return degreeDesignation;
    }

    public void setDegreeDesignation(String degreeDesignation) {
	this.degreeDesignation = degreeDesignation;
    }

    public Country getCountryWhereFinishedPrecedentDegree() {
	return this.countryWhereFinishedPrecedentDegree;
    }

    public void setCountryWhereFinishedPrecedentDegree(Country country) {
	this.countryWhereFinishedPrecedentDegree = country;
    }

    public SchoolLevelType getSchoolLevel() {
	return schoolLevel;
    }

    public void setSchoolLevel(SchoolLevelType schoolLevel) {
	this.schoolLevel = schoolLevel;
    }

    public boolean hasSchoolLevel() {
	return getSchoolLevel() != null;
    }

    public String getOtherSchoolLevel() {
	return otherSchoolLevel;
    }

    public void setOtherSchoolLevel(String otherSchoolLevel) {
	this.otherSchoolLevel = otherSchoolLevel;
    }

    public Set<String> validate() {

	final Set<String> result = new HashSet<String>();

	if (getCountryOfResidence() == null || getGrantOwnerType() == null || getDislocatedFromPermanentResidence() == null
		|| !isSchoolLevelValid() || !isMaritalStatusValid() || !isProfessionalConditionValid()
		|| !isProfessionTypeValid() || !isMotherSchoolLevelValid() || !isMotherProfessionTypeValid()
		|| !isMotherProfessionalConditionValid() || !isFatherProfessionalConditionValid()
		|| !isFatherProfessionTypeValid() || !isFatherSchoolLevelValid()
		|| getCountryWhereFinishedPrecedentDegree() == null
		|| (getInstitution() == null && StringUtils.isEmpty(getInstitutionName()))) {
	    result.add("error.CandidacyInformationBean.required.information.must.be.filled");
	}

	if (getCountryOfResidence() != null) {
	    if (getCountryOfResidence().isDefaultCountry() && getDistrictSubdivisionOfResidence() == null) {
		result.add("error.CandidacyInformationBean.districtSubdivisionOfResidence.is.required.for.default.country");
	    }
	    if (!getCountryOfResidence().isDefaultCountry()
		    && (getDislocatedFromPermanentResidence() == null || !getDislocatedFromPermanentResidence())) {
		result.add("error.CandidacyInformationBean.foreign.students.must.select.dislocated.option");
	    }
	}

	if (getDislocatedFromPermanentResidence() != null && getDislocatedFromPermanentResidence()
		&& getSchoolTimeDistrictSubdivisionOfResidence() == null) {
	    result.add("error.CandidacyInformationBean.schoolTimeDistrictSubdivisionOfResidence.is.required.for.dislocated.students");
	}

	if (getSchoolLevel() != null && getSchoolLevel() == SchoolLevelType.OTHER && StringUtils.isEmpty(getOtherSchoolLevel())) {
	    result.add("error.CandidacyInformationBean.schoolTimeDistrictSubdivisionOfResidence.other.school.level.description.is.required");
	}

	if (getGrantOwnerType() != null && getGrantOwnerType() == GrantOwnerType.OTHER_INSTITUTION_GRANT_OWNER
		&& getGrantOwnerProvider() == null) {
	    result.add("error.CandidacyInformationBean.grantOwnerProviderInstitutionUnitName.is.required.for.other.institution.grant.ownership");
	}

	return result;

    }

    public boolean isMaritalStatusValid() {
	return hasMaritalStatus() && getMaritalStatus() != MaritalStatus.UNKNOWN;
    }

    public boolean isSchoolLevelValid() {
	return hasSchoolLevel() && getSchoolLevel().isForStudent();
    }

    public boolean isProfessionTypeValid() {
	return hasProfessionType() && getProfessionType().isActive();
    }

    public boolean isProfessionalConditionValid() {
	return hasProfessionalCondition() && getProfessionalCondition() != ProfessionalSituationConditionType.MILITARY_SERVICE;
    }

    public boolean isMotherProfessionTypeValid() {
	return hasMotherProfessionType() && getMotherProfessionType().isActive();
    }

    public boolean isMotherProfessionalConditionValid() {
	return hasMotherProfessionalCondition()
		&& getMotherProfessionalCondition() != ProfessionalSituationConditionType.MILITARY_SERVICE;
    }

    public boolean isMotherSchoolLevelValid() {
	return hasMotherSchoolLevel() && getMotherSchoolLevel().isForStudentHousehold();
    }

    public boolean isFatherProfessionTypeValid() {
	return hasFatherProfessionType() && getFatherProfessionType().isActive();
    }

    public boolean isFatherProfessionalConditionValid() {
	return hasFatherProfessionalCondition()
		&& getFatherProfessionalCondition() != ProfessionalSituationConditionType.MILITARY_SERVICE;
    }

    public boolean isFatherSchoolLevelValid() {
	return hasFatherSchoolLevel() && getFatherSchoolLevel().isForStudentHousehold();
    }

    public boolean isValid() {
	return validate().isEmpty();
    }

    public void addDocumentFile(final File file) {
	documentFiles.add(file);
    }

    public Collection<File> getDocumentFiles() {
	final Collection<File> result = new ArrayList<File>();
	for (final File file : documentFiles) {
	    result.add(file);
	}
	return result;
    }

    public String getFormattedValues() {
	Formatter result = new Formatter();

	final Student student = getStudent();
	result.format("Student Number: %d\n", student.getNumber());
	result.format("Name: %s\n", student.getPerson().getName());
	if (hasPhdIndividualProgramProcess()) {
	    result.format("Degree: %s\n", getPhdIndividualProgramProcess().getDisplayName());
	} else {
	    result.format("Degree: %s\n", getRegistration().getDegree().getPresentationName());
	}

	return result.toString();
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}

	if (!(obj instanceof PersonalInformationBean)) {
	    return false;
	}

	final PersonalInformationBean other = (PersonalInformationBean) obj;

	if (hasPhdIndividualProgramProcess() && other.hasPhdIndividualProgramProcess()) {
	    return getPhdIndividualProgramProcess().equals(other.getPhdIndividualProgramProcess());
	} else if (hasRegistration() && other.hasRegistration()) {
	    return getRegistration().equals(other.getRegistration());
	} else {
	    return false;
	}
    }

    @Override
    public int hashCode() {
	int result = 17;

	if (hasPhdIndividualProgramProcess()) {
	    result = 37 * result + getPhdIndividualProgramProcess().hashCode();
	} else {
	    result = 37 * result + getRegistration().hashCode();
	}

	return result;
    }

    public String getDescription() {
	if (hasPhdIndividualProgramProcess()) {
	    return BundleUtil.getMessageFromModuleOrApplication("GEP", "label.personal.ingression.data.viewer.phd.program.name")
		    + " " + BundleUtil.getMessageFromModuleOrApplication("Application", "label.in") + " "
		    + getPhdIndividualProgramProcess().getPhdProgram().getName().getContent();
	} else {
	    return getRegistration().getDegreeDescription();
	}
    }

    public void setRaidesDegreeDesignation(DegreeDesignation raidesDegreeDesignation) {
	this.raidesDegreeDesignation = raidesDegreeDesignation;
    }

    public DegreeDesignation getRaidesDegreeDesignation() {
	return raidesDegreeDesignation;
    }

    private PrecedentDegreeInformation getPrecedentDegreeInformation() {
	if (hasPhdIndividualProgramProcess()) {
	    return getPhdIndividualProgramProcess().getPrecedentDegreeInformation(ExecutionYear.readCurrentExecutionYear());
	} else {
	    return getRegistration().getPrecedentDegreeInformation(ExecutionYear.readCurrentExecutionYear());
	}
    }

    private Student getStudent() {
	if (hasPhdIndividualProgramProcess()) {
	    return getPhdIndividualProgramProcess().getPerson().getStudent();
	} else {
	    return getRegistration().getStudent();
	}
    }

    @Service
    public void updatePersonalInformation() {
	PrecedentDegreeInformation precedentInfo = getPrecedentDegreeInformation();
	PersonalIngressionData personalData;

	if (precedentInfo == null) {
	    precedentInfo = new PrecedentDegreeInformation();
	    if (hasPhdIndividualProgramProcess()) {
		precedentInfo.setPhdIndividualProgramProcess(getPhdIndividualProgramProcess());
	    } else {
		precedentInfo.setRegistration(getRegistration());
	    }

	    personalData = new PersonalIngressionData(getStudent(), ExecutionYear.readCurrentExecutionYear(), precedentInfo);
	} else {
	    personalData = precedentInfo.getPersonalIngressionData();
	}

	precedentInfo.edit(this);
	personalData.edit(this);
    }
}
