<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="java.util.Date" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>


    <div align="center">
      <font color="#023264" size="-1">
        <h2>          A sua situaï¿?ï¿?o actual não lhe permite alterar a informação da sua candidatura !         </h2>
      </font>
      <hr/>
    </div>  
   
   <table>
   
    <logic:present name="<%= PresentationConstants.CANDIDATE_SITUATION %>">
		<bean:define id="situation" name="<%= PresentationConstants.CANDIDATE_SITUATION %>" />    
        <!-- Situacao da Candidatura -->
        <tr>
	        <td><h2><bean:message key="label.candidate.applicationInfoSituation" /></h2></td>
        </tr>
        
        <!-- Situacao -->
        <tr>
            <td><bean:message key="label.candidate.infoCandidateSituation" /></td>
            <td><bean:write name="situation" property="situation"/></td>
        </tr>
        
        <!-- Data da Situacao -->
        <tr>
            <td><bean:message key="label.candidate.infoCandidateSituationDate" /></td>
            <logic:present name="situation" property="date" >
	            <bean:define id="date" name="situation" property="date" />
				<td><%= Data.format2DayMonthYear((Date) date) %></td>   
			</logic:present>
        </tr>
        
        <!-- Observacoes -->
        <tr>
            <td><bean:message key="label.candidate.infoCandidateSituationRemarks" /></td>
            <td><bean:write name="situation" property="remarks"/></td>
        </tr>
    </logic:present>
   </table>

