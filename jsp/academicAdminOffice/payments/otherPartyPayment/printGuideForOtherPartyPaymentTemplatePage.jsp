<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<html:xhtml/>

<%@ page import="net.sourceforge.fenixedu.util.Money" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%--
	<title><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.printTemplates.guide"/></title>
--%>

<div style="font-family: Arial; padding: 0 1em;">


<table style="width: 100%;">
<tr>
	<td rowspan="2" style="width: 100px;">
		<img src="<%= request.getContextPath() %>/images/LogoIST.gif" alt="<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="LogoIST" bundle="IMAGE_RESOURCES" />"/>
	</td>
	<td style="padding-left: 1em;">
		<h3><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.printTemplates.institutionName.upper.case"/></h3>
		<b><bean:write name="currentUnit" property="name"/></b><br/>
		<b><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.costCenter"/> <bean:write name="currentUnit" property="costCenterCode"/></b>
		<hr size="1"/>
	</td>
</tr>
<tr>
	<td style="text-align: right;">
		<h3><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.guide"/></h3>
	</td>
</tr>
</table>




<bean:define id="person" name="createOtherPartyPayment" property="event.person" />

<p style="margin-top: 2em;"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.processFrom"/></strong></p>

<table>
	<tr>
		<td style="width: 300px"><bean:message key="label.net.sourceforge.fenixedu.domain.Person.name" bundle="APPLICATION_RESOURCES" />:</td>
		<td><bean:write name="person" property="name"/></td>
	</tr>
	<tr>
		<td><bean:message key="label.net.sourceforge.fenixedu.domain.Person.idDocumentType" bundle="APPLICATION_RESOURCES" />:</td>
		<td><bean:message name="person" property="idDocumentType.name" bundle="ENUMERATION_RESOURCES"/></td>
	</tr>
	<tr>
		<td><bean:message key="label.net.sourceforge.fenixedu.domain.Person.documentIdNumber" bundle="APPLICATION_RESOURCES" />:</td>
		<td><bean:write name="person" property="documentIdNumber"/></td>
	</tr>
</table>



<bean:define id="contributor" name="createOtherPartyPayment" property="contributorParty" />

<p style="margin-top: 2em;"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.guideForOtherPartyPayments.contributor"/></strong></p>

<table style="margin-bottom: 4em;">
	<tr>
		<td style="width: 300px"><bean:message key="label.net.sourceforge.fenixedu.domain.organizationalStructure.Party.name" bundle="APPLICATION_RESOURCES" />:</td>
		<td><bean:write name="contributor" property="name"/></td>
	</tr>
	<tr>
		<td><bean:message key="label.net.sourceforge.fenixedu.domain.Person.idDocumentType" bundle="APPLICATION_RESOURCES" />:</td>
		<td><bean:write name="contributor" property="socialSecurityNumber"/></td>
	</tr>
</table>



	<table style="width: 100%;">
		<tr>
			<td style="text-align: right;">
	     				<app:labelFormatter name="createOtherPartyPayment" property="event.description">
	     					<app:property name="enum" value="ENUMERATION_RESOURCES"/>
	     					<app:property name="application" value="APPLICATION_RESOURCES"/>
					<app:property name="default" value="APPLICATION_RESOURCES"/>	
	     				</app:labelFormatter>
			</td>
			<td style="text-align: right; width: 230px;">
				_______________
				<bean:define id="amountToPay" name="createOtherPartyPayment" property="amount" type="Money" /> <%= amountToPay.toPlainString() %><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.currencySymbol"/>
			</td>
		</tr>
	</table>




	<table style="width: 100%; padding-top: 1em;">
	<tr>
		<td style="text-align: right;">
			<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.totalAmountToPay"/></strong>
		</td>
		<td style="text-align: right; width: 230px;">
			<strong>_______________ <bean:define id="amountToPay" name="createOtherPartyPayment" property="amount" type="Money" /> <%= amountToPay.toPlainString() %><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.currencySymbol"/></strong>
		</td>
	</tr>
	</table>



	<p style="text-align: center; margin-top: 6em;">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.city"/>, <%= new java.text.SimpleDateFormat("dd MMMM yyyy", request.getLocale()).format(new java.util.Date()) %>
	</p>

	<p style="text-align: center; margin-top: 2em;">
		<b><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.theEmployee"/></b>
	</p>
	<p style="text-align: center;">
		___________________
	</p>





	<div style="margin-top: 15em;">
		<jsp:include page="/academicAdminOffice/payments/commons/footer.jsp" flush="true" />
	</div>


</div>



<div class="breakafter"></div>


</logic:present>
