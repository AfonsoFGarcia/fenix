<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<tiles:insert page="/layout/teacherLayout_2col.jsp" flush="true">
  <tiles:put name="serviceName" value="Portal Docente" />
  <tiles:put name="institutionName" value="Instituto Superior T&eacute;cnico" />  
  <tiles:put name="navGeral" value="/teacher/commonNavGeralTeacher.jsp" />
  <tiles:put name="executionCourseName" value="/teacher/executionCourseName.jsp" />
  <tiles:put name="body" value="/teacher/createSubSection_bd.jsp" />
  <tiles:put name="navLocal" value="/teacher/sectionsNavbar.jsp" type="page"/>
  <tiles:put name="footer" value="/sop/commonFooterSop.jsp" />
</tiles:insert>