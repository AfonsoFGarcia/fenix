<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><fr:view name="unit" property="name"/></em>
<bean:define id="functionalityAction" value="departmentFunctionalities" toScope="request"/>
<bean:define id="module" value="departmentMember" toScope="request"/>

