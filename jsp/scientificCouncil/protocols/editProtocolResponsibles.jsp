<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>
<h2><bean:message key="title.protocols.edit"/></h2>


<h3 class="mtop15"><bean:message key="label.protocol.responsibles"/></h3>

<fr:form action="/protocols.do?method=removeResponsible">
<strong><bean:message key="label.protocol.ist"/></strong><br/>
<logic:notEmpty name="protocolFactory" property="responsibles">
<fr:view name="protocolFactory" property="responsibles" schema="show.protocol.responsible">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>
	</fr:layout>
</fr:view>
</logic:notEmpty>
<logic:empty name="protocolFactory" property="responsibles">
	<p><em><bean:message key="label.protocol.hasNone"/></em></p>
</logic:empty>
<br/>

<strong><bean:message key="label.protocol.partner"/></strong><br/>
<logic:notEmpty name="protocolFactory" property="partnerResponsibles">
<table>
	<tr>
		<th><bean:message key="label.person.name"/></th>
		<th><bean:message key="label.unit"/></th>
		<th></th>				
	</tr>
	<logic:iterate id="partnerResponsible" name="protocolFactory" property="partnerResponsibles">
	<tr>
		<td><bean:write name="partnerResponsible" property="name"/></td>
		<td><bean:write name="partnerResponsible" property="unitText"/></td>
		<td>
			<html:submit property="createNew">
				<bean:message key="button.remove" />
			</html:submit>
		</td>				
	</tr>
	</logic:iterate>
</table>
	
<fr:view name="protocolFactory" property="partnerResponsibles" schema="show.protocol.responsible">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>
		<fr:property name="link(delete)" value="<%="/protocols.do?method=removeISTResponsible&amp;protocolID"%>" />
		<fr:property name="key(delete)" value="label.remove" />
		<fr:property name="param(delete)" value="idInternal" />
	</fr:layout>
</fr:view>
</logic:notEmpty>
<logic:empty name="protocolFactory" property="partnerResponsibles">
	<p><em><bean:message key="label.protocol.hasNone"/></em></p>
</logic:empty>
</fr:form>
<br/>

<logic:notPresent name="createExternalPerson">
<logic:notPresent name="createExternalUnit">
<fr:form action="/protocols.do?method=editResponsibles">
<span class="error0">
	<html:errors/>
	<html:messages id="message" name="errorMessage" message="true">
		<bean:write name="message" />
		<br />
	</html:messages>
</span>
<logic:present name="needToCreatePerson">
	<div class="warning0">
		<strong><bean:message key="label.attention"/></strong>:<br/>
		<bean:message key="message.protocol.createNewPerson"/>
	</div>
</logic:present>

<logic:equal name="protocolFactory" property="istResponsible" value="true">
<fr:edit id="istResponsible" name="protocolFactory" schema="search.istResponsible">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight mtop05 dinline"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	<fr:destination name="changePersonType" path="/protocols.do?method=prepareEditResponsibles"/>
</fr:edit>
</logic:equal>

<logic:equal name="protocolFactory" property="istResponsible" value="false">
<fr:edit id="partnerResponsible" name="protocolFactory" schema="search.partnerResponsible">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight mtop05 dinline"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	<fr:destination name="changePersonType" path="/protocols.do?method=prepareEditResponsibles"/>
</fr:edit>
</logic:equal>


<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.insert" />
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="cancel">
		<bean:message key="button.back" />
	</html:cancel>
	<logic:present name="needToCreatePerson">
		<html:submit bundle="HTMLALT_RESOURCES" property="createNew">
			<bean:message key="button.insertNewExternalPerson" />
		</html:submit>
	</logic:present>
</p>
</fr:form>
</logic:notPresent>
</logic:notPresent>

<!-- Create External Person -->
<logic:present name="createExternalPerson">
<fr:form action="/protocols.do?method=createExternalResponsible">
<logic:present name="needToCreateUnit">
	<div class="warning0">
		<strong><bean:message key="label.attention"/></strong>:<br/>
		<bean:message key="message.protocol.createNewUnit"/>
	</div>
</logic:present>
<strong><bean:message key="label.protocol.inserNewExternalPerson"/></strong><br/>
<fr:edit id="responsible" name="protocolFactory" schema="partnerResponsible.fullCreation">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight mtop05 dinline"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>
<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.insert" />
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="cancel">
		<bean:message key="button.cancel" />
	</html:cancel>
	<logic:present name="needToCreateUnit">
		<html:submit bundle="HTMLALT_RESOURCES" property="createNew">
			<bean:message key="button.insertNewExternalUnit" />
		</html:submit>
	</logic:present>	
</p>
</fr:form>
</logic:present>

<!-- Create External Unit -->
<logic:present name="createExternalUnit">
<fr:form action="/protocols.do?method=createExternalPersonAndUnit">
<strong><bean:message key="label.protocol.inserNewExternalUnit"/></strong><br/>
<fr:view name="protocolFactory" schema="partnerUnit.fullCreation">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight mtop05 dinline"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:view>
<fr:edit id="protocolFactory" name="protocolFactory" visible="false"/>
<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.insert" />
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="cancel">
		<bean:message key="button.cancel" />
	</html:cancel>
</p>
</fr:form>
</logic:present>