<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<ul class="treemenu">
	<li><a href="http://www.ist.utl.pt/pt/informacoes/">Informa��o</a></li>
  <li><a href="http://www.ist.utl.pt/pt/estrutura_interna/">Estrutura</a></li>
  <li><html:link page="/showServices.do">Servi�os</html:link></li>
  <li><a href="http://www.ist.utl.pt/pt/ensino/">Ensino</a></li>	
  <li><a href="http://www.ist.utl.pt/pt/investigacao/">I &amp; D</a></li>
</ul>

<ul class="treemenu">
	<li><a href="http://gape.ist.utl.pt/acesso/">Ingressos</a></li>
  <li><a href="http://alumni.ist.utl.pt/">Sa�das</a></li>
</ul>

<ul class="treemenu">
	<li><a href="http://istpress.ist.utl.pt/">IST Press</a></li>
  <li><a href="http://www.ist.utl.pt/pt/ligacao_sociedade/">Sociedade &amp; IST</a></li>
  <li><a href="http://www.ist.utl.pt/pt/viver_ist/">Viver no IST</a></li>
  <li><a href="http://www.utl.pt/">Universidade</a></li>
</ul>