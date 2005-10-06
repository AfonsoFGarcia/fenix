<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<script language="JavaScript" type="text/javascript"> 
	<!--
		function submitForm(){
			this.form.method.value="showDepartmentCompetenceCourses";
			this.form.submit();
		}		
	//-->
</script>	

		
<h2><bean:message key="label.manager.competence.course.management"/></h2>
<ul style="list-style-type: square;">
<li><html:link page="/createEditCompetenceCourse.do?method=prepareCreate"><bean:message key="label.manager.insert.competence.course"/></html:link></li>
</ul>
<br>
<br>

<span class="error"><html:errors/></span>
<html:form action="/competenceCourseManagement">
	<html:hidden property="method" value="deleteCompetenceCourses"/>
	<table>
		<tr>
			<td>
				<bean:message key="label.manager.department"/>
			</td>
			<td>
				<html:select property="departmentID" onchange="this.form.method.value='showDepartmentCompetenceCourses';this.form.submit();">
					<html:option value="null">&nbsp;</html:option>
					<html:options collection="departments" property="idInternal" labelProperty="name"/>
    			</html:select>
			</td>
		</tr>
	</table>
	
	<logic:notEmpty name="competenceCourses">	
			
			<table width="100%" cellpadding="0" border="0">
				<tr>
					<td class="listClasses-header">
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.code" />
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.competence.course" />
					</td>
				</tr>
					
					 
				<logic:iterate id="competenceCourse" name="competenceCourses">			
					<tr>	
						<td class="listClasses">
						
						<html:multibox property="competenceCoursesIds">
							<bean:write name="competenceCourse" property="idInternal"/>
						</html:multibox>
						</td>	
						<td class="listClasses"><html:link page="/competenceCourseManagement.do?method=showCompetenceCourse" paramId="competenceCourseID" paramName="competenceCourse" paramProperty="idInternal"><bean:write name="competenceCourse" property="code"/></html:link>
						</td>			
						<td class="listClasses"><p align="left"><html:link page="/competenceCourseManagement.do?method=showCompetenceCourse" paramId="competenceCourseID" paramName="competenceCourse" paramProperty="idInternal"><bean:write name="competenceCourse" property="name"/></html:link></p>
						</td>
				 	</tr>
				 		
				 </logic:iterate>
				
			</table>
			<br>
			<br>	
			<html:submit>
			   <bean:message key="label.manager.delete.selected.competences"/>
			</html:submit>
	
	</logic:notEmpty>
</html:form>