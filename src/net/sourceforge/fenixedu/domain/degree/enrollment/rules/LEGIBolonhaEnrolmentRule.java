package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

public class LEGIBolonhaEnrolmentRule extends BolonhaEnrolmentRule {
    
    private static final String DISSERTACAO_PRODUCAO = "B9W";
    
    private static final String DISSERTACAO_EMPREENDIMENTOS = "B9X";

    private static final String[] DEGREE_PRODUCAO = { "B5T", "B31", "AFS", "B32"};
    
    private static final String[] DEGREE_EMPREENDIMENTOS = { "B5T", "B31", "AA9", "AFS", "B32"};
    
    private static final String[] PRODUCAO = { "B9W" };
    
    private static final String[] EMPREENDIMENTOS = { "B9X"};
    
    private static final String[] COMMONS_PRODUCAO = { "A4G", "AA9", "A4B", "AFY", "B98", "A4H", "ABX"}; 
    
    private static final String[] COMMONS_EMPREENDIMENTOS = { "A4G", "AFY", "B98", "A4H", "ABX"};
    
    private static final String[] DEGREE_OPTIONS = { "AFY", "AJA", "B31", "ABX", "AFS", "B32"};
    
    private static final String PRODUCAO_CODE = "1120";

    private static final String EMPREENDIMENTOS_CODE = "1130";

    public LEGIBolonhaEnrolmentRule(StudentCurricularPlan studentCurricularPlan,
	    ExecutionPeriod executionPeriod) {
	super(studentCurricularPlan, executionPeriod);
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {

	if(studentCurricularPlan.getBranch() != null) {
	    if(studentCurricularPlan.getBranch().getCode().equals(PRODUCAO_CODE)) {
		return applyBranch(curricularCoursesToBeEnrolledIn, COMMONS_PRODUCAO, DEGREE_PRODUCAO, PRODUCAO, DISSERTACAO_PRODUCAO);
	    }
	    if(studentCurricularPlan.getBranch().getCode().equals(EMPREENDIMENTOS_CODE)) {
		return applyBranch(curricularCoursesToBeEnrolledIn, COMMONS_EMPREENDIMENTOS, DEGREE_EMPREENDIMENTOS, EMPREENDIMENTOS, DISSERTACAO_EMPREENDIMENTOS);
	    }
	}
	
	return curricularCoursesToBeEnrolledIn;
    }
    
    public List<CurricularCourse2Enroll> applyBranch(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn, String[] commons, String[] degree, String[] masterDegree, 
	    String dissertacao)
	    throws EnrolmentRuleDomainException {
	
	if(countEnroledInPreviousExecutionPeriodOrAprovedEnrolments(degree) > 0 || countEnroledOrAprovedEnrolments(degree) > 0) {
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(masterDegree));
	    if(countEnroledOrAprovedEnrolments(DEGREE_OPTIONS) == 4) {
		removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(DEGREE_OPTIONS));
	    }
	}

	if(countEnroledInPreviousExecutionPeriodOrAprovedEnrolments(masterDegree) > 0 || countEnroledOrAprovedEnrolments(masterDegree) > 0) {
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(degree));
	    if(isEnrolledInPreviousExecutionPeriod(dissertacao)) {
		removeCurricularCourse(curricularCoursesToBeEnrolledIn, dissertacao);
	    }
	}
	
	return curricularCoursesToBeEnrolledIn;
    }
}
