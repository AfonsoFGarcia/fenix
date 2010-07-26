<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@ page import="java.util.Locale"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>
<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile" %>
<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacyProcess" %>

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

<bean:define id="individualCandidacyProcess" name="individualCandidacyProcessBean" property="individualCandidacyProcess" type="ErasmusIndividualCandidacyProcess"/>
<bean:define id="processId" name="individualCandidacyProcess" property="idInternal"/>

<div class="breadcumbs">
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en">NMCI</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en/ist/">Study at IST</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= f("%s/candidacies/erasmus", request.getContextPath()) %>'><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="erasmus.title.application.submission" bundle="CANDIDATE_RESOURCES" />
</div>

<h1><bean:write name="application.name"/></h1>


<p>
	<b class="highlight1"><bean:message key="label.process.id" bundle="CANDIDATE_RESOURCES"/>: <bean:write name="individualCandidacyProcess" property="processCode"/></b>
</p>



<logic:equal name="individualCandidacyProcess" property="allRequiredFilesUploaded" value="false">
	<p class="mtop05"><bean:message key="message.missing.document.files" bundle="CANDIDATE_RESOURCES"/></p>
	
	<ul>
		<li><b>Passport photo</b> - The photo will be used to generate IST student card.</li>
		<li><b>Passport or identity card</b></li>
		<li><b>Learning agreement</b> - You're required to download, sign, stamp and reupload the document.</li>
		<li><b>Curriculum vitae</b></li>
		<li><b>Transcript of records</b></li>
	</ul>
			
	<p class="mbottom05"><em><bean:message key="message.ist.conditions.note" bundle="CANDIDATE_RESOURCES"/></em></p>
</logic:equal>

<% if(!individualCandidacyProcess.getValidatedByGri() || !individualCandidacyProcess.getValidatedByErasmusCoordinator()) { %>

<div class="h_box">
	<h3 class="mtop05">Learning Agreement</h3>
	<p>Please download the Learning Agreement document below.</p>
	<p>Please note that the document must be signed and stamped by your school before you upload.</p>
	<p class="mbottom05"><html:link page="<%= mappingPath + ".do?method=retrieveLearningAgreement&processId=" + processId %>">Download learning agreement</html:link></p>
</div>
<% } %>

<script src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>" type="text/javascript" >
</script>

<logic:equal value="true" name="isApplicationSubmissionPeriodValid">
<fr:form action='<%= mappingPath + ".do" %>' id="editCandidacyForm">
	<input type="hidden" name="method" id="methodForm"/>
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	<noscript>
		<html:submit onclick="this.form.method.value='prepareEditCandidacyProcess';"><bean:message key="button.edit" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:submit onclick="this.form.method.value='prepareEditCandidacyDocuments';"><bean:message key="label.edit.candidacy.documents" bundle="CANDIDATE_RESOURCES" /></html:submit>
		<html:cancel><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</noscript>
	<p class="mtop15">
		<a href="#" onclick="$('#methodForm').attr('value', 'prepareEditCandidacyProcess'); $('#editCandidacyForm').submit();"><bean:message key="button.edit" bundle="APPLICATION_RESOURCES" /> <bean:message key="label.application.lowercase" bundle="CANDIDATE_RESOURCES"/></a> | 
		<a href="#" onclick="$('#methodForm').attr('value', 'prepareEditCandidacyInformation'); $('#editCandidacyForm').submit();"><bean:message key="label.edit.application.educational.background" bundle="CANDIDATE_RESOURCES"/></a> |
		<a href="#" onclick="$('#methodForm').attr('value', 'prepareEditDegreeAndCourses'); $('#editCandidacyForm').submit();"><bean:message key="erasmus.label.edit.degree.and.courses" bundle="CANDIDATE_RESOURCES" /></a> | 
		<a href="#" onclick="$('#methodForm').attr('value', 'prepareEditCandidacyDocuments'); $('#editCandidacyForm').submit();"> <b><bean:message key="label.edit.candidacy.documents" bundle="CANDIDATE_RESOURCES" /></b></a>

