<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.Calendar" %>
<table>
	<tr>
		<logic:present name="infoCurricularCourse">
			<td>
				<h3><bean:message key="label.manager.curricularCourse.administrating"/></h3>
			</td>
			<td>
				<bean:define id="curricularCourseName" name="infoCurricularCourse" property="name"/>
				<h2><b><bean:write name="curricularCourseName"/></b></h2>
			</td>
		</logic:present>		
	</tr>
</table>

<span class="error"><html:errors/></span>
	
<h3><bean:message key="label.manager.viewAllcurricularCourseScopes"/></h3>

<logic:empty name="curricularCourseScopesList">
	<i><bean:message key="label.manager.curricularCourseScopes.nonExisting"/></i>
</logic:empty>

<logic:present name="curricularCourseScopesList" scope="request">
	<logic:notEmpty name="curricularCourseScopesList">
		<table width="70%" cellpadding="0" border="0">
			<tr>
				<td colspan="14" class="infoop">
					<bean:message key="label.manager.clickToCopy"/>
				</td>
			</tr>
			<tr>
				<td colspan="14">
					<br />
				</td>
			</tr>
			<tr>
				<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularYear" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularSemester" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.theoreticalHours" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.praticalHours" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.theoPratHours" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.labHours" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.maxIncrementNac" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.minIncrementNac" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.credits" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.ectsCredits" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.weigth" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.branch" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.beginDate" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.endDate" />
				</td>
			</tr>
			<logic:iterate id="curricularCourseScope" name="curricularCourseScopesList">
				<bean:define id="infoBranch" name="curricularCourseScope" property="infoBranch"/>
				<bean:define id="infoCurricularSemester" name="curricularCourseScope" property="infoCurricularSemester"/>
				<bean:define id="infoCurricularYear" name="infoCurricularSemester" property="infoCurricularYear"/>
				<tr>
					<td class="listClasses">
						<html:link page="<%="/insertCurricularCourseScopeFromAnother.do?method=prepareInsert&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId")%>" paramId="curricularCourseScopeId" paramName="curricularCourseScope" paramProperty="idInternal">
							<bean:write name="infoCurricularYear" property="year"/>
						</html:link>
					</td>
					<td class="listClasses">
						<html:link page="<%="/insertCurricularCourseScopeFromAnother.do?method=prepareInsert&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId")%>" paramId="curricularCourseScopeId" paramName="curricularCourseScope" paramProperty="idInternal">
							<bean:write name="infoCurricularSemester" property="semester"/>
						</html:link>
					</td>	 			
					<td class="listClasses">
						<html:link page="<%="/insertCurricularCourseScopeFromAnother.do?method=prepareInsert&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId")%>" paramId="curricularCourseScopeId" paramName="curricularCourseScope" paramProperty="idInternal">
							<bean:write name="curricularCourseScope" property="theoreticalHours"/>
						</html:link>
					</td>
					<td class="listClasses">
						<html:link page="<%="/insertCurricularCourseScopeFromAnother.do?method=prepareInsert&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId")%>" paramId="curricularCourseScopeId" paramName="curricularCourseScope" paramProperty="idInternal">
							<bean:write name="curricularCourseScope" property="praticalHours"/>
						</html:link>
					</td>
					<td class="listClasses">
						<html:link page="<%="/insertCurricularCourseScopeFromAnother.do?method=prepareInsert&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId")%>" paramId="curricularCourseScopeId" paramName="curricularCourseScope" paramProperty="idInternal">
							<bean:write name="curricularCourseScope" property="theoPratHours"/>
						</html:link>
					</td>
					<td class="listClasses">
						<html:link page="<%="/insertCurricularCourseScopeFromAnother.do?method=prepareInsert&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId")%>" paramId="curricularCourseScopeId" paramName="curricularCourseScope" paramProperty="idInternal">
							<bean:write name="curricularCourseScope" property="labHours"/>
						</html:link>
					</td>
					<td class="listClasses">
						<html:link page="<%="/insertCurricularCourseScopeFromAnother.do?method=prepareInsert&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId")%>" paramId="curricularCourseScopeId" paramName="curricularCourseScope" paramProperty="idInternal">
							<bean:write name="curricularCourseScope" property="maxIncrementNac"/>
						</html:link>
					</td>
					<td class="listClasses">
						<html:link page="<%="/insertCurricularCourseScopeFromAnother.do?method=prepareInsert&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId")%>" paramId="curricularCourseScopeId" paramName="curricularCourseScope" paramProperty="idInternal">
							<bean:write name="curricularCourseScope" property="minIncrementNac"/>
						</html:link>
					</td>
					<td class="listClasses">
						<html:link page="<%="/insertCurricularCourseScopeFromAnother.do?method=prepareInsert&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId")%>" paramId="curricularCourseScopeId" paramName="curricularCourseScope" paramProperty="idInternal">
							<bean:write name="curricularCourseScope" property="credits"/>
						</html:link>
					</td>
					<td class="listClasses">
						<html:link page="<%="/insertCurricularCourseScopeFromAnother.do?method=prepareInsert&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId")%>" paramId="curricularCourseScopeId" paramName="curricularCourseScope" paramProperty="idInternal">
							<bean:write name="curricularCourseScope" property="ectsCredits"/>
						</html:link>
					</td>
					<td class="listClasses">
						<html:link page="<%="/insertCurricularCourseScopeFromAnother.do?method=prepareInsert&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId")%>" paramId="curricularCourseScopeId" paramName="curricularCourseScope" paramProperty="idInternal">
							<bean:write name="curricularCourseScope" property="weigth"/>
						</html:link>
					</td>
					<logic:empty name="infoBranch" property="code">
						<td class="listClasses">&nbsp;</td>										
					</logic:empty>
					<logic:notEmpty name="infoBranch" property="code">
						<td class="listClasses">
						<html:link page="<%="/insertCurricularCourseScopeFromAnother.do?method=prepareInsert&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId")%>" paramId="curricularCourseScopeId" paramName="curricularCourseScope" paramProperty="idInternal">
							<bean:write name="infoBranch" property="code"/>
						</html:link>
						</td>										
					</logic:notEmpty>
					<td class="listClasses">
						<bean:define id="beginDate" name="curricularCourseScope" property="beginDate" type="java.util.Calendar"/>
							<%=beginDate.get(Calendar.DAY_OF_MONTH)%>/<%=String.valueOf(beginDate.get(Calendar.MONTH) + 1)%>/<%=beginDate.get(Calendar.YEAR)%>
					</td>
					<logic:empty name="curricularCourseScope" property="endDate">
						<td class="listClasses">&nbsp;</td>										
					</logic:empty>
					<logic:notEmpty name="curricularCourseScope" property="endDate">
						<td class="listClasses">
							<bean:define id="endDate" name="curricularCourseScope" property="endDate" type="java.util.Calendar"/>
							<%=endDate.get(Calendar.DAY_OF_MONTH)%>/<%=String.valueOf(endDate.get(Calendar.MONTH) + 1)%>/<%=endDate.get(Calendar.YEAR)%>
						</td>										
					</logic:notEmpty>
	 			</tr>
 			</logic:iterate>			
		</table>
	</logic:notEmpty>	 	
</logic:present>