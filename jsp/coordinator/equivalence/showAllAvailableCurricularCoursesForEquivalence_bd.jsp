<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="enrolmentsList" name="<%= SessionConstants.ENROLMENT_LIST %>" scope="request"/>

<center>
	<h2><bean:message key="title.coordinator.equivalence"/></h2>
	<b><bean:message key="label.first.step.coordinator.equivalence"/></b>
	<br/>
	<br/>
</center>

<table border="1" width="100%">
	<tr>
		<th align="center"><bean:message key="label.curricular.course.name"/></th>
		<th align="center"><bean:message key="label.degree.name"/></th>
		<th align="center"><bean:message key="label.student.number"/></th>
	</tr>
	<logic:iterate id="infoEnrolment" name="enrolmentsList" indexId="index">
		<bean:define id="link">
			/equivalence.do?method=show&studentOID=<bean:write name="infoEnrolment" property="infoStudentCurricularPlan.infoStudent.idInternal"/>
		</bean:define>
		<tr>
			<td align="center">
				<html:link page="<%= pageContext.findAttribute("link").toString() %>">
					<bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularCourse.name"/>
				</html:link>
			</td>
			<td align="center">
				<html:link page="<%= pageContext.findAttribute("link").toString() %>">
					<bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome"/>
				</html:link>
			</td>
			<td align="center">
				<html:link page="<%= pageContext.findAttribute("link").toString() %>">
					<bean:write name="infoEnrolment" property="infoStudentCurricularPlan.infoStudent.number"/>
				</html:link>
			</td>
		</tr>
	</logic:iterate>
</table>
