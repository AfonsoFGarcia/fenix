<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.edit.branch" /></h2>

<span class="error"><html:errors/></span>

<html:form action="/manageBranches" method="get">
	<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
	<html:hidden property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden property="branchId" value="<%= request.getParameter("branchId") %>"/>
	<html:hidden property="method" value="edit" /> 
	<html:hidden property="page" value="1"/>
	<table>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="label.name"/>
			</td>
			<td>
				<html:text size="60" property="name" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="label.nameEn"/>
			</td>
			<td>
				<html:text size="60" property="nameEn" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="label.manager.code"/>
			</td>
			<td>
				<html:text size="10" property="code" />
			</td>
		</tr>			
	</table>
	
	<br>

	<html:submit styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
	<html:reset  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>			
</html:form>