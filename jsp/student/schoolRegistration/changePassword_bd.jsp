<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

	<strong>P�gina 1 de 7</strong>
   <h2><p align="center">Por raz�es de seguran�a mude a sua senha</p></h2>
	<p align="center">
   		<font color='#FF0000'><strong>
   			Aten��o: A altera��o da senha s� ser� efectuada no final do processo de Matricula.<br/>
   			Se o processo de Matricula for cancelado a nova senha n�o ficar� guardada.
   		</strong></font>
   	</p>
   	
	<p align="center"><span class="error"><html:errors/></span></p>

<html:form action="/viewPersonalInfo?method=visualizeFirstTimeStudentPersonalInfoAction">
<html:hidden property="page" value="2"/>
    
    <!-- Old Password -->
	<table align="center">
       <tr>
         <td><bean:message key="label.candidate.oldPassword" bundle="DEFAULT"/>:</td>
         <td><html:password property="oldPassword"/></td>
       </tr>
	   <div></div>
       <!-- new password -->
       <tr>
         <td><bean:message key="label.candidate.newPassword" bundle="DEFAULT"/>:</td>
         <td><html:password property="newPassword"/></td>
         </td>
       </tr>
       <!-- retype new password -->
       <tr>
         <td><bean:message key="label.candidate.reTypePassword" bundle="DEFAULT"/>:</td>
         <td><html:password property="reTypeNewPassword"/></td>
         </td>
       </tr>
	</table>

	<br />
	<p align="center">
		<html:submit value="Alterar" styleClass="inputbutton" property="ok" />
		<html:reset value="Limpar" styleClass="inputbutton"/>
	</p>
</html:form>