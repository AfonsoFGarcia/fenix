<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="label.tutor"/></h2>
<span class="error"><html:errors/></span><br/>

<html:form action="/tutorManagement" focus="tutorNumber">

<html:hidden property="executionDegreeId" value="<%=  request.getAttribute("executionDegreeId").toString() %>"/>
<html:hidden property="method" value="readTutor" />
<html:hidden property="page" value="1" />

<bean:message key="label.tutorNumber"/>:&nbsp;<html:text property="tutorNumber" size="4"/>

<p />
<html:submit><bean:message key="label.submit"/></html:submit>
<p />
<p />
</html:form>