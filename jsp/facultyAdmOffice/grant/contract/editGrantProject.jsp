<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<strong><p align="center"><bean:message key="label.grant.project.edition"/></p></strong><br/>

<html:form action="/editGrantProject" style="display:inline">

	<%-- Presenting errors --%>
	<logic:messagesPresent>
	<span class="error">
		<html:errors/>
	</span><br/>
	</logic:messagesPresent>

	<html:hidden property="method" value="doEdit"/>
	<html:hidden property="page" value="1"/>

	<%-- grant project --%>
	<html:hidden property="idInternal"/>
	<html:hidden property="ojbConcreteClass"/>

	<table>
		<tr>
			<td align="left">
				<bean:message key="label.grant.project.number"/>:&nbsp;
			</td>
			<td>
				<html:text property="number"/><bean:message key="label.requiredfield"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.project.designation"/>:&nbsp;
			</td>
			<td>
				<html:text property="designation"/><bean:message key="label.requiredfield"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.project.responsibleTeacher.number"/>:&nbsp;
			</td>
			<td>
				<html:text property="responsibleTeacherNumber"/><bean:message key="label.requiredfield"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.project.grantCostCenter.number"/>:&nbsp;
			</td>
			<td>
				<html:text property="grantCostCenterNumber"/><bean:message key="label.requiredfield"/>
			</td>
		</tr>
		
	</table>

	<br/>

	<table>
		<tr>
			<td>
				<%-- Save button (edit/create) --%>
				<html:submit styleClass="inputbutton">
					<bean:message key="button.save"/>
				</html:submit>
</html:form>
			</td>
			<td>
				<html:form action="/manageGrantProject" style="display:inline">
					<%-- button cancel --%>
					<html:hidden property="method" value="prepareManageGrantProject"/>
					<html:submit styleClass="inputbutton" style="display:inline">
						<bean:message key="button.cancel"/>
					</html:submit>
				</html:form>
			</td>
		</tr>
	</table>