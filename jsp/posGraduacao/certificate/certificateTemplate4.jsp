<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoEnrolmentEvaluation" %>
	<logic:present name="<%= SessionConstants.ENROLMENT_LIST%>">
		<bean:define id="infoEnrolmentStudentCurricularPlan" name="<%= SessionConstants.ENROLMENT_LIST%>" />
	</logic:present>
	<logic:present name="<%= SessionConstants.EXTRA_ENROLMENT_LIST%>">
		<bean:define id="infoEnrolmentExtraCurricularPlan" name="<%= SessionConstants.EXTRA_ENROLMENT_LIST%>" />
	</logic:present>
	<logic:present name="<%= SessionConstants.ENROLMENT%>">
	<logic:present name="<%= SessionConstants.ENROLMENT_LIST%>">
	nas seguintes disciplinas:<br />
	<logic:iterate id="itr" name="infoEnrolmentStudentCurricularPlan">
	<br />
		<bean:write name="itr" property="infoCurricularCourseScope.infoCurricularCourse.name" />
	</logic:iterate>
	</logic:present>
	<logic:present name="<%= SessionConstants.EXTRA_ENROLMENT_LIST%>">
	nas seguintes disciplinas Extra-Curriculares:<br />
	<logic:iterate id="itr" name="infoEnrolmentExtraCurricularPlan">
	<br />
	<bean:write name="itr" property="infoCurricularCourseScope.infoCurricularCourse.name" />
	</logic:iterate>
		</logic:present>
	</logic:present>
		<logic:present name="<%= SessionConstants.APROVMENT%>">
	<logic:iterate id="itr" name="infoEnrolmentStudentCurricularPlan">
	<br />
		<bean:write name="itr" property="infoCurricularCourseScope.infoCurricularCourse.name" />
 		<bean:write name="itr" property="infoExecutionPeriod.infoExecutionYear.year" />
 		com 
  		<bean:write name="itr" property="infoEnrolmentEvaluation.grade" />
 		valores
	</logic:iterate>
		</logic:present>
		<logic:present name="<%= SessionConstants.EXTRA_CURRICULAR_APROVMENT%>">
			<bean:define id="infoEnrolmentStudentCurricularPlan" name="<%= SessionConstants.EXTRA_ENROLMENT_LIST%>" />
	<logic:iterate id="itr" name="infoEnrolmentStudentCurricularPlan">
<br />
 		<bean:write name="itr" property="infoCurricularCourseScope.infoCurricularCourse.name" />
 		<bean:write name="itr" property="infoExecutionPeriod.infoExecutionYear.year" />
 		com 
 		<bean:write name="itr" property="infoEnrolmentEvaluation.grade" />
		 valores
	</logic:iterate>
		</logic:present>