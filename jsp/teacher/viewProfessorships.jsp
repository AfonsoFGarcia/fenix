<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<span class="error"><html:errors/></span>	

<logic:present name="<%= SessionConstants.INFO_SITES_LIST %>" scope="session">
	
		<table cellpadding="0" border="0">
	 	<tr>   		
	 		<bean:message key="label.professorShips" />	
		 </tr>
	<logic:iterate name="<%= SessionConstants.INFO_SITES_LIST %>" id="site" indexID="index">
		<tr>
		<bean:define id="executionCourse" name="site" property="infoExecutionCourse"/>
		<html:link forward="/viewSite.do" >			
			<bean:write name="executionCourse" property="code"/>
		</html:link>	
		</tr>
	</logic:iterate>
	
	
</logic:present>
