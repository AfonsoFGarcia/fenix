<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<span class="error"><html:errors property="error.default" /></span>
<h2><bean:message key="message.createRootSection" /></h2>
<br />

<logic:present name="siteView">
<bean:define id="component" name="siteView" property="component"/>
<logic:present name="component" property="rootSections">
	<bean:define id="sections" name="component" property="rootSections"/>
</logic:present>
<logic:present name="component" property="regularSections">
	<bean:define id="sections" name="component" property="regularSections"/>
</logic:present>

<html:form action="/createSection">
<html:hidden property="page" value="1"/>	
<table>
<tr>
	<td>
		<bean:message key="message.sectionName"/>
	</td>
	<td>
		<html:text property="name" />
			<span class="error"><html:errors property="name"/></span>
	</td>
</tr>
<tr>
	<logic:present name="sections">
	<td>		
		<bean:message key="message.sectionOrder"/>		
	</td>
	<td>
		<html:select name="sectionForm" property="sectionOrder">
			<html:option value="-1"><bean:message key="label.end"/></html:option>
			<html:options collection="sections" labelProperty="name" property="sectionOrder"/>
			
		</html:select>
		<span class="error"><html:errors property="sectionOrder"/></span>
	</td>
	</logic:present>
	<logic:notPresent name="sections">
		<html:hidden property="sectionOrder" value="0"/>
	</logic:notPresent>
</tr>
</table>
<br />

<html:hidden property="method" value="createSection"/>
<html:hidden property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />

<logic:present name="component" property="regularSections">
	<html:hidden property="currentSectionCode" value="<%= pageContext.findAttribute("currentSectionCode").toString() %>" />
</logic:present>

<html:submit styleClass="inputbutton">
	<bean:message key="button.save"/>
</html:submit>
<html:reset  styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>			
</html:form>
</logic:present>