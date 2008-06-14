<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.notNeedToEnrol.enrolment" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<fr:view name="bean" schema="notNeedToEnroll.info">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mvert15"/>
		<fr:property name="rowClasses" value="tdhl1,,,,,"/>
	</fr:layout>
</fr:view>

<table>
	<tr>
		<td>
			<span class="warning0"><bean:message key="label.notNeedToEnrol.deleteQuestion" bundle="ACADEMIC_OFFICE_RESOURCES"/></span>
		</td>
		<td>
			<fr:form action="/notNeedToEnrolEnrolments.do">
				<html:hidden property="method" value="delete"/>
				<fr:edit id="notNeedToEnrolBeanDelete" name="bean" visible="false"/>
				<html:submit><bean:message key="button.delete" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
			</fr:form>
		</td>
	</tr>
</table>


<fr:form action="/notNeedToEnrolEnrolments.do">
	<html:hidden property="method" value="editNotNeedToEnrol"/>
	
	<p class="mtop15 mbottom05"><strong><bean:message key="label.studentDismissal.internalEnrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>	
	<logic:notEmpty name="bean" property="aprovedEnrolments">
		<fr:edit id="aprovedEnrolments" name="bean" property="aprovedEnrolments" schema="notNeedToEnroll.view.aproved.enrolment">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle1 thlight mtop05"/>
				<fr:property name="columnClasses" value=",acenter,,acenter"/>
				<fr:property name="sortBy" value="aprovedEnrolment.studentCurricularPlan.name,aprovedEnrolment.curricularCourse.name"/>
			</fr:layout>
		</fr:edit>
	</logic:notEmpty>
	<logic:empty name="bean" property="aprovedEnrolments">
		<p>
			<em><bean:message key="label.studentDismissal.internalEnrolments.empty" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
		</p>
	</logic:empty>


	<p class="mtop15 mbottom05"><strong><bean:message key="label.studentDismissal.externalEnrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
	<logic:notEmpty name="bean" property="externalEnrolments">
		<fr:edit id="externalEnrolments" name="bean" property="externalEnrolments" schema="notNeedToEnroll.view.external.enrolment">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle1 thlight"/>
				<fr:property name="sortBy" value="externalEnrolment.code"/>
			</fr:layout>
		</fr:edit>
	</logic:notEmpty>
	<logic:empty name="bean" property="externalEnrolments">
		<p>			
			<em><bean:message key="label.studentDismissal.externalEnrolments.empty" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
		</p>				
	</logic:empty>
	
	<fr:edit id="notNeedToEnrolBean" name="bean" visible="false"/>
	<p class="mtop2">
		<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
		<html:submit onclick="this.form.method.value='back'; return true;"><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	</p>
</fr:form>
