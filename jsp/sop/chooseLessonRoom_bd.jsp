<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
   		<td class="infoselected"><p>O curso seleccionado &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
       	</td>
   	</tr>
</table>
<br />
<h2><bean:message key="title.criarAula"/></h2>
<span class="error"><html:errors/></span>
<bean:message key="message.createLesson"/>
	<html:form action="/criarAulaForm" focus="nomeSala" >
	<html:hidden property="page" value="2"/>
<table cellspacing="0">
	<tr>
    	<td nowrap class="formTD"><bean:message key="property.aula.weekDay"/>: </td>
      	<td nowrap class="formTD"><html:hidden property="diaSemana"/><bean:write name="weekDayString"/></td>
   	</tr>
    <tr>
       	<td nowrap="nowrap" class="formTD"><bean:message key="property.aula.time.begining"/>: </td>
        <td nowrap="nowrap">
        	<html:hidden property="horaInicio" write="true"/>:
           	<html:hidden property="minutosInicio" write="true"/>
     	</td>
    </tr>
    <tr>
   		<td nowrap="nowrap" class="formTD"><bean:message key="property.aula.time.end"/>: </td>
        <td nowrap="nowrap">
	    	<html:hidden property="horaFim" write="true"/>:
          	<html:hidden property="minutosFim" write="true"/>
        </td> 
    </tr> 
    <tr>
    	<td nowrap class="formTD"><bean:message key="property.aula.type"/>: </td>
        <td nowrap class="formTD">
           	<html:select property="tipoAula" size="1">
               <option selected="selected"></option>
            <html:options collection="tiposAula" property="value" labelProperty="label"/>
            </html:select>
    	</td>
   	</tr>
  	<tr>
   		<td nowrap class="formTD"><bean:message key="property.aula.disciplina"/>: </td>
      	<td><jsp:include page="selectExecutionCourseList.jsp"/></td>
   </tr>
   <tr>
    	<td><bean:message key="property.aula.sala"/>: </td> 
       	<td>
     		<html:select property="nomeSala" size="1" >
               	<html:options collection="listaSalas" property="value" labelProperty="label"/>
           </html:select>
      	</td>
  	</tr>
</table>
<br />
<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
			 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
			 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
			 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
<html:submit property="operation" styleClass="inputbutton"><bean:message key="label.create"/></html:submit>
<html:reset value="Limpar" styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>
	</html:form>
