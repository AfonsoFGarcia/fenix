package net.sourceforge.fenixedu.domain.student;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;

public class PrecedentDegreeInformation extends PrecedentDegreeInformation_Base {

    public PrecedentDegreeInformation() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public String getInstitutionName() {
	return hasInstitution() ? getInstitution().getName() : null;
    }

    public void edit(PrecedentDegreeInformationBean precedentDegreeInformationBean) {

	Unit institution = precedentDegreeInformationBean.getInstitution();
	if (institution == null
		&& !StringUtils.isEmpty(precedentDegreeInformationBean.getInstitutionName())) {
	    institution = UnitUtils.readExternalInstitutionUnitByName(precedentDegreeInformationBean
		    .getInstitutionName());
	    if (institution == null) {
		institution = Unit.createNewNoOfficialExternalInstitution(precedentDegreeInformationBean
			.getInstitutionName());
	    }
	}

	this.setInstitution(institution);
	this.setDegreeDesignation(precedentDegreeInformationBean.getDegreeDesignation());
	this.setConclusionGrade(precedentDegreeInformationBean.getConclusionGrade());
	this.setConclusionYear(precedentDegreeInformationBean.getConclusionYear());
	this.setCountry(precedentDegreeInformationBean.getCountry());
    }
    
    public void delete() {
	removeCountry();
	removeInstitution();
	removeStudent();
	removeStudentCandidacy();
	
	removeRootDomainObject();
	deleteDomainObject();
    }

}
