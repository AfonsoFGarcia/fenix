<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<table>
<tr><td>
	<html:link page="/alternativeSite.do?method=management">
		<bean:message key="link.alternative"/>
	</html:link>
</tr>
<tr><td>
	<html:link page="/viewSite.do">
	<bean:message key="link.home"/>
</html:link>
</tr>
<tr><td><html:link page="/accessAnnouncementManagementAction.do">
	<bean:message key="link.announcements"/>
</html:link></td>
</tr>	
<tr><td><html:link page="/objectivesManagerDA.do?method=acessObjectives">
	<bean:message key="link.objectives"/>
</html:link></td>
</tr>	
<tr><td><html:link page="/programManagerDA.do?method=acessProgram">
	<bean:message key="link.program"/>
</html:link></td>
</tr>

<tr><td><html:link page="/bibliographicReferenceManager.do?method=viewBibliographicReference">
	<bean:message key="link.bibliography"/>
</html:link></td>
</tr>
<tr><td>
	<html:link page="/teachersManagerDA.do?method=viewTeachersByProfessorship">
	<bean:message key="link.teachers"/>
</html:link></td>
</tr>
<tr><td>
	<html:link page="/logoff.do">
	<bean:message key="link.logout"/>
</html:link></td>
</tr>

</table>

<logic:present name="<%= SessionConstants.LAST_ANNOUNCEMENT %>" >
	<br><br><br>
	<b><bean:message key="label.lastAnnouncement"/></b><br><br>
	<b><bean:write name="<%= SessionConstants.LAST_ANNOUNCEMENT %>" property="title" /></b><br>
	<bean:write name="<%= SessionConstants.LAST_ANNOUNCEMENT %>" property="information" /><br>
</logic:present>