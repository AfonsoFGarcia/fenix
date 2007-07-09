/*
 * DegreeType.java
 *
 * Created on 20 de Dezembro de 2002, 14:12
 */

package net.sourceforge.fenixedu.domain.degree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.LanguageUtils;

/**
 * 
 * @author Nuno Nunes & Joana Mota
 */
public enum DegreeType {

    DEGREE(
	    GradeScale.TYPE20, 
	    CurricularPeriodType.FIVE_YEAR, 
	    true, 				// canCreateStudent
	    false,				// canCreateStudentOnlyWithCandidacy
	    AdministrativeOfficeType.DEGREE, 
	    false,				// isFirstCycle 
	    false,				// isSecondCycle 
	    false,				// isThirdCycle 
	    true) {				// qualifiesForGraduateTitle

	@Override
	public Collection<CycleType> getCycleTypes() {
	    return Collections.emptySet();
	}
	
    },

    MASTER_DEGREE(
	    GradeScale.TYPE5, 
	    CurricularPeriodType.TWO_YEAR, 
	    false, 				// canCreateStudent
	    true,				// canCreateStudentOnlyWithCandidacy
	    AdministrativeOfficeType.MASTER_DEGREE, 
	    false, 				// isFirstCycle
	    false,				// isSecondCycle 
	    false,				// isThirdCycle 
	    true) {				// qualifiesForGraduateTitle
	
	@Override
	public Collection<CycleType> getCycleTypes() {
	    return Collections.emptySet();
	}

    },

    BOLONHA_DEGREE(
	    GradeScale.TYPE20, 
	    CurricularPeriodType.THREE_YEAR, 
	    true, 				// canCreateStudent 
	    false,				// canCreateStudentOnlyWithCandidacy
	    AdministrativeOfficeType.DEGREE, 
	    true, 				// isFirstCycle 
	    false, 				// isSecondCycle 
	    false, 				// isThirdCycle 
	    true) {				// qualifiesForGraduateTitle
		
	@Override
	public Collection<CycleType> getCycleTypes() {
	    return Collections.singleton(CycleType.FIRST_CYCLE);
	}

    },

    BOLONHA_MASTER_DEGREE(
	    GradeScale.TYPE20, 
	    CurricularPeriodType.TWO_YEAR, 
	    true, 				// canCreateStudent 
	    false,				// canCreateStudentOnlyWithCandidacy
	    AdministrativeOfficeType.DEGREE, 
	    false, 				// isFirstCycle  
	    true, 				// isSecondCycle 
	    false, 				// isThirdCycle 
	    true) {				// qualifiesForGraduateTitle
		
	@Override
	public Collection<CycleType> getCycleTypes() {
	    return Collections.singleton(CycleType.SECOND_CYCLE);
	}

    },

    BOLONHA_INTEGRATED_MASTER_DEGREE(
	    GradeScale.TYPE20, 
	    CurricularPeriodType.FIVE_YEAR, 
	    true, 				// canCreateStudent 
	    false,				// canCreateStudentOnlyWithCandidacy
	    AdministrativeOfficeType.DEGREE, 
	    true, 				// isFirstCycle 
	    true, 				// isSecondCycle 
	    false, 				// isThirdCycle 
	    true) {				// qualifiesForGraduateTitle
		
	@Override
	public Collection<CycleType> getCycleTypes() {
	    final Collection<CycleType> result = new HashSet<CycleType>();
	    result.add(CycleType.FIRST_CYCLE);
	    result.add(CycleType.SECOND_CYCLE);
	    
	    return result;
	}

    },

    BOLONHA_PHD_PROGRAM(
	    GradeScale.TYPE20, 
	    CurricularPeriodType.YEAR, 
	    true, 				// canCreateStudent 
	    false,				// canCreateStudentOnlyWithCandidacy
	    AdministrativeOfficeType.MASTER_DEGREE, 
	    false, 				// isFirstCycle 
	    false, 				// isSecondCycle 
	    true, 				// isThirdCycle 
	    true) {				// qualifiesForGraduateTitle
		
	@Override
	public Collection<CycleType> getCycleTypes() {
	    return Collections.singleton(CycleType.THIRD_CYCLE);
	}

    },

