<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:importAttribute/>
<tiles:useAttribute id="executionCourseLink" name="executionCourseLink" classname="java.lang.String"/>
<tiles:useAttribute id="paramId" name="paramId" classname="java.lang.String"/>
<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="infoTeacher" property="infoPerson.nome"/><br />
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="infoTeacher" property="teacherNumber"/>
</p>
<logic:notEmpty name="detailedProfessorshipList" >	
	<h2><bean:message key="label.teacher.professorships"/></h2>
		<table width="100%"cellpadding="0" border="0">
			<tr>
				<td class="listClasses-header"><bean:message key="label.execution-course.acronym" />
				</td>
				<td class="listClasses-header" style="text-align:left"><bean:message key="label.execution-course.name" />
				</td>
				<td class="listClasses-header" style="text-align:left"><bean:message key="label.execution-course.degrees" />
				</td>
				<td class="listClasses-header"><bean:message key="label.execution-period" />
				</td>
			</tr>
			<logic:iterate id="detailedProfessorship" name="detailedProfessorshipList">
				<bean:define id="professorship" name="detailedProfessorship" property="infoProfessorship"/>
				<bean:define id="infoExecutionCourse" name="professorship" property="infoExecutionCourse"/>
				<tr>
					<td class="listClasses">
						<html:link page="<%= executionCourseLink %>" paramId="<%= paramId %>" paramName="infoExecutionCourse" paramProperty="idInternal">
							<bean:write name="infoExecutionCourse" property="sigla"/>
						</html:link>
					</td>			
					<td class="listClasses" style="text-align:left">
						<html:link page="<%= executionCourseLink %>" paramId="<%= paramId %>" paramName="infoExecutionCourse" paramProperty="idInternal">
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
						<bean:write name="infoExecutionCourse" property="infoExecutionPeriod.name"/>&nbsp;-&nbsp;
						<bean:write name="infoExecutionCourse" property="infoExecutionPeriod.infoExecutionYear.year"/>
					</td>
					
				</tr>
			</logic:iterate>
	 	</table>
</logic:notEmpty>