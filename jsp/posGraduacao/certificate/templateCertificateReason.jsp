<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<logic:present name="<%= SessionConstants.DOCUMENT_REASON_LIST %>">	
	<p>Este certificado destina-se a fins comprovativos de:</p>
	<p><b><logic:iterate id="item" name="<%= SessionConstants.DOCUMENT_REASON_LIST %>" >
			<bean:write name="item" /><br />
		</logic:iterate></b></p>
</logic:present>
<logic:notPresent name="<%= SessionConstants.DOCUMENT_REASON_LIST %>">
	<p>Este certificado destina-se a fins comprovativos.</p>
</logic:notPresent>