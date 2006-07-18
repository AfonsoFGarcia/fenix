<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<table >
	<tr class="listClasses-header">
		<th><bean:message key="label.inquiries.year" bundle="INQUIRIES_RESOURCES"/></th>
		<th><bean:message key="label.inquiries.semester" bundle="INQUIRIES_RESOURCES"/></th>
		<th><bean:message key="table.header.inquiries.courseName" bundle="INQUIRIES_RESOURCES"/></th>		
		<th><bean:message key="table.header.inquiries.degrees" bundle="INQUIRIES_RESOURCES"/></th>	
		<th/>					
	</tr>
	
	<logic:iterate id="curricularCourseScope" name="curricularCourseScopes">	
		<tr><td>&nbsp;</td></tr>	
		<tr class="listClasses">
			<td>
				<strong><bean:write name="curricularCourseScope" property="curricularSemester.curricularYear.year" /></strong>
			</td>
			<td>
				<strong><bean:write name="curricularCourseScope" property="curricularSemester.semester" /></strong>
			</td>
			<td>			
				<strong><bean:write name="curricularCourseScope" property="curricularCourse.name" /></strong>
			</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>			
		</tr>			
		<logic:iterate id="executionCourse" name="curricularCourseScope" property="curricularCourse.associatedExecutionCourses" >
			<bean:define id="executionYear" name="executionYear" type="java.lang.String" />
			<logic:equal name="executionCourse" property="executionPeriod.executionYear.year" value='<%= executionYear %>' >	
				<tr class="listClasses">
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>									
						<bean:write name="executionCourse" property="nome" />
					</td>
					<td>
						<logic:iterate id="CurricularCourse" name="executionCourse" property="associatedCurricularCourses" >
							<bean:write name="CurricularCourse" property="degreeCurricularPlan.name" />, 					
						</logic:iterate>
					</td>
					<bean:define id="executionCourseID" name="executionCourse" property="idInternal" ></bean:define>
					<td>
						<html:link page='<%= "/teachingStaff.do?method=viewTeachingStaff&executionCourseID=" + executionCourseID %>'>
							Editar Corpo Docente
						</html:link>					
					</td>
				</tr>
			</logic:equal>					
		</logic:iterate>		
	</logic:iterate>
	
</table>

