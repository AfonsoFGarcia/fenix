<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value="�rea de Funcion�rio" />
  <tiles:put name="serviceName" value="�rea de Funcion�rio" />
  <tiles:put name="navLocal" value="/employee/mainMenu.jsp" />
  <tiles:put name="navGeral" value="/employee/commonNavGeralEmployee.jsp" />
  <tiles:put name="body-context" value=""/>  
  <tiles:put name="body" value="/employee/welcomeScreen.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>

