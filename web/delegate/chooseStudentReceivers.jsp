<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.sendMailToStudents" bundle="DELEGATES_RESOURCES" /></h2>

<bean:define id="year" name="currentExecutionYear" property="executionYear.externalId"/>
<fr:form action="/sendEmailToDelegateStudents.do?method=chooseExecutionYear">
	<fr:edit schema="choose.execution.year" name="currentExecutionYear" id="chooseExecutionYear" layout="tabular">
		<fr:destination name="postBackChooseExecutionYear" path="/sendEmailToDelegateStudents.do?method=chooseExecutionYear"/>
	</fr:edit>
</fr:form>

<logic:messagesPresent property="error" message="true">
	<html:messages id="message" property="error" message="true" bundle="MESSAGING_RESOURCES">
		<p><span class="error0"><bean:write name="message"/></span></p>
	</html:messages>
</logic:messagesPresent>

<logic:messagesPresent property="problem" message="true">
    <p>
        <span class="error0">
            <bean:message key="messaging.mail.address.problem.text" bundle="MESSAGING_RESOURCES"/>:
            <html:messages id="message" property="problem" message="true" bundle="MESSAGING_RESOURCES">
                <bean:write name="message"/>,
            </html:messages>
        </span>
    </p>
</logic:messagesPresent>

<logic:messagesPresent property="warning" message="true">
    <html:messages id="message" property="warning" message="true" bundle="MESSAGING_RESOURCES">
        <p><span class="warning0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<logic:messagesPresent property="confirmation" message="true">
    <html:messages id="message" property="confirmation" message="true" bundle="MESSAGING_RESOURCES">
        <p><span class="success0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<p class="mbottom05">
	<b><bean:message key="label.sendMailToStudents.chooseReceivers" bundle="DELEGATES_RESOURCES" /></b></p>
<p class="color888 mvert05">
	<bean:message key="label.sendMailToStudents.chooseReceivers.help" bundle="DELEGATES_RESOURCES" /></p>
<p class="mtop05 mbottom05">
	<b><bean:message key="label.delegates.sendMailTo" bundle="DELEGATES_RESOURCES" /></b>
	<html:link page='<%= "/sendEmailToDelegateStudents.do?method=prepare&amp;year=" + year%>'>
		<bean:message key="link.sendToDelegateStudents" bundle="DELEGATES_RESOURCES"/>
	</html:link>,
	<html:link page='<%= "/sendEmailToDelegateStudents.do?method=prepareSendToStudentsFromSelectedCurricularCourses&amp;year=" + year %>'>
		<bean:message key="link.sendToStudentsFromCurricularCourses" bundle="DELEGATES_RESOURCES"/>
	</html:link>
</p>
<logic:present name="recipients">
	<fr:edit id="recipientsBean" name="recipients"
		schema="delegates.show.recipients"
		action="/sendEmailToDelegateStudents.do?method=prepareSendEmail&contentContextPath_PATH=/comunicacao/comunicacao" />
</logic:present>


