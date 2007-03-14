<%@ page language="java" %>

<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />


	<logic:empty name="component" property="infoGroupPropertiesList">
		<h2><bean:message key="title.ExecutionCourseProjects"/></h2>
	
		<div class="infoop2">
			<logic:present name="hasProposals">
				<bean:message key="label.teacher.emptyProjectsAndLinkWithProposals.description" />
			</logic:present>
			<logic:notPresent name="hasProposals">
				<bean:message key="label.teacher.emptyProjectsAndLink.description" />
			</logic:notPresent>
		</div>
		
		<p><em class="warning0"><bean:message key="message.infoGroupPropertiesList.not.available" /></em></p>
		
		<ul>
			<li><html:link page="/createGroupProperties.do?method=prepareCreateGroupProperties" paramId="objectCode" paramName="objectCode" ><bean:message key="link.groupPropertiesDefinition"/></html:link></li>
			<logic:present name="hasProposals">
				<li>
					<html:link page="/viewNewProjectProposals.do?method=viewNewProjectProposals" paramId="objectCode" paramName="objectCode" ><bean:message key="link.executionCourseProposals.received"/></html:link>
				</li>
			</logic:present>
		</ul>
	</logic:empty>
	


 
 <logic:notEmpty name="component" property="infoGroupPropertiesList"> 
 
	<h2><bean:message key="title.ExecutionCourseProjects"/></h2>
	

	<div class="infoop2">
		<p>
		<logic:present name="hasProposals">
			<logic:present name="waitingAnswer">
				<bean:message key="label.teacher.viewProjectsAndLinkWithProposalsAndWaiting.description" />
			</logic:present>
			<logic:notPresent name="waitingAnswer">
				<bean:message key="label.teacher.viewProjectsAndLinkWithProposals.description" />
			</logic:notPresent>
		</logic:present>
		<logic:notPresent name="hasProposals">
			<logic:present name="waitingAnswer">
				<bean:message key="label.teacher.viewProjectsAndLinkAndWaiting.description" />
			</logic:present>
			<logic:notPresent name="waitingAnswer">
				<bean:message key="label.teacher.viewProjectsAndLink.description" />
			</logic:notPresent>
		</logic:notPresent>
		</p>
	</div>

	<p class="indent1">
		<span class="error0"><!-- Error messages go here --><html:errors /></span> 	
	</p>
	<ul class="mtop15">
		<li>
			<html:link page="/createGroupProperties.do?method=prepareCreateGroupProperties" paramId="objectCode" paramName="objectCode" ><bean:message key="link.groupPropertiesDefinition"/></html:link>
		</li>
		<logic:present name="hasProposals">
			<li>
				<html:link page="/viewNewProjectProposals.do?method=viewNewProjectProposals" paramId="objectCode" paramName="objectCode" ><bean:message key="link.executionCourseProposals.received"/></html:link>
			</li>
		</logic:present>
		<logic:present name="waitingAnswer">
			<li>
				<html:link page="/viewSentedProjectProposalsWaiting.do?method=viewSentedProjectProposalsWaiting" paramId="objectCode" paramName="objectCode" ><bean:message key="link.executionCourseProposals.sented"/></html:link>
			</li>
		</logic:present>
	</ul>

<table class="tstyle4">
        <tbody>
    	 <tr>
			<th width="20%" ><bean:message key="label.projectName" /></th>
			<th width="30%" ><bean:message key="label.projectDescription" /></th>
			<th width="30%" ><bean:message key="label.properties" /></th>
			<th width="20%" ><bean:message key="label.executionCourses" /></th>
		</tr>
            <logic:iterate id="infoGroupProperties" name="component" property="infoGroupPropertiesList" >
            	<tr>
                    <td class="acenter">
                        <b>
                        <html:link page="<%= "/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")%>" paramId="groupPropertiesCode" paramName="infoGroupProperties" paramProperty="idInternal">
						<bean:write name="infoGroupProperties" property="name"/></html:link>
						</b>
                    </td>
                    
                    <td class="acenter">
	             		<logic:notEmpty name="infoGroupProperties" property="projectDescription">
	                     	<bean:write name="infoGroupProperties" property="projectDescription" filter="false"/>
	                	</logic:notEmpty>
	             		<logic:empty name="infoGroupProperties" property="projectDescription">
	                     	<bean:message key="message.project.wihtout.description"/>
	                	</logic:empty>
                	</td>
                	
                	 <td  class="acenter">
                	 
                	 <logic:empty name="infoGroupProperties" property="maximumCapacity">
                	 <logic:empty name="infoGroupProperties" property="minimumCapacity">
                	 <logic:empty name="infoGroupProperties" property="groupMaximumNumber">
                	 	<bean:message key="message.project.wihtout.properties"/>
                	 </logic:empty>
                	 </logic:empty>
                	 </logic:empty>
                	 
                	 
                	 <logic:notEmpty name="infoGroupProperties" property="maximumCapacity">
                	 <bean:message key="label.teacher.viewProjectsAndLink.MaximumCapacity"/>: <bean:write name="infoGroupProperties" property="maximumCapacity"/>
                	 <br/>
                	 </logic:notEmpty>
                	 
                	 <logic:notEmpty name="infoGroupProperties" property="minimumCapacity">
                	 <bean:message key="label.teacher.viewProjectsAndLink.MinimumCapacity"/>: <bean:write name="infoGroupProperties" property="minimumCapacity"/>
                	 <br/>
                	 </logic:notEmpty>
                	 
                	 <logic:notEmpty name="infoGroupProperties" property="groupMaximumNumber">
                	 <bean:message key="message.groupPropertiesGroupMaximumNumber"/>: <bean:write name="infoGroupProperties" property="groupMaximumNumber"/>
                   	 </logic:notEmpty>
                	</td>
                	
                	<td class="acenter">
                		<bean:size id="count" name="infoGroupProperties" property="infoExportGroupings"/>
                		<logic:greaterThan name="count" value="1">
            		    <logic:iterate id="infoExportGrouping" name="infoGroupProperties" property="infoExportGroupings" >
                		<bean:define id="infoExecutionCourse" name="infoExportGrouping" property="infoExecutionCourse" />
							<bean:write name="infoExecutionCourse" property="nome"/><br/>
                    	 </logic:iterate>
                    		</logic:greaterThan>
						<logic:equal name="count" value="1">
							<bean:message key="message.project.wihtout.coavaliation"/>
                    	</logic:equal>
                    </td>
                	
                </tr>

            </logic:iterate>
            
            </tbody>
</table>
</logic:notEmpty>     
</logic:present>

<logic:notPresent name="siteView" property="component">
<h2>
<bean:message key="message.infoGroupPropertiesList.not.available" />
</h2>
</logic:notPresent>