<%@ page language="java" %>
<%@ page import="DataBeans.gesdis.InfoAnnouncement" %>
<%@ page import="java.lang.String" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<table border="0" style="text-align: left;">
    <html:form action="/announcementManagementAction">
        <tbody>
            <tr>
                <td>
                    <br>
					<html:link page="/announcementManagementAction.do?method=createAnnouncement">
						<bean:message key="label.insertAnnouncement" />
					</html:link>
                    &nbsp;&nbsp;
                    <br><br><hr>
                </td>
            </tr>
            <logic:iterate id="announcement" name="Announcements" scope="session" >
                <tr>
                    <td>
                        <br>
                        <h2><bean:write name="announcement" property="title"/></h2>
						<bean:write name="announcement" property="creationDate"/>
                    </td>
                </tr>
                <tr>
                    <td>
						<bean:write name="announcement" property="information"/>
                        <br><br>
                    </td>
                </tr>
                <tr>
                	<td>
                		<bean:message key="label.lastModificationDate" /><bean:write name="announcement" property="lastModifiedDate"/>
						<br><br>
                	</td>
            	</tr>
                <tr>
                    <td>
						<html:link page="/announcementManagementAction.do?method=prepareEditAnnouncement">
							<bean:message key="button.edit" />
						</html:link>
						<html:link page="/announcementManagementAction.do?method=deleteAnnouncement">
							<bean:message key="button.delete" />
						</html:link>
	                    &nbsp;&nbsp;
    	                <br><hr>
                    </td>
                </tr>
            </logic:iterate>
        </tbody>
    </html:form>
</table>