<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.student.enrolment.without.rules" /></h2>
<span class="error"><html:errors/></span>
<br />
<%-- HELP --%>
<table width="100%">
	<tr>
		<td class="infoop">
			<strong><bean:message key="label.enroll" />:</strong>&nbsp;<bean:message key="message.help.enroll2" />
		</td>
	</tr>
</table>
<br /><br />
<logic:present name="infoStudentEnrolmentContext">
	<bean:define id="infoCurricularCoursesToEnroll" name="infoStudentEnrolmentContext" property="finalInfoCurricularCoursesWhereStudentCanBeEnrolled" />	
	<bean:size id="curricularCoursesSize" name="infoCurricularCoursesToEnroll" />
	<strong><bean:message key="message.student.unenrolled.curricularCourses" /></strong>
	<br />
	<logic:lessEqual  name="curricularCoursesSize" value="0">
		<br />
		<img src="<%= request.getContextPath() %>/images/icon_arrow.gif" />&nbsp;<bean:message key="message.no.curricular.courses.noname"/>
		<br /><br />
	</logic:lessEqual >
	<html:form action="/courseEnrolmentWithoutRulesManagerDA">
		<html:hidden property="method" value="enrollCourses"/>
		<html:hidden property="page" value="1"/>
		<html:hidden property="studentNumber" />
		<html:hidden property="executionYear" />
		<html:hidden property="degreeType" />
		<logic:greaterThan name="curricularCoursesSize" value="0">
			<table >
				<logic:iterate id="infoCurricularCourse" name="infoCurricularCoursesToEnroll">
					<bean:define id="infoCurricularCourseId" name="infoCurricularCourse" property="idInternal" />
					<tr>
						<td><bean:write name="infoCurricularCourse" property="name"/></td>
						<td><html:multibox property="curricularCourses" value="<%= infoCurricularCourseId.toString() %>" /></td>
					</tr>
				</logic:iterate>
			</table>
			<br/>
			<br />
			<html:submit styleClass="inputbutton">
				<bean:message key="button.enroll"/>
			</html:submit>
			<html:reset styleClass="inputbutton">
				<bean:message key="button.clean"/>
			</html:reset>
		</logic:greaterThan>
		<html:cancel styleClass="inputbutton" onclick="this.form.method.value='readEnrollments';this.form.submit();">
			<bean:message key="button.cancel"/>
		</html:cancel>
	</html:form>
</logic:present>