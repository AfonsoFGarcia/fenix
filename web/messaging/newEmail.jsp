<%@page import="net.sourceforge.fenixedu.domain.person.RoleType"%>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<%@page import="net.sourceforge.fenixedu.domain.User"%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><bean:message bundle="MANAGER_RESOURCES" key="title.send.mail"/></h2>

<logic:present name="errorMessage">
	<p>
		<span class="error0"><bean:write name="errorMessage"/></span>
	</p>
</logic:present>
	
<form action="<%= request.getContextPath() + "/messaging/emails.do" %>" method="post">
	<html:hidden property="method" value="sendEmail"/>

	<fr:edit id="emailBean" name="emailBean">
		<fr:schema type="net.sourceforge.fenixedu.domain.util.email.EmailBean" bundle="MESSAGING_RESOURCES">
			<fr:slot name="sender" bundle="MESSAGING_RESOURCES" key="label.fromName" layout="menu-select-postback"
					validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
		        <fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.EmailSenderProvider" />
				<fr:property name="format" value="${fromName} (${fromAddress})" />
				<fr:property name="destination" value="selectSender"/>
			</fr:slot>
    		<fr:slot name="replyTos" layout="option-select" key="label.replyTos">
        		<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.EmailReplyTosProvider" />
        		<fr:property name="eachSchema" value="net.sourceforge.fenixedu.domain.util.email.ReplyTo.selectItem"/>
        		<fr:property name="eachLayout" value="values"/>
        		<fr:property name="classes" value="nobullet noindent"/>
        		<fr:property name="sortBy" value="replyToAddress"/>
    		</fr:slot>
    		<fr:slot name="recipients" layout="option-select" key="label.receiversGroup">
        		<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.EmailRecipientsProvider" />
        		<fr:property name="eachSchema" value="net.sourceforge.fenixedu.domain.util.email.Recipient.selectItem"/>
        		<fr:property name="eachLayout" value="values"/>
        		<fr:property name="classes" value="nobullet noindent"/>
        		<fr:property name="sortBy" value="toName"/>
    		</fr:slot>
			<fr:slot name="bccs" bundle="MESSAGING_RESOURCES" key="label.receiversOfCopy">
				<fr:property name="size" value="50" />
			</fr:slot>
			<fr:slot name="subject" bundle="MANAGER_RESOURCES" key="label.email.subject">
				<fr:property name="size" value="50" />
			</fr:slot>
			<fr:slot name="message" bundle="MANAGER_RESOURCES" key="label.email.message" layout="longText">
				<fr:property name="columns" value="80"/>
				<fr:property name="rows" value="10"/>
			</fr:slot>
			<%
				if (AccessControl.getUserView().hasRoleType(RoleType.MANAGER)
						|| AccessControl.getUserView().hasRoleType(RoleType.HTML_CAPABLE_SENDER)) {
			%>
					<fr:slot name="htmlMessage" bundle="MESSAGING_RESOURCES" key="label.email.message.html.content" layout="longText">
						<fr:property name="columns" value="80"/>
						<fr:property name="rows" value="10"/>
					</fr:slot>
			<%
				}
			%>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight mtop05 ulnomargin"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>

		<fr:destination name="selectSender" path="/emails.do?method=newEmail"/>
		<fr:destination name="cancel" path="/index.do"/>
	</fr:edit>

</form>