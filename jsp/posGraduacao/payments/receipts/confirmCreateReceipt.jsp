<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.masterDegree.administrativeOffice.payments.confirmCreateReceipt" /></h2>
<hr>
<br/>

<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>

<strong><bean:message key="label.masterDegree.administrativeOffice.payments.person" /></strong>:
<fr:view name="createReceiptBean" property="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
	</fr:layout>
</fr:view>

<br/>
<strong><bean:message key="label.masterDegree.administrativeOffice.payments.contributor" /></strong>:
<fr:view name="createReceiptBean" property="contributorParty" schema="contributor.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
	</fr:layout>
</fr:view>

<br/>
<strong><bean:message key="label.masterDegree.administrativeOffice.payments" /></strong>:
<table>
  <tr>
  	<td>
	  	<fr:view name="createReceiptBean" property="selectedEntries" schema="entry.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
			<fr:property name="sortBy" value="whenRegistered=desc"/>
		</fr:layout>
		</fr:view>
	</td>
  </tr>
  <tr>
    <td  align="right"><strong><bean:message key="label.masterDegree.administrativeOffice.payments.totalAmount"/></strong>:<bean:define id="totalAmount" name="createReceiptBean" property="totalAmount" type="java.math.BigDecimal"/>&nbsp;<%= totalAmount.toPlainString() %>&nbsp;<bean:message key="label.masterDegree.administrativeOffice.payments.currencySymbol"/></td>
  </tr>
</table>

<bean:define id="personId" name="createReceiptBean" property="person.idInternal"/>

<fr:form action='<%= "/payments.do?personId=" + personId %>'>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="paymentsForm" property="method" />
	<fr:edit id="createReceiptBean" name="createReceiptBean" visible="false" />
	
	<span class="warning0"><bean:message key="label.masterDegree.administrativeOffice.payments.confirmCreateReceiptQuestion"/></span>
	<br/><br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='createReceipt';"><bean:message key="button.masterDegree.administrativeOffice.payments.createReceipt"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='backToShowOperations';"><bean:message key="button.masterDegree.administrativeOffice.payments.back"/></html:cancel>
</fr:form>
