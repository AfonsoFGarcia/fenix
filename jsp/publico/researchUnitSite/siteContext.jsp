<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<bean:define id="siteActionName" value="/researchSite/viewResearchUnitSite.do" toScope="request"/>
<bean:define id="siteContextParam" value="siteID" toScope="request"/>
<bean:define id="siteContextParamValue" name="site" property="idInternal" toScope="request"/>
<bean:define id="site" name="site" toScope="request"/>
<bean:define id="announcementActionVariable" value="/researchSite/manageResearchUnitAnnouncements.do" toScope="request"/>
<bean:define id="eventActionVariable" value="/researchSite/manageResearchUnitAnnouncements.do" toScope="request"/>
