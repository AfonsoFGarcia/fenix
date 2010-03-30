<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@ page import="java.util.Locale"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>

<%!
	static String f(String value, Object ... args) {
    	return String.format(value, args);
	}
%>

<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>
<bean:define id="applicationInformationLinkDefault" name="application.information.link.default"/>
<bean:define id="applicationInformationLinkEnglish" name="application.information.link.english"/>

<div class="breadcumbs">
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en">NMCI</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en/ist/">Study at IST</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= f("%s/candidacies/erasmus", request.getContextPath()) %>'><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="erasmus.title.application.submission" bundle="CANDIDATE_RESOURCES" />
</div>

<h1><bean:write name="application.name"/></h1>

<p class="steps">
	<span><bean:message key="erasmus.label.step.one.personal.details" bundle="CANDIDATE_RESOURCES"/></span> >
	<span><bean:message key="erasmus.label.step.two.educational.background" bundle="CANDIDATE_RESOURCES" /></span> >
	<span class="actual"><bean:message key="erasmus.label.step.three.degree.and.subjects" bundle="CANDIDATE_RESOURCES" /></span> >
	<span><bean:message key="erasmus.label.step.four.honour.declaration" bundle="CANDIDATE_RESOURCES" /></span>	 
</p>

<%--
<p class="mtop15"><span><bean:message key="message.fields.required" bundle="CANDIDATE_RESOURCES"/></span></p>
--%>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES" property="captcha.error">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<html:messages id="message" message="true" bundle="CANDIDATE_RESOURCES" property="error">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<fr:hasMessages for="CandidacyProcess.personalDataBean" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<script src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>" type="text/javascript" >
</script>

<fr:form id="thisForm" action='<%= mappingPath + ".do?userAction=createCandidacy" %>'>

	<input type="hidden" id="removeId" name ="removeCourseId"/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>
	<input type="hidden" id="methodId" name="method" />
 	
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	<fr:edit id="degree.course.information.bean" name="degreeCourseInformationBean" visible="false" />

	<h2 class="mtop1"><bean:message key="label.erasmus.chooseCourses" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
	
	<bean:define id="universityName" name="individualCandidacyProcessBean" property="erasmusStudentDataBean.selectedUniversity.nameI18n.content" type="String"/> 
	
	<p class="mbottom05"><bean:message key="message.erasmus.for.chosen.university.must.select.majority.of.courses" bundle="ACADEMIC_OFFICE_RESOURCES" arg0="<%= universityName %>"/></p>

	<fr:view	name="individualCandidacyProcessBean"
				schema="ErasmusCandidacyProcess.view.possible.degrees">
			<fr:layout>
				<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
	</fr:view>

	<p>
		<strong><bean:message key="label.eramsus.candidacy.choosed.degree" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
	</p>
	
	<p>
		<fr:view name="individualCandidacyProcessBean" property="selectedCourseNameForView"/>
	</p>
	
	<p class="mbottom05"><em><bean:message key="message.erasmus.select.courses.of.associated.degrees" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
	
	<fr:edit id="degree.course.information.bean" name="degreeCourseInformationBean" schema="ErasmusCandidacyProcess.degreeCourseInformationBean">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
	        <fr:destination name="chooseDegreePostback" path="/candidacies/caseHandlingErasmusCandidacyIndividualProcess.do?method=chooseDegree" />
		</fr:layout>
	</fr:edit>
		
	<html:submit onclick="$('#methodId').attr('value', 'addCourse'); $('#skipValidationId').attr('value', 'true'); $('#thisForm').submit(); return true;">+ Add</html:submit>
	
	<logic:empty name="individualCandidacyProcessBean" property="sortedSelectedCurricularCourses">
		<p class="mvert15"><em><bean:message key="erasmus.message.empty.courses" bundle="CANDIDATE_RESOURCES" />.</em></p>
	</logic:empty>
	
	<logic:notEmpty name="individualCandidacyProcessBean" property="sortedSelectedCurricularCourses">
	
		<p class="mtop2 mbottom05"><b>Selected subjects</b></p>
	
		<table class="tstyle1 thlight thcenter mtop05">
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
								property="nameI18N"/>
				</td>
				<td>
					<fr:view	name="course"
								property="degree.nameI18N" /> - 
					<fr:view	name="course"
								property="degree.sigla" />
				</td>			
				<td>
					<a href="#" onclick="<%= f("$('#methodId').attr('value', 'removeCourse'); $('#skipValidationId').attr('value', 'true'); $('#removeId').attr('value', %s); $('#thisForm').submit()", curricularCourseId) %>"><bean:message key="label.erasmus.remove" bundle="ACADEMIC_OFFICE_RESOURCES" /></a>
				</td>
			</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>

	<p>
		<strong><bean:message key="label.eramsus.candidacy.choosed.degree" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>:
		<fr:view	name="individualCandidacyProcessBean" property="selectedCourseNameForView"/>
	</p>

	
	<p class="mtop2">
		<html:submit onclick="this.form.method.value='acceptHonourDeclaration'; return true;"><bean:message key="label.continue" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:cancel onclick="this.form.method.value='listProcesses'; return true;"><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</p>

</fr:form>
