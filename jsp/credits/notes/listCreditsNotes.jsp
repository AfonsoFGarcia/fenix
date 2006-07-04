<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<html:form action="/manageCreditsNotes">
	<html:hidden property="method" value="editNote"/>
	<html:hidden property="executionPeriodId"/>
	<html:hidden property="teacherId"/>
	<html:hidden property="noteType"/>
	
	<bean:define id="noteType" name="creditsNotesForm" property="noteType" type="java.lang.String" />

	<em><bean:message key="<%="label."+noteType %>" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></em>
	<h2><bean:message key="label.observations" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h2>

	<span class="error"><html:errors/></span>
	<html:messages id="message" message="true">
		<span class="error">
			<bean:write name="message"/>
		</span>
	</html:messages>
	

<%
	try {
		final String noteTypeHelp = java.util.ResourceBundle.getBundle("resources.TeacherCreditsSheetResources", request.getLocale()).getString("label."+noteType+".help");
%>
		<div class="infoop2">	
			<%= noteTypeHelp %>			
		</div>
<%
	} catch (java.util.MissingResourceException ex) {

	}
%>

<p class="mtop1 mbottom05"><bean:message key="label.observations" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</p>
<html:textarea cols="60" rows="8" property="<%= noteType %>" styleClass="mbottom05"/>

	
<div class="mtop1">
	<html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
	<html:submit styleClass="inputbutton" onclick="this.form.method.value='cancel';this.form.page.value='0'">
		<bean:message key="button.cancel"/>
	</html:submit>
</div>

</html:form>

