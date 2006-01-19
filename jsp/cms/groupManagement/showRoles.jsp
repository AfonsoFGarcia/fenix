<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

<logic:present name="person">
	<bean:define id="userGroupTypeToAdd" type="net.sourceforge.fenixedu.domain.accessControl.GroupTypes" name="userGroupTypeToAdd"/>
	<e:define id="userGroupTypeToAddString" enumeration="userGroupTypeToAdd" bundle="ENUMERATION_RESOURCES"/>
    
	<h2><bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.title.label"/></h2>
	<bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.creating.label"/> <b><bean:write name="userGroupTypeToAddString"/></b><br/>
	<b><bean:message  bundle="CMS_RESOURCES" key="cms.name.label"/>:</b> <bean:write name="userGroupForm" property="name"/><br/>
	<b><bean:message  bundle="CMS_RESOURCES" key="cms.description.label"/>:</b> <bean:write name="userGroupForm" property="description"/><br/>

	<logic:notEmpty name="roles">	
		<html:form action="/roleGroupOperationsManagement" method="get">
        
        		<html:hidden property="method" value="createGroup"/>
        		<html:hidden property="userGroupType"/>
        		<html:hidden property="name"/>
        		<html:hidden property="description"/>
            
			<table width="100%">
				<tr>
					<td class="listClasses-header">&nbsp;
					</td>
					<td class="listClasses-header"><bean:message  bundle="CMS_RESOURCES" key="cms.name.label"/>
					</td>
				</tr>			
				<logic:iterate id="role" name="roles" type="net.sourceforge.fenixedu.domain.Role">
					<tr>
						<td class="listClasses">
							<input type="radio" name="selectedRole" value="<%= role.getIdInternal() %>"/> 
						</td>
						<td class="listClasses"><bean:message name="role" bundle="ENUMERATION_RESOURCES" property="roleType.name"/></td>
					</tr>
				</logic:iterate>
			</table>
            
            <br/>
			<html:submit>
				<bean:message  bundle="CMS_RESOURCES" key="cms.save.button"/>
			</html:submit>
		</html:form>
	</logic:notEmpty>
</logic:present>