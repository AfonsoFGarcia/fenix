<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>


<logic:present name="registrationConclusionBean">
	<strong><bean:message  key="label.phd.studyPlan" bundle="PHD_RESOURCES"/></strong>
	<br/>
	<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view.registration.conclusion.bean" name="registrationConclusionBean">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop10" />
		</fr:layout>
	</fr:view>
	<br/>
</logic:present>

<logic:notPresent name="registrationConclusionBean">
	<logic:notEmpty name="process" property="studyPlan">
		<strong><bean:message  key="label.phd.studyPlan" bundle="PHD_RESOURCES"/></strong>
		<br/>
		<fr:view schema="PhdStudyPlan.description" name="process" property="studyPlan">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop10" />
			</fr:layout>
		</fr:view>
		<br />
	</logic:notEmpty>
</logic:notPresent>

