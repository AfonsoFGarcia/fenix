<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="administrative.office.services"/></em>
<h2><bean:message key="documents.requirement"/></h2>
<hr/><br/>

<logic:messagesPresent message="true">
	<span class="error">
		<html:messages id="message" message="true" bundle="STUDENT_RESOURCES">
			<bean:write name="message"/>
		</html:messages>
	</span>
</logic:messagesPresent>

<logic:notEmpty name="warningsToReport">
	<p class="warning0"><bean:message key="document.request.warnings.title"/></p>
	<ul>
		<logic:iterate id="warningToReport" name="warningsToReport">
			<li><bean:message name="warningToReport"/></li>
		</logic:iterate>
	</ul>
</logic:notEmpty>

<html:form action="/documentRequest.do?method=create">
	<html:hidden property="scpId" />
	<html:hidden property="schoolRegistrationExecutionYearId" />
	<html:hidden property="enrolmentDetailed"  />	
	<html:hidden property="enrolmentExecutionYearId" />
	<html:hidden property="degreeFinalizationAverage" />
	<html:hidden property="degreeFinalizationDetailed"/>
	<html:hidden property="chosenDocumentPurposeType" />
	<html:hidden property="otherPurpose" />
	<html:hidden property="notes" />
	<html:hidden property="urgentRequest" />

	<logic:iterate id="chosenDocumentRequestType" name="chosenDocumentRequestTypes">
		<html:hidden property="chosenDocumentRequestTypes" value="<%=chosenDocumentRequestType.toString()%>"/>
	</logic:iterate>

	<fr:edit nested="true" schema="DocumentRequestCreateBean.viewToConfirmCreation" name="documentRequestCreateBeans" id="documentRequestCreateBeans" action="/documentRequest.do?method=create">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4 thlight thright" />
		</fr:layout>
		<fr:destination name="cancel" path="/documentRequest.do?method=prepare"/>
	</fr:edit>

	<html:submit styleClass="inputbutton"><bean:message key="submit"/></html:submit>
	<html:cancel styleClass="inputbutton"><bean:message key="return"/></html:cancel>
</html:form>
