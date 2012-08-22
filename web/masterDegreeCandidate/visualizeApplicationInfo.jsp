<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert definition="df.layout.two-column" beanName="" flush="true">
  <tiles:put name="title" value="Candidatos de Mestrado" />
  <tiles:put name="serviceName" value="Portal de Candidatos a P�s-Gradua��es" />
  <tiles:put name="navLocal" value="/masterDegreeCandidate/navigation.jsp" />
  <tiles:put name="navGeral" value="/masterDegreeCandidate/commonNavGeralCandidate.jsp" />
  <tiles:put name="body" value="/masterDegreeCandidate/visualizeApplicationInfo_body.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>

