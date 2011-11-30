<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@page import="org.apache.struts.action.ActionMessages" %>
<%@ page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@ page import="java.util.Locale"%>
<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusApplyForSemesterType"%>

<%!
	static String f(String value, Object ... args) {
    	return String.format(value, args);
	}
%>




<script language="javascript">
	function set_image_size(imagetag, image) {
		var image_width = image.width;
		var image_height = image.height;
		
		if(image_width > 400 || image_height > 300) {
			var width_ratio = 400 / image_width;
			var height_ratio = 300 / image_height;

			imagetag.width = image_width * Math.min(width_ratio, height_ratio);
			imagetag.height = image_height * Math.min(width_ratio, height_ratio);
		} else {
			imagetag.width = image_width;
			imagetag.height = image_height;
		}
	}
</script>

<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>
<bean:define id="applicationInformationLinkDefault" name="application.information.link.default"/>
<bean:define id="applicationInformationLinkEnglish" name="application.information.link.english"/>

<div class="breadcumbs">
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en">NMCI</a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en/ist/">Study at IST</a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= f("%s/candidacies/erasmus", request.getContextPath()) %>'><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="erasmus.title.application.submission" bundle="CANDIDATE_RESOURCES" />
</div>

<h1><bean:write name="application.name"/></h1>

<p class="steps">
	<span><bean:message key="erasmus.label.step.one.personal.details" bundle="CANDIDATE_RESOURCES"/></span> >
	<span class="actual"><bean:message key="erasmus.label.step.two.educational.background" bundle="CANDIDATE_RESOURCES" /></span> >
	<span><bean:message key="erasmus.label.step.three.degree.and.subjects" bundle="CANDIDATE_RESOURCES" /></span> >
	<span><bean:message key="erasmus.label.step.four.honour.declaration" bundle="CANDIDATE_RESOURCES" /></span>	 
</p>


<html:messages id="message" message="true" bundle="CANDIDATE_RESOURCES" property="error">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>


