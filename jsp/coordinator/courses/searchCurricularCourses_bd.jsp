<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<span class="error"><html:errors /></span>
<html:form action="/executionCoursesInformation" >
<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="2" class="infoop">
			Nota: Na indica��odo nome pode ser fornecido apenas parte do nome da disciplina.<br/>
			Exemplo 1: Para selecionar todas as disciplinas que come�am com a letra "A" escreva <strong>A%</strong><br />
			Exemplo 2: Para selecionar todas as disciplinas que come�am com a letra "A" e que tenham um segundo nome que come�a com a letra "M" escreva <strong>A% M%</strong><br />
		</td>
	</tr>
	<tr><td colspan="2"><br /><br /></td></tr>
	<tr>
    	<td nowrap="nowrap">
    		<bean:message key="property.executionPeriod"/>:
    	</td>
    	<td nowrap="nowrap">
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriodOID" property="executionPeriodOID" size="1">
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
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.curricularYearOID" property="curricularYearOID" size="1">
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
		<html:text bundle="HTMLALT_RESOURCES" altKey="text.executionCourseName" property="executionCourseName" size="30"/>
    </td>
  </tr>  
  
</table>
<br />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="getExecutionCourses"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID"/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
<bean:message key="label.choose"/>
</html:submit>
</html:form>    
<br />
