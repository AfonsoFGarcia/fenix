<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		

	<bean:define id="projectId" name="selectedProject" property="idInternal" />
	<bean:define id="projectName" name="selectedProject" property="title.content" />

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.projectsManagement.superUseCaseTitle"/></em>
		
	<%-- TITLE --%>
	<h2/>
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.events.useCaseTitle"/> 
	  	<html:link page="<%="/projects/viewProject.do?method=prepare&projectId="+projectId%>">
			<%= projectName %>
		</html:link>
	</h2>  	
	
	<%-- LIST OF EXISTING ASSOCIATIONS --%>	
	<logic:notEmpty name="eventAssociations">
		<fr:edit id="associationsTable" name="eventAssociations" layout="tabular-editable" schema="projectEventAssociations.edit-role"
			action="<%= "/projects/editProject.do?method=prepareEditEventAssociations&projectId=" + projectId %>">
			<fr:layout>
				<fr:property name="link(remove)" value="<%= "/projects/editProject.do?method=removeEventAssociation&projectId=" + projectId %>"/>
				<fr:property name="param(remove)" value="idInternal/associationId"/>
				<fr:property name="key(remove)" value="researcher.project.editProject.events.remove"/>
				<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
			</fr:layout>	
			<fr:destination name="cancel" path="<%="/projects/viewProject.do?method=prepare&projectId="+projectId%>"/>				
		</fr:edit>
	</logic:notEmpty>
	<br/>

	<%-- CREATION OF A NEW ASSOCIATION --%>
	<h3/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.events.addNewEventAssociation"/> </h3>
	<logic:present name="simpleBean">
		<fr:edit id="simpleBean" name="simpleBean" action="<%="/projects/editProject.do?method=createEventAssociationSimple&projectId="+projectId%>" schema="projectEventAssociation.simpleCreation">
			<fr:destination name="invalid" path="<%="/projects/editProject.do?method=prepareEditEventAssociations&projectId="+projectId%>"/>			
			<fr:destination name="cancel" path="<%="/projects/viewProject.do?method=prepare&projectId="+projectId%>"/>				
		</fr:edit>
	</logic:present>
	<logic:present name="fullBean">
		<fr:edit id="fullBean" name="fullBean" action="<%="/projects/editProject.do?method=createEventAssociationFull&projectId="+projectId%>" schema="projectEventAssociation.fullCreation">
			<fr:destination name="invalid" path="<%="/projects/editProject.do?method=createEventAssociationSimple&projectId="+projectId%>"/>		
			<fr:destination name="cancel" path="<%="/projects/viewProject.do?method=prepare&projectId="+projectId%>"/>	
		</fr:edit>
	</logic:present>
  	<br/>
  	<html:link page="<%="/projects/viewProject.do?method=prepare&projectId="+projectId%>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.goBackToView" />
	</html:link>
</logic:present>
<br/>