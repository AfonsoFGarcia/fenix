<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<h3><bean:message key="title.insertAnnouncement"/></h3>
<html:form action="/announcementManagementAction" focus="title" >
	<html:hidden property="page" value="1"/>
	<table border="0" >
        	<tr>
        		<td>
				<h2><bean:message key="label.title" /></h2>
				</td>
				<td>
					<html:text name="insertAnnouncementForm" property="title" >
					</html:text>
				</td>
				<td><span class="error"><html:errors property="title"/></span></td>
			</tr>
        	<tr>
        		<td>
					<h2><bean:message key="label.information" /></h2>	
				</td>
				<td>
					<html:text name="insertAnnouncementForm" property="information" >
					</html:text>
				</td>
				<td><span class="error"><html:errors property="information"/></span></td>
			</tr>
        </table>

	<br>
<h3>	<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
	</html:reset>
	<html:hidden property="method" value="createAnnouncement" />
	<html:submit styleClass="inputbutton"> <bean:message key="button.save" />
	</html:submit>    </h3>

</html:form>