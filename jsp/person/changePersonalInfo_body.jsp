<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="java.util.Collection" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.RoleType" %>
<%@ page import="DataBeans.InfoRole" %>
<%@ page import="ServidorAplicacao.Servico.UserView" %>

<html>
  <head>
    <title><bean:message key="label.person.title.changePersonalInfo" /></title>
  </head>
  <body>

   <table>
   		<bean:define id="personalInfo" name="<%= SessionConstants.PERSONAL_INFO_KEY %>" scope="session"/>
    	<html:form action="/changePersonalInfoDispatchAction?method=change">
   	    <html:hidden property="page" value="1"/>
   	    
       	<html:hidden property="name" />
      	<html:hidden property="username" />
      	<html:hidden property="sex" />
      	<html:hidden property="identificationDocumentType" />
      	<html:hidden property="identificationDocumentNumber" />
      	<html:hidden property="identificationDocumentIssuePlace" />
      	<html:hidden property="maritalStatus" />
      	<html:hidden property="nationality" />
      	<html:hidden property="fatherName" />
      	<html:hidden property="motherName" />
      	<html:hidden property="birthPlaceParish" />
      	<html:hidden property="birthPlaceMunicipality" />
      	<html:hidden property="birthPlaceDistrict" />
      	<html:hidden property="address" />
      	<html:hidden property="place" />
      	<html:hidden property="postCode" />
      	<html:hidden property="addressParish" />
      	<html:hidden property="addressMunicipality" />
      	<html:hidden property="addressDistrict" />
      	<html:hidden property="telephone" />
      	<html:hidden property="contributorNumber" />
      	<html:hidden property="occupation" />
      	<html:hidden property="birthDay" />
      	<html:hidden property="birthMonth" />
      	<html:hidden property="birthYear" />
      	<html:hidden property="idIssueDateDay" />
      	<html:hidden property="idIssueDateMonth" />
      	<html:hidden property="idIssueDateYear" />
      	<html:hidden property="idExpirationDateDay" />
      	<html:hidden property="idExpirationDateMonth" />
      	<html:hidden property="idExpirationDateYear" />
      	<html:hidden property="areaOfAreaCode" />
   	  
      <span class="error"><html:errors/></span>

        <tr>
          <td colspan="2"><h2><bean:message key="label.person.title.changePersonalInfo" /></h2></td>
        </tr>



          <!-- Nome -->
          <tr>
            <td><bean:message key="label.person.name" /></td>
            <td><bean:write name="personalInfo" property="nome"/></td>
          </tr>
          <!-- Username -->
          <tr>
            <td><bean:message key="label.person.username" /></td>
            <td><bean:write name="personalInfo" property="username"/></td>
          </tr>

          <!-- Dados Pessoais -->
          <tr>
            <td><h2><bean:message key="label.person.title.personal.info" /><h2></td>
          </tr>
          
          <!-- Sexo -->
          <tr>
            <td><bean:message key="label.person.sex" /></td>
            <td><bean:write name="personalInfo" property="sexo"/></td>
          </tr>
 	      <!-- Numero do Documento de Identificacao -->
          <tr>
            <td><bean:message key="label.person.identificationDocumentNumber" /></td>
            <td><bean:write name="personalInfo" property="numeroDocumentoIdentificacao"/></td>
          </tr>
          <!-- Tipo do Documento de Identificacao -->
          <tr>
            <td><bean:message key="label.person.identificationDocumentType" /></td>
            <td><bean:write name="personalInfo" property="tipoDocumentoIdentificacao"/></td>
          </tr>
          <!-- Local de Emissao do Documento de Identificacao -->
          <tr>
            <td><bean:message key="label.person.identificationDocumentIssuePlace" /></td>
            <td><bean:write name="personalInfo" property="localEmissaoDocumentoIdentificacao"/></td>
          </tr>
          <!-- Data de Emissao do Documento de Identificacao -->
          <tr>
            <td><bean:message key="label.person.identificationDocumentIssueDate" /></td>
            <td><bean:write name="personalInfo" property="dataEmissaoDocumentoIdentificacao"/></td>
          </tr>
          <!-- Data de Validade do Documento de Identificacao -->
          <tr>
            <td><bean:message key="label.person.identificationDocumentExpirationDate" /></td>
            <td><bean:write name="personalInfo" property="dataValidadeDocumentoIdentificacao"/></td>
          </tr>
          <!-- Numero de Contribuinte -->
          <tr>
            <td><bean:message key="label.person.contributorNumber" /></td>
            <td><bean:write name="personalInfo" property="numContribuinte"/></td>
          </tr>
          <!-- Estado Civil -->
          <tr>
            <td><bean:message key="label.person.maritalStatus" /></td>
            <td><bean:write name="personalInfo" property="estadoCivil"/></td>
          </tr>
          <!-- Data de Nascimento -->
          <tr>
            <td><bean:message key="label.person.birth" /></td>
            <td><bean:write name="personalInfo" property="nascimento"/></td>
          </tr>
          <!-- Nacionalidade -->
          <tr>
            <td><bean:message key="label.person.country" /></td>
            <td><bean:write name="personalInfo" property="infoPais.nationality"/></td>
          </tr>
          
          <!-- Freguesia de Naturalidade -->
          <tr>
            <td><bean:message key="label.person.birthPlaceParish" /></td>
            <td><bean:write name="personalInfo" property="freguesiaNaturalidade"/></td>
          </tr>
          <!-- Concelho de Naturalidade -->
          <tr>
            <td><bean:message key="label.person.birthPlaceMunicipality" /></td>
            <td><bean:write name="personalInfo" property="concelhoNaturalidade"/></td>
          </tr>
          <!-- Distrito de Naturalidade -->
          <tr>
            <td><bean:message key="label.person.birthPlaceDistrict" /></td>
            <td><bean:write name="personalInfo" property="distritoNaturalidade"/></td>
          </tr>
          <!-- Nome do Pai -->
          <tr>
            <td><bean:message key="label.person.fatherName" /></td>
            <td><bean:write name="personalInfo" property="nomePai"/></td>
          </tr>
          <!-- Nome da Mae -->
          <tr>
            <td><bean:message key="label.person.motherName" /></td>
            <td><bean:write name="personalInfo" property="nomeMae"/></td>
          </tr>
          <!-- Profissao -->
          <tr>
            <td><bean:message key="label.person.occupation" /></td>
            <td><bean:write name="personalInfo" property="profissao"/></td>
          </tr>
          
          
          

          <!-- Dados de Residencia -->
          <tr>
            <td><h2><bean:message key="label.person.title.addressInfo" /><h2></td>
          </tr>
          <!-- Morada -->
          <tr>
            <td><bean:message key="label.person.address" /></td>
            <td><bean:write name="personalInfo" property="morada"/></td>
          </tr>
          <!-- Codigo Postal -->
          <tr>
            <td><bean:message key="label.person.postCode" /></td>
            <td><bean:write name="personalInfo" property="codigoPostal"/></td>
          </tr>
          <!-- Area do Codigo Postal -->
          <tr>
            <td><bean:message key="label.person.areaOfPostCode" /></td>
            <td><bean:write name="personalInfo" property="localidadeCodigoPostal"/></td>
          </tr>
          <!-- Localidade de Residencia -->
          <tr>
            <td><bean:message key="label.person.place" /></td>
            <td><bean:write name="personalInfo" property="localidade"/></td>
          </tr>
          <!-- Freguesia de Residencia -->
          <tr>
            <td><bean:message key="label.person.addressParish" /></td>
            <td><bean:write name="personalInfo" property="freguesiaMorada"/></td>
          </tr>
          <!-- Concelho de Residencia -->
          <tr>
            <td><bean:message key="label.person.addressMunicipality" /></td>
            <td><bean:write name="personalInfo" property="concelhoMorada"/></td>
          </tr>
          <!-- Distrito de Residencia -->
          <tr>
            <td><bean:message key="label.person.addressDistrict" /></td>
            <td><bean:write name="personalInfo" property="distritoMorada"/></td>
          </tr>


          <!-- Contactos -->
          <tr>
            <td><h2><bean:message key="label.person.title.contactInfo" /><h2></td>
          </tr>
          <!-- Telefone -->
          <tr>
            <td><bean:message key="label.person.telephone" /></td>
            <td><bean:write name="personalInfo" property="telefone"/></td>
          </tr>
          <!-- Telemovel -->
          <tr>
            <td><bean:message key="label.person.mobilePhone" /></td>
            <td><html:text property="mobilePhone"/></td>
          </tr>
          <!-- E-Mail -->
          <tr>
            <td><bean:message key="label.person.email" /></td>
	        <td><html:text property="email"/></td>
	      </tr>
          <!-- WebPage -->
          <tr>
            <td><bean:message key="label.person.webSite" /></td>
            <td><html:text property="webSite"/></td>
          </tr>
















        
