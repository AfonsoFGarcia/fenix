<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value=".IST - Portal de Coordenador"/>
  <tiles:put name="serviceName" value="Portal de Coordenador"/>
  <tiles:put name="navLocal" value="/coordinator/coordinatorMainMenu.jsp" />
  <tiles:put name="navGeral" value="/coordinator/commonNavGeralCoordinator.jsp"/>
  <tiles:put name="body-context" value=""/>
  <tiles:put name="body" value="/coordinator/courses/showOccupancyLevels_bd.jsp"/>
  <tiles:put name="footer" value="/coordinator/copyrightDefault.jsp"/>
</tiles:insert>
