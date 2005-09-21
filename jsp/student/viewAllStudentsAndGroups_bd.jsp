<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Map" %>

<logic:present name="infoSiteStudentsAndGroups">
	<h2><bean:message key="title.viewAllStudentsAndGroups"/></h2>

	<logic:empty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
		<div class="infoop"><bean:message key="label.student.viewAllStudentsAndGroups.description" /></div>
	</logic:empty>	
	
	<logic:notEmpty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
		<!--  <div class="infoop"><bean:message key="label.student.viewAllStudentsAndGroups.description" /></div> -->
	</logic:notEmpty>		


<span class="error"><html:errors/></span>


	<logic:empty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
			<p>
			<html:link page="<%="/viewShiftsAndGroups.do?method=execute&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
	   		<bean:message key="link.backToShiftsAndGroups"/></html:link> - <bean:message key="link.backToShiftsAndGroups.description"/>
			</p>
		<h2><bean:message key="message.infoSiteStudentsAndGroupsList.not.available" /></h2>
	</logic:empty>
	
	<logic:notEmpty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
			<p>
			<html:link page="<%="/viewShiftsAndGroups.do?method=execute&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
    		<bean:message key="link.backToShiftsAndGroups"/></html:link> - <bean:message key="link.backToShiftsAndGroups.description"/>
    		</p>



 	<bean:size id="count" name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList"/>
	<bean:message key="label.student.NumberOfStudents" /><%= count %>
	<br/>	
	<br/>
	
<table class="style1" width="75%" cellpadding="0" border="0">
	<tbody>

	<tr>
		<td class="listClasses-header" width="10%"><bean:message key="label.studentGroupNumber" />
		</td>
		<td class="listClasses-header" width="16%"><bean:message key="label.numberWord" />
		</td>
		<td class="listClasses-header" width="53%"><bean:message key="label.nameWord" />
		</td>
		<td class="listClasses-header" width="26%"><bean:message key="label.emailWord" />
		</td>
	</tr>
			
	<logic:iterate id="infoSiteStudentAndGroup" name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
	
		<bean:define id="infoSiteStudentInformation" name="infoSiteStudentAndGroup" property="infoSiteStudentInformation"/>
		<bean:define id="infoStudentGroup" name="infoSiteStudentAndGroup" property="infoStudentGroup"/>
		
		<tr>		
			<td class="listClasses"><bean:write name="infoStudentGroup" property="groupNumber"/>
			</td>
			
			<td class="listClasses"><bean:write name="infoSiteStudentInformation" property="number"/>
			</td>	
			
			<td class="listClasses"><bean:write name="infoSiteStudentInformation" property="name"/>
			</td>		
			
			<td class="listClasses">
					<bean:write name="infoSiteStudentInformation" property="email"/>
			</td>
		</tr>				
	 </logic:iterate>

</tbody>
</table>

<br/>


  </logic:notEmpty>

	 
</logic:present>
