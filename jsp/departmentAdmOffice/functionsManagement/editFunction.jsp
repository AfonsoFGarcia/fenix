<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="departmentAdmOffice.masterPage" attributeName="body-inline">

	<f:loadBundle basename="resources/DepartmentAdmOfficeResources" var="bundle"/>
	
	<h:form>
	
		<h:inputHidden binding="#{functionsManagementBackingBean.personIDHidden}"/>
    	<h:inputHidden binding="#{functionsManagementBackingBean.personFunctionIDHidden}"/>
		
		<h:outputText value="<H2>#{bundle['label.edit.function']}</H2>" escape="false"/>		
		<h:outputText value="<br/>" escape="false" />
		
		<h:panelGrid columns="2">
			<h:outputText value="<b>#{bundle['label.name']}</b>: " escape="false"/>		
			<h:outputText value="#{functionsManagementBackingBean.person.nome}" escape="false"/>									
				
			<h:outputText value="<b>#{bundle['label.search.unit']}:</b>" escape="false"/>	
			<h:panelGroup>
				<h:outputText value="#{functionsManagementBackingBean.unit.name}"/>	
				<h:outputText value=" - " rendered="#{!empty functionsManagementBackingBean.unit.topUnits}"/>	
				<fc:dataRepeater value="#{functionsManagementBackingBean.unit.topUnits}" var="topUnit">
					<h:outputText value="#{topUnit.name}<br/>" escape="false" />
				</fc:dataRepeater>
			</h:panelGroup>					
		</h:panelGrid>	
						
		<h:outputText styleClass="error" rendered="#{!empty functionsManagementBackingBean.errorMessage}"
				value="#{bundle[functionsManagementBackingBean.errorMessage]}"/>
	
		<h:outputText value="<br/>" escape="false" />
	
		<h:panelGrid columns="2" styleClass="infoop">			
			<h:outputText value="<b>#{bundle['label.search.function']}:</b>" escape="false"/>			
			<fc:selectOneMenu value="#{functionsManagementBackingBean.functionID}">
				<f:selectItems value="#{functionsManagementBackingBean.validFunctions}"/>				
			</fc:selectOneMenu>
						
			<h:outputText value="<b>#{bundle['label.credits']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText id="credits" required="true" size="5" maxlength="5" value="#{functionsManagementBackingBean.credits}"/>
				<h:message for="credits" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="<b>#{bundle['label.begin.date']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText id="beginDate" required="true" size="10" value="#{functionsManagementBackingBean.beginDate}">							
					<fc:dateValidator format="dd/MM/yyyy" strict="true"/>
				</h:inputText>	
				<h:outputText value="#{bundle['label.date.format']}"/>
				<h:message for="beginDate" styleClass="error"/>				
			</h:panelGroup>			
						
			<h:outputText value="<b>#{bundle['label.end.date']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText id="endDate" required="true" size="10" value="#{functionsManagementBackingBean.endDate}">
					<fc:dateValidator format="dd/MM/yyyy" strict="true"/>
				</h:inputText>				
				<h:outputText value="#{bundle['label.date.format']}"/>
				<h:message for="endDate" styleClass="error"/>
			</h:panelGroup>
		</h:panelGrid>				
		
		<h:outputText value="<br/>" escape="false" />	
		<h:panelGrid columns="2">
			<h:commandButton action="#{functionsManagementBackingBean.editFunction}" value="#{bundle['label.next']}" styleClass="inputbutton"/>				
			<h:commandButton action="alterFunction" immediate="true" value="#{bundle['button.choose.new.person']}" styleClass="inputbutton"/>								
		</h:panelGrid>	

	</h:form>

</ft:tilesView>