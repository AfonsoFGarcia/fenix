<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="link.markSheet.management"/></h2>
<br/>
<html:link action="/createMarkSheet.do?method=prepareCreateMarkSheet"><bean:message key="label.createMarkSheet"/></html:link>
<br/>
<br/>

<h2><strong><bean:message key="label.searchMarkSheet"/></strong></h2>

<logic:messagesPresent message="true">
	<ul>
	<html:messages id="messages" message="true">
		<li><span class="error0"><bean:write name="messages" /></span></li>
	</html:messages>
	</ul>
	<br/>
</logic:messagesPresent>

<fr:edit id="edit"
		 name="edit"
		 type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementSearchBean"
		 schema="markSheet.search"
		 action="/markSheetManagement.do?method=searchMarkSheets">
	<fr:destination name="postBack" path="/markSheetManagement.do?method=prepareSearchMarkSheetPostBack"/>
	<fr:destination name="invalid" path="/markSheetManagement.do?method=prepareSearchMarkSheetInvalid"/>
	<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:edit>

<br/>
<br/>

<logic:present name="searchResult">

	<strong><bean:write name="edit" property="curricularCourse.name"/></strong>
	
	<bean:define id="executionPeriodID" name="edit" property="executionPeriod.idInternal" />
	<bean:define id="degreeID" name="edit" property="degree.idInternal" />
	<bean:define id="degreeCurricularPlanID" name="edit" property="degreeCurricularPlan.idInternal" />
	<bean:define id="curricularCourseID" name="edit" property="curricularCourse.idInternal" />

	<bean:define id="url" name="url"/>	
	<html:link action='<%= "/createMarkSheet.do?method=prepareCreateMarkSheetFilled" + url %>'><bean:message key="label.createMarkSheet"/></html:link>
	<br/><br/>

	<logic:empty name="searchResult">
		<em><bean:message key="label.noMarkSheetsFound"/></em>
	</logic:empty>
	
	<logic:notEmpty name="searchResult">

		<table>
			<logic:iterate id="entry" name="searchResult" >
			
				<bean:define id="markSheetType" name="entry" property="key"/>
				<bean:define id="markSheetResult" name="entry" property="value"/>
				<tr>
					<td colspan="2">
						<strong><bean:message name="markSheetType" property="name" bundle="ENUMERATION_RESOURCES"/></strong>
						(<bean:write name="markSheetResult" property="numberOfEnroledStudents"/> <bean:message key="label.markSheet.of"/> <bean:write name="markSheetResult" property="totalNumberOfStudents"/> <bean:message key="label.markSheet.evaluatedStudents"/>)
					</td>
				</tr>
				<logic:iterate id="markSheet" name="markSheetResult" property="markSheetsSortedByEvaluationDate" type="net.sourceforge.fenixedu.domain.MarkSheet">
					<tr>
						<td>							
							<table>
								<tr>
									<td>-</td>
									<td>
										<bean:define id="evaluationDateDateTime" type="org.joda.time.DateTime" name="markSheet" property="evaluationDateDateTime"/>
										<bean:define id="evaluationDate" ><%= evaluationDateDateTime.toString("dd/MM/yyyy") %></bean:define>
										<bean:write name="evaluationDate"/>
									</td>
									<td><bean:message name="markSheet" property="markSheetType.name" bundle="ENUMERATION_RESOURCES"/></td>
									<td>(<bean:write name="markSheet" property="enrolmentEvaluationsCount"/> <bean:message key="label.markSheet.students"/>)</td>
									<td>- D<bean:write name="markSheet" property="responsibleTeacher.teacherNumber"/> -</td>
									<td><em><bean:message name="markSheet" property="markSheetState.name" bundle="ENUMERATION_RESOURCES"/></em></td>
									<logic:equal name="markSheet" property="submittedByTeacher" value="true">
										<td>(<bean:message key="label.markSheet.submittedByTeacher"/>)</td>
									</logic:equal>
								</tr>
							</table>
						</td>
						<td>
							<html:link action='<%= "/markSheetManagement.do?method=viewMarkSheet" + url %>' paramId="msID" paramName="markSheet" paramProperty="idInternal">
								<bean:message key="label.markSheet.view" />
							</html:link>
							
							<logic:equal name="markSheet" property="notConfirmed" value="true">
								<html:link action='<%= "/editMarkSheet.do?method=prepareEditMarkSheet" + url %>' paramId="msID" paramName="markSheet" paramProperty="idInternal">
									<bean:message key="label.markSheet.edit" />
								</html:link>
								<html:link action='<%= "/markSheetManagement.do?method=prepareDeleteMarkSheet" + url %>' paramId="msID" paramName="markSheet" paramProperty="idInternal">
									<bean:message key="label.markSheet.remove" />
								</html:link>
								<html:link action='<%= "/markSheetManagement.do?method=prepareConfirmMarkSheet" + url %>' paramId="msID" paramName="markSheet" paramProperty="idInternal">
									<bean:message key="label.markSheet.confirm" />
								</html:link>
							</logic:equal>							
							<logic:equal name="markSheet" property="notConfirmed" value="false">
								<html:link action='<%= "/rectifyMarkSheet.do?method=prepareRectifyMarkSheet" + url %>' paramId="msID" paramName="markSheet" paramProperty="idInternal">
									<bean:message key="label.markSheet.rectify" />
								</html:link>
							</logic:equal>
						</td>
					</tr>
				</logic:iterate>
				<tr><td></td><td></td></tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
</logic:present>