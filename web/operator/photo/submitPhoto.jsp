<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert definition="df.layout.two-column" beanName="" flush="true">
  <tiles:put name="title" value="Operador" />
  <tiles:put name="serviceName" value="Operador" />
  <tiles:put name="navLocal" value="/operator/operatorMainMenu.jsp" />
  <tiles:put name="navGeral" value="/operator/commonNavGeralOperator.jsp" />
  <tiles:put name="body-context" value="/commons/blank.jsp"/>  
  <tiles:put name="body" value="/operator/photo/submitPhoto_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>

