<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<em><bean:message key="label.facultyAdmOffice.portal.name"/></em>
<h2><bean:message key="label.grant.costcenter.edition"/></h2>


<html:form action="/editGrantCostCenter" style="display:inline">

	<%-- Presenting errors --%>
	<logic:messagesPresent>
		<html:errors/>
	</logic:messagesPresent>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="doEdit"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<%-- grant cost center --%>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>

	<table class="tstyle5">
		<tr>
			<td>
				<bean:message key="label.grant.costcenter.number"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.number" property="number"/> <bean:message key="label.requiredfield"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.grant.costcenter.designation"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.designation" property="designation" size="60"/> <bean:message key="label.requiredfield"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.grant.costcenter.responsibleTeacher.number"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.responsibleTeacherNumber" property="responsibleTeacherNumber" size="10"/> <bean:message key="label.requiredfield"/>&nbsp;
				<html:link page='<%= "/showTeachersList.do?method=showForm" %>' target="_blank">
					<bean:message key="link.teacher.showList"/>
				</html:link>
			</td>
		</tr>
	</table>



	<table>
		<tr>
			<td>
				<%-- Save button (edit/create) --%>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="button.save"/>
				</html:submit>
</html:form>
			</td>
			<td>
				<html:form action="/manageGrantCostCenter" style="display:inline">
					<%-- button cancel --%>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareManageGrantCostCenter"/>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" style="display:inline">
						<bean:message key="button.cancel"/>
					</html:submit>
				</html:form>
			</td>
		</tr>
	</table>