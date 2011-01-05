<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%@page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddStudyPlan"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.RequestPublicPresentationSeminarComission"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.RequestPublicThesisPresentation"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.ExemptPublicPresentationSeminarComission"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.ConfigurePhdIndividualProgramProcess" %>

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.viewProcess" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<%--
<div class="breadcumbs">
	<span class="actual">Step 1: Step Name</span> > 
	<span>Step N: Step name </span>
</div>
--%>

<logic:present name="backMethod">
	<bean:define id="backMethod" name="backMethod"/>
	<html:link action="<%="/phdIndividualProgramProcess.do?method=" + backMethod %>">
		� <bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
</logic:present>

<logic:notPresent name="backMethod">
	<html:link action="/phdIndividualProgramProcess.do?method=manageProcesses">
		� <bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
</logic:notPresent>

<br/><br/>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<logic:notEmpty name="process" property="person.personalPhotoEvenIfPending">
	<div style="float: right;">
		<bean:define id="photoCode" name="process" property="person.personalPhotoEvenIfPending.idInternal" />
		<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrievePendingByID&amp;photoCode=" + photoCode.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
	</div>
</logic:notEmpty>
 
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<table>
  <tr>
    <td>
		<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view" name="process">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
			</fr:layout>
		</fr:view>
	</td>
    <td style="vertical-align: top; padding-top: 1em;">
    	<bean:define id="process" name="process" />
		
    	<ul class="operations">
			<logic:equal name="process" property="activeState.active" value="true">
				<li>
					<html:link action="/phdIndividualProgramProcess.do?method=prepareEditPersonalInformation" paramId="processId" paramName="process" paramProperty="externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.phd.editPersonalInformation"/>
					</html:link>
				</li>
				<li>
					<html:link action="/phdIndividualProgramProcess.do?method=prepareUploadPhoto" paramId="processId" paramName="process" paramProperty="externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.edit.photo"/>
					</html:link>
				</li>
			</logic:equal>
			<phd:activityAvailable process="<%= process %>" activity="<%= AddStudyPlan.class %>">
				<li>
					<html:link action="/phdIndividualProgramProcess.do?method=manageStudyPlan" paramId="processId" paramName="process" paramProperty="externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.phd.studyPlanAndQualificationExams"/>
					</html:link>
				</li>
			</phd:activityAvailable>
			<logic:equal name="process" property="activeState.active" value="true">
				<li>
					<html:link action="/phdIndividualProgramProcess.do?method=prepareEditQualificationsAndJobsInformation" paramId="processId" paramName="process" paramProperty="externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.phd.editQualificationsAndJobsInformation"/>
					</html:link>
				</li>
				<li>
					<html:link action="/phdIndividualProgramProcess.do?method=prepareEditPhdIndividualProgramProcessInformation" paramId="processId" paramName="process" paramProperty="externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.phd.editPhdIndividualProgramProcessInformation"/>
					</html:link>
				</li>
				
				<br/>
				
				<li>
					<html:link action="/phdIndividualProgramProcess.do?method=prepareManageGuidingInformation" paramId="processId" paramName="process" paramProperty="externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.phd.manageGuidingInformation"/>
					</html:link>
				</li>
				<li>
					<html:link action="/phdIndividualProgramProcess.do?method=manageAlerts" paramId="processId" paramName="process" paramProperty="externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.phd.manageAlerts"/>
					</html:link>
				</li>
				<li>
					<jsp:include page="/phd/alertMessagesNotifier.jsp?global=false" />
				</li>
			</logic:equal>
			<li>
				<html:link action="/phdIndividualProgramProcess.do?method=managePhdIndividualProgramProcessState" paramId="processId" paramName="process" paramProperty="externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.manage.states"/>
				</html:link>
			</li>
			
			<br />
			
			<li>
				<html:link action="/phdAccountingEventsManagement.do?method=prepare" paramId="processId" paramName="process" paramProperty="externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.accounting.events.create"/>
				</html:link>
			</li>
			<li>
				<html:link action="/payments.do?method=showOperations" target="_blank" paramId="personId" paramName="process" paramProperty="person.idInternal">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.payments"/>
				</html:link>
			</li>
			
			<li>
				<html:link action="/phdAcademicServiceRequestManagement.do?method=listAcademicServiceRequests" paramId="phdIndividualProgramProcessId" paramName="process" paramProperty="externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.academic.service.requests" />
				</html:link>
			</li>
			
			<br/>
			
			<phd:activityAvailable process="<%= process %>" activity="<%= RequestPublicPresentationSeminarComission.class %>">
				<li>
					<html:link action="/phdIndividualProgramProcess.do?method=prepareRequestPublicPresentationSeminarComission" paramId="processId" paramName="process" paramProperty="externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.phd.request.public.presentation.seminar.comission"/>
					</html:link>
				</li>
			</phd:activityAvailable>
			<phd:activityAvailable process="<%= process %>" activity="<%= ExemptPublicPresentationSeminarComission.class %>">
				<li>
					<html:link action="/phdIndividualProgramProcess.do?method=prepareExemptPublicPresentationSeminarComission" paramId="processId" paramName="process" paramProperty="externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.phd.exempt.public.presentation.seminar.comission"/>
					</html:link>
				</li>
			</phd:activityAvailable>

		 
			<phd:activityAvailable process="<%= process %>" activity="<%= RequestPublicThesisPresentation.class %>">
			
				<li>
					<html:link action="/phdIndividualProgramProcess.do?method=prepareRequestPublicThesisPresentation" paramId="processId" paramName="process" paramProperty="externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.phd.request.public.thesis.presentation"/>
					</html:link>
				</li>
			
			</phd:activityAvailable>
			
			<%--<li>
				<html:link action="/phdIndividualProgramProcess.do?method=preparePhdEmailsManagement" paramId="processId" paramName="process" paramProperty="externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.manage.emails" />
				</html:link>
			</li> --%>

			<br/>
			
			<phd:activityAvailable process="<%= process %>" activity="<%= ConfigurePhdIndividualProgramProcess.class %>">
				<li>
					<html:link action="/phdIndividualProgramProcess.do?method=preparePhdConfigurationManagement" paramId="processId" paramName="process" paramProperty="externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.phd.manage.configuration" />
					</html:link>
				</li>
			</phd:activityAvailable>
		
		</ul>
    </td>
  </tr>
</table>

<logic:notEmpty name="process" property="student">
	<br/>
	<bean:define id="studentId" name="process" property="student.idInternal" />
	  
	<strong><bean:message  key="label.phd.student.information" bundle="PHD_RESOURCES"/></strong>
	<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view.student.information" name="process">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop10" />
		</fr:layout>
	</fr:view>
	<br/>
	<html:link page="<%= "/student.do?method=visualizeStudent&studentID=" + studentId %>">
		<bean:message key="label.phd.student.page" bundle="PHD_RESOURCES" />
	</html:link>
	<br />
</logic:notEmpty>

<%--Thesis --%>
<jsp:include page="viewThesisProcess.jsp" />

<%--CAT --%>
<jsp:include page="viewSeminarProcess.jsp" />

<%-- School part --%>
<jsp:include page="viewSchoolPart.jsp" />

<%--Candidacy --%>
<jsp:include page="viewCandidacyProcess.jsp" />




</logic:present>