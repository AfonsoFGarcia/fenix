/**
 * 
 */
package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/>
 * <br/>
 * <br/>
 *         Created on 16:54:06,17/Mar/2006
 * @version $Id:
 *          DegreeCurricularPlanActiveOrSchoolPartConcludedStudentsGroup.java
 *          23397 2006-11-17 14:31:10Z cfgi $
 */
public class DegreeCurricularPlanActiveOrSchoolPartConcludedStudentsGroup extends DegreeCurricularPlanGroup {
    private static final long serialVersionUID = 1052397518994080993L;

    private class StudentCurricularStateIsActiveOrSchoolPartConcluded implements Predicate {

	public boolean evaluate(Object arg0) {
	    boolean result = false;
	    if (arg0 instanceof StudentCurricularPlan) {
		StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) arg0;
		result = studentCurricularPlan.isLastStudentCurricularPlanFromRegistration()
			&& studentCurricularPlan.getRegistration().isActive();
	    }

	    return result;
	}
    }

    private class StudentCurricularPlanPersonTransformer implements Transformer {

	public Object transform(Object arg0) {
	    StudentCurricularPlan scp = (StudentCurricularPlan) arg0;

	    return scp.getRegistration().getPerson();
	}
    }

    public DegreeCurricularPlanActiveOrSchoolPartConcludedStudentsGroup(DegreeCurricularPlan degreeCurricularPlan) {
	super(degreeCurricularPlan);
    }

    @Override
    public int getElementsCount() {
	int elementsCount = 0;
	for (StudentCurricularPlan scp : this.getDegreeCurricularPlan().getStudentCurricularPlans()) {

	    if (scp.isLastStudentCurricularPlanFromRegistration() && scp.getRegistration().isActive()) {
		elementsCount++;
	    }
	}

	return elementsCount;
    }

    @Override
    public Set<Person> getElements() {
	Set<Person> elements = super.buildSet();
	Collection<StudentCurricularPlan> studentCurricularPlans = this.getDegreeCurricularPlan().getStudentCurricularPlans();
	Collection<StudentCurricularPlan> activeOrSchoolPartConcludedStudentCurricularPlans = CollectionUtils.select(
		studentCurricularPlans, new StudentCurricularStateIsActiveOrSchoolPartConcluded());
	Collection<Person> activeOrSchoolPartConcludedPersons = CollectionUtils.collect(
		activeOrSchoolPartConcludedStudentCurricularPlans, new StudentCurricularPlanPersonTransformer());
	elements.addAll(activeOrSchoolPartConcludedPersons);

	return super.freezeSet(elements);
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] { new IdOperator(getDegreeCurricularPlan()) };
    }

    public static class Builder implements GroupBuilder {

	public Group build(Object[] arguments) {
	    try {
		return new DegreeCurricularPlanActiveOrSchoolPartConcludedStudentsGroup((DegreeCurricularPlan) arguments[0]);
	    } catch (ClassCastException e) {
		throw new GroupDynamicExpressionException(
			"accessControl.group.builder.degreeCurricularPlanActiveOrSchoolPartConcludedStudentsGroup.notDegreeCurricularPlan",
			arguments[0].toString());
	    }
	}

	public int getMinArguments() {
	    return 0;
	}

	public int getMaxArguments() {
	    return 1;
	}

    }
}