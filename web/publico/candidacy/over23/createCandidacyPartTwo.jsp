<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>

<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/introducao" %>"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/licenciaturas" %>"><bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<a href='<%= fullPath + "?method=beginCandidacyProcessIntro" %>'><bean:write name="application.name"/> </a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:write name="application.name"/></h1>

<p class="steps">
	<span><bean:message key="label.step.one.personal.details" bundle="CANDIDATE_RESOURCES"/></span> &gt; 
	<span class="actual"><bean:message key="label.step.two.habilitations.document.files" bundle="CANDIDATE_RESOURCES"/></span>
</p>

<p class="mtop15"><bean:message key="message.fields.required" bundle="CANDIDATE_RESOURCES"/></p>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES" property="captcha.error">
	<span class="error0"><bean:write name="message"/></span>
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

<fr:form id="over23CandidacyForm" action='<%= mappingPath + ".do?userAction=createCandidacy" %>' encoding="multipart/form-data">

		<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
		
		<input type="hidden" id="methodId" name="method" value="submitCandidacy"/>
		<input type="hidden" id="removeIndexId" name="removeIndex" value=""/>
		<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>
		
		
		
		<h2 style="margin-top: 1em;"><bean:message key="title.over23.qualifications" bundle="CANDIDATE_RESOURCES"/></h2>
		
		<h3><bean:message key="label.over23.qualifications.concluded" bundle="CANDIDATE_RESOURCES"/></h3>
		<logic:iterate id="qualification" name="individualCandidacyProcessBean" property="formationConcludedBeanList" indexId="index">
			<bean:define id="qualificationId" name="qualification" property="id"/>
			<bean:define id="designationId"><%= "individualCandidacyProcessBean.habilitation.concluded.designation:" + qualificationId %></bean:define>
			<bean:define id="institutionNameId"><%= "individualCandidacyProcessBean.habilitation.concluded.institutionName:" + qualificationId %></bean:define>
			<bean:define id="beginYearId"><%= "individualCandidacyProcessBean.habilitation.concluded.begin.year:" + qualificationId %></bean:define>
			<bean:define id="endYearId"><%= "individualCandidacyProcessBean.habilitation.concluded.end.year:" + qualificationId %></bean:define>
			<bean:define id="designationErrorId"><%=  "error.individualCandidacyProcessBean.formation.designation:" + qualificationId %></bean:define>
			<bean:define id="institutionErrorId"><%=  "error.individualCandidacyProcessBean.formation.institution:" + qualificationId %></bean:define>
			<bean:define id="durationErrorId"><%=  "error.individualCandidacyProcessBean.formation.duration:" + qualificationId %></bean:define>
			<table class="tstyle5 thlight thleft mbottom0">
				<tr>
					<th style="width: 175px;"><bean:message key="label.over23.qualifications.name" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<fr:edit 	id='<%= designationId %>' 
									name="qualification"
									schema="PublicCandidacyProcessBean.formation.designation">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>
					</td>
					<td rowspan="3">
						<p><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + index + "; document.getElementById(\"methodId\").value=\"removeConcludedHabilitationsEntry\"; document.getElementById(\"over23CandidacyForm\").submit();" %>' href="#" >Remover </a></p>
					</td>
					<td class="tdclear">
						<span class="error0"><fr:message for="<%= designationId %>"/></span>
					</td>
				</tr>
				<tr>
					<th><bean:message key="label.over23.school" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<fr:edit 	id='<%= institutionNameId %>' 
							name="qualification"
							schema="PublicCandidacyProcessBean.formation.institutionUnitName">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>					
					</td>
					<td class="tdclear">
						<span class="error0"><fr:message for="<%= institutionNameId %>"/></span>
					</td>					
				</tr>
				<tr>
					<th><bean:message key="label.over23.execution.year.conclusion" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<fr:edit 	id='<%= endYearId %>'
									name="qualification"
									schema="PublicCandidacyProcessBean.over23.execution.year.conclusion">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>
					</td>
					<td class="tdclear">
		                <span class="error0"><fr:message for="<%= endYearId %>"/></span>
					</td>					
				</tr>
			</table>
		</logic:iterate>
		<p class="mtop05 mbottom2"><a onclick="document.getElementById('skipValidationId').value='true'; document.getElementById('methodId').value='addConcludedHabilitationsEntry'; document.getElementById('over23CandidacyForm').submit();" href="#">+ Adicionar</a></p>
		
		<h3><bean:message key="label.over23.qualifications.non.concluded" bundle="CANDIDATE_RESOURCES"/></h3>
		<logic:iterate id="qualification" name="individualCandidacyProcessBean" property="formationNonConcludedBeanList" indexId="index">
			<bean:define id="qualificationId" name="qualification" property="id"/>
			<bean:define id="designationId"><%= "individualCandidacyProcessBean.habilitation.concluded.designation:" + qualificationId %></bean:define>
			<bean:define id="institutionNameId"><%= "individualCandidacyProcessBean.habilitation.concluded.institutionName:" + qualificationId %></bean:define>
			<bean:define id="beginYearId"><%= "individualCandidacyProcessBean.habilitation.concluded.begin.year:" + qualificationId %></bean:define>
			<bean:define id="endYear"><%= "individualCandidacyProcessBean.habilitation.concluded.end.year:" + qualificationId %></bean:define>
			<bean:define id="designationErrorId"><%=  "error.individualCandidacyProcessBean.formation.designation:" + qualificationId %></bean:define>
			<bean:define id="institutionErrorId"><%=  "error.individualCandidacyProcessBean.formation.institution:" + qualificationId %></bean:define>
			<bean:define id="durationErrorId"><%=  "error.individualCandidacyProcessBean.formation.duration:" + qualificationId %></bean:define>
		
			<table class="tstyle5 thlight thleft mbottom0">
				<tr>
					<th style="width: 175px;"><bean:message key="label.over23.qualifications.name" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<fr:edit 	id='<%= designationId %>' 
									name="qualification"
									schema="PublicCandidacyProcessBean.formation.designation">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>
					</td>
					<td rowspan="3">
						<p><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + index + "; document.getElementById(\"methodId\").value=\"removeNonConcludedHabilitationsEntry\"; document.getElementById(\"over23CandidacyForm\").submit();" %>' href="#" >Remover </a></p>
					</td>
					<td class="tdclear">
		                <span class="error0"><fr:message for="<%= designationId %>"/></span>
					</td>
				</tr>
				<tr>
					<th><bean:message key="label.over23.school" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<fr:edit 	id='<%= institutionNameId %>' 
							name="qualification"
							schema="PublicCandidacyProcessBean.formation.institutionUnitName">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>					
					</td>
					<td class="tdclear">
		                <span class="error0"><fr:message for="<%= institutionNameId %>"/></span>
					</td>
				</tr>
			</table>
		</logic:iterate>
		<p class="mtop05 mbottom2"><a onclick="document.getElementById('skipValidationId').value='true'; document.getElementById('methodId').value='addNonConcludedHabilitationsEntry'; document.getElementById('over23CandidacyForm').submit();" href="#">+ Adicionar</a></p>




		<h3><bean:message key="label.over23.languages" bundle="CANDIDATE_RESOURCES"/></h3>
		
		<p class="mbottom05"><bean:message key="label.over23.languages.read" bundle="CANDIDATE_RESOURCES"/>&nbsp;<bean:message key="label.delimited.by.commas" bundle="CANDIDATE_RESOURCES"/>:</p>
		<fr:edit 	id='PublicCandidacyProcessBean.over23.languages.read' 
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.over23.languages.read">
			<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
		</fr:edit>					
		
		<p class="mbottom05"><bean:message key="label.over23.languages.write" bundle="CANDIDATE_RESOURCES"/>&nbsp;<bean:message key="label.delimited.by.commas" bundle="CANDIDATE_RESOURCES"/>:</p>
		<fr:edit 	id='PublicCandidacyProcessBean.over23.languages.write' 
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.over23.languages.write">
			<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
		</fr:edit>					

		<p class="mbottom05"><bean:message key="label.over23.languages.speak" bundle="CANDIDATE_RESOURCES"/>&nbsp;<bean:message key="label.delimited.by.commas" bundle="CANDIDATE_RESOURCES"/>:</p>
		<fr:edit 	id='PublicCandidacyProcessBean.over23.languages.speak' 
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.over23.languages.speak">
			<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
		</fr:edit>					
		
		
		
		
		
		
		<h2 style="margin-top: 1em;"><bean:message key="title.over23.bachelor.first.cycle.choice" bundle="CANDIDATE_RESOURCES"/></h2>
		
		<p class="mbottom05">Escolha um curso no menu e carregue no link "Seleccionar curso" para adicionar o curso � lista de cursos a que se candidata.</p>
		<fr:edit 	id='PublicCandidacyProcessBean.degrees'
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.over23.degrees">
			<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
		</fr:edit> <span class="red">*</span>
		<p class="mtop05"><a onclick="document.getElementById('skipValidationId').value='true'; document.getElementById('methodId').value='addSelectedDegreesEntry'; document.getElementById('over23CandidacyForm').submit();" href="#">Seleccionar curso</a></p>
				
		<logic:notEmpty name="individualCandidacyProcessBean" property="selectedDegrees">
			<ol class="mtop05">
			<logic:iterate id="degree" name="individualCandidacyProcessBean" property="selectedDegrees" indexId="index">
				<li>
					<fr:view name="degree" schema="Degree.name.and.sigla" >
						<fr:layout name="flow">
							<fr:property name="labelExcluded" value="true"/>
						</fr:layout> 
					</fr:view> | 
					<a onclick='<%= "document.getElementById('skipValidationId').value=\"true\"; document.getElementById(\"removeIndexId\").value=" + index + "; document.getElementById(\"methodId\").value=\"removeSelectedDegreesEntry\"; document.getElementById(\"over23CandidacyForm\").submit();" %>' href="#" ><bean:message key="label.over23.remove" bundle="CANDIDATE_RESOURCES"/> </a>
				</li>
			</logic:iterate>
			</ol>
		</logic:notEmpty>
	
	

	
		<h2 style="margin-top: 1em;"><bean:message key="label.over23.disabilities" bundle="CANDIDATE_RESOURCES"/></h2>
		<p class="mbottom05"><bean:message key="message.over23.disabilities.detail" bundle="CANDIDATE_RESOURCES"/>:</p>
		<fr:edit 	id="individualCandidacyProcessBean.disabilities"
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.over23.disabilities">
			<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
		</fr:edit>
		
		
		
		<h2 style="margin-top: 1em;"><bean:message key="title.over23.honor.declaration" bundle="CANDIDATE_RESOURCES"/></h2>
		<p><bean:message key="message.over23.honor.declaration.detail" bundle="CANDIDATE_RESOURCES"/></p>
		<p>
			<fr:edit 	id="individualCandidacyProcessBean.honor.declaration"
						name="individualCandidacyProcessBean"
						schema="PublicCandidacyProcessBean.over23.honor.agreement">
				<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
			</fr:edit>
			<bean:message key="label.over23.honor.declaration" bundle="CANDIDATE_RESOURCES"/> <span class="red">*</span>
		</p>
		
	
		
		<h2 style="margin-top: 1em;"><bean:message key="label.documentation" bundle="CANDIDATE_RESOURCES"/></h2>
		
		<p style="margin-bottom: 0.5em;"><bean:message key="label.curriculum.vitae.academic.professional" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></p>
		<fr:edit id="individualCandidacyProcessBean.document.file.curriculum.vitae"
			name="individualCandidacyProcessBean"
			property="curriculumVitaeDocument"
			schema="PublicCandidacyProcessBean.documentUploadBean">
		  <fr:layout name="flow">
		    <fr:property name="labelExcluded" value="true"/>
		  </fr:layout>
		</fr:edit>

		<p style="margin-bottom: 0.5em;"><bean:message key="label.legalized.transcript" bundle="CANDIDATE_RESOURCES"/>:</p>
		<logic:iterate id="document" name="individualCandidacyProcessBean" property="habilitationCertificateList" indexId="index">
			<bean:define id="documentId" name="document" property="id"/>
			<div class="mbottom05">
			<fr:edit id="<%= "individualCandidacyProcessBean.document.file.habilitation.certificate:" + index %>" 
				name="document"
				schema="PublicCandidacyProcessBean.documentUploadBean">
			  <fr:layout name="flow">
			    <fr:property name="labelExcluded" value="true"/>
			  </fr:layout>
			</fr:edit>
			<% if(index > 0) { %>
			<a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + index + "; document.getElementById(\"methodId\").value=\"removeHabilitationCertificateDocumentFileEntry\"; document.getElementById(\"over23CandidacyForm\").submit();" %>' href="#" ><bean:message key="label.over23.remove" bundle="CANDIDATE_RESOURCES"/>  </a>
			<% } %>

			</div>	
		</logic:iterate>
		<p class="mtop05"><a onclick="document.getElementById('skipValidationId').value='true'; document.getElementById('methodId').value='addHabilitationCertificateDocumentFileEntry'; document.getElementById('over23CandidacyForm').submit();" href="#">+ <bean:message key="label.over23.add" bundle="CANDIDATE_RESOURCES"/></a></p>

		<p style="margin-bottom: 0.5em;"><bean:message key="label.extra.documents.recomendations.portfolios" bundle="CANDIDATE_RESOURCES"/>:</p>
		<logic:iterate id="document" name="individualCandidacyProcessBean" property="reportOrWorkDocumentList" indexId="index">
			<div class="mbottom05">
			<bean:define id="documentId" name="document" property="id"/>
			<fr:edit id="<%=  "individualCandidacyProcessBean.document.file.reports.works:" + index %>" 
				name="document"
				schema="PublicCandidacyProcessBean.documentUploadBean">
			  <fr:layout name="flow">
			    <fr:property name="labelExcluded" value="true"/>
			  </fr:layout>
			</fr:edit>
			<% if(index > 0) { %>
			<a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + index + "; document.getElementById(\"methodId\").value=\"removeReportsOrWorksDocumentFileEntry\"; document.getElementById(\"over23CandidacyForm\").submit();" %>' href="#" ><bean:message key="label.over23.remove" bundle="CANDIDATE_RESOURCES"/> </a>
			<% } %>
			</div>
		</logic:iterate>
		<p class="mtop05"><a onclick="document.getElementById('skipValidationId').value='true'; document.getElementById('methodId').value='addReportsOrWorksDocumentFileEntry'; document.getElementById('over23CandidacyForm').submit();" href="#">+ <bean:message key="label.over23.add" bundle="CANDIDATE_RESOURCES"/></a></p>
		
		<p style="margin-bottom: 0.5em;"><bean:message key="label.copy.document.id" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></p>
		<fr:edit id="individualCandidacyProcessBean.document.file.identity.card.copy"
			name="individualCandidacyProcessBean" 
			property="documentIdentificationDocument"
			schema="PublicCandidacyProcessBean.documentUploadBean">
		  <fr:layout name="flow">
		    <fr:property name="labelExcluded" value="true"/>
		  </fr:layout>
		</fr:edit>

		<p style="margin-bottom: 0.5em;"><bean:message key="label.vat.card.copy" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></p>
		<fr:edit id="individualCandidacyProcessBean.document.file.vat.card.copy"
			name="individualCandidacyProcessBean"
			property="vatCatCopyDocument" 
			schema="PublicCandidacyProcessBean.documentUploadBean">
		  <fr:layout name="flow">
		    <fr:property name="labelExcluded" value="true"/>
		  </fr:layout>
		</fr:edit>

		<p style="margin-bottom: 0.5em;"><bean:message key="label.handicap.proof.document.file" bundle="CANDIDATE_RESOURCES"/>:</p>
		<fr:edit id="individualCandidacyProcessBean.document.file.handicap.proof"
			name="individualCandidacyProcessBean"
			property="handicapProofDocument" 
			schema="PublicCandidacyProcessBean.documentUploadBean">
		  <fr:layout name="flow">
		    <fr:property name="labelExcluded" value="true"/>
		  </fr:layout>
		</fr:edit>

		<p style="margin-bottom: 0.5em;"><bean:message key="label.payment.report" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></p>
		<fr:edit id="individualCandidacyProcessBean.document.file.payment.report"
			name="individualCandidacyProcessBean"
			property="paymentDocument" 
			schema="PublicCandidacyProcessBean.documentUploadBean">
		  <fr:layout name="flow">
		    <fr:property name="labelExcluded" value="true"/>
		  </fr:layout>
		</fr:edit>


		<div class="mtop15 mbottom1">
			<label for="captcha"><bean:message key="label.captcha" bundle="ALUMNI_RESOURCES" />:</label>
			<div class="mbottom05">
				<img src="<%= request.getContextPath() + "/publico/jcaptcha.do" %>" style="border: 1px solid #bbb; padding: 5px;"/>
			</div>
			<span class="color777"><bean:message key="label.captcha.process" bundle="ALUMNI_RESOURCES" /></span><br/>
			<input type="text" name="j_captcha_response" size="30" style="margin-bottom: 1em;"/>
			
			<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES" property="captcha.error">
				<span class="error0"><bean:write name="message"/></span>
			</html:messages>
		</div>

		<h2><bean:message key="label.payment.title" bundle="CANDIDATE_RESOURCES"/></h2>
		<p><bean:message key="message.payment.details" bundle="CANDIDATE_RESOURCES"/></p> 		
		<p><em><bean:message key="message.ist.conditions.note" bundle="CANDIDATE_RESOURCES"/></em></p>		


		<div class="mtop15"><bean:message key="message.nape.contacts" bundle="CANDIDATE_RESOURCES"/></div>
		
		<p class="mtop15">		
			<html:submit onclick="document.getElementById('skipValidationId').value='false'; document.getElementById('methodId').value='submitCandidacy'; this.form.submit();"><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /> <bean:message key="label.application" bundle="CANDIDATE_RESOURCES"/></html:submit>
			<html:submit onclick="document.getElementById('skipValidationId').value='false'; document.getElementById('methodId').value='backCandidacyCreation'; this.form.submit();"><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:submit>
		</p>
</fr:form>


