<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA.HideCancelledCandidaciesBean" %>

<html:xhtml/>

<bean:define id="processName" name="processName" />

<em><bean:message key="label.erasmus.candidacy" bundle="APPLICATION_RESOURCES"/></em>

<h2><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES" /></h2>

<%-- no candidacy process --%>
<logic:empty name="process">

	<logic:equal name="canCreateProcess" value="true">
		<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareCreateNewProcess"%>'>
			<bean:message key='<%= "link.create.new.process." + processName.toString()%>' bundle="APPLICATION_RESOURCES"/>	
		</html:link>
	</logic:equal>

	<logic:empty name="executionIntervals">
		<p><strong><bean:message key="label.candidacy.no.candidacies" bundle="APPLICATION_RESOURCES" /></strong></p>
	</logic:empty>

	<logic:notEmpty name="executionIntervals">
		<html:form action='<%= "/caseHandling" + processName.toString() + ".do?method=intro" %>'>
			<table class="tstyle5 thlight thright mbottom05">
				<tr>
					<th><bean:message key="label.executionYear" bundle="APPLICATION_RESOURCES" />:</th>
					<td>
						<html:select bundle="HTMLALT_RESOURCES" property="executionIntervalId" onchange="this.form.submit();">
							<html:option value=""><!-- w3c complient --></html:option>
							<html:options collection="executionIntervals" property="idInternal" labelProperty="qualifiedName"/>
						</html:select>
					</td>
				</tr>
				<logic:notEmpty name="candidacyProcesses">
				<tr>
					<th><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES" />:</th>
					<td>
						<html:select bundle="HTMLALT_RESOURCES" property="selectedProcessId">
							<html:option value=""><!-- w3c complient --></html:option>
							<html:options collection="candidacyProcesses" property="idInternal" labelProperty="candidacyPeriod.presentationName"/>
						</html:select>
					</td>
				</tr>
				</logic:notEmpty>
			</table>

			<p class="mtop05"><html:submit><bean:message key="label.choose"/></html:submit></p>			
		</html:form>

	</logic:notEmpty>
</logic:empty>

