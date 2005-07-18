<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<logic:present name="studentExecutionCoursesList">
	<logic:empty name="studentExecutionCoursesList">
		<h2><bean:message key="message.noStudentTests"/></h2>
	</logic:empty>
	
	<logic:notEmpty name="studentExecutionCoursesList" >
	
	<table>
		<tr>
			<td class="listClasses-header"><bean:message key="label.curricular.course.acronym"/></td>
			<td class="listClasses-header"><bean:message key="label.curricular.course.name"/></td>
		</tr>
		<logic:iterate id="executionCourse" name="studentExecutionCoursesList" type="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse">
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
<h2><bean:message key="message.noStudentTests"/></h2>
</logic:notPresent>