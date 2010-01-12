package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ErasmusStudentData extends ErasmusStudentData_Base {

    public ErasmusStudentData() {
	super();
	this.setRootDomainObject(RootDomainObject.getInstance());
    }

    public ErasmusStudentData(ErasmusIndividualCandidacy erasmusIndividualCandidacy, ErasmusStudentDataBean erasmusStudentDataBean) {
	this.setDateOfArrival(erasmusStudentDataBean.getDateOfArrival());
	this.setDateOfDeparture(erasmusStudentDataBean.getDateOfDeparture());
	this.setDiplomaConclusionYear(erasmusStudentDataBean.getDiplomaConclusionYear());
	this.setDiplomaName(erasmusStudentDataBean.getDiplomaName());
	this.setErasmusIndividualCandidacy(erasmusIndividualCandidacy);
	this.setExperienceCarryingOutProject(erasmusStudentDataBean.getExperienceCarryingOutProject());
	this.setHasContactedOtherStaff(erasmusStudentDataBean.getHasContactedOtherStaff());
	this.setHasDiplomaOrDegree(erasmusStudentDataBean.getHasDiplomaOrDegree());
	this.setHomeInstitutionAddress(erasmusStudentDataBean.getHomeInstitutionAddress());
	this.setHomeInstitutionEmail(erasmusStudentDataBean.getHomeInstitutionEmail());
	this.setHomeInstitutionCoordinatorName(erasmusStudentDataBean.getHomeInstitutionExchangeCoordinatorName());
	this.setHomeInstitutionFax(erasmusStudentDataBean.getHomeInstitutionFax());
	this.setHomeInstitutionName(erasmusStudentDataBean.getHomeInstitutionName());
	this.setHomeInstitutionPhone(erasmusStudentDataBean.getHomeInstitutionPhone());
	this.setMainSubjectThesis(erasmusStudentDataBean.getMainSubjectThesis());
	this.setNameOfContact(erasmusStudentDataBean.getNameOfContact());
	this.setTypesOfProgramme(erasmusStudentDataBean.getTypeOfProgrammeList());

    }

    public void edit(ErasmusStudentDataBean erasmusStudentDataBean) {
	this.setDateOfArrival(erasmusStudentDataBean.getDateOfArrival());
	this.setDateOfDeparture(erasmusStudentDataBean.getDateOfDeparture());
	this.setDiplomaConclusionYear(erasmusStudentDataBean.getDiplomaConclusionYear());
	this.setDiplomaName(erasmusStudentDataBean.getDiplomaName());
	this.setExperienceCarryingOutProject(erasmusStudentDataBean.getExperienceCarryingOutProject());
	this.setHasContactedOtherStaff(erasmusStudentDataBean.getHasContactedOtherStaff());
	this.setHasDiplomaOrDegree(erasmusStudentDataBean.getHasDiplomaOrDegree());
	this.setHomeInstitutionAddress(erasmusStudentDataBean.getHomeInstitutionAddress());
	this.setHomeInstitutionEmail(erasmusStudentDataBean.getHomeInstitutionEmail());
	this.setHomeInstitutionCoordinatorName(erasmusStudentDataBean.getHomeInstitutionExchangeCoordinatorName());
	this.setHomeInstitutionFax(erasmusStudentDataBean.getHomeInstitutionFax());
	this.setHomeInstitutionName(erasmusStudentDataBean.getHomeInstitutionName());
	this.setHomeInstitutionPhone(erasmusStudentDataBean.getHomeInstitutionPhone());
	this.setMainSubjectThesis(erasmusStudentDataBean.getMainSubjectThesis());
	this.setNameOfContact(erasmusStudentDataBean.getNameOfContact());
	this.setTypesOfProgramme(erasmusStudentDataBean.getTypeOfProgrammeList());
    }
}
