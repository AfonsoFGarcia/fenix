<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="java.lang.Math" %>


<strong><jsp:include page="../context.jsp"/></strong>
<br />
<br />


<logic:present name="<%= SessionConstants.LIST_INFOEXECUTIONCOURSE %>" scope="request">
	<bean:size id="numberInfoExecutionCourses" name="<%= SessionConstants.LIST_INFOEXECUTIONCOURSE %>"/>

	<logic:notEqual name="numberInfoExecutionCourses" value="0">
		<table>
			<tr>
				<td rowspan="2" class="listClasses-header">
					<bean:message key="label.name"/>
				</td>
				<td rowspan="2" class="listClasses-header">
					<bean:message key="label.occupancy"/>
				</td>
				<td rowspan="2" class="listClasses-header">
					<bean:message key="label.hours.load.total"/>				
				</td>
				<td rowspan="2" class="listClasses-header">
					<bean:message key="label.courseInformation"/>	
				</td>
				<td colspan="3" class="listClasses-header">
					<bean:message key="label.teachingReport"/>	
				</td>
				<td rowspan="2" class="listClasses-header">
					<bean:message key="message.teachingReport.students"/>
				</td>
			</tr>
			<tr>
				<td class="listClasses-header">
					<bean:message key="label.masterDegree.administrativeOffice.situation"/>	
				</td>
				<td class="listClasses-header">
					<bean:message key="message.teachingReport.AP/IN"/>(%)	
				</td>
				<td class="listClasses-header">
					<bean:message key="message.teachingReport.AP/AV"/>(%)	
				</td>
			</tr>

			<logic:iterate id="executionCourse" name="<%= SessionConstants.LIST_INFOEXECUTIONCOURSE %>" type="DataBeans.InfoExecutionCourse">
				<bean:define id="executionCourseOID" name="executionCourse" property="idInternal"/>
				<bean:define id="evaluated" name="executionCourse" property="infoSiteEvaluationStatistics.evaluated" type="java.lang.Integer"/>
				<bean:define id="enrolled" name="executionCourse" property="infoSiteEvaluationStatistics.enrolled" type="java.lang.Integer"/>
				<bean:define id="approved" name="executionCourse" property="infoSiteEvaluationStatistics.approved" type="java.lang.Integer"/>
				<tr>
					<logic:notEqual name="executionCourse" property="occupancy" value="-1">
						<td class="listClasses">
							<bean:write name="executionCourse" property="nome"/>
						</td>
						<td class="listClasses">
							<html:link page="<%= "/executionCoursesInformation.do?method=showOccupancyLevels&amp;"
									+ "executionCourseOID="
									+ pageContext.findAttribute("executionCourseOID") %>">
								<bean:write name="executionCourse" property="occupancy"/>
							</html:link>
						</td>
						
					</logic:notEqual>
				
				
					<logic:equal name="executionCourse" property="occupancy" value="-1">
						<td class="listClasses">
							<bean:write name="executionCourse" property="nome"/>
						</td>
						<td class="listClasses">
							<bean:message key="label.noShifts"/>
						</td>
					</logic:equal>

					<td class="listClasses">
						<bean:define id="equalLoad" name="executionCourse" property="equalLoad" />
						<html:link page="<%= "/executionCoursesInformation.do?method=showLoads&amp;"
								+ "executionCourseOID="
								+ pageContext.findAttribute("executionCourseOID") %>">
							<logic:equal name="executionCourse" property="equalLoad" value="true">
								<font color="#008000"><bean:message key ="label.hours.load.equal" /></font>
							</logic:equal>

							<logic:equal name="executionCourse" property="equalLoad" value="false">	
								<font color="#FF0000"><bean:message key ="label.hours.load.notEqual" /></font>
							</logic:equal>
						</html:link>
					</td>
					<td class="listClasses">
						<html:link page="/courseInformation.do" paramId="executionCourseId" paramName="executionCourse" paramProperty="idInternal">
							<bean:message key ="label.courseInformation.view" />
						</html:link>
					</td>
					<td class="listClasses">
						<html:link page="/viewTeachingReport.do?method=read" paramId="executionCourseId" paramName="executionCourse" paramProperty="idInternal">
							<logic:notEmpty name="executionCourse" property="courseReportFilled">
								<logic:equal name="executionCourse" property="courseReportFilled" value="true">
									<font color="#008000"><bean:message key ="link.filled" /></font>						
								</logic:equal>
								<logic:equal name="executionCourse" property="courseReportFilled" value="false">
									<font color="#FF0000"><bean:message key ="link.notFilled" /></font>						
								</logic:equal>
							</logic:notEmpty>
							<logic:empty name="executionCourse" property="courseReportFilled">
								<font color="#FF0000"><bean:message key ="link.notFilled" /></font>
							</logic:empty>
						</html:link>
					</td>
					<td class="listClasses">
						<% int ap_en =  Math.round(((float) approved.intValue() / (float) enrolled.intValue()) * 100); %>
						<%= ap_en %>						
					</td>
					<td class="listClasses">
						<% int ap_av = Math.round(((float) approved.intValue() / (float) evaluated.intValue()) * 100); %>
						<%= ap_av %>
					</td>
					<td class="listClasses">
						<html:link page="/studentsByCurricularCourse.do?method=readStudents" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal">
							<bean:message key ="link.students.see" />
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEqual>
	<logic:equal name="numberInfoExecutionCourses" value="0">
		<span class="error"><bean:message key="message.sop.search.execution.course.none"/></span>
	</logic:equal>
</logic:present>