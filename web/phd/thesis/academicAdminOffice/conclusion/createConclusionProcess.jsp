<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<bean:define id="processId" name="process" property="externalId" />

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="title.phd.conclusionProcesses" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="/phdThesisProcess.do?method=listConclusionProcesses" paramId="processId" paramName="processId">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view" name="process" property="individualProgramProcess">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>

<%--  ### End Of Context Information  ### --%>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<fr:form action="<%= "/phdThesisProcess.do?method=createConclusionProcess&amp;processId=" + processId %>">
	<fr:edit id="phdConclusionProcessBean" name="phdConclusionProcessBean" visible="false" />
	
	<logic:empty name="phdProgramInformation">
		<div class="warning">
			<bean:message bundle="PHD_RESOURCES" key="message.phdConclusionProcess.phdProgramInformation.for.conclusion.date.inexistent" />
		</div>
	</logic:empty>
	
	<logic:notEmpty name="phdProgramInformation">
		<p><strong><bean:message key="message.phdConclusionProcess.phdProgramInformation.for.this.conclusion.date" bundle="PHD_RESOURCES" /></strong></p>
		
		<fr:view name="phdProgramInformation">
			<fr:schema type="net.sourceforge.fenixedu.domain.phd.PhdProgramInformation" bundle="PHD_RESOURCES">
				<fr:slot name="beginDate" />
				<fr:slot name="minThesisEctsCredits" />
				<fr:slot name="maxThesisEctsCredits" />
				<fr:slot name="minStudyPlanEctsCredits" />
				<fr:slot name="maxStudyPlanEctsCredits" />
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>		
		</fr:view>
	</logic:notEmpty>
	
	<fr:edit id="phdConclusionProcessBean.edit" name="phdConclusionProcessBean" >
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.conclusion.PhdConclusionProcessBean">
			<fr:slot name="conclusionDate" required="true" />
			<fr:slot name="grade" required="true" />
			<fr:slot name="thesisEctsCredits" required="true" >
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DoubleValidator" />
			</fr:slot>
			
		<logic:notEqual name="process" property="individualProgramProcess.candidacyProcess.studyPlanExempted" value="true">
			<fr:slot name="studyPlanEctsCredits" required="true">
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DoubleValidator" />
			</fr:slot>
		</logic:notEqual>
		
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
		<fr:destination name="invalid" path="<%= "/phdThesisProcess.do?method=createConclusionProcessInvalid&amp;processId=" + processId %>" />
		<fr:destination name="cancel" path="<%= "/phdThesisProcess.do?method=listConclusionProcesses&amp;processId=" + processId %>" />
	</fr:edit>
	
	<html:submit><bean:message key="label.submit" bundle="PHD_RESOURCES" /></html:submit>
	<html:cancel><bean:message key="label.cancel" bundle="PHD_RESOURCES" /> </html:cancel>	
</fr:form>

</logic:present>
