<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<bean:define id="reportType" value="<%=request.getAttribute("reportType").toString()%>" />
<h2><bean:message key="title.projects" />&nbsp;-&nbsp;<bean:message key="<%="title."+reportType%>" /></h2>
<br />
<br />
<logic:present name="coordinatorsList">
	<logic:notEmpty name="coordinatorsList">
		<table>
			<tr>
				<td class="listClasses-header"><bean:message key="label.mecanographicNumber" /></td>
				<td class="listClasses-header"><bean:message key="label.name" /></td>
			</tr>
			<logic:iterate id="coordinator" name="coordinatorsList">
				<bean:define id="coordinatorCode" name="coordinator" property="code" />
				<tr>
					<td class="listClasses"><html:link
						page="<%="/projectReport.do?method=getReport&amp;reportType="+reportType+"&amp;coordinatorCode="+coordinatorCode%>">
						<bean:write name="coordinator" property="code" />
					</html:link></td>
					<td class="listClasses"><html:link
						page="<%="/projectReport.do?method=getReport&amp;reportType="+reportType+"&amp;coordinatorCode="+coordinatorCode%>">
						<bean:write name="coordinator" property="description" />
					</html:link></td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
	<logic:empty name="coordinatorsList">
		<span class="error"><bean:message key="message.noUserProjects" /></span>
	</logic:empty>
</logic:present>
