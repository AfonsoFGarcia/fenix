<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="title.studentListByCourse" /></h2>
<span class="error"><html:errors/></span>
<br />
<br />
<logic:present name="curricularCourses">
	<bean:message key="title.masterDegree.administrativeOffice.chooseCurricularCourse" />
	<br /><br />
	<bean:define id="path" value="/listStudentsByCourse" />
	<table>
		<!-- CurricularCourse -->
		<logic:iterate id="curricularCourseElem" name="curricularCourses">
		   	<bean:define id="courseID" name="curricularCourseElem" property="idInternal"/>
		   	<bean:define id="curricularCourse" name="curricularCourseElem" property="name"/>
			<html:link page="<%= path + ".do?method=chooseCurricularCourseByID&amp;courseID=" + courseID + "&amp;executionYear=" + pageContext.findAttribute("executionYear") + "&amp;degree=" + pageContext.findAttribute("degree") + "&amp;curricularCourse=" + pageContext.findAttribute("curricularCourse") %>">
				<bean:write name="curricularCourseElem" property="name"/>
			</html:link>
			<br />
		</logic:iterate>
	</table>
</logic:present>