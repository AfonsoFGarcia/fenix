<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.lang.String" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoLesson"%>
<%@ page import="DataBeans.InfoShift"%>

<%@ page import="java.util.Calendar" %>



<span class="error"><html:errors/></span>
<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />

<table align="left" width="100%">
<tbody>
<tr>
<td>
	
	
	<h2><bean:message key="title.StudentGroupInformation"/></h2>
	<br/>
	
	<bean:define id="infoStudentGroup" name="component" property="infoStudentGroup"/>
	<bean:define id="studentGroupCode" name="infoStudentGroup" property="idInternal"/>
	<bean:define id="groupPropertiesCode" name="infoStudentGroup" property="infoGroupProperties.idInternal"/>
	<bean:define id="shiftCode" name="infoStudentGroup" property="infoShift.idInternal"/>

	
	<logic:empty name="component" property="infoSiteStudentInformationList">
		
	<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop">
					<bean:message key="label.teacher.emptyStudentGroupInformation.description" />
				</td>
			</tr>
	</table>
	<br />
	
	
	</logic:empty>	
	
	<logic:notEmpty name="component" property="infoSiteStudentInformationList">
	
	<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop">
					<bean:message key="label.teacher.viewStudentGroupInformation.description" />
				</td>
			</tr>
	</table>
	<br />
	
	
	</logic:notEmpty>	
	
	

	<table align="left" width="75%" cellpadding="0" border="0">	
	<tbody>		
	
		<tr >
			<td class="listClasses-header" width="30%" rowspan="2">
				<bean:message key="property.shift"/>
			</td>
			<td class="listClasses-header" colspan="4" width="70%"> 
				<bean:message key="property.lessons"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-header" width="25%">
				<bean:message key="property.lesson.weekDay"/>
			</td>
			<td class="listClasses-header" width="15%">
				<bean:message key="property.lesson.beginning"/>
			</td>
			<td class="listClasses-header" width="15%">
				<bean:message key="property.lesson.end"/>
			</td>
			<td class="listClasses-header" width="15%">
				<bean:message key="property.lesson.room"/>
			</td>
		</tr>
		
	 	<bean:define id="infoStudentGroup" name="component" property="infoStudentGroup"/>
		<bean:define id="infoShift" name="infoStudentGroup" property="infoShift"/>	
		<bean:define id="shiftCode" name="infoShift" property="idInternal"/>	
						
	 		<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" length="1" indexId="infoLessonIndex">
            		<% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
                	<% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
                	<% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
                	<% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
					<tr>
						
						<td  class="listClasses" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size() %>">
							<bean:write name="infoShift" property="nome"/>
						</td>
						<td class="listClasses">
							<bean:write name="infoLesson" property="diaSemana"/> &nbsp;
						</td>
						<td class="listClasses">
							<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
						</td>
						<td class="listClasses">
							<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>								
						</td>
							
		               	<td class="listClasses">
							<bean:write name="infoLesson" property="infoSala.nome"/>
				 		</td>
				
				 	</tr>
				</logic:iterate>
				
				<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" offset="1">
                       <% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
                       <% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
						<tr >
							<td class="listClasses">
								<bean:write name="infoLesson" property="diaSemana"/> &nbsp;
							</td>
							<td class="listClasses">
								<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
							</td>
							<td class="listClasses">
								<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>
							</td>
							<td class="listClasses">
								<bean:write name="infoLesson" property="infoSala.nome"/>
							</td>
						</tr>
					</logic:iterate>

        </tbody>
    
	</table>

</td>
</tr>

<tr>
<td>
	<br/>
<logic:empty name="component" property="infoSiteStudentInformationList">
		
<html:link page="<%="/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()%>">
    	<bean:message key="link.backToShiftsAndGroups"/></html:link><br/>
	<br/>
		
	<bean:define id="nrOfElements" name="component" property="nrOfElements"/>
				 			 		
	<b><bean:message key="label.nrOfElements"/> </b><bean:write name="nrOfElements"/>
	<br/>
	<h2><bean:message key="message.infoSiteStudentGroupList.not.available" /></h2>
	<b><bean:message key="label.groupManagement"/></b>&nbsp
	<html:link page="<%="/editStudentGroupMembers.do?method=prepareEditStudentGroupMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString() + "&amp;shiftCode=" + shiftCode.toString()%>">
    	<bean:message key="link.editGroupMembers"/>
    </html:link>&nbsp|&nbsp
 
    
    <html:link page="<%="/editStudentGroupShift.do?method=prepareEditStudentGroupShift&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;shiftCode=" + shiftCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
    	<bean:message key="link.editGroupShift"/></html:link>&nbsp|&nbsp

	<html:link page="<%= "/deleteStudentGroup.do?method=deleteStudentGroup&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;shiftCode=" + shiftCode.toString() +"&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
    	<bean:message key="link.deleteGroup"/></html:link>
	
	</logic:empty> 
	
	<logic:notEmpty name="component" property="infoSiteStudentInformationList">
	
	
<html:link page="<%="/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()%>">
    	<bean:message key="link.backToShiftsAndGroups"/></html:link><br/>
	<br/>
		
	<bean:define id="nrOfElements" name="component" property="nrOfElements"/>
				 			 		
	
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
 
	 <b><bean:message key="label.nrOfElements"/> </b><bean:write name="nrOfElements"/>
	<br/>	  
	<tr>
		<td class="listClasses-header" width="16%"><bean:message key="label.numberWord" />
		</td>
		<td class="listClasses-header" width="63%"><bean:message key="label.nameWord" />
		</td>
		<td class="listClasses-header" width="26%"><bean:message key="label.emailWord" />
		</td>
	</tr>
	
		
	<logic:iterate id="infoSiteStudentInformation" name="component" property="infoSiteStudentInformationList">			
		<tr>		
			<td class="listClasses"><bean:write name="infoSiteStudentInformation" property="number"/>
			</td>	
			
			<td class="listClasses"><bean:write name="infoSiteStudentInformation" property="name"/>
			</td>		
			
			<td class="listClasses">
				<logic:present name="infoSiteStudentInformation" property="email">
					<bean:define id="mail" name="infoSiteStudentInformation" property="email"/>
					<html:link href="<%= "mailto:"+ mail %>"><bean:write name="infoSiteStudentInformation" property="email"/></html:link>
				</logic:present>
				<logic:notPresent name="infoSiteStudentInformation" property="email">
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
	<b><bean:message key="label.groupManagement"/></b>&nbsp
	<html:link page="<%="/editStudentGroupMembers.do?method=prepareEditStudentGroupMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString() + "&amp;shiftCode=" + shiftCode.toString()%>">
    	<bean:message key="link.editGroupMembers"/>
    </html:link>&nbsp|&nbsp
 
    
    <html:link page="<%="/editStudentGroupShift.do?method=prepareEditStudentGroupShift&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;shiftCode=" + shiftCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
    	<bean:message key="link.editGroupShift"/></html:link><br/>
  
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
<bean:message key="message.infoSiteStudentGroupList.not.available" />
</h2>
</logic:notPresent>
