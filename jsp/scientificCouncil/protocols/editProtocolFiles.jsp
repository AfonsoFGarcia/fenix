<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>
<h2><bean:message key="title.protocols.edit"/></h2>


<h3 class="mtop15"><bean:message key="label.protocol.files"/></h3>

<fr:form action="/protocols.do">
<html:hidden bundle="HTMLALT_RESOURCES" name="protocolsForm" property="method" value="deleteProtocolFile"/>
<html:hidden bundle="HTMLALT_RESOURCES" name="protocolsForm" property="fileID"/>
<fr:edit id="protocolFactory" name="protocolFactory" visible="false"/>

<logic:notEmpty name="protocolFactory" property="protocol.protocolFiles">
<table class="tstyle1">
	<tr>
		<th><bean:message key="label.name"/></th>
		<th></th>				
	</tr>
	<logic:iterate id="file" name="protocolFactory" property="protocol.protocolFiles" type="net.sourceforge.fenixedu.domain.protocols.ProtocolFile">
	<tr>
		<td><html:link href="<%= file.getDownloadUrl() %>" target="_blank"><bean:write name="file" property="filename"/></html:link></td>
		<td>
			<html:submit onclick="<%= "this.form.fileID.value=" + file.getIdInternal().toString()%>">
				<bean:message key="button.delete" />
			</html:submit>
		</td>				
	</tr>
	</logic:iterate>
</table>
</logic:notEmpty>

<logic:empty name="protocolFactory" property="protocol.protocolFiles">
	<p><em><bean:message key="label.protocol.hasNone"/></em></p>
</logic:empty>
<br/>
</fr:form>

<fr:form action="/protocols.do?method=addProtocolFile" encoding="multipart/form-data">
	<fr:edit name="protocolFactory" schema="edit.protocolFile">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight"/>
		</fr:layout>
	</fr:edit>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.insert" />
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="back">
		<bean:message key="button.back" />
	</html:cancel>

</fr:form>