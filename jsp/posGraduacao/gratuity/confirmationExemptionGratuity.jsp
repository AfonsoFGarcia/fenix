<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="Util.ExemptionGratuityType" %>

<span class="error"><html:errors/></span>

<logic:present name="removeExemptionGratuity">
	<h2><center>
	<logic:equal name="removeExemptionGratuity" value="true">
		<bean:message key="message.masterDegree.gratuity.successRemoveExemptionGratuity" />
	</logic:equal>
	<logic:equal name="removeExemptionGratuity" value="false">
		<bean:message key="error.impossible.removeExemptionGratuity" />
	</logic:equal>
	</center></h2>
</logic:present>

<logic:present name="exemptionGratuity">

<bean:define id="studentCurricularPlan" name="exemptionGratuity" property="infoStudentCurricularPlan" />
<logic:present name="studentCurricularPlan">
<bean:define id="studentCurricularPlanID" name="studentCurricularPlan" property="idInternal" />
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" class="infoselected">
			<b><bean:message key="student" /></b>&nbsp;<bean:write name="studentCurricularPlan" property="infoStudent.number" />&nbsp;<bean:write name="studentCurricularPlan" property="infoStudent.infoPerson.nome" /><br>
			<b><bean:message key="title.studentCurricularPlan"/></b>&nbsp;<bean:write name="studentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>&nbsp;
			<bean:define id="initialDate" name="studentCurricularPlan" property="infoDegreeCurricularPlan.initialDate" />		
			<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")+1) %>
			<logic:notEmpty name="studentCurricularPlan" property="infoDegreeCurricularPlan.endDate">
				<bean:define id="endDate" name="studentCurricularPlan" property="infoDegreeCurricularPlan.endDate" />	
			-<%= endDate.toString().substring(endDate.toString().lastIndexOf(" ")) %>
			</logic:notEmpty>
		</td>
	</tr>
</table>
<p />
</logic:present> 

<h2><center><bean:message key="message.masterDegree.gratuity.successInsertExemptionGratuity" /></center></h2>

<b><bean:message key="label.exemptionGratuity.value" />:</b>&nbsp;
<logic:notEqual name="exemptionGratuity" property="exemptionPercentage" value="-1">
	<bean:write name="exemptionGratuity" property="exemptionPercentage"/>		
</logic:notEqual>
<logic:equal name="exemptionGratuity" property="exemptionPercentage" value="-1">
	<bean:write name="exemptionGratuity" property="otherValueExemptionGratuity"/>		
</logic:equal>
<br />

<b><bean:message key="label.exemptionGratuity.justification" />:</b>&nbsp;
<logic:notEqual name="exemptionGratuity" property="exemptionType.value" value="<%= String.valueOf(ExemptionGratuityType.OTHER_TYPE) %>">
	<bean:define id="exemptionGratuityName" name="exemptionGratuity" property="exemptionType.name"/>
	<bean:define id="exemptionGratuityNameKEY" value="<%= "label.exemptionGratuity."+exemptionGratuityName.toString() %>"/>
	<bean:message name="exemptionGratuityNameKEY"/>&nbsp;
</logic:notEqual>		
<logic:equal name="exemptionGratuity" property="exemptionType.value" value="<%= String.valueOf(ExemptionGratuityType.OTHER_TYPE) %>">
	<bean:write name="exemptionGratuity" property="exemptionDescription" />
</logic:equal>		
<br /><br />	

<logic:notPresent name="exemptionGratuity" property="infoGratuityValues">
	<span class="error"><bean:message key="error.impossible.noGratuityValues" /></span>
</logic:notPresent>

</logic:present>
