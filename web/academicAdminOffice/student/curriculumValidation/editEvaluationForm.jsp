<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%@ page import="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean" %>
<%@ page import="net.sourceforge.fenixedu.domain.Enrolment" %>

<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.curriculum.validation.set.evaluations" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="studentCurricularPlanId" name="studentCurricularPlan" property="externalId"/>
<bean:define id="executionSemesterId" name="executionSemester" property="externalId" />
<bean:define id="enrolmentId" name="enrolment" property="externalId" />

<%-- 
<fr:form action="/studentEnrolments.do?method=backViewRegistration">
	<fr:edit id="studentEnrolment-back" name="studentEnrolmentBean" visible="false" />
	<html:cancel><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
</fr:form>
--%>

<p>
	<html:link page="<%= "/curriculumValidation.do?method=prepareSetEvaluations&amp;studentCurricularPlanId=" + studentCurricularPlanId  %>">
		<bean:message key="label.back" bundle="ACADEMIC_OFFICE_RESOURCES" />
	</html:link>
</p>

<logic:equal name="studentCurriculumValidationAllowed" value="false">
	<bean:message key="message.curriculum.validation.not.allowed" bundle="ACADEMIC_OFFICE_RESOURCES" />
</logic:equal>

<logic:equal name="studentCurriculumValidationAllowed" value="true"> 

<fr:hasMessages type="validation">
<div class="mvert1">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</div>
</fr:hasMessages>


<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="grade-messages">
	<p>
		<span class="error0"><bean:write name="message" /></span>
	</p>
</html:messages>

<style>
	th.evaluations {
	padding: 0;
	}
	td.evaluations {
	padding: 0;
	}
	td.evaluations table {
	margin: 0;
	border-collapse: collapse;
	}
	td.evaluations table td {
	padding-left: 0;
	padding-right: 0;
	border-left: none;
	border-right: none;
	}
	td.evaluations table tr th {
	/*display: none;*/
	padding: 0.25em;
	border-left: none;
	border-right: none;
	}
	td.evaluations table tr td {
	padding: 0.5em 0.25em;
	text-align: center;
	}
	table tr td table.tstyle4 tr td.eval_type {
	width: 175px !important;
	}
	td.evaluations table {
	}
	.borderBottomNone td {
	border-bottom: none;
	}
	.borderTopNone td {
	border-top: none;
	}
</style>

<fr:form action="<%= "/curriculumValidation.do?method=editEvaluations&amp;studentCurricularPlanId=" + studentCurricularPlanId + "&amp;executionSemesterId=" + executionSemesterId + "&amp;enrolmentId=" + enrolmentId %>">
	<table class="tstyle4 tdcenter thlight mtop05">
		<thead>
			<tr>
				<th style="width: 150px;"><bean:message key="label.set.evaluation.curricular.course.name" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
				<th style="width: 70px;"><bean:message key="label.set.evaluation.enrolment.state" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
				<th style="width: 80px; text-align: center;"><bean:message key="label.set.evaluation.enrolment.condition" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
				<th></th>
			</tr>
		</thead>
		
		<tbody>
			<% int i = 0; %>
			<logic:iterate id="entries" name="entriesList">
			
				<tr>
					<td>
						<%= ((java.util.List<MarkSheetEnrolmentEvaluationBean>) entries).get(0).getName() %>
						(<bean:message key="label.grade.scale" bundle="ACADEMIC_OFFICE_RESOURCES" />
						<%= ((java.util.List<MarkSheetEnrolmentEvaluationBean>) entries).get(0).getEnrolment().getGradeScale().getDescription() %>)
					</td>
					<td>
						<%= ((java.util.List<MarkSheetEnrolmentEvaluationBean>) entries).get(0).getEnrolmentState() %>
					</td>
					<td>
						<%= ((java.util.List<MarkSheetEnrolmentEvaluationBean>) entries).get(0).getEnrolmentCondition() %>
					</td>
					
					<td class="evaluations separator" colspan="7">
						<fr:edit 	id="<%= "set.evaluations.form." + i %>"
									name="entries"
									schema="set.evaluation.curriculum.entry"
									layout="tabular-editable">
							<fr:layout>
								<fr:property name="classes" value="tstyle4 tdcenter thlight"/>
						        <fr:property name="columnClasses" value="eval_type,,,,,,"/>
						        <fr:property name="rowClasses" value="borderTopNone,,,borderBottomNone" />
						        <fr:property name="display-headers" value="false" />			                
							</fr:layout>			 
						</fr:edit>
					</td>
				</tr>
				
				<% i++; %>
			</logic:iterate>
			
		</tbody>
	</table>

	<fr:edit id="set.evaluations.form" name="entriesList" />
	
	<html:submit><bean:message key="label.submit" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit>
</fr:form>

</logic:equal>