<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<h2><bean:message key="label.manager.edit.competenceCourse" /></h2>

<br>

<span class="error"><html:errors/></span>

<html:form action="/createEditCompetenceCourse">  
	<html:hidden property="method" value="edit"/>
	<html:hidden property="competenceCourseID"/>	
	<table>
		<tr>
			<td>
				<bean:message key="message.manager.degree.curricular.plan.name"/>
			</td>
			<td>
				<html:text size="60" property="name" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.manager.curricularCourse.code"/>
			</td>
			<td>
				<html:text size="12" property="code" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.manager.department"/>
			</td>
			<td>
				<logic:iterate id="department" name="departments">
					<html:multibox property="departmentIDs" >
						<bean:write name="department" property="idInternal"/>
					</html:multibox>
					<bean:write name="department" property="name"/><br/>
				</logic:iterate>
			</td>
		</tr>
	</table>
	
	<br>
	
	<html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
	<html:reset  styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>
</html:form>