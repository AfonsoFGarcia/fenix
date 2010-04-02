<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<h2>
	<bean:message bundle="APPLICATION_RESOURCES" key="label.publicRelationOffice.add.inquiry.people"/>
</h2>

<bean:define id="url">/alumniCerimony.do?method=addPeople&amp;cerimonyInquiryId=<bean:write name="cerimonyInquiry" property="externalId"/></bean:define>
<bean:define id="urlCancel">/alumniCerimony.do?method=viewInquiry&amp;cerimonyInquiryId=<bean:write name="cerimonyInquiry" property="externalId"/></bean:define>
<fr:edit id="usernameFileBean" name="usernameFileBean" action="<%= url %>">
	<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.publicRelationsOffice.UsernameFileBean">
		<fr:slot name="inputStream" key="label.file" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
			<fr:property name="fileNameSlot" value="filename"/>
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= urlCancel %>"/>
</fr:edit>
