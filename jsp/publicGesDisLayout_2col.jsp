<?xml version="1.0" encoding="iso-8859-1"?> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<logic:present name="<%=SessionConstants.INFO_SITE %>" property="style">
<bean:define id="style" name="<%=SessionConstants.INFO_SITE %>" property="style"/>	
<link href="<%= request.getContextPath()+ "/CSS/" + pageContext.findAttribute("style") %>" rel="stylesheet" type="text/css" />	
</logic:present>
<logic:notPresent name="<%=SessionConstants.INFO_SITE %>" property="style">
<link href="<%= request.getContextPath() %>/CSS/gesdis-web.css" rel="stylesheet" type="text/css" />	
</logic:notPresent>		


<link href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%= request.getContextPath() %>/script/gesdis-scripting.js"></script>
</head>
<body>
<%-- Layout component parameters : header, navLocal, body --%>
<!-- Navbar Lateral e Body Content -->
<table width="100%"  height="100%" border="0" cellspacing="0" cellpadding="0">
   <tr>
     <td  align="left" valign="top" bgcolor="#B5BED6"  class="barraist">
     <img alt="" border="0"  src="<%= request.getContextPath() %>/images/LogoIST.gif"  />
	</td>
    <td id="principal" bgcolor="#FFFFFF">
      <div id="header"><h4><tiles:getAsString name="institutionName" ignore="true"/></h4></div>
      <h1><tiles:getAsString name="executionCourseName"  ignore="true"/></h1>
      <br />
	  <tiles:insert attribute="body" ignore="true"/>      
    </td>	
    <td id="barranav" bgcolor="#EBEFFA" valign="top">
      <div class="blue-bckgr">
        <h3>Navega&ccedil;&atilde;o Geral</h3>
      </div>
      <div id="nav">
      <tiles:insert attribute="navbarGeral" ignore="true"/>	
    
      <tiles:insert attribute="navbar" ignore="true"/>	
      </div>
    </td>
  </tr>
</table>
<!--End Navbar Lateral e Body Content -->
<!-- Footer -->
<%-- <table width="100%%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="footer"><tiles:insert attribute="footer" ignore="true"/>
    </td>
  </tr>
</table> --%>
<!--End Footer -->
</body>
</html:html>

