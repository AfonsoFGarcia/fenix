<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>	

<!-- Import new CSS for this section: #navlateral  -->
<style>@import url(<%= request.getContextPath() %>/CSS/navlateralnew.css);</style>

<logic:present role="DEPARTMENT_MEMBER">
	
	<bean:define id="userView" name="<%= SessionConstants.U_VIEW %>" scope="session"/>
	<!-- Temporary solution (until we make expectations available for all departments) DEI Code = 28 -->
	<% String deiCode = "28"; %>
	<ul>
		<li class="navheader">
			<bean:message key="link.group.general.title" />
		</li>
		<li>
			<html:link page="/viewDepartmentTeachers/listDepartmentTeachers.faces">
				<bean:message key="link.departmentTeachers"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</html:link>
		</li>
		<li>
			<html:link page="/viewTeacherService/chooseExecutionYear.faces">
				<bean:message key="link.teacherService"/>
			</html:link>
		</li>
				
<%--		<li class="navheader">
			<bean:message key="link.group.individual.title" />		
		</li>
-->
	</ul>
	
	<br />
</logic:present>

