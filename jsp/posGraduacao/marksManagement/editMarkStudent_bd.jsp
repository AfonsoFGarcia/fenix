<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<span class="error"><html:errors/></span>
<h2><bean:message key="label.students.listMarks"/></h2>
<br />
<bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:<bean:write name="executionYear" />
&nbsp;-&nbsp;
<bean:message key="label.masterDegree.administrativeOffice.degree"/>:<bean:write name="degree" />
&nbsp;-&nbsp;
<bean:message key="label.masterDegree.administrativeOffice.curricularCourse"/>:<bean:write name="curricularCourse" />
<html:form action="/changeMarkDispatchAction?method=chooseStudentMarks">
	<table>
    <!-- Degree -->
    	<tr>
        	<td><bean:message key="label.number"/>: </td>
         	<td>
         		<html:text property="studentNumber" value=""/>      
         	</td>
        </tr>
	</table>
	<br />
	<html:hidden property="executionYear" value="<%= pageContext.findAttribute("executionYear").toString() %>" />
	<html:hidden property="degree" value="<%= pageContext.findAttribute("degree").toString() %>" />
	<html:hidden property="curricularCourse" value="<%= pageContext.findAttribute("curricularCourse").toString() %>" />
	<html:hidden property="curricularCourseCode" value="<%= pageContext.findAttribute("curricularCourseCode").toString() %>" />
	<html:hidden property="scopeCode" value="<%= pageContext.findAttribute("curricularCourseCode").toString() %>" />
	<html:hidden property="jspTitle" value="<%= pageContext.findAttribute("jspTitle").toString() %>" />
	<html:submit value="Seguinte" styleClass="inputbutton" property="ok"/>
</html:form>
