<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/layout/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value="Portal Gestor de Projectos"/>
  <tiles:put name="serviceName" value="Portal Gestor de Projectos"/>
  <tiles:put name="navLocal" value="/projectsManagement/navBar.jsp"/>
  <tiles:put name="navGeral" value="/commons/commonGeneralNavigationBar.jsp"/>
  <tiles:put name="body-context" value="/commons/blank.jsp"/>  
  <tiles:put name="body" value="/projectsManagement/firstPage.jsp"/>
  <tiles:put name="footer" value="/copyright.jsp"/>
</tiles:insert>
