<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoLesson"%>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoShift"%>

<%@ page import="java.util.Calendar" %>




<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />

	
	
	<h2><bean:message key="title.StudentGroupInformation"/></h2>
	
	<bean:define id="infoStudentGroup" name="component" property="infoStudentGroup"/>
	<bean:define id="studentGroupCode" name="infoStudentGroup" property="idInternal"/>
	<bean:define id="infoAttends" name="infoStudentGroup" property="infoAttends"/>
	<bean:define id="infoGrouping" name="infoStudentGroup" property="infoGrouping"/>
	<bean:define id="groupPropertiesCode" name="infoGrouping" property="idInternal"/>
	
<logic:empty name="component" property="infoSiteStudentInformationList">
	<logic:lessEqual name="ShiftType" value="2">
		<div class="infoop2">
			<bean:message key="label.teacher.emptyStudentGroupInformation.normalShift.description" />
		</div>
	</logic:lessEqual>	
	<logic:greaterEqual name="ShiftType" value="3">
		<div class="infoop2">
			<bean:message key="label.teacher.emptyStudentGroupInformation.notNormalShift.description" />
		</div>
	</logic:greaterEqual>
</logic:empty>	

<logic:notEmpty name="component" property="infoSiteStudentInformationList">
	<logic:lessEqual name="ShiftType" value="2">
		<div class="infoop2">
			<bean:message key="label.teacher.viewStudentGroupInformation.normalShift.description" />
		</div>
	</logic:lessEqual>
	<logic:greaterEqual name="ShiftType" value="3">
		<div class="infoop2">
			<bean:message key="label.teacher.viewStudentGroupInformation.notNormalShift.description" />
		</div>
	</logic:greaterEqual> 
</logic:notEmpty>


	<logic:lessEqual name="ShiftType" value="2">
	<bean:define id="infoShift" name="infoStudentGroup" property="infoShift"/>	
	<bean:define id="shiftCode" name="infoShift" property="idInternal"/>

	<table class="tstyle4 tdcenter">	
		<tr>
			<th width="30%" rowspan="2">
				<bean:message key="property.shift"/>
			</th>
			<th colspan="4" width="70%"> 
				<bean:message key="property.lessons"/>
			</th>
		</tr>
		<tr>
			<th width="25%">
				<bean:message key="property.lesson.weekDay"/>
			</th>
			<th width="15%">
				<bean:message key="property.lesson.beginning"/>
			</th>
			<th width="15%">
				<bean:message key="property.lesson.end"/>
			</th>
			<th width="15%">
				<bean:message key="property.lesson.room"/>
			</th>
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
						
						<td  rowspan="<%=((InfoShift) infoShift).getInfoLessons().size() %>">
							<bean:write name="infoShift" property="nome"/>
						</td>
						<td>
							<bean:write name="infoLesson" property="diaSemana"/> &nbsp;
						</td>
						<td>
							<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
						</td>
						<td>
							<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>								
						</td>
							
		               	<td>
		               		<logic:notEmpty name="infoLesson" property="infoSala">
								<bean:write name="infoLesson" property="infoSala.nome"/>
							</logic:notEmpty>	
				 		</td>
				
				 	</tr>
				</logic:iterate>
				
				<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" offset="1">
                       <% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
                       <% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
						<tr >
							<td>
								<bean:write name="infoLesson" property="diaSemana"/> &nbsp;
							</td>
							<td>
								<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
							</td>
							<td>
								<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>
							</td>
							<td>
								<logic:notEmpty name="infoLesson" property="infoSala">
									<bean:write name="infoLesson" property="infoSala.nome"/>
								</logic:notEmpty>	
							</td>
						</tr>
					</logic:iterate>
	</table>

</logic:lessEqual>


<p>
	<span class="error0"><!-- Error messages go here --><html:errors /></span>
</p>


<logic:empty name="component" property="infoSiteStudentInformationList">

<ul>
	<li>		
		<html:link page="<%="/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()%>">
			<bean:message key="link.backToShiftsAndGroups"/>
		</html:link>
	</li>
