<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="department" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
<h2><bean:message key="title.protocols"  bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>


<bean:define id="protocolID"><bean:write name="protocol" property="idInternal"/></bean:define>

<h3 class="separator2 mtop15"><bean:message key="label.protocol.data"  bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>

<fr:view name="protocol" schema="view.protocol">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
	</fr:layout>
</fr:view>


<!-- Responsibles -->
<h3 class="separator2 mtop15"><bean:message key="label.protocol.responsibles" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>

<p class="mbottom05"><strong><bean:message key="label.protocol.ist" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
<logic:notEmpty name="protocol" property="responsibles">
	<fr:view name="protocol" property="responsibles" schema="show.protocol.responsible">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight mtop05"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="protocol" property="responsibles">
	<p><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>.</em></p>
</logic:empty>

<p class="mbottom05"><strong><bean:message key="label.protocol.partner" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
<logic:notEmpty name="protocol" property="partnerResponsibles">
	<fr:view name="protocol" property="partnerResponsibles" schema="show.protocol.responsible">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight mtop05"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="protocol" property="partnerResponsibles">
	<p><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>.</em></p>
</logic:empty>


<!-- Units -->
<h3 class="separator2 mtop15"><bean:message key="label.protocol.units" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>

<p class="mbottom05"><strong><bean:message key="label.protocol.ist" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
<fr:view name="protocol" property="units" schema="show.protocol.unit">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight mtop05"/>
	</fr:layout>
</fr:view>

<logic:empty name="protocol" property="units">
	<p><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>.</em></p>
</logic:empty>

<p class="mbottom05"><strong><bean:message key="label.protocol.partner" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
<logic:notEmpty name="protocol" property="partners">
<fr:view name="protocol" property="partners" schema="show.protocol.partnerUnit">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight mtop05"/>
	</fr:layout>
</fr:view>
</logic:notEmpty>

<logic:empty name="protocol" property="partners">
	<p><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>.</em></p>
</logic:empty>


<!-- Files -->
<h3 class="separator2 mtop15"><bean:message key="label.protocol.files" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>

<logic:notEmpty name="protocol" property="protocolFiles">
<fr:view name="protocol" property="protocolFiles" schema="show.file">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight"/>
	</fr:layout>
</fr:view>
</logic:notEmpty>

<logic:empty name="protocol" property="protocolFiles">
	<p>
		<em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>.</em>
	</p>
</logic:empty>