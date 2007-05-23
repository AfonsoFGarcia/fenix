package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.DepartmentEmployeesGroup;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class DepartmentSite extends DepartmentSite_Base {
    
    public DepartmentSite(Department department) {
        super();
        
        setShowSectionTeachers(true);
        setShowSectionEmployees(true);
        setShowSectionCourses(true);
        setShowSectionDegrees(true);
        
        Unit unit = department.getDepartmentUnit();
        if (unit.hasSite()) {
            throw new DomainException("site.department.unit.already.has.site");
        }
        
        setUnit(department.getDepartmentUnit());
    }

    @Override
    public Boolean getShowSectionTeachers() {
        Boolean value = super.getShowSectionTeachers();
        return value == null ? true : value;
    }
    
    @Override
    @Checked("UnitSitePredicates.managers")
    public void setShowSectionTeachers(Boolean showSectionTeachers) {
        super.setShowSectionTeachers(showSectionTeachers);
    }
    
    @Override
    public Boolean getShowSectionEmployees() {
        Boolean value = super.getShowSectionEmployees();
        return value == null ? true : value;
    }
    
    @Override
    @Checked("UnitSitePredicates.managers")
    public void setShowSectionEmployees(Boolean showSectionEmployees) {
        super.setShowSectionEmployees(showSectionEmployees);
    }
    
    @Override
    public Boolean getShowSectionCourses() {
        Boolean value = super.getShowSectionCourses();
        return value == null ? true : value;
    }
    
    @Override
    @Checked("UnitSitePredicates.managers")
    public void setShowSectionCourses(Boolean showSectionCourses) {
        super.setShowSectionCourses(showSectionCourses);
    }
    
    @Override
    public Boolean getShowSectionDegrees() {
        Boolean value = super.getShowSectionDegrees();
        return value == null ? true : value;
    }
    
    @Override
    @Checked("UnitSitePredicates.managers")
    public void setShowSectionDegrees(Boolean showSectionDegrees) {
        super.setShowSectionDegrees(showSectionDegrees);
    }
    
    public Department getDepartment() {
        return getUnit().getDepartment();
    }
    
    @Override
    public IGroup getOwner() {
        return new GroupUnion(
                new RoleGroup(Role.getRoleByRoleType(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE)),
                new FixedSetGroup(getManagers())
        );
    }

    @Override
    public List<IGroup> getContextualPermissionGroups() {
        List<IGroup> groups = super.getContextualPermissionGroups();
        
        groups.add(new DepartmentEmployeesGroup(getDepartment()));
        
        return groups;
    }

    /**
     * The department already has the an internacionalized name.
     * 
     * @see Department#getNameI18n()
     */
    @Override
    public MultiLanguageString getUnitNameWithAcronym() {
    	return getDepartment().getNameI18n();
    }
    
}
