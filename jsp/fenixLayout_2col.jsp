<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="<%= request.getContextPath() %>/CSS/dotist.css" rel="stylesheet" media="screen" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" rel="stylesheet" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_print.css" rel="stylesheet" media="print" type="text/css" />
<title><tiles:getAsString name="title" ignore="true" /></title>
</head>
<body>
<%-- Layout component parameters : title, context, header, navGeral, navLocal, body, footer --%>
<!-- Context -->
<tiles:insert attribute="context" ignore="true"/>
<!--End Context -->
<!-- Header -->
<table id="header" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="header-td" width="25%"><img alt="" height="60" src="<%= request.getContextPath() %>/images/dotist_sop.gif" width="192" />
	</td>
    <td class="header-td"><div align="right"><h1><tiles:getAsString name="serviceName" /></h1></div>
    </td>
  </tr>
</table>
<!-- End Header -->
<!-- NavGeral -->
<table id="navbargeral" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="navbargeral-td"><tiles:insert attribute="navGeral" ignore="true"/>
    </td>
  </tr>
</table>
<!-- End NavGeral -->
<!-- Navbar Lateral e Body Content -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
   <tr>
     <td class="navlateral-td" align="left" valign="top" nowrap="nowrap">
     	<tiles:insert attribute="navLocal" ignore="true"/>
    </td>
     <td class="bodycontent" width="100%" align="left" valign="top" >
     	<tiles:insert attribute="body-context" ignore="true"/>
     	<tiles:insert attribute="body" />
	</td>
  </tr>
</table>
<!--End Navbar Lateral e Body Content -->
<!-- Footer -->
<table id="footer" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="footer-td"><tiles:insert attribute="footer" />
    </td>
  </tr>
</table>
<!--End Footer -->
</body>
</html:html>