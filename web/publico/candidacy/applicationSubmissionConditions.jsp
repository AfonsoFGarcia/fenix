<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>
<%@ page import="java.util.Locale"%>
<%@ page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>

<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>
<bean:define id="applicationInformationLinkDefault" name="application.information.link.default"/>
<bean:define id="applicationInformationLinkEnglish" name="application.information.link.english"/>

<div class="breadcumbs">
	<% 
		Locale locale = Language.getLocale();
		if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
		<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://www.ist.utl.pt/pt/candidatos/"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<% } else { %>
		<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://www.ist.utl.pt/en/prospective-students/"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<% } %>
	<% 
		if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
		<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= applicationInformationLinkDefault %>'><bean:write name="application.name"/> </a> &gt;
	<% } else { %>
		<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= applicationInformationLinkEnglish %>'><bean:write name="application.name"/> </a> &gt;
	<% } %>
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>


<h1><bean:write name="application.name" bundle="CANDIDATE_RESOURCES"/></h1>

<bean:message key="message.form.details.submission.note.new" bundle="CANDIDATE_RESOURCES"/>

	<% 
		if(locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
		<p><bean:message key="message.payment.details" bundle="CANDIDATE_RESOURCES"/></p>
		<p><em><bean:message key="message.bank.transfer.preferred.name.observation.fields" bundle="CANDIDATE_RESOURCES"/></em></p>
	<% } %>

<p class="mtop15">
	<bean:message key="message.ist.conditions.note" bundle="CANDIDATE_RESOURCES"/>
	<bean:message key="message.any.question.application.submission" bundle="CANDIDATE_RESOURCES"/>.
</p>


<div class="mtop15" id="contacts"><bean:message key="message.nape.contacts" bundle="CANDIDATE_RESOURCES"/></div>

<bean:define id="hash" name="hash"/> 
<fr:form action='<%= mappingPath + ".do?method=prepareCandidacyCreation&hash=" + hash %>'>
	<p class="mtop2"><html:submit><bean:message key="button.continue" bundle="APPLICATION_RESOURCES"/> �</html:submit></p>
</fr:form>

