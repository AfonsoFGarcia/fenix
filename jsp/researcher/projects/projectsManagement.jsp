<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		
  	<h2 id='pageTitle'/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.projectsManagement.title"/> </h2>
		
		
		<fr:view name="projects" layout="tabular-list" >
			<fr:layout>
				<fr:property name="subLayout" value="values"/>
				<fr:property name="subSchema" value="project.summary"/>
			
				<fr:property name="link(view)" value="/projects/viewProject.do?method=prepare"/>
				<fr:property name="param(view)" value="idInternal/projectId"/>
				<fr:property name="key(view)" value="researcher.project.projectsManagement.view"/>
				<fr:property name="bundle(view)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(view)" value="1"/>

				<fr:property name="link(delete)" value="/projects/projectsManagement.do?method=delete"/>
				<fr:property name="param(delete)" value="idInternal/projectId"/>
				<fr:property name="key(delete)" value="researcher.project.projectsManagement.delete"/>
				<fr:property name="bundle(delete)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(delete)" value="2"/>
			</fr:layout>
		</fr:view>
		
	<html:link page="/projects/createProject.do?method=prepareCreateProject"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.projectsManagement.createProject" /></html:link>
</logic:present>
		
<br/>