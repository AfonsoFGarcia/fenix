<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<tiles:insert page="/teacherLayout_2col.jsp" flush="true">
  <tiles:put name="serviceName" value="Portal Docente" />
  <tiles:put name="institutionName" value="Instituto Superior T&eacute;cnico" />
   <tiles:put name="executionCourseName" value="/teacher/executionCourseName.jsp" />
  <tiles:put name="body" value="/teacher/bibliographyManagement_bd.jsp" />
  <tiles:put name="navLocal" value="/teacher/navbar.jsp" type="page"/>
  <tiles:put name="footer" value="/sop/commonFooterSop.jsp" />
</tiles:insert>