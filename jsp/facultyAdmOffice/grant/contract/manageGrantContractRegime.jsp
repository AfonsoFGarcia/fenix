<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>


<%-- Presenting errors --%>
<logic:messagesPresent>
<center><b><bean:message key="label.grant.contract.regime.manage"/></b></center>
<span class="error">
	<html:errors/>
</span><br/>
</logic:messagesPresent>

<logic:messagesNotPresent>

<%--  CONTRACT REGIMES --%>    
<p class="infoselected">
	<b><bean:message key="label.grant.contract.regime.information"/></b><br/>
    <bean:message key="label.grant.owner.number"/>:&nbsp;<bean:write name="grantOwnerNumber"/><br/>
    <bean:message key="label.grant.contract.contractnumber"/>:&nbsp;<bean:write name="grantContractNumber"/>
</p>


<%-- LISTA DE REGIMES DE CONTRACTO ACTIVOS --%>

<logic:present name="infoGrantActiveContractRegimeList">
	<b><bean:message key="label.grant.contract.regime.active.list"/></b><br/>
    <table border="0" cellspacing="1" cellpadding="1">
    <%-- Table with contract regimes description rows --%>
    <tr>
        <td class="listClasses-header">
            <bean:message key="label.grant.contract.regime.beginDate"/>
        </td>
        <td class="listClasses-header">
            <bean:message key="label.grant.contract.regime.endDate"/>
        </td>
        <td class="listClasses-header">
            <bean:message key="label.grant.contract.orientationTeacher"/>
        </td>
        <td class="listClasses-header">&nbsp;</td>
    </tr>   
    <%-- Table with result of search --%>
    <logic:iterate id="infoGrantContractRegime" name="infoGrantActiveContractRegimeList">
        <tr>
        <td class="listClasses">
               <logic:present name="infoGrantContractRegime" property="dateBeginContract">
                    <dt:format pattern="dd-MM-yyyy">
                        <bean:write name="infoGrantContractRegime" property="dateBeginContract.time"/>
                    </dt:format>
			   </logic:present>
                <logic:notPresent name="infoGrantContractRegime" property="dateBeginContract">
                    &nbsp;
                </logic:notPresent>
            </td>
            <td class="listClasses">
                <logic:present name="infoGrantContractRegime" property="dateEndContract">
                    <dt:format pattern="dd-MM-yyyy">
                        <bean:write name="infoGrantContractRegime" property="dateEndContract.time"/>
                    </dt:format>
                </logic:present>
                <logic:notPresent name="infoGrantContractRegime" property="dateBeginContract">
                    &nbsp;
                </logic:notPresent>
            </td>
            <td class="listClasses">
                <logic:present name="infoGrantContractRegime" property="infoTeacher">
                        <bean:write name="infoGrantContractRegime" property="infoTeacher.teacherNumber"/>
                </logic:present>
                <logic:notPresent name="infoGrantContractRegime" property="infoTeacher">
                    &nbsp;
                </logic:notPresent>
            </td>
            <td class="listClasses">
                <bean:define id="idContractRegime" name="infoGrantContractRegime" property="idInternal"/>
                <html:link page='<%= "/editGrantContractRegime.do?method=prepareEditGrantContractRegime&amp;grantContractRegimeId=" + idContractRegime %>' > 
                    <bean:message key="link.grant.contract.regime.edit" />
                </html:link>        
            </td>
        </tr>
    </logic:iterate>
    </table>
</logic:present>

<br/><br/>

<%-- If there are no contract regimes --%>
<logic:notPresent name="infoGrantActiveContractRegimeList">
    <p align="center"><bean:message key="message.grant.contract.regime.nonExistent" /></p><br/><br/>
</logic:notPresent>

<bean:message key="message.grant.contract.regime.creation"/>:&nbsp;
<html:link page='<%= "/editGrantContractRegime.do?method=prepareEditGrantContractRegime&amp;idContract=" + request.getAttribute("idContract").toString() %>'>
	<bean:message key="link.create.contract.regime"/>
</html:link>
<br/><bean:message key="message.grant.contract.regime.creation.note" /><br/><br/>

<%-- LISTA DE SUBSIDIOS ACTIVOS --%>

<logic:present name="infoGrantNotActiveContractRegimeList">
<br/><br/>
	<b><bean:message key="label.grant.contract.regime.notactive.list"/></b><br/>
    <table border="0" cellspacing="1" cellpadding="1">
    <%-- Table with contract regimes description rows --%>
    <tr>
        <td class="listClasses-header">
            <bean:message key="label.grant.contract.regime.beginDate"/>
        </td>
        <td class="listClasses-header">
            <bean:message key="label.grant.contract.regime.endDate"/>
        </td>
        <td class="listClasses-header">
            <bean:message key="label.grant.contract.orientationTeacher"/>
        </td>
        <td class="listClasses-header">&nbsp;</td>
    </tr>   
    <%-- Table with result of search --%>
    <logic:iterate id="infoGrantContractRegime" name="infoGrantNotActiveContractRegimeList">
        <tr>
        <td class="listClasses">
               <logic:present name="infoGrantContractRegime" property="dateBeginContract">
                    <dt:format pattern="dd-MM-yyyy">
                        <bean:write name="infoGrantContractRegime" property="dateBeginContract.time"/>
                    </dt:format>
			   </logic:present>
                <logic:notPresent name="infoGrantContractRegime" property="dateBeginContract">
                    &nbsp;
                </logic:notPresent>
            </td>
            <td class="listClasses">
                <logic:present name="infoGrantContractRegime" property="dateEndContract">
                    <dt:format pattern="dd-MM-yyyy">
                        <bean:write name="infoGrantContractRegime" property="dateEndContract.time"/>
                    </dt:format>
                </logic:present>
                <logic:notPresent name="infoGrantContractRegime" property="dateBeginContract">
                    &nbsp;
                </logic:notPresent>
            </td>
            <td class="listClasses">
                <logic:present name="infoGrantContractRegime" property="infoTeacher">
                        <bean:write name="infoGrantContractRegime" property="infoTeacher.teacherNumber"/>
                </logic:present>
                <logic:notPresent name="infoGrantContractRegime" property="infoTeacher">
                    &nbsp;
                </logic:notPresent>
            </td>
            <td class="listClasses">
                <bean:define id="idContractRegime" name="infoGrantContractRegime" property="idInternal"/>
                <html:link page='<%= "/editGrantContractRegime.do?method=prepareEditGrantContractRegime&amp;grantContractRegimeId=" + idContractRegime %>' > 
                    <bean:message key="link.grant.contract.regime.edit" />
                </html:link>        
            </td>
        </tr>
    </logic:iterate>
    </table>
</logic:present>

<br/><br/><br/>
<center>
<html:form action="/manageGrantContract" style="display:inline">
	<html:hidden property="method" value="prepareManageGrantContractForm"/>
	<html:hidden property="page" value="1"/>
	<html:hidden property="idInternal" value='<%= request.getAttribute("idGrantOwner").toString() %>'/>
	<html:submit styleClass="inputbutton" style="display:inline">
		<bean:message key="button.manageGrantContract"/>
	</html:submit>
</html:form>		
</center>

</logic:messagesNotPresent>