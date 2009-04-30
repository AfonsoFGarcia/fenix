<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>
<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile" %>

<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/introducao" %>"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/licenciaturas" %>"><bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<a href='<%= fullPath + "?method=beginCandidacyProcessIntro" %>'><bean:write name="application.name"/> </a> &gt;
	<bean:message key="title.view.candidacy.process" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:write name="application.name"/></h1>


<logic:equal name="individualCandidacyProcessBean" property="individualCandidacyProcess.allRequiredFilesUploaded" value="false">
<div class="h_box_alt">
	<div class="lightbulb">
		<p><bean:message key="message.missing.document.files" bundle="CANDIDATE_RESOURCES"/></p>
		<ul>
			<logic:iterate id="missingDocumentFileType" name="individualCandidacyProcessBean" property="individualCandidacyProcess.missingRequiredDocumentFiles">
				<li><fr:view name="missingDocumentFileType"/></li>
			</logic:iterate>
		</ul>
		<p><bean:message key="message.ist.conditions.note" bundle="CANDIDATE_RESOURCES"/></p>
	</div>
</div>
</logic:equal>



<fr:form action='<%= mappingPath + ".do" %>' id="editCandidacyForm">
	<input type="hidden" name="method" id="methodForm"/>
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	<noscript>
	<html:submit onclick="this.form.method.value='prepareEditCandidacyProcess';"><bean:message key="button.edit" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:submit onclick="this.form.method.value='prepareEditCandidacyDocuments';"><bean:message key="button.edit" bundle="APPLICATION_RESOURCES" /> Documentos</html:submit>
	<html:cancel><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</noscript>
	
	<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditCandidacyProcess';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.edit.application" bundle="CANDIDATE_RESOURCES"/></a> | 
	<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditCandidacyHabilitations';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.edit.application.qualifications" bundle="CANDIDATE_RESOURCES"/></a> |
	<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditCandidacyDocuments';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.edit.documents" bundle="CANDIDATE_RESOURCES"/></a>
</fr:form>

<h2 style="margin-top: 1em;"><bean:message key="title.personal.data" bundle="CANDIDATE_RESOURCES"/></h2>

<fr:view name="individualCandidacyProcessBean" 
	schema="PublicCandidacyProcess.candidacyDataBean">
	<fr:layout name="tabular">
		<fr:property name="classes" value="thlight thleft"/>
        <fr:property name="columnClasses" value="width175px,,,"/>
	</fr:layout>
</fr:view>

<table class="tdtop">
	<tr>
		<td class="width175px"><bean:message key="label.photo" bundle="CANDIDATE_RESOURCES"/>:</td>
		<td>
			<logic:present name="individualCandidacyProcessBean" property="individualCandidacyProcess.photo">
			<bean:define id="photo" name="individualCandidacyProcessBean" property="individualCandidacyProcess.photo"/>
			<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><img src="<%= request.getContextPath() + ((IndividualCandidacyDocumentFile) photo).getDownloadUrl() %>" />
			</logic:present>
			
			<logic:notPresent name="individualCandidacyProcessBean" property="individualCandidacyProcess.photo">
				<em><bean:message key="message.does.not.have.photo" bundle="CANDIDATE_RESOURCES"/></em>
			</logic:notPresent>
		</td>
	</tr>
</table>

	
<h2 style="margin-top: 1em;"><bean:message key="title.over23.qualifications" bundle="CANDIDATE_RESOURCES"/></h2>


<h3 style="margin-bottom: 0.5em;"><bean:message key="label.over23.qualifications.concluded" bundle="CANDIDATE_RESOURCES"/></h3>

<logic:empty name="individualCandidacyProcessBean" property="formationConcludedBeanList">
	<p class="mtop05"><em><bean:message key="label.over23.has.no.qualifications" bundle="CANDIDATE_RESOURCES"/>.</em></p>	
</logic:empty>

<logic:notEmpty name="individualCandidacyProcessBean" property="formationConcludedBeanList">
	<table class="tstyle2 thlight thleft">
	<tr>
		<th><bean:message key="label.over23.qualifications.name" bundle="CANDIDATE_RESOURCES"/></th>
		<th><bean:message key="label.over23.school" bundle="CANDIDATE_RESOURCES"/></th>
		<th><bean:message key="label.over23.execution.year.conclusion" bundle="CANDIDATE_RESOURCES"/></th>
	</tr>
	<logic:iterate id="qualification" name="individualCandidacyProcessBean" property="formationConcludedBeanList" indexId="index">
	<tr>
		<td>
			<fr:view 	name="qualification"
						schema="PublicCandidacyProcessBean.formation.designation">
				<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
			</fr:view>
		</td>
		<td>
			<fr:view name="qualification"
				schema="PublicCandidacyProcessBean.formation.institutionUnitName.view">
				<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
			</fr:view>	
		</td>
		<td>
			<fr:view 	name="qualification"
						schema="PublicCandidacyProcessBean.over23.execution.year.conclusion">
				<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
			</fr:view>
		</td>
	</tr>
	</logic:iterate>
	</table>
