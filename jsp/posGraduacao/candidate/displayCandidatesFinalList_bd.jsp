<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="java.util.List" %>

<span class="error"><html:errors/></span>
	<br />
	<h2>Listas finais de Candidatos</h2>
	<br />

	<logic:iterate id="group" name="infoGroup" >
		<h2><bean:write name="group" property="situationName"/></h2>
  		<table>
        	<logic:iterate id="candidate" name="group" property="candidates">
        		<tr>
        			<td><bean:write name="candidate" property="candidateName"/></td>
        			<td><bean:write name="candidate" property="situationName"/></td>
        			<td><bean:write name="candidate" property="remarks"/></td>
        		    <logic:present name="candidate" property="orderPosition">
        				<td><bean:write name="candidate" property="orderPosition"/></td>
        			</logic:present>
        		</tr>
        	</logic:iterate> 
   		</table>
	</logic:iterate> 

	
    <logic:equal name="confirmation" value="YES">
        <html:form action="/displayListToSelectCandidates.do?method=aprove">
        	<logic:iterate id="candidate" name="candidatesID" indexId="indexCandidate">
                <html:hidden property='<%= "candidatesID[" + indexCandidate + "]" %>' />					
                <html:hidden property='<%= "situations[" + indexCandidate + "]" %>' />					
                <html:hidden property='<%= "remarks[" + indexCandidate + "]" %>' />					
                <html:hidden property='<%= "substitutes[" + indexCandidate + "]" %>' />					
        	</logic:iterate> 
    	    <html:submit value="Confirmar" styleClass="inputbutton" property="OK"/>
    	    <html:submit value="Cancelar" styleClass="inputbutton" property="NotOK"/>
    	</html:form>
	</logic:equal>
	<logic:notEqual name="confirmation" value="YES">
		<h2><bean:message key="label.candidateApprovalDone" /> </h2>
	</logic:notEqual>