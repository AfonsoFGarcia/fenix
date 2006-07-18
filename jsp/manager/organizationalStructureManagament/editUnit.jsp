<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	
	<f:loadBundle basename="resources/ManagerResources" var="bundle"/>
		
	<h:form>	
		
		<h:inputHidden binding="#{organizationalStructureBackingBean.unitIDHidden}"/>
		<h:inputHidden binding="#{organizationalStructureBackingBean.chooseUnitIDHidden}"/>
				
		<h:outputText value="<h2>#{bundle['link.editUnit']}</h2><br/>" escape="false" />
	
		<h:outputText styleClass="error" rendered="#{!empty organizationalStructureBackingBean.errorMessage}"
				value="#{bundle[organizationalStructureBackingBean.errorMessage]}<br/>" escape="false"/>
		
		<h:panelGrid columns="2" styleClass="infoop">		
		
			<h:outputText value="<b>#{bundle['message.name']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.unitName']}" id="name" required="true" size="60" value="#{organizationalStructureBackingBean.unitName}"/>
				<h:message for="name" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="<b>#{bundle['message.costCenter']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.unitCostCenter']}" id="costCenter" size="10" value="#{organizationalStructureBackingBean.unitCostCenter}">
					<fc:regexValidator regex="[0-9]*"/>
				</h:inputText>
				<h:message for="costCenter" styleClass="error"/>
			</h:panelGroup>						
			
			<h:outputText value="<b>#{bundle['message.acronym']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.unitAcronym']}" size="10" value="#{organizationalStructureBackingBean.unitAcronym}"/>				
			</h:panelGroup>	
			
			<h:outputText value="<b>#{bundle['message.webAddress']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.unitWebAddress']}" size="30" value="#{organizationalStructureBackingBean.unitWebAddress}"/>				
			</h:panelGroup>									
						
			<h:outputText value="<b>#{bundle['message.initialDate']}:</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.unitBeginDate']}" id="beginDate" required="true" size="10" value="#{organizationalStructureBackingBean.unitBeginDate}">
					<fc:regexValidator regex="([1-9]|0[1-9]|[12][0-9]|3[01])[/]([1-9]|0[1-9]|1[012])[/](19|20)\d\d"/>
				</h:inputText>
				<h:outputText value="#{bundle['date.format']}"/>
				<h:message for="beginDate" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="<b>#{bundle['message.endDate']}:</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.unitEndDate']}" id="endDate" size="10" value="#{organizationalStructureBackingBean.unitEndDate}"/>
				<h:outputText value="#{bundle['date.format']}"/>				
			</h:panelGroup>
		
			<h:outputText value="<b>#{bundle['message.uniType']}</b>" escape="false"/>
			<fc:selectOneMenu value="#{organizationalStructureBackingBean.unitTypeName}">
				<f:selectItems value="#{organizationalStructureBackingBean.validUnitType}"/>				
			</fc:selectOneMenu>
			
			<h:outputText value="<b>#{bundle['message.department']}</b>" escape="false"/>
			<fc:selectOneMenu value="#{organizationalStructureBackingBean.departmentID}">
				<f:selectItems value="#{organizationalStructureBackingBean.departments}"/>				
			</fc:selectOneMenu>
			
			<h:outputText value="<b>#{bundle['message.degree']}</b>" escape="false"/>
			<fc:selectOneMenu value="#{organizationalStructureBackingBean.degreeID}">
				<f:selectItems value="#{organizationalStructureBackingBean.degrees}"/>				
			</fc:selectOneMenu>
			
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />	
		<h:panelGrid columns="2">
			<h:commandButton alt="#{htmlAltBundle['commandButton.submit']}" action="#{organizationalStructureBackingBean.editUnit}" value="#{bundle['button.submit']}" styleClass="inputbutton"/>				
			<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" action="backToUnitDetails" immediate="true" value="#{bundle['label.return']}" styleClass="inputbutton"/>								
		</h:panelGrid>				
				
	</h:form>
	
</ft:tilesView>