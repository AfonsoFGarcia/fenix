<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value=".IST - �rea Pessol" />
  <tiles:put name="serviceName" value="�rea Pessoal" />
  <tiles:put name="navLocal" value="/person/mainMenu.jsp" />
  <tiles:put name="navGeral" value="/person/commonNavGeralPerson.jsp" />
  <tiles:put name="body-context" value=""/>  
  <tiles:put name="body" value="/person/welcomeScreen.jsp" />
  <tiles:put name="footer" value="/person/commonFooter.jsp" />
</tiles:insert>

