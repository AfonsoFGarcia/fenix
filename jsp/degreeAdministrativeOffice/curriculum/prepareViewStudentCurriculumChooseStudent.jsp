<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<h2><bean:message key="title.student.curriculum"/></h2>

<span class="error0"><!-- Error messages go here --><html:errors /></span>

<html:form action="/viewCurriculum" focus="studentNumber">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareReadByStudentNumber"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	
	<logic:present name="executionDegreeId">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeId" property="executionDegreeId" value="<%=pageContext.findAttribute("executionDegreeId").toString()%>"/>
	</logic:present>
	<logic:present name="degreeCurricularPlanID">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%=pageContext.findAttribute("degreeCurricularPlanID").toString()%>"/>
	</logic:present>
	
	<table class="tstyle5">
		<tr>
			<td><bean:message key="label.choose.student"/></td>
			<td><input alt="input.studentNumber" type="text" name="studentNumber" size="5" maxlength="5"/></td>
		</tr>
	</table>
	
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.submit.student"/></html:submit>
	</p>
</html:form>
