<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.student.enrollment.improvment" /></h2>


<span class="error"><html:errors/></span>
<html:form action="/improvmentEnrollment" focus="studentNumber">
	<html:hidden property="method" value="start"/>
	<html:hidden property="page" value="1"/>
	<table>
		<tr>
			<td><bean:message key="label.choose.student" />&nbsp;</td>
			<td>
				<input type="text" name="studentNumber" size="5" maxlength="5"/>
			</td>
		</tr>
		<tr>
			<td><bean:message key="label.choose.year.execution"/>&nbsp;</td>
			<td>
				<html:select property="executionPeriod" >
					<html:optionsCollection name="executionPeriods"/>
				</html:select>
			</td>				
		</tr>
	</table>
	<br/>
	<br/>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.submit.student"/>
	</html:submit>
	<html:reset styleClass="inputbutton">
		<bean:message key="button.clean"/>
	</html:reset>
</html:form>