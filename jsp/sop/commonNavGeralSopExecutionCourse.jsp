<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<div id="navgeral">
<ul>	
	<li><html:link page="/home.do">Home</html:link></li>
	<li><html:link page="/manageExecutionCourses.do?method=prepareSearch&amp;page=0&amp" styleClass="active">Gest�o de Disciplinas</html:link></li>
	<li><html:link page="/chooseExecutionPeriod.do?method=prepare">Gest�o de Hor�rios</html:link></li>
	<li><html:link page="/principalSalas.do">Gest�o de Salas</html:link></li>
	<li><html:link page="/mainExamsNew.do?method=prepare"><bean:message key="link.writtenEvaluationManagement"/></html:link></li>
	<li><html:link page="/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare"><bean:message key="link.curriculumHistoric" bundle="CURRICULUM_HISTORIC_RESOURCES"/></html:link></li>
</ul>
</div>