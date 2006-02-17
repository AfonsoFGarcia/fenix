<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<center>
	<img alt=""  src="<%= request.getContextPath() %>/images/logo-fenix.gif" width="100" height="100"/>
</center>

<div style="font-size: 1.20em;">

<p><strong>&raquo; 
		<html:link module="/manager" page="/readDegrees.do">
			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.readDegrees" />
		</html:link>
</strong></p>

<p><strong>&nbsp; &raquo;
		<html:link module="/manager" page="<%= "/readDegree.do?degreeId=" + request.getParameter("degreeId") %>">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.backReadDegree" />
		</html:link>
</strong></p>

<p><strong>&nbsp; &nbsp; &raquo;
		<html:link module="/manager" page="<%= "/readDegreeCurricularPlan.do?degreeId=" + request.getParameter("degreeId") + "&degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") %>">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.backReadDegreeCurricularPlan" />
		</html:link>
</strong></p>

<p><strong>&nbsp; &nbsp; &nbsp;&raquo;
		<html:link module="/manager" page="<%= "/readCurricularCourse.do?degreeId=" + request.getParameter("degreeId") + "&degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&curricularCourseId=" + request.getParameter("curricularCourseId") %>">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.backReadCurricularCourse" />
		</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" page="/manageExecutionPeriods.do?method=prepare">
		Gest&atilde;o de Periodos Execu��o
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" page="/executionCourseManagement.do?method=firstPage">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement" />
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" page="/teachersManagement.do?method=firstPage">
		<bean:message bundle="MANAGER_RESOURCES" key="link.manager.teachersManagement" />
	</html:link>
</strong></p>
<p><strong>&raquo;
	<html:link module="/manager" page="/findPerson.do?method=prepareFindPerson&page=0">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.personManagement" />
	</html:link>
</strong></p>
<%--
<p><strong>&raquo; 
	<html:link module="/manager" page="/readExecutionPeriods.do">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.executionCourse" />
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" page="/chooseDegreesForExecutionCourseMerge.do?method=prepareChooseDegreesAndExecutionPeriod">
		Juntar Disciplinas Execu��o
	</html:link>
</strong></p>
--%>
<p><strong>&raquo; 
	<html:link module="/manager" page="/manageCache.do?method=prepare">
		Gest&atilde;o da Cache
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" page="/manageAdvisories.do?method=prepare">
		Gest&atilde;o da Avisos
	</html:link>
</strong></p>

</div>
