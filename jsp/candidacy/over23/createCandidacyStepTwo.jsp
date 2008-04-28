<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<h2><bean:message key="label.candidacy.create" bundle="APPLICATION_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<fr:form action="/caseHandlingOver23IndividualCandidacyProcess.do">
 	<html:hidden property="method" value="createNewProcess" />

	<fr:edit id="over23IndividualCandidacyProcessBean" name="over23IndividualCandidacyProcessBean" visible="false" />

	<logic:notEmpty name="over23IndividualCandidacyProcessBean" property="candidacyProcess">
		<h3 class="mtop15 mbottom025"><bean:message key="label.add.degree" bundle="APPLICATION_RESOURCES"/>:</h3>
		<fr:edit id="Over23IndividualCandidacyProcessBean.degrees"
			name="over23IndividualCandidacyProcessBean"
			schema="Over23IndividualCandidacyProcessBean.degrees">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="/caseHandlingOver23IndividualCandidacyProcess.do?method=fillCandidacyInformationInvalid" />
		</fr:edit>
		<html:submit onclick="this.form.method.value='addDegreeToCandidacy';return true;"><bean:message key="label.add" bundle="APPLICATION_RESOURCES" /></html:submit>
		
		<br/>
		<br/>
		
		<logic:notEmpty name="over23IndividualCandidacyProcessBean" property="selectedDegrees">
		<h3 class="mtop15 mbottom025"><bean:message key="label.degrees" bundle="APPLICATION_RESOURCES"/>:</h3>
		<fr:view name="over23IndividualCandidacyProcessBean" property="selectedDegrees" schema="Degree.name.and.sigla">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight"/>
				<fr:property name="checkable" value="true" />
				<fr:property name="checkboxName" value="degreesToDelete" />
				<fr:property name="checkboxValue" value="idInternal" />	
			</fr:layout>
		</fr:view>
		<html:submit onclick="this.form.method.value='removeDegreeFromCandidacy';return true;"><bean:message key="label.remove" bundle="APPLICATION_RESOURCES" /></html:submit>
		<br/>
		<br/>
		<br/>
		</logic:notEmpty>
	</logic:notEmpty>
	
	<html:submit><bean:message key="label.create" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:submit onclick="this.form.method.value='backToStepOne'; return true;"><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel onclick="this.form.method.value='listProcesses'; return true;"><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>

</fr:form>