<%-- candidacy process of current year --%>
<logic:notEmpty name="process">

	<bean:define id="processId" name="process" property="idInternal" />
	<bean:define id="childProcessName" name="childProcessName" />
	<bean:size id="candidacyProcessesSize" name="candidacyProcesses" />

	<logic:present role="INTERNATIONAL_RELATION_OFFICE">
	<logic:equal name="canCreateProcess" value="true">
		<p>
			<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareCreateNewProcess"%>'>
				<bean:message key='<%= "link.create.new.process." + processName.toString()%>' bundle="APPLICATION_RESOURCES"/>	
			</html:link>
		</p>
	</logic:equal>
	</logic:present>

	<html:form action='<%= "/caseHandling" + processName.toString() + ".do?method=intro" %>'>

		<p class="mbottom05"><strong><bean:message key="label.erasmus.filter.applications.by" bundle="CANDIDATE_RESOURCES" /></strong></p>

		<table class="tstyle5 thlight thright mtop025 mbottom05 ulnomargin">
			<tr>
				<th><bean:message key="label.executionYear" bundle="APPLICATION_RESOURCES" /></th>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" property="executionIntervalId" onchange="this.form.submit();">
						<html:option value=""><!-- w3c complient --></html:option>
						<html:options collection="executionIntervals" property="idInternal" labelProperty="name"/>
					</html:select>
				</td>
			</tr>
			<logic:notEmpty name="candidacyProcesses">
				<logic:greaterThan name="candidacyProcessesSize" value="1">
					<tr>
						<th><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES" /></th>
						<td>
							<html:select bundle="HTMLALT_RESOURCES" property="selectedProcessId">
								<html:option value=""><!-- w3c complient --></html:option>
								<html:options collection="candidacyProcesses" property="idInternal" labelProperty="candidacyPeriod.presentationName"/>
							</html:select>
						</td>
					</tr>
				</logic:greaterThan>
			</logic:notEmpty>
			<tr>
				<td><bean:message key="label.hide.cancelled.candidacies" bundle="CANDIDATE_RESOURCES"/>?</td>
				<td> 
					<fr:edit id="hide.cancelled.candidacies" name="hideCancelledCandidacies" slot="value">
						<fr:layout name="radio-postback"/>
					</fr:edit>
				</td>
			</tr>					
		</table>

		<p><html:submit><bean:message key="label.choose"/></html:submit></p>
		<bean:define id="chooseDegreeBeanSchemaName" name="chooseDegreeBeanSchemaName" type="String" /> 
		<fr:edit id="choose.degree.bean" name="chooseDegreeBean" schema="<%= chooseDegreeBeanSchemaName %>" >
			<fr:destination name="postback" path="<%= "/caseHandling" + processName.toString() + ".do?method=intro" %>"/>
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
	</html:form>




	<%-- show main process information --%>
	<fr:view name="process" schema="CandidacyProcess.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
	</fr:view>
	
	
	
	
	<logic:notEmpty name="processActivities">
		<%-- list main process activities --%>
		<ul>
		<logic:iterate id="activity" name="processActivities">
			<bean:define id="activityName" name="activity" property="class.simpleName" />
			<li>
				<logic:notEmpty name="hideCancelledCandidacies">
					<bean:define id="hideCancelledCandidacies" name="hideCancelledCandidacies"/>
	 				<bean:define id="hideCancelledCandidaciesValue" > <%= ((HideCancelledCandidaciesBean) hideCancelledCandidacies).getValue().toString() %></bean:define>
					<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareExecute" + activityName.toString() + "&amp;processId=" + processId.toString() + "&amp;hideCancelledCandidacies=" + hideCancelledCandidaciesValue %>'>
						<bean:message name="activity" property="class.name" bundle="CASE_HANDLING_RESOURCES" />
					</html:link>
				</logic:notEmpty>
				<logic:empty name="hideCancelledCandidacies">
					<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareExecute" + activityName.toString() + "&amp;processId=" + processId.toString()  %>'>
						<bean:message name="activity" property="class.name" bundle="CASE_HANDLING_RESOURCES" />
					</html:link>
				</logic:empty>
			</li>
		</logic:iterate>
		</ul>
	</logic:notEmpty>
	
	<logic:present role="INTERNATIONAL_RELATION_OFFICE">
		<%-- Show processes with not viewed learning agreements --%>
		<p><strong><bean:message key="title.erasmus.not.viewed.approved.learning.agreements.list" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
		<logic:empty name="process" property="processesWithNotViewedApprovedLearningAgreements">
			<p><bean:message key="message.erasmus.not.viewed.approved.learning.agreements.empty" bundle="ACADEMIC_OFFICE_RESOURCES" /></p>
		</logic:empty>
		
		<logic:notEmpty name="process" property="processesWithNotViewedApprovedLearningAgreements">
			<fr:view name="process" property="processesWithNotViewedApprovedLearningAgreements" schema="ErasmusIndividualCandidacyProcess.show.not.viewed.learning.agreements">
				<fr:layout name="tabular-sortable">
					<fr:property name="classes" value="tstyle4 thcenter thcenter thcenter"/>
					<fr:property name="columnClasses" value="tdcenter, tdcenter, tdcenter, "/>
				
					<fr:property name="sortParameter" value="sortBy"/>
		            <fr:property name="sortUrl" value='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'/>
	    	        <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "candidacy.mostRecentApprovedLearningAgreement.uploadTime" : request.getParameter("sortBy") %>"/>
	
					<fr:property name="linkFormat(viewProcess)" value='<%= "/caseHandling" + childProcessName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=${idInternal}"%>' />
					<fr:property name="key(viewProcess)" value="label.candidacy.show.candidate"/>
					<fr:property name="bundle(viewProcess)" value="APPLICATION_RESOURCES"/>
					
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
	</logic:present>
	
	<logic:present role="INTERNATIONAL_RELATION_OFFICE">
		<%-- Show processes with not viewed learning agreements --%>
		<p><strong><bean:message key="title.erasmus.not.viewed.erasmus.alerts.process.list" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
		<logic:empty name="process" property="processesWithNotViewedAlerts">
			<p><bean:message key="message.erasmus.not.viewed.erasmus.alerts.process.empty" bundle="ACADEMIC_OFFICE_RESOURCES" /></p>
		</logic:empty>
		
		<logic:notEmpty name="process" property="processesWithNotViewedAlerts">
			<fr:view name="process" property="processesWithNotViewedAlerts" schema="ErasmusIndividualCandidacyProcess.show.not.viewed.alerts.processes">
				<fr:layout name="tabular-sortable">
					<fr:property name="classes" value="tstyle4 thcenter thcenter thcenter"/>
					<fr:property name="columnClasses" value="tdcenter, tdcenter, tdcenter, "/>
				
					<fr:property name="sortParameter" value="sortBy"/>
		            <fr:property name="sortUrl" value='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'/>
	    	        <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "mostRecentAlert.whenCreated" : request.getParameter("sortBy") %>"/>
	
					<fr:property name="linkFormat(viewProcess)" value='<%= "/caseHandling" + childProcessName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=${idInternal}"%>' />
					<fr:property name="key(viewProcess)" value="label.candidacy.show.candidate"/>
					<fr:property name="bundle(viewProcess)" value="APPLICATION_RESOURCES"/>
					
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
	</logic:present>
	
	<p><strong><bean:message key="title.erasmus.application.process.list" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
	<%-- create child process --%>
	<logic:present role="INTERNATIONAL_RELATION_OFFICE">
		<logic:equal name="canCreateChildProcess" value="true">
			<html:link action='<%= "/caseHandling" + childProcessName.toString() + ".do?method=prepareCreateNewProcess&amp;parentProcessId=" + processId.toString() %>'>
				+ <bean:message key='<%= "link.create.new.process." + childProcessName.toString()%>' bundle="APPLICATION_RESOURCES"/>	
			</html:link>
		</logic:equal>
	</logic:present>
	
	<%-- show child processes --%>
	<logic:notEmpty name="childProcesses">
		<fr:view name="childProcesses" schema="ErasmusIndividualCandidacyProcess.list.processes">
			<fr:layout name="tabular-sortable">
				<fr:property name="classes" value="tstyle4 thcenter thcenter thcenter"/>
				<fr:property name="columnClasses" value="tdcenter, tdcenter, tdcenter, "/>

				<fr:property name="linkFormat(viewProcess)" value='<%= "/caseHandling" + childProcessName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=${idInternal}"%>' />
				<fr:property name="key(viewProcess)" value="label.candidacy.show.candidate"/>
				<fr:property name="bundle(viewProcess)" value="APPLICATION_RESOURCES"/>
							
				<fr:property name="sortParameter" value="sortBy"/>
	            <fr:property name="sortUrl" value='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'/>
    	        <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "candidacyState,candidacyDate=desc" : request.getParameter("sortBy") %>"/>
			</fr:layout>
		</fr:view>
		<bean:size id="childProcessesSize" name="childProcesses" />
		
		<p class="mvert05"><bean:message key="label.numberOfCandidates" bundle="APPLICATION_RESOURCES" />: <strong><bean:write name="childProcessesSize" /></strong></p>
		
	</logic:notEmpty>
	
</logic:notEmpty>
