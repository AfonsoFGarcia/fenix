<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>

<bean:define id="infoStudentShiftEnrolment" name="<%= SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY %>" />
<table width="70%" align="center">
	<tr>
		<td align="left">
			<logic:present name="infoStudentShiftEnrolment" property="allowedClasses">
				<h3>
					Turmas
				</h3>
				<logic:iterate id="infoClass" name="infoStudentShiftEnrolment" property="allowedClasses" length="1" offset="0">
					<bean:define id="curricularYear" name="infoClass" property="anoCurricular"/>
					<h4>
						<bean:write name="curricularYear"/>
						� Ano
					</h4>
					<html:link page="/studentShiftEnrolmentManager.do?method=showAvailableShifts" paramId="class" paramName="infoClass" paramProperty="nome" >
						<bean:write name="infoClass" property="nome"/>
					</html:link>
				</logic:iterate>
				<logic:iterate id="infoClass" name="infoStudentShiftEnrolment" property="allowedClasses" offset="2">
					<bean:define id="infoClassCurricularYear" name="infoClass" property="anoCurricular"/>
					<logic:notEqual name="curricularYear" value="<%= infoClassCurricularYear.toString() %>">
						<bean:define id="curricularYear" name="infoClass" property="anoCurricular"/>
						<h4>
							<bean:write name="curricularYear"/>
							� Ano
						</h4>
					</logic:notEqual>
					<html:link page="/studentShiftEnrolmentManager.do?method=showAvailableShifts" paramId="class" paramName="infoClass" paramProperty="nome" >
						<bean:write name="infoClass" property="nome"/>
					</html:link>
				</logic:iterate>
				<br>
			</logic:present>
			<logic:present name="infoLessons">
				<bean:size id="infoLessonsSize" name="infoLessons"/>
				<logic:equal name="infoLessonsSize" value="0">
					N�o est� inscrito em nenhum turno...
				</logic:equal>
				<logic:notEqual name="infoLessonsSize" value="0">
					<h3>Hor�rio:</h3>
					<app:gerarHorario name="infoLessons" type="<%= TimeTableType.CLASS_TIMETABLE_WITHOUT_LINKS %>"/>
				</logic:notEqual>
			</logic:present>
			<logic:notPresent name="infoStudentShiftEnrolment" property="allowedClasses">
				N�o existem turmas dispon�veis.
			</logic:notPresent>
		</td>
	</tr>
</table>
