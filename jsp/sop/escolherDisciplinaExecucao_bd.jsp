<%@ page language="java" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
	   	<table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td class="infoselected"><p>O curso seleccionado
              &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
            </td>
          </tr>
        </table>
        <br/>
        <h2><bean:message key="title.choose.discipline"/></h2>
        <span class="error"><html:errors /></span>
        <bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
        <html:form action="<%= path %>">        
        	<html:hidden property="page" value="1"/>
    	<table border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td nowrap="nowrap" class="formTD"><bean:message key="property.course"/></td>
            <td nowrap="nowrap" class="formTD"><jsp:include page="selectExecutionCourseList.jsp"/></td>
          </tr>
        </table>
<br />


<logic:present name="classOID">
	<html:hidden property="<%= SessionConstants.CLASS_VIEW_OID %>"
				 value="<%= pageContext.findAttribute("classOID").toString() %>"/>
</logic:present>

<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
			 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
			 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
			 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
<html:submit value="Procurar" styleClass="inputbutton"><bean:message key="label.search"/></html:submit>
</html:form>