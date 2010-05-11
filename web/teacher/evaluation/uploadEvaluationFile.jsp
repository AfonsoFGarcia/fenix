<%@ page language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<h2><bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.upload.title" /></h2>

<logic:present name="fileUploadBean">
	<bean:define id="urlUploadInvalid">/teacherEvaluation.do?method=<bean:write name="backAction" />&amp;evalueeOID=<bean:write
			name="fileUploadBean" property="teacherEvaluationProcess.evaluee.externalId" />
	</bean:define>
	<bean:define id="type" name="fileUploadBean" property="teacherEvaluationFileType.name" type="java.lang.String" />
	<bean:define id="backAction" name="backAction" type="java.lang.String" />
	<fr:edit id="fileUploadBean" name="fileUploadBean"
		action="<%= "/teacherEvaluation.do?method=uploadEvaluationFile&amp;backAction=" + backAction %>">
		<fr:schema bundle="ENUMERATION_RESOURCES" type="net.sourceforge.fenixedu.domain.teacher.evaluation.FileUploadBean">
			<fr:slot name="inputStream" key="<%= type %>"
				validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
				<fr:property name="fileNameSlot" value="filename" />
				<fr:property name="size" value="30" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thmiddle mtop05" />
			<fr:property name="columnClasses" value=",,tderror1 tdclear" />
		</fr:layout>
		<fr:destination name="cancel" path="<%= urlUploadInvalid %>" />
		<fr:destination name="invalid" path="<%= urlUploadInvalid %>" />
	</fr:edit>
</logic:present>
