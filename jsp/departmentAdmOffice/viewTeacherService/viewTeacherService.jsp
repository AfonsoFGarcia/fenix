<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld"  prefix="c" %>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>


<ft:tilesView definition="departmentAdmOffice.masterPage"
	attributeName="body-inline">
	
<style>
table.vtsbc {
margin-bottom: 1em;
border: 2px solid #ccc;
text-align: center;
border-collapse: collapse;
}
table.vtsbc th {
padding: 0.2em 0.2em;
border: 1px solid #ddd;
border-bottom: 1px solid #ccc;
background-color: #eaeaea;
font-weight: normal;
}
table.vtsbc td {
background-color: #fafafa;
border: none;
border: 1px solid #eee;
padding: 0.25em 0.5em;
}
table.vtsbc td.courses {
background-color: #ffe;
width: 300px;
padding: 0.25em 0.25em;
text-align: center;
}
.center {
text-align: center;
}
.backwhite {
text-align: left;
background-color: #fff;
}
.backwhite a {
/*color: #888;*/
}
.backwhite ul {
margin: 0.3em 0;
}
.backwhite ul li {
padding: 0.2em 0.5em;
}
.backwhite ul li a {
/*text-decoration: none;*/
/*border-bottom: 1px solid #ddd;*/
}

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


	
	<f:loadBundle basename="resources/DepartmentMemberResources"
		var="bundle" />
	<f:loadBundle basename="resources/EnumerationResources"
		var="bundleEnumeration" />
		
	<h:outputText value="<i>#{bundle['label.teacherService.title']}</i><p /><p />" escape="false"/>
	
	<h:outputText value="<h2>#{viewTeacherService.departmentName}</h2> <p />" escape="false"/>
	
	<h:form>
		<h:panelGrid columns="1" styleClass="search">
			<h:panelGrid columns="3" styleClass="search">
				<h:outputText value="#{bundle['label.common.executionYear']}&nbsp;" escape="false" styleClass="leftColumn" />
				<fc:selectOneMenu value="#{viewTeacherService.selectedExecutionYearID}"
					onchange="this.form.submit();">
					<f:selectItems binding="#{viewTeacherService.executionYearItems}"/>
				</fc:selectOneMenu>
				<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
			 	<h:outputText value="#{bundle['label.common.courseSemester']}&nbsp;" escape="false" styleClass="leftColumn" />
				<fc:selectOneMenu value="#{viewTeacherService.selectedExecutionPeriodID}"
					onchange="this.form.submit();">
					<f:selectItems binding="#{viewTeacherService.executionPeriodsItems}"/>
				</fc:selectOneMenu>	
				<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID2' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
			</h:panelGrid>
			<h:panelGrid columns="2" styleClass="search" width="100%">
				<h:selectManyCheckbox value="#{viewTeacherService.selectedViewByTeacherOptions}" layout="pageDirection"
					onchange="this.form.submit();">
					<f:selectItems binding="#{viewTeacherService.viewByTeacherOptionsItems}"/>
				</h:selectManyCheckbox>
				<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID3' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
			</h:panelGrid>
		</h:panelGrid>
	</h:form>	
		
	<h:outputText value="<br />" escape="false" />

	<h:outputText value="<b>#{bundle['label.teacherService.navigateByTeacher']}</b>" escape="false"/>
	<h:outputText value=" #{bundle['label.teacherService.separator']} " escape="false"/>
	<h:outputText value="<a href='viewTeacherServiceByCourse.faces?selectedExecutionYearID=#{viewTeacherService.selectedExecutionYearID}'>" escape="false"/>
	<h:outputText value="#{bundle['label.teacherService.navigateByCourse']}" escape="false"/>
	<h:outputText value="</a> <br /> <p />" escape="false"/>
		
	<h:outputText value="<p /><b>#{bundle['label.teacherService.teacher.title']}</b><p />" escape="false"/>
	
	<h:outputText value="<table class='vtsbc'>" escape="false" />
		<h:outputText value="<tr class='center'>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.number']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.category']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.name']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.hours']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.credits']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.totalLecturedHours']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.availability']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.accumulatedCredits']} <br/> #{viewTeacherService.previousExecutionYear.year}</th>" escape="false" />
		<h:outputText value="</tr>" escape="false" />
		<f:verbatim>
			<fc:dataRepeater value="#{viewTeacherService.teacherServiceDTO}" var="teacher">
				<h:outputText value="<tr id=#{teacher.teacherIdInternal}>" escape="false" />
				<h:outputText value="<td title=\"#{bundle['label.teacherService.teacher.number']}\">#{teacher.teacherNumber}</td>" escape="false" />					
				<h:outputText value="<td title=\"#{bundle['label.teacherService.teacher.category']}\">#{teacher.teacherCategory}</td>" escape="false" />	
				<h:outputText value="<td class='courses' title=\"#{bundle['label.teacherService.teacher.name']}\">#{teacher.teacherName}</td>" escape="false" />	
				<h:outputText value="<td title=\"#{bundle['label.teacherService.teacher.hours']}\">#{teacher.teacherRequiredHours}</td>" escape="false" />	
				<h:outputText value="<td title=\"#{bundle['label.teacherService.teacher.credits']}\">#{teacher.formattedTeacherSpentCredits}</td>" escape="false" />	
				<h:outputText value="<td title=\"#{bundle['label.teacherService.teacher.totalLecturedHours']}\">#{teacher.totalLecturedHours}</td>" escape="false" />	
				<h:outputText value="<td title=\"#{bundle['label.teacherService.teacher.availability']}\"> #{teacher.availability} </td>" escape="false" />
				<h:outputText value="<td title=\"#{bundle['label.teacherService.teacher.accumulatedCredits']}\"> #{teacher.formattedTeacherAccumulatedCredits} </td>" escape="false" />
				<h:outputText value="</tr>" escape="false" />
										
				<h:panelGroup rendered="#{viewTeacherService.viewCreditsInformation == true}">
					<h:outputText value="<tr>" escape="false" />
					<h:outputText value="<td colspan=8 class='backwhite' style='background-color: #fff;'>" escape="false" />
						<h:outputText value="<ul>" escape="false" />
							<fc:dataRepeater value="#{teacher.executionCourseTeacherServiceList}" var="coursesList">
								<h:outputText value="<li><a href='viewTeacherServiceByCourse.faces?selectedExecutionYearID=#{viewTeacherService.selectedExecutionYearID}##{coursesList.executionCourseIdInternal}'>" escape="false"/>
								<h:outputText value="#{coursesList.description} " escape="false" />	
							 	<h:outputText value="#{bundle['label.teacherService.hours']}" escape="false" />
							 	<h:outputText value="</a></li>" escape="false"/>
							</fc:dataRepeater>
							<fc:dataRepeater value="#{teacher.managementFunctionList}" var="managementFunction">
								<h:outputText value="<li>#{managementFunction.functionName} - #{managementFunction.credits}</li>" escape="false" />
							</fc:dataRepeater>
							<fc:dataRepeater value="#{teacher.exemptionSituationList}" var="exemptionSituation">
								<h:outputText value="<li>" escape="false"/>
								<h:outputText value="#{bundleEnumeration[exemptionSituation.functionName]}" />
								<h:outputText value=" - #{exemptionSituation.credits}</li>" escape="false" />
							</fc:dataRepeater>
						<h:outputText value="</ul>" escape="false" />
					<h:outputText value="</td>" escape="false" />
					<h:outputText value="</tr>" escape="false" />
				</h:panelGroup>
			</fc:dataRepeater>
		</f:verbatim>
	<h:outputText value="</table>" escape="false" />

	
</ft:tilesView>