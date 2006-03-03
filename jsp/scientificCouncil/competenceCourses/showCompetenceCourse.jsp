<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
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
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.departmentUnit.department.realName}</li>" escape="false"/>
	<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.unit.parentUnits}" var="scientificAreaUnit">
		<h:outputText value="<li><strong>#{scouncilBundle['area']}: </strong>" escape="false"/>
		<h:outputText value="#{scientificAreaUnit.name} > #{CompetenceCourseManagement.competenceCourse.unit.name}</li>" escape="false"/>
	</fc:dataRepeater>		
	<h:outputText value="</ul>" escape="false"/>
		
	<h:outputText value="<p class='mtop2 mbottom0'><strong>#{scouncilBundle['activeCurricularPlans']}: </strong></p>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}">
		<h:outputText value="<i>#{scouncilBundle['noCurricularCourses']}</i>" escape="false"/>
	</h:panelGroup>
	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}">
		<h:outputText value="<ul class='mtop0 mbottom3'>" escape="false"/>
		<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}" var="curricularCourse">			
			<h:outputText value="<li>" escape="false"/>
			<h:outputLink value="../curricularPlans/viewCurricularPlan.faces" target="_blank">
				<h:outputText value="#{curricularCourse.parentDegreeCurricularPlan.name}" escape="false"/>
				<f:param name="action" value="close"/>
				<f:param name="organizeBy" value="groups"/>
				<f:param name="showRules" value="false"/>
				<f:param name="hideCourses" value="false"/>
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
	<h:outputText value="<p><strong>#{scouncilBundle['state']}: </strong>" escape="false"/>
	<h:outputText value="<span class='highlight1'>#{enumerationBundle[CompetenceCourseManagement.competenceCourse.curricularStage.name]}</span></p>" escape="false"/>
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
		<h:outputText value="#{competenceCourseLoad.autonomousWorkHours} <b>h/#{scouncilBundle['lowerCase.semester']}</li></b>" escape="false"/>

		<h:outputText value="<li><strong>#{scouncilBundle['ectsCredits']}: "escape="false"/>
		<h:outputText value="#{competenceCourseLoad.ectsCredits}</strong></li>" escape="false"/>
		<h:outputText value="</ul>" escape="false"/>
	</fc:dataRepeater>	
	<h:outputText value="</li>" escape="false"/>
	<h:outputText value="</ul></div>" escape="false"/>

	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<p class='mbottom0'><em>#{scouncilBundle['portuguese']}: </em></p>" escape="false"/>
	<h:outputText value="<table class='showinfo1 emphasis2'>" escape="false"/>
	<h:outputText value="<tr><th>#{scouncilBundle['objectives']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.competenceCourse.objectives}" linebreak="true"/>
	<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.objectives}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="<tr><th>#{scouncilBundle['program']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.competenceCourse.program}" linebreak="true"/>
	<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.program}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="<tr><th>#{scouncilBundle['evaluationMethod']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.competenceCourse.evaluationMethod}" linebreak="true"/>
	<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.evaluationMethod}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="</table>" escape="false"/>
	
	<h:outputText value="<p class='mbottom0'><em>#{scouncilBundle['english']}: </em></p>" escape="false"/>
	<h:outputText value="<table class='showinfo1 emphasis2'>" escape="false"/>
	<h:outputText value="<tr><th>#{scouncilBundle['objectivesEn']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.competenceCourse.objectivesEn}" linebreak="true"/>
	<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.objectivesEn}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="<tr><th>#{scouncilBundle['programEn']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.competenceCourse.programEn}" linebreak="true"/>
	<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.programEn}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="<tr><th>#{scouncilBundle['evaluationMethodEn']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.competenceCourse.evaluationMethodEn}" linebreak="true"/>	
	<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.evaluationMethodEn}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="</table>" escape="false"/>
	<h:outputText value="</div>" escape="false"/>

	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<h3 class='mbottom0'>#{scouncilBundle['bibliographicReference']}</h3>" escape="false"/>	
	<h:outputText value="<p><b>#{enumerationBundle['MAIN']}</b></p>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.mainBibliographicReferences}">
		<h:outputText value="<i>#{scouncilBundle['noBibliographicReferences']}</i><br/>" escape="false"/>
	</h:panelGroup>	
	<fc:dataRepeater value="#{CompetenceCourseManagement.mainBibliographicReferences}" var="bibliographicReference" rendered="#{!empty CompetenceCourseManagement.mainBibliographicReferences}">
		<h:panelGroup rendered="#{bibliographicReference.type.name == 'MAIN'}">
			<h:outputText value="<ul class='nobullet temp1 mbottom2'>" escape="false"/>					
			<h:outputText value="<li><span>#{scouncilBundle['title']}:</span>" escape="false"/>
			<h:outputText value="<a href='#{bibliographicReference.url}'>#{bibliographicReference.title}</a></li>" rendered="#{bibliographicReference.url != 'http://'}" escape="false"/>
			<h:outputText value="#{bibliographicReference.title}</li>" rendered="#{bibliographicReference.url == 'http://'}" escape="false"/>			
			
			<h:outputText value="<li><span>#{scouncilBundle['author']}:</span>" escape="false"/>
			<h:outputText value="<em>#{bibliographicReference.authors}</em></li>" escape="false"/>
			
			<h:outputText value="<li><span>#{scouncilBundle['year']}:</span>" escape="false"/>
			<h:outputText value="#{bibliographicReference.year}</li>" escape="false"/>
			
			<h:outputText value="<li><span>#{scouncilBundle['reference']}:</span>" escape="false"/>
			<h:outputText value="#{bibliographicReference.reference}</li>" escape="false"/>
			
			<h:outputText value="</ul>" escape="false"/>
		</h:panelGroup>
	</fc:dataRepeater>
	
	<h:outputText value="<p><b>#{enumerationBundle['SECONDARY']}</b></p>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.secondaryBibliographicReferences}">
		<h:outputText value="<i>#{scouncilBundle['noBibliographicReferences']}</i><br/>" escape="false"/>
	</h:panelGroup>	
	<fc:dataRepeater value="#{CompetenceCourseManagement.secondaryBibliographicReferences}" var="bibliographicReference" rendered="#{!empty CompetenceCourseManagement.secondaryBibliographicReferences}">
		<h:panelGroup rendered="#{bibliographicReference.type.name == 'SECONDARY'}">
			<h:outputText value="<ul class='nobullet temp1 mbottom2'>" escape="false"/>					
			<h:outputText value="<li><span>#{scouncilBundle['title']}:</span>" escape="false"/>
			<h:outputText value="<a href='#{bibliographicReference.url}'>#{bibliographicReference.title}</a></li>" rendered="#{bibliographicReference.url != 'http://'}" escape="false"/>
			<h:outputText value="#{bibliographicReference.title}</li>" rendered="#{bibliographicReference.url == 'http://'}" escape="false"/>			
				
			<h:outputText value="<li><span>#{scouncilBundle['author']}:</span>" escape="false"/>
			<h:outputText value="<em>#{bibliographicReference.authors}</em></li>" escape="false"/>
			
			<h:outputText value="<li><span>#{scouncilBundle['year']}:</span>" escape="false"/>
			<h:outputText value="#{bibliographicReference.year}</li>" escape="false"/>
			
			<h:outputText value="<li><span>#{scouncilBundle['reference']}:</span>" escape="false"/>
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