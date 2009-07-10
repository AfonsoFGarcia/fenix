<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<a href="http://www.ist.utl.pt/en/about-IST/global-cooperation/IST-EPFL/">IST-EPFL</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<fr:form id="editGuidingsForm" action="/candidacies/phdProgramCandidacyProcess.do">
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
	<input type="hidden" id="methodId" name="method" value="" />
	<input type="hidden" id="removeIndexId" name="removeIndex" value=""/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>	
	
	<a href="#" onclick="javascript:document.getElementById('skipValidationId').value='true';javascript:document.getElementById('methodId').value='backToViewCandidacy';document.getElementById('editGuidingsForm').submit();">� <bean:message bundle="PHD_RESOURCES" key="label.back"/></a>
	<br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<logic:equal name="canEditCandidacy" value="true">

	<%--  ### Error Messages  ### --%>
	<jsp:include page="/phd/errorsAndMessages.jsp" />
	<%--  ### End of Error Messages  ### --%>
	
	<logic:notPresent name="candidacyBean">
		<em><bean:message key="label.php.public.candidacy.hash.not.found" bundle="PHD_RESOURCES"/></em>
	</logic:notPresent>
	
	<logic:present name="candidacyBean">
		<h2 style="margin-top: 1em;"><bean:message key="title.public.phd.guidings" bundle="PHD_RESOURCES"/></h2>
		
		<logic:notEmpty name="candidacyBean" property="guidings">
		
			<div class="fs_form">
			<p class="mtop15"><span><bean:message key="message.mandatory.fields" bundle="PHD_RESOURCES"/></span></p>
			<fieldset style="display: block;">
			
				<logic:iterate id="guidingBean" name="candidacyBean" property="guidings" indexId="index">
					<strong><%= index.intValue() + 1 %>.</strong>
					<bean:define id="guidingId" name="index" />
					<fr:edit id="guidingBean" name="guidingBean" schema="Public.PhdProgramGuidingBean.edit">
						<fr:layout name="tabular-editable">
							<fr:property name="classes" value="thlight thleft"/>
							<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
							<fr:property name="requiredMarkShown" value="true" />
						</fr:layout>
						<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=editCandidacyGuidingsInvalid" />
						<fr:destination name="cancel" path="/candidacies/phdProgramCandidacyProcess.do?method=prepareEditCandidacyGuidings" />
					</fr:edit>
					<p class="mtop05"><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + guidingId + "; document.getElementById(\"methodId\").value=\"removeGuidingFromCreationList\"; document.getElementById(\"editGuidingsForm\").submit();" %>' href="#" >- <bean:message key="label.remove" bundle="PHD_RESOURCES"/></a></p>
				</logic:iterate>
			
			</fieldset>
			</div>
			<p><html:submit onclick="document.getElementById('methodId').value='addGuidingToExistingCandidacy';" bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.add"/></html:submit></p>
		</logic:notEmpty>
		
		<logic:notEmpty name="candidacyBean" property="candidacyHashCode.individualProgramProcess.guidings">
			<logic:iterate id="guiding" name="candidacyBean" property="candidacyHashCode.individualProgramProcess.guidings" indexId="index" >
				<strong><%= index.intValue() + 1 %>.</strong>
				<bean:define id="guidingId" name="guiding" property="externalId" />
				<fr:view name="guiding" schema="Public.PhdProgramGuiding.view">
					<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft"/>
				        <fr:property name="columnClasses" value="width175px,,,,"/>
					</fr:layout>
				</fr:view>
				<p class="mtop05"><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + guidingId + "; document.getElementById(\"methodId\").value=\"removeGuidingFromExistingCandidacy\"; document.getElementById(\"editGuidingsForm\").submit();" %>' href="#" >- <bean:message key="label.remove" bundle="PHD_RESOURCES"/></a></p>
			</logic:iterate>
		</logic:notEmpty>
		<p><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"methodId\").value=\"prepareAddGuidingToExistingCandidacy\"; document.getElementById(\"editGuidingsForm\").submit();" %>' href="#" >+ <bean:message key="label.add" bundle="PHD_RESOURCES"/></a></p>
			
	</logic:present>
</logic:equal>

</fr:form>
