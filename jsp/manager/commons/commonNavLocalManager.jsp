<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<center>
	<img alt=""  src="<%= request.getContextPath() %>/images/logo-fenix.gif" width="100" height="100"/>
</center>

<br />
<strong>Gest�o de Execu��es</strong>
<p><strong>&raquo; 
		<html:link page="/readDegrees.do">
			<bean:message key="label.manager.readDegrees" />
		</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link page="/manageExecutionPeriods.do?method=prepare">
		Gest&atilde;o de Periodos Execu��o
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link page="/executionCourseManagement.do?method=firstPage">
		<bean:message key="label.manager.executionCourseManagement" />
	</html:link>
</strong></p>

<br />
<strong>Gest�o de Pessoal</strong>
<p><strong>&raquo; 
	<html:link page="/teachersManagement.do?method=firstPage">
		<bean:message key="link.manager.teachersManagement" />
	</html:link>
</strong></p>

<p><strong>&raquo;
	<html:link page="/personManagement.do?method=firstPage">
		<bean:message key="label.manager.personManagement" />
	</html:link>
</strong></p>

<br />
<strong><bean:message key="label.generateFiles"/></strong>
<p><strong>&raquo;
	<html:link page="/generateFiles.do?method=firstPage">
		<bean:message key="label.generateFiles"/>
	</html:link>
</strong></p>

<br />
<strong>Gest�o do Sistema</strong>
<p><strong>&raquo; 

	<html:link page="/manageCache.do?method=prepare">
		Gest&atilde;o da Cache
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link page="/manageAdvisories.do?method=prepare">
		Gest&atilde;o da Avisos
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link page="/monitorServices.do?method=monitor">
		Monitoriza��o de Servi�os
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link page="/monitorRequestLogs.do?method=listFiles">
		<bean:message key="manager.monitor.requests.title"/>
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link page="/monitorUsers.do?method=monitor">
		Monitoriza��o de Utilizadores
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link page="/monitorSystem.do?method=monitor">
		Informa��es do Sistema
	</html:link>
</strong></p>