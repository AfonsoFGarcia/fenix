<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="DataBeans.InfoLesson"%>
<%@ page import="DataBeans.InfoShift"%>

<%@ page import="java.util.Calendar" %>

<logic:present name="infoSiteStudentsAndGroups">

<table align="left" width="100%">
<tbody>
<tr>
<td>
	
	<br/>
	<h2><bean:message key="title.viewStudentsAndGroupsByShift"/></h2>
	<br/>
	

	<logic:empty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
		
	<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop">
					<bean:message key="label.teacher.viewStudentsAndGroupsByShift.description" />
				</td>
			</tr>
	</table>
	<br />
	
	
	</logic:empty>	
	
	<logic:notEmpty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
	
	<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop">
					<bean:message key="label.teacher.viewStudentsAndGroupsByShift.description" />
				</td>
			</tr>
	</table>
	
	</logic:notEmpty>		
	
<br/>
<span class="error"><html:errors/></span>
<br/>
<br/>
	
	
</td>
</tr>

<tr>
<td>
	

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
		
	 	<bean:define id="infoShift" name="infoSiteStudentsAndGroups" property="infoShift"/>
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


<span class="error"><html:errors/></span>
<br/>
<br/>


<logic:empty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
<html:link page="<%="/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
    	<bean:message key="link.backToShiftsAndGroups"/></html:link><br/>

	<br/>
		
	<h2><bean:message key="message.infoSiteStudentsAndGroupsList.not.available" /></h2>
 	
	</logic:empty> 
	
	
	
	<logic:notEmpty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
	<html:link page="<%="/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
    	<bean:message key="link.backToShiftsAndGroups"/></html:link><br/>
	

	<br/>
				 			 		
	
	<table width="75%" cellpadding="0" border="0">
	<tbody>
	
	<br/>
 	<bean:size id="count" name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList"/>
	<bean:message key="label.teacher.NumberOfStudentsInShift" /><%= count %>
	<br/>	
	<br/>
	
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
		<bean:define id="infoStudentGroup" name="infoSiteStudentAndGroup" property="infoStudentGroup"/>	
		<bean:define id="infoSiteStudentInformation" name="infoSiteStudentAndGroup" property="infoSiteStudentInformation"/>
		<tr>	
		
			<td class="listClasses">
			<bean:write name="infoStudentGroup" property="groupNumber"/>
			</td>
			
			<td class="listClasses">
			<bean:write name="infoSiteStudentInformation" property="number"/>
			</td>	
			
			<td class="listClasses">
			<bean:write name="infoSiteStudentInformation" property="name"/>
			</td>		
			
			<td class="listClasses">
			<bean:write name="infoSiteStudentInformation" property="email"/>
			</td>
		</tr>				
	 </logic:iterate>

</tbody>
</table>

<br/>
<br/>

  </logic:notEmpty>

</td>
  </tr>
 </tbody>
	
</table>
	 
</logic:present>
