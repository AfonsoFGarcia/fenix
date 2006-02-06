<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<tiles:insert page="/layout/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="serviceName" value="Gest�o WebSite" />
  <tiles:put name="institutionName" value="Instituto Superior T&eacute;cnico" />
   <tiles:put name="navGeral" value="/webSiteManager/commonNavGeral.jsp" />  
  <tiles:put name="body" value="/webSiteManager/listItems_bd.jsp" />
  <tiles:put name="navLocal" value="/webSiteManager/sectionsNavbar.jsp" type="page"/>
  <tiles:put name="footer" value="/sop/commonFooterSop.jsp" />
</tiles:insert>