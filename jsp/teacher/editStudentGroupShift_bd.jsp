<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.editStudentGroupShift"/></h2>

<logic:present name="siteView" property="component">
 <bean:define id="component" name="siteView" property="component" />

<html:form action="/editStudentGroupShift" method="get">
<html:hidden property="page" value="1"/>
<span class="error"><html:errors/></span>

<br>
<br>		 

<table width="50%" cellpadding="0" border="0">
		
		<tr>
			<td><bean:message key="message.editStudentGroupShift.oldShift"/></td>
			<td><bean:write name="shift" property="nome"/></td>
			<td><span class="error"><html:errors property="shiftType"/></span></td>
		</tr>
		
		<tr>
	 	<td><bean:message key="message.editStudentGroupShift"/></td>
		
		<td>
		<html:select property="shift" size="1">
    	<html:options collection="shiftsList" property="value" labelProperty="label"/>
    	</html:select>
    	</td>
			
			<td><span class="error"><html:errors property="shiftType"/></span></td>
		</tr>	

 
</table>
<br>
<html:submit styleClass="inputbutton"><bean:message key="button.change"/>                    		         	
</html:submit>
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>
<br>
<br>
<html:hidden property="method" value="editStudentGroupShift"/>
<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<html:hidden  property="shiftCode" value="<%= request.getParameter("shiftCode") %>" />
<html:hidden  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode") %>" />
<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />

</html:form>

</logic:present>