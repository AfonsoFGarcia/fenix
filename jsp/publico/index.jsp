<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/publicGesDisLayout_2col.jsp" flush="true">
  <tiles:put name="title" value=".Instituto Superior T&eacute;cnico" /> 
  <tiles:put name="serviceName" value="Instituto Superior T&eacute;cnico" />
  <tiles:put name="navGeral" value="" />
 <tiles:put name="navbarGeral" value="/publico/commonNavLocalPubWithExecutionPeriodSelection.jsp" />
  <tiles:put name="body" value="/publico/index_bd.jsp" />
  <tiles:put name="footer" value="/publico/commonFooterPub.jsp" />
</tiles:insert>
