<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />


<em><bean:message key="label.person.main.title" /></em>
<p><html:link page="/visualizePersonalInfo.do"><bean:message bundle="APPLICATION_RESOURCES" key="label.return"/></html:link></p>
 <h2><bean:message bundle="APPLICATION_RESOURCES" key="label.contact.validation.title"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
    <p><span class="infoop2"><!-- Error messages go here --><bean:write name="message" /></span>
    </p>
</html:messages>

<logic:notPresent name="isPhysicalAddress">
<logic:present name="valid">
	<logic:equal name="valid" value="true">
		<bean:define id="profileLink">
			<html:link page="/visualizePersonalInfo.do"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.contact.validation.success.profile"/></html:link>
		</bean:define>
		<span class="success0"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.contact.validation.success" arg0="<%=profileLink%>"/></span>
	</logic:equal>
	<logic:equal name="valid" value="false">
		<logic:notEqual name="tries" value="3">
			<bean:define id="availableTries">
				<bean:write name="tries"/>
			</bean:define>
			<p><span class="error0"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.contact.validation.invalid" arg0="<%=availableTries%>"/></span></p>
		</logic:notEqual>
	</logic:equal>
</logic:present>
<logic:equal name="valid" value="false">
	<logic:greaterThan name="tries" value="0">
		<form action="<%= request.getContextPath() + "/person/partyContacts.do"%>" method="post">
			<input type="hidden" name="method" value="inputValidationCode"/>
			<input type="hidden" name="partyContactValidation" value="<%= request.getAttribute("partyContactValidation") %>"/>
			C�digo Valida��o <input name="validationCode" type="text"/>
			<input type="submit" value="Validar">
		</form>
		<p class="mbottom2">
		<logic:equal name="canValidateRequests" value="true">
			<bean:define id="tokenRequestURL">
			<html:link page="/partyContacts.do?method=requestValidationToken" paramId="partyContactValidation" paramName="partyContactValidation">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.contact.validation.request.token.submit"/>
			</html:link>
			</bean:define>
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.contact.validation.request.token" arg0="<%= tokenRequestURL %>"/>
		</logic:equal>
		</p>
	</logic:greaterThan>
</logic:equal>
</logic:notPresent>

<logic:present name="isPhysicalAddress">
	<div class="mtop2">
	<fr:form action="/partyContacts.do?method=validatePhysicalAddress" encoding="multipart/form-data">
		<fr:edit id="physicalAddressBean" name="physicalAddressBean" schema="contacts.validate.PhysicalAddress">
			<fr:layout name="tabular-editable">
				<fr:property name="columnClasses" value=",,tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="/partyContacts.do?method=validatePhysicalAddressInvalid"/>
		</fr:edit>
		<html:submit styleId="submitBtn"><bean:message key="button.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
	</fr:form>
	</div>
</logic:present>
