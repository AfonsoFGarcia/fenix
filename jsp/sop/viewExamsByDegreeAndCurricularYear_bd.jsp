<%@ page language="java" %>
<%@ page import ="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="DataBeans.InfoExecutionCourseAndExams" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
	   	<table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td class="infoselected"><p>O curso seleccionado
              &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
            </td>
          </tr>
        </table>
        <br/>
        <h2><bean:message key="title.exams.list"/></h2>
        <span class="error"><html:errors/></span>
		<logic:notPresent name="<%= SessionConstants.INFO_EXAMS_KEY %>" scope="session">
			<table align="center"  cellpadding='0' cellspacing='0'>
				<tr align="center">
					<td>
						<font color='red'> <bean:message key="message.exams.none.for.executionDegree.CurricularYear.ExecutionPeriod"/> </font>
					</td>
				</tr>
			</table>
		</logic:notPresent>
		
<logic:present name="<%= SessionConstants.INFO_EXAMS_KEY %>" scope="session">
	<table align="center" border='1' cellpadding='10'>
				<tr align="center">
					<td>
						<bean:message key="property.course"/>
					</td>
					<td>
						<bean:message key="property.number.students.attending.course"/>
					</td>
					<td>
						<bean:message key="property.exam.1stExam"/>
					</td>
					<td>
						<bean:message key="property.exam.2stExam"/>
					</td>
				</tr>
				<logic:iterate id="infoExecutionCourseAndExams" name="<%= SessionConstants.INFO_EXAMS_KEY %>" scope="session">
					<tr align="center">
						<td>
							<bean:write name="infoExecutionCourseAndExams" property="infoExecutionCourse.nome"/>
						</td>
						<td>
							<bean:write name="infoExecutionCourseAndExams" property="numberStudentesAttendingCourse"/>
						</td>
						<td>
						  <logic:present name="infoExecutionCourseAndExams" property="infoExam1">
							<logic:notEmpty name="infoExecutionCourseAndExams" property="infoExam1.day">
								<%= ((InfoExecutionCourseAndExams)infoExecutionCourseAndExams).getInfoExam1().getDay().get(Calendar.YEAR) %> -
								<%= ((InfoExecutionCourseAndExams)infoExecutionCourseAndExams).getInfoExam1().getDay().get(Calendar.MONTH) + 1 %> -
								<%= ((InfoExecutionCourseAndExams)infoExecutionCourseAndExams).getInfoExam1().getDay().get(Calendar.DAY_OF_MONTH) %>
								<logic:notEmpty name="infoExecutionCourseAndExams" property="infoExam1.beginning">
			                    	<% Integer iH = new Integer(((InfoExecutionCourseAndExams)infoExecutionCourseAndExams).getInfoExam1().getBeginning().get(Calendar.HOUR_OF_DAY)); %>
			                    	<% Integer iM = new Integer(((InfoExecutionCourseAndExams)infoExecutionCourseAndExams).getInfoExam1().getBeginning().get(Calendar.MINUTE)); %>
									<br/>
									<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
								</logic:notEmpty>
							</logic:notEmpty>
							<logic:empty name="infoExecutionCourseAndExams" property="infoExam1.day">
								<bean:message key="message.exam.not.scheduled"/>
							</logic:empty>
							<logic:notEmpty name="infoExecutionCourseAndExams" property ="infoExam1.associatedRooms">
								<br/>
								<logic:iterate id="room" name="infoExecutionCourseAndExams" property ="infoExam1.associatedRooms">
									<bean:write name="room" property="nome"/>
								</logic:iterate>
							</logic:notEmpty>
						  </logic:present>
						  <logic:notPresent name="infoExecutionCourseAndExams" property="infoExam1">
						  	<bean:message key="message.exam.not.scheduled"/>
  						  </logic:notPresent>							
						</td>
						<td>
						  <logic:present name="infoExecutionCourseAndExams" property="infoExam2">
							<logic:notEmpty name="infoExecutionCourseAndExams" property="infoExam2.day">
								<%= ((InfoExecutionCourseAndExams)infoExecutionCourseAndExams).getInfoExam2().getDay().get(Calendar.YEAR) %> -
								<%= ((InfoExecutionCourseAndExams)infoExecutionCourseAndExams).getInfoExam2().getDay().get(Calendar.MONTH) + 1 %> -
								<%= ((InfoExecutionCourseAndExams)infoExecutionCourseAndExams).getInfoExam2().getDay().get(Calendar.DAY_OF_MONTH) %>
								<logic:notEmpty name="infoExecutionCourseAndExams" property="infoExam2.beginning">
			                    	<% Integer iH = new Integer(((InfoExecutionCourseAndExams)infoExecutionCourseAndExams).getInfoExam2().getBeginning().get(Calendar.HOUR_OF_DAY)); %>
			                    	<% Integer iM = new Integer(((InfoExecutionCourseAndExams)infoExecutionCourseAndExams).getInfoExam2().getBeginning().get(Calendar.MINUTE)); %>
									<br/>
									<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
								</logic:notEmpty>
							</logic:notEmpty>
							<logic:empty name="infoExecutionCourseAndExams" property="infoExam2.day">
								<bean:message key="message.exam.not.scheduled"/>
							</logic:empty>
							<logic:notEmpty name="infoExecutionCourseAndExams" property ="infoExam2.associatedRooms">
								<br/>
								<logic:iterate id="room" name="infoExecutionCourseAndExams" property ="infoExam2.associatedRooms">
									<bean:write name="room" property="nome"/>
								</logic:iterate>
							</logic:notEmpty>
						  </logic:present>
						  <logic:notPresent name="infoExecutionCourseAndExams" property="infoExam2">
						  	<bean:message key="message.exam.not.scheduled"/>
  						  </logic:notPresent>
						</td>
					</tr>
			</logic:iterate>
	</table>
				
</logic:present>