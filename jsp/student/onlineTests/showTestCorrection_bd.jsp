<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<logic:present name="infoStudentTestQuestionList">
	<center>
	<logic:empty name="infoStudentTestQuestionList">
		<h2><bean:message key="message.studentTest.no.available"/></h2>
	</logic:empty>
	
	<logic:notEmpty name="infoStudentTestQuestionList" >
	<html:form action="/studentTests">
	<html:hidden property="method" value="testsFirstPage"/>

	<logic:iterate id="testQuestion" name="infoStudentTestQuestionList" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion"/>
	<bean:define id="distributedTest" name="testQuestion" property="distributedTest" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest"/>
	<bean:define id="testCode" name="distributedTest" property="idInternal"/>
		
	<bean:define id="objectCode" name="distributedTest" property="infoTestScope.infoObject.idInternal"/>
	<html:hidden property="objectCode" value="<%= objectCode.toString() %>"/>
	<html:hidden property="testCode" value="<%= testCode.toString() %>"/>
	
		<h2><bean:write name="distributedTest" property="title"/></h2>
		<b><bean:write name="distributedTest" property="testInformation"/></b>
	</center>
	<br/>
	<br/>
	<bean:define id="testType" name="distributedTest" property="testType.type"/>
	<%if(((Integer)testType).intValue()!=3){%>
	<b><bean:message key="label.test.totalClassification"/>:</b>&nbsp;<bean:write name="classification"/>
	<%}%>
	<jsp:include page="showStudentTest_bd.jsp">
		<jsp:param name="pageType" value="correction"/>
		<jsp:param name="testCode" value="<%=testCode%>"/>
 	</jsp:include>
	<table align="center">
	<tr>
		<td><html:submit styleClass="inputbutton"><bean:message key="button.back"/></html:submit></td>
	</tr>
	</table>
	</html:form>
	</logic:notEmpty>
</logic:present>
<logic:notPresent name="infoStudentTestQuestionList">
<center>
	<h2><bean:message key="message.studentTest.no.available"/></h2>
</center>
</logic:notPresent>