<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	
	<h:outputText value="#{CurricularCourseManagement.degreeCurricularPlan.name}" style="font-style: italic"/>
	<h2><h:outputText value="#{bolonhaBundle['associateCurricularCourse']}"/></h2>

	<br/>
	<h:form>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'"/>
		
		<h:outputText style="font-weight: bold" value="#{bolonhaBundle['chooseCurricularCourseAndContext']}: "/>
		<h:messages infoClass="infoMsg" errorClass="error" layout="table" globalOnly="true"/>
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['curricularCourse']}: " />
			<h:selectOneMenu value="#{CurricularCourseManagement.curricularCourseID}">
				<f:selectItems value="#{CurricularCourseManagement.curricularCourses}"/>
			</h:selectOneMenu>
			
			<h:outputText value="#{bolonhaBundle['courseGroup']}: "/>
			<h:selectOneMenu value="#{CurricularCourseManagement.courseGroupID}">
				<f:selectItems value="#{CurricularCourseManagement.courseGroups}" />
			</h:selectOneMenu>
		
			<h:outputText value="#{bolonhaBundle['curricularYear']}: " />
			<h:selectOneMenu value="#{CurricularCourseManagement.curricularYearID}">
				<f:selectItems value="#{CurricularCourseManagement.curricularYears}" />
			</h:selectOneMenu>
		
			<h:outputText value="#{bolonhaBundle['semester']}: " />
			<h:selectOneMenu value="#{CurricularCourseManagement.curricularSemesterID}">
				<f:selectItems value="#{CurricularCourseManagement.curricularSemesters}" />
			</h:selectOneMenu>
		</h:panelGrid>
		<br/><br/><hr>
		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['associate']}"
			action="#{CurricularCourseManagement.addContext}"/>	
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['back']}"
			action="buildCurricularPlan"/>
	</h:form>
</ft:tilesView>