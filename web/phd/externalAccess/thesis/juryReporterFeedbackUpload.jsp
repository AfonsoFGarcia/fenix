<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.domain.phd.access.PhdProcessAccessType"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.access.PhdExternalOperationBean"%>
<%@page import="pt.ist.fenixWebFramework.renderers.validators.EmailValidator"%>

<%@page import="pt.ist.fenixWebFramework.renderers.validators.FileValidator"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.PhdThesisReportFeedbackDocument"%><html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<bean:message key="label.phds" bundle="PHD_RESOURCES"/>
</div>
<br/>

<bean:define id="accessTypeName"><%= PhdProcessAccessType.JURY_REPORTER_FEEDBACK_UPLOAD.getLocalizedName() %></bean:define>
<h2><bean:write name="accessTypeName" /></h2>

<br/>
<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="/phdExternalAccess.do?method=prepare" paramId="hash" paramName="participant" paramProperty="accessHashCode">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=operationBean" />
<%--  ### End of Error Messages  ### --%>

<logic:notEmpty name="participant">
	
	<%-- Process details --%>
	<jsp:include page="processDetails.jsp" />
	
	<logic:notEmpty name="lastReportFeedbackDocument">
		<br/>
		<strong><bean:message key="label.phd.jury.report.feedback.document.previous" bundle="PHD_RESOURCES" /></strong>
		<fr:view name="lastReportFeedbackDocument">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4 thlight mtop15" />
				<fr:property name="columnClasses" value="acenter width12em, width30em, thlight" />
			</fr:layout>
			
			<fr:schema bundle="PHD_RESOURCES" type="<%= PhdThesisReportFeedbackDocument.class.getName() %>">
				<fr:slot name="uploadTime" />
				<fr:slot name="filename" />
				<fr:slot name="documentVersion"/>
			</fr:schema>
			
		</fr:view>
		<br/>
	</logic:notEmpty>	
	
	<logic:equal name="canUploadFeedbackDocument" value="true">

		<fr:form action="/phdExternalAccess.do?method=juryReporterFeedbackUpload" encoding="multipart/form-data">
		
			<fr:edit id="operationBean" name="operationBean">
				<fr:schema bundle="PHD_RESOURCES" type="<%= PhdExternalOperationBean.class.getName() %>">
				
					<fr:slot name="email" required="true" validator="<%= EmailValidator.class.getName() %>">
						<fr:property name="size" value="40" />
					</fr:slot>
					<fr:slot name="password" required="true" layout="password">
						<fr:property name="size" value="40" />
					</fr:slot>
					
					<fr:slot name="documentBean.file" required="true">
						<fr:validator name="<%= FileValidator.class.getName() %>">
							<fr:property name="maxSize" value="100mb" />
							<fr:property name="acceptedExtensions" value="pdf" />
						</fr:validator>
						<fr:property name="fileNameSlot" value="documentBean.filename"/>
						<fr:property name="size" value="20"/>
					</fr:slot>
					
				</fr:schema>
			
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
					<fr:property name="columnClasses" value=",,tdclear tderror1" />
				</fr:layout>
				
				<fr:destination name="invalid" path="/phdExternalAccess.do?method=prepareJuryReporterFeedbackUploadInvalid"/>
			</fr:edit>
			
			<html:submit><bean:message key="label.submit" /></html:submit>
		</fr:form>
	</logic:equal>
	
</logic:notEmpty>


