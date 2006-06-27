<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="newParticipationsSchema" value="result.participations" type="java.lang.String"/>
	<logic:present name="participationsSchema">
		<bean:define id="newParticipationsSchema" name="participationsSchema" type="java.lang.String"/>
	</logic:present>

	<bean:define id="newSchemaInternalPersonCreation" value="resultParticipation.internalPerson.creation" type="java.lang.String"/>
	<logic:present name="schemaInternalPersonCreation">
		<bean:define id="newSchemaInternalPersonCreation" name="schemaInternalPersonCreation" type="java.lang.String"/>
	</logic:present>
		<bean:define id="newSchemaExternalPersonSimpleCreation" value="resultParticipation.externalPerson.simpleCreation" type="java.lang.String"/>
	<logic:present name="schemaExternalPersonSimpleCreation">
		<bean:define id="newSchemaExternalPersonSimpleCreation" name="schemaExternalPersonSimpleCreation" type="java.lang.String"/>
	</logic:present>
		<bean:define id="newSchemaExternalPersonFullCreation" value="resultParticipation.externalPerson.fullCreation" type="java.lang.String"/>
	<logic:present name="schemaExternalPersonFullCreation">
		<bean:define id="newSchemaExternalPersonFullCreation" name="schemaExternalPersonFullCreation" type="java.lang.String"/>
	</logic:present>
	
	
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.superUseCaseTitle"/></em>
	
	<%-- TITLE --%>		
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.participations.useCaseTitle"/></h2>
	
	<%-- Participation List --%>

	<bean:define id="resultId" name="result" property="idInternal" />
	<fr:edit name="result" property="resultParticipations" schema="<%=newParticipationsSchema%>" layout="tabular-editable"
		action="<%= "/result/resultParticipationManagement.do?method=prepareEditParticipation&resultId=" + resultId %>">
		<fr:layout>
			<fr:property name="sortBy" value="personOrder"/>
			<fr:property name="link(moveDown)" value="<%= "/result/resultParticipationManagement.do?method=changePersonsOrder" +
				        								  "&resultId=" + resultId.toString() +
				        								  "&offset=1" %>"/>
			<fr:property name="param(moveDown)" value="idInternal/oid"/>
			<fr:property name="key(moveDown)" value="link.moveDown"/>
			<fr:property name="bundle(moveDown)" value="RESEARCHER_RESOURCES"/>
			<fr:property name="order(moveDown)" value="1"/>
			<fr:property name="excludedFromLast(moveDown)" value="true"/>
			
							
			<fr:property name="link(moveUp)" value="<%= "/result/resultParticipationManagement.do?method=changePersonsOrder" + 
			        									"&resultId=" + resultId.toString() +
			        									"&offset=-1" %>"/>
			<fr:property name="param(moveUp)" value="idInternal/oid"/>
			<fr:property name="key(moveUp)" value="link.moveUp"/>
			<fr:property name="bundle(moveUp)" value="RESEARCHER_RESOURCES"/>
			<fr:property name="order(moveUp)" value="2"/>
			<fr:property name="excludedFromFirst(moveUp)" value="true"/>

			<fr:property name="link(removeParticipation)" value="<%="/result/resultParticipationManagement.do?method=removeParticipation&resultId=" + resultId %>"/>
			<fr:property name="param(removeParticipation)" value="idInternal/participationId"/>
			<fr:property name="key(removeParticipation)" value="link.remove"/>
			<fr:property name="bundle(removeParticipation)" value="RESEARCHER_RESOURCES"/>
			<fr:property name="order(removeParticipation)" value="3"/>								
			
		</fr:layout>
 	</fr:edit>

	
	<logic:notPresent name="external">
		<h3>
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.editResult.participation.addNewPerson"/>
			<html:link page="<%="/result/resultParticipationManagement.do?method=prepareEditParticipationWithSimpleBean&external=false&resultId=" + resultId %>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.participants.internal" />
			</html:link>				
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.participants.or" />
			<html:link page="<%="/result/resultParticipationManagement.do?method=prepareEditParticipationWithSimpleBean&external=true&resultId=" + resultId %>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.participants.external" />
			</html:link>				
		</h3>
	</logic:notPresent>
	<logic:present name="external">
		<logic:equal name="external" value="false">
			<h3>
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.editResult.participation.addNewInternalPerson"/>
			</h3>
			<fr:edit id="simpleBean" name="simpleBean" action="<%="/result/resultParticipationManagement.do?method=createParticipationInternalPerson&resultId=" + resultId %>" schema="<%=newSchemaInternalPersonCreation%>">
				<fr:destination name="invalid" path="<%="/result/resultParticipationManagement.do?method=prepareEditParticipationWithSimpleBean&external=false&resultId=" + resultId %>"/>	
				<fr:destination name="cancel" path="<%="/result/resultParticipationManagement.do?method=prepareEditParticipation&resultId=" + resultId %>"/>	
			</fr:edit>
		</logic:equal>
		<logic:equal name="external" value="true">
			<h3>
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.editResult.participation.addNewExternalPerson"/>			
			</h3>
			<logic:present name="simpleBean">
				<fr:edit id="simpleBean" name="simpleBean" action="<%="/result/resultParticipationManagement.do?method=createParticipationExternalPerson&external=true&resultId=" + resultId %>" schema="<%=newSchemaExternalPersonSimpleCreation%>">
					<fr:destination name="invalid" path="<%="/result/resultParticipationManagement.do?method=prepareEditParticipationWithSimpleBean&external=true&resultId=" + resultId %>"/>	
					<fr:destination name="cancel" path="<%="/result/resultParticipationManagement.do?method=prepareEditParticipation&resultId=" + resultId %>"/>	
				</fr:edit>						
			</logic:present>
			<logic:present name="fullBean">
				<fr:edit id="fullBean" name="fullBean" action="<%="/result/resultParticipationManagement.do?method=createParticipationExternalPerson&resultId=" + resultId %>" schema="<%=newSchemaExternalPersonFullCreation%>">
					<fr:destination name="invalid" path="<%="/result/resultParticipationManagement.do?method=prepareEditParticipationWithFullBean&external=true&resultId=" + resultId %>"/>
					<fr:destination name="cancel" path="<%="/result/resultParticipationManagement.do?method=prepareEditParticipation&resultId=" + resultId %>"/>	
				</fr:edit>
			</logic:present>
		</logic:equal>
	</logic:present>
	
	<br/>
	<html:link page="<%="/result/resultParticipationManagement.do?method=backToResult&resultId=" + resultId %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.goBackToView" />
	</html:link>
	
</logic:present>
<br/>