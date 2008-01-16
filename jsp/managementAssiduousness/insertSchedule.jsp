<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="label.schedule"/></h2>

<bean:define id="employee" name="UserView" property="person.employee" />

<span class="error0 mtop0">
	<html:messages id="validation" message="true">
	<bean:write name="validation" />
	</html:messages>
</span>

<logic:present name="workScheduleTypeFactory">
<bean:define id="method" value="insertSchedule"/>

<logic:notEmpty name="workScheduleTypeFactory" property="oldIdInternal">
	<bean:define id="method" value="editSchedule"/>
</logic:notEmpty>		

	<fr:form action="/assiduousnessParametrization.do">
	<html:hidden property="method" value="<%= method.toString() %>"/>
	<table class="tstyle3 thright thlight mbottom05">
		<tr>
			<th><bean:message key="label.acronym" bundle="ASSIDUOUSNESS_RESOURCES"/>:<span class="required">*</span></th>
			<td>
				<fr:edit name="workScheduleTypeFactory" id="acronym"
					schema="workScheduleTypeFactory.acronym"
					type="net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory">
					<fr:layout name="flow">
						<fr:property name="labelExcluded" value="true"/>
					</fr:layout>
					<fr:hidden slot="modifiedBy" name="UserView" property="person.employee" />
				</fr:edit>
			</td>
		</tr>
		
		<tr>
			<th>
				<bean:message key="label.scheduleClockingType" bundle="ASSIDUOUSNESS_RESOURCES"/>:
				
			</th>
			<td>
				<fr:edit name="workScheduleTypeFactory" id="scheduleClockingType" slot="scheduleClockingType"
					type="net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory">
					<fr:layout name="menu-select">
						<fr:property name="classes" value="thlight thcenter"/>
						<fr:property name="eachLayout" value="this-does-not-exist" />
						<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.assiduousness.ScheduleClockingTypeAsProvider" />
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		
		<tr>
			<th><bean:message key="label.validity" bundle="ASSIDUOUSNESS_RESOURCES"/>: <span class="required">*</span></th>
			<td>
				<fr:edit name="workScheduleTypeFactory" id="validity"
					schema="workScheduleTypeFactory.validity"
					type="net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory" layout="tabular-row">
					<fr:layout>
						<fr:property name="classes" value="tstylenone thlight thcenter"/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		
		<tr>
			<th><bean:message key="label.dayTimeSchedule" bundle="ASSIDUOUSNESS_RESOURCES"/>:<span class="required">*</span></th>
			<td>
				<fr:edit name="workScheduleTypeFactory" id="dayTime" schema="workScheduleTypeFactory.dayTime"
					type="net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory" layout="tabular-row">
					<fr:layout>
						<fr:property name="classes" value="tstylenone thlight thcenter"/>
						<fr:property name="columnClasses" value=",,acenter width6em"/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		
		<tr>
			<th><bean:message key="label.clockingTimeSchedule" bundle="ASSIDUOUSNESS_RESOURCES"/>:<span class="required">*</span></th>
			<td>
				<fr:edit name="workScheduleTypeFactory" id="clockingTime"
					schema="workScheduleTypeFactory.clockingTime"
					type="net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory" layout="tabular-row">
					<fr:layout>
						<fr:property name="headerClasses" value="hidden"/>
						<fr:property name="classes" value="tstylenone"/>
						<fr:property name="columnClasses" value=",,acenter width6em"/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		</tr>
		
		<tr>
			<th><bean:message key="label.normalFirstWorkPeriod" bundle="ASSIDUOUSNESS_RESOURCES"/>:<span class="required">*</span></th>
			<td>
				<fr:edit name="workScheduleTypeFactory" id="normalWorkFirstPeriod"
					schema="workScheduleTypeFactory.normalWorkFirstPeriod"
					type="net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory" layout="tabular-row">
					<fr:layout>
						<fr:property name="headerClasses" value="hidden"/>
						<fr:property name="classes" value="tstylenone"/>
						<fr:property name="columnClasses" value=",,acenter width6em"/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		</tr>
		
		<tr>
			<th><bean:message key="label.normalSecondWorkPeriod" bundle="ASSIDUOUSNESS_RESOURCES"/>:</th>
			<td>
				<fr:edit name="workScheduleTypeFactory" id="normalWorkSecondPeriod"
					schema="workScheduleTypeFactory.normalWorkSecondPeriod"
					type="net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory" layout="tabular-row">
					<fr:layout>
						<fr:property name="headerClasses" value="hidden"/>
						<fr:property name="classes" value="tstylenone"/>
						<fr:property name="columnClasses" value=",,acenter width6em"/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		</tr>
		
		<tr>
			<th><bean:message key="label.fixedFirstWorkPeriod" bundle="ASSIDUOUSNESS_RESOURCES"/>:</th>
			<td>
				<fr:edit name="workScheduleTypeFactory" id="fixedWorkFirstPeriod"
					schema="workScheduleTypeFactory.fixedWorkFirstPeriod"
					type="net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory" layout="tabular-row">
					<fr:layout>
						<fr:property name="headerClasses" value="hidden"/>
						<fr:property name="classes" value="tstylenone"/>
						<fr:property name="columnClasses" value=",,acenter width6em"/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		</tr>
		
		<tr>
			<th><bean:message key="label.fixedSecondWorkPeriod" bundle="ASSIDUOUSNESS_RESOURCES"/>:</th>
			<td>
				<fr:edit name="workScheduleTypeFactory" id="fixedWorkSecondPeriod"
					schema="workScheduleTypeFactory.fixedWorkSecondPeriod"
					type="net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory" layout="tabular-row">
					<fr:layout>
						<fr:property name="headerClasses" value="hidden"/>
						<fr:property name="classes" value="tstylenone"/>
						<fr:property name="columnClasses" value=",,acenter width6em"/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		
		<tr>
			<th><bean:message key="label.mealPeriod" bundle="ASSIDUOUSNESS_RESOURCES"/>:</th>
			<td>
				<fr:edit name="workScheduleTypeFactory" id="mealInterval"
					schema="workScheduleTypeFactory.mealInterval"
					type="net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory" layout="tabular-row">
					<fr:layout>
						<fr:property name="classes" value="tstylenone thlight thcenter"/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		
		<tr>
			<th><bean:message key="label.mealPeriod" bundle="ASSIDUOUSNESS_RESOURCES"/>:</th>
			<td>
				<fr:edit name="workScheduleTypeFactory" id="mealDiscount"
					schema="workScheduleTypeFactory.mealDiscount"
					type="net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory" layout="tabular-row">
					<fr:layout>
						<fr:property name="classes" value="tstylenone thlight thcenter"/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
	</table>
	
		<p class="smalltxt mtop05"><bean:message key="message.requiredField" bundle="ASSIDUOUSNESS_RESOURCES"/></p>

		<p class="mtop1">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible">
				<bean:message key="button.confirm" />
			</html:submit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible" onclick="this.form.method.value='cancelSchedule'">
				<bean:message key="button.cancel" />
			</html:submit>
		</p>
	</fr:form>
</logic:present>
