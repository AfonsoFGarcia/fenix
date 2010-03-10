<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>
<%@page import="org.apache.struts.action.ActionMessages" %>
<%@ page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@ page import="java.util.Locale"%>

<%!
	static String f(String value, Object ... args) {
    	return String.format(value, args);
	}
%>


<html:xhtml/>

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


<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>
<bean:define id="applicationInformationLinkDefault" name="application.information.link.default"/>
<bean:define id="applicationInformationLinkEnglish" name="application.information.link.english"/>

<div class="breadcumbs">
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en">GRI</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en/ist/">Study at IST</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= f("%s/candidacies/erasmus", request.getContextPath()) %>'><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="erasmus.title.application.submission" bundle="CANDIDATE_RESOURCES" />
</div>

<h1><bean:write name="application.name"/></h1>

<p class="steps">
	<span><bean:message key="erasmus.label.step.one.personal.details" bundle="CANDIDATE_RESOURCES"/></span> >
	<span class="actual"><bean:message key="erasmus.label.step.two.educational.background" bundle="CANDIDATE_RESOURCES" /></span> >
	<span><bean:message key="erasmus.label.step.three.degree.and.subjects" bundle="CANDIDATE_RESOURCES" /></span> >
	<span><bean:message key="erasmus.label.step.four.honour.declaration" bundle="CANDIDATE_RESOURCES" /></span>	 
</p>


<fr:form action='<%= mappingPath + ".do?userAction=createCandidacy" %>' id="erasmusCandidacyForm">

	<input type="hidden" id="methodId" name="method" value="createNewProcess"/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>
 	
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />

	<logic:notEmpty name="individualCandidacyProcessBean" property="candidacyProcess">
	
		<h2 class="mtop1"><bean:message key="label.erasmus.home.institution" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
		<fr:edit 	id="erasmusIndividualCandidacyProcessBean.home.institution" 
					name="individualCandidacyProcessBean" 
					schema="ErasmusIndividualCandidacyProcess.home.institution.edit" 
					property="erasmusStudentDataBean">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		        <fr:property name="requiredMarkShown" value="true" />
		        <fr:destination name="chooseCountryPostback" path="<%= "/candidacies/caseHandlingErasmusCandidacyIndividualProcess.do?method=chooseCountry" %>"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/candidacies/caseHandlingErasmusCandidacyIndividualProcess.do?method=continueCandidacyCreationInvalid" %>'  />
		</fr:edit>
		
		<h2 class="mtop1"><bean:message key="label.erasmus.current.study" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
		<fr:edit 	id="erasmusIndividualCandidacyProcessBean.current.study" 
					name="individualCandidacyProcessBean" 
					schema="ErasmusIndividualCandidacyProcess.current.study.edit" >
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		        <fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/candidacies/caseHandlingErasmusCandidacyIndividualProcess.do?method=continueCandidacyCreationInvalid" %>'  />
		</fr:edit>
		
		<h2 class="mtop1"><bean:message key="label.erasmus.period.of.study" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
		<fr:edit	id="erasmusIndividualCandidacyProcessBean.period.of.study"
					name="individualCandidacyProcessBean"
					schema="ErasmusIndividualCandidacyProcess.period.of.study.edit" >
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		        <fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/candidacies/caseHandlingErasmusCandidacyIndividualProcess.do?method=continueCandidacyCreationInvalid" %>'  />
		</fr:edit>
		
	</logic:notEmpty>
	
	<p></p>
	
	<html:submit onclick="this.form.method.value='fillDegreeInformation'; return true;"><bean:message key="label.continue" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel onclick="this.form.method.value='backCandidacyCreation'; return true;"><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>

</fr:form>