<%--        
        
	   	<% UserView userView = (UserView) session.getAttribute("UserView"); %>
        <!-- Nome -->
        <tr>
         <td><bean:message key="label.person.name" /></td>
         <td>
           <% if (userView.hasRoleType(RoleType.MASTER_DEGREE_CANDIDATE) &&
                  userView.hasRoleType(RoleType.PERSON) && 
                  (userView.getRoles().size() == 2)) {
           %>
               <html:text property="name"/>
           <% } else { %>
		   	   <html:hidden property="name"/>
               <bean:write name="personalInfo" property="nome"/>
           <% } %>               
          </td>
        </tr>
        <!-- Estado Civil -->
        <tr>
         <td><bean:message key="label.person.maritalStatus" /></td>
         <td>
            <html:select property="maritalStatus">
                <html:options collection="<%= SessionConstants.MARITAL_STATUS_LIST_KEY %>" property="value" labelProperty="label"/>
             </html:select>          
         </td>
        </tr>
        <!-- Username -->
        <tr>
         <td><bean:message key="label.person.username" /></td>
          <td><html:text property="username"/></td>
        </tr>
        <!-- Nome do Pai -->
        <tr>
         <td><bean:message key="label.person.fatherName" /></td>
          <td><html:text property="fatherName"/></td>
        </tr>
        <!-- Nome da Mae -->
        <tr>
         <td><bean:message key="label.person.motherName" /></td>
          <td><html:text property="motherName"/></td>
        </tr>
        
        <!-- Data de Nascimento -->
        <tr>
         <td><bean:message key="label.person.birth" /></td>
          <td><html:select property="birthYear">
                <html:options collection="<%= SessionConstants.YEARS_KEY %>" property="value" labelProperty="label"/>
             </html:select>
             <html:select property="birthMonth">
                <html:options collection="<%= SessionConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
             </html:select>
             <html:select property="birthDay">
                <html:options collection="<%= SessionConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
             </html:select>
          </td>          
        </tr>

        
        <!-- Freguesia de Naturalidade -->
        <tr>
         <td><bean:message key="label.person.birthPlaceParish" /></td>
          <td><html:text property="birthPlaceParish"/></td>
        </tr>
        <!-- Concelho de Naturalidade -->
        <tr>
         <td><bean:message key="label.person.birthPlaceMunicipality" /></td>
          <td><html:text property="birthPlaceMunicipality"/></td>
        </tr>
        <!-- Distrito de Naturalidade -->
        <tr>
         <td><bean:message key="label.person.birthPlaceDistrict" /></td>
          <td><html:text property="birthPlaceDistrict"/></td>
        </tr>
        <!-- Numero do Documento de Identificacao -->
        <tr>
         <td><bean:message key="label.person.identificationDocumentNumber" /></td>
          <td><html:text property="identificationDocumentNumber"/></td>
        </tr>
        <!-- Local de Emissao do Documento de Identificacao -->
        <tr>
         <td><bean:message key="label.person.identificationDocumentIssuePlace" /></td>
          <td><html:text property="identificationDocumentIssuePlace"/></td>
        </tr>
        
	    <!-- Data de Emissao do Documento de Identificacao -->
        <tr>
         <td><bean:message key="label.person.identificationDocumentIssueDate" /></td>
          <td><html:select property="idIssueDateYear">
                <html:options collection="<%= SessionConstants.YEARS_KEY %>" property="value" labelProperty="label"/>
             </html:select>
             <html:select property="idIssueDateMonth">
                <html:options collection="<%= SessionConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
             </html:select>
             <html:select property="idIssueDateDay">
                <html:options collection="<%= SessionConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
             </html:select>
          </td>          
        </tr>

	<!-- Data de Validade do Documento de Identificacao -->
        <tr>
         <td><bean:message key="label.person.identificationDocumentExpirationDate" /></td>
         <td><html:select property="idExpirationDateYear">
                <html:options collection="<%= SessionConstants.EXPIRATION_YEARS_KEY %>" property="value" labelProperty="label"/>
             </html:select>
             <html:select property="idExpirationDateMonth">
                <html:options collection="<%= SessionConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
             </html:select>
             <html:select property="idExpirationDateDay">
                <html:options collection="<%= SessionConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
             </html:select>
          </td>          
        </tr>


        <!-- Tipo do Documento de Identificacao -->
        <tr>
         <td><bean:message key="label.person.identificationDocumentType" /></td>
         <td>
            <html:select property="identificationDocumentType">
                <html:options collection="<%= SessionConstants.IDENTIFICATION_DOCUMENT_TYPE_LIST_KEY %>" property="value" labelProperty="label"/>
             </html:select>          
         </td>
        </tr>
        <!-- Morada -->
        <tr>
         <td><bean:message key="label.person.address" /></td>
          <td><html:text property="address"/></td>
        </tr>
        <!-- Localidade -->
        <tr>
         <td><bean:message key="label.person.place" /></td>
          <td><html:text property="place"/></td>
        </tr>
        <!-- Codigo Postal -->
        <tr>
         <td><bean:message key="label.person.postCode" /></td>
          <td><html:text property="postCode"/></td>
        </tr>
        <!-- Area do Codigo Postal -->
        <tr>
         <td><bean:message key="label.person.areaOfPostCode" /></td>
          <td><html:text property="areaOfAreaCode"/></td>
        </tr>
        <!-- Freguesia de Morada -->
        <tr>
         <td><bean:message key="label.person.addressParish" /></td>
          <td><html:text property="addressParish"/></td>
        </tr>
        <!-- Concelho de Morada -->
        <tr>
         <td><bean:message key="label.person.addressMunicipality" /></td>
          <td><html:text property="addressMunicipality"/></td>
        </tr>
        <!-- Distrito de Morada -->
        <tr>
         <td><bean:message key="label.person.addressDistrict" /></td>
          <td><html:text property="addressDistrict"/></td>
        </tr>
        <!-- telefone -->
        <tr>
         <td><bean:message key="label.person.telephone" /></td>
          <td><html:text property="telephone"/></td>
        </tr>
        <!-- telemovel -->
        <tr>
         <td><bean:message key="label.person.mobilePhone" /></td>
          <td><html:text property="mobilePhone"/></td>
        </tr>
        <!-- Email -->
        <tr>
         <td><bean:message key="label.person.email" /></td>
          <td><html:text property="email"/></td>
        </tr>
        <!-- HomePage -->
        <tr>
         <td><bean:message key="label.person.webSite" /></td>
          <td><html:text property="webSite"/></td>
        </tr>
        <!-- Numero de Contribuinte -->
        <tr>
         <td><bean:message key="label.person.contributorNumber" /></td>
          <td><html:text property="contributorNumber"/></td>
        </tr>
        <!-- Profissao -->
        <tr>
         <td><bean:message key="label.person.occupation" /></td>
          <td><html:text property="occupation"/></td>
        </tr>
        <!-- Sexo -->
        <tr>
         <td><bean:message key="label.person.sex" /></td>
         <td>
            <html:select property="sex">
                <html:options collection="<%= SessionConstants.SEX_LIST_KEY %>" property="value" labelProperty="label"/>
             </html:select>          
         </td>
        </tr>
        
        <!-- Nacionalidade -->
        <tr>
         <td><bean:message key="label.person.nationality" /></td>
         <td>
            <html:select property="nationality">
                <html:options collection="<%= SessionConstants.NATIONALITY_LIST_KEY %>" property="value" labelProperty="label"/>
             </html:select>          
         </td>
        </tr>

--%>

        
        <td colspan="2">
           <html:submit property="Alterar">Alterar Dados</html:submit>
           <html:reset property="Reset">Dados Originais</html:reset>
        </td>
      </html:form>  
   </table>
  </body>
</html>