<%-- 		
		<logic:equal value="false" name="individualCandidacyProcess" property="isCandidacyProcessWithEidentifer">
			! <a href="#" onclick="$('#methodForm').attr('value', 'prepareBindLinkSubmitedIndividualCandidacyWithStork'); $('#editCandidacyForm').submit();"> <b><bean:message key="label.bind.national.citizen.card" bundle="CANDIDATE_RESOURCES" /></b></a>
		</logic:equal>
--%>

	</p>
</fr:form>
</logic:equal>

<logic:equal value="false" name="isApplicationSubmissionPeriodValid">

<% if(!individualCandidacyProcess.getValidatedByGri() || !individualCandidacyProcess.getValidatedByErasmusCoordinator()) { %>
<fr:form action='<%= mappingPath + ".do" %>' id="editCandidacyForm">
	<input type="hidden" name="method" id="methodForm"/>
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	<noscript>
		<html:submit onclick="this.form.method.value='prepareEditCandidacyDocuments';"><bean:message key="label.edit.candidacy.documents" bundle="CANDIDATE_RESOURCES" /></html:submit>
		<html:cancel><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</noscript>
	<p class="mtop15">
		<a href="#" onclick="$('#methodForm').attr('value', 'prepareEditCandidacyDocuments'); $('#editCandidacyForm').submit();"> <b><bean:message key="label.edit.candidacy.documents" bundle="CANDIDATE_RESOURCES" /></b></a>
	</p>
</fr:form>	
<% } %>

</logic:equal>



<h2 class="mtop1 mbottom05"><bean:message key="title.personal.data" bundle="CANDIDATE_RESOURCES"/></h2>

<logic:equal name="individualCandidacyProcessBean" property="individualCandidacyProcess.isCandidateWithRoles" value="true">
<fr:view name="individualCandidacyProcessBean" 
	schema="PublicCandidacyProcess.candidacyDataBean.internal.candidate.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thleft"/>
        <fr:property name="columnClasses" value="width175px,,,,"/>
	</fr:layout>
</fr:view>
</logic:equal>

<logic:equal name="individualCandidacyProcessBean" property="individualCandidacyProcess.isCandidateWithRoles" value="false">
<fr:view name="individualCandidacyProcess" property="personalDetails" 
	schema="ErasmusCandidacyProcess.personalData">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thleft"/>
        <fr:property name="columnClasses" value="width175px,,,,"/>
	</fr:layout>
</fr:view>
</logic:equal>



<h2 class="mtop1 mbottom05"><bean:message key="label.documentation" bundle="CANDIDATE_RESOURCES"/></h2> 

<logic:empty name="individualCandidacyProcess" property="candidacy.documents">
	<p>
		<em><bean:message key="message.documents.empty" bundle="CANDIDATE_RESOURCES"/>.</em>
	</p>
</logic:empty>

<logic:notEmpty name="individualCandidacyProcess" property="activeDocumentFiles">
<table class="tstyle2 thlight thcenter">
	<tr>
		<th><bean:message key="label.candidacy.document.kind" bundle="CANDIDATE_RESOURCES"/></th>
		<th><bean:message key="label.dateTime.submission" bundle="CANDIDATE_RESOURCES"/></th>
		<th><bean:message key="label.document.file.name" bundle="CANDIDATE_RESOURCES"/></th>
	</tr>

	
	<logic:iterate id="documentFile" name="individualCandidacyProcess" property="activeDocumentFiles">
	<tr>
		<td><fr:view name="documentFile" property="candidacyFileType"/></td>
		<td><fr:view name="documentFile" property="uploadTime"/></td>
		<td><fr:view name="documentFile" property="filename"/></td>
	</tr>	
	</logic:iterate>
</table>
</logic:notEmpty>

