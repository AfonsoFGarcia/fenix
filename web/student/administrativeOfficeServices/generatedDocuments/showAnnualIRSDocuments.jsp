<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<logic:present role="STUDENT">
	<h2><strong><bean:message key="label.documents.anualIRS" bundle="STUDENT_RESOURCES"/></strong></h2>
		
	<logic:empty name="annualIRSDocuments">
		<bean:message  key="label.documents.noDocumentFound" bundle="STUDENT_RESOURCES" />
	</logic:empty>
	<logic:notEmpty name="annualIRSDocuments">	
		<fr:view name="annualIRSDocuments" schema="AnnualIRSDeclarationDocument.view">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle2 thlight thcenter mtop05" />
						<fr:property name="linkFormat(download)" value="${downloadUrl}"/>
						<fr:property name="key(download)" value="label.download"/>
						<fr:property name="contextRelative(download)" value="false" />
						<fr:property name="bundle(download)" value="STUDENT_RESOURCES" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	
</logic:present>


