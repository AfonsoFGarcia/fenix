<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoEnrolment" %>
<%@ page import="DataBeans.InfoEnrolmentInExtraCurricularCourse" %>
<%@ page import="DataBeans.InfoEnrolmentInOptionalCurricularCourse" %>

<span class="error"><html:errors/></span>

<h2 align="left"><bean:message key="title.studentCurricularPlan"/></h2>


<html:form action="/alterStudentCurricularPlan">
<html:hidden property="method" value="edit"/>
<bean:define id="idInternal" name="studentCurricularPlan" property="idInternal"/>
<html:hidden property="studentCurricularPlanId" value="<%= idInternal.toString() %>"/>
<table border="0" cellspacing="3" cellpadding="10">
	<tr>
		<td>
			<b><bean:message key="label.student.degree" /></b>
			<bean:write name="studentCurricularPlan" property="infoDegreeCurricularPlan.name" />
			<bean:write name="studentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome" />
		</td>						
	</tr>	
	<tr>
		<td>
			<b><bean:message key="label.student.branch" /></b>
			<bean:write name="studentCurricularPlan" property="infoBranch.code" />
			<bean:write name="studentCurricularPlan" property="infoBranch.name" />
		</td>						
	</tr>		
	<tr>
		<td>
			<b><bean:message key="label.student.specialization" /></b>
			<bean:write name="studentCurricularPlan" property="specialization" />
		</td>
	</tr>		
	<tr>
		<td>
			<b><bean:message key="label.student.state" /></b>
			<logic:present name="<%= SessionConstants.STATE %>">
				<html:select name="studentCurricularPlanForm" property="currentState">	
	          	 <html:options collection="<%= SessionConstants.STATE %>" property="value" labelProperty="label"/>
	    	 	</html:select>   
			</logic:present>
		</td>						
	</tr>	
	<tr>
		<td>
			<b><bean:message key="label.student.startDate" /></b>
			<bean:write name="studentCurricularPlan" property="startDate" />
		</td>								
	</tr>	
	<tr>
		<td>
			<b><bean:message key="label.student.credits" /></b>
			<logic:present name="<%= SessionConstants.STATE %>">
				<html:text name="studentCurricularPlanForm" property="credits" size="4"/>
			</logic:present>
		</td>
	</tr>	
	<tr>
		<td>
			<b><bean:message key="label.student.completedCourses" /></b>	
			<bean:write name="studentCurricularPlan" property="completedCourses" />
		</td>
	</tr>
	<tr>
		<td>
			<b><bean:message key="label.student.enrolledCourses" /></b>
			<bean:write name="studentCurricularPlan" property="enrolledCourses" />
		</td>
	</tr>	
	<tr>
		<td>
			<b><bean:message key="label.student.classification" /></b>
			<bean:write name="studentCurricularPlan" property="classification" />
		</td>
	</tr>	
	<tr>
		<td>
			<logic:notPresent name="studentCurricularPlan" property="infoEnrolments">
				<p><bean:message key="message.no.enrolments" /></p>
			</logic:notPresent>
			
			<logic:present name="studentCurricularPlan" property="infoEnrolments">
				<bean:size id="sizeEnrolments" name="studentCurricularPlan" property="infoEnrolments" />

				<logic:lessEqual name="sizeEnrolments" value="0">
					<p><h2><bean:message key="message.no.enrolments" /></h2></p>
				</logic:lessEqual>
				
				<logic:greaterThan name="sizeEnrolments" value="0">
				<html:hidden property="size" value='<%= pageContext.findAttribute("sizeEnrolments").toString() %>'/>
					<table>
						<tr>
							<td colspan="3" align="center"><h3><bean:message key="title.enrolments"/></h3></td>
						</tr>	
						<tr>
							<th align="left"><bean:message key="label.enrolment.curricularCourse"/></th>
							<th align="left"><bean:message key="label.enrolment.type"/></th>
							<th align="left"><bean:message key="label.enrolment.state"/></th>
							<th align="left"><bean:message key="label.enrolment.year"/></th>
							<th align="left"><bean:message key="label.enrolment.extraCurricular"/></th>							
						</tr>
						<logic:iterate id="infoEnrolment" name="studentCurricularPlan" property="infoEnrolments">
							<tr>	
								<td>
									<bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularCourse.code" />&nbsp;
									<bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularCourse.name" />
								</td>
								<td>
									<%if (infoEnrolment instanceof InfoEnrolmentInOptionalCurricularCourse) { %>
										<bean:message key="label.enrolment.type.optional" />
									<%} else if (infoEnrolment instanceof InfoEnrolmentInExtraCurricularCourse) { %>
										<bean:message key="label.enrolment.type.extra" />
									<%} else { %> 										
										<bean:message key="label.enrolment.type.normal" />
									<%}	%>								
								</td>
								<td>
									<bean:define id="state" name="infoEnrolment" property="enrolmentState" />
									<bean:message key="<%= pageContext.findAttribute("state").toString() %>"/>
								</td>
								<td>
									<bean:write name="infoEnrolment" property="infoExecutionPeriod.infoExecutionYear.year" />&nbsp;
									<bean:write name="infoEnrolment" property="infoExecutionPeriod.name" />
								</td>
								<td>
									<bean:define  id="idEnrolment" name="infoEnrolment" property="idInternal"/>
									<html:multibox property="extraCurricularCourses"><bean:write name="infoEnrolment" property="idInternal"/> </html:multibox >&nbsp;
								</td>
							</tr>		
						</logic:iterate>
					</table>
				</logic:greaterThan>
			</logic:present>				
		</td>
	</tr>	
</table>	
 <html:submit value="Submeter" styleClass="inputbutton">
</html:submit>
</html:form>	






