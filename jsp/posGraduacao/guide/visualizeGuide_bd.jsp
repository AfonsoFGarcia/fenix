<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="java.util.List" %>
<%@ page import="java.lang.Integer" %>

<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoGuideSituation" %>
<%@ page import="DataBeans.InfoGuide" %>
<%@ page import="Util.State" %>
<%@ page import="Util.SituationOfGuide" %>

     <bean:define id="infoGuide" name="<%= SessionConstants.GUIDE %>" scope="request" />
     <bean:define id="number" name="infoGuide" property="number" />
     <bean:define id="year" name="infoGuide" property="year" />
     <bean:define id="version" name="infoGuide" property="version" />

	 <strong>
	 <bean:message key="label.masterDegree.administrativeOffice.guideInformation" 
				   arg0='<%= pageContext.findAttribute("version").toString() %>'
				   arg1='<%= pageContext.findAttribute("number").toString() %>' 
				   arg2='<%= pageContext.findAttribute("year").toString() %>' 
	  />
	 </strong>
	 	
     <table>
     

          <tr>
            <td><bean:message key="label.person.name" /></td>
            <td><bean:write name="infoGuide" property="infoPerson.nome"/></td>
          </tr>

          <tr>
            <td> <bean:message key="label.masterDegree.administrativeOffice.degree"/> </td>
            <td> <bean:write name="infoGuide" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.nome"/> </td>
          </tr>

          <tr> 
            <td><strong>Entidade Pagadora:</strong></td>
            <td>&nbsp;</td>
          </tr>
          <tr> 
            <td><bean:message key="label.masterDegree.administrativeOffice.contributorNumber"/></td>
            <td><bean:write name="infoGuide" property="infoContributor.contributorNumber"/></td>
          </tr>
          <tr> 
            <td><bean:message key="label.masterDegree.administrativeOffice.contributorName"/></td>
            <td><bean:write name="infoGuide" property="infoContributor.contributorName"/></td>
          </tr>
          <tr> 
            <td><bean:message key="label.masterDegree.administrativeOffice.contributorAddress"/></td>
            <td><bean:write name="infoGuide" property="infoContributor.contributorAddress"/></td>
          </tr>

	</table>
	<br>
	<br>
	<table>
		<tr align="center">
			<td><bean:message key="label.masterDegree.administrativeOffice.documentType" /></td>
			<td><bean:message key="label.masterDegree.administrativeOffice.description" /></td>
			<td><bean:message key="label.masterDegree.administrativeOffice.quantity" /></td>
			<td><bean:message key="label.masterDegree.administrativeOffice.price" /></td>
		</tr>

         <logic:iterate id="guideEntry" name="infoGuide" property="infoGuideEntries">
           <tr>
            <td><bean:write name="guideEntry" property="documentType"/></td>
            <td><bean:write name="guideEntry" property="description"/></td>
            <td><bean:write name="guideEntry" property="quantity"/></td>
            <td align="right"><bean:write name="guideEntry" property="price"/> <bean:message key="label.currencySymbol" /></td>
		   </tr>
         </logic:iterate>
         
         <tr>
         	<td></td>
         	<td></td>
         	<td><bean:message key="label.masterDegree.administrativeOffice.guideTotal" />:</td>
         	<td align="right"><bean:write name="infoGuide" property="total"/> <bean:message key="label.currencySymbol" /></td>
         </tr>
     </table>
     
     <br>
     <br>
     <table>
		<tr>
			<td><bean:message key="label.masterDegree.administrativeOffice.creationDate" /></td>
			<td><bean:write name="infoGuide" property="creationDate"/></td>
		</tr>
		<tr>
			<td><bean:message key="label.masterDegree.administrativeOffice.remarks" /></td>
			<td><bean:write name="infoGuide" property="remarks"/></td>
		</tr>
     </table>

	<br>
	<br>

	<strong><bean:message key="label.masterDegree.administrativeOffice.guideSituationList" /></strong>
	
	<br>
	<br>



         <logic:iterate id="guideSituation" name="infoGuide" property="infoGuideSituations">
	      <table>
            <% if (((InfoGuideSituation) guideSituation).getState().equals(new State(State.ACTIVE))) { %>
	            
				<tr>
         	    <td><p><b><center><bean:message key="label.masterDegree.administrativeOffice.activeSituation" /></center></b></p></td>
				</tr>
         	<% } %>
           <tr>
			<td><bean:message key="label.masterDegree.administrativeOffice.remarks" /></td>
            <td><bean:write name="guideSituation" property="remarks"/></td>
           </tr>
           <tr>
			<td><bean:message key="label.masterDegree.administrativeOffice.situation" /></td>
            <td><bean:write name="guideSituation" property="situation"/></td>
           </tr>
           <tr>
			<td><bean:message key="label.masterDegree.administrativeOffice.situationDate" /></td>
            <td><bean:write name="guideSituation" property="date"/></td>
           </tr>
           <% if (((InfoGuideSituation) guideSituation).getSituation().equals(SituationOfGuide.PAYED_TYPE)) { %>
           		<tr>
        			<td><bean:message key="label.masterDegree.administrativeOffice.payment" /></td>
        			<td><bean:write name="infoGuide" property="paymentType"/></td>
        		</tr>
           		<tr>
        			<td><bean:message key="label.masterDegree.administrativeOffice.paymentDate" /></td>
        			<td><bean:write name="infoGuide" property="paymentDate"/></td>
        		</tr>
         	<% } %>
          </table>
          <br><br>
         </logic:iterate>


	<br>	
	<br>

    <bean:define id="arguments"><%= "&" %>page=0<%= "&" %>year=<bean:write name="year"/><%= "&" %>number=<bean:write name="number"/><%= "&" %>version=<bean:write name="version"/>
    </bean:define>
	

			<% List guideList = (List) session.getAttribute(SessionConstants.GUIDE_LIST);
			   InfoGuide guide = (InfoGuide) request.getAttribute(SessionConstants.GUIDE);
			   if(guide.getVersion().equals(new Integer(guideList.size())) && !guide.getInfoGuideSituation().getSituation().equals(SituationOfGuide.ANNULLED_TYPE)) {
			%>	
        	<table>
        		<tr>
        		<td width="30%">
        			<bean:define id="linkChangeSituation">/editGuideSituation.do?method=prepareEditSituation<bean:write name="arguments"/>
                	</bean:define>
        			<html:link page='<%= pageContext.findAttribute("linkChangeSituation").toString() %>'>
        				<bean:message key="link.masterDegree.administrativeOffice.changeGuideSituation" />
        			</html:link>
        		</td>
        		<td width="30%">
        			<bean:define id="linkChangeInformation">/editGuideInformation.do?method=prepareEditInformation<bean:write name="arguments"/>
                	</bean:define>
        			<html:link page='<%= pageContext.findAttribute("linkChangeInformation").toString() %>'>
        				<bean:message key="link.masterDegree.administrativeOffice.changeGuideInformation" />
        			</html:link>
        		</td>
        		</tr>
        	</table> 
        	<% } else { %>
        		<strong><bean:message key="label.masterDegree.administrativeOffice.nonChangeableGuide" /></strong>
        	<% } %>
        	

	