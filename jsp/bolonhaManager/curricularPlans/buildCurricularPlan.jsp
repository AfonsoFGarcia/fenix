<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<style>@import url(<%= request.getContextPath() %>/CSS/dotist_degreeStructure.css);</style>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<i>#{CurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[CurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</i>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['buildCurricularPlan']}</h2>" escape="false"/>

	<h:outputLink value="createCurricularCourse.faces" rendered="#{!empty CurricularCourseManagement.degreeCurricularPlan.degreeModule.courseGroupContexts}">
		<h:outputText value="#{bolonhaBundle['createCurricularCourse']}" />
		<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
	</h:outputLink>
	<h:outputText value="<br/>" escape="false"/>
	<h:outputLink value="editCurricularPlanStructure.faces">
		<h:outputText value="#{bolonhaBundle['edit.curricularPlan.structure']}" />
		<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
	</h:outputLink>

	<h:outputText value="<br/><br/>#{bolonhaBundle['view.structure.organized.by']}: " escape="false"/>
	<h:outputLink value="buildCurricularPlan.faces">
		<h:outputText value="#{bolonhaBundle['groups']}" />
		<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
		<f:param name="organizeBy" value="groups"/>
	</h:outputLink>
	<h:outputText value=" , " escape="false"/>
	<h:outputLink value="buildCurricularPlan.faces">
		<h:outputText value="#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}" />
		<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
		<f:param name="organizeBy" value="years"/>		
	</h:outputLink>

	<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'/>"/>
	<h:messages styleClass="error0" infoClass="success0" layout="table" globalOnly="true"/>
	<fc:degreeCurricularPlanRender dcp="#{CurricularCourseManagement.degreeCurricularPlan}" onlyStructure="false" toEdit="true" organizeBy="<%=request.getParameter("organizeBy")%>"/>

	<h:form>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'/>"/>

		<h:outputText value="<br/><hr/>" escape="false"/>
		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['return']}"
			action="curricularPlansManagement"/>
	</h:form>

</ft:tilesView>
