<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/layout/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value="SOP" />
  <tiles:put name="serviceName" value="SOP - Servi�o de Organiza��o Pedag�gica" />
  <tiles:put name="navGeral" value="/sop/commonNavGeralSopExecutionCourse.jsp" />
  <tiles:put name="navLocal" value="/sop/commonNavLocalExecutionCourses.jsp" />
  <tiles:put name="body-context" value="/commons/blank.jsp" />  
  <tiles:put name="body" value="/sop/listExecutionCourseGroupings_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>