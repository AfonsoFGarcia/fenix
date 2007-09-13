<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.curricularCourseRequests" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<div style="float: right;">
	<bean:define id="personID" name="bean" property="registration.student.person.idInternal"/>
	<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<p class="mvert2">
	<span class="showpersonid">
		<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="bean" property="registration.student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>

<logic:present name="bean" property="registration.ingressionEnum">
<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="bean" property="registration" schema="student.registrationDetail" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
		<fr:property name="rowClasses" value=",tdhl1,,,,,"/>
	</fr:layout>
</fr:view>
</logic:present>
<logic:notPresent name="bean" property="registration.ingressionEnum">
<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="bean" property="registration" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
		<fr:property name="rowClasses" value=",tdhl1,,,,,"/>
	</fr:layout>
</fr:view>
</logic:notPresent>

<bean:define id="registrationId" name="bean" property="registration.idInternal"/>
<fr:edit name="bean" schema="curricular.course.requests" action="<%= "/curricularCourseRequests.do?method=createRequest&registrationId=" + registrationId %>">
	<fr:layout>
		<fr:property name="classes" value="tstyle5"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= "/student.do?method=visualizeRegistration&registrationID=" + registrationId%>"/>
</fr:edit>	

