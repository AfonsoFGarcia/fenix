<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>

	<h:outputText value="<em>#{CurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[CurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>	
	<h:outputText value="<h2>#{bolonhaBundle['createCurricularCourse']}</h2>" escape="false"/>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	<h:form>
		<fc:viewState binding="#{CurricularCourseManagement.viewState}"/>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input id='curricularYearID' name='curricularYearID' type='hidden' value='#{CurricularCourseManagement.curricularYearID}'/>"/>
		<h:outputText escape="false" value="<input id='curricularSemesterID' name='curricularSemesterID' type='hidden' value='#{CurricularCourseManagement.curricularSemesterID}'/>"/>
		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<h4 class='first'>#{bolonhaBundle['competenceCourse']}:</h4><br/>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['department']}:</label>" escape="false"/>
		<fc:selectOneMenu value="#{CurricularCourseManagement.departmentUnitID}" onchange="this.form.submit();"
				valueChangeListener="#{CurricularCourseManagement.resetCompetenceCourse}">
			<f:selectItems value="#{CurricularCourseManagement.departmentUnits}"/>
		</fc:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>			
		<h:outputText value="<p><label>#{bolonhaBundle['competenceCourse']}:</label>" escape="false"/>
		<fc:selectOneMenu value="#{CurricularCourseManagement.competenceCourseID}" onchange="this.form.submit();">
			<f:selectItems value="#{CurricularCourseManagement.competenceCourses}"/>
		</fc:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>		
		<h:panelGroup rendered="#{(!empty CurricularCourseManagement.competenceCourseID) && (CurricularCourseManagement.competenceCourseID != 0) }">
			<h:outputText value="<p class='mtop1'><label class='lempty'>.</label>" escape="false"/>
			<h:outputLink value="../competenceCourses/showCompetenceCourse.faces" target="_blank">
				<h:outputText value="#{bolonhaBundle['showPage']} #{bolonhaBundle['competenceCourse']}"/>
				<f:param name="competenceCourseID" value="#{CurricularCourseManagement.competenceCourseID}"/>
			</h:outputLink>
			<h:outputText value=" (#{bolonhaBundle['newPage']})" escape="false"/>
			<h:outputText value="</p>" escape="false"/>
		</h:panelGroup>
		<h:outputText value="</fieldset></div>" escape="false"/>

		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<h4 class='first'>#{bolonhaBundle['curricularCourseInformation']}:</h4><br/>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['weight']}:</label>" escape="false"/>
		<h:inputText id="weight" maxlength="5" size="5" value="#{CurricularCourseManagement.weight}" />
		<h:message for="weight" styleClass="error0"/>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['prerequisites']}:</label>" escape="false"/>
		<h:inputTextarea id="prerequisites" cols="80" rows="5" value="#{CurricularCourseManagement.prerequisites}"/>
		<h:outputText value="</p></fieldset>" escape="false"/>
		
		<h:outputText value="<h4 class='first'>#{bolonhaBundle['english']}:</h4><br/>"  escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['prerequisitesEn']}:</label>" escape="false"/>
		<h:inputTextarea id="prerequisitesEn" cols="80" rows="5" value="#{CurricularCourseManagement.prerequisitesEn}"/>
		<h:outputText value="</p></fieldset></div>" escape="false"/>

		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<h4 class='first'>#{bolonhaBundle['context']}:</h4><br/>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['courseGroup']}:</label>" escape="false"/>
		<fc:selectOneMenu value="#{CurricularCourseManagement.courseGroupID}">
			<f:selectItems value="#{CurricularCourseManagement.courseGroups}" />
		</fc:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['curricularYear']}:</label>" escape="false"/>
		<h:selectOneMenu value="#{CurricularCourseManagement.curricularYearID}">
			<f:selectItems value="#{CurricularCourseManagement.curricularYears}" />
		</h:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
			
		<h:outputText value="<p><label>#{bolonhaBundle['semester']}:</label>" escape="false"/>
		<fc:selectOneMenu value="#{CurricularCourseManagement.curricularSemesterID}">
			<f:selectItems value="#{CurricularCourseManagement.curricularSemesters}" />
		</fc:selectOneMenu>
		<h:outputText value="</p></fieldset></div>" escape="false"/>		

		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['create']}"
			action="#{CurricularCourseManagement.createCurricularCourse}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}"
			action="buildCurricularPlan"/>
	</h:form>
</ft:tilesView>