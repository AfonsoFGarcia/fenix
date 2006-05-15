<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<p>
	<span class="error">
		<html:errors/>
	</span>
</p>

<logic:messagesPresent property="error.exception.notAuthorized">
	<span class="error">
		<bean:message key="label.notAuthorized.courseInformation" />
	</span>	
</logic:messagesPresent>

<logic:messagesNotPresent property="error.exception.notAuthorized">
	<img alt="Administra��o de disciplina" src="<%= request.getContextPath() %>/images/title_adminDisc.gif" />
	<p><bean:message key="label.instructions" /></p>
</logic:messagesNotPresent>