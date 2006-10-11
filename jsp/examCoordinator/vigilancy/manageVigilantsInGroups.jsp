<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em>
<h2><bean:message key="label.vigilancy.manageVigilantsInGroups.title" bundle="VIGILANCY_RESOURCES"/></h2>

<ul>
	<li><html:link  page="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement&show=groups"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.back"/></html:link></li>
</ul>

<logic:notEmpty name="vigilants">
<div class="warning0">
	<bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.unableToRemoveVigilantsDueToConvokes"/>
	<fr:view name="vigilants">
		<fr:layout>
			<fr:property name="eachLayout" value="values"/>
			<fr:property name="eachSchema" value="presentVigilant"/>
			<fr:property name="classes" value="mbottom05"/>
		</fr:layout>
	</fr:view>
</div>

</logic:notEmpty>

<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="VIGILANCY_RESOURCES">
		<p>
			<span class="error0"><bean:write name="messages" /></span>
		</p>
	</html:messages>
</logic:messagesPresent>

<fr:form action="/vigilancy/vigilantGroupManagement.do?method=addVigilantsToGroup">

<strong><bean:message key="label.vigilancy.manageDepartmentVigilants" bundle="VIGILANCY_RESOURCES"/></strong>:
<fr:edit id="bounds" name="bounds">
<fr:layout name="vigilantsInGroup-render">
	<fr:property name="personSchema" value="presentPersonWithCatAndNumber"/>
	<fr:property name="classes" value="tstyle1"/>
</fr:layout>
<fr:destination name="cancel" path="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement&show=groups"/>
</fr:edit>

<strong><bean:message key="label.vigilancy.manageExternalVigilants" bundle="VIGILANCY_RESOURCES"/></strong>:
<fr:edit id="externalBounds" name="externalBounds">
<fr:layout name="vigilantsInGroup-render">
	<fr:property name="personSchema" value="presentExternalPersonWithCatAndNumber"/>
	<fr:property name="classes" value="tstyle1"/>
</fr:layout>

</fr:edit>
<html:submit><bean:message key="label.submit" bundle="VIGILANCY_RESOURCES"/></html:submit>
<html:cancel><bean:message key="label.cancel" bundle="VIGILANCY_RESOURCES"/></html:cancel>

</fr:form>

<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="VIGILANCY_RESOURCES">
		<p>
			<span class="error0"><bean:write name="messages" /></span>
		</p>
	</html:messages>
</logic:messagesPresent>

<p class="mtop3"><strong><bean:message key="label.vigilanclabel.vigilancy.isConvokabley.externalPersonToGroup" bundle="VIGILANCY_RESOURCES"/></strong></p>
<p>

<fr:form id="addSingleVigilant" action="/vigilancy/vigilantGroupManagement.do?method=addVigilantToGroupByUsername">
<fr:edit name="bean" id="addExternalPersonToGroup" schema="addExternalPerson"
	nested="true">
	<fr:destination name="cancel" path="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement&show=groups"/>
</fr:edit>
<p class="mtop0">
	<span class="switchInline"><a href="javascript:document.getElementById('addSingleVigilant').submit()"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.add"/></a></span>
	<html:submit styleClass="switchNone"><bean:message key="label.vigilancy.add" bundle="VIGILANCY_RESOURCES"/></html:submit>
</p>
</fr:form>

