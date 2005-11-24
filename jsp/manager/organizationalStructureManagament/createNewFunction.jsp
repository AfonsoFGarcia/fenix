<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	
	<f:loadBundle basename="ServidorApresentacao/ManagerResources" var="bundle"/>
		
	<h:form>	
		<h:inputHidden binding="#{organizationalStructureBackingBean.unitIDHidden}"/>

		<h:outputText value="<h2>#{bundle['link.new.function2']}</h2><br/>" escape="false" />
	
		<h:panelGrid styleClass="infoselected" columns="2">
			<h:outputText value="<b>#{bundle['message.name']}</b>" escape="false"/>		
			<h:outputText value="#{organizationalStructureBackingBean.unit.name}" escape="false"/>												
		</h:panelGrid>	

		<h:outputText value="<br/>" escape="false" />	
	
		<h:outputText styleClass="error" rendered="#{!empty organizationalStructureBackingBean.errorMessage}"
				value="#{bundle[organizationalStructureBackingBean.errorMessage]}<br/>" escape="false"/>

		<h:panelGrid columns="2" styleClass="infoop">		
		
			<h:outputText value="<b>#{bundle['title.FunctionName']}:</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText id="name" required="true" size="30" value="#{organizationalStructureBackingBean.functionName}"/>
				<h:message for="name" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="<b>#{bundle['message.initialDate']}:</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText id="beginDate" required="true" size="10" value="#{organizationalStructureBackingBean.functionBeginDate}">
					<fc:regexValidator regex="([1-9]|0[1-9]|[12][0-9]|3[01])[/]([1-9]|0[1-9]|1[012])[/](19|20)\d\d"/>
				</h:inputText>
				<h:outputText value="#{bundle['date.format']}"/>
				<h:message for="beginDate" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="<b>#{bundle['message.endDate']}:</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText id="endDate" size="10" value="#{organizationalStructureBackingBean.functionEndDate}"/>
				<h:outputText value="#{bundle['date.format']}"/>				
			</h:panelGroup>
		
			<h:outputText value="<b>#{bundle['message.uniType']}</b>" escape="false"/>
			<fc:selectOneMenu value="#{organizationalStructureBackingBean.functionTypeName}">
				<f:selectItems value="#{organizationalStructureBackingBean.validFunctionType}"/>				
			</fc:selectOneMenu>		
		
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />	
		<h:panelGrid columns="2">
			<h:commandButton action="#{organizationalStructureBackingBean.createFunction}" value="#{bundle['link.new.function2']}" styleClass="inputbutton"/>				
			<h:commandButton action="backToUnitDetails" immediate="true" value="#{bundle['label.return']}" styleClass="inputbutton"/>								
		</h:panelGrid>		
				
	</h:form>
	
</ft:tilesView>