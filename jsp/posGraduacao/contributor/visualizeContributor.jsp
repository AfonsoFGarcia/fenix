<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value=".IST - Secretaria de P�s-Gradua��o" />
  <tiles:put name="serviceName" value="Secretaria de P�s-Gradua��o" />
  <tiles:put name="navLocal" value="/posGraduacao/contributor/contributorMenu.jsp" />
  <tiles:put name="navGeral" value="/posGraduacao/commonNavGeralPosGraduacao.jsp" />
  <tiles:put name="body-context" value=""/>  
  <tiles:put name="body" value="/posGraduacao/contributor/visualizeContributor_bd.jsp" />
  <tiles:put name="footer" value="/posGraduacao/copyrightDefault.jsp" />
</tiles:insert>