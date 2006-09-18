<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<logic:present name="executionCourse">

	<bean:define id="executionCourseID" name="executionCourse" property="idInternal"/>	
	<bean:define id="submitUrl">/manageExecutionCourse.do?method=listExecutionCoursesToImportEvaluationMethod&executionCourseID=<bean:write name="executionCourseID"/></bean:define>
	<bean:define id="showEvaluationMethod">/manageExecutionCourse.do?method=evaluationMethod&executionCourseID=<bean:write name="executionCourseID"/></bean:define>	
	<bean:define id="postBackLink">/manageExecutionCourse.do?method=prepareImportEvaluationMethodPostBack&executionCourseID=<bean:write name="executionCourseID"/></bean:define>	
	<bean:define id="invalidLink">/manageExecutionCourse.do?method=prepareBiliographicReferencesInvalid&executionCourseID=<bean:write name="executionCourseID"/></bean:define>	

	<H2><bean:message key="link.import.evaluationMethod"/></H2>
							
	<fr:edit id="importContentBean" name="importContentBean" schema="ChooseDegreePeriodAndCurricularYearToImportLessonPlannings"
		 action="<%= submitUrl %>">
		<fr:destination name="postBack" path="<%= postBackLink %>"/>
		<fr:destination name="invalid" path="<%= invalidLink %>"/>
		<fr:destination name="cancel" path="<%= showEvaluationMethod %>"/>
		<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4"/>
		        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>

	<logic:notEmpty name="importContentBean" property="curricularYear">	
	 	<logic:notEmpty name="importContentBean" property="executionPeriod">	
	 		<logic:notEmpty name="importContentBean" property="executionDegree">	
				<br/>
				<bean:define id="importEvaluationMethodUrl">/manageExecutionCourse.do?method=importEvaluationMethod&executionCourseID=<bean:write name="executionCourseID"/></bean:define>		
				<H3><bean:message key="label.choose.course"/></H3>			
				<fr:edit id="importContentBeanWithExecutionCourse" name="importContentBean" schema="ListExecutionCoursesToImportContent" 
					action="<%= importEvaluationMethodUrl %>" >
					<fr:destination name="cancel" path="<%= showEvaluationMethod %>"/>
					<fr:destination name="invalid" path="<%= invalidLink %>"/>
					<fr:layout name="tabular" >
							<fr:property name="classes" value="tstyle4"/>
					        <fr:property name="columnClasses" value="listClasses,,"/>
					</fr:layout>
				</fr:edit>						
			</logic:notEmpty>
		</logic:notEmpty>
	</logic:notEmpty>
	
	<logic:notEmpty name="shifts">		
		<br/>	
		<H3><bean:message key="label.choose.shift"/></H3>
		<html:form action="/manageExecutionCourse">
			<html:hidden property="method" value="importLessonPlanningsBySummaries"/>
			<html:hidden property="executionCourseID" value="<%= executionCourseID.toString() %>"/>
			<table class="tstyle4" border="0">	
				<tr>
					<th class="listClasses"><bean:message key="label.shift"/>:</th>		
					<td>
						<html:select bundle="HTMLALT_RESOURCES" altKey="select.shift" property="shiftID" >
							<html:options collection="shifts" property="value" labelProperty="label"/>
						</html:select>				
					</td>					
				</tr>				
			</table>
			<html:submit><bean:message key="label.submit"/></html:submit>
			<html:button property="cancel" onclick="this.form.method.value='evaluationMethod';this.form.submit()"><bean:message key="button.cancel"/></html:button>	
		</html:form>
	</logic:notEmpty>
	
</logic:present>
