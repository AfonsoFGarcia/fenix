<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<logic:present name="infoSiteStudentTestFeedback">
	<center><h2><bean:message key="message.studentTest.sent"/></h2></center>
	<bean:define id="responseNumber" name="infoSiteStudentTestFeedback" property="responseNumber"/>
	<bean:define id="notResponseNumber" name="infoSiteStudentTestFeedback" property="notResponseNumber"/>
	<bean:define id="errors" name="infoSiteStudentTestFeedback" property="errors"/>
	<table>
		<logic:iterate id="error" name="errors">
			<tr><td><span class="error"><bean:write name="error"/></span></td></tr>
		</logic:iterate>
		<tr>
			<td><b><bean:message key="message.studentQuestionsAnsweredNumber"/></b></td>
			<td><bean:write name="responseNumber"/></td>
		</tr>
		<tr>
			<td><b><bean:message key="message.studentQuestionsNotAnsweredNumber"/></b></td>
			<td><bean:write name="notResponseNumber"/></td>
		</tr>
	</table>
	
	
	
	<html:form action="/studentTestManagement">
	<html:hidden property="method" value="simulateTest"/>
	<html:hidden property="doTestSimulation" value="true"/>
	<html:hidden property="objectCode" value="<%= request.getParameter("objectCode") %>"/>
	<html:hidden property="distributedTestCode" value="<%= request.getParameter("distributedTestCode") %>"/>

	<logic:present name="infoStudentTestQuestionList">
		<logic:iterate id="testQuestion" name="infoStudentTestQuestionList" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion"/>
		<bean:define id="distributedTest" name="testQuestion" property="distributedTest" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest"/>
		<bean:define id="distributedTestCode" name="distributedTest" property="idInternal"/>
		<html:hidden property="testInformation" name="distributedTest" property="testInformation"/>
		<bean:define id="testType" name="distributedTest" property="testType.type"/>
		<html:hidden property="testType" value="<%=testType.toString()%>"/>
		<bean:define id="availableCorrection" name="distributedTest" property="correctionAvailability.availability"/>
		<html:hidden property="availableCorrection" value="<%=availableCorrection.toString()%>"/>
		<html:hidden property="imsFeedback" name="distributedTest" property="imsFeedback"/>
		<html:hidden property="distributedTestCode" value="<%= distributedTestCode.toString() %>"/>
		<center>
		<h2><bean:write name="distributedTest" property="title"/></h2>
		<b><bean:write name="distributedTest" property="testInformation"/></b>
		</center>
		<br/>
		<br/>
		<bean:define id="testType" name="distributedTest" property="testType.type"/>
		<jsp:include page="showStudentTest.jsp">
			<jsp:param name="pageType" value="feedback"/>
			<jsp:param name="correctionType" value=""/>
			<jsp:param name="testCode" value="<%=distributedTestCode%>"/>
	 	</jsp:include>
	</logic:present>
	<logic:notPresent name="infoStudentTestQuestionList">
		<bean:define id="infoStudentTestQuestionList" name="infoSiteStudentTestFeedback" property="infoStudentTestQuestionList"/>
		<logic:iterate id="testQuestion" name="infoStudentTestQuestionList" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion" indexId="questionIndex">
			<bean:define id="question" name="testQuestion" property="question" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion"/>
			<bean:define id="questionType" name="question" property="questionType.type"/>
			<bean:define id="questionCode" name="question" property="idInternal"/>
			<html:hidden property='<%="questionCode"+questionIndex%>' value="<%= questionCode.toString() %>"/>
			<bean:define id="optionShuffle" name="testQuestion" property="optionShuffle"/>
			<html:hidden property='<%="optionShuffle"+questionIndex%>' value="<%= optionShuffle.toString() %>"/>
			<html:hidden property='<%="questionType"+questionIndex%>' value="<%= questionType.toString() %>"/>
			
			<logic:notEmpty name="question" name="testQuestion" property="response">
				<bean:define id="questionValue" value='<%="question"+ questionIndex%>'/>
				<%if(((Integer)questionType).intValue()==1 ){ %>
					<logic:notEmpty name="question" name="testQuestion" property="response.response">
						<logic:iterate id="r" name="testQuestion" property="response.response">
							<html:hidden property="<%=questionValue%>" value="<%= r.toString() %>"/>
						</logic:iterate>
					</logic:notEmpty>
				<%}else{%>
				<bean:define id="r" name="testQuestion" property="response.response"/>
				<html:hidden property="<%=questionValue%>" value="<%= r.toString() %>"/>
				<%}%>

			</logic:notEmpty>
			
		</logic:iterate>
		<bean:define id="distributedTest" name="testQuestion" property="distributedTest" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest"/>
		<bean:define id="distributedTestCode" name="distributedTest" property="idInternal"/>
		<html:hidden property="testInformation" name="distributedTest" property="testInformation"/>
		<bean:define id="testType" name="distributedTest" property="testType.type"/>
		<html:hidden property="testType" value="<%=testType.toString()%>"/>
		<bean:define id="availableCorrection" name="distributedTest" property="correctionAvailability.availability"/>
		<html:hidden property="availableCorrection" value="<%=availableCorrection.toString()%>"/>
		<html:hidden property="imsFeedback" name="distributedTest" property="imsFeedback"/>
		
	</logic:notPresent>
	<br/>
	<br/>
	<tr>
	<center>
	<html:hidden property="showCorrection" value="true"/>
	<html:submit styleClass="inputbutton"><bean:message key="button.back"/></html:submit>
	</center>
	</html:form>
</logic:present>
<logic:notPresent name="infoSiteStudentTestFeedback">
	<center><h2><bean:message key="message.studentTest.notSent"/></h2></center>
</logic:notPresent>