<%@ page language="java" %>

<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />
	<logic:empty name="component" property="infoGroupPropertiesList">
	<h2><bean:message key="message.infoGroupPropertiesList.not.available" /></h2>
	</logic:empty>
<table border="0" style="text-align: left;">
        <tbody>
        
        <tr>
			<td class="listClasses-header"><bean:message key="label.projectName" />
			</td>
			<td class="listClasses-header"><bean:message key="label.projectDescription" />
			</td>
		</tr>
            <logic:iterate id="infoGroupProperties" name="component" property="infoGroupPropertiesList" >
                <tr>
                    <td class="listClasses">
                        <br>
                        <b>
							<li><html:link page="<%= "/viewSite.do" + "?method=viewProjectStudentGroupsAction&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" paramId="groupProperties" paramName="infoGroupProperties" paramProperty="idInternal">
								<bean:write name="infoGroupProperties" property="name"/></b>
							</html:link>
							</li>
							
                    </td>
                    
                     <td class="listClasses">
                    <html:textarea name="infoGroupProperties" property="projectDescription" cols="30" rows="4"/>
                   
                	</td>
                </tr>

            </logic:iterate>
        </tbody>
</table>
</logic:present>

<logic:notPresent name="siteView" property="component">
<h4>
<bean:message key="message.infoGroupPropertiesList.not.available" />
</h4>
</logic:notPresent>