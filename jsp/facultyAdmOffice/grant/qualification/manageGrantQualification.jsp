<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<%-- Presenting errors --%>
<logic:messagesPresent>
<center><b><bean:message key="label.grant.qualification.information"/></b></center>
<span class="error">
	<html:errors/>
</span><br/>
</logic:messagesPresent>

<logic:messagesNotPresent>

<%--  CONTRATOS --%>    
<p class="infoselected">
	<b><bean:message key="label.grant.qualification.information"/></b><br/>
    <bean:message key="label.grant.owner.number"/>:&nbsp;<bean:write name="idInternal"/><br/>
    <logic:present name="grantOwnerName">
	    <bean:message key="label.grant.owner.infoperson.name"/>:&nbsp;<bean:write name="grantOwnerName"/><br/>
	</logic:present></p>

<logic:present name="infoQualificationList">
    <table border="0" cellspacing="1" cellpadding="1">
    <%-- Table with contract description rows --%>
    <tr>
        <td class="listClasses-header">
            <bean:message key="label.grant.qualification.degree"/>
        </td>
        <td class="listClasses-header">
            <bean:message key="label.grant.qualification.school"/>
        </td>
        <td class="listClasses-header">
            <bean:message key="label.grant.qualification.qualificationDate"/>
        </td>
        <td class="listClasses-header">
        	&nbsp;
        </td>
        <td class="listClasses-header">
        	&nbsp;
        </td>
    </tr>   
    <%-- Table with result of search --%>
    <logic:iterate id="infoGrantQualification" name="infoQualificationList">
        <tr>
            <td class="listClasses">&nbsp;
                <bean:write name="infoGrantQualification" property="degree"/>
            </td>
            <td class="listClasses">&nbsp;
                <bean:write name="infoGrantQualification" property="school"/>
            </td>
            <td class="listClasses">
                <logic:present name="infoGrantQualification" property="date">
                    <dt:format pattern="dd-MM-yyyy">
                        <bean:write name="infoGrantQualification" property="date.time"/>
                    </dt:format>
                </logic:present>
                <logic:notPresent name="infoGrantQualification" property="date">
                    ---
                </logic:notPresent>
            </td>
            <td class="listClasses">
                <bean:define id="idQualification" name="infoGrantQualification" property="idInternal"/>
                <html:link page='<%= "/editGrantQualification.do?method=prepareEditGrantQualificationForm&amp;idQualification=" + idQualification + "&amp;idInternal=" + request.getAttribute("idInternal").toString()+ "&amp;load=" + 1 %>' > 
                    <bean:message key="link.grant.qualification.edit" />
                </html:link>
            </td>
            <td class="listClasses">        
                <html:link page='<%= "/editGrantQualification.do?method=doDelete&amp;idQualification=" + idQualification + "&amp;idPerson=" + request.getAttribute("idPerson").toString() + "&amp;idInternal=" + request.getAttribute("idInternal").toString() + "&amp;username=" + request.getAttribute("username").toString() %>' > 
                    <bean:message key="link.grant.qualification.delete" />
                </html:link>        
                
            </td>       
        </tr>
    </logic:iterate>
    </table>
</logic:present>
    
<%-- If there are no qualifications --%>
<logic:notPresent name="infoQualificationList">
    <p align="center"><bean:message key="message.grant.qualification.nonExistentContracts" /></p>
</logic:notPresent>

<br/><br/>

<bean:message key="message.grant.qualification.creation"/>:&nbsp;
<html:link page='<%= "/editGrantQualification.do?method=prepareEditGrantQualificationForm&amp;idPerson=" + request.getAttribute("idPerson").toString() + "&amp;idInternal=" + request.getAttribute("idInternal").toString() + "&amp;username=" + request.getAttribute("username").toString() %>'>
	<bean:message key="label.grant.qualification.create"/>
</html:link>

<br/><br/><br/>
<center>
<html:form action="/manageGrantOwner" style="display:inline">
	<html:hidden property="method" value="prepareManageGrantOwnerForm"/>
	<html:hidden property="page" value="1"/>
	<html:hidden property="idInternal" value='<%= request.getAttribute("idInternal").toString() %>'/>
	<html:submit styleClass="inputbutton" style="display:inline">
		<bean:message key="button.manageGrantOwner"/>
	</html:submit>
</html:form>
</center>

</logic:messagesNotPresent>