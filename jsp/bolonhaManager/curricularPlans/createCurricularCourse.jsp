<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>

	<h:outputText value="#{CurricularCourseManagement.degreeCurricularPlan.name}" style="font-style: italic"/>
	<h2><h:outputText value="#{bolonhaBundle['createCurricularCourse']}"/></h2>
	<h:messages infoClass="infoMsg" errorClass="error" layout="table" globalOnly="true"/>
	<h:form>
		<fc:viewState binding="#{CurricularCourseManagement.viewState}" />
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'"/>
		<h:outputText style="font-weight: bold" value="#{bolonhaBundle['competenceCourse']}: "/>
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['department']}: " />
			<h:selectOneMenu value="#{CurricularCourseManagement.departmentUnitID}" onchange="this.form.submit();"
					valueChangeListener="#{CurricularCourseManagement.resetCompetenceCourse}">
				<f:selectItems value="#{CurricularCourseManagement.departmentUnits}"/>
			</h:selectOneMenu>
			
			<h:outputText value="#{bolonhaBundle['competenceCourse']}: " />
			<h:selectOneMenu value="#{CurricularCourseManagement.competenceCourseID}" onchange="this.form.submit();">
				<f:selectItems value="#{CurricularCourseManagement.competenceCourses}"/>
			</h:selectOneMenu>
		</h:panelGrid>

		<h:panelGroup rendered="#{(!empty CurricularCourseManagement.competenceCourseID) && (CurricularCourseManagement.competenceCourseID != 0) }">
			<h:outputLink value="../competenceCourses/showCompetenceCourse.faces" target="_blank">
				<h:outputText value="#{bolonhaBundle['showPage']} #{bolonhaBundle['competenceCourse']}"/>
				<f:param name="competenceCourseID" value="#{CurricularCourseManagement.competenceCourseID}"/>
			</h:outputLink>
			<h:outputText value=" (#{bolonhaBundle['newPage']})<br/>" escape="false"/>
		</h:panelGroup>
		<br/>
		<h:outputText style="font-weight: bold" value="#{bolonhaBundle['curricularCourseInformation']}: <br/>"  escape="false"/>
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['weight']}: "/>
			<h:inputText id="weight" maxlength="5" size="5" value="#{CurricularCourseManagement.weight}" />
			
			<h:outputText value="#{bolonhaBundle['prerequisites']}: "/>
			<h:inputTextarea id="prerequisites" cols="80" rows="5" value="#{CurricularCourseManagement.prerequisites}"/>
		</h:panelGrid>

		<h:outputText style="font-weight: bold" value="#{bolonhaBundle['english']}: <br/>"  escape="false"/>
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['prerequisitesEn']}: "/>
			<h:inputTextarea id="prerequisitesEn" cols="80" rows="5" value="#{CurricularCourseManagement.prerequisitesEn}"/>
		</h:panelGrid>
		<br/>		
		<h:outputText style="font-weight: bold" value="#{bolonhaBundle['context']}: "/><br/>
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
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
		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['create']}"
			action="#{CurricularCourseManagement.createCurricularCourse}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}"
			action="buildCurricularPlan"/>
	</h:form>
</ft:tilesView>