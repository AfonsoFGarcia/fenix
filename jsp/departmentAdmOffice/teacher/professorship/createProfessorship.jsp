<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="Util.TipoCurso" %>
<bean:define id="infoTeacher" name="infoTeacher" scope="request" />
<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="infoTeacher" property="infoPerson.nome"/><br />
	<bean:define id="teacherNumber" name="infoTeacher" property="teacherNumber"/>
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="teacherNumber"/><br />
	<%--<b><bean:message key="label.executionYear" /> </b> <bean:write name="executionYear" property="year" /> <br />--%>
<%--	(<i><html:link page="/teacherSearchForTeacherCreditsSheet.do?method=doSearch&page=1" paramId="teacherNumber" paramName="infoTeacher" paramProperty="teacherNumber">
		<bean:message key="label.departmentTeachersList.teacherCreditsSheet"/>
	</html:link></i>) --%>

</p>

<logic:messagesPresent>
	<html:errors/>
</logic:messagesPresent>
<html:form action="/createProfessorship">
	<html:hidden property="teacherNumber"/>
	<html:hidden property="method" value="createProfessorship"/>	
	<html:hidden property="page" />
	

	<h2>
		<span class="emphasis-box">1</span> <i><bean:message key="label.professorship.chooseExecutionPeriod"/></i>
	</h2>
	<html:select property="executionPeriodId" onchange="this.form.page.value='1';this.form.method.value='showExecutionDegrees';this.form.submit();">
		<option></option>
		<html:options collection="executionPeriods" property="idInternal" labelProperty="description"/>
	</html:select>
	
	<logic:present name="executionDegrees">
		<h2>
			<span class="emphasis-box">2</span> <i><bean:message key="label.professorship.chooseExecutionDegree"/></i>
		</h2>
		<html:select property="executionDegreeId" onchange="this.form.page.value='2';this.form.method.value='showExecutionDegreeExecutionCourses';this.form.submit();">
			<option></option>
			<html:options collection="executionDegrees" labelProperty="label" property="value"/>
		</html:select>
	</logic:present>
	
	<logic:present name="executionCourses">
		<h2>
			<span class="emphasis-box">3</span> <i><bean:message key="label.professorship.chooseExecutionCourse"/></i>
		</h2>
		<html:select property="executionCourseId">
			<option></option>		
			<html:options collection="executionCourses" labelProperty="nome" property="idInternal"/>
		</html:select>
		<br />
		<html:checkbox property="responsibleFor" value="true">
			<bean:message key="label.professorship.responsibleFor"/>
		</html:checkbox>
		<br />
		<br />
		<html:submit styleClass="inputbutton">
			<bean:message key="button.ok"/>
		</html:submit>
	</logic:present>
</html:form>
