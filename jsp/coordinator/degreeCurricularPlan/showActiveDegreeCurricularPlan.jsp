<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="label.coordinator.degreeCurricular.active"/></h2>

<logic:present name="allActiveCurricularCourseScopes">
			<logic:iterate id="curricularCourseScopeElem" name="allActiveCurricularCourseScopes" type="DataBeans.InfoCurricularCourseScope" length="1">
				<bean:define id="currentSemester" name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/>
			</logic:iterate>
	<table>
		<tr>
			<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularYear"/></td>
			<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularSemester"/></td>
			<td class="listClasses-header"><bean:message key="label.curricularCourse"/></td>
			<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.branch"/></td>
		</tr>
			<logic:iterate id="curricularCourseScopeElem" name="allActiveCurricularCourseScopes" type="DataBeans.InfoCurricularCourseScope">
				<logic:notEqual name="curricularCourseScopeElem" property="infoCurricularSemester.semester" value="<%= pageContext.findAttribute("currentSemester").toString()%>">
					<tr>
						<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularYear"/></td>
						<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularSemester"/></td>
						<td class="listClasses-header"><bean:message key="label.curricularCourse"/></td>
						<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.branch"/></td>
					</tr>
					<bean:define id="currentSemester" name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/>
				</logic:notEqual>
				<tr>
					<td class="listClasses"><bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/></td>
					<td class="listClasses"><bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/></td>
					<td class="listClasses" style="text-align:left">
					<bean:define id="curricularCourseCode" name="curricularCourseScopeElem" property="infoCurricularCourse.idInternal"/>
						<html:link page="<%="/degreeCurricularPlanManagement.do?method=viewActiveCurricularCourseInformation&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + curricularCourseCode %>">
							<bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.name"/>
						</html:link>
					</td>
					<td class="listClasses">
						<logic:notEmpty name="curricularCourseScopeElem" property="infoBranch.prettyCode">
							<bean:write name="curricularCourseScopeElem" property="infoBranch.prettyCode"/>
						</logic:notEmpty>
						<logic:empty name="curricularCourseScopeElem" property="infoBranch.prettyCode">
							&nbsp;
						</logic:empty>
					</td>
				</tr>
			</logic:iterate>
		</logic:iterate>
	</table>
</logic:present>
