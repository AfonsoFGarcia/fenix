<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<a href="http://www.ist.utl.pt/en/html/ist-epfl/">IST-EPFL</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<bean:define id="startDate" name="candidacyPeriod" property="start" type="org.joda.time.DateTime" />
<bean:define id="endDate" name="candidacyPeriod" property="end" type="org.joda.time.DateTime" />

<p><em><bean:message key="message.out.of.candidacy.period" arg0="<%= startDate.toString("dd/MM/yyyy") %>" arg1="<%= endDate.toString("dd/MM/yyyy") %>" bundle="PHD_RESOURCES"/></em></p>
