<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="df.executionDegreeManagement.default" attributeName="body-inline">
	<f:loadBundle basename="resources/ManagerResources" var="managerResources"/>
	
	<h:outputText styleClass="success0" rendered="#{!empty createExecutionDegrees.createdDegreeCurricularPlans}" value="Os seguintes planos curriculares foram criados correctamente:"/>
	<fc:dataRepeater value="#{createExecutionDegrees.createdDegreeCurricularPlans}" var="degreeCurricularPlan">
		<h:outputText value="<p>#{degreeCurricularPlan.name}</p>" escape="false"/>
	</fc:dataRepeater>

	<p>
	<h:messages errorClass="error0" infoClass="success0"/>
	</p>
	
	<h:form>
		<h:inputHidden value="#{createExecutionDegrees.chosenDegreeType}" />
						
		<h:outputText value="<strong>Planos Curriculares Pr�-Bolonha:</strong>" escape="false" />

		<h:panelGroup rendered="#{!empty createExecutionDegrees.degreeCurricularPlansSelectItems.value}">
			<h:selectManyCheckbox value="#{createExecutionDegrees.choosenDegreeCurricularPlansIDs}" layout="pageDirection" >
				<f:selectItems binding="#{createExecutionDegrees.degreeCurricularPlansSelectItems}" />
			</h:selectManyCheckbox>
		</h:panelGroup>		
		<h:panelGroup rendered="#{empty createExecutionDegrees.degreeCurricularPlansSelectItems.value}">
			<h:outputText value="<p><em>N�o existem planos curriculares activos</em></p>" escape="false" />
		</h:panelGroup>

		<br/>
		
		<h:outputText value="<strong>Planos Curriculares de Bolonha:</strong>" escape="false" />
		<h:panelGroup rendered="#{!empty createExecutionDegrees.bolonhaDegreeCurricularPlansSelectItems.value}">
			<h:selectManyCheckbox value="#{createExecutionDegrees.choosenBolonhaDegreeCurricularPlansIDs}" layout="pageDirection" >
				<f:selectItems binding="#{createExecutionDegrees.bolonhaDegreeCurricularPlansSelectItems}" />
			</h:selectManyCheckbox>
		</h:panelGroup>
		<h:panelGroup rendered="#{empty createExecutionDegrees.bolonhaDegreeCurricularPlansSelectItems.value}">
			<h:outputText value="<p><em>N�o existem planos curriculares activos e aprovados ou publicados</em></p>" escape="false" />
		</h:panelGroup>

		<br/>
		
		<h:panelGroup rendered="#{!empty createExecutionDegrees.degreeCurricularPlansSelectItems.value || !empty createExecutionDegrees.bolonhaDegreeCurricularPlansSelectItems.value}">
			<p>
				<h:panelGrid columns="2">
					<h:outputText value="<strong>Ano lectivo de execu��o:</strong>" escape="false" />
					<h:selectOneMenu value="#{createExecutionDegrees.choosenExecutionYearID}">
						<f:selectItems value="#{createExecutionDegrees.executionYears}" />
					</h:selectOneMenu>
								
					<h:outputText value="<strong>Campus:</strong>" escape="false" />
					<h:selectOneMenu value="#{createExecutionDegrees.campus}" >
						<f:selectItems value="#{createExecutionDegrees.allCampus}" />
					</h:selectOneMenu>
						
					<h:outputText value="<strong>Mapa de exames tempor�rio:<strong>" escape="false" />
					<h:selectBooleanCheckbox value="#{createExecutionDegrees.temporaryExamMap}" />		
				</h:panelGrid>
			</p>
			<br/>
			<p>
				<h:panelGrid columns="12">
				 	<h:outputText value="<strong>Periodo Aulas 1� Semestre:</strong>" escape="false"/>
					<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason1BeginDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason1BeginMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason1BeginYear}">
						<f:selectItems value="#{createExecutionDegrees.years}" />
					</h:selectOneMenu>
					<h:outputText value=" a " />
					<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason1EndDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason1EndMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason1EndYear}">
						<f:selectItems value="#{createExecutionDegrees.years}" />
					</h:selectOneMenu>
					
				 	<h:outputText value="<strong>Periodo Exames 1� Semestre:</strong>" escape="false"/>
					<h:selectOneMenu value="#{createExecutionDegrees.examsSeason1BeginDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSeason1BeginMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSeason1BeginYear}">
						<f:selectItems value="#{createExecutionDegrees.years}" />
					</h:selectOneMenu>
					<h:outputText value=" a " />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSeason1EndDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSeason1EndMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSeason1EndYear}">
						<f:selectItems value="#{createExecutionDegrees.years}" />
					</h:selectOneMenu>					
				
				 	<h:outputText value="<strong>Periodo Aulas 2� Semestre:<strong>" escape="false" />
					<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason2BeginDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason2BeginMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason2BeginYear}">
						<f:selectItems value="#{createExecutionDegrees.years}" />
					</h:selectOneMenu>
					<h:outputText value=" a " />
					<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason2EndDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason2EndMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.lessonSeason2EndYear}">
						<f:selectItems value="#{createExecutionDegrees.years}" />
					</h:selectOneMenu>
				
				 	<h:outputText value="<strong>Periodo Exames 2� Semestre:</strong>" escape="false" />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSeason2BeginDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSeason2BeginMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSeason2BeginYear}">
						<f:selectItems value="#{createExecutionDegrees.years}" />
					</h:selectOneMenu>
					<h:outputText value=" a " />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSeason2EndDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSeason2EndMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSeason2EndYear}">
						<f:selectItems value="#{createExecutionDegrees.years}" />
					</h:selectOneMenu>	
					
					<h:outputText value="<strong>Periodo Exames �poca Especial:</strong>" escape="false" />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSpecialSeasonBeginDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSpecialSeasonBeginMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSpecialSeasonBeginYear}">
						<f:selectItems value="#{createExecutionDegrees.years}" />
					</h:selectOneMenu>
					<h:outputText value=" a " />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSpecialSeasonEndDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSpecialSeasonEndMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.examsSpecialSeasonEndYear}">
						<f:selectItems value="#{createExecutionDegrees.years}" />
					</h:selectOneMenu>
			
					<h:outputText value="<strong>Prazo para Lan�amento de Notas 1� Semestre:</strong>" escape="false" />
					<h:selectOneMenu value="#{createExecutionDegrees.gradeSubmissionNormalSeason1EndDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.gradeSubmissionNormalSeason1EndMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.gradeSubmissionNormalSeason1EndYear}">
						<f:selectItems value="#{createExecutionDegrees.years}" />
					</h:selectOneMenu>
					<%-- It's necessary to put these empty outputText's because panelGrid has 12 columns --%>
					<h:outputText value="" />
					<h:outputText value="" />
					<h:outputText value="" />
					<h:outputText value="" />
					<h:outputText value="" />
					<h:outputText value="" />
					
					<h:outputText value="<strong>Prazo para Lan�amento de Notas 2� Semestre:</strong>" escape="false" />
					<h:selectOneMenu value="#{createExecutionDegrees.gradeSubmissionNormalSeason2EndDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.gradeSubmissionNormalSeason2EndMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.gradeSubmissionNormalSeason2EndYear}">
						<f:selectItems value="#{createExecutionDegrees.years}" />
					</h:selectOneMenu>
					<%-- It's necessary to put these empty outputText's because panelGrid has 12 columns --%>
					<h:outputText value="" />
					<h:outputText value="" />
					<h:outputText value="" />
					<h:outputText value="" />
					<h:outputText value="" />
					<h:outputText value="" />
					
					<h:outputText value="<strong>Prazo para Lan�amento de Notas �poca Especial:</strong>" escape="false" />
					<h:selectOneMenu value="#{createExecutionDegrees.gradeSubmissionSpecialSeasonEndDay}">
						<f:selectItems value="#{createExecutionDegrees.days}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.gradeSubmissionSpecialSeasonEndMonth}">
						<f:selectItems value="#{createExecutionDegrees.months}" />
					</h:selectOneMenu>
					<h:outputText value="/" />
					<h:selectOneMenu value="#{createExecutionDegrees.gradeSubmissionSpecialSeasonEndYear}">
						<f:selectItems value="#{createExecutionDegrees.years}" />
					</h:selectOneMenu>
					<%-- It's necessary to put these empty outputText's because panelGrid has 12 columns --%>
					<h:outputText value="" />
					<h:outputText value="" />
					<h:outputText value="" />
					<h:outputText value="" />
					<h:outputText value="" />
					<h:outputText value="" />
				</h:panelGrid>
			</p>
			
			<h:commandButton action="#{createExecutionDegrees.createExecutionDegrees}" value="#{managerResources['label.create']}" styleClass="inputbutton"/>

		</h:panelGroup>
		
		<h:commandButton action="back" value="#{managerResources['label.return']}" immediate="true" styleClass="inputbutton"/>
	</h:form>

</ft:tilesView>



