<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="<%= request.getContextPath() %>/CSS/dotist.css" rel="stylesheet" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" rel="stylesheet" type="text/css" />
</head>
<body>
<%-- Layout component parameters : title, header, navGeral, navLocal, body, footer --%>
<!-- Header -->
<div id="header">	
	<img alt="Logo dot ist" src="<%= request.getContextPath() %>/images/dotist-id.gif" />
    <p><tiles:getAsString name="serviceName" /></p>
</div>
<div id="hdr-nav">
	<bean:define id="supportMail" type="java.lang.String">mailto:<bean:message key="suporte.mail" bundle="GLOBAL_RESOURCES"/></bean:define>
	<a href="<%= supportMail %>">
		<img alt="Icon de Suporte" src="<%= request.getContextPath() %>/images/sup-bar.gif" />
	</a>
	<a href="logoff.do">
		<img alt="Icon de Logout" src="<%= request.getContextPath() %>/images/logoff-bar.gif" />
	</a>
</div>
<!-- End Header -->
<!-- Navbar Lateral e Body Content -->
<table width="100%" border="0" cellspacing="0">
   <tr>
    <td id="bodycontent" width="100%" align="left" valign="top"><tiles:insert attribute="body" />
	</td>
  </tr>
</table>
<!--End Navbar Lateral e Body Content -->
<!-- Footer -->
<div id="footer">
    <tiles:insert attribute="footer" />
</div>
<!--End Footer -->
</body>
</html:html>

