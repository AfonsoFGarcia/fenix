<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.candidacy.academicAdminOffice.manageCandidacyDocuments" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<bean:define id="individualProgramProcessId" name="process" property="individualProgramProcess.externalId" />

<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + individualProgramProcessId.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<table>
  <tr style="vertical-align: top;">
    <td style="width: 50%">
    	<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
		<fr:view schema="PhdIndividualProgramProcess.view" name="process" property="individualProgramProcess">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
			</fr:layout>
		</fr:view>
	</td>
    <td>
	    <strong><bean:message  key="label.phd.candidacyProcess" bundle="PHD_RESOURCES"/></strong>
		<fr:view schema="PhdProgramCandidacyProcess.view" name="process">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
			</fr:layout>
		</fr:view>
    </td>
  </tr>
</table>





<%--  ### End Of Context Information  ### --%>

<bean:define id="processId" name="process" property="externalId" />
<br/>

<%--  ### Documents  ### --%>
<strong><bean:message  key="label.phd.documents" bundle="PHD_RESOURCES"/></strong>
<logic:empty name="process" property="documents">
	<br/>
	<bean:message  key="label.phd.noDocuments" bundle="PHD_RESOURCES"/>
</logic:empty>
<logic:notEmpty name="process" property="documents">
	<fr:view schema="PhdProgramCandidacyProcessDocument.view" name="process" property="documents">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
			
			<fr:property name="linkFormat(view)" value="${downloadUrl}"/>
			<fr:property name="key(view)" value="label.view"/>
			<fr:property name="bundle(view)" value="PHD_RESOURCES"/>
			<fr:property name="order(view)" value="0"/>
			
			<fr:property name="linkFormat(delete)" value="/phdProgramCandidacyProcess.do?method=deleteDocument&documentId=${externalId}&processId=${phdCandidacyProcess.externalId}"/>
			<fr:property name="key(delete)" value="label.delete"/>
			<fr:property name="bundle(delete)" value="PHD_RESOURCES"/>
			<fr:property name="confirmationKey(delete)" value="message.confirm.document.delete" />
			<fr:property name="confirmationBundle(delete)" value="PHD_RESOURCES" />
			<fr:property name="order(delete)" value="1" />
			
			<fr:property name="sortBy" value="documentType=asc" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<%--  ### End Of Documents  ### --%>
<br/><br/>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>
<fr:form action="/phdProgramCandidacyProcess.do?method=uploadDocuments" encoding="multipart/form-data">
  	<input type="hidden" name="method" value="" />
  	<input type="hidden" name="processId" value="<%= processId.toString()  %>" />
  	
  	<strong><bean:message  key="label.phd.add.documents" bundle="PHD_RESOURCES"/></strong>
	<fr:edit id="documentsToUpload"
		name="documentsToUpload"
		schema="PhdCandidacyDocumentUploadBean.edit"
		action="/phdProgramCandidacyProcess.do?method=uploadDocuments">
	
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
			<fr:destination name="invalid" path="/phdProgramCandidacyProcess.do?method=uploadDocumentsInvalid" />
		</fr:layout>
	</fr:edit>
	
<%--  ### Buttons (e.g. Submit)  ### --%>
  <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='uploadDocuments';"><bean:message bundle="APPLICATION_RESOURCES" key="label.add"/></html:submit>
<%--  ### End of Buttons (e.g. Submit)  ### --%>

</fr:form>

<br/><br/>


<%--  ### End of Operation Area  ### --%>


</logic:present>
