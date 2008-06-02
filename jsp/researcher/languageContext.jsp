<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="enum" %>

<logic:present role="RESEARCHER">	
	<div style="float: right;">
		<html:form action="/changeLocaleTo.do">
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.changeLanguage"/>:
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.windowLocation" property="windowLocation" value=""/>
		 	<enum:labelValues id="values" enumeration="pt.utl.ist.fenix.tools.util.i18n.Language" bundle="ENUMERATION_RESOURCES" />
		 	<html:select bundle="HTMLALT_RESOURCES" property="newLanguage" onchange="this.form.windowLocation.value=window.location; this.form.submit();" value="<%= pt.utl.ist.fenix.tools.util.i18n.Language.getLanguage().toString() %>">
				<html:options collection="values" property="value" labelProperty="label"/>
			</html:select>
			<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="select.newLanguage">
				<bean:message key="button.submit"/>
			</html:submit>
		</html:form>
	</div>
	<script type="text/javascript">
	hideButtons();
	</script>
</logic:present>
