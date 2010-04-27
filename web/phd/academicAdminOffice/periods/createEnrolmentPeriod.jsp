<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.presentationTier.Action.phd.ManageEnrolmentsBean"%>
<html:xhtml/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.create.enrolment.period" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="/phdIndividualProgramProcess.do?method=manageEnrolmentPeriods">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<fr:edit id="createBean" name="createBean" 
		 action="/phdIndividualProgramProcess.do?method=createEnrolmentPeriod">

	<fr:schema bundle="PHD_RESOURCES" type="<%= ManageEnrolmentsBean.class.getName() %>">

		<fr:slot name="startDate" required="true" />
		<fr:slot name="endDate" required="true" />

		<fr:slot name="degreeCurricularPlans" layout="option-select" required="true">
			<fr:property name="classes" value="nobullet noindent" />
		
			<fr:property name="providerClass" value="<%= ManageEnrolmentsBean.PhdDegreeCurricularPlansProvider.class.getName() %>" />

			<fr:property name="eachLayout" value="values" />
			<fr:property name="eachSchema" value="DegreeCurricularPlan.presentationName" />

			<fr:property name="sortBy" value="presentationName" />
			<fr:property name="selectAllShown" value="true" />
		</fr:slot>
	</fr:schema>

	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
		<fr:property name="requiredMarkShown" value="true" />
	</fr:layout>
	
	<fr:destination name="invalid" path="/phdIndividualProgramProcess.do?method=createEnrolmentPeriodInvalid"/>
	<fr:destination name="cancel" path="/phdIndividualProgramProcess.do?method=manageEnrolmentPeriods" />
	
</fr:edit>

</logic:present>