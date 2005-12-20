<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="departmentMember.masterPage"
	attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/DepartmentMemberResources"
		var="bundle" />
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources"
		var="bundleEnumeration" />
		
	<f:verbatim>
		<style>
		table.search {
		background-color: #f5f5f5;
		border-collapse: collapse;
		}
		table.search tr td {
		border: 1px solid #fff;
		padding: 0.3em;
		}
		.leftcolumn {
		text-align: right;
		}
		
		h2.cd_heading {
		font-weight: normal;
		margin-top: 3em;
		border-top: 1px solid #e5e5e5;
		background-color: #fafafa;
		padding: 0.25em 0 0em 0.25em;
		padding: 0.5em 0.25em;
		}
		h2.cd_heading span {
		margin-top: 2em;
		border-bottom: 2px solid #fda;
		}
		
		div.cd_block {
		background-color: #fed;
		padding: 0.5em 0.5em 0.5em 0.5em;
		}
		
		table.cd {
		border-collapse: collapse;
		}
		table.cd th {
		border: 1px solid #ccc;
		background-color: #eee;
		padding: 0.5em;
		text-align: center;
		}
		table.cd td {
		border: 1px solid #ccc;
		background-color: #fff;
		padding: 0.5em;
		text-align: center;
		}
		
		p.insert {
		padding-left: 2em;
		}
		div.cd_float {
		width: 100%;
		float: left;
		padding: 0 2.5em;
		padding-bottom: 1em;
		}
		ul.cd_block {
		width: 43%;
		list-style: none;
		float: left; 
		margin: 0;
		padding: 0;
		padding: 1em;
		}
		ul.cd_block li {
		}
		ul.cd_nostyle {
		list-style: none;
		}
		</style>
	</f:verbatim>

	<h:form>
		<fc:viewState binding="#{viewDepartmentTeachers.viewState}"/>
				
		<h:outputText value="<h2>#{bundle['label.teacher.details.title']}</h2>" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" escape="false" />
				
		<h:panelGrid columns="2" styleClass="search">
			<h:outputText value="#{bundle['label.common.executionYear']}:" styleClass="leftColumn"/>
			<fc:selectOneMenu id="dropDownListExecutionYearID" value="#{viewDepartmentTeachers.selectedExecutionYearID}" valueChangeListener="#{viewDepartmentTeachers.onSelectedExecutionYearChanged}" onchange="this.form.submit();">
				<f:selectItems value="#{viewDepartmentTeachers.executionYears}" />
			</fc:selectOneMenu>
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />
		
		<h:outputText value="<ul>" escape="false"/>
			<h:outputText value="<li>" escape="false"/>
				<h:outputLink value="#personalInformation">
					<h:outputText escape="false" value="#{bundle['label.teacher.details.personalInformation']}"/>
				</h:outputLink>
			<h:outputText value="</li>" escape="false"/>
			<h:outputText value="<li>" escape="false"/>
				<h:outputLink value="#lecturedCoursesInformation">
					<h:outputText escape="false" value="#{bundle['label.teacher.details.lecturedCoursesInformation']}"/>
				</h:outputLink>
			<h:outputText value="</li>" escape="false"/>
			<h:outputText value="<li>" escape="false"/>
				<h:outputLink value="#orientationInformation">
					<h:outputText escape="false" value="#{bundle['label.teacher.details.orientationInformation']}"/>
				</h:outputLink>
			<h:outputText value="</li>" escape="false"/>
		<h:outputText value="</ul>" escape="false"/>
				
		<!-- Personal Information -->
		<h:outputText value="<h2 id='personalInformation' class='cd_heading'><span>#{bundle['label.teacher.details.personalInformation']}</span></h2>" escape="false" />
		<h:outputText value="<ul style=\"list-style: none;\">" escape="false" />
			<h:outputText value="<li>" escape="false"/>				
				<h:outputText value="<strong>#{bundle['label.teacher.name']}:</strong>"  escape="false"/>
				<h:outputText value="&nbsp;" escape="false" />
				<h:outputText
					value="#{viewDepartmentTeachers.selectedTeacher.infoPerson.nome}" />
			<h:outputText value="</li>" escape="false"/>
			<h:outputText value="<li>" escape="false" />
				<h:outputText value="<strong>#{bundle['label.teacher.number']}:</strong>" escape="false"/>
				<h:outputText value="&nbsp;" escape="false" />
				<h:outputText
					value="#{viewDepartmentTeachers.selectedTeacher.teacherNumber}" />
			<h:outputText value="</li>" escape="false"/>			
			<h:outputText value="<li>" escape="false"/>
				<h:outputText value="<strong>#{bundle['label.teacher.category']}:</strong>"  escape="false"/>
				<h:outputText value="&nbsp;" escape="false" />
				<h:outputText
					value="#{viewDepartmentTeachers.selectedTeacher.infoCategory.shortName}" />
			<h:outputText value="</li>" escape="false"/>
		<h:outputText value="</ul>" escape="false"/>
		
		<!-- Lectured Courses Information -->
		<h:outputText value="<h2 id='lecturedCoursesInformation' class='cd_heading'><span>#{bundle['label.teacher.details.lecturedCoursesInformation']}</span></h2>" escape="false" />
	
		<!-- Lectured Degree Courses -->
		<h:outputText value="#{bundle['label.common.degree']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
		<h:panelGroup rendered="#{!(empty viewDepartmentTeachers.lecturedDegreeExecutionCourses)}">
			<h:dataTable value="#{viewDepartmentTeachers.lecturedDegreeExecutionCourses}" var="lecturedCourse" styleClass="cd">
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.courseName']}" />
					</f:facet>
					<h:outputText value="#{lecturedCourse.nome}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.courseYear']}" />
					</f:facet>
					<h:outputText value="#{lecturedCourse.executionPeriod.executionYear.year}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.courseSemester']}" />
					</f:facet>
					<h:outputText value="#{lecturedCourse.executionPeriod.semester}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.courseDegrees']}" />
					</f:facet>
					<h:outputText value="#{viewDepartmentTeachers.lecturedDegreeExecutionCourseDegreeNames[lecturedCourse.idInternal]}" />
				</h:column>
			</h:dataTable>
		</h:panelGroup>
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{empty viewDepartmentTeachers.lecturedDegreeExecutionCourses}">
			<h:outputText value="#{bundle['label.common.noLecturedCourses']}"></h:outputText>
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />
		
		<!-- Lectured Master Degree Courses -->
		<h:outputText value="#{bundle['label.common.masterDegree']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
		<h:panelGroup rendered="#{!(empty viewDepartmentTeachers.lecturedMasterDegreeExecutionCourses)}">
			<h:dataTable value="#{viewDepartmentTeachers.lecturedMasterDegreeExecutionCourses}" var="lecturedCourse" styleClass="cd">
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.courseName']}" />
					</f:facet>
					<h:outputText value="#{lecturedCourse.nome}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.courseYear']}" />
					</f:facet>
					<h:outputText value="#{lecturedCourse.executionPeriod.executionYear.year}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.courseSemester']}" />
					</f:facet>
					<h:outputText value="#{lecturedCourse.executionPeriod.semester}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.common.courseDegrees']}" />
					</f:facet>
					<h:outputText value="#{viewDepartmentTeachers.lecturedMasterDegreeExecutionCourseDegreeNames[lecturedCourse.idInternal]}" />
				</h:column>
			</h:dataTable>
		</h:panelGroup>
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{empty viewDepartmentTeachers.lecturedMasterDegreeExecutionCourses}">
			<h:outputText value="#{bundle['label.common.noLecturedCourses']}"></h:outputText>
		</h:panelGrid>
						
		<!-- Orientations Information -->
		<h:outputText value="<h2 id='orientationInformation' class='cd_heading'><span>#{bundle['label.teacher.details.orientationInformation']}</span></h2>" escape="false" />
		
		<!-- Final Degree Work Orientations -->
		<h:outputText value="#{bundle['label.common.degree']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
		<h:panelGroup rendered="#{!(empty viewDepartmentTeachers.finalDegreeWorkAdvises)}">
			<h:dataTable value="#{viewDepartmentTeachers.finalDegreeWorkAdvises}" var="finalDegreeWorkAdvise" styleClass="cd">
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.teacher.details.orientationInformation.finalDegreeWorkStudentNumber']}" />
					</f:facet>
					<h:outputText value="#{finalDegreeWorkAdvise.student.number}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.teacher.details.orientationInformation.finalDegreeWorkStudentName']}" />
					</f:facet>
					<h:outputText value="#{finalDegreeWorkAdvise.student.person.nome}" />
				</h:column>
			</h:dataTable>
		</h:panelGroup>
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{empty viewDepartmentTeachers.finalDegreeWorkAdvises}">
			<h:outputText value="#{bundle['label.teacher.details.orientationInformation.noFinalDegreeWorks']}" />
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />
			
		<!-- Master Degree Orientations -->
		<h:outputText value="#{bundle['label.common.masterDegree']}" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
		<h:panelGroup rendered="#{!(empty viewDepartmentTeachers.guidedMasterDegreeThesisList)}">
			<h:dataTable value="#{viewDepartmentTeachers.guidedMasterDegreeThesisList}" var="masterDegreeThesisDataVersion" styleClass="cd">
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.teacher.details.orientationInformation.masterDegreeThesisTitle']}" />
					</f:facet>
					<h:outputText value="#{masterDegreeThesisDataVersion.dissertationTitle}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.teacher.details.orientationInformation.masterDegreeThesisStudentName']}" />
					</f:facet>
					<h:outputText value="#{masterDegreeThesisDataVersion.masterDegreeThesis.studentCurricularPlan.student.person.nome}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.teacher.details.orientationInformation.masterDegreeProofDate']}" />
					</f:facet>
					<h:panelGroup rendered="#{(masterDegreeThesisDataVersion.masterDegreeThesis.activeMasterDegreeProofVersion != null) && (masterDegreeThesisDataVersion.masterDegreeThesis.activeMasterDegreeProofVersion.proofDate != null)}">
						<h:outputFormat value="{0, date, dd / MM / yyyy}" escape="false">
							<f:param value="#{masterDegreeThesisDataVersion.masterDegreeThesis.activeMasterDegreeProofVersion.proofDate}" />
						</h:outputFormat>
					</h:panelGroup>
					<h:panelGroup rendered="#{(masterDegreeThesisDataVersion.masterDegreeThesis.activeMasterDegreeProofVersion == null) || (masterDegreeThesisDataVersion.masterDegreeThesis.activeMasterDegreeProofVersion.proofDate == null)}">
						<h:outputText value="#{bundle['label.common.notAvailable']}"></h:outputText>
					</h:panelGroup>
				</h:column>
			</h:dataTable>
		</h:panelGroup>
		<h:panelGrid border="0" cellpadding="0" cellspacing="0" rendered="#{empty viewDepartmentTeachers.guidedMasterDegreeThesisList}">
			<h:outputText value="#{bundle['label.teacher.details.orientationInformation.noMasterDegreeThesis']}" />
		</h:panelGrid>	
		
	</h:form>
</ft:tilesView>
