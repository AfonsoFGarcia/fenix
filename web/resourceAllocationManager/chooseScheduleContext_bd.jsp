<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@page
    import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants"%><html:xhtml />

<em><bean:message key="title.resourceAllocationManager.management" /></em>
<h2><bean:message key="title.manage.schedule" /></h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<p class="mtop15 mbottom05"><bean:message key="label.chooseDegreeAndYear" />:</p>

<fr:form action="/chooseContext.do?method=choose">

	<fr:edit name="<%=SessionConstants.CONTEXT_SELECTION_BEAN%>" schema="degreeContext.choose">
		<fr:destination name="degreePostBack" path="/chooseContext.do?method=choosePostBack" />
		<fr:destination name="yearPostBack" path="/chooseContext.do?method=choosePostBack" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:edit>

	<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Submeter"
		styleClass="inputbutton">
		<bean:message key="label.next" />
	</html:submit></p>
</fr:form>