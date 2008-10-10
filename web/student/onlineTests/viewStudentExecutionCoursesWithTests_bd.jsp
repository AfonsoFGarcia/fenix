<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.student.portalTitle"/></em>
<h2><bean:message key="link.tests"/></h2>

<logic:present name="registrationSelectExecutionYearBean">

	<fr:form action="/studentTests.do?method=viewStudentExecutionCoursesWithTests">
		<fr:edit id="executionYear" name="registrationSelectExecutionYearBean" slot="executionYear">
			<fr:layout name="menu-select-postback">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.StudentExecutionYearsProvider" />
				<fr:property name="choiceType" value="net.sourceforge.fenixedu.domain.ExecutionYear" />
				<fr:property name="format" value="${year}" />
				<fr:property name="destination" value="postBack"/>
			</fr:layout>
		</fr:edit>
		<p><html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton">
			<bean:message key="button.submit" />
		</html:submit></p>
	</fr:form>
</logic:present>
<br/>

<logic:present name="studentExecutionCoursesList">
	<logic:empty name="studentExecutionCoursesList">
		<p class="mvert15"><em><bean:message key="message.noStudentTests"/></em></p>
	</logic:empty>
	
	<logic:notEmpty name="studentExecutionCoursesList" >
	
	<table>
		<tr>
			<th class="listClasses-header"><bean:message key="label.curricular.course.acronym"/></th>
			<th class="listClasses-header"><bean:message key="label.curricular.course.name"/></th>
		</tr>
		<logic:iterate id="executionCourse" name="studentExecutionCoursesList" type="net.sourceforge.fenixedu.domain.ExecutionCourse">
		<tr>
			<td class="listClasses">
				<html:link page="/studentTests.do?method=testsFirstPage" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal">
					<bean:write name="executionCourse" property="sigla"/>
				</html:link>
			</td>
			<td class="listClasses">
				<html:link page="/studentTests.do?method=testsFirstPage" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal">
					<bean:write name="executionCourse" property="nome"/>
				</html:link>
			</td>
		</tr>
		</logic:iterate>
	</table>
	</logic:notEmpty>
</logic:present>

<logic:notPresent name="studentExecutionCoursesList">
	<p class="mvert15"><em><bean:message key="message.noStudentTests"/></em></p>
</logic:notPresent>