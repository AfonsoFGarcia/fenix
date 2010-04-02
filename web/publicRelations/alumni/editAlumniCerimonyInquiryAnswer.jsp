<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<h2>
	<bean:message key="label.publicRelationOffice.editAlumniCerimonyInquiryAnswer" bundle="APPLICATION_RESOURCES"/>
</h2>

<bean:define id="url">/alumniCerimony.do?method=viewInquiry&amp;cerimonyInquiryId=<bean:write name="cerimonyInquiryAnswer" property="cerimonyInquiry.externalId"/></bean:define>
<fr:edit id="cerimonyInquiryAnswer" name="cerimonyInquiryAnswer" action="<%= url %>">
	<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.domain.alumni.CerimonyInquiry">
		<fr:slot name="text" key="label.publicRelationOffice.alumniCerimonyInquiry.answer"/>
	</fr:schema>
</fr:edit>

