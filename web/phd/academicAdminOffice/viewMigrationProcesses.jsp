<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>


<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.manage.emails.create" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<bean:define id="migrationProcesses" name="migrationProcesses" />

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<p>
	<html:link action="/phdIndividualProgramProcess.do?method=manageProcesses">
		� <bean:message key="label.back" bundle="PHD_RESOURCES" />
	</html:link>
</p>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%-- ### Operational Area ### --%>

<logic:empty name="migrationProcesses">
	N�o h� processos de migra��o
</logic:empty> 

<logic:notEmpty name="migrationProcesses">

	<bean:size id="processesCount" name="migrationProcesses"/>
	<bean:message  key="label.phd.process.count" bundle="PHD_RESOURCES" arg0="<%= processesCount.toString() %>"/>
	
	<logic:iterate id="migrationProcess" name="migrationProcesses">
		<fr:view name="migrationProcess" schema="PhdMigrationProcess">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop1" />
			</fr:layout>
		</fr:view>
	</logic:iterate>
	
</logic:notEmpty>

</logic:present>