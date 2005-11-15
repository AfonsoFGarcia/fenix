<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>



<br>

<span class="error"><html:errors/></span>



<br/>
<bean:write name="infoExecutionPeriod" property="name" /> - 
<bean:write name="infoExecutionPeriod" property="infoExecutionYear.year" />
<br/>

<html:form action="/mergeExecutionCoursesForm" >
	    
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="mergeExecutionCourses"/>
	<br/>
	
	
	<br/>
	
	
	<table cellpadding="1" >
	<tr >
	<td valign="top">
	<bean:write name="sourceInfoDegree" property="nome" /> -
	<bean:write name="sourceInfoDegree" property="sigla" />
	<br/>
	<strong>Escolha a Disciplina Execu��o de Origem</strong>
	<br/>	
	<br/>
	<table>
	<logic:iterate id="executionCourse" name="sourceExecutionCourses">
		<tr>
			<td class="listClasses">
			<html:radio property="sourceExecutionCourseId" idName="executionCourse" value="idInternal"/> 
			</td>
			<td class="listClasses">
			<bean:write name="executionCourse" property="sigla"/>
			</td>
			<td class="listClasses">
			<bean:write name="executionCourse" property="nome"/>
			</td>
		</tr>
	</logic:iterate>
	</table>
	</td>
	<td valign="top">
	<bean:write name="destinationInfoDegree" property="nome" /> -
	<bean:write name="destinationInfoDegree" property="sigla" />
	<br/>
	<strong>Escolha a Disciplina Execu��o de Destino</strong>
	<table>
	<logic:iterate id="executionCourse" name="destinationExecutionCourses">
		<tr>
			<td class="listClasses">
			<html:radio property="destinationExecutionCourseId" idName="executionCourse" value="idInternal"/> 
			</td>
			<td class="listClasses">
			<bean:write name="executionCourse" property="sigla"/>
			</td>
			<td class="listClasses">
			<bean:write name="executionCourse" property="nome"/>
			</td>
		</tr>
	</logic:iterate>
	</table>
	</td>
	</tr>
	</table>
	<br/>
	<br/>
	<html:submit styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
	<html:reset  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>
</html:form>