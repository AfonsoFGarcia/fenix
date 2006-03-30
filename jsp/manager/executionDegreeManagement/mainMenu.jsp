<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<div style="font-size: 1.20em;">
	<p><strong>&raquo; 
		<html:link module="/manager" page="/index.do">
			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.mainPage" />
		</html:link>
	</strong></p>
	
	<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegreeManagement"/></h3>
	
	<p><strong>&raquo; 
  	<html:link module="/manager" page="/degree/chooseDegreeType.faces">
  		Cria��o de Cursos de Execu��o
  	</html:link>
	</strong></p>

</div>