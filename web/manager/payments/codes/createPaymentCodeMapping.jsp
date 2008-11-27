<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<logic:present role="MANAGER">

	<h2><bean:message key="label.create" bundle="MANAGER_RESOURCES" /></h2>

	<logic:messagesPresent message="true">
		<ul class="nobullet list6">
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>

	<fr:edit id="paymentCodeMappingBean" name="paymentCodeMappingBean" schema="PaymentCodeMappingBean.create"
			action="/payments.do?method=createPaymentCodeMapping">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		<fr:destination name="cancel" path="/payments.do?method=prepareViewPaymentCodeMappings" />
		<fr:destination name="invalid" path="/payments.do?method=createPaymentCodeMappingInvalid" />
	</fr:edit>

</logic:present>
