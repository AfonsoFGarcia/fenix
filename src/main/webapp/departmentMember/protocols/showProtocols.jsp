<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="department" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
<h2><bean:message key="title.protocols" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>

<logic:notEmpty name="protocolHistories">
	<logic:iterate id="protocolHistory" name="protocolHistories" type="net.sourceforge.fenixedu.domain.protocols.ProtocolHistory">
		<span class="warning0"><bean:message key="message.protocol.endDateAlert" arg0="<%= protocolHistory.getProtocol().getProtocolNumber()%>" arg1="<%= protocolHistory.getEndDate().toString()%>" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
		</span>
	</logic:iterate>
</logic:notEmpty>

<logic:notEmpty name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.protocols">
	<fr:view name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.protocols" schema="show.protocol.toList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight"/>
			<fr:property name="link(view)" value="/protocols.do?method=viewProtocol" />
			<fr:property name="key(view)" value="link.view" />
			<fr:property name="param(view)" value="idInternal" />
			<fr:property name="bundle(view)" value="SCIENTIFIC_COUNCIL_RESOURCES" />
		</fr:layout>
	</fr:view> 
</logic:notEmpty>