<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="title.rectorateSubmission" /></h2>

<p><html:link action="/rectorateDocumentSubmission.do?method=index">
    <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="link.rectorateSubmission.backToIndex" />
</html:link></p>

<bean:define id="creation" name="batch" property="creation" type="org.joda.time.DateTime" />
<bean:define id="count" name="batch" property="documentRequestCount" />
<h3 class="mtop15 mbottom05"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"
    key="label.rectorateSubmission.batchDetails" arg0="<%= creation.toString("dd-MM-yyyy") %>"
    arg1="<%= count.toString() %>" /></h3>

<logic:empty name="requests">
    <p><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.rectorateSubmission.noRequestsInBatch" /></p>
</logic:empty>

<logic:notEmpty name="requests">
    <fr:view name="requests">
        <fr:schema bundle="ACADEMIC_OFFICE_RESOURCES"
            type="net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest">
            <fr:slot name="registryCode.code" key="label.rectorateSubmission.registryCode" />
            <fr:slot name="documentRequestType" key="label.rectorateSubmission.registryCode" />
            <fr:slot name="requestedCycle" key="label.rectorateSubmission.requestedCycle" />
            <fr:slot name="degreeType" key="label.degreeType" />
            <fr:slot name="student.number" key="label.studentNumber" />
            <fr:slot name="person.name" key="label.Student.Person.name" />
            <fr:slot name="academicServiceRequestSituationType" key="label.currentSituation" />
            <fr:slot name="lastGeneratedDocument" layout="link" key="label.rectorateSubmission.document">
                <fr:property name="key" value="link.download" />
                <fr:property name="bundle" value="COMMON_RESOURCES" />
            </fr:slot>
        </fr:schema>
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle4 thlight thcenter" />
            <fr:property name="sortBy" value="registryCode.code=asc" />
            <fr:property name="link(view)"
                value="/student.do?method=visualizeRegistration" />
            <fr:property name="param(view)" value="registration.idInternal/registrationID" />
            <fr:property name="key(view)" value="link.rectorateSubmission.viewRegistration" />
            <fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES" />
            <fr:property name="blankTarget(view)" value="true" />
            <fr:property name="link(receive)"
                value="/academicServiceRequestsManagement.do?method=prepareReceiveAcademicServiceRequest" />
            <fr:property name="param(receive)" value="idInternal/academicServiceRequestId" />
            <fr:property name="key(receive)" value="link.rectorateSubmission.receive" />
            <fr:property name="bundle(receive)" value="ACADEMIC_OFFICE_RESOURCES" />
            <fr:property name="visibleIf(receive)" value="activeSituation.sentToExternalEntity" />
            <fr:property name="blankTarget(receive)" value="true" />
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<logic:notEmpty name="actions">
    <bean:define id="batchOid" name="batch" property="externalId" />
    <ul>
        <logic:iterate id="action" name="actions">
            <li><html:link
                action="<%= "/rectorateDocumentSubmission.do?method=" + action +  "&amp;batchOid=" + batchOid %>">
                <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="<%= "link.rectorateSubmission." + action %>" />
            </html:link></li>
        </logic:iterate>
    </ul>
</logic:notEmpty>