<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.student.LEEC.enrollment.rules.without"/></h2>

<span class="error"><html:errors/></span>
<html:form action="/courseEnrolmentWithoutRulesManagerDA" focus="studentNumber">
	<html:hidden property="method" value="readExecutionCourseEnrollments"/>
	<html:hidden property="page" value="1"/>
	<logic:present name="degreeType">
		<html:hidden property="degreeType" value="<%=pageContext.findAttribute("degreeType").toString()%>"/>
	</logic:present>
	<table>
		<tr>
			<td><bean:message key="label.choose.student"/>&nbsp;</td>
			<td>
				<input type="text" name="studentNumber" size="5" maxlength="5"/>
			</td>
		</tr>
		<tr>
			<td><bean:message key="label.choose.year.execution"/>&nbsp;</td>
			<td>
				<html:select property="executionYear" >
					<html:option value="" key="label.student.enrollment.select">
						<bean:message key="label.student.enrollment.select"/>
					</html:option>
					<html:optionsCollection name="executionYears"/>
				</html:select>
			</td>				
		</tr>
	</table>
	<br/>
	<br/>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.submit.student"/>
	</html:submit>
</html:form>
