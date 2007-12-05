<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h1><bean:message key="link.Publications" bundle="RESEARCHER_RESOURCES"/></h1>


<bean:define id="showContextPath" value="/" toScope="request" />
<bean:define id="searchContextPath" value="/" toScope="request" />
<bean:define id="showAction" value="showPublications.do?" toScope="request" />
<bean:define id="searchAction" value="searchPublications.do?" toScope="request" />
<bean:define id="showArguments" value="<%= "sectionID=" + request.getParameter("sectionID") + "&homepageID=" + request.getParameter("homepageID") %>" toScope="request" />
<bean:define id="searchArguments" value="<%= "sectionID=" + request.getParameter("sectionID") + "&homepageID=" + request.getParameter("homepageID") %>" toScope="request" />
<bean:define id="showMethod" value="&method=showPublications" toScope="request" />
<bean:define id="searchMethod" value="&method=prepareSearchPublication" toScope="request" />

<bean:define id="searchPublicationLabelKey" value="label.search.publications.person" toScope="request" />

<jsp:include page="../../../commons/sites/unitSite/searchPublications.jsp" />

