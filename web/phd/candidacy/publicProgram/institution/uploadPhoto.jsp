<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<script type="text/javascript">
	function clearFileInputs() {
		var tags = document.getElementsByTagName("input");
		for(i=0; i<tags.length; i++) {
			var tag = tags[i];
			if (tag.type == "file") {
				tag.parentNode.innerHTML = tag.parentNode.innerHTML;
			}
		}       
	}
</script>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<jsp:include page="/phd/candidacy/publicProgram/institution/commonBreadcumbs.jsp" />

	<bean:message key="title.edit.candidacy.upload.documents" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.institution.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="hash" name="process" property="candidacyHashCode.value" />
<p>	
	<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=viewApplication" paramId="hash" paramName="hash" >
		� <bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
</p>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--
  CHECK: has candidacy period? 
<logic:equal value="true" name="isApplicationSubmissionPeriodValid">
--%>
<fr:form id="uploadPhotoForm" action="<%= "/applications/phd/phdProgramApplicationProcess.do?method=uploadPhoto&processId=" + processId %>" encoding="multipart/form-data">
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />

<p class="mtop15">
	<em><bean:message key="message.mandatory.fields" bundle="PHD_RESOURCES"/></em><br/>
	<em><bean:message key="message.max.photo.file.size" bundle="PHD_RESOURCES"/></em>
</p>

<logic:notPresent name="candidacyBean">
	<em><bean:message key="label.php.public.candidacy.hash.not.found" bundle="PHD_RESOURCES"/></em>
</logic:notPresent>

<logic:present name="candidacyBean">
		<div class="fs_form">
		<fieldset style="display: block;">
			<fr:edit id="uploadPhotoBean" name="uploadPhotoBean" schema="PhotographUploadBean.upload">		
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="thlight thleft"/>
					<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
					<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
				<fr:destination name="invalid" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=uploadPhotoInvalid&processId=" + processId %>" />
			</fr:edit>
		</fieldset>
		</div>	
		<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.add"/></html:submit></p>
</logic:present>
</fr:form>
