<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants, DataBeans.InfoCurricularCourseScope" %>

<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>"/>
<bean:define id="executionYear" name="infoEnrolmentContext" property="infoExecutionPeriod.infoExecutionYear.year"/>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td bgcolor="#FFFFFF" class="infoselected">
			<b><bean:message key="label.info.about.current.execution.period"/></b><br/>
			<bean:message key="label.execution.year" arg0="<%= executionYear.toString() %>"/> - <bean:write name="infoEnrolmentContext" property="infoExecutionPeriod.name"/>
			<br/><br/>
			<b><bean:message key="label.info.about.chosen.student"/></b><br/>
			<b><bean:message key="label.student.degree"/></b>&nbsp;<bean:write name="infoEnrolmentContext" property="infoStudentActiveCurricularPlan.infoDegreeCurricularPlan.infoDegree.nome"/>&nbsp;<b>-</b>&nbsp;<b><bean:message key="label.student.number"/></b>&nbsp;<bean:write name="infoEnrolmentContext" property="infoStudentActiveCurricularPlan.infoStudent.number"/>
			<br/><br/>
			<b><bean:message key="label.info.about.chosen.degree"/></b><br/>
			<bean:write name="infoEnrolmentContext" property="chosenOptionalInfoDegree.nome"/>
		</td>
	</tr>
</table>
<br/>
<html:errors/>