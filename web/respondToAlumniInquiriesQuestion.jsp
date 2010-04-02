<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:html xhtml="true">
	<head>
		<title>
			<bean:message key="dot.title" bundle="GLOBAL_RESOURCES" /> - <bean:message key="message.inquiries.title" bundle="INQUIRIES_RESOURCES"/>
		</title>

		<link href="<%= request.getContextPath() %>/CSS/logdotist.css" rel="stylesheet" type="text/css" />

		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	</head>
	<body>
		<div id="container">
			<div id="dotist_id">
				<img alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />"
						src="<bean:message key="dot.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>" />
			</div>
			<div id="txt">
				<h1><bean:write name="cerimonyInquiryPerson" property="cerimonyInquiry.description"/></h1>
				<div class="mtop1">
					<bean:write name="cerimonyInquiryPerson" property="cerimonyInquiry.text" filter="false"/>
				</div>
			</div>
			<br />

			<div align="center">
				<fr:form action="/respondToAlumniInquiriesQuestion.do">
					<html:hidden property="method" value="registerAlumniResponseNow"/>
					<html:hidden property="contentContextPath_PATH" value="/comunicacao/comunicacao"/>
					<fr:edit id="cerimonyInquiryPerson" name="cerimonyInquiryPerson">
						<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.domain.alumni.CerimonyInquiryPerson">
							<fr:slot name="cerimonyInquiryAnswer" key="label.response" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
								<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.CerimonyInquiryAnswerProvider"/>
								<fr:property name="format" value="${text}" />
							</fr:slot>
						</fr:schema>
						<fr:layout name="tabular">
							<fr:property name="classes" value="form"/>
							<fr:property name="columnClasses" value=",,tderror"/>
						</fr:layout>
					</fr:edit>
					<fr:destination name="cancel" path="/respondToAlumniInquiriesQuestion.do?method=registerAlumniResponseRespondLater"/>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="inquiries.respond.now" property="ok">
						<bean:message key="button.inquiries.respond.now" />
					</html:submit>
				</fr:form>
				<br/>
				<form method="post" action="<%= request.getContextPath() %>/respondToAlumniInquiriesQuestion.do">
					<html:hidden property="method" value="registerAlumniResponseRespondLater"/>
					<html:hidden property="contentContextPath_PATH" value="/comunicacao/comunicacao"/>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="inquiries.respond.later" property="ok">
						<bean:message key="button.inquiries.respond.later" />
					</html:submit>
				</form>
			</div>

		</div>
	</body>
</html:html>
