<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.createMarkSheet"/></h2>
<br/>
<h3><u><bean:message key="label.createMarkSheet.step.one"/></u> &gt; <bean:message key="label.createMarkSheet.step.two"/></h3>

<logic:messagesPresent message="true">
	<ul>
	<html:messages id="messages" message="true">
		<li><span class="error0"><bean:write name="messages" /></span></li>
	</html:messages>
	</ul>
	<br/>
</logic:messagesPresent>

<fr:edit id="edit"
		 name="edit"
		 type="net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementCreateBean"
		 schema="markSheet.create.step.one"
		 action="/createMarkSheet.do?method=createMarkSheetStepOne">
	<fr:destination name="postBack" path="/createMarkSheet.do?method=prepareSearchMarkSheetPostBack"/>
	<fr:destination name="invalid" path="/createMarkSheet.do?method=prepareSearchMarkSheetInvalid"/>
	<fr:layout name="tabular"/>
</fr:edit>