<fr:form action='<%= mappingPath + ".do?userAction=createCandidacy" %>' id="erasmusCandidacyForm">

	<input type="hidden" id="methodId" name="method" value="createNewProcess"/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>
 	
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />

	<logic:notEmpty name="individualCandidacyProcessBean" property="candidacyProcess">
	
		<h2 class="mtop15 mbottom05"><bean:message key="label.erasmus.home.institution" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
		
		<p><em>Choose your country and university.</em></p>
		
		<fr:edit 	id="erasmusIndividualCandidacyProcessBean.home.institution" 
					name="individualCandidacyProcessBean" 
					schema="ErasmusIndividualCandidacyProcess.home.institution.edit" 
					property="erasmusStudentDataBean">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle5 thlight thleft mtop05 ulnomargin inobullet"/>
		        <fr:property name="columnClasses" value="width225px,,tdclear tderror1"/>
		        <fr:property name="requiredMarkShown" value="true" />
		        <fr:property name="requiredMessageShown" value="false" />
		        <fr:destination name="chooseCountryPostback" path="<%= "/candidacies/caseHandlingErasmusCandidacyIndividualProcess.do?method=chooseCountry" %>"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/candidacies/caseHandlingErasmusCandidacyIndividualProcess.do?method=continueCandidacyCreationInvalid" %>'  />
		</fr:edit>
		
		<h2 class="mtop15 mbottom05"><bean:message key="label.erasmus.current.study" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
		<fr:edit 	id="erasmusIndividualCandidacyProcessBean.current.study" 
					name="individualCandidacyProcessBean" 
					schema="ErasmusIndividualCandidacyProcess.current.study.edit" >
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle5 thlight thleft mtop05 ulnomargin inobullet"/>
		        <fr:property name="columnClasses" value="width225px,,tdclear tderror1"/>
		        <fr:property name="requiredMarkShown" value="true" />
		        <fr:property name="requiredMessageShown" value="false" />
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/candidacies/caseHandlingErasmusCandidacyIndividualProcess.do?method=continueCandidacyCreationInvalid" %>'  />
		</fr:edit>
		
		<h2 class="mtop15 mbottom05"><bean:message key="label.erasmus.period.of.study" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
		<p><em>Specify the date of arrival and departure. Also specify the types of study you're going to do at IST.</em></p>
		<fr:edit	id="erasmusIndividualCandidacyProcessBean.period.of.study"
					name="individualCandidacyProcessBean"
					schema="ErasmusIndividualCandidacyProcess.period.of.study.edit" >
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle5 thlight thleft mtop05 ulnomargin inobullet"/>
		        <fr:property name="columnClasses" value="width225px,,tdclear tderror1"/>
		        <fr:property name="requiredMarkShown" value="true" />
		        <fr:property name="requiredMessageShown" value="false" />
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/candidacies/caseHandlingErasmusCandidacyIndividualProcess.do?method=continueCandidacyCreationInvalid" %>'  />
		</fr:edit>
		
		<%--
		<h2 class="mtop15 mbottom05"><bean:message key="label.erasmus.applyForSemester" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
		<p><em>Choose the semester(s) you're applying to:</em></p>
		--%>
		<logic:equal name="individualCandidacyProcessBean" property="candidacyProcess.forSemester" value="<%= ErasmusApplyForSemesterType.FIRST_SEMESTER.name() %>">
			<fr:edit		id="erasmusStudentDataBean.applyForSemester.edit"
						name="individualCandidacyProcessBean"
						property="erasmusStudentDataBean"
						schema="ErasmusStudentDataBean.applyForSemester.edit">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle5 thlight thleft mtop05"/>
			        <fr:property name="columnClasses" value="width225px,,tdclear tderror1"/>
	  		        <fr:property name="requiredMarkShown" value="true" />
			        <fr:property name="requiredMessageShown" value="false" />
				</fr:layout>
			</fr:edit>
		</logic:equal>
		
		
		
		<h2 class="mtop15 mbottom05"><bean:message key="title.erasmus.language.competence" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
		<p><em>Note: All master programmes are lectured in english</em></p>

		<p><strong>Do you wish to participate in the intensive Portuguese Language Course?</strong></p>
		<logic:equal name="individualCandidacyProcessBean" property="candidacyProcess.forSemester" value="<%= ErasmusApplyForSemesterType.FIRST_SEMESTER.name() %>">
			<fr:edit	id="erasmusIndividualCandidacyProcessBean.language.intensive.course"
						name="individualCandidacyProcessBean"
						property="erasmusStudentDataBean"
						schema="ErasmusStudentData.languageCompetence.intensive.portuguese.course">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle5 thlight thleft mtop05 ulnomargin inobullet"/>
			        <fr:property name="columnClasses" value="width225px,,tdclear tderror1"/>
			        <fr:property name="requiredMarkShown" value="true" />
			        <fr:property name="requiredMessageShown" value="false" />
				</fr:layout>
			</fr:edit>
		</logic:equal>
		
		<logic:equal name="individualCandidacyProcessBean" property="candidacyProcess.forSemester" value="<%= ErasmusApplyForSemesterType.SECOND_SEMESTER.name() %>">
			<fr:edit	id="erasmusIndividualCandidacyProcessBean.language.intensive.course"
						name="individualCandidacyProcessBean"
						property="erasmusStudentDataBean">
				<fr:schema type="net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusStudentData" bundle="ACADEMIC_OFFICE_RESOURCES">
					<fr:slot name="intensivePortugueseCourseFebruary" key="label.erasmus.language.competence.intensivePortugueseCourseFebruary" layout="radio"/>
				</fr:schema>
						
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle5 thlight thleft mtop05 ulnomargin inobullet"/>
			        <fr:property name="columnClasses" value="width225px,,tdclear tderror1"/>
			        <fr:property name="requiredMarkShown" value="true" />
			        <fr:property name="requiredMessageShown" value="false" />
				</fr:layout>
			</fr:edit>
		</logic:equal>

		<h2 class="mtop15 mbottom05"><bean:message key="label.observations" bundle="CANDIDATE_RESOURCES"/></h2>
		<fr:edit id="individualCandidacyProcessBean.observations"
			name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcessBean.observations">
 		  <fr:layout name="flow">
		    <fr:property name="labelExcluded" value="true"/>
		  </fr:layout>
		</fr:edit>

	</logic:notEmpty>
	

	<p class="mtop1">
		<html:submit onclick="this.form.method.value='fillDegreeInformation'; return true;"><bean:message key="label.continue" bundle="APPLICATION_RESOURCES" /></html:submit>
		<%--
		<html:cancel onclick="this.form.method.value='backCandidacyCreation'; return true;"><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
		--%>
	</p>

</fr:form>

