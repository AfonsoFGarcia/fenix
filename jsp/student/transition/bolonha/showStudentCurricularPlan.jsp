<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer.OrganizationType"%>
<html:xhtml />

<logic:present role="STUDENT">
	<h2><bean:message key="label.curricularPlan"
		bundle="APPLICATION_RESOURCES" /> - <bean:write name="registration" property="lastStudentCurricularPlan.degreeCurricularPlan.presentationName"/></h2>

	
	<logic:notEmpty name="registration" property="lastStudentCurricularPlan">
		<div class="infoop2 mbottom15">
			<bean:message  key="label.transition.bolonha.message.part1" bundle="STUDENT_RESOURCES"/>
			<bean:message  key="label.transition.bolonha.message.part2" bundle="STUDENT_RESOURCES"/>
		</div>
		<fr:edit name="registration" property="lastStudentCurricularPlan">
			<fr:layout>
				<fr:property name="organizedBy" value="<%=OrganizationType.GROUPS.name()%>" />
				<fr:property name="detailed" value="true" />
			</fr:layout>
		</fr:edit>
	</logic:notEmpty>
	<logic:empty name="registration" property="lastStudentCurricularPlan">
		<i><bean:message  key="label.transition.bolonha.noEquivalences"/></i>
	</logic:empty>
		

</logic:present>


