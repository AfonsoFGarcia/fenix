<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoRoom" %>
<%@ page import="DataBeans.InfoViewExamByDayAndShift" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.exams.list"/></h2>
<table width="100%">
	<tr>
		<td class="infoselected"><p>O dia e turno selecionados s&atilde;o:</p>
			<strong><jsp:include page="timeContext.jsp"/></strong>
        </td>
    </tr>
</table>
<br/><br/>
<span class="error"><html:errors/></span>

<bean:define id="deleteConfirm">
	return confirm('<bean:message key="message.confirm.delete.exam"/>')
</bean:define>

<logic:notPresent name="<%= SessionConstants.LIST_EXAMSANDINFO %>" scope="session">
	<table align="center">
		<tr>
			<td>
				<span class="error"><bean:message key="message.exams.none.for.day.shift"/></span>
			</td>
		</tr>
	</table>
</logic:notPresent>
<logic:present name="<%= SessionConstants.LIST_EXAMSANDINFO %>" scope="session">
	<table width="100%">
		<tr>
			<td class="listClasses-header"><bean:message key="property.course"/></td>
			<td class="listClasses-header"><bean:message key="property.degrees"/></td>
			<td class="listClasses-header"><bean:message key="property.number.students.attending.course"/></td>
			<td class="listClasses-header"><bean:message key="property.exam.reservedRooms"/></td>
			<td class="listClasses-header"><bean:message key="property.exam.number.vacancies"/></td>
			<td class="listClasses-header"><bean:message key="property.exam.manage"/></td>
		</tr>
		<logic:iterate id="infoViewExam" indexId="index" name="<%= SessionConstants.LIST_EXAMSANDINFO %>" scope="session">
			<% int seatsReserved = 0; %>
			<tr>
				<td class="listClasses">
					<logic:iterate id="infoExecutionCourse" name="infoViewExam" property="infoExecutionCourses">
						<bean:write name="infoExecutionCourse" property="nome"/><br />
					</logic:iterate>					
				</td>
				<td class="listClasses">
					<logic:iterate id="infoDegree" name="infoViewExam" property="infoDegrees">
						<bean:write name="infoDegree" property="sigla"/><br />
					</logic:iterate>
				</td>
				<td class="listClasses"><bean:write name="infoViewExam" property="numberStudentesAttendingCourse"/></td>
				<td class="listClasses">
					<logic:present name="infoViewExam" property="infoExam.associatedRooms">
						<logic:iterate id="infoRoom" name="infoViewExam" property="infoExam.associatedRooms">
							<bean:write name="infoRoom" property="nome"/>; 
							<% seatsReserved += ((InfoRoom) infoRoom).getCapacidadeExame().intValue(); %>
						</logic:iterate> 
					</logic:present>
					<logic:notPresent name="infoViewExam" property="infoExam.associatedRooms">
						<bean:message key="message.exam.no.rooms"/>
					</logic:notPresent>					
				</td>
				<td>
					<%= ((InfoViewExamByDayAndShift) infoViewExam).getNumberStudentesAttendingCourse().intValue() - seatsReserved %>
				</td>
				<td>
					<html:link paramId="indexExam" paramName="index" href="viewExamsDayAndShiftForm.do?method=edit"><bean:message key="label.edit"/></html:link>;
					<html:link paramId="indexExam" paramName="index" href="viewExamsDayAndShiftForm.do?method=delete" onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'><bean:message key="label.delete"/></html:link>
					<html:link paramId="indexExam" paramName="index" href="viewExamsDayAndShiftForm.do?method=addExecutionCourse"><br /><bean:message key="label.add.executionCourse"/></html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
	<br />
	<br />
	- N� de vagas para exames: <bean:write name="<%= SessionConstants.AVAILABLE_ROOM_OCCUPATION %>" scope="session"/>
<br />
</logic:present>