</ul>
		
	<bean:define id="nrOfElements" name="component" property="nrOfElements"/>

	<p> 		
		<b><bean:message key="label.nrOfElements"/></b> <bean:write name="nrOfElements"/>
	</p>

	<p>
		<span class="warning0"><bean:message key="message.infoSiteStudentGroupList.not.available" /></span>
	</p>
	
	<p>
		<bean:message key="label.groupManagement"/>&nbsp&nbsp
		
		<logic:lessEqual name="ShiftType" value="2">
		<bean:define id="infoShift" name="infoStudentGroup" property="infoShift"/>	
		<bean:define id="shiftCode" name="infoShift" property="idInternal"/>
		<html:link page="<%="/editStudentGroupMembers.do?method=prepareEditStudentGroupMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString() + "&amp;shiftCode=" + shiftCode.toString()%>">
	    	<bean:message key="link.editGroupMembers"/>
	    </html:link>
	 	</logic:lessEqual>
	 	
		<logic:greaterEqual name="ShiftType" value="3">
		<html:link page="<%="/editStudentGroupMembers.do?method=prepareEditStudentGroupMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.editGroupMembers"/>
	    </html:link>&nbsp|&nbsp
	 	</logic:greaterEqual> 
	 
	 
	   <logic:equal name="ShiftType" value="1">
	    <bean:define id="infoShift" name="infoStudentGroup" property="infoShift"/>	
		<bean:define id="shiftCode" name="infoShift" property="idInternal"/>
	    
	    <html:link page="<%="/editStudentGroupShift.do?method=prepareEditStudentGroupShift&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;shiftCode=" + shiftCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.editGroupShift"/></html:link>&nbsp|&nbsp
		</logic:equal>
		
		<logic:equal name="ShiftType" value="3">
	    <html:link page="<%="/enrollStudentGroupShift.do?method=prepareEnrollStudentGroupShift&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.enrollStudentGroupInShift"/></html:link>&nbsp|&nbsp
		</logic:equal>
		
		<logic:equal name="ShiftType" value="2">
	    <html:link page="<%="/unEnrollStudentGroupShift.do?method=unEnrollStudentGroupShift&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.unEnrollStudentGroupShift"/></html:link>&nbsp|&nbsp
		</logic:equal>
		
		<logic:lessEqual name="ShiftType" value="2">
		<bean:define id="infoShift" name="infoStudentGroup" property="infoShift"/>	
		<bean:define id="shiftCode" name="infoShift" property="idInternal"/>
		<html:link page="<%= "/deleteStudentGroup.do?method=deleteStudentGroup&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;shiftCode=" + shiftCode.toString() +"&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.deleteGroup"/></html:link>
		</logic:lessEqual>
		
		<logic:greaterEqual name="ShiftType" value="3">
		<html:link page="<%= "/deleteStudentGroup.do?method=deleteStudentGroup&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.deleteGroup"/></html:link>
		</logic:greaterEqual> 
	</p>
	
	
	</logic:empty> 
	
	<logic:notEmpty name="component" property="infoSiteStudentInformationList">
	
	<ul class="mbottom05">
		<li>
			<html:link page="<%="/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()%>">
		    	<bean:message key="link.backToShiftsAndGroups"/>
			</html:link>
		</li>
	</ul>
		
	<bean:define id="nrOfElements" name="component" property="nrOfElements"/>
				 			 		

	 <% Map sendMailParameters = new TreeMap(request.getParameterMap());
        sendMailParameters.put("method","start");
		request.setAttribute("sendMailParameters",sendMailParameters);%>
	<bean:define id="sendMailLinkParameters" type="java.util.Map" name="sendMailParameters"/>
	<ul class="mtop05">
		<li>
		   <html:link page="/sendMailToWorkGroupStudents.do" name="sendMailLinkParameters">
				<bean:message key="link.sendEmailToAllStudents"/>
		   </html:link>
	   </li>
	</ul>

 	<p class="mtop15 mbottom1">
		<b><bean:message key="label.GroupNumber"/></b> <bean:write name="infoStudentGroup" property="groupNumber" /> <br/>
		<b><bean:message key="label.nrOfElements"/></b> <bean:write name="nrOfElements"/>
	</p>
	
	<table class="tstyle4 mtop05">
	<tr>
		<th width="16%"><bean:message key="label.numberWord" /></th>
		<th width="63%"><bean:message key="label.nameWord" /></th>
		<th width="26%"><bean:message key="label.emailWord" /></th>
	</tr>
			
	<logic:iterate id="infoSiteStudentInformation" name="component" property="infoSiteStudentInformationList">			
		<tr>		
			<td><bean:write name="infoSiteStudentInformation" property="number"/>
			</td>	
			
			<td><bean:write name="infoSiteStudentInformation" property="name"/>
			</td>		
			
			<td>
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




	<p>
		<b><bean:message key="label.groupManagement"/></b>&nbsp
		
		<logic:lessEqual name="ShiftType" value="2">
		<bean:define id="infoShift" name="infoStudentGroup" property="infoShift"/>	
		<bean:define id="shiftCode" name="infoShift" property="idInternal"/>
		<html:link page="<%="/editStudentGroupMembers.do?method=prepareEditStudentGroupMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString() + "&amp;shiftCode=" + shiftCode.toString()%>">
	    	<bean:message key="link.editGroupMembers"/>
	    </html:link>&nbsp|&nbsp
	 	</logic:lessEqual>
	 	
	 	<logic:greaterEqual name="ShiftType" value="3">
		<html:link page="<%="/editStudentGroupMembers.do?method=prepareEditStudentGroupMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.editGroupMembers"/>
	    </html:link>&nbsp|&nbsp
	 	</logic:greaterEqual> 
	 	
	    <logic:equal name="ShiftType" value="1">
	     <bean:define id="infoShift" name="infoStudentGroup" property="infoShift"/>	
		 <bean:define id="shiftCode" name="infoShift" property="idInternal"/>
	    <html:link page="<%="/editStudentGroupShift.do?method=prepareEditStudentGroupShift&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;shiftCode=" + shiftCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.editGroupShift"/></html:link><br/>
	    	</logic:equal>
	    	
	    <logic:equal name="ShiftType" value="3">
	    <html:link page="<%="/enrollStudentGroupShift.do?method=prepareEnrollStudentGroupShift&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.enrollStudentGroupInShift"/></html:link>&nbsp|&nbsp
		</logic:equal>
		
		<logic:equal name="ShiftType" value="2">
	    <html:link page="<%="/unEnrollStudentGroupShift.do?method=unEnrollStudentGroupShift&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.unEnrollStudentGroupShift"/></html:link>&nbsp|&nbsp
		</logic:equal>
	</p>
  
 	
</logic:notEmpty>
	 
</logic:present>

<logic:notPresent name="siteView" property="component">
<p>
	<span class="warning0"><bean:message key="message.infoSiteStudentGroupList.not.available" /></span>
</p>
</logic:notPresent>
