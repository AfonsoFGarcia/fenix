<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayoutStudent_2col.jsp" flush="true">
  <tiles:put name="title" value=".IST - Candidatos de Mestrado" />
  <tiles:put name="serviceName" value="Portal de Candidatos a P�s-Gradua��es" />
  <tiles:put name="navLocal" value="/candidato/navigation.jsp" />
  <tiles:put name="navGeral" value="/candidato/globalNav.jsp" />
  <tiles:put name="body" value="/candidato/changePasswordSuccess_body.jsp" />
  <tiles:put name="footer" value="/candidato/copyrightDefault.jsp" />
</tiles:insert>

