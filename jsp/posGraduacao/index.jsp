<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value=".IST - Secretaria de P�s-Gradua��o" />
  <tiles:put name="serviceName" value="Secretaria de P�s-Gradua��o" />
  <tiles:put name="navLocal" value="/posGraduacao/mainMenu.jsp" />
  <tiles:put name="navGeral" value="/posGraduacao/globalNav.jsp" />
  <tiles:put name="body" value="/posGraduacao/welcomeScreen.jsp" />
  <tiles:put name="footer" value="/posGraduacao/copyrightDefault.jsp" />
</tiles:insert>
