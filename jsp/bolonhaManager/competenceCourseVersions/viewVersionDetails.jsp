<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="competenceCourseID" name="changeRequest" property="competenceCourse.idInternal"/>

<em><bean:message key="label.manage.versions" bundle="BOLONHA_MANAGER_RESOURCES"/></em>
<h2><bean:message key="label.view.versions" bundle="BOLONHA_MANAGER_RESOURCES"/></h2>

<ul>
	<li>
		<html:link page="<%= "/competenceCourses/manageVersions.do?method=showVersions&competenceCourseID=" + competenceCourseID %>"><bean:message key="label.back" bundle="APPLICATION_RESOURCES"/></html:link>
	</li>
</ul>

<logic:notEmpty name="changeRequest">
		
	<p class="mbottom05"><strong><bean:message key="label.generalInformation" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong>:</p>
	<fr:view name="changeRequest" schema="present.competenceCourseInformation.change.request">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright thtop mtop05"/>
			<fr:property name="columnClasses" value="width150px,"/>
		</fr:layout>
	</fr:view>
	
	<p class="mtop15 mbottom05"><strong><bean:message key="label.loadInformation" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong>:</p>
	<fr:view name="changeRequest" schema="present.semester1.loads">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
		</fr:layout>
	</fr:view>
	
	<logic:equal name="changeRequest" property="regime" value="ANUAL">
		<fr:view name="changeRequest" schema="present.semester2.loads">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
			</fr:layout>
		</fr:view>			
	</logic:equal>

	<p class="mtop15 mbottom05"><strong><bean:message key="label.bibliographyInformation" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong>:</p>

	<logic:notEmpty name="changeRequest" property="bibliographicReferences.bibliographicReferencesSortedByOrder">

		<logic:notEmpty name="changeRequest"  property="bibliographicReferences.mainBibliographicReferences">
			<p class="mbottom05"><bean:message key="label.primaryBibliography" bundle="BOLONHA_MANAGER_RESOURCES"/></p>
			<fr:view name="changeRequest" property="bibliographicReferences.mainBibliographicReferences" schema="view.reference">	
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thlight thright thcenter mtop05"/>
				</fr:layout>
			</fr:view>
		</logic:notEmpty>

		<logic:notEmpty name="changeRequest" property="bibliographicReferences.secondaryBibliographicReferences">		
			<p class="mbottom05"><bean:message key="label.secundaryBibliography" bundle="BOLONHA_MANAGER_RESOURCES"/></p>
			<fr:view name="changeRequest" property="bibliographicReferences.secondaryBibliographicReferences" schema="view.reference">	
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thlight thright thcenter mtop05"/>
				</fr:layout>
			</fr:view>
	   </logic:notEmpty>

	</logic:notEmpty>

	<logic:empty name="changeRequest" property="bibliographicReferences.bibliographicReferencesSortedByOrder">
		<bean:message key="label.no.bibliography" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
	</logic:empty>
	
</logic:notEmpty>

<logic:empty name="changeRequest">
	<bean:message key="label.changeRequest.notAvailable" bundle="BOLONHA_MANAGER_RESOURCES"/>
</logic:empty>