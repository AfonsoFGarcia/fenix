<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2>Gest�o de Salas</h2>
<br />
Seleccione a op��o pretendida para criar ou editar as caracter�sticas de uma sala. <br />
DevNote: Deveria de haver uma descri��o do que se pretende fazer, e o que est� envolvido, na cria��o de salas.
<br/>
<br/>
<span class="error"><html:errors /></span>
<html:form action="/principalSalas">
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td nowrap="nowrap" class="formTD"><bean:message key="property.executionPeriod"/></td>
    <td nowrap="nowrap" class="formTD"><jsp:include page="selectExecutionPeriodList.jsp"/></td>
  </tr>
</table>
<html:hidden property="method" value="choose"/>
<html:hidden property="page" value="1"/>
  <html:submit styleClass="inputbutton">
   	  <bean:message key="label.choose"/>
  </html:submit>
</html:form>    