<% if(!individualCandidacyProcess.getValidatedByGri() || !individualCandidacyProcess.getValidatedByErasmusCoordinator()) { %>
<p><a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditCandidacyDocuments';document.getElementById('editCandidacyForm').submit();"> <bean:message key="label.edit.candidacy.documents" bundle="CANDIDATE_RESOURCES" /></a></p>
<% } %>

	<h2 class="mtop15 mbottom05"><bean:message key="label.erasmus.home.institution" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

	<fr:view name="individualCandidacyProcess" property="candidacy.erasmusStudentData" schema="ErasmusIndividualCandidacyProcess.home.institution.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width175px,,,,"/>
		</fr:layout>
	</fr:view>


	<h2 class="mtop1 mbottom05"><bean:message key="label.erasmus.current.study" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
	<em><bean:message key="label.erasmus.current.study.detailed" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
	<fr:view name="individualCandidacyProcess" schema="ErasmusIndividualCandidacyProcess.current.study.view" property="candidacy.erasmusStudentData">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width175px,,,,"/>
		</fr:layout>
	</fr:view>
	
	<h2 class="mtop1 mbottom05"><bean:message key="label.erasmus.period.of.study" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
	<fr:view name="individualCandidacyProcess" schema="ErasmusIndividualCandidacyProcess.period.of.study.view" property="candidacy.erasmusStudentData">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width175px,,,,"/>
		</fr:layout>
	</fr:view>


	<h2 class="mtop1 mbottom05"><bean:message key="label.erasmus.courses" bundle="CANDIDATE_RESOURCES"/></h2>

	<table class="tstyle2 thlight thcenter">
	<tr>
		<th><bean:message key="label.erasmus.course" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
	</tr>
	<logic:iterate id="course" name="individualCandidacyProcess" property="sortedSelectedCurricularCourses" indexId="index">
		<bean:define id="curricularCourseId" name="course" property="externalId" />
	<tr>
		<td>
			<fr:view 	name="course"
						property="nameI18N">
<%-- 				<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>--%>
			</fr:view>
		</td>
	</tr>
	</logic:iterate>
	</table>	

	<h2 class="mtop1 mbottom05"><bean:message key="label.erasmus.appliedSemester" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
	<fr:view	name="individualCandidacyProcess"
				property="candidacy.erasmusStudentData"
				schema="ErasmusStudentDataBean.applyForSemester.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width175px,,,,"/>
		</fr:layout>
	</fr:view>
	

	<h2 class="mtop1 mbottom05"><bean:message key="title.erasmus.language.competence" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
	
	<p class="mbottom05"><strong>Intensive Portuguese Course</strong></p>
	<fr:view	name="individualCandidacyProcess"
				property="candidacy.erasmusStudentData"
				schema="ErasmusStudentData.languageCompetence.intensive.portuguese.course.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thleft mtop025"/>
	        <fr:property name="columnClasses" value="width175px,,,,"/>
		</fr:layout>
	</fr:view>


	<%-- show approved learning agreements--%>

	<p><strong><bean:message key="label.erasmus.approved.learning.agreements" bundle="CANDIDATE_RESOURCES"/></strong></p> 
	
	<logic:equal name="individualCandidacyProcess" property="studentAccepted" value="false">
		<p class="mbottom05"><em><bean:message key="label.erasmus.approved.learning.agreements.empty" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>	
	</logic:equal>

	<logic:empty name="individualCandidacyProcess" property="candidacy.approvedLearningAgreements" >
		<p class="mbottom05"><em><bean:message key="label.erasmus.approved.learning.agreements.empty" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
	</logic:empty>
	
	<logic:equal name="individualCandidacyProcess" property="studentAccepted" value="true">
	<logic:notEmpty name="individualCandidacyProcess" property="candidacy.mostRecentApprovedLearningAgreement" >
		<fr:view name="individualCandidacyProcess" property="candidacy.mostRecentApprovedLearningAgreement">
			<fr:schema type="net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile" bundle="CANDIDATE_RESOURCES">
				<fr:slot name="filename" key="label.document.file.name" />
				<fr:slot name="this" key="label.document.file.link" layout="link"/>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	</logic:equal>


<div class="mtop2" id="contacts">
	<bean:message key="erasmus.contacts.text" bundle="CANDIDATE_RESOURCES" />
</div>


