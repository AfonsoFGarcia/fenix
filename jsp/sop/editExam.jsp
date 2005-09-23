<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value="SOP" />
  <tiles:put name="serviceName" value="SOP - Servi�o de Organiza��o Pedag�gica" />
  <tiles:put name="navGeral" value="/sop/commonNavGeralSopSchedule.jsp" />
  <tiles:put name="navLocal" value="/sop/commonNavLocalExamsSop.jsp" />
  <tiles:put name="body-context" value="/commons/blank.jsp"/>  
  <tiles:put name="body" value="/sop/editExam_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
<%--
  <tiles:put name="context" value="/commons/contextExecutionCourseAndExecutionDegreeAndCurricularYear.jsp" />
  <tiles:put name="context" value="/commons/contextExecutionDegreeAndCurricularYears.jsp" />
--%>
  <tiles:put name="context" value="/commons/contextDateAndTimeAndCurricularYearsExecutionCourseAndExecutionDegreeAndCurricularYear.jsp" />
</tiles:insert>