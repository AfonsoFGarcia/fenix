package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import pt.ist.fenixWebFramework.services.Service;

public class EctsInstitutionByCurricularYearConversionTable extends EctsInstitutionByCurricularYearConversionTable_Base {

    protected EctsInstitutionByCurricularYearConversionTable(Unit school, AcademicInterval year, CycleType cycle,
	    CurricularYear curricularYear, EctsComparabilityTable table) {
	super();
	setSchool(school);
	setYear(year);
	setCycle(cycle);
	setCurricularYear(curricularYear);
	setEctsTable(table);
    }

    @Override
    protected RootDomainObject getRootDomainObject() {
	return getSchool().getRootDomainObject();
    }

    @Override
    public DomainObject getTargetEntity() {
	return getSchool();
    }

    @Service
    public static EctsInstitutionByCurricularYearConversionTable createConversionTable(Unit ist,
	    AcademicInterval executionInterval, CycleType cycleType, CurricularYear curricularYear, String[] table) {
	for (EctsInstitutionByCurricularYearConversionTable conversion : ist.getEctsCourseConversionTables()) {
	    if (conversion.getYear().equals(executionInterval) && conversion.getCycle().equals(cycleType)
		    && conversion.getCurricularYear().equals(curricularYear)) {
		throw new DuplicateEctsConversionTable();
	    }
	}
	return new EctsInstitutionByCurricularYearConversionTable(ist, executionInterval, cycleType, curricularYear,
		new EctsComparabilityTable(table));
    }
}
