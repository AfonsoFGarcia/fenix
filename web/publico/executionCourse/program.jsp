<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2 class="mbottom1">
	<bean:message key="link.program"/>
</h2>

	<bean:define id="executionPeriod" type="net.sourceforge.fenixedu.domain.ExecutionSemester"
			name="executionCourse" property="executionPeriod"/>

<logic:iterate id="entry" name="executionCourse" property="curricularCoursesIndexedByCompetenceCourse">
	<bean:define id="competenceCourse" name="entry" property="key"/>
	<logic:equal name="competenceCourse" property="curricularStage.name" value="APPROVED">
		<div class="mbottom2">
		<p class="mbottom05"><em><fr:view name="competenceCourse" property="nameI18N"/></em></p>
		<h3 class="mvert0">
			<logic:iterate id="curricularCourse" name="entry" property="value" indexId="i">
				<logic:notEqual name="i" value="0"><br/></logic:notEqual>
				<bean:define id="degree" name="curricularCourse" property="degreeCurricularPlan.degree"/>
				<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="degreeType.name"/>
				<bean:message key="label.in"/>
				<fr:view name="degree" property="nameI18N">
					<fr:layout name="html"> 
						<fr:property name="escaped" value="false" />
						<fr:property name="newlineAware" value="true" />
					</fr:layout>
				</fr:view>
			</logic:iterate>
		</h3>

			<h4 class="mbottom05 greytxt">
				<bean:message key="title.program"/>
			</h4>
			<div class="mtop05 coutput2" style="line-height: 1.5em;">
				<fr:view name="competenceCourse" property="programI18N">
					<fr:layout name="html">
						<fr:property name="newlineAware" value="true" />
					</fr:layout>
				</fr:view>
			</div>
	   </div>
	</logic:equal>
</logic:iterate>

	<logic:iterate id="curricularCourse" type="net.sourceforge.fenixedu.domain.CurricularCourse"
			name="executionCourse" property="curricularCoursesSortedByDegreeAndCurricularCourseName">
		<bean:define id="degree" name="curricularCourse" property="degreeCurricularPlan.degree"/>
		<logic:notEqual name="curricularCourse" property="bolonhaDegree" value="true">
			<% net.sourceforge.fenixedu.domain.Curriculum curriculum = curricularCourse.findLatestCurriculumModifiedBefore(executionPeriod.getExecutionYear().getEndDate()); %>
			<% net.sourceforge.fenixedu.domain.Curriculum lastCurriculum = curricularCourse.findLatestCurriculum(); %>
			<% request.setAttribute("curriculum", curriculum); %>
			<% request.setAttribute("lastCurriculum", lastCurriculum); %>
		<div class="mbottom2">
			<p class="mbottom05"><em><fr:view name="curricularCourse" property="nameI18N"/></em></p>
				<h3 class="mvert0">
					<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="degreeType.name"/>
					<bean:message key="label.in"/>
					<fr:view name="degree" property="nameI18N"/>
				</h3>
					<logic:present name="curriculum">
						<h4 class="mbottom05 greytxt">
							<bean:message key="title.program"/>
						</h4>
						<div class="mtop05 coutput2" style="line-height: 1.5em;">
						<fr:view name="curriculum" property="programI18N"/>
						</div>
					</logic:present>
					<logic:notPresent name="curriculum">
						<bean:message key="message.program.not.defined"/>
					</logic:notPresent>
			</div>
		</logic:notEqual>
	</logic:iterate>
	