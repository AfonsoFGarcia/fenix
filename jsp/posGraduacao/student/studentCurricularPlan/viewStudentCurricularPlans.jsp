<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<bean:define id="studentCurricularPlansList" name="studentCurricularPlansList" scope="request"/>

<h2 align="left"><bean:message key="title.masterDegree.administrativeOffice.chooseStudentCurricularPlan"/></h2>
<span class="error"><html:errors/></span>
<table border="0" cellspacing="3" cellpadding="10">
	<tr>
		<th align="left"><bean:message key="label.masterDegree.administrativeOffice.studentCurricularPlanState"/></th>
		<th align="left"><bean:message key="label.masterDegree.administrativeOffice.studentCurricularPlanStartDate"/></th>
		<th align="left"><bean:message key="label.masterDegree.administrativeOffice.studentCurricularPlanBranch"/></th>
		<th align="left"><bean:message key="label.masterDegree.administrativeOffice.studentCurricularPlanDegree"/></th>
	</tr>
	<logic:iterate id="infoStudentCurricularPlan" name="studentCurricularPlansList" indexId="index">
		<bean:define id="showStudentCurricularCoursePlanLink">
			/showStudentCurricularCoursePlan.do?studentCurricularPlanId=<bean:write name="infoStudentCurricularPlan" property="idInternal"/>
		</bean:define>
		<tr>
			<td align="left">
				<html:link page="<%= pageContext.findAttribute("showStudentCurricularCoursePlanLink").toString() %>">
					<bean:write name="infoStudentCurricularPlan" property="currentState"/>
				</html:link>
			</td>
			<td align="left">
				<html:link page="<%= pageContext.findAttribute("showStudentCurricularCoursePlanLink").toString() %>">
					<bean:write name="infoStudentCurricularPlan" property="startDateFormatted"/>
				</html:link>
			</td>
			<td align="left">
				<logic:empty name="infoStudentCurricularPlan" property="infoBranch">
					<bean:message key="label.masterDegree.administrativeOffice.noBranch"/>
				</logic:empty>
				<logic:notEmpty name="infoStudentCurricularPlan" property="infoBranch">
					<logic:equal name="infoStudentCurricularPlan" property="infoBranch.name" value="<%= new String("") %>">
						<bean:message key="label.masterDegree.administrativeOffice.noBranch"/>
					</logic:equal>
					<logic:notEqual name="infoStudentCurricularPlan" property="infoBranch.name" value="<%= new String("") %>">
						<html:link page="<%= pageContext.findAttribute("showStudentCurricularCoursePlanLink").toString() %>">
							<bean:write name="infoStudentCurricularPlan" property="infoBranch.name"/>
						</html:link>
					</logic:notEqual>
				</logic:notEmpty>
			</td>
			<td align="left">
				<html:link page="<%= pageContext.findAttribute("showStudentCurricularCoursePlanLink").toString() %>">
					<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>
				</html:link>
			</td>
		</tr>
	</logic:iterate>
</table>
