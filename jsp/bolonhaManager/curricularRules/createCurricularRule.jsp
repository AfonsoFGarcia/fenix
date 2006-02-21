<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<i>#{bolonhaBundle['bolonhaManager']}</i>" escape="false"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['set.param']}</h2>" escape="false">
		<f:param value="#{bolonhaBundle['curricularRule']}"/>
	</h:outputFormat>
	
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

	<h:form>
		<fc:viewState binding="#{CurricularRulesManagement.viewState}"/>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularRulesManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input id='degreeModuleID' name='degreeModuleID' type='hidden' value='#{CurricularRulesManagement.degreeModuleID}'/>"/>
		
		<h:outputText value="<p><strong>#{bolonhaBundle['degreeModule.to.apply.rule']}:</strong> " escape="false"/>
		<h:outputText value="<span class='attention'>#{CurricularRulesManagement.degreeModule.name}</span><p/>" escape="false"/>

		<h:panelGroup rendered="#{!empty CurricularRulesManagement.degreeModule.curricularRules}">
		<h:outputText value="<p><strong>#{bolonhaBundle['existent.curricularRules']}: </strong><br/>" escape="false"/>
			<h:outputText value="<ul>" escape="false"/>
			<fc:dataRepeater value="#{CurricularRulesManagement.rulesLabels}" var="curricularRule">
				<h:outputText value="<li>#{curricularRule}</li>" escape="false"/>
			</fc:dataRepeater>
			<h:outputText value="</ul>" escape="false"/>
		</h:panelGroup>
		
		<h:outputText value="<div class='simpleblock4'> " escape="false"/>
		<h:outputText value="<h4 class='first'>#{bolonhaBundle['new.rule']}</h4>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>

		<h:outputText value="<p><label>#{bolonhaBundle['type.of.rule']}:</label>" escape="false"/>
		<fc:selectOneMenu value="#{CurricularRulesManagement.selectedCurricularRuleType}" onchange="this.form.submit();"
				valueChangeListener="#{CurricularRulesManagement.onChangeCurricularRuleTypeDropDown}">
			<f:selectItems binding="#{CurricularRulesManagement.curricularRuleTypeItems}"/>
		</fc:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
		
		<h:panelGroup rendered="#{CurricularRulesManagement.selectedCurricularRuleType == 'PRECEDENCY_APPROVED_DEGREE_MODULE' 
					|| CurricularRulesManagement.selectedCurricularRuleType == 'PRECEDENCY_ENROLED_DEGREE_MODULE' 
					|| CurricularRulesManagement.selectedCurricularRuleType == 'PRECEDENCY_BETWEEN_DEGREE_MODULES' 
					|| CurricularRulesManagement.selectedCurricularRuleType == 'EXCLUSIVENESS'}">
			<h:outputText value="<p><label>#{bolonhaBundle['curricularCourse']}:</label>" escape="false"/>
			<fc:selectOneMenu value="#{CurricularRulesManagement.selectedDegreeModuleID}">
				<f:selectItems binding="#{CurricularRulesManagement.degreeModuleItems}"/>
			</fc:selectOneMenu>
			<h:outputText value="</p>" escape="false"/> 
		</h:panelGroup>				

		<h:panelGroup rendered="#{CurricularRulesManagement.selectedCurricularRuleType == 'DEGREE_MODULES_SELECTION_LIMIT'}">
			<h:outputText value="<p><label>#{bolonhaBundle['options']}:</label>" escape="false"/>
			<h:outputText value="#{bolonhaBundle['minimum']}: " escape="false"/>
			<h:inputText id="minimumLimit" maxlength="8" size="4" value="#{CurricularRulesManagement.minimumLimit}"/>
			<h:outputText value=" " escape="false"/>
			<h:outputText value="#{bolonhaBundle['maximum']}: " escape="false"/>
			<h:inputText id="maximumLimit" maxlength="8" size="4" value="#{CurricularRulesManagement.maximumLimit}"/>
			<h:outputText value="</p>" escape="false"/>
		</h:panelGroup>
		
		<h:panelGroup rendered="#{CurricularRulesManagement.selectedCurricularRuleType == 'CREDITS_LIMIT'}">
			<h:outputText value="<p><label>#{bolonhaBundle['credits']}:</label>" escape="false"/>
			<h:outputText value="#{bolonhaBundle['minimum']}: " escape="false"/>
			<h:inputText id="minimumCredits" maxlength="8" size="4" value="#{CurricularRulesManagement.minimumCredits}"/>
			<h:outputText value=" " escape="false"/>
			<h:outputText value="#{bolonhaBundle['maximum']}: " escape="false"/>
			<h:inputText id="maximumCredits" maxlength="8" size="4" value="#{CurricularRulesManagement.maximumCredits}"/>
			<h:outputText value="</p>" escape="false"/>
		</h:panelGroup>
		
		<h:panelGroup rendered="#{CurricularRulesManagement.selectedCurricularRuleType == 'PRECEDENCY_BETWEEN_DEGREE_MODULES'}">
			<h:outputText value="<p><label>#{bolonhaBundle['credits']}:</label>" escape="false"/>			
			<h:inputText id="minimumCreditsForPrecedencyBetweenDegreeModules" maxlength="8" size="4" value="#{CurricularRulesManagement.minimumCredits}"/>
			<h:outputText value="</p>" escape="false"/>
		</h:panelGroup>
 
		<h:panelGroup rendered="#{CurricularRulesManagement.selectedCurricularRuleType == 'ANY_CURRICULAR_COURSE'}">
			<h:outputText value="<p><label>#{bolonhaBundle['credits']}:</label>" escape="false"/>
			<h:inputText id="creditsForAnyCurricularCourseRule" maxlength="8" size="4" value="#{CurricularRulesManagement.credits}"/>
			<h:outputText value="</p>" escape="false"/>
			<h:outputText value="<p><label>#{bolonhaBundle['years']}:</label>" escape="false"/>
			<h:outputText value="#{bolonhaBundle['minimum']}: " escape="false"/>
			<h:inputText id="minimumYear" maxlength="8" size="4" value="#{CurricularRulesManagement.minimumYear}"/>
			<h:outputText value=" " escape="false"/>
			<h:outputText value="#{bolonhaBundle['maximum']}: " escape="false"/>
			<h:inputText id="maximumYear" maxlength="8" size="4" value="#{CurricularRulesManagement.maximumYear}"/>
			<h:outputText value="</p>" escape="false"/>
			<h:outputText value="<p><label>#{bolonhaBundle['apply.in']} #{bolonhaBundle['semester']}:</label>" escape="false"/>
			<fc:selectOneMenu value="#{CurricularRulesManagement.selectedSemester}">
				<f:selectItem itemLabel="#{bolonhaBundle['both']}" itemValue="0"/>
				<f:selectItem itemLabel="1" itemValue="1"/>
				<f:selectItem itemLabel="2" itemValue="2"/>
			</fc:selectOneMenu>
			<h:outputText value="</p>" escape="false"/>
			<h:outputText value="<p><label>#{bolonhaBundle['degreeType']}:</label>" escape="false"/>
			<fc:selectOneMenu value="#{CurricularRulesManagement.selectedDegreeType}" onchange="this.form.submit();"
					valueChangeListener="#{CurricularRulesManagement.onChangeDegreeTypeDropDown}">
				<f:selectItem itemLabel="IST" itemValue="no_selection"/>
				<f:selectItem itemLabel="#{enumerationBundle['DEGREE']}" itemValue="DEGREE"/>
				<f:selectItem itemLabel="#{enumerationBundle['MASTER_DEGREE']}" itemValue="MASTER_DEGREE"/>
				<f:selectItem itemLabel="#{enumerationBundle['INTEGRATED_MASTER_DEGREE']}" itemValue="INTEGRATED_MASTER_DEGREE"/>
			</fc:selectOneMenu>
			<h:outputText value="</p>" escape="false"/>
			<h:outputText value="<p><label>#{bolonhaBundle['degree']}:</label>" escape="false"/>
			<fc:selectOneMenu value="#{CurricularRulesManagement.selectedDegreeID}">
				<f:selectItems binding="#{CurricularRulesManagement.degreeItems}"/>
			</fc:selectOneMenu>
			<h:outputText value="</p>" escape="false"/>
			<h:outputText value="<p><label>#{bolonhaBundle['department']}:</label>" escape="false"/>
			<fc:selectOneMenu value="#{CurricularRulesManagement.selectedDepartmentUnitID}">
				<f:selectItems binding="#{CurricularRulesManagement.departmentUnitItems}"/>
			</fc:selectOneMenu>
			<h:outputText value="</p>" escape="false"/>
		</h:panelGroup>
		
		<h:outputText value="<p><label>#{bolonhaBundle['apply.in']} #{bolonhaBundle['group']}:</label>" escape="false"/>
		<fc:selectOneMenu value="#{CurricularRulesManagement.selectedContextCourseGroupID}">
			<f:selectItems binding="#{CurricularRulesManagement.courseGroupItems}"/>
		</fc:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
		
		<h:panelGroup rendered="#{CurricularRulesManagement.selectedCurricularRuleType == 'PRECEDENCY_APPROVED_DEGREE_MODULE' 
					|| CurricularRulesManagement.selectedCurricularRuleType == 'PRECEDENCY_ENROLED_DEGREE_MODULE'}">
			<h:outputText value="<p><label>#{bolonhaBundle['apply.in']} #{bolonhaBundle['semester']}:</label>" escape="false"/>
			<fc:selectOneMenu value="#{CurricularRulesManagement.selectedSemester}">
				<f:selectItem itemLabel="#{bolonhaBundle['both']}" itemValue="0"/>
				<f:selectItem itemLabel="1" itemValue="1"/>
				<f:selectItem itemLabel="2" itemValue="2"/>
			</fc:selectOneMenu>
			<h:outputText value="</p>" escape="false"/>
		</h:panelGroup>
		
		<h:outputText value="</fieldset></div>" escape="false"/>
		
		<h:outputText value="<p>" escape="false"/>
		<h:commandButton value="#{bolonhaBundle['create']}" styleClass="inputbutton" action="#{CurricularRulesManagement.createCurricularRule}"/>
		<h:commandButton immediate="true" value="#{bolonhaBundle['cancel']}" styleClass="inputbutton" action="setCurricularRules"/>
		<h:outputText value="</p>" escape="false"/>
	</h:form>
</ft:tilesView>
