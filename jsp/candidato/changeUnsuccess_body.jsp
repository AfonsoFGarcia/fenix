<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="java.util.Date" %>
<%@ page import="Util.Data" %>


    <div align="center">
      <font color="#023264" size="-1">
        <h2>          A sua situa��o actual n�o lhe permite alterar a informa��o da sua candidatura !         </h2>
      </font>
      <hr>
    </div>  
   
   <table>
   <bean:define id="situation" name="<%= SessionConstants.CANDIDATE_SITUATION %>" scope="session" />
    <logic:present name="situation">
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

