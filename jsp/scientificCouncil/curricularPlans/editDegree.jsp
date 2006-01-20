<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ScientificCouncilResources" var="scouncilBundle"/>
	
	<h:outputText value="<i>#{ScientificCouncilDegreeManagement.degree.nome}" escape="false"/>
	<h:outputText value=" (#{ScientificCouncilDegreeManagement.degree.acronym})</i>" escape="false"/>
	<h:outputFormat value="<h2>#{scouncilBundle['edit.param']}</h2>" escape="false">
		<f:param value="#{scouncilBundle['degree']}" />
	</h:outputFormat>
	<h:form>
		<h:outputText escape="false" value="<input id='degreeId' name='degreeId' type='hidden' value='#{ScientificCouncilDegreeManagement.degreeId}'/>"/>

		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<h4 class='first'>#{scouncilBundle['degree.data']}:</h4><br/>" escape="false"/>
		<h:outputText styleClass="error0" rendered="#{!empty ScientificCouncilDegreeManagement.errorMessage}"
			value="#{ScientificCouncilDegreeManagement.errorMessage}<br/>" escape="false"/>

		<h:outputText value="<br/><fieldset class='lfloat'>" escape="false"/>

		<h:outputText value="<p><label>#{scouncilBundle['name']} (pt):</label>" escape="false"/>
		<h:panelGroup>
			<h:inputText id="name" value="#{ScientificCouncilDegreeManagement.name}" required="true" maxlength="100" size="60"/>
			<h:message for="name" errorClass="error0" rendered="#{empty ScientificCouncilDegreeManagement.errorMessage}"/>
		</h:panelGroup>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label>#{scouncilBundle['name']} (en):</label>" escape="false"/>
		<h:panelGroup>
			<h:inputText id="nameEn" value="#{ScientificCouncilDegreeManagement.nameEn}" required="true" maxlength="100" size="60"/>
			<h:message for="nameEn" errorClass="error0" rendered="#{empty ScientificCouncilDegreeManagement.errorMessage}"/>
		</h:panelGroup>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label>#{scouncilBundle['acronym']}:</label>" escape="false"/>
		<h:panelGroup>
			<h:inputText id="acronym" value="#{ScientificCouncilDegreeManagement.acronym}" required="true" maxlength="6" size="6"/>
			<h:message for="acronym" errorClass="error0" rendered="#{empty ScientificCouncilDegreeManagement.errorMessage}"/>
		</h:panelGroup>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label>#{scouncilBundle['degreeType']}:</label>" escape="false"/>
		<h:panelGroup>
			<h:selectOneMenu id="bolonhaDegreeType" value="#{ScientificCouncilDegreeManagement.bolonhaDegreeType}">
				<f:selectItems value="#{ScientificCouncilDegreeManagement.bolonhaDegreeTypes}" />
			</h:selectOneMenu>
			<h:message for="bolonhaDegreeType" errorClass="error0" rendered="#{empty ScientificCouncilDegreeManagement.errorMessage}"/>
		</h:panelGroup>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label>#{scouncilBundle['ectsCredits']}:</label>" escape="false"/>
		<h:panelGroup>
			<h:inputText id="ectsCredits" value="#{ScientificCouncilDegreeManagement.ectsCredits}" required="true" maxlength="3" size="2"/>
			<h:message for="ectsCredits" errorClass="error0" rendered="#{empty ScientificCouncilDegreeManagement.errorMessage}"/>
		</h:panelGroup>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="</fieldset></div>" escape="false"/>

		<br/><br/><hr/>
		<h:commandButton styleClass="inputbutton" value="#{scouncilBundle['edit']}"
			action="#{ScientificCouncilDegreeManagement.editDegree}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{scouncilBundle['cancel']}"
			action="curricularPlansManagement"/>
	</h:form>

</ft:tilesView>
