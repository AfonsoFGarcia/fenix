<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%-- Presenting errors --%>
<logic:messagesPresent>
<span class="error">
	<html:errors/>
</span><br/>
</logic:messagesPresent>


<html:form action="/editGrantInsurance" style="display:inline">

	<p class="infoselected">
	<b><bean:message key="label.grant.contract.information"/></b><br/>
	<bean:message key="label.grant.contract.contractnumber"/>:&nbsp;<bean:write name="contractNumber"/><br/>
	</p>

<strong><p align="center"><bean:message key="label.grant.insurance.edition"/></p></strong><br/>

	
	<html:hidden property="method" value="doEdit"/>
	<html:hidden property="page" value="1"/>

	<%-- In case of validation error --%>
	<%-- grant contract --%>
	<html:hidden property="idContract"/> 
	<%-- grant owner  --%>
	<html:hidden property="idGrantOwner"/>	
	<%-- grant insurance --%>
	<html:hidden property="idGrantInsurance"/>
	<html:hidden property="totalValue"/>
	
	
	<table>
		<tr>
			<td align="left">
				<bean:message key="label.grant.insurance.totalValue"/>:&nbsp;
			</td>
			<td>
				<logic:present name="editGrantInsuranceForm" property="totalValue">
					<bean:define id="totalValue" name="editGrantInsuranceForm" property="totalValue"/>
					<%-- This part of the code is used to truncate the totalValue--%>
					<% 
						int totalValueTruncated = (int)(((Double)totalValue).doubleValue() / 100);
						double totalValueRounded = ((double)totalValueTruncated) * 100;
						totalValue = new Double(totalValueRounded);
					%>
					<p><bean:write name="totalValue"/>&nbsp;<bean:message key="label.euroSymbol"/></p>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.insurance.dateBeginInsurance"/>:&nbsp;
			</td>
			<td>
				<html:text property="dateBeginInsurance" size="10"/>&nbsp;<bean:message key="label.requiredfield"/><bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
		<td align="left">
			<bean:message key="label.grant.insurance.dateEndInsurance"/>:&nbsp;
		</td>
		<td>
			<html:text property="dateEndInsurance" size="10"/>&nbsp;<bean:message key="label.dateformat"/>
		</td>
		</tr>
		<tr>
			<td align="left" colspan="2">
				<bean:message key="label.grant.insurance.grantPaymentEntity.number"/> &nbsp;<bean:message key="label.requiredfield"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="label.grant.insurance.paymentEntity.project"/>:&nbsp;
			</td>
			<td>
				<html:text property="project" size="10"/>&nbsp; 
					<html:link page='<%= "/showPaymentEntitiesList.do?method=showForm&amp;project=1" %>' target="_blank">
						<bean:message key="link.grantproject.showList"/>
					</html:link>
			</td>
		</tr>
		<tr>
			<td align="left">
				&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="label.grant.insurance.paymentEntity.costCenter"/>:&nbsp;
			</td>
			<td>
				<html:text property="costcenter" size="10"/>&nbsp; 
				<html:link page='<%= "/showPaymentEntitiesList.do?method=showForm&amp;costcenter=1" %>' target="_blank">
					<bean:message key="link.grantcostcenter.showList"/>
				</html:link>
			</td>
		</tr>
	</table>

	<br/>

	<table>
		<tr>
			<td>
				<html:submit styleClass="inputbutton">
					<bean:message key="button.save"/>
				</html:submit>

</html:form>
			</td>
			<td>
				<html:form action="/manageGrantContract" style="display:inline">
				<html:hidden property="method" value="prepareManageGrantContractForm"/>
				<html:hidden property="page" value="1"/>
				<html:hidden property="idInternal" value='<%= request.getAttribute("idGrantOwner").toString() %>'/>
					<html:submit styleClass="inputbutton" style="display:inline">
						<bean:message key="button.cancel"/>
					</html:submit>
				</html:form>
			</td>
		</tr>
	</table>	
<br/><br/>

<logic:present name="editGrantInsuranceForm" property="idGrantInsurance">
	<bean:message key="message.grant.contract.movement.manage"/>:&nbsp;
	<bean:define id="idContract" name="editGrantInsuranceForm" property="idContract"/>
	<bean:define id="idGrantOwner" name="editGrantInsuranceForm" property="idGrantOwner"/>
	<html:link page='<%= "/manageGrantContractMovement.do?method=prepareManageGrantContractMovement&amp;idContract=" + idContract + "&amp;idGrantOwner=" + idGrantOwner %>'>
		<bean:message key="link.grant.contract.movement.manage"/>
	</html:link>
</logic:present>