<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.externalUnits.department" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:write name="unitResultBean" property="unit.name"/> </h2>

<fr:view name="unitResultBean" schema="AbstractExternalUnitResultBean.view-breadCrumbs-path">
	<fr:layout name="flow">
		<fr:property name="labelExcluded" value="true" />
	</fr:layout>
</fr:view>


<bean:define id="unitId">&amp;oid=<bean:write name="unitResultBean" property="unit.idInternal"/></bean:define>
<ul class="mtop15">
	<li><html:link page="<%="/externalUnits.do?method=prepareCreateExternalCurricularCourse" + unitId %>"><bean:message key="label.externalUnits.createExternalCurricularCourse" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
</ul>

<ul class="mtop15">
	<li><html:link page="<%="/externalUnits.do?method=prepareEditUnit" + unitId %>"><bean:message key="label.externalUnits.editInformation" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
</ul>

<table class="tstyle2">
	<tr><td class="aright"><bean:message key="label.externalUnits.externalCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/>:</td><td><bean:size id="size" name="externalCurricularCourses" /> <%= size %></td></tr>
</table>


<h3><bean:message key="label.externalUnits.externalCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/>:</h3>
<logic:notEmpty name="externalCurricularCourses">
	<fr:view name="externalCurricularCourses" schema="ExternalCurricularCourseResultBean.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight mvert05"/>
			<fr:property name="columnClasses" value=",acenter,acenter,acenter"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="externalCurricularCourses">
	<p>
		<em><bean:message key="label.externalUnits.noExternalCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	</p>
</logic:empty>

</logic:present>
