<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="java.util.List" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>



<html>
  <head>
    <title><bean:message key="title.candidate.chooseCandidate" /></title>
  </head>
  <body>
   <span class="error"><html:errors/></span>

    <bean:define id="candidateList" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE_LIST %>" scope="session" />
    

	<bean:define id="link">/chooseCandidate.do?candidate=</bean:define>

    <%= ((List) candidateList).size()%> <bean:message key="label.masterDegree.administrativeOffice.candidatesFound"/>        
    <br><bean:message key="label.masterDegree.chooseOne"/><br>
    <% if (((List) candidateList).size() != 0) { %>
        	<logic:iterate id="candidate" name="candidateList" indexId="indexCandidate">
            	<bean:define id="candidateLink">
            		<bean:write name="link"/><bean:write name="indexCandidate"/>
            	</bean:define>
                <html:link page='<%= pageContext.findAttribute("candidateLink").toString() %>'>
                    <bean:write name="candidate" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.nome" /> - 
        			<bean:write name="candidate" property="specialization" />
                </html:link>
                <br>
        	</logic:iterate>
	<% } %>
  </body>
</html>
