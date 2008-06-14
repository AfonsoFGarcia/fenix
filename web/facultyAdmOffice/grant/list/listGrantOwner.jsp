<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<em><bean:message key="label.facultyAdmOffice.portal.name"/></em>
<h2><bean:message key="label.grant.owner.list"/></h2>

<%-- Presenting errors --%>
<logic:messagesPresent>
	<html:errors/>
</logic:messagesPresent>

<logic:messagesNotPresent>

<table>
<tr>
	<logic:present name="beforeSpan">
	<td>
        <html:link page='<%= "/listGrantOwner.do?method=prepareFirstTimeListGrantOwner&amp;spanNumber=" + request.getAttribute("beforeSpan") + "&amp;orderBy=" + request.getAttribute("orderBy") + "&amp;totalElements=" + request.getAttribute("totalElements") %>' > 
		   	<bean:message key="link.grant.owner.list.before.page"/>
		</html:link>
	</td>
	</logic:present>
	<td>
<html:form action="/listGrantOwner" style="display:inline">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareListGrantOwner"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<%-- span attributes --%>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.orderBy" property="orderBy"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.totalElements" property="totalElements"/>
	<html:text bundle="HTMLALT_RESOURCES" altKey="text.spanNumber" property="spanNumber" size="2"/>/<bean:write name="numberOfSpans"/>
</html:form>
	</td>
	<logic:present name="afterSpan">
	<td>
        <html:link page='<%= "/listGrantOwner.do?method=prepareFirstTimeListGrantOwner&amp;spanNumber=" + request.getAttribute("afterSpan") + "&amp;orderBy=" + request.getAttribute("orderBy") + "&amp;totalElements=" + request.getAttribute("totalElements") %>' > 
		   	<bean:message key="link.grant.owner.list.after.page"/>
		</html:link>
	</td>
	</logic:present>
</tr>
</table>

<logic:present name="listGrantOwner">

    <table class="tstyle4">
    <%-- Table with list grant owner description rows --%>
    <tr>
        <th>
	        <html:link page='<%= "/listGrantOwner.do?method=prepareFirstTimeListGrantOwner&amp;spanNumber=" + request.getAttribute("spanNumber") +  "&amp;orderBy=orderByNumber&amp;totalElements=" + request.getAttribute("totalElements") %>' > 
	            <bean:message key="label.list.grant.owner.number"/>
			</html:link>
        </th>
        <th>
	        <html:link page='<%= "/listGrantOwner.do?method=prepareFirstTimeListGrantOwner&amp;spanNumber=" + request.getAttribute("spanNumber") + "&amp;orderBy=orderByFirstName&amp;totalElements=" + request.getAttribute("totalElements") %>' > 
	            <bean:message key="label.list.grant.owner.first.name"/>
			</html:link>
        </th>
        <th>
	        <%-- <html:link page='<%= "/listGrantOwner.do?method=prepareFirstTimeListGrantOwner&amp;spanNumber=" + request.getAttribute("spanNumber") + "&amp;orderBy=orderByFirstName&amp;totalElements=" + request.getAttribute("totalElements") %>' > --%> 
	            <bean:message key="label.list.grant.owner.last.name"/>
			<%-- </html:link> --%>
        </th>
        <th></th>
    </tr>   
    
    <%-- Table with result of search --%>
    <logic:iterate id="infoListGrantOwnerByOrder" name="listGrantOwner">
        <tr>
            <td class="acenter">
	        	<logic:present name="infoListGrantOwnerByOrder" property="grantOwnerNumber">
		            <bean:write name="infoListGrantOwnerByOrder" property="grantOwnerNumber"/>
	            </logic:present>
            </td>
            <td>
	        	<logic:present name="infoListGrantOwnerByOrder" property="firstName">
		            <bean:write name="infoListGrantOwnerByOrder" property="firstName"/>
	            </logic:present>
            </td>
            <td>
	        	<logic:present name="infoListGrantOwnerByOrder" property="lastName">
		            <bean:write name="infoListGrantOwnerByOrder" property="lastName"/>
	            </logic:present>
            </td>
            <td>
	            <%-- Show all the information of a grant owner --%>
                <bean:define id="idGrantOwner" name="infoListGrantOwnerByOrder" property="grantOwnerId"/>
                <html:link page='<%= "/listGrantOwner.do?method=showGrantOwner&amp;grantOwnerId=" + idGrantOwner.toString() %>' > 
                    <bean:message key="link.grant.owner.show" />
                </html:link>        
            </td>       
        </tr>
    </logic:iterate>
    </table>
</logic:present>


<table>
<tr>
	<logic:present name="beforeSpan">
	<td>
        <html:link page='<%= "/listGrantOwner.do?method=prepareFirstTimeListGrantOwner&amp;spanNumber=" + request.getAttribute("beforeSpan") + "&amp;orderBy=" + request.getAttribute("orderBy") + "&amp;totalElements=" + request.getAttribute("totalElements") %>' > 
		   	<bean:message key="link.grant.owner.list.before.page"/>
		</html:link>
	</td>
	</logic:present>
	<td>
	
	<html:form action="/listGrantOwner" style="display:inline">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareListGrantOwner"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<%-- span attributes --%>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.orderBy" property="orderBy"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.totalElements" property="totalElements"/>
	<html:text bundle="HTMLALT_RESOURCES" altKey="text.spanNumber" property="spanNumber" size="2"/>/<bean:write name="numberOfSpans"/>
	</html:form>
	
	</td>
	<logic:present name="afterSpan">
	<td>
        <html:link page='<%= "/listGrantOwner.do?method=prepareFirstTimeListGrantOwner&amp;spanNumber=" + request.getAttribute("afterSpan") + "&amp;orderBy=" + request.getAttribute("orderBy") + "&amp;totalElements=" + request.getAttribute("totalElements") %>' > 
		   	<bean:message key="link.grant.owner.list.after.page"/>
		</html:link>
	</td>
	</logic:present>
</tr>
</table>

</logic:messagesNotPresent>