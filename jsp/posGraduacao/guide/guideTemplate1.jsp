<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<html>
    <head>
    	<title><bean:message key="title.masterDegree.administrativeOffice.printGuide"/></title>
    </head>

    <body>
     <bean:define id="guide" name="<%= SessionConstants.GUIDE %>" scope="session" />
    <table width="100%" height="100%" border="0">
    <tr><td>
     <table width="100%" border="0" valign="top">
      <tr> 
        <td height="100" colspan="2">
          <table border="0" width="100%" height="104" align="center" cellpadding="0" cellspacing="0">
            <tr> 
              <td width="50" height="100">
               <img src="/posgrad/Imagens/istlogo2.gif" width="50" height="104" border="0"/> 
              </td>
              <td>
                &nbsp;
              </td>
              <td>
                <table border="0" width="100%" height="100%">
                  <tr align="left"> 
                    <td>&nbsp;<b>INSTITUTO SUPERIOR T�CNICO</b><br>
                      &nbsp;<b>Secretaria da P�s-Gradua��o</b><br>
                      &nbsp;<b>Centro de Custo 0212</b>
                      <hr size="1">
                    </td>
                  </tr>
                  <tr> 
                    <td align="right" valign="top"> <b>Guia de Pagamento N�: </b> 
                     <bean:write name="guide" property="number"/> 
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
	</table>

	</td>
	</tr>
    <tr><td>

	<table width="100%" border="0">
	 <tr>
	 <td>
      <table width="100%" border="0">
          <tr>
            <td width="20%"><strong>Processo de:</strong></td>
            <td width="80%">&nbsp;</td>
          </tr>
          <tr>
            <td> <bean:message key="label.masterDegree.administrativeOffice.requesterName"/> </td>
            <td> <bean:write name="guide" property="infoPerson.nome"/> </td>
          </tr>
          <tr>
            <td> <bean:message key="label.masterDegree.administrativeOffice.degree"/> </td>
            <td> <bean:write name="guide" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.nome"/> </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
    
          <tr> 
            <td width="30%"><strong>Entidade Pagadora:</strong> </td>
            <td width="70%" >&nbsp;</td>
          </tr>
          <tr> 
            <td><bean:message key="label.masterDegree.administrativeOffice.contributorNumber"/></td>
            <td><bean:write name="guide" property="infoContributor.contributorNumber"/></td>
          </tr>
          <tr> 
            <td><bean:message key="label.masterDegree.administrativeOffice.contributorName"/></td>
            <td><bean:write name="guide" property="infoContributor.contributorName"/></td>
          </tr>
          <tr> 
            <td><bean:message key="label.masterDegree.administrativeOffice.contributorAddress"/></td>
            <td><bean:write name="guide" property="infoContributor.contributorAddress"/></td>
          </tr>
	  </table>
	 </td>
	 </tr>
	 <tr>
	 <td> 
	   <table align="right">
        	<logic:iterate id="guideEntry" name="guide" property="infoGuideEntries" >
        	  <tr>
    			<td><bean:write name="guideEntry" property="documentType"/>&nbsp;<bean:write name="guideEntry" property="description"/></td>
    			<td>.........................................</td>&nbsp;
    			<td><bean:write name="guideEntry" property="price"/>&nbsp;<bean:message key="label.currencySymbol"/></td>
        	  </tr>
        	</logic:iterate >
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
    	<tr>
    	  	<td><strong>A liquidar a import�ncia de </strong></td>
   			<td>_____________________</td>&nbsp;
   			<td><strong><bean:write name="guide" property="total"/>&nbsp;<bean:message key="label.currencySymbol"/></strong></td>
    	</tr>
	   </table>
	 </td>
	 </tr>
	 <tr>
	 <td>
     <table valign="bottom" width="100%">
       <tr>
        <td>&nbsp;</td>
         <td colspan="2" valign="bottom">
           &nbsp;<div align="center">&nbsp;</div>
           <div align="center">&nbsp;</div>
           <div align="center"><b>O Funcion�rio</b> <br>
            <br>
            <br>
           </div>
          <hr align="center" width="300" size="1">
         </td>
       </tr>
	 </table>
	 </td>
	 </tr>
	</table>

	 </td>
	 </tr>
     <tr>	 
	 <td>



    <table width="100%" border="0">
     <tr>	 
	 <td>
	 	<table align="center" width="90%" valign="bottom">
	      <tr>
          <td colspan="2" valign="bottom" >
            <div align="center">
              <font size="2"> Documento processado por computador. S� � v�lido como recibo ap�s o carimbo de pago e devidamente assinado.</font> 
            </div>
            <hr size="1" color="#000000" width="90%">
            <div align="center">
              <font size="2"> Av. Rovisco Pais, 1 1049-001 Lisboa Codex Telefone: 218417336 Fax: 218419531 Contribuinte N�: 501507930</font>
            </div>
          </td>
          </tr>
        </table>
     </td>	 
	 </tr>
	</table>

    </td>
    </tr>
    </table>
    
	
    </body>
</html>