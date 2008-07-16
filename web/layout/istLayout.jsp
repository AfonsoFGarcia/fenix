<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext"%>
<html:html xhtml="true" locale="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta name="keywords" content="ensino,  ensino superior, universidade, instituto, ci�ncia, instituto superior t�cnico, investiga��o e desenvolvimento" />
<meta name="description" content="O Instituto Superior T�cnico � a maior escola de engenharia, ci�ncia e tecnologia em Portugal." />

<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/iststyle.css"/>
<link rel="stylesheet" type="text/css" media="print" href="<%= request.getContextPath() %>/CSS/iststyle_print.css"/>
<tiles:insert attribute="css-headers" ignore="true" />

<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/expmenu.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/hideButtons.js"></script>

<title>
<logic:present name="<%= FilterFunctionalityContext.CONTEXT_KEY %>">
<bean:define id="contentContext" name="<%= FilterFunctionalityContext.CONTEXT_KEY %>" property="selectedContainer" type="net.sourceforge.fenixedu.domain.contents.Content"/> 
	<logic:equal name="contentContext" property="unitSite" value="true">
		<bean:write name="contentContext" property="unit.partyName"/> -
	</logic:equal>
</logic:present>

<tiles:getAsString name="title" ignore="true" />

</title> <%-- TITLE --%>

</head>

<body>
<jsp:include page="deployWarning.jsp" flush="true"/>
<tiles:insert attribute="page-context" ignore="true"/>

<!-- BEGIN BROWSER UPGRADE MESSAGE -->
<div class="browser_upgrade">
  <p><strong>Aviso:</strong>
  Se est&aacute; a ler esta mensagem, provavelmente, o browser que utiliza n&atilde;o &eacute; compat&iacute;vel
  com os &quot;standards&quot; recomendados pela <a href="http://www.w3.org">W3C</a>.
  Sugerimos vivamente que actualize o seu browser para ter uma melhor experi&ecirc;ncia
  de utiliza&ccedil;&atilde;o deste &quot;website&quot;.
  Mais informa&ccedil;&otilde;es em <a href="http://www.webstandards.org/upgrade/">webstandards.org</a>.</p>
  <p><strong>Warning:</strong> If you are reading this message, probably, your
    browser is not compliant with the standards recommended by the <a href="http://www.w3.org">W3C</a>. We suggest
    that you upgrade your browser to enjoy a better user experience of this website.
    More informations on <a href="http://www.webstandards.org/upgrade/">webstandards.org</a>.</p>
</div>
<!-- END BROWSER UPGRADE MESSAGE -->

<!-- SYMBOLSROW -->
<div id="header">
	<tiles:insert attribute="symbols_row" ignore="true"/>
</div>

<!-- PROFILE NAVIGATION -->
<div id="perfnav">
	<tiles:insert attribute="profile_navigation" ignore="true"/>
</div>

<div id="holder">
<table id="bigtable" width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>

<td id="latnav_container" width="155">
<!--MAIN NAVIGATION -->   
<div id="latnav">
	<tiles:insert attribute="main_navigation" ignore="true"/>
</div>
</td>

<!-- DEGREE SITE -->
<td width="100%" colspan="3" id="main">

	 <div id="version">
				<html:form action="/changeLocaleTo.do">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.windowLocation" property="windowLocation" value=""/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.newLanguage" property="newLanguage" value=""/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.newCountry" property="newCountry" value=""/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.newVariant" property="newVariant" value=""/>
			 
	            <logic:notEqual name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="pt">
					<input type="image" src="<%= request.getContextPath() %>/images/flags/pt.gif" alt="<bean:message key="pt" bundle="IMAGE_RESOURCES" />" title="Portugu�s" value="PT"
					 onclick="this.form.newLanguage.value='pt';this.form.newCountry.value='PT';this.form.newVariant.value='<%= net.sourceforge.fenixedu._development.PropertiesManager.getProperty("variant") %>';this.form.windowLocation.value=window.location;this.form.submit();" />
					<input class="activeflag" type="image" src="<%= request.getContextPath() %>/images/flags/en.gif" alt="English" title="English" value="EN" 
					onclick="this.form.newLanguage.value='en';this.form.newCountry.value='EN';this.form.newVariant.value='<%= net.sourceforge.fenixedu._development.PropertiesManager.getProperty("variant") %>';this.form.windowLocation.value=window.location;this.form.submit();"/>
				</logic:notEqual>
					
	            <logic:notEqual name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="en">			
					<input class="activeflag" type="image" src="<%= request.getContextPath() %>/images/flags/pt.gif" alt="Portugu�s" title="Portugu�s" value="PT"
					 onclick="this.form.newLanguage.value='pt';this.form.newCountry.value='PT';this.form.newVariant.value='<%= net.sourceforge.fenixedu._development.PropertiesManager.getProperty("variant") %>';this.form.windowLocation.value=window.location;this.form.submit();" />
					<input type="image" src="<%= request.getContextPath() %>/images/flags/en.gif" alt="English" title="English" value="EN" 
					onclick="this.form.newLanguage.value='en';this.form.newCountry.value='EN';this.form.newVariant.value='<%= net.sourceforge.fenixedu._development.PropertiesManager.getProperty("variant") %>';this.form.windowLocation.value=window.location;this.form.submit();"/>
				</logic:notEqual>
		
			</html:form>
	</div> 

<tiles:insert attribute="body-context" ignore="true"/>
<tiles:insert attribute="body" ignore="true"/>
<tiles:getAsString name="body-inline" ignore="true"/>

</td>

</tr>
</table>
</div>
<!-- FOOTER --> 
<div id="footer">
<tiles:insert attribute="footer" ignore="true"/>
</div>

<script type="text/javascript">
	hideButtons()
</script>

<tiles:insert attribute="analytics" ignore="true"/>

</body>
</html:html>
