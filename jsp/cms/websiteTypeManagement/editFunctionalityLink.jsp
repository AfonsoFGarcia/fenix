<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h3><bean:message bundle="CMS_RESOURCES" key="cms.functionalityLinkManagement.label" /></h3>

<logic:present name="functionalityLink">
    <fr:edit action="/functionalityLinkManagement.do?method=start"
             name="functionalityLink" layout="tabular" schema="functionalityLink.edit"/>
</logic:present>
