<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str" %>
<html:xhtml/>

<br/>
<h2><strong><bean:message key="link.create.external.person" bundle="MANAGER_RESOURCES"/></strong></h2>
<br/>
<h2><strong><bean:message key="label.login.info" bundle="MANAGER_RESOURCES" /></strong></h2>
<logic:iterate id="identification" name="person" property="user.identifications">
	<fr:view name="identification" schema="net.sourceforge.fenixedu.domain.Login" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:view>
<br/>
</logic:iterate>
<br/>
<h2><strong><bean:message key="label.person.title.personal.info" bundle="MANAGER_RESOURCES" /></strong></h2>
<fr:view name="person" schema="net.sourceforge.fenixedu.domain.Person" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>
