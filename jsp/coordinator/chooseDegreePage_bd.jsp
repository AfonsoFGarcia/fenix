<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="java.util.List" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

   <span class="error"><html:errors/></span>

    <bean:define id="degreeList" name="<%= SessionConstants.MASTER_DEGREE_LIST %>" scope="session" />
	<bean:define id="link">/chooseDegree.do?degree=</bean:define>

    <%= ((List) degreeList).size()%> <bean:message key="label.masterDegree.administrativeOffice.degreesFound"/>        
    <br><bean:message key="label.masterDegree.chooseOne"/><br>
    <% if (((List) degreeList).size() != 0) { %>
        	<logic:iterate id="degree" name="degreeList" indexId="indexDegree">
            	<bean:define id="degreeLink">
            		<bean:write name="link"/><bean:write name="indexDegree"/>
            	</bean:define>
                <html:link page='<%= pageContext.findAttribute("degreeLink").toString() %>'>
                    <bean:write name="degree" property="infoDegreeCurricularPlan.infoDegree.nome" /> - 
                    <bean:write name="degree" property="infoDegreeCurricularPlan.infoDegree.sigla" />
                </html:link>
                <br>
        	</logic:iterate>
	<% } %>
