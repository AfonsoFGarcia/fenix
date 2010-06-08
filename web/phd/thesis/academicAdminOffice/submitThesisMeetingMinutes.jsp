<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean"%>

<html:xhtml/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
<bean:define id="processId" name="process" property="externalId" />
<bean:define id="individualProcessId" name="process" property="individualProgramProcess.externalId" />


<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.thesis.submit.thesis.meeting.minutes" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&amp;processId=" + individualProcessId.toString() %>">
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


<logic:notEmpty name="process" property="juryMeetingMinutesDocument">
	<br/><br/>
	<strong><bean:message key="label.phd.thesis.schedule.meeting.minutes.previous.document" bundle="PHD_RESOURCES" /></strong>
	<fr:view name="process" property="juryMeetingMinutesDocument" layout="link" />
	<br/>
</logic:notEmpty>

<%--  ### End Of Context Information  ### --%>

<br/>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<fr:form action="<%= "/phdThesisProcess.do?processId=" + processId.toString() %>" encoding="multipart/form-data">
	<input type="hidden" name="method" />
	<fr:edit id="thesisBean" name="thesisBean" visible="false" />
	
	
	<strong><bean:message key="label.phd.thesis.submit.thesis.meeting.minutes" bundle="PHD_RESOURCES" /></strong>
	<fr:edit id="thesisBean" name="thesisBean">

		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdThesisProcessBean.class.getName() %>">
			<fr:slot name="toNotify" layout="radio">
				<fr:property name="classes" value="liinline nobullet"/>
			</fr:slot>
		</fr:schema>

		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:edit>
	
	<fr:edit id="thesisBean.edit.documents" name="thesisBean" property="documents">
	
		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdProgramDocumentUploadBean.class.getName() %>">
			<fr:slot name="type" readOnly="true" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean.type" layout="phd-enum-renderer" />
			<fr:slot name="file" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean.file">
				<fr:property name="fileNameSlot" value="filename"/>
				<fr:property name="size" value="20"/>
			</fr:slot>
		</fr:schema>
	
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
	</fr:edit>

<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='submitThesisMeetingMinutes';"><bean:message bundle="PHD_RESOURCES" key="label.submit"/></html:submit>
<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='viewIndividualProgramProcess';"><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>	
</fr:form>


<br/><br/>

<%--  ### End of Operation Area  ### --%>

<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>

</logic:present>