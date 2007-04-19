<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>
<em><bean:message key="title.scientificCouncil.portalTitle" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
<h2><bean:message key="title.protocols.edit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>


<fr:form action="/editProtocol.do">
<html:hidden bundle="HTMLALT_RESOURCES" name="protocolsForm" property="method" value="removeISTResponsible"/>
<html:hidden bundle="HTMLALT_RESOURCES" name="protocolsForm" property="responsibleID"/>
<fr:edit id="protocolFactory" name="protocolFactory" visible="false"/>


<!-- IST Responsibles -->
<div class="mtop2 mbottom15">
<p class="mbottom0"><strong><bean:message key="label.protocol.internalResponsibles" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
<logic:notEmpty name="protocolFactory" property="responsibles">
<table class="tstyle1">
	<tr>
		<th><bean:message key="label.person.name" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></th>
		<th><bean:message key="label.unit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></th>
		<th></th>				
	</tr>
	<logic:iterate id="responsible" name="protocolFactory" property="responsibles" type="net.sourceforge.fenixedu.domain.Person">
	<tr>
		<td><bean:write name="responsible" property="name"/></td>
		<td><bean:write name="responsible" property="unitText"/></td>
		<td>
			<html:submit onclick="<%= "this.form.responsibleID.value=" + responsible.getIdInternal().toString()%>">
				<bean:message key="button.remove" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
			</html:submit>
		</td>				
	</tr>
	</logic:iterate>
</table>
</logic:notEmpty>
<logic:empty name="protocolFactory" property="responsibles">
	<p><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>.</p>
</logic:empty>
</div>

<!-- Partner Responsibles -->
<div class="mbottom2">
<p class="mbottom0"><strong><bean:message key="label.protocol.externalResponsibles" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
<logic:notEmpty name="protocolFactory" property="partnerResponsibles">
<table class="tstyle1">
	<tr>
		<th><bean:message key="label.person.name" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></th>
		<th><bean:message key="label.unit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></th>
		<th></th>				
	</tr>
	<logic:iterate id="partnerResponsible" name="protocolFactory" property="partnerResponsibles" type="net.sourceforge.fenixedu.domain.Person">
	<tr>
		<td><bean:write name="partnerResponsible" property="name"/></td>
		<td><bean:write name="partnerResponsible" property="unitText"/></td>
		<td>
			<html:submit onclick="<%= "this.form.responsibleID.value=" + partnerResponsible.getIdInternal().toString() + ";this.form.method.value='removePartnerResponsible'"%>">
				<bean:message key="button.remove" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
			</html:submit>
		</td>				
	</tr>
	</logic:iterate>
</table>
</logic:notEmpty>

<logic:empty name="protocolFactory" property="partnerResponsibles">
	<p><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>.</p>
</logic:empty>
</div>
</fr:form>



<!-- Add responsible -->
<logic:notPresent name="createExternalPerson">
<logic:notPresent name="createExternalUnit">
<fr:form action="/editProtocol.do?method=editResponsibles">

<p>
<span class="error0">
	<html:errors bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
	<html:messages id="message" name="errorMessage" message="true" bundle="SCIENTIFIC_COUNCIL_RESOURCES">
		<bean:write name="message" />
	</html:messages>
</span>
</p>

<logic:present name="needToCreatePerson">
	<div class="warning0">
		<strong><bean:message key="label.attention" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>:</strong><br/>
		<bean:message key="message.protocol.createNewPerson" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
	</div>
</logic:present>

<logic:equal name="protocolFactory" property="istResponsible" value="true">
<fr:edit id="istResponsible" name="protocolFactory" schema="search.istResponsible">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight mvert05"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	<fr:destination name="changePersonType" path="/editProtocol.do?method=prepareEditResponsibles"/>
</fr:edit>
</logic:equal>

<logic:equal name="protocolFactory" property="istResponsible" value="false">
<fr:edit id="partnerResponsible" name="protocolFactory" schema="search.partnerResponsible">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight mvert05"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	<fr:destination name="changePersonType" path="/editProtocol.do?method=prepareEditResponsibles"/>
</fr:edit>
</logic:equal>

<p class="mtop05">
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.insert" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
	</html:submit>
	<logic:notPresent name="needToCreatePerson">
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="back">
			<bean:message key="button.back" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
		</html:cancel>
	</logic:notPresent>
	<logic:present name="needToCreatePerson">
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="cancel">
			<bean:message key="button.cancel" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
		</html:cancel>	
		<html:submit bundle="HTMLALT_RESOURCES" property="createNew">
			<bean:message key="button.insertNewExternalPerson" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
		</html:submit>
	</logic:present>
</p>
</fr:form>
</logic:notPresent>
</logic:notPresent>

<!-- Create External Person -->
<logic:present name="createExternalPerson">
<fr:form action="/editProtocol.do?method=createExternalResponsible">
<logic:present name="needToCreateUnit">
	<div class="warning0">
		<strong><bean:message key="label.attention" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>:</strong><br/>
		<bean:message key="message.protocol.createNewUnit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
	</div>
</logic:present>

<p class="mbottom0"><strong><bean:message key="label.protocol.insertNewExternalPerson" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>:</strong></p>
<fr:edit id="responsible" name="protocolFactory" schema="partnerResponsible.fullCreation">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight mvert05"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>
<p class="mtop05">
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.insert" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="cancel">
		<bean:message key="button.cancel" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
	</html:cancel>
	<logic:present name="needToCreateUnit">
		<html:submit bundle="HTMLALT_RESOURCES" property="createNew">
			<bean:message key="button.insertNewExternalUnit" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
		</html:submit>
	</logic:present>	
</p>
</fr:form>
</logic:present>

<!-- Create External Unit -->
<logic:present name="createExternalUnit">
<fr:form action="/editProtocol.do?method=createExternalPersonAndUnit">
<p class="mbottom0"><strong><bean:message key="label.protocol.insertNewExternalPersonUnit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>:</strong></p>
<fr:view name="protocolFactory" schema="partnerUnit.fullCreation">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight mvert05"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:view>

<fr:edit id="country" name="protocolFactory" schema="partnerUnit.country">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight mvert05 thmiddle"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>
<fr:edit id="protocolFactory" name="protocolFactory" visible="false"/>
<p class="mtop05">
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.insert" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="cancel">
		<bean:message key="button.cancel" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
	</html:cancel>
</p>
</fr:form>
</logic:present>