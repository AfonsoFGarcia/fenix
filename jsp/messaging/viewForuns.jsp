<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


asdasdasdasdsadsda
<h3><bean:message key="label.viewForuns.title" /></h3>

<logic:present name="foruns">
	<fr:view name="foruns" layout="tabular" schema="messaging.viewForuns.forum">
		<fr:layout>
		    <fr:property name="classes" value="style1"/>
      		<fr:property name="columnClasses" value="listClasses,"/>
			<fr:property name="link(view)" value="/forunsManagement.do?method=viewForum"/>
			<fr:property name="param(view)" value="idInternal/forumId"/>
			<fr:property name="key(view)" value="messaging.viewForum.link"/>
		</fr:layout>
	</fr:view>
</logic:present>