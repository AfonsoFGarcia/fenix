<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<logic:present name="siteView"> 

<h2><bean:message key="title.insertGroupProperties"/></h2>

<br>

<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop">
			<bean:message key="label.teacher.insertGroupProperties.description" />
		</td>
	</tr>
</table>
<br>
<span class="error"><html:errors/></span>

<html:form action="/createGroupProperties">
<html:hidden property="page" value="1"/>
<u><bean:message key="message.insertGroupPropertiesData"/></u>
<br>
<br>


<table>
		<tr>
			<td><bean:message key="message.groupPropertiesName"/>
			</td>
			<td><html:text size="40" property="name" /></td>
		</tr>
	    
		<tr>
			<td><bean:message key="message.groupPropertiesProjectDescription"/></td>
			<td><html:textarea property="projectDescription" cols="30" rows="4"/></td>
		</tr>
		
    	<tr>
			<td><bean:message key="message.groupPropertiesEnrolmentBeginDay"/></td>
			<td><html:text size="10" property="enrolmentBeginDay" /></td>
			
		</tr>
		<tr>
			<td><bean:message key="message.groupPropertiesEnrolmentEndDay"/></td>
			<td><html:text size="10" property="enrolmentEndDay" /></td>
			<%--<td><span class="error"><html:errors property="enrolmentEndDay"/></span></td>--%>
		</tr>
    
    	<tr>
			<td><bean:message key="message.groupPropertiesEnrolmentPolicy"/></td>
			<td><html:select property="enrolmentPolicy">
	    		<html:option key="option.groupProperties.enrolmentPolicy.atomic" value="true"/>
	    		<html:option key="option.groupProperties.enrolmentPolicy.individual" value="false"/>
	    		</html:select>
	    	</td>
			
		</tr>
    
    	<tr>
			<td><bean:message key="message.groupPropertiesShiftType"/>
			<br><br></td>
			<td><html:select property="shiftType">
				<html:options name="shiftTypeValues" labelName="shiftTypeNames"/>
				</html:select>
			</td>
			
		</tr>

	    <tr>
			<td><bean:message key="message.groupPropertiesMaximumCapacity"/>
			<br><bean:message key="label.teacher.insertGroupProperties.MaximumCapacityDescription"/>
			<br>
			<br>
			</td>
			<td><html:text size="5" property="maximumCapacity" /></td>
			
		</tr>	
    	<tr>
			<td><bean:message key="message.groupPropertiesMinimumCapacity"/>
			<br><bean:message key="label.teacher.insertGroupProperties.MinimumCapacityDescription"/>
			<br>
			<br>
			</td>
			<td><html:text size="5" property="minimumCapacity" /></td>
			
		</tr>
		<tr>
			<td><bean:message key="message.groupPropertiesIdealCapacity"/>
			<br><bean:message key="label.teacher.insertGroupProperties.IdealCapacityDescription"/>
			<br>
			<br>
			</td>
			<td><html:text size="5" property="idealCapacity" /></td>
			
		</tr>

		<tr>
			<td><bean:message key="message.groupPropertiesGroupMaximumNumber"/></td>
			<td><html:text size="5" property="groupMaximumNumber" /></td>
			
		</tr>	


</table>
<br />
<br />

<table>
<tr>
	<td>
		<html:submit styleClass="inputbutton"><bean:message key="button.save"/>                    		         	
		</html:submit>       
	</td>
	<td>
		<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
		</html:reset>  
	</td>
		<html:hidden property="method" value="createGroupProperties"/>	
		<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />

		</html:form>

	

	
		<html:form action="/viewExecutionCourseProjects" method="get">
	<td>
		<html:cancel styleClass="inputbutton"><bean:message key="button.cancel"/>                    		         	
		</html:cancel>
	</td>
		<html:hidden property="method" value="viewExecutionCourseProjects"/>
		<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
		</html:form>
	
</tr>
</table>


</logic:present>

<logic:notPresent name="siteView">
<h2>
<bean:message key="message.insert.infoGroupProperties.not.available" />
</h2> 
</logic:notPresent> 