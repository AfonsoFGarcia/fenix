<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<p class="invisible">
	<strong>
		&raquo; Gest&atilde;o de Hor�rios
	</strong>
</p>
<ul>
  <li>
  	<html:link page="<%= "/prepararEscolherContexto.do?"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID") %>">
  		<bean:message key="link.schedules.chooseContext"/>
  	</html:link>
  </li>
</ul>

<p class="invisible">
	<strong>
		&raquo; Listagens de Hor�rios
	</strong>
</p>
<ul>
  <li>
  	<html:link page="<%= "/viewAllClassesSchedulesDA.do?method=choose&amp;"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID") %>">
  		<bean:message key="link.schedules.listAllByClass"/>
  	</html:link>
  </li>
  <li>
  	<html:link page="<%= "/viewAllRoomsSchedulesDA.do?method=choose&amp;"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID") %>">
  		<bean:message key="link.schedules.listAllByRoom"/>
  	</html:link>
  </li>
</ul>