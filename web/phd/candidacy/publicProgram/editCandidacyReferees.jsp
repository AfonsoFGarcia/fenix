<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<a href="http://www.ist.utl.pt/en/html/ist-epfl/">IST-EPFL</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<fr:form id="editCandidacyRefereeForm" action="/candidacies/phdProgramCandidacyProcess.do">
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
	
	<input type="hidden" id="methodId" name="method" value="" />
	<input type="hidden" id="removeIndexId" name="removeIndex" value=""/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>	
	
	<a href="#" onclick="javascript:document.getElementById('skipValidationId').value='true';javascript:document.getElementById('methodId').value='backToViewCandidacy';document.getElementById('editCandidacyRefereeForm').submit();">� <bean:message bundle="PHD_RESOURCES" key="label.back"/></a>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<logic:equal name="canEditCandidacy" value="true">

	<%--  ### Error Messages  ### --%>
	<jsp:include page="/phd/errorsAndMessages.jsp" />
	<%--  ### End of Error Messages  ### --%>
	
	<logic:notPresent name="candidacyBean">
		<em><bean:message key="label.php.public.candidacy.hash.not.found" bundle="PHD_RESOURCES"/></em>
	</logic:notPresent>
	
	<logic:present name="candidacyBean">
	
		<h2 style="margin-top: 1em;"><bean:message key="title.public.phd.reference.letters.authors" bundle="PHD_RESOURCES"/></h2>	
		
		<p><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"methodId\").value=\"prepareAddCandidacyRefereeToExistingCandidacy\"; document.getElementById(\"editCandidacyRefereeForm\").submit();" %>' href="#" >+ <bean:message key="label.add" bundle="PHD_RESOURCES"/></a></p>
		
		<logic:notEmpty name="refereeBean">
			<p class="mtop15"><span><bean:message key="message.fields.required" bundle="CANDIDATE_RESOURCES"/></span></p>
	
			<fr:edit id="refereeBean" name="refereeBean" schema="Public.PhdProgramCandidacyProcess.referee">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="thlight thleft"/>
					<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
					<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
				<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=editCandidacyRefereesInvalid" />
			</fr:edit>	
			<html:submit onclick="document.getElementById('methodId').value='addCandidacyRefereeToExistingCandidacy';" bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.add"/></html:submit>
			<br/>
			<br/>
		</logic:notEmpty>
		
		
		<logic:notEmpty name="candidacyBean" property="candidacyHashCode.individualProgramProcess.phdCandidacyReferees">
			<logic:iterate id="candidacyReferee" name="candidacyBean" property="candidacyHashCode.individualProgramProcess.phdCandidacyReferees" indexId="index" >
				<strong><%= index.intValue() + 1 %>.</strong>
				<bean:define id="candidacyRefereeId" name="candidacyReferee" property="externalId" />
				<fr:view name="candidacyReferee" schema="PhdCandidacyReferee.view">
					<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft"/>
				        <fr:property name="columnClasses" value="width175px,,,,"/>
					</fr:layout>
				</fr:view>
				<logic:equal name="candidacyReferee" property="letterAvailable" value="false">
					<p class="mtop05"><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + candidacyRefereeId + "; document.getElementById(\"methodId\").value=\"sendCandidacyRefereeEmail\"; document.getElementById(\"editCandidacyRefereeForm\").submit();" %>' href="#" ><bean:message key="label.resend.email" bundle="PHD_RESOURCES"/></a></p>
				</logic:equal>
			</logic:iterate>
		</logic:notEmpty>
		 
	</logic:present>
</logic:equal>
</fr:form>
