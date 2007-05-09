<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/layout/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value="SOP" />
  <tiles:put name="serviceName" value="Servi�o de Organiza��o Pedag�gica" />
  <tiles:put name="navGeral" value="/commons/commonGeneralNavigationBar.jsp" />
  <tiles:put name="navLocal" value="/sop/commonNavLocalSalasSop.jsp" />
  <tiles:put name="body-context" value="/commons/blank.jsp"/>  
  <tiles:put name="body" value="/sop/pesquisarSalas_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>
