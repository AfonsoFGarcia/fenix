<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<html:html>
  <head>
    <title>EP2002 - Emitir Certificado</title>
  </head>
  <body>
    <table>
  	    <tr>
          <td>
          		<B><bean:message key="label.chefe.artigo"/> 
          		CHEFE DE SEC��O DE P�S-GRADUA��O DO INSTITUTO 
          		SUPERIOR T�CNICO DA</B>
          </td>
         </tr>
         <tr>
           <td>
          		<B>UNIVERSIDADE T�CNICA DE LISBOA</B>
          	</td>
        </tr>
        <tr>
          <td>
          		<B> CERTIFICA, a requerimento do interessado que do seu processo individual </B>
          </td>
        </tr>
        <tr>
          <td>
          		<B>organizado e arquivado nesta Secretaria, consta que:</B>
          </td>
        </tr>
        <tr>
                <td align="left">
                     <B><bean:write name="infoStudent" property="infoPerson.nome"/></B>
                </td>
         </tr>
           <tr>
                <td align="left">
                     <B>filho de <bean:write name="infoStudent" property="infoPerson.nomePai"/></B>
                </td>
         </tr>
                  <tr>
                <td align="left">
                     <B>e de <bean:write name="infoStudent" property="infoPerson.nomeMae"/></B>
                </td>
         </tr>
         <tr>
                <td align="left">
                     <B>natural de <bean:write name="infoStudent" property="infoPerson.distritoNaturalidade"/></B>
                </td>
         </tr>
          <tr>
                <td align="left">
                     <B>de nacionalidade <bean:write name="infoStudent" property="infoPerson.nacionalidade"/></B>
                </td>
         </tr>
		<tr>
				<td>
					<B>tem Matricula no ano lectivo de ??ano lectivo??, no curso 
							de mestrado em:</B>
				</td>
		</tr>
		<tr>
				<td>
					<B><bean:write name="infoCourse" property="name"/></B>
				</td>
		</tr>
		<tr>
				<td>
						<B>O curso tem a dura��o m�xima de quatro semestres, compreendendo a frequ�ncia do </B>
				</td>
		</tr>
		<tr>
				<td>
						<B>curso de especializa��o e a apresenta��o de uma disserta��o original</B>
				</td>
		</tr>
    	<tr>
          		<td>
          			<jsp:include page="dataObtainCertificate.jsp"/>
          		</td>
         </tr>
    	</table>
  </body>
  </html:html>
