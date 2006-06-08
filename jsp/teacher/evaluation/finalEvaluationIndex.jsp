<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">

<style>
ul.links {
list-style: none;
padding-left: 2em;
}
ul.links li {
padding-top: 0;
padding-bottom: 0;
margin-top: 0;
margin-bottom: 0;
}
</style>

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	<h:messages layout="table" errorClass="error"/>
	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		
		<h:outputText value="<h2>#{bundle['label.finalEvaluation']}</h2>" escape="false" />
	
		<h:outputText value="<ul class=\"links\"><li><b>#{bundle['label.students.listMarks']}:</b> " escape="false"/>
		<h:commandLink action="enterShowMarksListOptions">
			<f:param name="evaluationID" value="#{evaluationManagementBackingBean.finalEvaluation.idInternal}" />
			<h:outputFormat value="#{bundle['link.teacher.evaluation.grades']}"/>
		</h:commandLink>
		
		<h:outputText value="<b> | </b>" escape="false"/>
		<h:commandLink action="enterPublishMarks">
			<f:param name="evaluationID" value="#{evaluationManagementBackingBean.finalEvaluation.idInternal}" />
			<h:outputFormat value="#{bundle['link.publishMarks']}" />
		</h:commandLink>

<%--		<h:outputText value="<b> | </b>" escape="false"/>
		<h:commandLink action="enterSubmitMarksList">
			<f:param name="evaluationID" value="#{evaluationManagementBackingBean.finalEvaluation.idInternal}" />		
			<h:outputFormat value="#{bundle['label.submit.listMarks']}" />
		</h:commandLink>
--%>
		<h:outputText value="<b> | </b>" escape="false"/>
		<h:outputLink value="#{evaluationManagementBackingBean.contextPath}/teacher/markSheetManagement.do?method=prepareSubmitMarks&executionCourseID=#{evaluationManagementBackingBean.executionCourseID}">
			<h:outputText value="#{bundle['label.submit.listMarks']}"/>
		</h:outputLink>
		
		<h:outputText value="</li></ul>" escape="false"/>
	</h:form>

</ft:tilesView>
