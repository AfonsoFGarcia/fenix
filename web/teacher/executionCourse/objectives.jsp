<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="link.objectives"/></h2>

<div class="infoop2">
	<bean:message key="label.objectives.explanation" />
</div>


<p>
	<span class="error"><!-- Error messages go here -->
		<html:errors/>
	</span>
	<span class="info">
	<!-- w3c Complient -->
		<html:messages id="info" message="true"/>
	</span>
</p>

<logic:present name="executionCourse">

	<bean:define id="showNote" value="false" toScope="request"/>
	
	<bean:define id="executionPeriod" type="net.sourceforge.fenixedu.domain.ExecutionSemester"
			name="executionCourse" property="executionPeriod"/>

	<logic:iterate id="curricularCourse" type="net.sourceforge.fenixedu.domain.CurricularCourse"
			name="executionCourse" property="curricularCoursesSortedByDegreeAndCurricularCourseName">
		<bean:define id="degree" name="curricularCourse" property="degreeCurricularPlan.degree"/>

		<logic:equal name="curricularCourse" property="bolonhaDegree" value="true">
			<bean:define id="competenceCourse" name="curricularCourse" property="competenceCourse"/>
			
				<bean:define id="competenceCourse" name="curricularCourse" property="competenceCourse"/>
				<p class="mtop15 mbottom025 color777">
					<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="degreeType.name"/>
					<bean:message key="label.in"/>
					<bean:write name="degree" property="nome"/>
				</p>
				<h3 class="mtop025">
					<bean:write name="competenceCourse" property="name"/>  
						<logic:equal name="competenceCourse" property="curricularStage.name" value="PUBLISHED">
								<bean:define id="showNote" value="true" toScope="request"/>
								<span style="font-size: 0.7em; font-weight: normal; background: #ffa;"><bean:message key="label.competenceCourse.notAprroved"/>(*)</span>
						</logic:equal>
				</h3>
				<blockquote>
					<h4>
						<bean:message key="label.generalObjectives"/>
					</h4>
					<bean:write name="competenceCourse" property="objectives" filter="false"/>
					<logic:present name="competenceCourse" property="generalObjectivesEn">
						<br/>
						<h4>
							<bean:message key="label.generalObjectives.eng"/>
						</h4>
						<bean:write name="competenceCourse" property="generalObjectivesEn" filter="false"/>
					</logic:present>
				</blockquote>
		</logic:equal>

		<logic:notEqual name="curricularCourse" property="bolonhaDegree" value="true">
			<% net.sourceforge.fenixedu.domain.Curriculum curriculum = curricularCourse.findLatestCurriculumModifiedBefore(executionPeriod.getExecutionYear().getEndDate()); %>
			<% net.sourceforge.fenixedu.domain.Curriculum lastCurriculum = curricularCourse.findLatestCurriculum(); %>
			<% request.setAttribute("curriculum", curriculum); %>
			<% request.setAttribute("lastCurriculum", lastCurriculum); %>

				<h3 class="mtop2">
					<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="degreeType.name"/>
					<bean:message key="label.in"/>
					<bean:write name="degree" property="nome"/>
					<br/>
					<bean:write name="curricularCourse" property="name"/>
				</h3>
				<blockquote>
					<logic:present name="curriculum">
						<h4>
							<bean:message key="label.generalObjectives"/>
						</h4>
						<bean:write name="curriculum" property="generalObjectives" filter="false"/>
						<logic:present name="curriculum" property="generalObjectivesEn">
							<br/>
							<h4>
								<bean:message key="label.generalObjectives.eng"/>
							</h4>
							<bean:write name="curriculum" property="generalObjectivesEn" filter="false"/>
						</logic:present>
						<h4>
							<bean:message key="label.operacionalObjectives"/>
						</h4>
						<bean:write name="curriculum" property="operacionalObjectives" filter="false"/>
						<logic:present name="curriculum" property="operacionalObjectivesEn">
							<br/>
							<h4>
								<bean:message key="label.operacionalObjectives.eng"/>
							</h4>
							<bean:write name="curriculum" property="operacionalObjectivesEn" filter="false"/>
						</logic:present>
					</logic:present>
					<logic:notPresent name="curriculum">
						<bean:message key="message.objectives.not.defined"/>
					</logic:notPresent>
				</blockquote>
				<logic:present name="curriculum">
					<% if ((lastCurriculum == curriculum && (curricularCourse.getBasic() == null || !curricularCourse.getBasic().booleanValue()))
						|| curriculum.getOperacionalObjectives() == null || curriculum.getOperacionalObjectivesEn() == null
						|| curriculum.getOperacionalObjectives().equals(org.apache.commons.lang.StringUtils.EMPTY) || curriculum.getOperacionalObjectivesEn().equals(org.apache.commons.lang.StringUtils.EMPTY)
						|| curriculum.getGeneralObjectives() == null || curriculum.getGeneralObjectivesEn() == null
						|| curriculum.getGeneralObjectives().equals(org.apache.commons.lang.StringUtils.EMPTY) || curriculum.getGeneralObjectivesEn().equals(org.apache.commons.lang.StringUtils.EMPTY)) { %>

						<bean:define id="url" type="java.lang.String">/editObjectives.do?method=prepareEditObjectives&amp;curriculumID=<bean:write name="curriculum" property="idInternal"/></bean:define>
						<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
							<bean:message key="button.edit"/>
						</html:link>
					<% } %>
				</logic:present>
				<logic:notPresent name="curriculum">
					<% if (curricularCourse.getBasic() == null || !curricularCourse.getBasic().booleanValue()) { %>
						<bean:define id="url" type="java.lang.String">/createObjectives.do?method=prepareCreateObjectives&amp;curricularCourseID=<bean:write name="curricularCourse" property="idInternal"/></bean:define>
						<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
							<bean:message key="button.create"/>
						</html:link>
					<% } %>
				</logic:notPresent>
		</logic:notEqual>

	</logic:iterate>
	
	<logic:equal name="showNote" value="true">
		<p  class="mtop2"><em>* <bean:message key="label.competenceCourse.notAprroved.note"/></em></p>
	</logic:equal>
	
</logic:present>