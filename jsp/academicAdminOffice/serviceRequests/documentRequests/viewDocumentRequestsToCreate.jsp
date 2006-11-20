<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="documentRequests" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>
<hr/>
<br/>
<strong><bean:message key="label.documentRequests.confirmation" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong>

<logic:messagesPresent message="true">
	<span class="error"><!-- Error messages go here -->
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

<fr:form action="/documentRequestsManagement.do?method=create">
<p style="margin-top: 4em;">
	<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="documentRequestCreateBean" paramProperty="registration.idInternal">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</p>
<br/>

	<fr:edit schema="DocumentRequestCreateBean.chooseDocumentRequestType" name="documentRequestCreateBean" visible="false"
		type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4"/>
	      	<fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>
<logic:present name="additionalInformationSchemaName">
	<bean:define id="additionalInformationSchemaName" name="additionalInformationSchemaName" type="java.lang.String"/>
	<fr:edit name="documentRequestCreateBean" schema="<%= additionalInformationSchemaName %>" visible="false"
		type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean" />
</logic:present>
	<fr:edit name="documentRequestCreateBean" schema="DocumentRequestCreateBean.purposes"  visible="false"
		type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean" />
	
	<fr:edit name="documentRequestCreateBean" schema="DocumentRequestCreateBean.notes"  visible="false"
		type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean" />


	<fr:view schema="DocumentRequestCreateBean.chooseDocumentRequestType" name="documentRequestCreateBean" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>	
	</fr:view>
<logic:present name="additionalInformationSchemaName">
	<bean:define id="additionalInformationSchemaName" name="additionalInformationSchemaName" type="java.lang.String"/>	
	<fr:view name="documentRequestCreateBean" schema="<%= additionalInformationSchemaName %>" 
		type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>	
	</fr:view>		
</logic:present>

	<fr:view name="documentRequestCreateBean" schema="DocumentRequestCreateBean.purposes" 
		type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>	
	</fr:view>
	
	<fr:view name="documentRequestCreateBean" schema="DocumentRequestCreateBean.notes" 
		type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>	
	</fr:view>	

	<html:submit styleClass="inputbutton"><bean:message key="button.confirm" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	
</fr:form>