    BOLONHA_ADVANCED_FORMATION_DIPLOMA(
	    GradeScale.TYPE20, 
	    CurricularPeriodType.YEAR, 
	    true, 				// canCreateStudent 
	    true,				// canCreateStudentOnlyWithCandidacy
	    AdministrativeOfficeType.MASTER_DEGREE, 
	    false, 				// isFirstCycle 
	    false, 				// isSecondCycle 
	    true, 				// isThirdCycle 
	    false) {				// qualifiesForGraduateTitle
		
	@Override
	public Collection<CycleType> getCycleTypes() {
	    return Collections.singleton(CycleType.THIRD_CYCLE);
	}

    },

    BOLONHA_SPECIALIZATION_DEGREE(
	    GradeScale.TYPE20, 
	    CurricularPeriodType.YEAR, 
	    true, 				// canCreateStudent 
	    false,				// canCreateStudentOnlyWithCandidacy
	    AdministrativeOfficeType.MASTER_DEGREE, 
	    false, 				// isFirstCycle 
	    false, 				// isSecondCycle 
	    false, 				// isThirdCycle 
	    false) {				// qualifiesForGraduateTitle
		
	@Override
	public Collection<CycleType> getCycleTypes() {
	    return Collections.emptySet();
	}

    };


    private GradeScale gradeScale;

    private CurricularPeriodType curricularPeriodType;

    private boolean canCreateStudent;

    private boolean canCreateStudentOnlyWithCandidacy;

    private AdministrativeOfficeType administrativeOfficeType;
    
    private boolean isFirstCycle;
    
    private boolean isSecondCycle;
    
    private boolean isThirdCycle;
    
    private boolean qualifiesForGraduateTitle;
    
    private DegreeType(GradeScale gradeScale, CurricularPeriodType curricularPeriodType,
	    boolean canCreateStudent, boolean canCreateStudentOnlyWithCandidacy,
	    AdministrativeOfficeType administrativeOfficeType, boolean isFirstCycle,
	    boolean isSecondCycle, boolean isThirdCycle, boolean qualifiesForGraduateTitle) {
	this.gradeScale = gradeScale;
	this.curricularPeriodType = curricularPeriodType;
	this.canCreateStudent = canCreateStudent;
	this.canCreateStudentOnlyWithCandidacy = canCreateStudentOnlyWithCandidacy;
	this.administrativeOfficeType = administrativeOfficeType;
	this.isFirstCycle = isFirstCycle;
	this.isSecondCycle = isSecondCycle;
	this.isThirdCycle = isThirdCycle;
	this.qualifiesForGraduateTitle = qualifiesForGraduateTitle;
    }

    public String getName() {
	return name();
    }

    public GradeScale getGradeScale() {
	return this.gradeScale;
    }

    public CurricularPeriodType getCurricularPeriodType() {
	return curricularPeriodType;
    }

    public int getYears() {
	return (int) this.curricularPeriodType.getWeight();
    }

    final public boolean hasExactlyOneCurricularYear() {
	return getYears() == 1;
    }
    
    public double getDefaultEctsCredits() {
	switch (getCurricularPeriodType()) {
	case YEAR:
	    return 30;
	case TWO_YEAR:
	    return 120;
	case THREE_YEAR:
	    return 180;
	case FIVE_YEAR:
	    return 300;
	default:
	    return 0;
	}
    }

    public boolean isBolonhaType() {
	return this != DegreeType.DEGREE && this != DegreeType.MASTER_DEGREE;
    }

    public boolean isDegree() {
	return this == DegreeType.DEGREE || this == DegreeType.BOLONHA_DEGREE;
    }

    public boolean isMasterDegree() {
	return this == DegreeType.MASTER_DEGREE || this == DegreeType.BOLONHA_MASTER_DEGREE;
    }

    public boolean isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree() {
	return this.isDegree() || this == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE;
    }

    public boolean canCreateStudent() {
	return canCreateStudent;
    }

    public boolean canCreateStudentOnlyWithCandidacy() {
	return canCreateStudentOnlyWithCandidacy;
    }

    public AdministrativeOfficeType getAdministrativeOfficeType() {
	return administrativeOfficeType;
    }

