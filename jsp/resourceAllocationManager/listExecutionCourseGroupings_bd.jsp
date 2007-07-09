<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:form action="/listExecutionCourseGroupings">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="selectExecutionPeriod"/>

	<em><bean:message key="label.manager.executionCourses"/></em>
	<h2><bean:message key="label.execution.course.groupings" bundle="SOP_RESOURCES"/></h2>

	<p class="mtop2">
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriodID" property="executionPeriodID" onchange="this.form.submit();">
			<logic:notPresent name="executionPeriod">
				<html:option value=""></html:option>
			</logic:notPresent>
			<html:options collection="executionPeriods" labelProperty="qualifiedName" property="idInternal"/>
		</html:select>
	</p>
	<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.submit"/>
	</html:submit>
</html:form>

<logic:present name="executionPeriod">
	<p>
		<html:link page="/listExecutionCourseGroupings.do?method=downloadExecutionCourseGroupings"
				paramId="executionPeriodID" paramName="executionPeriod" paramProperty="idInternal">
			<bean:message key="link.downloadExcelSpreadSheet"/>
			<html:img border="0" src="<%= request.getContextPath() + "/images/excel.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES"/>
		</html:link>
	</p>
</logic:present>