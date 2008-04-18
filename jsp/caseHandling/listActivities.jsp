<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<bean:define id="processName" name="processName"/>
<bean:define id="processId" name="process" property="idInternal"/>

<fr:view schema="caseHandling.list.activities" name="activities">
	<fr:layout name="tabular">
		<fr:property name="linkFormat(executeActivity)" value='<%= "/caseHandling" + processName.toString() + ".do?method=prepareExecute${id}&amp;processId=" + processId.toString()%>' />
		<fr:property name="key(executeActivity)" value="link.execute.activity"/>
		<fr:property name="bundle(executeActivity)" value="APPLICATION_RESOURCES"/>
	</fr:layout>	
</fr:view>