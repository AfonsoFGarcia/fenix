<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<span class="error"><html:errors/></span>	
<br/>
<table>
<tr>
	<td class="listClasses-header" ><bean:message key="label.season"/></td>
	<td class="listClasses-header" ><bean:message key="label.day"/></td>
	<td class="listClasses-header" ><bean:message key="label.beginning"/></td>	
	<td class="listClasses-header" ><bean:message key="label.students.enrolled.exam"/></td>
</tr>
<logic:iterate id="exam" name="infoExamList">
	<bean:define id="idInternal" name="exam" property="inInternal"/>
<tr>
	<td class="listClasses"><bean:write name="exam" property="season"/></td>
	<td class="listClasses"><bean:write name="exam" property="date"/></td>
	<td class="listClasses"><bean:write name="exam" property="beginningHour"/></td>
	<td class="listClasses-header" >
		<html:link 
			page="<%= "/showStudentsEnrolledInExamAction.do?objectCode="+ pageContext.findAttribute("objectCode")+"&amp;examCode=" +pageContext.findAttribute("idInternal") %>" >
			<bean:message key="label.students.enrolled.exam"/></html:link></td>
</tr>
</logic:iterate>
</table>

