<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<style>
	.alignTopRight {
		vertical-align: top;
		text-align: right;
		tex
		
	}
</style>

<ft:tilesView definition="departmentMember.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/DepartmentMemberResources" var="bundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="bundleEnumeration"/>
	
	<h:outputText value="#{bundle['label.personalExpectationsManagement.title']}" escape="false" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
	<h:outputText value="&nbsp;-&nbsp;" escape="false" />
	<h:outputText value="#{teacherExpectationManagement.selectedExecutionYearName}" escape="false" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
		
	
	<h:form onsubmit="updateRTEs();">
		<h:inputHidden binding="#{teacherExpectationManagement.selectedExecutionYearIdHidden}"  />
		<h:inputHidden value="#{teacherExpectationManagement.selectedExecutionYearName}" />

		<!-- Education Expectations Transport Data -->
		<h:inputHidden id="graduations" value="#{teacherExpectationManagement.graduations}" />
		<h:inputHidden id="graduationsDescription" value="#{teacherExpectationManagement.graduationsDescription}" />
		<h:inputHidden id="cientificPosGraduations" value="#{teacherExpectationManagement.cientificPosGraduations}" />
		<h:inputHidden id="cientificPosGraduationsDescription" value="#{teacherExpectationManagement.cientificPosGraduationsDescription}" />
		<h:inputHidden id="professionalPosGraduations" value="#{teacherExpectationManagement.professionalPosGraduations}" />
		<h:inputHidden id="professionalPosGraduationsDescription" value="#{teacherExpectationManagement.professionalPosGraduationsDescription}" />
		<h:inputHidden id="seminaries" value="#{teacherExpectationManagement.seminaries}"  />
		<h:inputHidden id="seminariesDescription" value="#{teacherExpectationManagement.seminariesDescription}" />
		<h:inputHidden id="educationMainFocus" value="#{teacherExpectationManagement.educationMainFocus}" />
							
		<!-- Investigation Expectations -->
		<!-- Research And Dev -->
		<h:outputText value="<h2>#{bundle['label.personalExpectationsManagement.investigation']}</h2>" escape="false" />
		<h:panelGrid columns="2" columnClasses="alignTopRight infocell,infocell">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.researchAndDevProjects']}" />
			<h:panelGroup>
				<h:inputText id="researchAndDevProjects" value="#{teacherExpectationManagement.researchAndDevProjects}" required="true" maxlength="3" size="3" />
				<h:outputText value="&nbsp;&nbsp;" escape="false" />
				<h:message for="researchAndDevProjects" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="#{bundle['label.personalExpectationsManagement.jornalArticlePublications']}" />
			<h:panelGroup>
				<h:inputText id="jornalArticlePublications" value="#{teacherExpectationManagement.jornalArticlePublications}" required="true" maxlength="3" size="3" />
				<h:outputText value="&nbsp;&nbsp;" escape="false" />
				<h:message for="jornalArticlePublications" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="#{bundle['label.personalExpectationsManagement.bookPublications']}" />
			<h:panelGroup>
				<h:inputText id="bookPublications" value="#{teacherExpectationManagement.bookPublications}" required="true" maxlength="3" size="3" />
				<h:outputText value="&nbsp;&nbsp;" escape="false" />
				<h:message for="bookPublications" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="#{bundle['label.personalExpectationsManagement.conferencePublications']}" />
			<h:panelGroup>
				<h:inputText id="conferencePublications" value="#{teacherExpectationManagement.conferencePublications}" required="true" maxlength="3" size="3" />
				<h:outputText value="&nbsp;&nbsp;" escape="false" />
				<h:message for="conferencePublications" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="#{bundle['label.personalExpectationsManagement.technicalReportPublications']}" />
			<h:panelGroup>
				<h:inputText id="technicalReportPublications" value="#{teacherExpectationManagement.technicalReportPublications}" required="true" maxlength="3" size="3" />
				<h:outputText value="&nbsp;&nbsp;" escape="false" />
				<h:message for="technicalReportPublications" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="#{bundle['label.personalExpectationsManagement.patentPublications']}" />
			<h:panelGroup>
				<h:inputText id="patentPublications" value="#{teacherExpectationManagement.patentPublications}" required="true" maxlength="3" size="3" />
				<h:outputText value="&nbsp;&nbsp;" escape="false" />
				<h:message for="patentPublications" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="#{bundle['label.personalExpectationsManagement.otherPublications']}" />
			<h:panelGroup>
				<h:inputText id="otherPublications" value="#{teacherExpectationManagement.otherPublications}" required="true" maxlength="3" size="3" />
				<h:outputText value="&nbsp;&nbsp;" escape="false" />
				<h:message for="otherPublications" styleClass="error"/>
			</h:panelGroup>
		</h:panelGrid>
		<h:panelGrid columns="1">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.otherPublicationsDescription']}" />
			<fc:htmlEditor width="400" height="95" id="otherPublicationsDescription" value="#{teacherExpectationManagement.otherPublicationsDescription}" required="false" showButtons="false"/>
			<h:message for="otherPublicationsDescription" styleClass="error"/>
		</h:panelGrid>
			
		<h:panelGrid columns="1">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" escape="false" />
			<fc:htmlEditor id="researchAndDevMainFocus" value="#{teacherExpectationManagement.researchAndDevMainFocus}" height="300" width="300" showButtons="true" />
		</h:panelGrid>
	
		<h:outputText value="<br/>" escape="false" />
								
		<h:outputText value="<p>#{bundle['label.personalExpectationsManagement.orientation']}</p>" style="font: bold 13px Verdana, Arial, Helvetica, sans-serif;" escape="false" />
		<h:panelGrid columns="2" columnClasses="alignTopRight infocell,infocell" >
				<h:outputText value="#{bundle['label.personalExpectationsManagement.phdOrientations']}" />
				<h:panelGroup>
					<h:inputText id="phdOrientations" value="#{teacherExpectationManagement.phdOrientations}" required="true" maxlength="3" size="3" />
					<h:outputText value="&nbsp;&nbsp;" escape="false"/>
					<h:message for="phdOrientations" styleClass="error"/>
				</h:panelGroup>
				
				<h:outputText value="#{bundle['label.personalExpectationsManagement.masterDegreeOrientations']}" />
				<h:panelGroup>
					<h:inputText id="masterDegreeOrientations" value="#{teacherExpectationManagement.masterDegreeOrientations}" required="true" maxlength="3" size="3" />
					<h:outputText value="&nbsp;&nbsp;" escape="false" />
					<h:message for="masterDegreeOrientations" styleClass="error"/>
				</h:panelGroup>
				
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />
		
		<!-- Orientation -->
		<h:outputText value="#{bundle['label.common.finalDegreeWorks']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{!(empty teacherExpectationManagement.finalDegreeWorks)}">
			<h:dataTable value="#{teacherExpectationManagement.finalDegreeWorks}" var="finalDegreeWork" columnClasses="listClasses" headerClass="listClasses-header" style="width: 70%;">
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.finalDegreeWorkTitle']}" />
					</f:facet>
					<h:outputText value="#{finalDegreeWork.title}" />
				</h:column>
					<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.finalDegreeWorkOrientationPercentage']}" />
					</f:facet>
					<h:outputText value="#{finalDegreeWork.orientatorsCreditsPercentage}" />
				</h:column>
					<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.finalDegreeWorkYear']}" />
					</f:facet>
					<h:outputText value="#{finalDegreeWork.executionDegree.executionYear.year}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.finalDegreeWorkDegree']}" />
					</f:facet>
					<h:outputText value="#{finalDegreeWork.executionDegree.degreeCurricularPlan.degree.sigla}" />
				</h:column>
			</h:dataTable>
		</h:panelGrid>
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{empty teacherExpectationManagement.finalDegreeWorks}">
			<h:outputText value="#{bundle['label.common.noFinalDegreeWorks']}" />
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />
		
		<h:panelGrid columns="4" columnClasses="alignTopRight infocell">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.finalDegreeWorkOrientations']}" />
			<h:panelGroup>
				<h:inputText id="finalDegreeWorkOrientations" value="#{teacherExpectationManagement.finalDegreeWorkOrientations}" required="true" maxlength="3" size="3" />
				<h:outputText value="&nbsp;&nbsp;" escape="false" />
				<h:message for="finalDegreeWorkOrientations" styleClass="error"/>
			</h:panelGroup>
		</h:panelGrid>
		
		
		<h:panelGrid columns="1">
			<h:outputText value="#{bundle['label.personalExpectationsManagement.mainFocus']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" escape="false" />
			<fc:htmlEditor value="#{teacherExpectationManagement.orientationsMainFocus}" height="300" width="300" showButtons="true" />
		</h:panelGrid>

									
		<h:outputText value="<br/><br/>" escape="false" />
		<fc:commandButton action="next" value="#{bundle['button.next']}" styleClass="inputbutton" />
		
	</h:form>
</ft:tilesView>