<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<h2><bean:message key="title.allProjectsAccesses" /></h2>
<br />
<logic:present name="projectsAccessesList" scope="request">
	<logic:notEmpty name="projectsAccessesList" scope="request">
		<table cellspacing="0">
			<tr>
				<td class="infoop"><span class="emphasis-box">Info</span></td>
				<td class="infoop"><bean:message key="message.projectAccess" /></td>
			</tr>
		</table>
		<br />
		<br />
		<table>
			<tr>
				<td class="listClasses-header"><bean:message key="label.username" /></td>
				<td class="listClasses-header"><bean:message key="label.name" /></td>
				<td class="listClasses-header"><bean:message key="label.projectNumber" /></td>
				<td class="listClasses-header"><bean:message key="label.acronym" /></td>
				<td class="listClasses-header"><bean:message key="label.beginDate" /></td>
				<td class="listClasses-header"><bean:message key="label.endDate" /></td>
			</tr>
			<logic:iterate id="projectAccess" name="projectsAccessesList">
				<bean:define id="person" name="projectAccess" property="infoPerson" />
				<bean:define id="username" name="person" property="username" />
				<bean:define id="personCode" name="person" property="idInternal" />
				<bean:define id="projectCode" name="projectAccess" property="keyProject" />
				<bean:define id="infoProject" name="projectAccess" property="infoProject" />
				<tr>
					<td td class="listClasses"><bean:write name="person" property="username" /></td>
					<td td class="listClasses"><bean:write name="person" property="nome" /></td>
					<td td class="listClasses"><bean:write name="infoProject" property="projectCode" /></td>
					<td td class="listClasses"><bean:write name="infoProject" property="title" /></td>
					<td td class="listClasses"><bean:write name="projectAccess" property="beginDateFormatted" /></td>
					<td td class="listClasses"><bean:write name="projectAccess" property="endDateFormatted" /></td>
					<td><html:link page="<%="/projectAccessEdition.do?method=prepareEditProjectAccess&amp;projectCode="+projectCode+"&amp;personCode="+personCode%>">Editar</html:link></td>
					<td><html:link page="<%="/projectAccess.do?method=removeProjectAccess&amp;projectCode="+projectCode+"&amp;username="+username%>">Remover</html:link></td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
	<logic:empty name="projectsAccessesList" scope="request">
		<span class="error"><bean:message key="message.noOtherUsers" /></span>
	</logic:empty>
	<br />
	<br />
</logic:present>
<logic:notPresent name="projectsAccessesList" scope="request">
	<span class="error"><bean:message key="message.noUserProjects" /></span>
</logic:notPresent>
