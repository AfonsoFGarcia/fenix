<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:form action="/correctGrantContract">

	<%-- Presenting Errors --%>
	<logic:messagesPresent>
	<p align="center"><span class="error">
	<html:errors/>
	</span></p><br/>
	</logic:messagesPresent>    

	<table class="infoop">
	 <tr>
		<td rowspan=4><p class="emphasis-box">i</p></td>
	    <td><bean:message key="info.grant.correct.grantcontract.title"/></td>
	 </tr>
	 <tr>
	 	<td><bean:message key="info.grant.correct.grantcontract.grantownernumber"/>(<bean:message key="label.grant.owner.number"/>)</td>
	 </tr>
	 <tr>
	 	<td><bean:message key="info.grant.correct.grantcontract.contractnumber"/>(<bean:message key="label.grant.contract.number.correction"/>)</td>
	 </tr>
	 <tr>
	 	<td><bean:message key="info.grant.correct.grantcontract.newcontractnumber"/>(<bean:message key="label.grant.new.contract.number.correction"/>)</td>
	 </tr>
	</table><br><br>

	<html:hidden property="method" value="changeNumberContract"/>
	<html:hidden property="action" value="changeNumberContract"/>
	<html:hidden property="page" value="1"/>

	
	<%-- Change number of Grant Contract --%> 
	<table>
		<tr>
			<td align="left"><bean:message key="label.grant.owner.number"/>:&nbsp;</td>
			<td><html:text property="grantOwnerNumber" size="5"/></td>
		</tr>
		<tr>
			<td align="left"><bean:message key="label.grant.contract.number.correction"/>:&nbsp;</td>
			<td><html:text property="grantContractNumber" size="5"/></td>
		</tr>
		<tr>
			<td align="left"><bean:message key="label.grant.new.contract.number.correction"/>:&nbsp;</td>
			<td><html:text property="newGrantContractNumber" size="5"/></td>
		</tr>
	</table>
	
	<p><html:submit styleClass="inputbutton">
		<bean:message key="button.correct"/>
	</html:submit></p>
	
	<br/>
	<logic:present name="correctionNumber3">
	    <p><strong><bean:message key="message.grant.correction.successfull"/></strong></p><br/>
	</logic:present>
	
</html:form>
