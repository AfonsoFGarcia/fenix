<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<h2><bean:message key="title.accessDelegation" /></h2>
<script language="Javascript" type="text/javascript">
<!--
var disable = false;

function invertStatus(){
	if ( disable == false ) { 
		disable = true; 
	} else { 
		disable = false;
	}
	for (var i=0; i<document.forms[0].projectCodes.length; i++){
		var e = document.forms[0].projectCodes[i];
		if (disable == true) { e.disabled=true; } else { e.disabled=false; }
	}
}

function changeFocus(input) { 
	if( input.value.length == input.maxLength) { 
		next=getIndex(input)+1;
		if (next<document.forms[0].elements.length){
			document.forms[0].elements[next].focus();
		}
	} 
} 

function getIndex(input){
	var index = -1, i = 0; 
	while ( i < input.form.length && index == -1 ) 
	if ( input.form[i] == input ) { 
		index = i; 
	} else { 
		i++; 
	} 
	return index; 
}
// -->
</script>
<br />
<logic:present name="personAccessesList" scope="request">
	<h3><bean:message key="message.personProjectAccess" /></h3>
	<logic:notEmpty name="personAccessesList" scope="request">
		<table>
			<tr>
				<td class="listClasses-header"><bean:message key="label.username" /></td>
				<td class="listClasses-header"><bean:message key="label.name" /></td>
				<td class="listClasses-header"><bean:message key="label.projectNumber" /></td>
				<td class="listClasses-header"><bean:message key="label.acronym" /></td>
				<td class="listClasses-header"><bean:message key="label.beginDate" /></td>
				<td class="listClasses-header"><bean:message key="label.endDate" /></td>
			</tr>
			<logic:iterate id="projectAccess" name="personAccessesList">
				<bean:define id="person" name="projectAccess" property="infoPerson" />
				<bean:define id="username" name="person" property="username" />
				<bean:define id="personCode" name="person" property="idInternal" />
				<bean:define id="projectCode" name="projectAccess" property="keyProject" />
				<bean:define id="infoProject" name="projectAccess" property="infoProject" />
				<tr>
					<td td class="listClasses"><bean:write name="person" property="username" /></td>
					<td td class="listClasses"><bean:write name="person" property="nome" /></td>
					<td td class="listClasses"><bean:write name="infoProject" property="projectCode" /></td>
					<td td class="listClasses"><bean:write name="infoProject" property="title" /></td>
					<td td class="listClasses"><bean:write name="projectAccess" property="beginDateFormatted" /></td>
					<td td class="listClasses"><bean:write name="projectAccess" property="endDateFormatted" /></td>
					<td><html:link page="<%="/projectAccessEdition.do?method=prepareEditProjectAccess&amp;projectCode="+projectCode+"&amp;personCode="+personCode%>">Editar</html:link></td>
					<td><html:link page="<%="/projectAccess.do?method=removeProjectAccess&amp;projectCode="+projectCode+"&amp;username="+username%>">Remover</html:link></td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
	<logic:empty name="personAccessesList" scope="request">
		<span class="error"><bean:message key="message.person.noProjectAccesses" /></span>
	</logic:empty>
	<br />
	<br />
	<br />
	<br />
	<logic:present name="projectList">
		<h3><bean:message key="message.availableProjects" /></h3>
		<logic:notEmpty name="projectList">
			<html:form action="/projectAccess" focus="beginDay">
				<html:hidden property="username" value="<%=(pageContext.findAttribute("username")).toString()%>" />
				<html:hidden property="method" value="delegateAccess" />
				<html:hidden property="page" value="2" />
				<logic:present name="noProjectsSelected">
					<span class="error"><bean:message key="errors.requiredProject" /></span>
				</logic:present>
				<table>
					<tr>
						<td class="listClasses-header"><bean:message key="label.projectNumber" /></td>
						<td class="listClasses-header"><bean:message key="label.acronym" /></td>
						<td class="listClasses-header"><html:multibox property="projectCode" onclick="invertStatus()" value="-1" /></td>
					</tr>
					<logic:iterate id="project" name="projectList">
						<bean:define id="projectCode" name="project" property="projectCode" />
						<tr>
							<td class="listClasses"><bean:write name="project" property="projectIdentification" /></td>
							<td class="listClasses"><bean:write name="project" property="title" /></td>
							<td class="listClasses"><html:multibox property="projectCodes">
								<bean:write name="project" property="projectCode" />
							</html:multibox></td>
						</tr>
					</logic:iterate>
				</table>
				<br />
				<br />
				<table>
					<logic:present name="invalidTime">
						<tr>
							<td><span class="error"><bean:message key="errors.invalid.time.interval" /></span></td>
						</tr>
					</logic:present>
					<logic:present name="invalidEndTime">
						<tr>
							<td><span class="error"><bean:message key="errors.invalidEndTime" /></span></td>
						</tr>
					</logic:present>
					<tr>
						<td><span class="error"><html:errors property="beginDay" /><html:errors property="beginMonth" /><html:errors property="beginYear" /></span></td>
					</tr>
					<tr>
						<td><span class="error"><html:errors property="endDay" /><html:errors property="endMonth" /><html:errors property="endYear" /></span></td>
					</tr>
				</table>
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td><bean:message key="message.sinceDate" /><bean:message key="message.dateFormat" /></td>
						<td><html:text maxlength="2" size="2" property="beginDay" onkeyup="changeFocus(this)" /> / <html:text maxlength="2" size="2"
							property="beginMonth" onkeyup="changeFocus(this)" /> / <html:text maxlength="4" size="4" property="beginYear" onkeyup="changeFocus(this)" /></td>
					</tr>
					<tr>
						<td><bean:message key="message.untilDate" /><bean:message key="message.dateFormat" /></td>
						<td><html:text maxlength="2" size="2" property="endDay" onkeyup="changeFocus(this)" /> / <html:text maxlength="2" size="2" property="endMonth"
							onkeyup="changeFocus(this)" /> / <html:text maxlength="4" size="4" property="endYear" onkeyup="changeFocus(this)" /></td>
					</tr>
				</table>
				<br />
				<html:submit styleClass="inputbutton">
					<bean:message key="label.add" />
				</html:submit>
			</html:form>
		</logic:notEmpty>
	</logic:present>
	<logic:empty name="projectList">
		<span class="error"><bean:message key="message.noOtherUserProjects" /></span>
	</logic:empty>
	<br />
	<br />
</logic:present>
