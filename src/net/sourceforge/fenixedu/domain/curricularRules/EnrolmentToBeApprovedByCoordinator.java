package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class EnrolmentToBeApprovedByCoordinator extends EnrolmentToBeApprovedByCoordinator_Base {
    
    /**
     * This constructor should be used in context of Composite Rule
     */
    protected EnrolmentToBeApprovedByCoordinator() {
        super();
        setCurricularRuleType(CurricularRuleType.ENROLMENT_TO_BE_APPROVED_BY_COORDINATOR);
    }

    public EnrolmentToBeApprovedByCoordinator(DegreeModule degreeModuleToApplyRule, CourseGroup contextCourseGroup,
            ExecutionPeriod begin, ExecutionPeriod end) {

        this();

        if (degreeModuleToApplyRule == null || begin == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }

        setDegreeModuleToApplyRule(degreeModuleToApplyRule);
        setContextCourseGroup(contextCourseGroup);
        setBegin(begin);
        setEnd(end);
    }
    
    public void edit(CourseGroup contextCourseGroup) {
        setContextCourseGroup(contextCourseGroup);
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        final List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>(1);
        labelList.add(new GenericPair<Object,Boolean>("label.enrolmentToBeApprovedByCoordinator", true));
        if (getContextCourseGroup() != null) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.inGroup", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getContextCourseGroup().getName(), false));
        }
        return labelList;
    }

    @Override
    public boolean evaluate(Class< ? extends DomainObject> object) {
        // TODO Auto-generated method stub
        return false;
    }
    
}
