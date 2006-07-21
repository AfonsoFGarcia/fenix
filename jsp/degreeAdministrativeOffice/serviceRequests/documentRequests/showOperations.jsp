<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="label.documentRequestsManagement.operations" /></h2>

<hr><br/>

<table>
	<tr>
		<td>
			<html:link action="/documentRequestsManagement.do?method=viewNewRequests">
					<bean:message key="label.documentRequestsManagement.newDocumentRequests" />
			</html:link>
		</td>
	</tr>
	<tr>
		<td>
			<html:link action="/documentRequestsManagement.do?method=prepareSearch">
					<bean:message key="label.documentRequestsManagement.searchDocumentRequests" />
			</html:link>
		</td>
	</tr>
</table>
