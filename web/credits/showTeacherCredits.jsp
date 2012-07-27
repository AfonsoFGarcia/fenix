<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<html:xhtml/>

<em><bean:message key="label.teacherService.credits"/></em>
<h2><bean:message key="link.teacherCreditsSheet.view" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h2>

<logic:present name="teacherBean">

	<fr:view name="teacherBean" property="teacher">
		<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.Teacher">
			<fr:slot name="person.name" key="label.name"/>
			<fr:slot name="teacherId" key="label.teacher.id"/>
			<fr:slot name="currentWorkingDepartment.name" key="label.department" layout="null-as-label"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
    		<fr:property name="columnClasses" value="width12em,,"/>
		</fr:layout>
	</fr:view>

	<logic:notEmpty name="teacherBean" property="annualTeachingCredits">
		<fr:view name="teacherBean" property="annualTeachingCredits">
			<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.credits.util.TeacherCreditsBean">
				<fr:slot name="executionYear" key="label.executionYear" layout="link">
					<fr:property name="subSchema" value="net.sourceforge.fenixedu.domain.ExecutionYear.view"/>
					<fr:property name="subLayout" value="values"/>
					<fr:property name="contextRelative" value="true" />
					<fr:property name="moduleRelative" value="true" />
					<fr:property name="useParent" value="true" />
					<fr:property name="linkFormat" value="/credits.do?method=viewAnnualTeachingCredits&amp;executionYearOid=${executionYear.externalId}&amp;teacherOid=${teacher.externalId}"/>
				</fr:slot>
				<fr:slot name="teachingCredits" key="label.credits.teachingCredits.simpleCode" layout="null-as-label" help="label.credits.teachingCredits.simpleCode">
					<fr:property name="subLayout" value="decimal-format"/>
				</fr:slot>
				<fr:slot name="masterDegreeThesesCredits" key="label.credits.masterDegreeTheses.simpleCode" layout="null-as-label" help="label.credits.masterDegreeTheses.simpleCode">
					<fr:property name="subLayout" value="decimal-format"/>
				</fr:slot>
				<fr:slot name="phdDegreeThesesCredits" key="label.credits.phdDegreeTheses.simpleCode" layout="null-as-label" help="label.credits.phdDegreeTheses.simpleCode">
					<fr:property name="subLayout" value="decimal-format"/>
				</fr:slot>
				<fr:slot name="projectsTutorialsCredits" key="label.credits.projectsAndTutorials.simpleCode" layout="null-as-label" help="label.credits.projectsAndTutorials.simpleCode">
					<fr:property name="subLayout" value="decimal-format"/>
				</fr:slot>
				<fr:slot name="managementFunctionCredits" key="label.credits.managementPositions.simpleCode" layout="null-as-label" help="label.credits.managementPositions.simpleCode">
					<fr:property name="subLayout" value="decimal-format"/>
				</fr:slot>
				<fr:slot name="othersCredits" key="label.credits.otherCredits.simpleCode" layout="null-as-label" help="label.credits.otherCredits.simpleCode">
					<fr:property name="subLayout" value="decimal-format"/>
				</fr:slot>
				<fr:slot name="creditsReduction" layout="null-as-label" key="label.credits.creditsReduction.simpleCode" help="label.credits.creditsReduction.simpleCode">
					<fr:property name="subLayout" value="decimal-format"/>
				</fr:slot>
				<fr:slot name="serviceExemptionCredits" key="label.credits.serviceExemptionSituations.simpleCode" layout="null-as-label" help="label.credits.serviceExemptionSituations.simpleCode">
					<fr:property name="subLayout" value="decimal-format"/>
				</fr:slot>
				<fr:slot name="annualTeachingLoad" layout="null-as-label" key="label.credits.normalizedAcademicCredits.simpleCode" help="label.credits.normalizedAcademicCredits.simpleCode">
					<fr:property name="subLayout" value="decimal-format"/>
				</fr:slot>
				<fr:slot name="yearCredits" layout="null-as-label" key="label.credits.yearCredits.simpleCode" help="label.credits.yearCredits.simpleCode">
					<fr:property name="subLayout" value="decimal-format"/>
				</fr:slot>
				<fr:slot name="finalCredits" layout="null-as-label" key="label.credits.finalCredits.simpleCode" help="label.credits.finalCredits.simpleCode">
					<fr:property name="subLayout" value="decimal-format"/>
				</fr:slot>
				<fr:slot name="accumulatedCredits" layout="null-as-label" key="label.credits.totalCredits" help="label.credits.totalCredits">
					<fr:property name="subLayout" value="decimal-format"/>
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 printborder" />
				<fr:property name="columnClasses" value="bgcolor3 acenter" />
				<fr:property name="headerClasses" value=",,,,,,,,,bgcolore5e5e5,bgcolore5e5e5,bgcolore5e5e5,bgcolore5e5e5" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>