<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="DataBeans.InfoCurricularCourseScope, java.lang.Boolean, Util.Data, java.util.Calendar" %>
<logic:present name="infoExecutionYears">
	<bean:define id="infoCurricularCourseName"><%=pageContext.findAttribute("infoCurricularCourseName")%></bean:define>
	<h2><bean:write name="infoCurricularCourseName"/></h2>
	<span class="error"><html:errors/></span>
	<p><html:link page="<%="/degreeCurricularPlanManagement.do?method=viewActiveCurricularCourseInformation&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode") %>">
		(<font color="#0066CC"><bean:message key="link.coordinator.degreeCurricularPlan.see"/>&nbsp;<bean:message key="label.coordinator.degreeCurricularPlan.current.information"/></font>)
	</html:link></p>
	<table>
		<tr>
			<td><bean:message key="title.masterDegree.administrativeOffice.chooseExecutionYear"/>:</td>
		</tr>
		<logic:iterate id="executionYear" name="infoExecutionYears">
			<tr>
				<td>
					<bean:define id="stringExecutionYear" name="executionYear" property="label"/>
					<html:link page="<%="/degreeCurricularPlanManagement.do?method=viewCurricularCourseInformationHistory&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode") + "&amp;executionYear=" + stringExecutionYear + "&amp;infoCurricularCourseName=" + infoCurricularCourseName %>">
						<bean:write name="executionYear" property="label"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>