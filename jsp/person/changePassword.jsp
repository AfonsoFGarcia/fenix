<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value=".IST - &Aacute;rea de Pessoa" />
  <tiles:put name="serviceName" value="&Aacute;rea de Pessoa" />
  <tiles:put name="navLocal" value="/person/mainMenu.jsp" />
  <tiles:put name="navGeral" value="/person/globalNav.jsp" />
  <tiles:put name="body" value="/person/changePassword_body.jsp" />
  <tiles:put name="footer" value="/person/copyrightDefault.jsp" />
</tiles:insert>