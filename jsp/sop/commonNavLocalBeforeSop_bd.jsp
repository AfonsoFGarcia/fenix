<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<p class="invisible"><strong>&raquo; Gest&atilde;o de Hor�rios</strong></p>
<ul>
  <li><html:link page="/prepararEscolherContexto.do"><bean:message key="link.schedules.chooseContext"/></html:link></li>
</ul>

<p class="invisible"><strong>&raquo; Listagens de Hor�rios</strong></p>
<ul>
  <li><html:link page="/viewAllClassesSchedules.do"><bean:message key="link.schedules.listAllByClass"/></html:link></li>
  <li><html:link page="/viewAllRoomsSchedules.do"><bean:message key="link.schedules.listAllByRoom"/></html:link></li>
</ul>

