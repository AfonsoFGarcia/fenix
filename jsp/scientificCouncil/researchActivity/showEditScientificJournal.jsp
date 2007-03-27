<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="title.scientificCouncil.portalTitle" /></em>
<h2><bean:message key="title.scientificJournal.edit" /></h2>

<br />
<br />
<div class="forminline dinline">
	<fr:form action="/editScientificJournal.do?method=prepare">
		<fr:edit schema="scientific.journal.edit" id="edit" name="pageContainerBean" property="selected" type="net.sourceforge.fenixedu.domain.research.activity.ScientificJournal">
			<fr:destination name="invalid" path="/editScientificJournal.do?method=invalid"/>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
		<fr:edit id="pageContainerBean" name="pageContainerBean" visible="false"/>
		<html:submit><bean:message key="submit"/></html:submit>
	</fr:form>
	<fr:form action="/editScientificJournal.do?method=back">
		<fr:edit id="back" name="pageContainerBean" visible="false"/>
		<html:submit><bean:message key="return"/></html:submit>
	</fr:form>
</div>