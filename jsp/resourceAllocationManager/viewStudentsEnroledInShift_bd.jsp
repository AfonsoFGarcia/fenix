<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<script language="Javascript" type="text/javascript">
<!--

var select = false;

function invertSelect(){
	if ( select == false ) { 
		select = true; 
	} else { 
		select = false;
	}
	if(document.forms[0].studentIDs.type=='checkbox'){
		var e = document.forms[0].studentIDs;
		e.checked = select;
	}else{
		for (var i=0; i<document.forms[0].studentIDs.length; i++){
			var e = document.forms[0].studentIDs[i];
			e.checked = select;
		}
	}
}
// -->
</script>

<em><bean:message key="title.resourceAllocationManager.management"/></em>
<h2><bean:message key="link.manage.turnos"/></h2>

<p class="mbottom05">O curso seleccionado &eacute;:</p>
<strong><jsp:include page="context.jsp"/></strong>

<bean:define id="shiftName" name="<%= SessionConstants.SHIFT %>" property="nome"/>
<bean:define id="shiftId" name="<%= SessionConstants.SHIFT %>" property="idInternal"/>
<bean:define id="shiftType" name="<%= SessionConstants.SHIFT %>" property="shiftTypesIntegerComparator"/>

<h3>Alunos Inscritos</h3>

<p>
	<logic:present name="<%= SessionConstants.EXECUTION_COURSE %>" scope="request">
		<bean:write name="<%= SessionConstants.EXECUTION_COURSE %>" property="nome"/>
	</logic:present>
</p>

<p>Turno: <bean:write name="shiftName"/></p>


<logic:present name="<%= SessionConstants.STUDENT_LIST %>" scope="request">
<html:form action="/manageShiftStudents">
	<table>
		<tr>
			<th>
			</th>
			<th>
				<bean:message key="label.number"/>
			</th>
			<th>
				<bean:message key="label.name"/>
			</th>
			<th>
				<bean:message key="label.mail"/>
			</th>
			<th>
				<bean:message key="label.degree"/>
			</th>
			<th>
			</th>
		</tr>
		<logic:iterate id="shiftEnrolment" name="shift" property="shift.shiftEnrolmentsOrderedByDate">
			<bean:define id="student" name="shiftEnrolment" property="registration"/>
			<tr>
				<td>
					<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.studentIDs" property="studentIDs"><bean:write name="student" property="idInternal"/></html:multibox>
				</td>
				<td>
					<bean:write name="student" property="number"/>
				</td>
				<td>
					<bean:write name="student" property="student.person.name"/>
				</td>
				<td>
					<bean:write name="student" property="student.person.email"/>
				</td>
				<td>
					<bean:write name="student" property="activeOrConcludedStudentCurricularPlan.degreeCurricularPlan.degree.sigla"/>
				</td>
				<td>
					<dt:format pattern="dd/MM/yyyy HH:mm:ss">
						<bean:write name="shiftEnrolment" property="createdOn.millis"/>
					</dt:format>
				</td>
			</tr>
		</logic:iterate>
	</table>


<p class="mtop2"><strong><bean:message key="title.transfer.students.shif"/></strong></p>

<p>
	<span class="info"><bean:message key="message.transfer.students.shift.notice"/></span>
</p>

<logic:present name="<%= SessionConstants.SHIFTS %>" scope="request">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="changeStudentsShift"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.oldShiftId" property="oldShiftId" value="<%= pageContext.findAttribute("shiftId").toString() %>"/>

		<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
					 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
		<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
					 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
		<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
					 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
		<html:hidden alt="<%= SessionConstants.EXECUTION_COURSE_OID %>" property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
					 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
		<html:hidden alt="<%= SessionConstants.SHIFT_OID %>" property="<%= SessionConstants.SHIFT_OID %>"
					 value="<%= pageContext.findAttribute("shiftOID").toString() %>"/>

		<table>
			<tr>
				<th>
				</th>
				<th>
				</th>
			</tr>
			<logic:iterate id="otherShift" name="<%= SessionConstants.SHIFTS %>">
				<logic:notEqual name="otherShift" property="nome" value="<%= pageContext.findAttribute("shiftName").toString() %>">
					<bean:define id="otherShiftId" name="otherShift" property="idInternal"/>
					<bean:define id="otherShiftType" name="otherShift" property="shiftTypesIntegerComparator"/>
					<logic:equal name="shiftType" value="<%= pageContext.findAttribute("otherShiftType").toString() %>">
						<tr>
							<td>
								<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.newShiftId" property="newShiftId" value="<%= pageContext.findAttribute("otherShiftId").toString() %>"/>
							</td>
							<td>
								<bean:write name="otherShift" property="nome"/>
							</td>
						</tr>
					</logic:equal>
				</logic:notEqual>
			</logic:iterate>
		</table>
		<p>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.transfer"/></html:submit>
		</p>
</logic:present>

	</html:form> 
</logic:present>

<logic:notPresent name="<%= SessionConstants.STUDENT_LIST %>" scope="request">
	<p>
		<span class="warning0"><!-- Error messages go here --><bean:message key="errors.students.none.in.shift"/></span>	
	</p>
</logic:notPresent>