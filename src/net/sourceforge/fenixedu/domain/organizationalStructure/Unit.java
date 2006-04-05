/*
 * Created on Sep 16, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Contract;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.DateFormatUtil;

public class Unit extends Unit_Base {

    public Unit() {
        super();
    }

    public List<Unit> getTopUnits() {
        Unit unit = this;
        List<Unit> allTopUnits = new ArrayList<Unit>();
        if (unit.hasAnyParentUnits()) {
            for (Unit parentUnit : this.getParentUnits()) {
                if (!parentUnit.hasAnyParentUnits() && !allTopUnits.contains(parentUnit)) {
                    allTopUnits.add(parentUnit);
                } else if (parentUnit.hasAnyParentUnits()) {
                    for (Unit parentUnit2 : parentUnit.getTopUnits()) {
                        if (!allTopUnits.contains(parentUnit2)) {
                            allTopUnits.add(parentUnit2);
                        }
                    }
                }
            }
        }
        return allTopUnits;
    }

    public Unit getDepartmentUnit() {
        List<Unit> parentUnits = this.getParentUnits();
        if (isUnitDepartment(this)) {
            return this;
        } else if (!parentUnits.isEmpty()) {
            for (Unit parentUnit : parentUnits) {
                if (isUnitDepartment(parentUnit)) {
                    return parentUnit;
                } else if (parentUnit.hasAnyParentUnits()) {
                    Unit departmentUnit = parentUnit.getDepartmentUnit();
                    if (departmentUnit == null) {
                        continue;
                    } else {
                        return departmentUnit;
                    }
                }
            }
        }
        return null;
    }

    private boolean isUnitDepartment(Unit unit) {
        return (unit.getType() != null && unit.getType().equals(PartyTypeEnum.DEPARTMENT) && unit
                .getDepartment() != null);
    }

    public List<Unit> getInactiveSubUnits(Date currentDate) {
        List<Unit> allInactiveSubUnits = new ArrayList<Unit>();
        for (Unit subUnit : this.getSubUnits()) {
            if (!subUnit.isActive(currentDate)) {
                allInactiveSubUnits.add(subUnit);
            }
        }
        return allInactiveSubUnits;
    }

    public List<Unit> getActiveSubUnits(Date currentDate) {
        List<Unit> allActiveSubUnits = new ArrayList<Unit>();
        for (Unit subUnit : this.getSubUnits()) {
            if (subUnit.isActive(currentDate)) {
                allActiveSubUnits.add(subUnit);
            }
        }
        return allActiveSubUnits;
    }

    public List<Unit> getAllInactiveSubUnits(Date currentDate) {

        Set<Unit> allInactiveSubUnits = new HashSet<Unit>();
        for (Unit subUnit : this.getSubUnits()) {
            readAndSaveInactiveSubUnits(subUnit, allInactiveSubUnits, currentDate);
        }

        return new ArrayList<Unit>(allInactiveSubUnits);
    }

    private void readAndSaveInactiveSubUnits(Unit unit, Set<Unit> allInactiveSubUnits, Date currentDate) {
        if (!unit.isActive(currentDate)) {
            allInactiveSubUnits.add(unit);
        }
        for (Unit subUnit : unit.getSubUnits()) {
            readAndSaveInactiveSubUnits(subUnit, allInactiveSubUnits, currentDate);
        }
    }

    public List<Unit> getAllActiveSubUnits(Date currentDate) {
        Set<Unit> allActiveSubUnits = new HashSet<Unit>();
        for (Unit subUnit : this.getSubUnits()) {
            readAndSaveActiveSubUnits(subUnit, allActiveSubUnits, currentDate);
        }
        return new ArrayList<Unit>(allActiveSubUnits);
    }

    private void readAndSaveActiveSubUnits(Unit unit, Set<Unit> allActiveSubUnits, Date currentDate) {
        if (unit.isActive(currentDate)) {
            allActiveSubUnits.add(unit);
        }
        for (Unit subUnit : unit.getSubUnits()) {
            readAndSaveActiveSubUnits(subUnit, allActiveSubUnits, currentDate);
        }
    }

    public void edit(String unitName, Integer unitCostCenter, String acronym, Date beginDate,
            Date endDate, PartyTypeEnum type, Unit parentUnit, AccountabilityType accountabilityType) {

        this.setName(unitName);
        this.setBeginDate(beginDate);
        this.setEndDate(endDate);
        this.setType(type);
        this.setCostCenterCode(unitCostCenter);
        this.setAcronym(acronym);

        if (parentUnit != null && accountabilityType != null) {
            this.addParent(parentUnit, accountabilityType);
        }    
        if (endDate != null && endDate.before(beginDate)) {
            throw new DomainException("error.endDateBeforeBeginDate");
        }
    }

    public boolean isActive(Date currentDate) {
        return (this.getEndDate() == null || (DateFormatUtil.equalDates("yyyyMMdd", this.getEndDate(),
                currentDate) || this.getEndDate().after(currentDate)));
    }

    public void delete() {
        if (!hasAnyChilds()
                && (!hasAnyParents() || (this.getParentUnits().size() == 1 && this.getParents().size() == 1))
                && !hasAnyFunctions() && !hasAnyWorkingContracts() && !hasAnyMailingContracts()
                && !hasAnySalaryContracts() && !hasAnyCompetenceCourses() && !hasAnyExternalPersons()
                && !hasAnyAssociatedNonAffiliatedTeachers()) {

            if (hasAnyParentUnits()) {
                this.getParents().get(0).delete();
            }

            for (; !getParticipatingAnyCurricularCourseCurricularRules().isEmpty(); getParticipatingAnyCurricularCourseCurricularRules()
                    .get(0).delete())
                ;

            removeDepartment();
            removeDegree();
            removeRootDomainObject();
            deleteDomainObject();
        } else {
            throw new DomainException("error.delete.unit");
        }
    }

    public List<Contract> getWorkingContracts(Date begin, Date end) {
        List<Contract> contracts = new ArrayList<Contract>();
        for (Contract contract : this.getWorkingContracts()) {
            if (contract.belongsToPeriod(begin, end)) {
                contracts.add(contract);
            }
        }
        return contracts;
    }

    public List<Unit> getScientificAreaUnits() {
        List<Unit> result = new ArrayList<Unit>();
        for (Unit unit : this.getSubUnits()) {
            if (unit.getType() != null && unit.getType().equals(PartyTypeEnum.SCIENTIFIC_AREA)) {
                result.add(unit);
            }
        }
        return result;
    }

    public List<Unit> getCompetenceCourseGroupUnits() {
        List<Unit> result = new ArrayList<Unit>();
        for (Unit unit : this.getSubUnits()) {
            if (unit.getType() != null && unit.getType().equals(PartyTypeEnum.COMPETENCE_COURSE_GROUP)) {
                result.add(unit);
            }
        }
        return result;
    }

    public Double getScientificAreaUnitEctsCredits() {
        double result = 0.0;
        for (Unit competenceCourseGroupUnit : getCompetenceCourseGroupUnits()) {
            for (CompetenceCourse competenceCourse : competenceCourseGroupUnit.getCompetenceCourses()) {
                result += competenceCourse.getEctsCredits();
            }
        }
        return result;
    }

    public Double getScientificAreaUnitEctsCredits(List<Context> contexts) {
        double result = 0.0;
        for (Context context : contexts) {
            if (context.getChildDegreeModule().isLeaf()) {
                CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();

                if (!curricularCourse.isOptional()
                        && curricularCourse.getCompetenceCourse().getScientificAreaUnit().equals(this)) {
                    result += curricularCourse.getCompetenceCourse().getEctsCredits();
                }
            }
        }
        return result;
    }

    public List<Unit> getDegreeUnits() {
        List<Unit> result = new ArrayList<Unit>();
        for (Unit unit : this.getSubUnits()) {
            if (unit.getType() != null && unit.getType().equals(PartyTypeEnum.DEGREE)) {
                result.add(unit);
            }
        }

        return result;
    }

    public List<Teacher> getTeachers(Date begin, Date end) {
        List<Teacher> teachers = new ArrayList<Teacher>();
        List<Employee> employees = getWorkingEmployees(begin, end);
        for (Employee employee : employees) {
            Teacher teacher = employee.getPerson().getTeacher();
            if (teacher != null
                    && !teacher.getAllLegalRegimensWithoutEndSituations(begin, end).isEmpty()) {
                teachers.add(teacher);
            }
        }
        return teachers;
    }

    public Teacher getTeacherByPeriod(Integer teacherNumber, Date begin, Date end) {
        for (Employee employee : getWorkingEmployees(begin, end)) {
            Teacher teacher = employee.getPerson().getTeacher();
            if (teacher != null && teacher.getTeacherNumber().equals(teacherNumber)
                    && !teacher.getAllLegalRegimensWithoutEndSituations(begin, end).isEmpty()) {
                return teacher;
            }
        }
        return null;
    }

    public List<Employee> getWorkingEmployees(Date begin, Date end) {
        Set<Employee> employees = new HashSet<Employee>();
        readAndSaveEmployees(this, employees, begin, end);
        return new ArrayList<Employee>(employees);
    }

    private void readAndSaveEmployees(Unit unit, Set<Employee> employees, Date begin, Date end) {
        for (Contract contract : unit.getWorkingContracts(begin, end)) {
            employees.add(contract.getEmployee());
        }
        for (Unit subUnit : unit.getSubUnits()) {
            readAndSaveEmployees(subUnit, employees, begin, end);
        }
    }

    public List<Unit> getParentUnits() {
        Set<Unit> allParentUnits = new HashSet<Unit>();
        List<Accountability> allParents = this.getParents();
        for (Accountability accountability : allParents) {
            if (accountability.getParentParty() instanceof Unit) {
                allParentUnits.add((Unit) accountability.getParentParty());
            }
        }
        return new ArrayList<Unit>(allParentUnits);
    }

    public List<Unit> getSubUnits() {
        Set<Unit> allChildsUnits = new HashSet<Unit>();
        List<Accountability> allChilds = this.getChilds();
        for (Accountability accountability : allChilds) {
            if (accountability.getChildParty() instanceof Unit) {
                allChildsUnits.add((Unit) accountability.getChildParty());
            }
        }
        return new ArrayList<Unit>(allChildsUnits);
    }

    public boolean hasAnyParentUnits() {
        List<Accountability> allParents = this.getParents();
        for (Accountability accountability : allParents) {
            if (accountability.getParentParty() instanceof Unit) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAnySubUnits() {
        List<Accountability> allChilds = this.getChilds();
        for (Accountability accountability : allChilds) {
            if (accountability.getChildParty() instanceof Unit) {
                return true;
            }
        }
        return false;
    }

    public Accountability addParent(Unit parentUnit, AccountabilityType accountabilityType) {
        return new Accountability(parentUnit, this, accountabilityType);
    }

    public Accountability addChild(Unit childUnit, AccountabilityType accountabilityType) {
        return new Accountability(this, childUnit, accountabilityType);
    }

    public void removeParent(Unit parentUnit) {
        for (Accountability accountability : this.getParents()) {
            if (accountability.getParentParty().equals(parentUnit)) {
                accountability.delete();
                break;
            }
        }
    }

    public void removeChild(Unit childUnit) {
        for (Accountability accountability : this.getChilds()) {
            if (accountability.getChildParty().equals(childUnit)) {
                accountability.delete();
                break;
            }
        }
    }

    public NonAffiliatedTeacher findNonAffiliatedTeacherByName(final String name) {
        for (final NonAffiliatedTeacher nonAffiliatedTeacher : getAssociatedNonAffiliatedTeachersSet()) {
            if (nonAffiliatedTeacher.getName().equalsIgnoreCase(name)) {
                return nonAffiliatedTeacher;
            }
        }
        return null;
    }

    public AccountabilityTypeEnum getRelationType(Unit withUnit){
        for (Accountability accountability : getParents()) {
            if(accountability.getParentParty().equals(withUnit)){
                return accountability.getAccountabilityType().getType();
            }
        }
        for (Accountability accountability : getChilds()) {
            if(accountability.getChildParty().equals(withUnit)){
                return accountability.getAccountabilityType().getType();
            }
        }
        return null;
    }

    public static List<Unit> readAllUnits() {
        List<Unit> allUnits = new ArrayList<Unit>();
        for (Party party : RootDomainObject.getInstance().getPartys()) {
           if (party instanceof Unit) {               
               allUnits.add((Unit) party);
           }
        }
        return allUnits;
    }
}
