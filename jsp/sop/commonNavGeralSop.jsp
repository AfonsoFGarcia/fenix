<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<ul id="navgeral">
	<li><html:link page="/home.do" styleClass="active">Home</html:link></li>
	<li><html:link page="/manageExecutionCourses.do?method=prepareSearch&amp;page=0">Gest�o de Disciplinas</html:link></li>
	<li><html:link page="/chooseExecutionPeriod.do?method=prepare">Gest�o de Hor�rios</html:link></li>
	<li><html:link page="/principalSalas.do">Gest�o de Salas</html:link></li>
	<li><html:link page="/mainExamsNew.do?method=prepare">Gest�o de Exames</html:link></li>  
	<li><html:link page="/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare"><bean:message key="link.curriculumHistoric" bundle="CURRICULUM_HISTORIC_RESOURCES"/></html:link></li>
</ul>