<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.secondCycle" bundle="APPLICATION_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
	<br/>
</html:messages>

<bean:define id="processId" name="process" property="idInternal" />

<fr:edit id="secondCycleCandidacyProcessBean"
	name="secondCycleCandidacyProcessBean"
	schema="SecondCycleCandidacyProcessBean.manage"
	action="<%= "/caseHandlingSecondCycleCandidacyProcess.do?method=executeEditCandidacyPeriod&amp;processId=" + processId %>">
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
	</fr:layout>
	<fr:destination name="invalid" path="<%= "/caseHandlingSecondCycleCandidacyProcess.do?method=executeEditCandidacyPeriodInvalid&amp;processId=" + processId %>" />
	<fr:destination name="cancel"  path="<%= "/caseHandlingSecondCycleCandidacyProcess.do?method=listProcessAllowedActivities&amp;processId=" + processId %>" />
</fr:edit>
