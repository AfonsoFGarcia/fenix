package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public abstract class DepartmentByExecutionYearGroup extends LeafGroup {

    private final DomainReference<ExecutionYear> executionYearReference;

    private final DomainReference<Department> departmentReference;

    public DepartmentByExecutionYearGroup(ExecutionYear executionYear, Department department) {
	this.executionYearReference = new DomainReference<ExecutionYear>(executionYear);
	this.departmentReference = new DomainReference<Department>(department);
    }

    public DepartmentByExecutionYearGroup(String executionYearName, String departmentName) {
	this(ExecutionYear.readExecutionYearByName(executionYearName), Department.readByName(departmentName));
    }

    public Department getDepartment() {
	return this.departmentReference.getObject();
    }

    public ExecutionYear getExecutionYear() {
	return this.executionYearReference.getObject();
    }

    @Override
    public String getName() {
	return RenderUtils.getResourceString("GROUP_NAME_RESOURCES", "label.name." + getClass().getSimpleName());
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] { new StaticArgument(getExecutionYear().getYear()), new StaticArgument(getDepartment().getName()) };
    }

    public static abstract class Builder implements GroupBuilder {

	public Group build(Object[] arguments) {
	    String yearName;
	    String departmentName;

	    try {
		yearName = (String) arguments[0];
	    } catch (ClassCastException e) {
		throw new WrongTypeOfArgumentException(0, String.class, arguments[0].getClass());
	    }

	    try {
		departmentName = (String) arguments[1];
	    } catch (ClassCastException e) {
		throw new WrongTypeOfArgumentException(1, String.class, arguments[1].getClass());
	    }

	    return buildConcreteGroup(yearName, departmentName);
	}

	protected abstract DepartmentByExecutionYearGroup buildConcreteGroup(String year, String department);

	public int getMinArguments() {
	    return 2;
	}

	public int getMaxArguments() {
	    return 2;
	}

    }
}
