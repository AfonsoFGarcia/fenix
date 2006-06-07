<%@ page language="java" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %> 
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.teacher.TeacherAdministrationViewerDispatchAction"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.ForwardEmailAction"%>
<bean:define id="component" name="siteView" property="commonComponent" />
<bean:define id="executionCourse" name="siteView" property="executionCourse" />
<bean:define id="sections" name="component" property="sections"/>
<ul>
	<li><html:link page="<%= "/viewSite.do?method=firstPage&amp;objectCode=" + pageContext.findAttribute("executionCourseCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>">
		<bean:message  key="link.inicialPage"/></html:link></li>

	<li><html:link page="<%= "/viewSite.do" + "?method=announcements&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>">
		<bean:message  key="link.announcements"/></html:link></li>
	
	<li><html:link page="<%= "/viewSite.do" + "?method=summaries&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>">
		<bean:message key="link.summaries.public"/></html:link></li>
	
	<li><html:link page="<%= "/viewSite.do?method=evaluationMethod&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>">
		<bean:message  key="link.evaluationMethod"/></html:link></li>

	<li><html:link page="<%= "/viewSite.do?method=bibliography&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>">
		<bean:message  key="link.bibliography"/></html:link></li>

	<li><html:link page="<%= "/viewSite.do" + "?method=timeTable&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>">
		<bean:message  key="link.executionCourse.timeTable"/></html:link></li>

	<li><html:link page="<%= "/viewSite.do" + "?method=shifts&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>">
		<bean:message  key="link.executionCourse.shifts"/></html:link></li>

	<li><html:link page="<%= "/viewSite.do" + "?method=evaluations&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>">
		<bean:message  key="link.evaluation"/></html:link></li>

	<li><html:link page="<%= "/viewSite.do" + "?method=viewExecutionCourseProjects&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>">
		<bean:message  key="link.groupings"/></html:link></li>

	<logic:notEmpty name="sections" >
		<logic:present name="infoSection" >
<%-- 			<li><html:link page="<%=  request.getContextPath() + RequestUtils.getModuleName(request,application)%>"></html:link></li>
--%>
			<app:generateSectionMenu name="sections" path="<%=  request.getContextPath() + RequestUtils.getModuleName(request,application)%>" activeSectionName="infoSection" />
		</logic:present>
		<logic:notPresent name="infoSection" >
			<app:generateSectionMenu name="sections" path="<%=  request.getContextPath() + RequestUtils.getModuleName(request,application)%>" />
		</logic:notPresent>		
	</logic:notEmpty>
	
	<li>
		<bean:define id="imageURL" type="java.lang.String">
			background: url(<%= request.getContextPath() %>/images/rss_ico.gif) 10px 3px no-repeat; padding-left: 32px;
		</bean:define>
		<html:link page="<%= "/viewSite.do" + "?method=rss&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" style="<%=imageURL%>">
			<bean:message  key="label.rss"/>
		</html:link>
	</li>

	<logic:equal name="executionCourse" property="site.dynamicMailDistribution" value="true">
			<%
				StringBuffer buffer = new StringBuffer();
				buffer.append(ForwardEmailAction.emailAddressPrefix);
				buffer.append(request.getParameter("objectCode")).append("&#64");
				buffer.append(TeacherAdministrationViewerDispatchAction.mailingListDomainConfiguration());
			%>
		<bean:define id="advisoryText">
			<bean:message  key="send.email.dynamicMailDistribution.link" bundle="PUBLIC_DEGREE_INFORMATION"/>
		</bean:define>
		<html:link href="<%="mailto:" +buffer.toString() %>" titleKey="send.email.dynamicMailDistribution.title" bundle="PUBLIC_DEGREE_INFORMATION">
			<div class="email"><p><bean:message key="send.email.dynamicMailDistribution.link" bundle="PUBLIC_DEGREE_INFORMATION"/></p>
		</html:link>
	</logic:equal>

	
	<logic:notEqual name="executionCourse" property="site.dynamicMailDistribution" value="true">
		<logic:notEmpty name="component" property="mail" >	
			<bean:define id="siteMail" name="component" property="mail" />
			<html:link href="<%= "mailto:" + pageContext.findAttribute("siteMail") %>" titleKey="send.email.singleMail.title" bundle="PUBLIC_DEGREE_INFORMATION">
			<div class="email">3<p><bean:message key="send.email.dynamicMailDistribution.link" bundle="PUBLIC_DEGREE_INFORMATION"/></p>
			</html:link>
		</logic:notEmpty>
	</logic:notEqual>
	

</ul>



	
	
