<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
<%@ page import="DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons"%>
<%@ page import="DataBeans.InfoLesson"%>
<%@ page import="java.util.Calendar" %>

<logic:notPresent name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" scope="session">
<table align="center"  cellpadding='0' cellspacing='0'>
			<tr align="center">
				<td>
					<font color='red'> <bean:message key="message.public.notfound.executionCourse"/> </font>
				</td>
			</tr>
		</table>
</logic:notPresent>

<logic:present name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" scope="session">

<h2><bean:message key="property.executionCourse.associatedCurricularCourses"/></h2>
	<logic:present name="publico.infoCurricularCourses" scope="session">
			<table align="center" cellspacing="0" cellpadding="0" >
					<tr class="timeTable_line" align="center">
						<td class="horariosHoras_first">
							<bean:message key="property.curricularCourse.name"/>
						</td>
						<td class="horariosHoras_first">
							<bean:message key="property.degree.initials"/>
						</td>
						<td class="horariosHoras_first">
							<bean:message key="property.curricularCourse.curricularYear"/>
						</td>
						<td class="horariosHoras_first">
							<bean:message key="property.curricularCourse.semester"/>
						</td>
					</tr>			
				<logic:iterate id="curricularCourse" name="publico.infoCurricularCourses" scope="session">
					<tr class="timeTable_line" align="center">
						<td class="horariosHoras_first">
							<bean:write name="curricularCourse" property="name"/>
						</td>
						<td class="horariosHoras_first">
							<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla"/>
						</td>
						<td class="horariosHoras_first">
							<logic:iterate id="infoCurricularSemester" name="curricularCourse" property="associatedInfoCurricularSemesters">
								<bean:write name="infoCurricularSemester" property="infoCurricularYear.year"/>&nbsp;
							</logic:iterate>
						</td>
						<td class="horariosHoras_first">
							<logic:iterate id="infoCurricularSemester" name="curricularCourse" property="associatedInfoCurricularSemesters">
								<bean:write name="infoCurricularSemester" property="semester"/>&nbsp;
							</logic:iterate>
						</td>
					</tr>
				</logic:iterate>
			</table>			
	</logic:present>
	
<logic:notPresent name="publico.infoCurricularCourses" scope="session">
		<bean:message key="message.public.notfound.curricularCourses"/>
</logic:notPresent>
</logic:present>		
