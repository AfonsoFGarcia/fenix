<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
    	<td class="infoselected">
    		<p>O curso seleccionado &eacute;:</p>
    		<strong><jsp:include page="context.jsp"/></strong>
		</td>
	</tr>
</table>

<bean:define id="shiftName" name="<%= SessionConstants.SHIFT %>" property="nome"/>
<bean:define id="shiftId" name="<%= SessionConstants.SHIFT %>" property="idInternal"/>
<bean:define id="shiftType" name="<%= SessionConstants.SHIFT %>" property="tipo.tipo"/>

<h2>
<logic:present name="<%= SessionConstants.EXECUTION_COURSE %>" scope="request">
	<bean:write name="<%= SessionConstants.EXECUTION_COURSE %>" property="nome"/>
</logic:present> 
<br />
Turno: <bean:write name="shiftName"/>
<br />
<br />
Alunos Inscritos
</h2>

<br />
<logic:present name="<%= SessionConstants.STUDENT_LIST %>" scope="request">
		<table>
			<tr>
				<td class="listClasses-header">
					<bean:message key="label.number"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.name"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.mail"/>
				</td>
			</tr>
			<logic:iterate id="student" name="<%= SessionConstants.STUDENT_LIST %>">
				<tr align="center">
					<td class="listClasses">
						<bean:write name="student" property="number"/>
					</td>
					<td class="listClasses">
						<bean:write name="student" property="infoPerson.nome"/>
					</td>
					<td class="listClasses">
						<bean:write name="student" property="infoPerson.email"/>
					</td>
				</tr>
			</logic:iterate>
		</table>

<br />
<h2><bean:message key="title.transfer.students.shif"/></h2>
<br />
<span class="info"><bean:message key="message.transfer.students.shift.notice"/></span>
<br />
<logic:present name="<%= SessionConstants.SHIFTS %>" scope="request">
	<html:form action="/manageShiftStudents">
		<html:hidden property="page" value="1"/>
		<html:hidden property="method" value="changeStudentsShift"/>
		<html:hidden property="oldShiftId" value="<%= pageContext.findAttribute("shiftId").toString() %>"/>

		<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
					 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
		<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
					 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
		<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
					 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
		<html:hidden property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
					 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
		<html:hidden property="<%= SessionConstants.SHIFT_OID %>"
					 value="<%= pageContext.findAttribute("shiftOID").toString() %>"/>

		<table>
			<tr>
				<td class="listClasses-header">
				</td>
				<td class="listClasses-header">
				</td>
			</tr>
			<logic:iterate id="otherShift" name="<%= SessionConstants.SHIFTS %>">
				<logic:notEqual name="otherShift" property="nome"
						value="<%= pageContext.findAttribute("shiftName").toString() %>">
					<bean:define id="otherShiftId" name="otherShift" property="idInternal"/>
					<bean:define id="otherShiftType" name="otherShift" property="tipo.tipo"/>
					<logic:equal name="shiftType"
							value="<%= pageContext.findAttribute("otherShiftType").toString() %>">
					
						<tr align="center">
							<td class="listClasses">
								<html:radio property="newShiftId" value="<%= pageContext.findAttribute("otherShiftId").toString() %>"/>
							</td>
							<td class="listClasses">
								<bean:write name="otherShift" property="nome"/>
							</td>
						</tr>
					</logic:equal>
				</logic:notEqual>
			</logic:iterate>
		</table>

		<html:submit styleClass="inputbutton"><bean:message key="button.transfer"/></html:submit>
	</html:form> 

</logic:present>
</logic:present>

<logic:notPresent name="<%= SessionConstants.STUDENT_LIST %>" scope="request">
	<span class="error"><bean:message key="errors.students.none.in.shift"/></span>	
</logic:notPresent>