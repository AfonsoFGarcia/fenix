<%@ page language="java" %>
<%@ page import="DataBeans.InfoAnnouncement" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<br />
<table width="100%">
<tr>
<td class="infoop"><bean:message key="label.announcement.explanation" /></td>
</tr>
</table>
<br />
<logic:present name="siteView"> 
<bean:define id="bodyComponent" name="siteView" property="component"/>
<bean:define id="announcementsList" name="bodyComponent" property="announcements"/>
<table width="100%">
    <html:form action="/announcementManagementAction">
            <tr>
                <td>
					<div class="gen-button">
						<html:link page="<%= "/announcementManagementAction.do?method=prepareCreateAnnouncement&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
							<bean:message key="label.insertAnnouncement" />
						</html:link></div>
					<br />
					<br />
                </td>
            </tr>
            
            <logic:iterate id="announcement" name="announcementsList" >
                <tr>
                    <td>
                        <strong><bean:write name="announcement" property="title"/></strong>
                    </td>
                </tr>
               	<tr>
                    <td>
						<span class="px9"><bean:write name="announcement" property="creationDateFormatted"/> | <bean:message key="label.lastModificationDate" /><bean:write name="announcement" property="lastModifiedDateFormatted"/></span>
                    </td>
                </tr>
                <tr>
                    <td>
						<bean:write name="announcement" property="information" filter="false"/>
                        <br />
                        <br />
                    </td>
                </tr>
                <tr>
                    <td>
						<div class="gen-button">
							<bean:define id="announcementCode" name="announcement" property="idInternal" />
							<html:link page="<%= "/announcementManagementAction.do?method=prepareEditAnnouncement&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;announcementCode=" + announcementCode %>">
								<bean:message key="button.edit" /> 
							</html:link></div>
						<div class="gen-button">
							<html:link page="<%= "/announcementManagementAction.do?method=deleteAnnouncement&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;announcementCode=" + announcementCode %>">
								<bean:message key="button.delete" />
							</html:link></div>
    	                <br />
    	                <br />
                    </td>
                </tr>
            </logic:iterate>
    </html:form>
</table>
</logic:present> 