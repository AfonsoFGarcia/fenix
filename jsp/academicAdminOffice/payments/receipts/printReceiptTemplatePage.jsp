<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page import="org.joda.time.DateTime"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.util.Money" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">


<div style="font-family: Arial; padding: 0 1em;">


<table style="width: 95%;">
<tr>
	<td rowspan="2" style="width: 95%px;">
		<img src="<%= request.getContextPath() %>/images/LogoIST.gif" alt="<bean:message key="LogoIST" bundle="IMAGE_RESOURCES" />"/>
	</td>
	<td style="padding-left: 1em;">
		<h3><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.printTemplates.institutionName.upper.case"/></h3>
		<bean:define id="ownerUnit" name="receipt" property="ownerUnit" />
		<b><bean:write name="ownerUnit" property="name"/></b><br/>
		<b><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.printTemplates.costCenter"/> <bean:write name="ownerUnit" property="costCenterCode"/></b>
		<br/>
		<bean:define id="creatorUnit" name="receipt" property="creatorUnit" />
		<bean:define id="creatorUnitId" name="creatorUnit" property="idInternal" />
		<logic:notEqual name="ownerUnit" property="idInternal" value="<%=creatorUnitId.toString()%>">
			<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.issuedBy"/>:</strong> <bean:write name="creatorUnit" property="name"/>
		</logic:notEqual>
		<hr size="1"/>
	</td>
</tr>
<tr>
	<td style="text-align: right;">
		<bean:define id="original" name="original" />
		<logic:equal name="original" value="true">
			<strong><bean:message  key="label.payments.printTemplates.original" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong>
		</logic:equal>
		<logic:notEqual name="original" value="true">
			<strong><bean:message  key="label.payments.printTemplates.duplicate" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong>
		</logic:notEqual>
		<br/>
		<logic:equal name="receipt" property="annulled" value="true">
			<strong><bean:message  key="label.payments.printTemplates.annulledDocument" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			<br/>
		</logic:equal>
		<b>
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.printTemplates.receipt.receiptNumber"/>
			<bean:write name="receipt" property="numberWithSeries" />/<bean:write name="receipt" property="year" />
		</b>
		<logic:greaterEqual name="receipt" property="receiptsVersionsCount" value="2">
			<p><em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.receipt.secondPrintVersion"/></em></p>
		</logic:greaterEqual>
	</td>
</tr>
</table>




<p style="margin-bottom: 0.5em; margin-top: 1em;"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.processFrom"/></strong></p>

<table style="margin-top: 0.5em;">
	<tr>
		<td style="width: 300px"><bean:message key="label.net.sourceforge.fenixedu.domain.Person.name" bundle="APPLICATION_RESOURCES" />:</td>
		<td><bean:write name="receipt" property="person.name"/></td>
	</tr>
	<tr>
		<td><bean:message key="label.net.sourceforge.fenixedu.domain.Person.idDocumentType" bundle="APPLICATION_RESOURCES"/>:</td>
		<td><bean:message name="receipt" property="person.idDocumentType.name" bundle="ENUMERATION_RESOURCES"/></td>
	</tr>
	<tr>
		<td><bean:message key="label.net.sourceforge.fenixedu.domain.Person.documentIdNumber" bundle="APPLICATION_RESOURCES" />:</td>
		<td><bean:write name="receipt" property="person.documentIdNumber"/></td>
	</tr>
</table>



<p style="margin-bottom: 0.5em; margin-top: 2em;"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.receipt.contributor"/> </strong></p>

<table style="margin-top: 0.5em; margin-bottom: 4em;">
	<tr>
		<td style="width: 300px"><bean:message bundle="APPLICATION_RESOURCES" key="label.net.sourceforge.fenixedu.domain.organizationalStructure.Party.name" />:</td>
		<td><bean:write name="receipt" property="contributorParty.name" /></td>
	</tr>
	<tr>
		<td><bean:message key="label.net.sourceforge.fenixedu.domain.organizationalStructure.Party.socialSecurityNumber" bundle="APPLICATION_RESOURCES" />:</td>
		<td><bean:write name="receipt" property="contributorParty.socialSecurityNumber" /></td>
	</tr>
	<tr>
		<td><bean:message bundle="APPLICATION_RESOURCES" key="label.net.sourceforge.fenixedu.domain.organizationalStructure.Party.address" />:</td>
		<td><bean:write name="receipt" property="contributorParty.address" /></td>
	</tr>
	<logic:notEmpty name="receipt" property="contributorParty.areaCode">
		<tr>
			<td><bean:message key="label.net.sourceforge.fenixedu.domain.organizationalStructure.Party.areaCode" bundle="APPLICATION_RESOURCES" /></td>
			<td><bean:write name="receipt" property="contributorParty.areaCode" /> - <bean:write name="receipt" property="contributorParty.areaOfAreaCode" /></td>
		</tr>
	</logic:notEmpty>
</table>

	

					
	<logic:iterate id="entry" name="sortedEntries">
		<table style="width: 95%;">
			<tr>
				<td style="text-align: right;">
					<app:labelFormatter name="entry" property="description">
			   			<app:property name="enum" value="ENUMERATION_RESOURCES"/>
			    		<app:property name="application" value="APPLICATION_RESOURCES"/>
						<app:property name="default" value="APPLICATION_RESOURCES"/>
					</app:labelFormatter>
				</td>
				<td style="text-align: right; width: 190px;">
					_______________
					<bean:define id="amount" name="entry" property="originalAmount" type="Money" /> <%=amount.toPlainString()%><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.currencySymbol" />
				</td>
			</tr>
		</table>
	</logic:iterate>
	
	
	<table style="width: 95%; padding-top: 1em;">
	<tr>
		<td style="text-align: right;">
			<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.totalAmountReceived"/></strong>
		</td>
		<td style="text-align: right; width: 190px;">
			<strong>_______________ <bean:define id="totalAmount" name="receipt" property="totalAmount" type="Money" /><%=totalAmount.toPlainString()%><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.currencySymbol" /></strong>
		</td>
	</tr>
	</table>
  

	<p style="text-align: left; margin-top: 3em; font-size: 10pt;">
		<bean:message  key="label.payments.printTemplates.vatExemptionNotice" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		<br/>
		<bean:define id="receiptCreationDate" name="receipt" property="whenCreated" />
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.city"/>, <%= ((DateTime)receiptCreationDate).toString("dd MMMM yyyy", pt.utl.ist.fenix.tools.util.i18n.Language.getLocale()) %>
	</p>

	<p style="text-align: right; margin-top: 1em; padding-right: 15em; font-size: 10pt;">
		<b><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.theEmployee"/></b>
	</p>
	<p style="text-align: right; padding-right: 10em; font-size: 10pt;">
		_____________________________
	</p>


	<bean:size id="entriesSize" name="sortedEntries"/>
	<div style="<%= "margin-top: " + (9 - ((entriesSize - 1) * 2))  + "em;"%>">
		<jsp:include page="/academicAdminOffice/payments/commons/footer.jsp" flush="true" />
	</div>


</div>


</logic:present>
