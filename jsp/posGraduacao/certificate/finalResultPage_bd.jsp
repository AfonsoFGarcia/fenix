<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN%>" />
<bean:define id="infoExecutionYear" name="<%= SessionConstants.INFO_EXECUTION_YEAR %>" scope="session" />
<bean:define id="infoEnrolmentStudentCurricularPlan" name="<%= SessionConstants.ENROLMENT_LIST%>" />
<table width="90%">
	<tr> 
    	<td align="center" ><h2>Folha de Apuramento Final</h2><b>Ano lectivo <bean:write name="infoExecutionYear" property="year"/></b><br /><br /><br /><br /></td>
    <tr>
 		<td>Curso de <bean:write name="infoStudentCurricularPlan" property="specialization"/> em 
 			<b><bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/></b>
		</td>
	<tr>
 		<td><p>O Aluno n� <bean:write name="infoStudentCurricularPlan" property="infoStudent.number"/> - <b><bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/></b><p/></td>
    <tr>
 		<td>concluiu a parte escolar do curso de <bean:write name="infoStudentCurricularPlan" property="specialization"/> acima indicado, constitu�da pelas seguintes desciplinas e classifica��es:<br /><br /></td>
 	</tr> 
 </table>	
 <table width="90%" cellspacing="0">
 	<tr> 
 		<td class="header-first">Disciplinas</td>
 		<td class="header">Classif.</td>
 		<td class="header-last">Cred.</td>
 	</tr>	
 	<tr>
     	<logic:iterate id="itr" name="infoEnrolmentStudentCurricularPlan">
   	<tr>
      	<td class="cell-first"><bean:write name="itr" property="infoCurricularCourseScope.infoCurricularCourse.name" /></td>
     	<td class="cell-middle"><logic:iterate id="itr1" name="itr" property="infoEvaluations"><bean:write name="itr1" property="grade" /></logic:iterate></td>
 		<td class="cell-last"><bean:write name="itr" property="infoCurricularCourseScope.infoCurricularCourse.credits" /></td>  	
     </tr>
	</logic:iterate>
	<tr>
		<td class="results" align="right"><b>Total de cr�ditos:</b></td>
		<td class="results">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td class="results" align="center"><b>Total</b></td>
	</tr>
	</table>
	<%-- BRUNO ESTA PARTE DEVER� SER IMPRESSA NO VERSO DA �LTIMA PAGINA --%> 	
	<%--The Final Result --%>
	<jsp:include page="./finalResultTemplate1.jsp" flush="true" />