</logic:notEmpty>

<h3 style="margin-bottom: 0.5em;"><bean:message key="label.over23.qualifications.non.concluded" bundle="CANDIDATE_RESOURCES"/></h3>

<logic:empty name="individualCandidacyProcessBean" property="formationNonConcludedBeanList">
	<p class="mtop05"><em><bean:message key="label.over23.has.no.qualifications" bundle="CANDIDATE_RESOURCES"/>.</em></p>	
</logic:empty>

<logic:notEmpty name="individualCandidacyProcessBean" property="formationNonConcludedBeanList">
<logic:iterate id="qualification" name="individualCandidacyProcessBean" property="formationNonConcludedBeanList" indexId="index">
	<table class="thlight thleft">
		<tr>
			<th><bean:message key="label.over23.qualifications.name" bundle="CANDIDATE_RESOURCES"/>:</th>
			<td>
				<fr:view 	name="qualification"
							schema="PublicCandidacyProcessBean.formation.designation">
					<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
				</fr:view>
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.over23.school" bundle="CANDIDATE_RESOURCES"/>:</th>
			<td>
				<fr:view name="qualification"
					schema="PublicCandidacyProcessBean.formation.institutionUnitName.view">
					<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
				</fr:view>					
			</td>
		</tr>
	</table>
</logic:iterate>
</logic:notEmpty>

<h3><bean:message key="label.over23.languages" bundle="CANDIDATE_RESOURCES"/></h3>
<p>
	<bean:message key="label.over23.languages.read" bundle="CANDIDATE_RESOURCES"/>: 
	<fr:view 	name="individualCandidacyProcessBean"
				schema="PublicCandidacyProcessBean.over23.languages.read">
		<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
	</fr:view>
</p>

<p>
	<bean:message key="label.over23.languages.write" bundle="CANDIDATE_RESOURCES"/>: 
	<fr:view 	name="individualCandidacyProcessBean"
				schema="PublicCandidacyProcessBean.over23.languages.write">
		<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
	</fr:view>
</p>

<p>
	<bean:message key="label.over23.languages.speak" bundle="CANDIDATE_RESOURCES"/>: 
	<fr:view 	name="individualCandidacyProcessBean"
				schema="PublicCandidacyProcessBean.over23.languages.speak">
		<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
	</fr:view>
</p>


<h3 style="margin-top: 1em;"><bean:message key="title.over23.bachelor.first.cycle.choice" bundle="CANDIDATE_RESOURCES"/></h3>
<ol class="mtop05">
	<logic:iterate id="degree" name="individualCandidacyProcessBean" property="selectedDegrees" indexId="index">
		<li>				
			<fr:view name="degree" schema="Degree.name.and.sigla" >
				<fr:layout name="flow">
					<fr:property name="labelExcluded" value="true"/>
				</fr:layout> 
			</fr:view>
		</li>
	</logic:iterate>
</ol>

<logic:notEmpty name="individualCandidacyProcessBean" property="disabilities">
<h3 style="margin-top: 1em;"><bean:message key="label.over23.disabilities" bundle="CANDIDATE_RESOURCES"/></h3>
<fr:view 	name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcessBean.over23.disabilities">
	<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
</fr:view>
</logic:notEmpty>

<h2 style="margin-top: 1em;"><bean:message key="label.documentation" bundle="CANDIDATE_RESOURCES"/></h2> 

<bean:define id="individualCandidacyProcess" name="individualCandidacyProcessBean" property="individualCandidacyProcess"/>

<logic:empty name="individualCandidacyProcess" property="candidacy.documents">
	<p><em><bean:message key="label.no.documents.associated" bundle="CANDIDATE_RESOURCES"/>.</em></p>
</logic:empty>

<logic:notEmpty name="individualCandidacyProcess" property="candidacy.documents">
<table class="tstyle2 thlight thcenter">
	<tr>
		<th><bean:message key="label.candidacy.document.kind" bundle="CANDIDATE_RESOURCES"/></th>
		<th><bean:message key="label.dateTime.submission" bundle="CANDIDATE_RESOURCES"/></th>
		<th><bean:message key="label.document.file.name" bundle="CANDIDATE_RESOURCES"/></th>
	</tr>

	
	<logic:iterate id="documentFile" name="individualCandidacyProcess" property="candidacy.documents">
	<tr>
		<td><fr:view name="documentFile" property="candidacyFileType"/></td>
		<td><fr:view name="documentFile" property="uploadTime"/></td>
		<td><fr:view name="documentFile" property="filename"/></td>
	</tr>	
	</logic:iterate>
</table>
</logic:notEmpty>

