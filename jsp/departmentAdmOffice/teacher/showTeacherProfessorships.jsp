<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<bean:parameter id="executionPeriodId" name="executionPeriodId" />
<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="infoTeacher" property="infoPerson.nome"/><br />
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="infoTeacher" property="teacherNumber"/> <br />
	(<i><html:link page='<%= "/teacherSearchForTeacherCreditsSheet.do?method=doSearch&page=1&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherNumber" paramName="infoTeacher" paramProperty="teacherNumber">
		<bean:message key="label.departmentTeachersList.teacherCreditsSheet"/>
	</html:link></i>)		
</p>

<bean:parameter id="executionPeriodId" name="executionPeriodId"/>

<logic:messagesPresent>
	<html:errors />
</logic:messagesPresent>
<logic:notEmpty name="detailedProfessorshipList" >	
	<bean:define id="executionCourseLink">
	/manageTeacherShiftProfessorships.do?page=0&amp;method=showForm&amp;teacherId=<bean:write name="infoTeacher" property="idInternal"/>&amp;executionPeriodId=<bean:write name="executionPeriodId"/>	
	</bean:define>
	<h2><bean:message key="label.teacher.professorships"/></h2>
		<table width="100%"cellpadding="5" border="0">
			<tr>
				<td class="listClasses-header" style="text-align:left">
					<bean:message key="label.execution-course.name" />
				</td>
				<td class="listClasses-header" style="text-align:left">	
					<bean:message key="label.execution-course.degrees" />
				</td>
				<td class="listClasses-header">
					<bean:message key="label.professorship.responsibleFor" />
				</td>
				<td class="listClasses-header">
					<bean:message key="label.execution-period" />
				</td>
			</tr>
			<logic:iterate id="detailedProfessorship" name="detailedProfessorshipList">
				<bean:define id="professorship" name="detailedProfessorship" property="infoProfessorship"/>
				<bean:define id="infoExecutionCourse" name="professorship" property="infoExecutionCourse"/>
				<tr>
					<td class="listClasses" style="text-align:left">
						<html:link page="<%= executionCourseLink %>" paramId="executionCourseId" paramName="infoExecutionCourse" paramProperty="idInternal">
							<bean:write name="infoExecutionCourse" property="nome"/>
						</html:link>
					</td>
					<td class="listClasses" style="text-align:left">
						<bean:define id="infoDegreeList" name="detailedProfessorship" property="infoDegreeList" />
						<bean:size id="degreeSizeList" name="infoDegreeList"/>
						<logic:iterate id="infoDegree" name="infoDegreeList" indexId="index">
							<bean:write name="infoDegree" property="sigla" /> 
							<logic:notEqual name="degreeSizeList" value="<%= String.valueOf(index.intValue() + 1) %>">
							,
							</logic:notEqual>
						</logic:iterate>
					</td>					
					<td class="listClasses">
						<logic:equal name="detailedProfessorship" property="responsibleFor" value="true">
							<bean:message key="label.yes"/>
						</logic:equal>
						<logic:equal name="detailedProfessorship" property="responsibleFor" value="false">
							<bean:message key="label.no"/>
						</logic:equal>					
					</td>					
					<td class="listClasses">
						<bean:write name="infoExecutionCourse" property="infoExecutionPeriod.name"/>&nbsp;-&nbsp;
						<bean:write name="infoExecutionCourse" property="infoExecutionPeriod.infoExecutionYear.year"/>
					</td>
				</tr>
			</logic:iterate>
	 	</table>
</logic:notEmpty>

