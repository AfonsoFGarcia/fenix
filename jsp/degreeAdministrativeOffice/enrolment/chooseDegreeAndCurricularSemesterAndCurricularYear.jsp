<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="infoExecutionDegreesNamesList" name="<%= SessionConstants.DEGREE_LIST %>"/>

<h2 align="center"><bean:message key="title.student.enrolment.without.rules"/></h2>
<html:errors/>
<br/>
<b><bean:message key="label.second.step.enrolment"/></b>
<center>
<br/>
<br/>
<html:form action="/prepareEnrolmentContext.do">
	<html:hidden property="method" value="prepare"/>
	<html:hidden property="page" value="1"/>
	<table border="0">
		<tr>
			<td align="left"><bean:message key="label.choose.degree"/>&nbsp;</td>
			<td align="left">
				<html:select property="infoExecutionDegreeName" size="1">
					<html:options collection="infoExecutionDegreesNamesList" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td align="left"><bean:message key="label.choose.semester"/>&nbsp;</td>
			<td align="left">
				<html:select property="semester" size="1">
					<html:option value=""><bean:message key="label.choose"/></html:option>
					<html:option value="1"><bean:message key="label.first.semester"/></html:option>
					<html:option value="2"><bean:message key="label.second.semester"/></html:option>
				</html:select>
			</td>
		</tr>
		<tr>
			<td align="left"><bean:message key="label.choose.year"/>&nbsp;</td>
			<td align="left">
				<html:select property="year" size="1">
					<html:option value=""><bean:message key="label.choose"/></html:option>
					<html:option value="1"><bean:message key="label.first.year"/></html:option>
					<html:option value="2"><bean:message key="label.second.year"/></html:option>
					<html:option value="3"><bean:message key="label.third.year"/></html:option>
					<html:option value="4"><bean:message key="label.fourth.year"/></html:option>
					<html:option value="5"><bean:message key="label.fiveth.year"/></html:option>
				</html:select>
			</td>
		</tr>
	</table>
	<br/>
	<br/>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.submit.degree.type.and.student"/>
	</html:submit>
</html:form>
</center>