<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h3><bean:message key="label.manager.teachers.modification"/></h3>

<ul style="list-style-type: square;">
	<li><html:link page="<%="/insertProfessorShipByNumber.do?method=prepareInsert&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId") + "&amp;executionCourseId=" + request.getParameter("executionCourseId")%>">
			<bean:message key="label.manager.insert.professorShip.by.number"/>
		</html:link>
	</li>
</ul>
	
<logic:notPresent name="infoTeachersList">
	<i><bean:message key="label.manager.teachers.nonExisting"/></i>
</logic:notPresent>
	
<logic:present name="infoTeachersList" scope="request">
	<html:form action="/saveTeachersBody" method="get">
		<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>	
		<html:hidden property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
		<html:hidden property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
		<html:hidden property="executionCourseId" value="<%= request.getParameter("executionCourseId") %>"/>
		<table width="80%" cellpadding="0" border="0">
			<tr>
				<td class="listClasses-header"><bean:message key="label.manager.teacher.name" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.teacher.number" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.teaches" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.responsible" />
				</td>	
			</tr>
			<logic:iterate id="infoTeacher" name="infoTeachersList">
				<tr>
					<bean:define id="teacherId" name="infoTeacher" property="idInternal"/>	 			
					<td class="listClasses"><bean:write name="infoTeacher" property="infoPerson.nome"/>
					</td>
					<td class="listClasses"><bean:write name="teacherId"/>
					</td>
					<td class="listClasses">
						<html:multibox property="professorShipTeachersIds">
							<bean:write name="teacherId"/>
						</html:multibox>
					</td>
					<td class="listClasses">
						<html:multibox property="responsibleTeachersIds">
							<bean:write name="teacherId"/>
						</html:multibox>
					</td>
	 			</tr>
	 		</logic:iterate>						
		</table>
		<br>
		<html:submit><bean:message key="label.manager.save.modifications"/></html:submit>
	</html:form> 	
</logic:present>
