<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

    
    <h2><bean:message key="label.certificate.declaration.create" /></h2>
   <span class="error"><html:errors/></span>
   
   <table>
    <html:form action="/chooseFinalResultInfoAction?method=choose">
   	  <html:hidden property="page" value="1"/>
   	  
       <!-- Requester Number -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.requesterNumber"/> </td>
         <td><html:text property="requesterNumber" value=""/></td>
       </tr>

	

      <!-- Graduation Type --> 
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.graduationType"/> </td>
         <td>
            <html:select property="graduationType">
                <html:options collection="<%= SessionConstants.SPECIALIZATIONS %>" property="value" labelProperty="label" />
             </html:select>          
         </td>
        </tr>
        <tr>
        <td></td>
        <td></td>
        </tr>
 
		<tr>
        <td> <html:submit value="Submeter" property="ok"/></td>
        <td></td>
        </tr>
        
    </html:form>
   </table>

  


