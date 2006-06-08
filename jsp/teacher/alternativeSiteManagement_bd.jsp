<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.ForwardEmailAction"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.teacher.TeacherAdministrationViewerDispatchAction"%>

<p>
<span class="error"><html:errors/></span>
</p>

<h2><bean:message key="title.personalizationOptions"/></h2>
<logic:present name="siteView"> 
<html:form action="/alternativeSite" method="get">
<bean:define id="bodyComponent" name="siteView" property="component"/>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop"><img src="<%= request.getContextPath() %>/images/number_1.gif" alt="" />
		</td>
		<td class="infoop"><bean:message key="message.siteandmail.information" />
		</td>
	</tr>
</table>
<br />
<table width="100%">
	<td width="200px">
		<bean:message key="message.siteAddress"/>
	</td>
	<td><html:text property="siteAddress" size="30"/>
	</td>
	<td><span class="error" ><html:errors property="siteAddress"/></span>
	</td>
</tr>
<tr>
	<td width="200px">
	  <html:radio property="dynamicMailDistribution" value="false"/><bean:message key="message.mailAddressCourse"/>
	<td>
	  <html:text property="mail" size="30"/>
	</td>
	<td><span class="error" >
	  <html:errors property="mail"/></span>
    </td>    
</tr>
<tr>
	<td width="200px">
	  		<html:radio property="dynamicMailDistribution" value="true"/> <bean:message key="dyanamicMailDistribution.prompt.message"/>
	<td>
		<%=ForwardEmailAction.emailAddressPrefix%><%=request.getParameter("objectCode")%>&#64;<%=TeacherAdministrationViewerDispatchAction.mailingListDomainConfiguration() %>
	</td>
	<td><span class="error" >
	  <html:errors property="dynamicMailDistribution"/></span>
    </td>    
</tr>
<tr>
	<td colspan="3">
		
	</td>
</tr>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td class="infoop"><img src="<%= request.getContextPath() %>/images/number_2.gif" alt="" />
	</td>
	<td class="infoop">
	  <bean:message key="message.initialStatement.explanation" />
	</td>
</tr>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td width="200px" valign="top">
		<bean:message key="message.initialStatement"/>
	</td>	
	<td><html:textarea name="bodyComponent" property="initialStatement" rows="10" cols="56"/> 
	</td>
</tr>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td class="infoop"><img src="<%= request.getContextPath() %>/images/number_3.gif" alt="" />
	</td>
	<td class="infoop">
	  <bean:message key="message.introduction.explanation" />
	</td>
</tr>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td width="200px" valign="top">
		<bean:message key="message.introduction"/>
	</td>	
	<td><html:textarea name="bodyComponent" property="introduction" rows="10" cols="56"/></td> 
</tr>
</table>
<h3><table>
<html:hidden property="method" value="editCustomizationOptions"/>
<html:hidden property="page" value="1"/>
<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />

<tr align="center">	
	<td>
	<html:submit styleClass="inputbutton" property="confirm">
		<bean:message key="button.save"/>
	</html:submit>
	</td>
	<td>
		<html:reset styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>
	</td>
</tr>
</table></h3>
</html:form>
</logic:present>