<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%>

<html:html xhtml="true">
<head>

<title><tiles:getAsString name="title" ignore="true" /></title> <%-- TITLE --%>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/iststyle.css" />
<link rel="stylesheet" type="text/css" media="print" href="<%= request.getContextPath() %>/CSS/iststyle_print.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/webservice.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/calendar.css" />

</head>
<body>

<style>
h1 a:link, h1 a:visited, h1 a:hover {
background: url(<%= request.getContextPath() %>/images/logo_iaeste.gif) no-repeat top left;
width: 96px;
height: 99px;
display: block;
text-decoration: none;
border: none;
overflow: hidden;
}
</style>

<jsp:include page="deployWarning.jsp" flush="true"/>
<p class="skipnav"><a href="#main">Saltar men&uacute; de navega&ccedil;&atilde;o</a></p>
<!-- START HEADER -->
    <div id="logoist">
        <h1><%= ContentInjectionRewriter.HAS_CONTEXT_PREFIX %><a href="#">IAESTE</a></h1>
        <!-- <img alt="[Logo] Instituto Superior T�cnico" height="51" src="http://www.ist.utl.pt/img/wwwist.gif" width="234" /> -->
    </div>
<!-- END HEADER -->
<!--START MAIN CONTENT -->

<div id="container_800px">
<div id="wrapper">

<tiles:insert attribute="body" ignore="true"/>

</div> <!-- #wrapper -->
</div> <!-- #container -->

</body>
</html:html>