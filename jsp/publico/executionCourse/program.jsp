<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2>
	<bean:message key="link.program"/>
</h2>

	<bean:define id="executionPeriod" type="net.sourceforge.fenixedu.domain.ExecutionPeriod"
			name="executionCourse" property="executionPeriod"/>

<logic:iterate id="entry" name="executionCourse" property="curricularCoursesIndexedByCompetenceCourse">
	<bean:define id="competenceCourse" name="entry" property="key"/>
	<logic:equal name="competenceCourse" property="curricularStage.name" value="APPROVED">
		<h3>
			<bean:write name="competenceCourse" property="name"/>
			<br/>
			<logic:iterate id="curricularCourse" name="entry" property="value" indexId="i">
				<logic:notEqual name="i" value="0"><br/></logic:notEqual>
				<bean:define id="degree" name="curricularCourse" property="degreeCurricularPlan.degree"/>
				<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="degreeType.name"/>
				<bean:message key="label.in"/>
				<bean:write name="degree" property="nome"/>
			</logic:iterate>
		</h3>
		<blockquote>
			<h4>
				<bean:message key="title.program"/>
			</h4>
			<bean:write name="competenceCourse" property="program" filter="false"/>
			<logic:notEmpty name="competenceCourse" property="programEn">
				<br/>
				<h4>
					<bean:message key="title.program.eng"/>
				</h4>
				<bean:write name="competenceCourse" property="programEn" filter="false"/>
			</logic:notEmpty>
		</blockquote>
	</logic:equal>
</logic:iterate>

	<logic:iterate id="curricularCourse" type="net.sourceforge.fenixedu.domain.CurricularCourse"
			name="executionCourse" property="curricularCoursesSortedByDegreeAndCurricularCourseName">
		<bean:define id="degree" name="curricularCourse" property="degreeCurricularPlan.degree"/>
		<logic:notEqual name="curricularCourse" property="isBolonha" value="true">
			<% net.sourceforge.fenixedu.domain.Curriculum curriculum = curricularCourse.findLatestCurriculumModifiedBefore(executionPeriod.getExecutionYear().getEndDate()); %>
			<% net.sourceforge.fenixedu.domain.Curriculum lastCurriculum = curricularCourse.findLatestCurriculum(); %>
			<% request.setAttribute("curriculum", curriculum); %>
			<% request.setAttribute("lastCurriculum", lastCurriculum); %>

				<h3>
					<bean:write name="curricularCourse" property="name"/>
					<br/>
					<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="degreeType.name"/>
					<bean:message key="label.in"/>
					<bean:write name="degree" property="nome"/>
				</h3>
				<blockquote>
					<logic:present name="curriculum">
						<h4>
							<bean:message key="title.program"/>
						</h4>
						<bean:write name="curriculum" property="program" filter="false"/>
						<logic:notEmpty name="curriculum" property="programEn">
							<br/>
							<h4>
								<bean:message key="title.program.eng"/>
							</h4>
							<bean:write name="curriculum" property="programEn" filter="false"/>
						</logic:notEmpty>
					</logic:present>
					<logic:notPresent name="curriculum">
						<bean:message key="message.program.not.defined"/>
					</logic:notPresent>
				</blockquote>
		</logic:notEqual>

		<br/>
		<br/>
	</logic:iterate>
