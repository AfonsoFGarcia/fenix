package net.sourceforge.fenixedu.domain.student;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * TODO: remove this, enrich RegistrationAgreement instead
 * This enum shouldn't have been created. 
 *
 */

@Deprecated
public enum MobilityProgram {

    SOCRATES, 
    
    ERASMUS, 

    MINERVA, 
    
    COVENANT_WITH_AZORES, 
    
    COVENANT_WITH_INSTITUTION {

	@Override
	public String getSpecificDescription() {
	    return ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale()).getString(getQualifiedName()) + UniversityUnit.getInstitutionsUniversityUnit().getName();
	}
    
    };

    public String getQualifiedName() {
	Class<?> enumClass = this.getClass();
	if (!enumClass.isEnum() && Enum.class.isAssignableFrom(enumClass)) {
            enumClass = enumClass.getEnclosingClass();
        }

	return enumClass.getSimpleName() + "." + name();
    }

    public String getSpecificDescription() {
	return ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale()).getString(getQualifiedName());
    }
    
    public String getDescription() {
	return getSpecificDescription();
    }

}
