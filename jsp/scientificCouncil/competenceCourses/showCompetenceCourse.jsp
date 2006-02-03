<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>
<style>
.temp1 {
clear: both;
}
.temp1 li span {
float: left; 
width: 100px;
padding-right: 10px;
}
</style>		
	<h:outputText value="<em>#{scouncilBundle['competenceCourse']}</em>" escape="false" />
	<h:outputText value="<h2>#{CompetenceCourseManagement.competenceCourse.name}</h2>" escape="false"/>

	<h:outputText value="<ul class='nobullet padding1 indent0 mtop3'>" escape="false"/>
	<h:outputText value="<li><strong>#{scouncilBundle['department']}: </strong>" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.personDepartment.realName}</li>" escape="false"/>
	<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.unit.parentUnits}" var="scientificAreaUnit">
		<h:outputText value="<li><strong>#{scouncilBundle['area']}: </strong>" escape="false"/>
		<h:outputText value="#{scientificAreaUnit.name} > #{CompetenceCourseManagement.competenceCourse.unit.name}</li>" escape="false"/>
	</fc:dataRepeater>		
	<h:outputText value="</ul>" escape="false"/>
		
	<h:outputText value="<p class='mtop2 mbottom0'><strong>#{scouncilBundle['activeCurricularPlans']}: </strong></p>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}">
		<h:outputText value="(#{scouncilBundle['noCurricularCourses']})"/>
	</h:panelGroup>
	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}">
		<h:outputText value="<ul class='mtop0 mbottom3'>" escape="false"/>
		<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}" var="curricularCourse">			
			<h:outputText value="<li>" escape="false"/>
			<h:outputLink value="../curricularPlans/viewCurricularPlan.faces" target="_blank">
				<h:outputText value="#{curricularCourse.parentDegreeCurricularPlan.name}" escape="false"/>
				<f:param name="action" value="close"/>
				<f:param name="dcpId" value="#{curricularCourse.parentDegreeCurricularPlan.idInternal}"/>
			</h:outputLink>
			<h:outputText value=" > "/>
			<h:outputLink value="../curricularPlans/viewCurricularCourse.faces" target="_blank">
				<h:outputText value="#{curricularCourse.name}" escape="false"/>
				<f:param name="action" value="close"/>
				<f:param name="curricularCourseID" value="#{curricularCourse.idInternal}"/>
			</h:outputLink>
			<h:outputText value="</li>" escape="false"/>
		</fc:dataRepeater>
		<h:outputText value="</ul>" escape="false"/>
	</h:panelGroup>	

	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<p><strong><u>#{scouncilBundle['state']}: </strong>" escape="false"/>
	<h:outputText value="#{enumerationBundle[CompetenceCourseManagement.competenceCourse.curricularStage.name]}</u></p>" escape="false"/>
	<h:outputText value="<br/>" escape="false"/>
	<h:outputText value="<ul class='nobullet padding1 indent0 mbottom0'>" escape="false"/>	
	<h:outputText value="<li><strong>#{scouncilBundle['name']} (pt): </strong>" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.name}</li>" escape="false"/>
	<h:outputText value="<li><strong>#{scouncilBundle['nameEn']} (en): </strong>" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.nameEn}</li>" escape="false" />
	<h:outputText value="<li><strong>#{scouncilBundle['acronym']}: </strong>" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.acronym}</li>" escape="false"/>	
	<h:outputText value="<li><strong>#{scouncilBundle['type']}: </strong>" escape="false"/>
	<h:outputText value="#{scouncilBundle['basic']}</li>" rendered="#{CompetenceCourseManagement.competenceCourse.basic}" escape="false"/>
	<h:outputText value="#{scouncilBundle['nonBasic']}</li>" rendered="#{!CompetenceCourseManagement.competenceCourse.basic}" escape="false"/>
	<h:outputText value="</ul></div>" escape="false"/>
	
	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<ul class='nobullet padding1 indent0 mbottom0'>" escape="false"/>
	<h:outputText value="<li><strong>#{scouncilBundle['regime']}: </strong>" escape="false"/>
	<h:outputText value="#{enumerationBundle[CompetenceCourseManagement.competenceCourse.regime.name]}</li>" escape="false"/>	
	<h:outputText value="<li><strong>#{scouncilBundle['lessonHours']}: </strong>" escape="false"/>	
	<fc:dataRepeater value="#{CompetenceCourseManagement.sortedCompetenceCourseLoads}" var="competenceCourseLoad" rowCountVar="numberOfElements">
		<h:outputText value="<p class='mbotton0'><em>#{competenceCourseLoad.order}� #{scouncilBundle['semester']}</em></p>" escape="false"
			rendered="#{CompetenceCourseManagement.competenceCourse.regime.name == 'ANUAL' && numberOfElements == 2}"/>
		
		<h:outputText value="<ul class='mvert0'>" escape="false"/>
		<h:outputText value="<li>#{scouncilBundle['theoreticalLesson']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.theoreticalHours} h/#{scouncilBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText value="<li>#{scouncilBundle['problemsLesson']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.problemsHours} h/#{scouncilBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText value="<li>#{scouncilBundle['laboratorialLesson']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.laboratorialHours} h/#{scouncilBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText value="<li>#{scouncilBundle['seminary']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.seminaryHours} h/#{scouncilBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText value="<li>#{scouncilBundle['fieldWork']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.fieldWorkHours} h/#{scouncilBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText value="<li>#{scouncilBundle['trainingPeriod']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.trainingPeriodHours} h/#{scouncilBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText value="<li>#{scouncilBundle['tutorialOrientation']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.tutorialOrientationHours} h/#{scouncilBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText value="<li>#{scouncilBundle['autonomousWork']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.autonomousWorkHours} h/<b>#{scouncilBundle['lowerCase.semester']}</b></li>" escape="false"/>

		<h:outputText value="<li><strong>#{scouncilBundle['ectsCredits']}: "escape="false"/>
		<h:outputText value="#{competenceCourseLoad.ectsCredits}</strong></li>" escape="false"/>
		<h:outputText value="</ul>" escape="false"/>
	</fc:dataRepeater>	
	<h:outputText value="</li>" escape="false"/>
	<h:outputText value="</ul></div>" escape="false"/>

	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<p class='mbottom0'><em>#{bolonhaBundle['portuguese']}: </em></p>" escape="false"/>
	<h:outputText value="<table class='showinfo1 highlight2'>" escape="false"/>
	<h:outputText value="<tr><th>#{bolonhaBundle['objectives']}:</th>" escape="false"/>
	<fc:extendedOutputText value="<td>#{CompetenceCourseManagement.competenceCourse.objectives}</td></tr>" escape="false" linebreak="true"/>
	<h:outputText value="<tr><th>#{bolonhaBundle['program']}:</th>" escape="false"/>
	<fc:extendedOutputText value="<td>#{CompetenceCourseManagement.competenceCourse.program}</td></tr>" escape="false" linebreak="true"/>
	<h:outputText value="<tr><th>#{bolonhaBundle['evaluationMethod']}:</th>" escape="false"/>
	<fc:extendedOutputText value="<td>#{CompetenceCourseManagement.competenceCourse.evaluationMethod}</td></tr>" escape="false" linebreak="true"/>
	<h:outputText value="</table>" escape="false"/>
	<h:outputText value="</div>" escape="false"/>
	
	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<p class='mbottom0'><em>#{bolonhaBundle['english']}:</em></p>" escape="false"/>
	<h:outputText value="<table class='showinfo1 highlight2'>" escape="false"/>
	<h:outputText value="<tr><th>#{bolonhaBundle['objectivesEn']}:</th>" escape="false"/>
	<fc:extendedOutputText value="<td>#{CompetenceCourseManagement.competenceCourse.objectivesEn}</td></tr>" escape="false" linebreak="true"/>
	<h:outputText value="<tr><th>#{bolonhaBundle['programEn']}:</th>" escape="false"/>
	<fc:extendedOutputText value="<td>#{CompetenceCourseManagement.competenceCourse.programEn}</td></tr>" escape="false" linebreak="true"/>
	<h:outputText value="<tr><th>#{bolonhaBundle['evaluationMethodEn']}:</th>" escape="false"/>
	<fc:extendedOutputText value="<td>#{CompetenceCourseManagement.competenceCourse.evaluationMethodEn}</td></tr>" escape="false" linebreak="true"/>	
	<h:outputText value="</table>" escape="false"/>
	<h:outputText value="</div>" escape="false"/>
	
	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<h3 class='mbottom0'>#{bolonhaBundle['bibliographicReference']}</h3>" escape="false"/>	
	<h:outputText value="<p><b>#{enumerationBundle['MAIN']}</b></p>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.mainBibliographicReferences}">
		<h:outputText value="<em>#{bolonhaBundle['noBibliographicReferences']}</em><br/>" escape="false"/>
	</h:panelGroup>	
	<fc:dataRepeater value="#{CompetenceCourseManagement.mainBibliographicReferences}" var="bibliographicReference" rendered="#{!empty CompetenceCourseManagement.mainBibliographicReferences}">
		<h:panelGroup rendered="#{bibliographicReference.type.name == 'MAIN'}">
			<h:outputText value="<ul class='nobullet temp1 mbottom2'>" escape="false"/>					
			<h:outputText value="<li><span>#{bolonhaBundle['title']}:</span>" escape="false"/>
			<h:outputText value="<a href='#{bibliographicReference.url}'>#{bibliographicReference.title}</a></li>" escape="false"/>
			
			<h:outputText value="<li><span>#{bolonhaBundle['author']}:</span>" escape="false"/>
			<h:outputText value="<em>#{bibliographicReference.authors}</em></li>" escape="false"/>
			
			<h:outputText value="<li><span>#{bolonhaBundle['year']}:</span>" escape="false"/>
			<h:outputText value="#{bibliographicReference.year}</li>" escape="false"/>
			
			<h:outputText value="<li><span>#{bolonhaBundle['reference']}:</span>" escape="false"/>
			<h:outputText value="#{bibliographicReference.reference}</li>" escape="false"/>
			
			<h:outputText value="</ul>" escape="false"/>
		</h:panelGroup>
	</fc:dataRepeater>
	
	<h:outputText value="<p><b>#{enumerationBundle['SECONDARY']}</b></p>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.secondaryBibliographicReferences}">
		<h:outputText value="<em>#{bolonhaBundle['noBibliographicReferences']}</em><br/>" escape="false"/>
	</h:panelGroup>	
	<fc:dataRepeater value="#{CompetenceCourseManagement.secondaryBibliographicReferences}" var="bibliographicReference" rendered="#{!empty CompetenceCourseManagement.secondaryBibliographicReferences}">
		<h:panelGroup rendered="#{bibliographicReference.type.name == 'SECONDARY'}">
			<h:outputText value="<ul class='nobullet temp1 mbottom2'>" escape="false"/>					
			<h:outputText value="<li><span>#{bolonhaBundle['title']}:</span>" escape="false"/>
			<h:outputText value="<a href='#{bibliographicReference.url}'>#{bibliographicReference.title}</a></li>" escape="false"/>
				
			<h:outputText value="<li><span>#{bolonhaBundle['author']}:</span>" escape="false"/>
			<h:outputText value="<em>#{bibliographicReference.authors}</em></li>" escape="false"/>
			
			<h:outputText value="<li><span>#{bolonhaBundle['year']}:</span>" escape="false"/>
			<h:outputText value="#{bibliographicReference.year}</li>" escape="false"/>
			
			<h:outputText value="<li><span>#{bolonhaBundle['reference']}:</span>" escape="false"/>
			<h:outputText value="#{bibliographicReference.reference}</li>" escape="false"/>
			
			<h:outputText value="</ul>" escape="false"/>
		</h:panelGroup>
	</fc:dataRepeater>
	<h:outputText value="</div>" escape="false"/>
	
	<h:form>
		<h:outputText escape="false" value="<input id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourse.idInternal}'/>"/>
		<h:outputText escape="false" value="<input id='action' name='action' type='hidden' value='#{CompetenceCourseManagement.action}'/>"/>
		<h:outputText escape="false" value="<input id='selectedDepartmentUnitID' name='selectedDepartmentUnitID' type='hidden' value='#{CompetenceCourseManagement.selectedDepartmentUnitID}'/>"/>
		<h:panelGroup rendered="#{!empty CompetenceCourseManagement.action}">
			<h:commandButton immediate="true" styleClass="inputbutton" action="competenceCoursesManagement" value="#{scouncilBundle['back']}" />
		</h:panelGroup>
		
		<h:panelGroup rendered="#{empty CompetenceCourseManagement.action}">
			<h:commandButton immediate="true" styleClass="inputbutton" onclick="window.close()" value="#{scouncilBundle['close']}" />
		</h:panelGroup>
	</h:form>
</ft:tilesView>