    public String getLocalizedName() {
	return ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils.getLocale())
		.getString(getQualifiedName());
    }

    public String getQualifiedName() {
	return DegreeType.class.getSimpleName() + "." + name();
    }

    final public String getFilteredName() {
	final StringBuilder result = new StringBuilder(getLocalizedName());
	final String toRemove;
	
	if (isBolonhaType()) {
	    toRemove = " Bolonha";
	} else if (this == DegreeType.DEGREE) {
	    final ResourceBundle applicationResources = ResourceBundle.getBundle(
		    "resources.ApplicationResources", LanguageUtils.getLocale());
	    toRemove = " (" + getYears() + " " + applicationResources.getString("years") + ")";
	} else {
	    toRemove = "";
	}
	
	if (result.toString().contains(toRemove)) {
	    result.replace(result.indexOf(toRemove), result.indexOf(toRemove) + toRemove.length(), "");
	}
	
	return result.toString();
    }

    final public boolean getQualifiesForGraduateTitle() {
	return qualifiesForGraduateTitle;
    }
	   
    @Deprecated
    final public String getSeniorTitle() {
    	return getGraduateTitle();
    }

    final public String getGraduateTitle() {
	if (getQualifiesForGraduateTitle()) {
	    return ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils.getLocale()).getString(getQualifiedName() + ".graduate.title");
	}
	
	return StringUtils.EMPTY;
    }

    final public String getGraduateTitle(final CycleType cycleType) {
	if (getQualifiesForGraduateTitle()) {
	
	    if (cycleType == null) {
		return getGraduateTitle();
	    }
    	
	    if (getCycleTypes().isEmpty()) {
    	    	throw new DomainException("DegreeType.has.no.cycle.type");
	    }
    	
	    if (!hasCycleTypes(cycleType)) {
    	    	throw new DomainException("DegreeType.doesnt.have.such.cycle.type");
	    }
    	
	    if (getQualifiesForGraduateTitle()) {
		return ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils.getLocale()).getString(getQualifiedName() + (isComposite() ? "." + cycleType.name() : "") + ".graduate.title");
	    }
	
	}
	
	return StringUtils.EMPTY;
    }

   public static List<DegreeType> getBolonhaDegreeTypes() {
	final List<DegreeType> result = new ArrayList<DegreeType>();

	for (final DegreeType degreeType : values()) {
	    if (degreeType.isBolonhaType()) {
		result.add(degreeType);
	    }
	}

	return result;
    }

   public boolean isFirstCycle() {
       return isFirstCycle;
   }

   public boolean isSecondCycle() {
       return isSecondCycle;
   }

   public boolean isThirdCycle() {
       return isThirdCycle;
   }
   
   abstract public Collection<CycleType> getCycleTypes();

   final public boolean hasAnyCycleTypes() {
       return !getCycleTypes().isEmpty();
   }
   
   final public boolean hasCycleTypes(final CycleType cycleType) {
       return getCycleTypes().contains(cycleType);
   }
  
   final public boolean isComposite() {
       return getCycleTypes().size() > 1;
   }

   final public boolean hasExactlyOneCycleType() {
       return getCycleTypes().size() == 1;
   }
   
   final public CycleType getCycleType() {
       if (hasExactlyOneCycleType()) {
	   return getCycleTypes().iterator().next();
       }
       
       throw new DomainException("DegreeType.has.more.than.one.cycle.type");
   }
    
   final public boolean isStrictlyFirstCycle() {
       return hasExactlyOneCycleType() && getCycleTypes().contains(CycleType.FIRST_CYCLE);
   }
   
   public CycleType getFirstCycleType() {
	final Collection<CycleType> cycleTypes = getCycleTypes();
	if (cycleTypes.isEmpty()) {
	    return null;
	}
	return Collections.min(cycleTypes, CycleType.CYCLE_TYPE_COMPARATOR);
   }
  
   public CycleType getLastCycleType() {
	final Collection<CycleType> cycleTypes = getCycleTypes();
	if (cycleTypes.isEmpty()) {
	    return null;
	}

	return Collections.max(cycleTypes, CycleType.CYCLE_TYPE_COMPARATOR);
   }

}
