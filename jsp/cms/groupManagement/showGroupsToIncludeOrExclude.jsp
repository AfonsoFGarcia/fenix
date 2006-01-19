<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ page import="net.sourceforge.fenixedu.domain.accessControl.GroupTypes"%>
<%@ page import="net.sourceforge.fenixedu.domain.accessControl.Group"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<logic:present name="person">
	<bean:define id="userGroupTypeToAdd" type="net.sourceforge.fenixedu.domain.accessControl.GroupTypes" name="userGroupTypeToAdd"/>
	<e:define id="userGroupTypeToAddString" enumeration="userGroupTypeToAdd" bundle="ENUMERATION_RESOURCES"/>
    
	<h2><bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.title.label"/></h2>
	<bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.creating.label"/> <b><bean:write name="userGroupTypeToAddString"/></b><br/>
	<b><bean:message  bundle="CMS_RESOURCES" key="cms.name.label"/>:</b> <bean:write name="userGroupForm" property="name"/><br/>
	<b><bean:message  bundle="CMS_RESOURCES" key="cms.description.label"/>:</b> <bean:write name="userGroupForm" property="description"/><br/>

	<bean:define id="groupsIterator" type="java.util.Iterator" scope="request" name="person" property="personalGroupsIterator"/>
	<bean:define id="numberOfGroups" type="java.lang.Integer" property="personalGroupsCount" name="person"/>	
	
	<logic:greaterThan name="numberOfGroups" value="0">	
		<html:form action="/differenceGroupOperationsManagement" method="get">
        
        		<html:hidden property="method" value="createGroup"/>
        		<html:hidden property="userGroupType"/>
        		<html:hidden property="name"/>
        		<html:hidden property="description"/>
            
            <h3><bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.includeGroups.title.label"/></h3>
            
			<table width="100%">
				<tr>
					<td class="listClasses-header">&nbsp;
					</td>
					<td class="listClasses-header"><bean:message  bundle="CMS_RESOURCES" key="cms.name.label"/>
					</td>
					<td class="listClasses-header"><bean:message  bundle="CMS_RESOURCES" key="cms.description.label"/>
					</td>
					<td class="listClasses-header"><bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.type.label"/>
					</td>
					<td class="listClasses-header"><bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.groupCardinality.label"/>
					</td>			
					<td class="listClasses-header">&nbsp;
					</td>
				</tr>			
				<logic:iterate id="group" name="person" property="personalGroupsIterator" type="net.sourceforge.fenixedu.domain.PersonalGroup">
					<bean:define id="readableGroupType" value="<%= GroupTypes.userGroupTypeByClass(group.getGroup().getClass()).toString() %>"/>
                    <bean:define id="groupSize" name="group" property="group.elementsCount" type="java.lang.Integer"/>
                    
					<tr>
						<td class="listClasses">
							<input type="checkbox" name="selectedGroups" value="<%= group.getIdInternal().toString() %>"/> 
						</td>
						<td class="listClasses"><bean:write name="group" property="name"/></td>
						<td class="listClasses"><bean:write name="group" property="description"/></td>
						<td class="listClasses"><bean:message bundle="CMS_RESOURCES" name="readableGroupType" bundle="ENUMERATION_RESOURCES"/></td>
						<%
        						Map params = new HashMap();
        						params.put("method","viewElements");
        						params.put("groupId",group.getIdInternal());
                            
        						request.setAttribute("params", params);
						 %>
						<td class="listClasses"><bean:write name="groupSize"/> <bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.elements.label"/></td>
						<td class="listClasses"><html:link  name="params" module="/cms" action="/personalGroupsManagement" target="_blank" ><bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.viewElements.link"/></html:link></td>
					</tr>			
				</logic:iterate>
			</table>
            
            <h3><bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.excludeGroups.title.label"/></h3>
            
            <table width="100%">
                <tr>
                    <td class="listClasses-header">&nbsp;
                    </td>
                    <td class="listClasses-header"><bean:message  bundle="CMS_RESOURCES" key="cms.name.label"/>
                    </td>
                    <td class="listClasses-header"><bean:message  bundle="CMS_RESOURCES" key="cms.description.label"/>
                    </td>
                    <td class="listClasses-header"><bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.type.label"/>
                    </td>
                    <td class="listClasses-header"><bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.groupCardinality.label"/>
                    </td>           
                    <td class="listClasses-header">&nbsp;
                    </td>
                </tr>           
                <logic:iterate id="group" name="person" property="personalGroupsIterator" type="net.sourceforge.fenixedu.domain.PersonalGroup">
                    <bean:define id="readableGroupType" value="<%= GroupTypes.userGroupTypeByClass(group.getGroup().getClass()).toString() %>"/>
                    <bean:define id="groupSize" name="group" property="group.elementsCount" type="java.lang.Integer"/>

                    <tr>
                        <td class="listClasses">
                            <input type="checkbox" name="excludeGroups" value="<%= group.getIdInternal().toString() %>"/> 
                        </td>
                        <td class="listClasses"><bean:write name="group" property="name"/></td>
                        <td class="listClasses"><bean:write name="group" property="description"/></td>
                        <td class="listClasses"><bean:message bundle="CMS_RESOURCES" name="readableGroupType" bundle="ENUMERATION_RESOURCES"/></td>
                        <%
                                Map params = new HashMap();
                                params.put("method","viewElements");
                                params.put("groupId",group.getIdInternal());

                                request.setAttribute("params", params);
                         %>
                        <td class="listClasses"><bean:write name="groupSize"/> <bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.elements.label"/></td>
                        <td class="listClasses"><html:link  name="params" module="/cms" action="/personalGroupsManagement" target="_blank" ><bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.viewElements.link"/></html:link></td>
                    </tr>           
                </logic:iterate>
            </table>
            
            <br/>
			<html:submit>
				<bean:message  bundle="CMS_RESOURCES" key="cms.save.button"/>
			</html:submit>
		</html:form>
	</logic:greaterThan>
</logic:present>