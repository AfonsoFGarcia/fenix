<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html:html xhtml="true">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="shortcut icon" href="<%= request.getContextPath() %>/images/favicon.ico" type="image/ico"/>
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/layout.css"  media="screen"  />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/general.css" media="screen" />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/color.css" media="screen" />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/print.css" media="print" />

	<link href="<%= request.getContextPath() %>/CSS/posgrad_tablelayout.css" rel="stylesheet" media="screen" type="text/css" />
	<link href="<%= request.getContextPath() %>/CSS/posgrad_print.css" rel="stylesheet" media="print" type="text/css" />
	<title><tiles:getAsString name="title" ignore="true" /></title>
	<tiles:insert attribute="head" ignore="true"/>
	<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/hideButtons.js"></script>
</head>

<body>
<jsp:include page="deployWarning.jsp" flush="true"/>
<%-- Layout component parameters : title, header, navGeral, navLocal, body, footer --%>


<!-- Header -->
<div id="top">
	<h1 id="logo">
		<img alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />" src="<bean:message key="dot.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>"/>
	</h1>

	<%--
	<tiles:getAsString name="serviceName" />
	--%>

	<tiles:insert page="/commons/headerButtons.jsp" />
	<p id="user">
		<tiles:insert page="/commons/personalInfoTitleBar.jsp" />
	</p>
</div>
<!-- End Header -->


<!-- NavGeral -->
<div id="navtop">
	<tiles:insert attribute="navGeral" />
</div>
<!-- End NavGeral -->


<!-- Container -->
<div id="container">

	<!-- NavLateral -->
	<div id="navlateral">
		<tiles:insert attribute="navLocal" ignore="true"/>
	</div>
	<!-- End NavLateral -->
	


	<!-- Content -->
	<div id="content">
	
	  	<tiles:insert attribute="body-context" ignore="true"/>
	  	<tiles:insert attribute="body" ignore="true"/>
	  	
	</div>
	<!-- End Content -->

	<!-- Footer -->
	<div id="footer">
		<tiles:insert attribute="footer" />
	</div>
	<!--End Footer -->

</div>
<!-- End Container -->

<script type="text/javascript">
	hideButtons();
</script>

</body>
</html:html>