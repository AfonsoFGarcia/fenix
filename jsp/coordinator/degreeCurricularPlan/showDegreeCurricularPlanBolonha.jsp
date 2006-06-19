<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView locale="<%=request.getAttribute(org.apache.struts.Globals.LOCALE_KEY).toString()%>" definition="coordinatorDegreeCurricularPlan" attributeName="body-inline">
	<style>@import "<%= request.getContextPath() %>/CSS/transitional.css";</style>
	<f:loadBundle basename="resources/ApplicationResources" var="applicationBundle"/>
	<f:loadBundle basename="resources/PublicDegreeInformation" var="publicDegreeInfoBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<h2>#{bolonhaBundle['curricularPlan']}</h2>" escape="false"/>

	<h:panelGroup rendered="#{CurricularCourseManagement.degreeCurricularPlan.draft}">
		<h:outputText value="<p><em>#{applicationBundle['error.curricularPlanInDraftStage']}</em><p>" escape="false"/>
	</h:panelGroup>

	<h:form>

		<h:panelGroup rendered="#{!CurricularCourseManagement.degreeCurricularPlan.draft}">
	
			<h:panelGroup rendered="#{empty CurricularCourseManagement.degreeCurricularPlan.executionDegrees}">
				<h:outputText value="<p><em>#{applicationBundle['error.curricularPlanHasNoExecutionDegrees']}</em><p>" escape="false"/>
			</h:panelGroup>
		
			<h:panelGroup rendered="#{!empty CurricularCourseManagement.degreeCurricularPlan.executionDegrees}">
				<h:outputText value="<div class='simpleblock4'>" escape="false"/>
					<h:outputText value="<p>#{publicDegreeInfoBundle['public.degree.information.label.executionYear']}: " escape="false"/>
					<h:selectOneMenu value="#{CurricularCourseManagement.executionYearID}" onchange="this.form.submit();">
						<f:selectItems value="#{CurricularCourseManagement.executionYearItems}" />
					</h:selectOneMenu>
					<h:outputText value="</p>" escape="false"/>
					<h:panelGroup rendered="#{!empty CurricularCourseManagement.degreeCurricularPlan.degreeStructure.childs}">
						<h:outputText value="<p>#{bolonhaBundle['view.structure.organized.by']}: " escape="false"/>
						<h:outputLink value="showDegreeCurricularPlanBolonha.faces" rendered="#{CurricularCourseManagement.organizeBy == 'years'}">
							<h:outputText value="#{bolonhaBundle['groups']}" />
							<f:param name="degreeID" value="#{CurricularCourseManagement.degreeCurricularPlan.degree.idInternal}"/>
							<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
							<f:param name="organizeBy" value="groups"/>
							<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
							<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
							<f:param name="action" value="#{CurricularCourseManagement.action}"/>
						</h:outputLink>
						<h:outputText value="<span class='highlight3'>#{bolonhaBundle['groups']}</span>" rendered="#{CurricularCourseManagement.organizeBy == 'groups'}" escape="false"/>
						<h:outputText value=" , " escape="false"/>
						<h:outputLink value="showDegreeCurricularPlanBolonha.faces" rendered="#{CurricularCourseManagement.organizeBy == 'groups'}">
							<h:outputText value="#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}" />
							<f:param name="degreeID" value="#{CurricularCourseManagement.degreeCurricularPlan.degree.idInternal}"/>
							<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
							<f:param name="organizeBy" value="years"/>
							<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
							<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
							<f:param name="action" value="#{CurricularCourseManagement.action}"/>
						</h:outputLink>
						<h:outputText value="<span class='highlight3'>#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}</span>" rendered="#{CurricularCourseManagement.organizeBy == 'years'}" escape="false"/>
						<h:outputText value="</p>" escape="false"/>
					</h:panelGroup>
			
					<h:panelGroup rendered="#{!empty CurricularCourseManagement.degreeCurricularPlan.root.childContexts}">	
						<h:outputText value="<p>" escape="false"/>
						<h:outputText value="#{bolonhaBundle['curricularRules']}: " escape="false"/>
						<h:outputLink value="showDegreeCurricularPlanBolonha.faces" rendered="#{CurricularCourseManagement.showRules == 'false'}">
							<h:outputText value="#{bolonhaBundle['show']}" />
							<f:param name="degreeID" value="#{CurricularCourseManagement.degreeCurricularPlan.degree.idInternal}"/>
							<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
							<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
							<f:param name="showRules" value="true"/>
							<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
							<f:param name="action" value="#{CurricularCourseManagement.action}"/>		
						</h:outputLink>
						<h:outputText value="<span class='highlight3'>#{bolonhaBundle['show']}</span>" rendered="#{CurricularCourseManagement.showRules == 'true'}" escape="false"/>
						<h:outputText value=" , " escape="false"/>
						<h:outputLink value="showDegreeCurricularPlanBolonha.faces" rendered="#{CurricularCourseManagement.showRules == 'true'}">
							<h:outputText value="#{bolonhaBundle['hide']}" />
							<f:param name="degreeID" value="#{CurricularCourseManagement.degreeCurricularPlan.degree.idInternal}"/>
							<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
							<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
							<f:param name="showRules" value="false"/>
							<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
							<f:param name="action" value="#{CurricularCourseManagement.action}"/>
						</h:outputLink>
						<h:outputText value="<span class='highlight3'>#{bolonhaBundle['hide']}</span>" rendered="#{CurricularCourseManagement.showRules == 'false'}" escape="false"/>
						<h:outputText value="</p>" escape="false"/>
					</h:panelGroup>
				
					<h:panelGroup rendered="#{!empty CurricularCourseManagement.degreeCurricularPlan.root.childContexts && CurricularCourseManagement.organizeBy == 'groups'}">
						<h:outputText value="<p>" escape="false"/>
						<h:outputText value="#{bolonhaBundle['curricularCourses']}: " escape="false"/>
						<h:outputLink value="showDegreeCurricularPlanBolonha.faces" rendered="#{CurricularCourseManagement.hideCourses == 'true'}">
							<h:outputText value="#{bolonhaBundle['show']}" />
							<f:param name="degreeID" value="#{CurricularCourseManagement.degreeCurricularPlan.degree.idInternal}"/>
							<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
							<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
							<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
							<f:param name="hideCourses" value="false"/>
							<f:param name="action" value="#{CurricularCourseManagement.action}"/>
						</h:outputLink>
						<h:outputText value="<span class='highlight3'>#{bolonhaBundle['show']}</span>" rendered="#{CurricularCourseManagement.hideCourses == 'false'}" escape="false"/>
						<h:outputText value=" , " escape="false"/>
						<h:outputLink value="showDegreeCurricularPlanBolonha.faces" rendered="#{CurricularCourseManagement.hideCourses == 'false'}">
							<h:outputText value="#{bolonhaBundle['hide']}" />
							<f:param name="degreeID" value="#{CurricularCourseManagement.degreeCurricularPlan.degree.idInternal}"/>
							<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
							<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
							<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
							<f:param name="hideCourses" value="true"/>
							<f:param name="action" value="#{CurricularCourseManagement.action}"/>
						</h:outputLink>
						<h:outputText value="<span class='highlight3'>#{bolonhaBundle['hide']}</span>" rendered="#{CurricularCourseManagement.hideCourses == 'true'}" escape="false"/>
						<h:outputText value="</p>" escape="false"/>
					</h:panelGroup>
			
				<h:outputText value="</div>" escape="false"/>
				
				<h:outputText value="<br/>" escape="false"/>
				<fc:degreeCurricularPlanRender 
					dcp="#{CurricularCourseManagement.degreeCurricularPlan}" 
					organizeBy="<%=request.getParameter("organizeBy")%>"
					showRules="<%=request.getParameter("showRules")%>"
					hideCourses="<%=request.getParameter("hideCourses")%>"
					executionYear="#{CurricularCourseManagement.executionYear}"/>
			</h:panelGroup>
	
		</h:panelGroup>

		<h:outputText escape="false" value="<input id='degreeID' name='degreeID' type='hidden' value='#{DegreeManagementBackingBean.degreeId}'/>"/>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'/>"/>
 		<h:outputText escape="false" value="<input id='executionYearID' name='executionYearID' type='hidden' value='#{CurricularCourseManagement.executionYearID}'/>"/>
		<h:outputText escape="false" value="<input id='organizeBy' name='organizeBy' type='hidden' value='#{CurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input id='showRules' name='showRules' type='hidden' value='#{CurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input id='hideCourses' name='hideCourses' type='hidden' value='#{CurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input id='action' name='action' type='hidden' value='#{CurricularCourseManagement.action}'/>"/>
	</h:form>

</ft:tilesView>
