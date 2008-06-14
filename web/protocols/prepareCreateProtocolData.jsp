<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="title.scientificCouncil.portalTitle" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
<h2><bean:message key="title.protocols.create" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>

<fr:form action="/createProtocol.do?method=prepareCreateProtocolResponsibles">

<p class="breadcumbs">
	<span class="actual"><bean:message key="label.protocol.create.step1" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></span> > 
	<span><bean:message key="label.protocol.create.step2" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></span> > 
	<span><bean:message key="label.protocol.create.step3" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></span>
</p>

<p>
	<span class="error0">
		<html:errors bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
		<html:messages id="message" name="errorMessage" message="true" bundle="SCIENTIFIC_COUNCIL_RESOURCES">
			<bean:write name="message" />
		</html:messages>
	</span>
</p>

<fr:edit id="protocolData" name="protocolFactory" schema="create.protocol">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>		
	</fr:layout>
	<fr:destination name="invalid" path="/createProtocol.do?method=invalidProtocolData"/>
</fr:edit>

<p>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.back">
		<bean:message key="button.back" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
	</html:cancel>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.next" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
	</html:submit>
</p>
</fr:form>