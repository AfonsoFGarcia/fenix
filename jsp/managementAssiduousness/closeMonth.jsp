<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.closeMonth" /></h2>

<logic:present name="yearMonthToExport">
	<h3><fr:view name="yearMonthToExport" schema="show.date">
		<fr:layout name="flow">
			<fr:property name="labelExcluded" value="true" />
		</fr:layout>
	</fr:view></h3>
	<p>
	<fr:form action="/monthClosure.do?method=exportClosedMonth">
		<fr:edit id="yearMonthToExport" name="yearMonthToExport" schema="show.date" visible="false"/>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible">
			<bean:message key="button.exportAplica" />
		</html:submit>
	</fr:form>
	<br/>
	<fr:form action="/monthClosure.do?method=exportClosedMonthList">
		<fr:edit id="yearMonthToExportList" name="yearMonthToExport" schema="show.date" visible="false"/>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.open" styleClass="invisible">
			<bean:message key="button.exportList" />
		</html:submit>
	</fr:form>
	<br/>
	<fr:form action="/monthClosure.do?method=openMonth">
		<fr:edit id="yearMonthToOpen" name="yearMonthToExport" schema="show.date" visible="false"/>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.open" styleClass="invisible">
			<bean:message key="button.openMonth" />
		</html:submit>
	</fr:form>
	</p>
</logic:present>

<logic:present name="yearMonth">
	<h3><fr:view name="yearMonth" schema="show.date">
		<fr:layout name="flow">
			<fr:property name="labelExcluded" value="true" />
		</fr:layout>
	</fr:view></h3>
	<logic:equal name="yearMonth" property="canCloseMonth" value="true">
		<fr:form action="/monthClosure.do?method=closeMonth">
			<fr:edit id="yearMonth" name="yearMonth" schema="show.date" visible="false"/>
			<bean:message key="message.closeMonthConfirmation" bundle="ASSIDUOUSNESS_RESOURCES"/>
			<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible">
				<bean:message key="button.confirm" />
			</html:submit></p>
		</fr:form>
	</logic:equal>
	<logic:notEqual name="yearMonth" property="canCloseMonth" value="true">
		<bean:message key="message.cantCloseMonth" bundle="ASSIDUOUSNESS_RESOURCES"/>
	</logic:notEqual>
</logic:present>