<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value=".IST - &Aacute;rea Pessoal" />
  <tiles:put name="serviceName" value="&Aacute;rea Pessoal" />
  <tiles:put name="navGeral" value="" />
  <tiles:put name="navLocal" value="" />
  <tiles:put name="body" value="/person/error_bd.jsp" />
  <tiles:put name="footer" value="/person/commonFooter.jsp" />
</tiles:insert>