<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<ul>
  <li class="navheader">Gest&atilde;o de Hor�rios</li>
  <li>
  	<html:link page="<%= "/chooseContext.do?method=prepare&amp;"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID") %>">
  		<bean:message key="link.schedules.chooseContext"/>
  	</html:link>
  </li>
  <li class="navheader">Listagens de Hor�rios</li>
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