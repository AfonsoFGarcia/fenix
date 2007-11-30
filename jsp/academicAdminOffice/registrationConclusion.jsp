<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="student.registrationConclusionProcess" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<ul class="mtop2">
	<li>
		<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registrationConclusionBean" paramProperty="registration.idInternal">
			<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</li>
</ul>

<div style="float: right;">
	<bean:define id="personID" name="registrationConclusionBean" property="registration.student.person.idInternal"/>
	<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<p class="mvert2">
	<span class="showpersonid">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>

<logic:present name="registration" property="ingressionEnum">
<h3 class="mbottom025"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationDetail" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>
		<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
	</fr:layout>
</fr:view>
</logic:present>


<logic:notPresent name="registration" property="ingressionEnum">
<h3 class="mbottom025"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>
		<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
	</fr:layout>
</fr:view>
</logic:notPresent>

<logic:equal name="registrationConclusionBean" property="concluded" value="false">
	<ul class="list7 mtop2 error0" style="list-style: none;">
		<li>
			<span><bean:message key="registration.not.concluded" bundle="ACADEMIC_OFFICE_RESOURCES"/></span>
		</li>
	</ul>
</logic:equal>
<logic:equal name="registrationConclusionBean" property="concluded" value="true">
	<h3 class="mvert15"><bean:message key="label.summary" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

	<logic:equal name="registrationConclusionBean" property="byCycle" value="true" >
		<fr:view name="registrationConclusionBean" schema="RegistrationConclusionBean.confirmConclusionForCycle">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>
				<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
			</fr:layout>
		</fr:view>
	</logic:equal>
	<logic:equal name="registrationConclusionBean" property="byCycle" value="false" >
		<fr:view name="registrationConclusionBean" schema="RegistrationConclusionBean.confirmConclusionForRegistration">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>
				<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
			</fr:layout>
		</fr:view>
	</logic:equal>

	<p>
		<html:link page="/registration.do?method=prepareRegistrationConclusionDocument" paramId="registrationId" paramName="registration" paramProperty="idInternal">
			Folha de <bean:message key="student.registrationConclusionProcess" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</p>

</logic:equal>

<h3 class="mvert15"><bean:message key="registration.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<p class="mvert15">
	<fr:view name="registrationConclusionBean" property="curriculumForConclusion">
		<fr:layout>
			<fr:property name="visibleCurricularYearEntries" value="false" />
		</fr:layout>
	</fr:view>
</p>
