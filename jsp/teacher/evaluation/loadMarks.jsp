<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<h2>#{bundle['title.evaluation.manage.marksListWithFile']}</h2>" escape="false">
		<f:param value="#{evaluationManagementBackingBean.executionCourse.nome}" />
	</h:outputFormat>

	<h:form enctype="multipart/form-data">
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />

		<h:panelGrid width="100%" columns="1" cellspacing="8" cellpadding="8">
			<h:panelGroup>
				<h:outputText value="<b>#{bundle['label.exam']}:</b> " escape="false" rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}"/>
				<h:outputText value="<b>#{bundle['label.written.test']}:</b> " escape="false" rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>
				<h:outputText value="#{evaluationManagementBackingBean.evaluation.season}, " rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}"/>
				<h:outputText value="#{evaluationManagementBackingBean.evaluation.description}, " rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>
				<h:outputText value="#{bundle['label.day']}" />
				<h:outputFormat value="{0, date, dd/MM/yyyy}">
					<f:param value="#{evaluationManagementBackingBean.evaluation.dayDate}"/>
				</h:outputFormat>
				<h:outputText value=" #{bundle['label.at']}" />
				<h:outputFormat value="{0, date, HH:mm}">
					<f:param value="#{evaluationManagementBackingBean.evaluation.beginningDate}"/>
				</h:outputFormat>
			</h:panelGroup>

			<h:panelGrid styleClass="infoop" columns="1">
				<h:outputText value="#{bundle['label.fileUpload.information']}" escape="false"/>
			</h:panelGrid>

			<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
				value="#{bundle[evaluationManagementBackingBean.errorMessage]}"/>
			<h:messages showSummary="true" errorClass="error" rendered="#{empty evaluationManagementBackingBean.errorMessage}"/>

			<h:outputText value="#{bundle['label.file']}: <br/>" escape="false"/>
			<h:outputText value="<input size=\"30\" type=\"file\" name=\"theFile\"/>" escape="false"/>
			
			<h:panelGroup>
				<h:commandButton styleClass="inputbutton" action="#{evaluationManagementBackingBean.loadMarks}" value="#{bundle['button.send']}"/>
				<h:commandButton immediate="true" action="success" styleClass="inputbutton" value="#{bundle['button.cancel']}"/>
			</h:panelGroup>
		</h:panelGrid>
	</h:form>

</ft:tilesView>
