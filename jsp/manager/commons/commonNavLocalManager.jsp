<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<center>
	<img alt=""  src="<%= request.getContextPath() %>/images/logo-fenix.gif" width="100" height="100"/>
</center>
<br />
<strong>Mensagens e Avisos</strong>
<p><strong>&raquo; 
	<html:link module="/manager" page="/advisoriesManagement/listCurrentAdvisories.faces">
		Gest�o de Avisos
	</html:link>
</strong></p>
<p><strong>&raquo; 
	<html:link module="/manager" page="/sendMail.faces">
		Envio de E-mails
	</html:link>
</strong></p>
<br />
<strong><bean:message bundle="MANAGER_RESOURCES" key="title.manager.organizationalStructureManagement"/></strong>
<p><strong>&raquo; 
	<html:link module="/manager" page="/organizationalStructureManagament/listAllUnits.faces">
		<bean:message bundle="MANAGER_RESOURCES" key="link.manager.organizationalStructureManagement" />
	</html:link>
</strong></p>

<br />
<strong>Gest�o de Execu��es</strong>
<p><strong>&raquo; 
		<html:link module="/manager" module="/manager" page="/readDegrees.do">
			<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.readDegrees" />
		</html:link>
</strong></p>
<p><strong>&raquo; 
	  	<html:link module="/manager" module="/manager" page="/curricularPlans/chooseCurricularPlan.faces">
	  		Consulta de Curr�culo
	  	</html:link>  
</strong></p>

<p><strong>&raquo; 
	  	<html:link module="/manager" module="/manager" page="/degree/chooseDegreeType.faces">
	  		Cria��o de Cursos de Execu��o
	  	</html:link>  
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/manageExecutionPeriods.do?method=prepare">
		Gest�o de Periodos Execu��o
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/executionCourseManagement.do?method=firstPage">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement" />
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/competenceCourseManagement.do?method=prepare">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.competence.course.management" />
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/manageEnrolementPeriods.do?method=prepare">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="title.manage.enrolement.period" />
	</html:link>
</strong></p>

<br />
<strong>Gest�o de Pessoal</strong>
<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/teachersManagement.do?method=firstPage">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="link.manager.teachersManagement" />
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/studentsManagement.do?method=show">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="link.manager.studentsManagement" />
	</html:link>
</strong></p>

<p><strong>&raquo;
	<html:link module="/manager" module="/manager" page="/personManagement.do?method=firstPage">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.personManagement" />
	</html:link>
</strong></p>

<br />
<strong><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manageFiles"/></strong>
<p><strong>&raquo;
	<html:link module="/manager" module="/manager" page="/generateFiles.do?method=firstPage">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.generateFiles"/>
	</html:link>
</strong></p>
<p><strong>&raquo;
	<html:link module="/manager" module="/manager" page="/uploadFiles.do?method=firstPage">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.uploadFiles"/>
	</html:link>
</strong></p>
<br />

<strong><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manageFinance"/></strong>
<p><strong>&raquo;
	<html:link module="/manager" module="/manager" page="/guideManagement.do?method=firstPage">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.guidesManagement"/>
	</html:link>	
</strong></p>
<p><strong>&raquo;
	<html:link module="/manager" module="/manager" page="/gratuity/updateGratuitySituations.faces">
		Actualizar Situa��es de Propina
	</html:link>
</strong></p>
<br/>
<strong>Gest�o de CMS</strong>
<p><strong>&raquo; 
	<html:link module="/cms" action="/userGroupsManagement.do?method=prepare" titleKey="person.userGroupsManagement.label.title">
		<bean:message bundle="MANAGER_RESOURCES" key="link.userGroupsManagement" />
	</html:link>
	</strong></p>
<p><strong>&raquo;
	<html:link module="/cms" action="/cmsConfigurationManagement.do?method=prepare" titleKey="person.userGroupsManagement.label.title">
		<bean:message bundle="MANAGER_RESOURCES" key="link.cmsConfiguration" />
	</html:link>	
</strong></p>
<p><strong>&raquo;
	<html:link module="/cms" action="/executionCourseWebsiteManagement?method=viewAll" >
		<bean:message bundle="MANAGER_RESOURCES" key="cms.executionCourseWebsite.label" />
	</html:link>	
</strong></p>
<br />
<strong>Gest�o de Suport</strong>
<p><strong>&raquo;
	<html:link module="/manager" module="/manager" page="/manageGlossary.do?method=prepare">
		Gest�o de Gloss�rio
	</html:link>
</strong></p>
<p><strong>&raquo;
	<html:link module="/manager" module="/manager" page="/manageFAQs.do?method=prepare">
		Gest�o de FAQ's
	</html:link>
</strong></p>

<br />
<strong>Gest�o do Sistema</strong>
<p><strong>&raquo; 

	<html:link module="/manager" page="/manageCache.do?method=prepare">
		Gest�o da Cache
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/monitorServices.do?method=monitor">
		Monitoriza��o de Servi�os
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/monitorRequestLogs.do?method=listFiles">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="manager.monitor.requests.title"/>
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/monitorUsers.do?method=monitor">
		Monitoriza��o de Utilizadores
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/monitorSystem.do?method=monitor">
		Informa��es do Sistema
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/somePage.faces">
		Java Server Faces Example
	</html:link>
<p><strong>&raquo; 
	<html:link module="/manager" page="/someStrutsPage.do?method=showFirstPage">
		Struts Example
	</html:link>
<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/changePasswordForward.do">
		Kerberos Test
	</html:link>
	
</strong></p>
<br />
<strong>Gest�o de CMS</strong>
<p><strong>&raquo; 
	<html:link module="/manager" module="/cms" action="/userGroupsManagement.do?method=prepare" titleKey="person.userGroupsManagement.label.title">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="link.userGroupsManagement" />
	</html:link>
	</strong></p>
<p><strong>&raquo;
	<html:link module="/manager" module="/cms" action="/cmsConfigurationManagement.do?method=prepare" titleKey="person.userGroupsManagement.label.title">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="link.cmsConfiguration" />
	</html:link>	
</strong></p>
