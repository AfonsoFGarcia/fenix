<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<span class="error"><html:errors/>
	<logic:present name="errors">
		<bean:write name="errors" filter="true" />
	</logic:present	>
</span>

<logic:present name="siteView">
	<bean:define id="infoSiteItems" name="siteView" property="component"/>
	<bean:define id="item" name="infoSiteItems" property="item"/>
	<bean:define id="items" name="infoSiteItems" property="items"/>
	
<html:form action="/editItem">
	<html:hidden property="page" value="1"/>
	<logic:present name="verEditor">
		<html:hidden property="information" />
	</logic:present>
	
<table width="100%">
<tr>
	<td>
		<bean:message key="message.itemName"/>
	</td>
	<td>
		<html:text name="item" property="name"/> <span class="error"><html:errors property="name"/></span>
	</td>
</tr>

<tr>
	<td>
		<bean:message key="message.sectionOrder"/>		
	</td>
	<td>
		<bean:define id="itemOrder" name="item" property="itemOrder" type="java.lang.Integer"/>
		<html:select name="item" property="itemOrder" size="1">
			<logic:iterate id="otherItem" name="items" type="net.sourceforge.fenixedu.dataTransferObject.InfoItem">
				<bean:define id="otherItemOrder" type="java.lang.String"><%= String.valueOf(otherItem.getItemOrder().intValue()) %></bean:define>
				<bean:define id="selected" value=""></bean:define>
				<logic:equal name="otherItemOrder" value="<%= String.valueOf(itemOrder.intValue() + 1) %>">
					<bean:define id="selected">selected="selected"</bean:define>
					<bean:define id="isSelected">yes</bean:define>
				</logic:equal>
				<option value="<bean:write name='otherItem' property='itemOrder'/>" <bean:write name="selected"/>><bean:write name="otherItem" property="name"/></option>
			</logic:iterate>
			<bean:define id="selected" value=""></bean:define>
			<logic:notPresent name="isSelected">
					<bean:define id="selected">selected="selected"</bean:define>
			</logic:notPresent>
				<option value="-1" <bean:write name="selected"/>><bean:message key="label.end" /></option>
		</html:select>
		<span class="error"><html:errors property="itemOrder"/></span>
	</td>
</tr>

<tr>
	<td>
		<bean:message key="message.itemUrgent"/>:
	</td>
	<td>
		<html:select name="item" property="urgent" size="1" >				
				<html:option key="label.no" value="false"></html:option>
				<html:option key="label.yes" value="true"></html:option>
		</html:select>
		<span class="error"><html:errors property="urgent"/></span>
	</td>
</tr>
<tr>
	<td>&nbsp;</td>
</tr>
<tr>
	<td>&nbsp;</td>
	<td colspan='2'>
		<bean:message key="label.editor"/>
		<html:radio property="editor" value="true" onclick="this.form.method.value='prepareEditItem';this.form.page.value=0;this.form.submit();"/>
		&nbsp;
		<bean:message key="label.plain.text"/>
		<html:radio property="editor" value="false" onclick="this.form.method.value='prepareEditItem';this.form.page.value=0;this.form.submit();"/>					
	</td>

</tr>
<tr>
	<td>&nbsp;</td>
</tr>
<tr>
	<td valign="top">
		<bean:message key="message.itemInformation"/>:
	</td>
	<td>
		<logic:present name="verEditor">
			<script language="JavaScript" type="text/javascript"> 
			<!--
				initEditor();		
			//-->
			</script>
					
			<noscript>JavaScript must be enable to use this form <br> </noscript>
				
			<script language="JavaScript" type="text/javascript"> 
			<!--
				writeTextEditor(300, 300, document.forms[0].information.value);		
			//-->
			</script>	
		</logic:present>
		<logic:notPresent name="verEditor">
			<html:textarea property="information" rows="20" cols="80"/>
		</logic:notPresent>	
		<span class="error"><html:errors property="information"/></span>
	</td>
</tr>
</table>			
<br/>
<html:hidden property="method" value="editItem" />
<html:hidden property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<bean:define id="itemCode" name="item" property="idInternal"/>
<html:hidden property="itemCode" value="<%= itemCode.toString() %>" />
<bean:define id="currentSection" name="item" property="infoSection"/>
<bean:define id="currentSectionCode" name="currentSection" property="idInternal"/>
<html:hidden property="currentSectionCode" value="<%= currentSectionCode.toString() %>" />
	
<logic:present name="verEditor">
	<html:submit styleClass="inputbutton" onclick="this.form.information.value=update()">
		<bean:message key="button.save"/>
	</html:submit>
</logic:present>	
<logic:notPresent name="verEditor">
	<html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
</logic:notPresent>	
<html:reset  styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>
</html:form>
</logic:present>