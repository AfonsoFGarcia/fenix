<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="java.util.Date" %>
 
 <html:errors />
 <table width="100%">
 	<tr valign="bottom">
 		<td><h2><bean:message key="label.person.title.personalConsult" bundle="DEFAULT" /></h2></td>
 		<td align="right">
 			<logic:present name="personalInfo">
 				<bean:define id="personID" name="personalInfo" property="idInternal"/>
 				<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/viewPhoto.do?personCode="+personID.toString()%>"/>
 			</logic:present>
 		</td>
 	</tr>
 </table>
 <br />
        <logic:present name="personalInfo">
        <bean:define id="personID" name="personalInfo" property="idInternal"/>
		<bean:define id="studentNumber" name="studentNumber" />
		<html:form action="<%= "/editInformation?method=prepare&personId=" + personID.toString() + "&studentNumber=" + studentNumber.toString() %>">
			<html:submit styleClass="inputbutton">Editar dados</html:submit>
		</html:form>
		<table width="100%" cellpadding="0" cellspacing="0">
          <!-- Dados Pessoais -->
          <tr>
            <td class="infoop" width="25"><span class="emphasis-box">1</span></td>
            <td class="infoop"><strong><bean:message key="label.person.title.personal.info" bundle="DEFAULT" /></strong></td>
          </tr>
		</table>
		<br />
		<table width="100%">
          <!-- Nome -->
          <tr>
            <td width="30%"><bean:message key="label.person.name" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="nome" /></td>
          </tr>
          <!-- Username -->
          <tr>
            <td width="30%"><bean:message key="label.person.username" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="username"/></td> 
          </tr>
          <!-- Sexo -->
          <tr>
            <td width="30%"><bean:message key="label.person.sex" bundle="DEFAULT" /></td>
            <td class="greytxt">
            	<bean:define id="gender" name="personalInfo" property="sexo"/>
            	<bean:message key='<%=gender.toString()%>' bundle="ENUMERATION_RESOURCES"/>
            </td>
          </tr>
 	      <!-- Numero do Documento de Identificacao -->
          <tr>
            <td width="30%"><bean:message key="label.person.identificationDocumentNumber" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="numeroDocumentoIdentificacao"/></td>
          </tr>
          <!-- Tipo do Documento de Identificacao -->
          <tr>
            <td width="30%"><bean:message key="label.person.identificationDocumentType" bundle="DEFAULT" /></td>
            <td class="greytxt">
            	<bean:define id="idType" name="personalInfo" property="tipoDocumentoIdentificacao"/>
            	<bean:message key='<%=idType.toString()%>' bundle="DEFAULT"/>
            </td>
          </tr>
          <!-- Local de Emissao do Documento de Identificacao -->
          <tr>
            <td width="30%"><bean:message key="label.person.identificationDocumentIssuePlace" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="localEmissaoDocumentoIdentificacao"/></td>
          </tr>
          <!-- Data de Emissao do Documento de Identificacao -->
          <tr>
            <td width="30%"><bean:message key="label.person.identificationDocumentIssueDate" bundle="DEFAULT" /></td>
            <logic:present name="personalInfo" property="dataEmissaoDocumentoIdentificacao" >
	            <bean:define id="date" name="personalInfo" property="dataEmissaoDocumentoIdentificacao" />
				<td class="greytxt"><%= Data.format2DayMonthYear((Date) date) %></td>            
			</logic:present>
          </tr>
          <!-- Data de Validade do Documento de Identificacao -->
          <tr>
            <td width="30%"><bean:message key="label.person.identificationDocumentExpirationDate"bundle="DEFAULT" /></td>
            <logic:present name="personalInfo" property="dataValidadeDocumentoIdentificacao" >
	            <bean:define id="date" name="personalInfo" property="dataValidadeDocumentoIdentificacao" />
				<td class="greytxt"><%= Data.format2DayMonthYear((Date) date) %></td>            
			</logic:present>
          </tr>
          <!-- Numero de Contribuinte -->
          <tr>
            <td width="30%"><bean:message key="label.person.contributorNumber" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="numContribuinte"/></td>
          </tr>
          <!-- Profissao -->
          <tr>
            <td width="30%"><bean:message key="label.person.occupation" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="profissao"/></td>
          </tr>
          <!-- Estado Civil -->
          <tr>
            <td width="30%"><bean:message key="label.person.maritalStatus" bundle="DEFAULT" /></td>
            <bean:define id="maritalStatus" name="personalInfo" property="maritalStatus"/>
            <td class="greytxt"><bean:message key='<%= maritalStatus.toString() %>' bundle="DEFAULT" /></td>
          </tr>
		</table>
		<br />
		<table width="100%" cellpadding="0" cellspacing="0">
          <!-- Filia��o -->
          <tr>
          	<td class="infoop" width="25"><span class="emphasis-box">2</span></td>
          	<td class="infoop"><strong><bean:message key="label.person.title.filiation" bundle="DEFAULT" /></strong></td>
          </tr>
		</table>
		<br />
		<table width="100%">
          <!-- Data de Nascimento -->
          <tr>
            <td width="30%"><bean:message key="label.person.birth" bundle="DEFAULT" /></td>
            <logic:present name="personalInfo" property="nascimento" >
	            <bean:define id="date" name="personalInfo" property="nascimento" />
				<td class="greytxt"><%= Data.format2DayMonthYear((Date) date) %></td>            
			</logic:present>
          </tr>
          <!-- Nacionalidade -->
          <tr>
            <td width="30%"><bean:message key="label.person.country" bundle="DEFAULT" /></td>
            <logic:present name="personalInfo"  property="infoPais">
		     	<td class="greytxt"><bean:write name="personalInfo" property="infoPais.nationality"/></td>
	        </logic:present>
	     	<logic:notPresent name="personalInfo"  property="infoPais">
		     	<td class="greytxt"></td>
	        </logic:notPresent>
          </tr>   
          <!-- Freguesia de Naturalidade -->
          <tr>
            <td width="30%"><bean:message key="label.person.birthPlaceParish" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="freguesiaNaturalidade"/></td>
          </tr>
          <!-- Concelho de Naturalidade -->
          <tr>
            <td width="30%"><bean:message key="label.person.birthPlaceMunicipality" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="concelhoNaturalidade"/></td>
          </tr>
          <!-- Distrito de Naturalidade -->
          <tr>
            <td width="30%"><bean:message key="label.person.birthPlaceDistrict" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="distritoNaturalidade"/></td>
          </tr>
          <!-- Nome do Pai -->
          <tr>
            <td width="30%"><bean:message key="label.person.fatherName" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="nomePai"/></td>
          </tr>
          <!-- Nome da Mae -->
          <tr>
            <td width="30%"><bean:message key="label.person.motherName" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="nomeMae"/></td>
          </tr>
		</table>
		<br />
		<table width="100%" cellpadding="0" cellspacing="0">
          <!-- Dados de Residencia -->
          <tr>
            <td class="infoop" width="25"><span class="emphasis-box">3</span></td>
            <td class="infoop"><strong><bean:message key="label.person.title.addressInfo" bundle="DEFAULT" /></strong></td>
          </tr>
		</table>
		<br />
		<table width="100%">
          <!-- Morada -->
          <tr>
            <td width="30%"><bean:message key="label.person.address" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="morada"/></td>
          </tr>
          <!-- Codigo Postal -->
          <tr>
            <td width="30%"><bean:message key="label.person.postCode" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="codigoPostal"/></td>
          </tr>
          <!-- Area do Codigo Postal -->
          <tr>
            <td width="30%"><bean:message key="label.person.areaOfPostCode" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="localidadeCodigoPostal"/></td>
          </tr>
          <!-- Localidade de Residencia -->
          <tr>
            <td width="30%"><bean:message key="label.person.place" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="localidade"/></td>
          </tr>
          <!-- Freguesia de Residencia -->
          <tr>
            <td width="30%"><bean:message key="label.person.addressParish" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="freguesiaMorada"/></td>
          </tr>
          <!-- Concelho de Residencia -->
          <tr>
            <td width="30%"><bean:message key="label.person.addressMunicipality" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="concelhoMorada"/></td>
          </tr>
          <!-- Distrito de Residencia -->
          <tr>
            <td width="30%"><bean:message key="label.person.addressDistrict" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="distritoMorada"/></td>
          </tr>
		</table>
		<br />
		<table width="100%" cellpadding="0" cellspacing="0">
          <!-- Contactos -->
          <tr>
            <td class="infoop" width="25"><span class="emphasis-box">4</span></td>
            <td class="infoop"><strong><bean:message key="label.person.title.contactInfo" bundle="DEFAULT" /></strong></td>
          </tr>
		</table>
		<br />
		<table width="100%">
          <!-- Telefone -->
          <tr>
            <td width="30%"><bean:message key="label.person.telephone" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="telefone"/></td>
          </tr>
          <!-- Telemovel -->
          <tr>
            <td width="30%"><bean:message key="label.person.mobilePhone" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="telemovel"/></td>
          </tr>
          <!-- E-Mail -->
          <tr>
            <td width="30%"><bean:message key="label.person.email" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="email"/></td>
          </tr>
          <!-- WebPage -->
          <tr>
            <td width="30%"><bean:message key="label.person.webSite" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="enderecoWeb"/></td>
          </tr>
        </logic:present>
    </table>
