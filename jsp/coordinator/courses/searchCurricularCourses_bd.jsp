<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="java.util.List"%>


<strong><jsp:include page="../context.jsp"/></strong>

<br />
<br />

<span class="error"><html:errors /></span>
Nota: Na indica��o do nome pode ser fornecido apenas parte do nome da disciplina.<br />
Exemplo 1: Para selecionar todas as disciplinas que come�am com a letra "A" escreva <strong>A%</strong><br />
Exemplo 2: Para selecionar todas as disciplinas que come�am com a letra "A" e que tenham um segundo nome que come�a com a letra "M" escreva <strong>A% M%</strong><br />
<br />
<br />

<html:form action="/executionCoursesInformation" >
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td nowrap="nowrap">
    	<bean:message key="property.executionPeriod"/>:
    </td>
    <td nowrap="nowrap">
		<html:select property="executionPeriodOID" size="1">
			<html:options	property="value" 
     						labelProperty="label" 
							collection="<%= SessionConstants.LABELLIST_EXECUTIONPERIOD %>" />
		</html:select>
    </td>
  </tr>
  <tr>
    <td nowrap="nowrap">
    	<bean:message key="property.curricularYear"/>:
    </td>
    <td nowrap="nowrap">
		<html:select property="curricularYearOID" size="1">
			<html:options	property="value" 
     						labelProperty="label" 
							collection="<%= SessionConstants.LABELLIST_CURRICULAR_YEARS %>" />
		</html:select>
    </td>
  </tr>
  <tr>
    <td nowrap="nowrap">
    	<bean:message key="property.executionCourse.name"/>:
    </td>
    <td nowrap="nowrap">
		<html:text property="executionCourseName" size="30"/>
    </td>
  </tr>  
  
</table>
<br />
<html:hidden property="method" value="getExecutionCourses"/>
<html:hidden property="page" value="1"/>
<html:submit styleClass="inputbutton">
<bean:message key="label.choose"/>
</html:submit>
</html:form>    
<br />
