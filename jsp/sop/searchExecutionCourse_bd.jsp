<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="java.util.List"%>
<h2>Selecionar Disciplina(s)</h2>
<br />
<span class="error"><html:errors /></span>
Nota: Na indica��o do nome pode ser fornecido apenas parte do nome da disciplina.<br />
O caracter <strong>%</strong> � o wildcard.
<br />
<br />
<html:form action="/manageExecutionCourses" focus="executionPeriodOID">
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td nowrap="nowrap" width="150">
    	<bean:message key="property.executionPeriod"/>:
    </td>
    <td nowrap="nowrap">
		<%-- On select the page has to be re-selected!!! --%>
		<html:select property="executionPeriodOID" size="1">
			<html:options	property="value" 
     						labelProperty="label" 
							collection="<%= SessionConstants.LABELLIST_EXECUTIONPERIOD %>" />
		</html:select>
    </td>
  </tr>
  <tr>
    <td nowrap="nowrap" width="150">
    	<bean:message key="property.executionDegree"/>:
    </td>
    <td nowrap="nowrap">
		<html:select property="executionDegreeOID" size="1">
			<html:options	property="value" 
     						labelProperty="label" 
							collection="<%= SessionConstants.LIST_INFOEXECUTIONDEGREE %>" />
		</html:select>
    </td>
  </tr>
  <tr>
    <td nowrap="nowrap" width="150">
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
    <td nowrap="nowrap" width="150">
    	<bean:message key="property.executionCourse.name"/>:
    </td>
    <td nowrap="nowrap">
		<html:text property="executionCourseName" size="30"/>
    </td>
  </tr>
</table>
<br />
<html:hidden property="method" value="search"/>
<html:hidden property="page" value="1"/>
<html:submit styleClass="inputbutton">
<bean:message key="label.choose"/>
</html:submit>
</html:form>    
