<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>

<span class="error"><html:errors/></span>

<bean:write name="executionPeriodName"/>
<logic:present name="executionDegreeName">
	<logic:notEmpty name="executionDegreeName">
		> <bean:write name="executionDegreeName"/>
	</logic:notEmpty>
</logic:present>	
> <bean:write name="executionCourseName"/>
<html:form action="/editExecutionCourseManageCurricularCourses">
	<input type="hidden" name="method" value="prepareAssociateCurricularCourse"/>
	<html:hidden property="executionCourseId" value="<%= pageContext.findAttribute("executionCourseId").toString() %>" />
	<html:hidden property="executionCourseName" value="<%= pageContext.findAttribute("executionCourseName").toString() %>" />
	<html:hidden property="executionPeriod"/>
	<html:hidden property="executionDegree"/>
	<html:hidden property="curYear"/>				
	<html:hidden property="executionCoursesNotLinked"/>
	
	<b><bean:message key="link.manager.executionCourseManagement.associate"/></b>
	<p class="infoop">
		<bean:message key="message.manager.executionCourseManagement.chooseDegree"/>
	</p>
	<table>
		<tr>
			<td style="text-align:right">
				<bean:message key="property.context.degree"/>:
			</td>
			<td>
				<html:select property="degreeCurricularPlan" size="1">
					<html:options collection="<%=SessionConstants.DEGREES%>" property="value" labelProperty="label"/>
				</html:select>
				<br />
			</td>
		</tr>
	</table>
	<br />
	<html:submit styleClass="inputbutton">
		<bean:message key="button.manager.executionCourseManagement.continue"/>
	</html:submit>
</html:form>