<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayoutPub_2col.jsp" flush="true">
  <tiles:put name="title" value=".Instituto Superior T�cnico" />
  <tiles:put name="serviceName" value="Instituto Superior T�cnico" />
  <tiles:put name="navGeral" value="" />
  <tiles:put name="navLocal" value="/publico/commonNavLocalPub.jsp" />
  <tiles:put name="body" value="/publico/viewClassTimeTable_bd.jsp" />
  <tiles:put name="footer" value="/publico/commonFooterPub.jsp" />
</tiles:insert>