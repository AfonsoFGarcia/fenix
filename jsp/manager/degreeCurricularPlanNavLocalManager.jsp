<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<center>
	<img alt=""  src="<%= request.getContextPath() %>/images/logo-fenix.gif" width="100" height="100"/>
</center>

<p><strong>&raquo; 
		<html:link page="/readDegrees.do">
			<bean:message key="label.manager.readDegrees" />
		</html:link>
</strong></p>

<p><strong>&nbsp; &raquo;
		<html:link page="<%= "/readDegree.do?degreeId=" + request.getAttribute("degreeId")%>">
			<bean:message key="label.manager.backReadDegree" />
		</html:link>
</strong></p>

<p><strong>&nbsp; &nbsp; &raquo;
		<html:link page="<%= "/readDegreeCurricularPlan.do?degreeId=" + request.getAttribute("degreeId") + "&degreeCurricularPlanId=" + request.getAttribute("degreeCurricularPlanId")%>">
		<bean:message key="label.manager.backReadDegreeCurricularPlan" />
		</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link page="/manageExecutionPeriods.do?method=prepare">
		Gest&atilde;o de Periodos Execu��o
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link page="/executionCourseManagement.do?method=firstPage">
		<bean:message key="label.manager.executionCourseManagement" />
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link page="/teachersManagement.do?method=firstPage">
		<bean:message key="link.manager.teachersManagement" />
	</html:link>
</strong></p>

<%--
<p><strong>&raquo; 
	<html:link page="/readExecutionPeriods.do">
		<bean:message key="label.manager.insert.executionCourse" />
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link page="/chooseDegreesForExecutionCourseMerge.do?method=prepareChooseDegreesAndExecutionPeriod">
		Juntar Disciplinas Execu��o
	</html:link>
</strong></p>
--%>

<p><strong>&raquo;
	<html:link page="/findPerson.do?method=prepareFindPerson&page=0">
		<bean:message key="label.manager.personManagement" />
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link page="/manageCache.do?method=prepare">
		Gest&atilde;o da Cache
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link page="/manageAdvisories.do?method=prepare">
		Gest&atilde;o da Avisos
	</html:link>
</strong></p>
