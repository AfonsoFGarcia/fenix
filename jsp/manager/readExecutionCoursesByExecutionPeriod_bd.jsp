<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<table>
		<td>
			<h2><bean:message key="label.manager.curricularCourse.administrating"/></h2>
		</td>
		<td>
			<h2><b><bean:define id="curricularCourseName" value="<%= request.getParameter("name") %>"/>
					<bean:write name="curricularCourseName"/></b></h2>
		</td>
</table>
<table>
		<td>
			<h3><bean:message key="label.manager.executionPeriod"/></h3>
		</td>
		<td>
			<h2><bean:write name="executionPeriodNameAndYear"/></h2>
		</td>
</table>

<span class="error"><html:errors/></span>
<logic:notEmpty name="infoExecutionCoursesList" scope="request">

<html:form action="/associateExecutionCourseToCurricularCourse" method="get">
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="associate"/>
	<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>	
	<html:hidden property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
	<html:hidden property="executionPeriodId" value="<%= request.getParameter("executionPeriodId") %>"/>
	<html:hidden property="name"  value="<%= request.getParameter("name") %>"/>

	<bean:message key="list.title.execution.courses.toAssociate"/>
	<br>
	</br>
	<table>
		<tr>
			<td class="listClasses-header">	
			</td>
			<td class="listClasses-header"><bean:message key="label.manager.executionCourse.name" />
			</td>
			<td class="listClasses-header"><bean:message key="label.manager.executionCourse.code" />
			</td>
			<td class="listClasses-header"><bean:message key="label.manager.executionCourse.executionPeriod" />
			</td>
		</tr>
		<logic:iterate id="infoExecutionCourse" name="infoExecutionCoursesList">
		<bean:define id="infoExecutionCourse" name="infoExecutionCourse"/>
							
			<tr>	
				<td class="listClasses">
				 
					<html:radio property="executionCourseId" idName="infoExecutionCourse" value="idInternal" />	
							
				</td>			
				<td class="listClasses"><bean:write name="infoExecutionCourse" property="nome"/>
				</td>
				<td class="listClasses"><bean:write name="infoExecutionCourse" property="sigla"/>
				</td>
				<td class="listClasses"><bean:write name="infoExecutionCourse" property="infoExecutionPeriod.name"/>
				</td>
			</tr>
		</logic:iterate>	
	</table>
	<html:submit ><bean:message key="label.manager.associate.execution.courses"/></html:submit>
</html:form>
</logic:notEmpty>

<logic:empty name="infoExecutionCoursesList" scope="request">
	<span class="error">
		<html:errors /><bean:message key="errors.execution.courses.none"/>
	</span>
</logic:empty>