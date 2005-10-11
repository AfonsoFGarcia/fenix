<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<h2>#{bundle['title.evaluation.manage.marksList']}</h2>" escape="false">
		<f:param value="#{evaluationManagementBackingBean.executionCourse.nome}" />
	</h:outputFormat>

	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />

		<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
			value="#{bundle[evaluationManagementBackingBean.errorMessage]}"/>

		<h:panelGrid width="100%" columns="1" cellspacing="8" cellpadding="8">
			<h:panelGroup rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}">
				<h:outputText value="<b>#{bundle['label.exam']}:</b> " escape="false"/>
				<h:outputText value="#{evaluationManagementBackingBean.evaluation.season}, "/>
				<h:outputText value="#{bundle['label.day']}" />
				<h:outputFormat value="{0, date, dd/MM/yyyy}">
					<f:param value="#{evaluationManagementBackingBean.evaluation.dayDate}"/>
				</h:outputFormat>
				<h:outputText value=" #{bundle['label.at']}" />
				<h:outputFormat value="{0, date, HH:mm}">
					<f:param value="#{evaluationManagementBackingBean.evaluation.beginningDate}"/>
				</h:outputFormat>
			</h:panelGroup>

			<h:panelGroup rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.WrittenTest'}">
				<h:outputText value="<b>#{bundle['label.written.test']}:</b> " escape="false"/>
				<h:outputText value="#{evaluationManagementBackingBean.evaluation.description}, "/>
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
				<h:outputText value="#{bundle['label.marksOnline.instructions']}" escape="false"/>
				<h:outputText value="<br/>" escape="false"/>
				<h:commandLink action="enterLoadMarks">
					<f:param name="evaluationIDHidden" value="#{evaluationManagementBackingBean.evaluation.idInternal}" />
					<f:param name="executionCourseIDHidden" value="#{evaluationManagementBackingBean.executionCourse.idInternal}" />
					<f:param name="evaluationID" value="#{evaluationManagementBackingBean.evaluation.idInternal}" />
					<f:param name="executionCourseID" value="#{evaluationManagementBackingBean.executionCourse.idInternal}" />
					<h:outputFormat value="#{bundle['label.load.marks']}" />
				</h:commandLink>
			</h:panelGrid>

			<h:dataTable value="#{evaluationManagementBackingBean.executionCourseAttends}" var="attends" headerClass="listClasses-header" columnClasses="listClasses">
				<h:column>
					<f:facet name="header"><h:outputText value="#{bundle['label.number']}"/></f:facet>
					<h:outputText value="#{attends.aluno.number}" />
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="#{bundle['label.name']}"/></f:facet>
					<h:outputText value="#{attends.aluno.person.nome}" />
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="#{bundle['label.enrolmentEvaluationType']}"/></f:facet>
					<h:outputText rendered="#{attends.enrolment == null}" value="#{bundle['message.notEnroled']}"/>
					<h:outputText rendered="#{attends.enrolment != null}" value="#{attends.enrolment.enrolmentEvaluationType}"/>
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="#{bundle['label.mark']}"/></f:facet>
					<h:inputText size="2" maxlength="2" value="#{evaluationManagementBackingBean.marks[attends.idInternal]}"/>
				</h:column>
			</h:dataTable>

		</h:panelGrid>
		<h:commandButton styleClass="inputbutton" action="#{evaluationManagementBackingBean.editMarks}" value="#{bundle['button.save']}"/>
		<h:commandButton immediate="true" action="#{evaluationManagementBackingBean.evaluation.class.getSimpleName}" styleClass="inputbutton" value="#{bundle['button.cancel']}"/>				
	</h:form>

</ft:tilesView>
