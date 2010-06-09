<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%!

	static String f(String value, Object ... args) {
    	return String.format(value, args);
	}
%>


<html:xhtml/>


<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>

<%-- <h2><bean:message key="label.candidacy.edit" bundle="APPLICATION_RESOURCES"/></h2> --%>

<h2>Edit Degree and Course Information</h2>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>
<fr:hasMessages for="individualCandidacyProcessBean.precedentDegreeInformation" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<bean:define id="processId" name="process" property="idInternal" />

<script src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>" type="text/javascript" ></script>

<fr:form action='<%= f("/caseHandlingErasmusIndividualCandidacyProcess.do?userAction=editCandidacy&amp;processId=%s&amp", processId.toString()) %>'>

	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	<fr:edit id="degree.course.information.bean" name="degreeCourseInformationBean" visible="false" />

	<p><strong><bean:message key="label.erasmus.home.institution" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
	<fr:edit 	id="erasmusIndividualCandidacyProcessBean.home.institution" 
				name="individualCandidacyProcessBean"
				schema="ErasmusIndividualCandidacyProcess.university.edit" 
				property="erasmusStudentDataBean">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle5 thlight thright"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="chooseCountryPostback" path="<%= "/caseHandlingErasmusIndividualCandidacyProcess.do?userAction=editCandidacy&method=chooseCountry&amp;processId=" + processId.toString() %>"/>
		<fr:destination name="chooseUniversityPostback" path="<%= "/caseHandlingErasmusIndividualCandidacyProcess.do?userAction=editCandidacy&method=chooseUniversity&amp;processId=" + processId.toString() %>"/>
	</fr:edit>
</fr:form>

<logic:empty name="individualCandidacyProcessBean" property="erasmusStudentDataBean.selectedUniversity">
	<p><em><bean:message key="message.erasmus.select.university" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
</logic:empty>

<logic:notEmpty name="individualCandidacyProcessBean" property="erasmusStudentDataBean.selectedUniversity">
<fr:form action='<%= f("/caseHandlingErasmusIndividualCandidacyProcess.do?userAction=editCandidacy&amp;processId=%s&amp", processId.toString()) %>' id="thisForm">

	<input type="hidden" id="removeId" name ="removeCourseId"/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>
	<input type="hidden" id="methodId" name="method" />
 	
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	<fr:edit id="degree.course.information.bean" name="degreeCourseInformationBean" visible="false" />


	<p><strong><bean:message key="label.erasmus.chooseCourses" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>

	<bean:define id="universityName" name="individualCandidacyProcessBean" property="erasmusStudentDataBean.selectedUniversity.nameI18n.content" type="String"/> 
	<p><em><bean:message key="message.erasmus.for.chosen.university.must.select.majority.of.courses" bundle="ACADEMIC_OFFICE_RESOURCES" arg0="<%= universityName %>"/></em></p>

	<fr:view	name="individualCandidacyProcessBean"
				schema="ErasmusCandidacyProcess.view.possible.degrees">
			<fr:layout>
				<fr:property name="classes" value="tstyle1 thlight thright"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
	</fr:view>
	
	
	<p><em><bean:message key="message.erasmus.select.courses.of.associated.degrees" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
	<fr:edit id="degree.course.information.bean.edit" name="degreeCourseInformationBean" schema="ErasmusCandidacyProcess.degreeCourseInformationBean">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle5 thlight thright"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
	        <fr:destination name="chooseDegreePostback" path='<%= f("/caseHandlingErasmusIndividualCandidacyProcess.do?userAction=editCandidacy&method=chooseDegree&amp;processId=%s", processId) %>' />
		</fr:layout>
	</fr:edit>
		
	
	<p><html:submit onclick="$('#methodId').attr('value', 'addCourse'); $('#skipValidationId').attr('value', 'true'); $('#thisForm').submit(); return true;">+ <bean:message key="label.add" bundle="APPLICATION_RESOURCES" /></html:submit></p>
	
	
	
	
	<table class="tstyle2 thlight thcenter">
	<tr>
		<th><bean:message key="label.erasmus.course" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
		<th><bean:message key="label.erasmus.degree" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
		<th><!-- just in case --></th>
	</tr>
	<logic:iterate id="course" name="individualCandidacyProcessBean" property="sortedSelectedCurricularCourses" indexId="index">
		<bean:define id="curricularCourseId" name="course" property="externalId" />
	<tr>
		<td>
			<fr:view 	name="course"
						property="nameI18N">
			</fr:view>
		</td>
		<td>
			<fr:view	name="course"
						property="degree.nameI18N" /> - 
			<fr:view	name="course"
						property="degree.sigla" />
		</td>		
		<td>
			<a onclick="<%= f("$('#methodId').attr('value', 'removeCourse'); $('#skipValidationId').attr('value', 'true'); $('#removeId').attr('value', %s); $('#thisForm').submit()", curricularCourseId) %>"><bean:message key="label.erasmus.remove" bundle="ACADEMIC_OFFICE_RESOURCES" /></a>
		</td>
	</tr>
	</logic:iterate>
	</table>

	<p>
		<strong><bean:message key="label.eramsus.candidacy.choosed.degree" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>:
		<fr:view	name="individualCandidacyProcessBean" property="selectedCourseNameForView"/>
	</p>
	
	<p>
		<html:submit onclick="this.form.method.value='executeEditDegreeAndCoursesInformation'; return true;"><bean:message key="label.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:cancel onclick="this.form.method.value='listProcessAllowedActivities'; return true;"><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</p>
</fr:form>
</logic:notEmpty>
