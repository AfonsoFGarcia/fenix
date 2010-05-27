<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>
<%@page import="net.sourceforge.fenixedu.util.stork.SPUtil" %>
<%@page import="java.net.URL" %>
<%@page import="com.lowagie.text.html.HtmlEncoder" %>


<%!

	static String f(String value, Object ... args) {
    	return String.format(value, args);
	}
%>


<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en">NMCI</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en/ist/">Study at IST</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= f("%s/candidacies/erasmus", request.getContextPath()) %>'><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="erasmus.title.application.submission" bundle="CANDIDATE_RESOURCES" />
</div>

<h1><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES"/></h1>

<%
	// String applicationUrl = SPUtil.getInstance().getSpInvokeUrl();
	
	String applicationUrl = java.net.URLEncoder.encode("http://fenix.ist.utl.pt/applications/erasmus/returnFromPeps");
	String attributes = java.net.URLEncoder.encode(SPUtil.getInstance().getAttributesList());
	applicationUrl = f("http://storker.ist.utl.pt/IST-SP/IndexPage?applicationUrl=%s&attributesList=%s", applicationUrl, attributes);
%>

<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= applicationUrl %>">Click here in order to authenticate with your national citizen card</a>

</body>
</html>
