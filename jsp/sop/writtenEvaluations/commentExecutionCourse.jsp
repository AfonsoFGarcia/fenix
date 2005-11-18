<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="definition.sop.examsPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ApplicationResourcesSOP" var="bundleSOP"/>
	<f:loadBundle basename="ServidorApresentacao/ApplicationResources" var="bundle"/>

	<h:outputText value="<h2>#{bundleSOP['title.insert.comment']}</h2><br/>" escape="false"/>

	<h:form>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionCourseIdHidden}" />

		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionPeriodIdHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionDegreeIdHidden}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.curricularYearIdHidden}"/>
		<fc:viewState binding="#{SOPEvaluationManagementBackingBean.viewState}" />
		<h:outputText escape="false" value="<input id='executionPeriodOID' name='executionPeriodOID' type='hidden' value='#{SOPEvaluationManagementBackingBean.executionPeriodOID}'/>"/>

		<h:panelGrid styleClass="infoselected">
			<h:outputText value="#{bundleSOP['property.executionPeriod']}: #{SOPEvaluationManagementBackingBean.executionPeriodLabel}" escape="false"/>
			
			<h:outputText value="#{bundleSOP['property.context.degree']}: #{SOPEvaluationManagementBackingBean.executionDegreeLabel}" escape="false"/>
			
			<h:outputText value="#{bundleSOP['property.context.curricular.year']}: #{SOPEvaluationManagementBackingBean.curricularYear}" escape="false"/>
			
			<h:outputText value="#{bundleSOP['property.aula.disciplina']}: <b>#{SOPEvaluationManagementBackingBean.executionCourse.nome}</b>" escape="false"/>
		</h:panelGrid>
		<h:outputText value="<br/><br/>" escape="false"/>

		<h:outputText styleClass="error" rendered="#{!empty SOPEvaluationManagementBackingBean.errorMessage}"
			value="#{bundle[SOPEvaluationManagementBackingBean.errorMessage]}"/>
		<h:messages showSummary="true" errorClass="error" rendered="#{empty SOPEvaluationManagementBackingBean.errorMessage}"/>

		<h:panelGrid columns="1" border="0">
			<h:outputText value="#{bundleSOP['label.comment']}:" escape="false"/>
			<h:inputTextarea rows="2" cols="56" value="#{SOPEvaluationManagementBackingBean.comment}"/>
		</h:panelGrid>

		<h:outputText value="<br/>" escape="false"/>
 		<h:commandButton 	styleClass="inputbutton"
							value="#{bundleSOP['button.insert']}" 
							action="#{SOPEvaluationManagementBackingBean.commentExecutionCourse}" 
							title="#{bundleSOP['button.insert']}"/>
		<h:commandButton immediate="true" action="writtenEvaluationCalendar" styleClass="inputbutton" value="#{bundleSOP['button.cancel']}"/>
	</h:form>
</ft:tilesView>
