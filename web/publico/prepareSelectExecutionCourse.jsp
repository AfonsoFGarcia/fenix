<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/layout/publicGesDisLayout_2col.jsp" flush="true">
  <tiles:put name="navGeral" value="/commons/blank.jsp" />
  <tiles:put name="navbarGeral" value="/publico/commonNavLocalPub.jsp" />
  <tiles:put name="body" value="/publico/prepareSelectExecutionCourse_bd.jsp" />
  <tiles:put name="footer" value="/commons/blank.jsp" />
</tiles:insert>