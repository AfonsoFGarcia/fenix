<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="<%= request.getContextPath() + "/CSS/dotist.css" %>" rel="stylesheet" type="text/css" />
<link href="<%= request.getContextPath() + "/CSS/dotist_timetables.css" %>" rel="stylesheet" type="text/css" />
<title><tiles:getAsString name="title" ignore="true" /></title>
</head>
<body>
<%-- Layout component parameters : title, context, header, navGeral, navLocal, body, footer --%>
<!-- Context -->
<tiles:insert attribute="context" ignore="true"/>
<!--End Context -->
<!-- Header -->
<div id="header">	
	<img alt="Logo dot ist" src="<%= request.getContextPath() %>/images/dotist-id.gif" />
    <p><tiles:getAsString name="serviceName" /></p>
</div>
<div id="hdr-nav"><a href="mailto:suporte@dotist.utl.pt"><img alt="Icon de Suporte" src="<%= request.getContextPath() %>/images/sup-bar.gif" /></a><a href="logoff.do"><img alt="Icon de Logout" src="<%= request.getContextPath() %>/images/logoff-bar.gif" /></a></div>
<!-- End Header -->
<!-- NavGeral -->
<tiles:insert attribute="navGeral" />

<tiles:insert page="/commons/personalInfoTitleBar.jsp" />
<!-- End NavGeral -->
<!-- Navbar Lateral e Body Content -->
<table width="100%" border="0" cellspacing="0">
   <tr>
     <td id="navlateral_photos" width="25%" valign="top" nowrap="nowrap"><tiles:insert attribute="photos" />
     </td>
     <td id="bodycontent" width="100%" align="left" valign="top">
     	<tiles:insert attribute="body-context" ignore="true"/>     
     	<tiles:insert attribute="body" ignore="true"/>
	</td>
  </tr>
</table>
<!--End Navbar Lateral e Body Content -->
<!-- Footer -->
<div id="footer">
    <p><tiles:insert attribute="footer" /></p>
</div>
<!--End Footer -->
</body>
</html:html>

