<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2><bean:message key="title.teachers"/></h2>

<div class="infoop2">
	<bean:message key="label.teachers.explanation" />
	<p><bean:message key="label.teachers.specialTeacherWarning" /></p>
</div>

<p>
	<span class="error0"><!-- Error messages go here --><html:errors /></span>	
</p>

<logic:present name="siteView">
<bean:define id="infoSiteTeachers" name="siteView" property="component"/>
<bean:define id="teachersList" name="infoSiteTeachers" property="infoTeachers"/>
<bean:define id="isResponsible" name="infoSiteTeachers" property="isResponsible"/>

<logic:equal name="isResponsible" value="true">
<ul class="mvert15">
	<li>
		<html:link page="<%= "/teacherManagerDA.do?method=prepareAssociateTeacher&amp;objectCode=" + pageContext.findAttribute("objectCode") %>"><bean:message key="link.addTeacher"/></html:link>
	</li>
</ul> 
</logic:equal>
<table class="tstyle2 tdcenter">
	<tr>
		<th><bean:message key="label.istid" bundle="APPLICATION_RESOURCES" /></th>
		<th><bean:message key="label.name" /></th>
		<logic:equal name="isResponsible" value="true">
			<th><bean:message key="label.teacher.responsible" /></th>			
			<th><bean:message key="message.edit" /></th>    
		</logic:equal>
	</tr>	
	<logic:iterate id="professorship" name="listPersons">
	<bean:define id="person" name="professorship" property="person" />
	<tr>
		<td><bean:write name="person"  property="istUsername" /></td>
		<td><bean:write name="person" property="name" /></td>	
		<logic:equal name="isResponsible" value="true">
		<logic:equal name="professorship" property="responsibleFor" value="false">
			<td>
				<bean:message key="label.no.capitalized" />
			</td>
			<bean:define id="teacherCode" name="person" property="istUsername"/>		
			<td>
				<html:link page="<%= "/teachersManagerDA.do?method=removeTeacher&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;teacherCode=" + teacherCode %>">
					<bean:message key="link.removeTeacher"/>
				</html:link>
			</td>
		</logic:equal>
		<logic:equal name="professorship" property="responsibleFor" value="true">
			<td>
				<bean:message key="label.yes.capitalized" />
			</td>
			<bean:define id="teacherCode" name="person" property="istUsername"/>		
			<td>
				<bean:message key="label.notAvailable" />
			</td>
		</logic:equal>
		</logic:equal>
	</tr>
	</logic:iterate>	
</table>
</logic:present>