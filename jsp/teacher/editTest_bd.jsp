<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.editTest"/></h2>
<table>
	<tr>
		<td class="infoop"><bean:message key="message.editTest.information" /></td>
	</tr>
</table>
<br/>
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>
<bean:define id="infoTest" name="component" property="infoTest"/>
<bean:define id="infoTestQuestionList" name="component" property="infoTestQuestions"/>

<html:form action="/testEdition">
<html:hidden property="page" value="1"/>
<html:hidden property="method" value="editTest"/>
<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<table>
	<tr>
		<td><b><bean:message key="label.title"/></b></td>
		<td><bean:write name="infoTest" property="title"/></td>
	</tr>
	<logic:notEqual name="infoTest" property="information" value="">
		<tr>
			<td><b><bean:message key="label.information"/></b></td>
			<td><bean:write name="infoTest" property="information"/></td>
		</tr>
	</logic:notEqual>
</table>
<table>
	<tr>
		<td><div class="gen-button">
		<html:link page="<%= "/testEdition.do?method=prepareEditTestHeader&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")%>">
		<bean:message key="link.editTestHeader" />
		</html:link>&nbsp;&nbsp;&nbsp;</div></td>
		<td><div class="gen-button">
		<html:link page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")%>">
		<bean:message key="label.test.insertQuestion"/>
		</html:link>&nbsp;&nbsp;&nbsp;</div></td>
		<td><div class="gen-button">
		<html:link page="<%= "/testsManagement.do?method=showTests&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")%>">
		<bean:message key="label.finish"/>
		</html:link></div></td>
	</tr>
</table>
<br/>
<table>
	<tr><td><hr></td></tr>
	<logic:iterate id="testQuestion" name="infoTestQuestionList" type="DataBeans.InfoTestQuestion">
	<tr>
		<td><b><bean:message key="message.tests.question" /></b>&nbsp;<bean:write name="testQuestion" property="testQuestionOrder"/></td></tr>
		<bean:define id="testQuestionValue" name="testQuestion" property="testQuestionValue"/>
		<bean:define id="testQuestionValue" value="<%= (new java.text.DecimalFormat("#0.##").format(Double.parseDouble(testQuestionValue.toString())).toString()) %>"/>		
		<tr><td><b><bean:message key="message.tests.questionValue" /></b>&nbsp;<bean:write name="testQuestionValue"/></td></tr>
		<bean:define id="thisQuestion" name="testQuestion" property="question" type="DataBeans.InfoQuestion"/>
		<bean:define id="questionCode" name="thisQuestion" property="idInternal"/>
		<tr><td><table><tr><td>
			<div class="gen-button">
			<html:link page="<%= "/testQuestionEdition.do?method=prepareEditTestQuestion&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode") +"&amp;questionCode=" +questionCode%>">
			<bean:message key="title.editTestQuestion" />
			</html:link>&nbsp;&nbsp;&nbsp;</div></td>
			<td><div class="gen-button">
			<html:link page="<%= "/testEdition.do?method=deleteTestQuestion&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode") +"&amp;questionCode=" +questionCode%>">
			<bean:message key="link.removeTestQuestion" />
			</html:link></div></td>
		</tr></table>
		<% request.setAttribute("iquestion", thisQuestion); %>
		<jsp:include page="showQuestion.jsp">
			<jsp:param name="showResponses" value="false"/>
		</jsp:include>
	</td></tr>
	<tr><td><hr></td></tr>
	</logic:iterate>
</table>
<br/>
<bean:size id="infoTestQuestionListSize" name="component" property="infoTestQuestions"/>
<logic:notEqual name="infoTestQuestionListSize" value="0">
	<table>
		<tr><td>
			<div class="gen-button">
			<html:link page="<%= "/testsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")%>">
			<bean:message key="label.test.insertQuestion" />
			</html:link>&nbsp;&nbsp;&nbsp;</div></td>
			<td><div class="gen-button">
			<html:link page="<%= "/testsManagement.do?method=showTests&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")%>">
			<bean:message key="label.finish" />
			</html:link></div></td>
		</tr>
	</table>
</logic:notEqual>
</html:form>
