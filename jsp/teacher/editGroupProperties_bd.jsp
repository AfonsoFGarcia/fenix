<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>




<logic:present name="siteView" property="component"> 

<h2><bean:message key="title.editGroupProperties"/></h2>

<bean:define id="infoSiteGroupProperties" name="siteView" property="component"/>
<bean:define id="groupProperties" name="infoSiteGroupProperties" property="infoGroupProperties"/>

<html:form action="/editGroupProperties">
<html:hidden property="page" value="1"/>
<br>

<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop">
			<bean:message key="label.teacher.editGroupProperties.description" />
		</td>
	</tr>
</table>
<br>

<h2><span class="error"><html:errors/></span></h2>
<br>
<table>
		<tr>
			<td><bean:message key="message.groupPropertiesName"/></td>
			<td><html:text size="40" name="groupProperties" property="name" /></td>
			
		</tr>
		
		<tr>
			<td><bean:message key="message.groupPropertiesProjectDescription"/></td>
			<td><html:textarea name="groupProperties" property="projectDescription" cols="30" rows="4"/></td>
			
		</tr>
		
	    <tr>
			<td><bean:message key="message.groupPropertiesEnrolmentBeginDay"/></td>
			<td><logic:empty name="groupProperties" property="enrolmentBeginDayFormatted">
			
				<html:text size="10" property="enrolmentBeginDayFormatted"/>
				<i><bean:message key="label.at" /></i>
				<html:text size="5" property="enrolmentBeginHourFormatted"/>
				<i>(dd/mm/aaaa <bean:message key="label.at" /> hh:mm)</i><br />
			</logic:empty>
			
			<logic:notEmpty name="groupProperties" property="enrolmentBeginDayFormatted">
				<html:text size="10" name="groupProperties" property="enrolmentBeginDayFormatted" />
				<i><bean:message key="label.at" /></i>
				<html:text size="5" name="groupProperties" property="enrolmentBeginHourFormatted"/>
				<i>(dd/mm/aaaa <bean:message key="label.at" /> hh:mm)</i><br />
			</logic:notEmpty>
			</td>
			
		</tr>
	    
 		 </tr>
	    	<tr>
			<td><bean:message key="message.groupPropertiesEnrolmentEndDay"/></td>
			<td>
			<logic:empty name="groupProperties" property="enrolmentEndDayFormatted">
				<html:text size="10" property="enrolmentEndDayFormatted"/>
				<i><bean:message key="label.at" /></i>
				<html:text size="5" property="enrolmentEndHourFormatted"/>
				<i>(dd/mm/aaaa <bean:message key="label.at" /> hh:mm)</i><br />				
			</logic:empty>
			<logic:notEmpty name="groupProperties" property="enrolmentEndDayFormatted">
				<html:text size="10" name="groupProperties" property="enrolmentEndDayFormatted" />
				<i><bean:message key="label.at" /></i>
				<html:text size="5" name="groupProperties" property="enrolmentEndHourFormatted"/>
				<i>(dd/mm/aaaa <bean:message key="label.at" /> hh:mm)</i><br />			
			</logic:notEmpty>
			</td>
			
		</tr>
			
		<bean:define id="enrolmentPolicyValue" name="enrolmentPolicyValue"/>
		<tr>
			<td><bean:message key="message.groupPropertiesEnrolmentPolicy"/></td>
			<td><html:select property="enrolmentPolicy">
			<html:option value="<%= enrolmentPolicyValue.toString() %>"><bean:write name="enrolmentPolicyName"/></html:option>
			<html:options name="enrolmentPolicyValues" labelName="enrolmentPolicyNames"/>
			</html:select></td>			
		</tr>
		<bean:define id="shiftTypeValue" type="java.lang.Object" value="SEM TURNO"/>
	    <logic:present 	name="groupProperties" property="shiftType">
		 	<bean:define id="shiftTypeValue" name="groupProperties" property="shiftType"/>
		</logic:present>
		
		<tr>
			<td><bean:message key="message.groupPropertiesShiftType"/></td>
			<td>
			<html:select property="shiftType" >
				<html:option value="<%= shiftTypeValue.toString() %>"><bean:write name="shiftTypeValue"/></html:option>
			<html:options collection="shiftTypeValues" property="value" labelProperty="label"/>
			</html:select>
			</td>		
		</tr>

		<tr>
			<td><bean:message key="message.groupPropertiesMaximumCapacity"/>
			<br><bean:message key="label.teacher.insertGroupProperties.MaximumCapacityDescription"/>
			<br>
			<br>
			</td>
			
			<td>
			<logic:empty name="groupProperties" property="maximumCapacity">
			<html:text size="5" property="maximumCapacity"/>
			</logic:empty>
			<logic:notEmpty name="groupProperties" property="maximumCapacity">
			<html:text size="5" name="groupProperties" property="maximumCapacity" />
			</logic:notEmpty>
			</td>
			
		</tr>	
    	<tr>
			<td><bean:message key="message.groupPropertiesMinimumCapacity"/>
			<br><bean:message key="label.teacher.insertGroupProperties.MinimumCapacityDescription"/>
			<br>
			<br>
			</td>
			<td>
			<logic:empty name="groupProperties" property="minimumCapacity">
			<html:text size="5" property="minimumCapacity"/>
			</logic:empty>
			<logic:notEmpty name="groupProperties" property="minimumCapacity">
			<html:text size="5" name="groupProperties" property="minimumCapacity" />
			</logic:notEmpty>
			</td>
			
		</tr>
		<tr>
			<td><bean:message key="message.groupPropertiesIdealCapacity"/>
			<br><bean:message key="label.teacher.insertGroupProperties.IdealCapacityDescription"/>
			<br>
			<br>
			</td>
			<td>
			<logic:empty name="groupProperties" property="idealCapacity">
			<html:text size="5" property="idealCapacity"/>
			</logic:empty>
			<logic:notEmpty name="groupProperties" property="idealCapacity">
			<html:text size="5" name="groupProperties" property="idealCapacity" />
			</logic:notEmpty>
			</td>
			
		</tr>
		<tr>
			<td><bean:message key="message.groupPropertiesGroupMaximumNumber"/></td>
			<td>
			<logic:empty name="groupProperties" property="groupMaximumNumber">
			<html:text size="5" property="groupMaximumNumber"/>
			</logic:empty>
			<logic:notEmpty name="groupProperties" property="groupMaximumNumber">
			<html:text size="5" name="groupProperties" property="groupMaximumNumber" />
			</logic:notEmpty>
			</td>
			
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
		<html:hidden property="method" value="editGroupProperties"/>	
		<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
		<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
		</html:form>

	
	
		<html:form action="/viewShiftsAndGroups" method="get">
	<td>
		<html:cancel styleClass="inputbutton"><bean:message key="button.cancel"/>                    		         	
		</html:cancel>
	</td>
		<html:hidden property="method" value="viewShiftsAndGroups"/>
		<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
		<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
	</html:form>

</tr>
</table>

</logic:present>



<logic:notPresent name="siteView" property="component"> 
<h2>
<bean:message key="message.infoGroupProperties.not.available" />
</h2>
</logic:notPresent>