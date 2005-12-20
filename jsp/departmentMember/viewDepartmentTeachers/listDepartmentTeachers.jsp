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
		</style>
	</f:verbatim>
	
	<h:form>
	
		<h:outputText value="<h2>#{bundle['label.teacher.list.title']}</h2>" escape="false" />

		<h:outputText value="<h3>#{viewDepartmentTeachers.department.realName}</h3>" escape="false" />

	
		<h:panelGrid columns="2" styleClass="search">
			<h:outputText value="#{bundle['label.common.executionYear']}:" styleClass="leftColumn"/>
			<fc:selectOneMenu id="dropDownListExecutionYearID" value="#{viewDepartmentTeachers.selectedExecutionYearID}" valueChangeListener="#{viewDepartmentTeachers.onSelectedExecutionYearChanged}" onchange="this.form.submit();">
				<f:selectItems value="#{viewDepartmentTeachers.executionYears}" />
			</fc:selectOneMenu>
		</h:panelGrid>
	
		<h:outputText value="<br/>" escape="false" />
			
		<h:dataTable value="#{viewDepartmentTeachers.departmentTeachers}"
			var="teacher" columnClasses="listClasses" headerClass="listClasses-header" style="width: 70%;">
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.teacher.number']}"></h:outputText>
				</f:facet>
				<h:outputText value="#{teacher.teacherNumber}" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.teacher.name']}"></h:outputText>
				</f:facet>
				<fc:commandLink value="#{teacher.person.nome}"
					action="viewDetails"
					actionListener="#{viewDepartmentTeachers.selectTeacher}">
					<f:param id="teacherId" name="teacherID"
						value="#{teacher.idInternal}" />
				</fc:commandLink>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.teacher.category']}"></h:outputText>
				</f:facet>
				<h:outputText value="#{teacher.category.shortName}" />
			</h:column>
		</h:dataTable>
	</h:form>
</ft:tilesView>
