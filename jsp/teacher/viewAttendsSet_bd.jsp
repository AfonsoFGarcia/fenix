<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Map" %>


<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />

<table align="left" width="100%">
<tbody>
<tr>
<td>
	
	<br/>
	<h2><bean:message key="title.attendsSetInformation"/></h2>
	<br/>
	
	<bean:define id="infoAttendsSet" name="component" property="infoAttendsSet"/>
	<bean:define id="attendsSetCode" name="infoAttendsSet" property="idInternal"/>
	<bean:define id="groupPropertiesCode" name="infoAttendsSet" property="infoGroupProperties.idInternal"/>


	
	<logic:empty name="infoAttendsSet" property="infoAttends">
		
	<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop">
					<bean:message key="label.teacher.emptyAttendsSet.description" />
				</td>
			</tr>
	</table>
	<br />
	
	
	</logic:empty>	
	
	<logic:notEmpty name="infoAttendsSet" property="infoAttends">
	
	<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop">
					<bean:message key="label.teacher.viewAttendsSet.description" />
				</td>
			</tr>
	</table>
	<br />
	
	
	</logic:notEmpty>		
	
<br/>
<span class="error"><html:errors/></span>
<br/>
<br/>
	
	
</td>
</tr>

<tr>
<td>
	<br/>



<logic:empty name="infoAttendsSet" property="infoAttends">

<html:link page="<%="/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()%>">
    	<bean:message key="link.backToShiftsAndGroups"/></html:link><br/>
	<br/>
		
	<h2><bean:message key="message.infoAttendsSet.not.available" /></h2>
	<b><bean:message key="label.attendsSetManagement"/></b>&nbsp
	<html:link page="<%="/editAttendsSetMembers.do?method=prepareEditAttendsSetMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;attendsSetCode=" + attendsSetCode.toString()%>">
    	<bean:message key="link.editAttendsSetMembers"/>
    </html:link>&nbsp|&nbsp
 	
	</logic:empty> 
	
	
	
	<logic:notEmpty name="infoAttendsSet" property="infoAttends">

<html:link page="<%="/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()%>">
    	<bean:message key="link.backToShiftsAndGroups"/></html:link><br/>
	<br/>
		
	
				 			 		
	
	<table width="75%" cellpadding="0" border="0">
	<tbody>
	 <% Map sendMailParameters = new TreeMap(request.getParameterMap());
        sendMailParameters.put("method","prepare");
		request.setAttribute("sendMailParameters",sendMailParameters);%>
	<bean:define id="sendMailLinkParameters" type="java.util.Map" name="sendMailParameters"/>
	   <html:link page="/sendMailToAllStudents.do" name="sendMailLinkParameters">
			<bean:message key="link.sendEmailToAllStudents"/><br/><br/>
	   </html:link>
	
	<br/>
 	<bean:size id="count" name="infoAttendsSet" property="infoAttends"/>
	<bean:message key="label.teacher.NumberOfStudents" /><%= count %>
	<br/>	
	<br/>
	
	<tr>
		<td class="listClasses-header" width="16%"><bean:message key="label.numberWord" />
		</td>
		<td class="listClasses-header" width="63%"><bean:message key="label.nameWord" />
		</td>
		<td class="listClasses-header" width="26%"><bean:message key="label.emailWord" />
		</td>
	</tr>
			
	<logic:iterate id="infoFrequentaWithAll" name="infoAttendsSet" property="infoAttends">
	
		<bean:define id="infoStudent" name="infoFrequentaWithAll" property="aluno"/>
		<bean:define id="infoPerson" name="infoStudent" property="infoPerson"/>
		
		<tr>		
			<td class="listClasses"><bean:write name="infoStudent" property="number"/>
			</td>	
			
			<td class="listClasses"><bean:write name="infoPerson" property="nome"/>
			</td>		
			
			<td class="listClasses">
				<logic:present name="infoPerson" property="email">
					<bean:define id="mail" name="infoPerson" property="email"/>
					<html:link href="<%= "mailto:"+ mail %>"><bean:write name="infoPerson" property="email"/></html:link>
				</logic:present>
				<logic:notPresent name="infoPerson" property="email">
					&nbsp;
				</logic:notPresent>
				
			</td>
		</tr>				
	 </logic:iterate>

</tbody>
</table>

<br/>
<br/>

<table width="70%" cellpadding="0" border="0">
<tbody>
	<b><bean:message key="label.attendsSetManagement"/></b>&nbsp
	<html:link page="<%="/editAttendsSetMembers.do?method=prepareEditAttendsSetMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;attendsSetCode=" + attendsSetCode.toString()%>">
    	<bean:message key="link.editAttendsSetMembers"/>
    </html:link>&nbsp|&nbsp
	<html:link page="<%="/deleteAttendsSetMembersByExecutionCourse.do?method=deleteAttendsSetMembersByExecutionCourse&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;attendsSetCode=" + attendsSetCode.toString()%>">
    	<bean:message key="link.deleteAttendsSetMembersByExecutionCourse"/>
    </html:link>&nbsp|&nbsp
    <html:link page="<%="/deleteAllAttendsSetMembers.do?method=deleteAllAttendsSetMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;attendsSetCode=" + attendsSetCode.toString()%>">
    	<bean:message key="link.deleteAllAttendsSetMembers"/>
    </html:link>&nbsp|&nbsp
    
    
    
    
 </tbody>
</table>   	
  </logic:notEmpty>

</td>
  </tr>
 </tbody>
	
</table>
	 
</logic:present>

<logic:notPresent name="siteView" property="component">
<h2>
<bean:message key="message.infoAttendsSet.not.available" />
</h2>
</logic:notPresent>
