<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ page import="net.sourceforge.fenixedu.domain.accessControl.UserGroupTypes"%>
<%@ page import="net.sourceforge.fenixedu.domain.accessControl.UserGroup"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<bean:define id="viewAction" name="viewAction" type="java.lang.String"/>
<h2><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.userGroupsManagement.title.label"/></h2>
<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.userGroupsManagement.saved.info"/>
<bean:define id="group" type="net.sourceforge.fenixedu.domain.accessControl.UserGroup" name="group"/>
<%
UserGroupTypes groupType = UserGroupTypes.userGroupTypeByClass(group.getClass());
request.setAttribute("groupType",groupType);
%>
<e:define id="readableGroupType" enumeration="groupType" bundle="ENUMERATION_RESOURCES"/>
<table>
	<tr>
		<td>
			<b><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.userGroupsManagement.creating.type.label"/></b>
			:
		</td>
		<td>
			<bean:write name="readableGroupType"/><br/>
		</td>
	</tr>
	<tr>
		<td>
			<b><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.userGroupsManagement.creating.name.label"/></b>
			:
		</td>
		<td>
			<bean:write name="group" property="name"/><br/>
		</td>
	</tr>
	<tr>
		<td>
			<b><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.userGroupsManagement.creating.description.label"/></b>
			:
		</td>
		<td>
			<bean:write name="group" property="description"/><br/>
		</td>
	</tr>
</table>
	<%
	Map params = new HashMap();
	params.put("method","viewElements");
	params.put("groupId",group.getIdInternal());
	request.setAttribute("params",params);
	 %>
<html:link  name="params" module="/cms" action="/userGroupsManagement" target="_blank" ><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.userGroupsManagement.viewElements.link"/></html:link><br/>
<html:link  module="/cms" page="/userGroupsManagement.do?method=prepare" ><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.userGroupsManagement.label"/></html:link><br/>