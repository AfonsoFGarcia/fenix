<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<style>
form {
margin: 0;
padding: 0;
}
</style>


<h2><bean:message key="title.enrolmentGroup.insertNewGroup"/></h2>

	<div class="infoop">
		<p><strong><bean:message key="label.student.viewGroupEnrolment.description.title1" />:</strong><br/>
		<bean:message key="label.student.viewGroupEnrolment.description.2" /></p>
		<p><strong><bean:message key="label.student.viewGroupEnrolment.description.title2" />:</strong><br/>
		<bean:message key="label.student.viewGroupEnrolment.description.2" /></p>
	</div>


	<html:form action="/groupEnrolment" method="get">

	<br/>

	<h2><span class="error"><html:errors/></span></h2>		 

	<br/>

	<bean:define id="groupNumber" name="groupNumber"/>

		<p><b class="infoop3"><bean:message key="label.GroupNumber"/><bean:write name="groupNumber"/></b></p>
		<p><bean:message key="label.infoStudents.studentsWithoutGroup" /></p>

<logic:present name="infoUserStudent"> 

	<table class="style1" width="80%" cellpadding="0" border="0">	
		<tr>
		
		<td width="5%" class="listClasses-header">
		</td>
		<td width="10%" class="listClasses-header"><bean:message key="label.numberWord" />
		</td>
		<td width="35%" class="listClasses-header"><bean:message key="label.nameWord" />
		</td>
		<td width="20%" class="listClasses-header"><bean:message key="label.emailWord" />
		</td>
		</tr>
			<tr>	
			
				<td class="listClasses">
				<input type="checkbox" name="studentsAutomaticallyEnroled" checked disabled>
				</td>	
				
				<td class="listClasses"><bean:write name="infoUserStudent" property="number"/>
				</td>	
				<bean:define id="infoPerson" name="infoUserStudent" property="infoPerson"/>		
				<td class="listClasses"><bean:write name="infoPerson" property="nome"/>
				</td>
				<td class="listClasses"><bean:write name="infoPerson" property="email"/>
				</td>
	 		</tr>	
		</table>
	</logic:present>
	
<logic:present name="infoStudents"> 
	<logic:empty name="infoStudents">
	<h2>
	<bean:message key="message.infoStudents.not.available" />
	</h2>
	</logic:empty>
	
	<logic:notEmpty name="infoStudents">
	
	<br>
	<table class="style1" width="80%" cellpadding="0" border="0">	
		
	
		<logic:iterate id="infoStudent" name="infoStudents">			
			<tr>	
				<td width="5%" class="listClasses">
				<html:multibox property="studentsNotEnroled">
				<bean:define id="infoPerson" name="infoStudent" property="infoPerson" />
				<bean:write name="infoPerson" property="username"/>
				</html:multibox>
				</td>	
				<td width="10%" class="listClasses"><bean:write name="infoStudent" property="number"/>
				</td>	
				<bean:define id="infoPerson" name="infoStudent" property="infoPerson"/>		
				<td width="35%" class="listClasses"><bean:write name="infoPerson" property="nome"/>
				</td>
				<td width="20%" class="listClasses"><bean:write name="infoPerson" property="email"/>
				</td>
	 		</tr>	
	 	</logic:iterate>
	 

		</table>
		<br>
	</logic:notEmpty>
	</logic:present>


<html:hidden property="method" value="enrolment"/>
<html:hidden  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>
<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
<logic:present name="shiftCode"> 
<html:hidden  property="shiftCode" value="<%= request.getParameter("shiftCode") %>" />
</logic:present>
<html:hidden  property="groupNumber" value="<%= groupNumber.toString() %>" />

<table>
<tr>
	<td>
		<html:submit styleClass="inputbutton"><bean:message key="button.finalize.enrolment"/></html:submit>       
	</td>
	
	<td>
		<html:reset styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>  
		</html:form>
	</td>
	
	<td>
		<html:form action="/viewShiftsAndGroups" method="get">
		<html:cancel styleClass="inputbutton"><bean:message key="button.cancel"/></html:cancel>
		<html:hidden property="method" value="execute"/>
		<html:hidden  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>
		<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode")%>"/>		
		</html:form>
	</td>
</tr>
</table>
