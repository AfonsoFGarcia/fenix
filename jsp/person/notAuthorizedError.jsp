<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value=".IST - &Aacute;rea de Pessoa" />
  <tiles:put name="serviceName" value="&Aacute;rea de Pessoa" />
  <tiles:put name="navGeral" value="/person/commonNavGeralPerson.jsp" />
  <tiles:put name="navLocal" value="" />
  <tiles:put name="body-context" value=""/>  
  <tiles:put name="body" value="/person/notAuthorizedError_bd.jsp" />
  <tiles:put name="footer" value="/person/commonFooter.jsp" />
</tiles:insert>