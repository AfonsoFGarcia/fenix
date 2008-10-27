<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType" %> 

<html:xhtml/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	<h2><bean:message key="documentRequests" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>
	
	<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
		<p>
			<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
		</p>
	</html:messages>
	
	<ul class="mtop2">
		<li>
			<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="documentRequestCreateBean" paramProperty="registration.idInternal">
				<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</html:link>
		</li>
	</ul>
		
	<div style="float: right;">
		<bean:define id="personID" name="documentRequestCreateBean" property="registration.student.person.idInternal"/>
		<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
	</div>
	
	<p class="mvert2">
		<span class="showpersonid">
			<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
			<fr:view name="documentRequestCreateBean" property="registration.student" schema="student.show.personAndStudentInformation.short">
				<fr:layout name="flow">
					<fr:property name="labelExcluded" value="true"/>
				</fr:layout>
			</fr:view>
		</span>
	</p>
	
	
	<logic:present name="documentRequestCreateBean" property="registration.ingression">
	<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<fr:view name="documentRequestCreateBean" property="registration" schema="student.registrationDetail.short" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
			<fr:property name="rowClasses" value=",tdhl1,,,,,"/>
		</fr:layout>
	</fr:view>
	</logic:present>
	<logic:notPresent name="documentRequestCreateBean" property="registration.ingression">
	<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<fr:view name="documentRequestCreateBean" property="registration" schema="student.registrationsWithStartData" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
			<fr:property name="rowClasses" value=",tdhl1,,,,,"/>
		</fr:layout>
	</fr:view>
	</logic:notPresent>
	
	
	<fr:form action="/documentRequestsManagement.do?method=viewDocumentRequestToCreate">
	
	<p class="mbottom025"><strong><bean:message key="message.document.to.request" bundle="ACADEMIC_OFFICE_RESOURCES"/>:</strong></p>
	
		<!-- Choose Document Request Type -->
		<bean:define id="schema" name="documentRequestCreateBean" property="schema" type="java.lang.String"/>
		<fr:edit name="documentRequestCreateBean" schema="<%=schema%>" type="net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thright thlight mtop025 mbottom0 thmiddle"/>
				<fr:property name="columnClasses" value="width14em,width40em,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="documentRequestTypeChoosedPostBack" path="/documentRequestsManagement.do?method=documentRequestTypeChoosedPostBack"/>
	
		</fr:edit>
		
		<!-- Insert additional Information (if any) -->
		<logic:present name="additionalInformationSchemaName">
			<bean:define id="additionalInformationSchemaName" name="additionalInformationSchemaName" type="java.lang.String"/>
			<fr:edit name="documentRequestCreateBean" schema="<%= additionalInformationSchemaName %>" type="net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thright thlight mvert0 thmiddle"/>
					<fr:property name="columnClasses" value="width14em,width40em,tdclear tderror1"/>
				</fr:layout>	
				<fr:destination name="executionYearChangedPostBack" path="/documentRequestsManagement.do?method=executionYearToCreateDocumentChangedPostBack"/>
				<fr:destination name="executionPeriodChangedPostBack" path="/documentRequestsManagement.do?method=executionPeriodToCreateDocumentChangedPostBack"/>
				<fr:destination name="useAllPostBack" path="/documentRequestsManagement.do?method=useAllPostBack"/>
			</fr:edit>
		</logic:present>
		
		<logic:notEmpty name="documentRequestCreateBean" property="chosenDocumentRequestType">
			
			<!-- Requested Cycle -->
			<logic:equal name="documentRequestCreateBean" property="hasCycleTypeDependency" value="true">
				<fr:edit name="documentRequestCreateBean" schema="DocumentRequestCreateBean.requestedCycle" type="net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thright thlight mvert0 thmiddle"/>
						<fr:property name="columnClasses" value="width14em,width40em,tdclear tderror1"/>
					</fr:layout>	
				</fr:edit>
			</logic:equal>
	
			<!-- Mobility Program -->
			<logic:equal name="documentRequestCreateBean" property="hasMobilityProgramDependency" value="true">
				<fr:edit name="documentRequestCreateBean" schema="DocumentRequestCreateBean.mobilityProgram" type="net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thright thlight mvert0 thmiddle"/>
						<fr:property name="columnClasses" value="width14em,width40em,tdclear tderror1"/>
					</fr:layout>	
				</fr:edit>
			</logic:equal>
	
			<!-- Purposes -->
			<logic:notEqual name="documentRequestCreateBean" property="chosenDocumentRequestType" value="<%=DocumentRequestType.DIPLOMA_REQUEST.name()%>">
				<fr:edit name="documentRequestCreateBean" schema="DocumentRequestCreateBean.purposes" type="net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thright thlight mvert0 thmiddle"/>
						<fr:property name="columnClasses" value="width14em,width40em,tdclear tderror1"/>
					</fr:layout>	
				</fr:edit>
			</logic:notEqual>
	
			<!-- Can be free processed? -->
			<logic:equal name="documentRequestCreateBean" property="chosenDocumentRequestType.canBeFreeProcessed" value="true">
				<fr:edit name="documentRequestCreateBean" schema="DocumentRequestCreateBean.freeProcessed" type="net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thright thlight mvert0 thmiddle"/>
						<fr:property name="columnClasses" value="width14em,width40em,tdclear tderror1"/>
					</fr:layout>	
				</fr:edit>
			</logic:equal>
	
		</logic:notEmpty>
		
		<p class="mtop15">
			<html:submit><bean:message key="button.continue"/></html:submit>
		</p>
		
	</fr:form>

</logic:present>
