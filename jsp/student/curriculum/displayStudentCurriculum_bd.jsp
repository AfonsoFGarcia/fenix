<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants, Util.CurricularCourseType" %>
<%@ page import="Util.EnrollmentState" %>
<%@ page import="Util.EnrollmentStateSelectionType" %>
<%@ page import="DataBeans.InfoEnrolmentInExtraCurricularCourse" %>
<%@ page import="DataBeans.InfoEnrolment" %>
<%@ page import="java.util.List" %>

<%@ page import="DataBeans.util.InfoStudentCurricularPlansWithSelectedEnrollments" %>
<%@ page import="DataBeans.InfoStudentCurricularPlan" %>

  <span class="error"><html:errors/></span>

	<bean:define id="infoStudentCPs" name="studentCPs" scope="request" type="DataBeans.util.InfoStudentCurricularPlansWithSelectedEnrollments" />
	<bean:define id="selectedStudentCPs" name="infoStudentCPs" property="infoStudentCurricularPlans" />

	<bean:size id="numCPs" name="selectedStudentCPs"/>

	<html:form action="/viewCurriculum.do?method=getStudentCP">
		
		<table width="100%">
			<tr>
				<td>
					<bean:message key="label.studentCurricularPlan" bundle="STUDENT_RESOURCES" />	
				</td>
				<td>
					<html:select property="studentCPID"
						onchange='document.studentCurricularPlanAndEnrollmentsSelectionForm.submit();'>
						<html:options collection="allSCPs" property="value" labelProperty="label" />
					</html:select>
				</td>
			</tr>
			
			<tr>
				<td>
					<bean:message key="label.enrollmentsFilter" bundle="STUDENT_RESOURCES" />	
				</td>
				<td>
					<html:select property="select"
						onchange='document.studentCurricularPlanAndEnrollmentsSelectionForm.submit();' >
						<html:options collection="enrollmentOptions" property="value" labelProperty="label"/>
					</html:select>
				</td>
			</tr>
		</table>
		
		<logic:present property="studentNumber" name="studentCurricularPlanAndEnrollmentsSelectionForm">
			<html:hidden name="studentCurricularPlanAndEnrollmentsSelectionForm" property="studentNumber"/>
		</logic:present>
	</html:form>


	<logic:notEqual name="numCPs" value="0">
		
		<table width="100%">		
			<logic:iterate id="infoStudentCurricularPlan" name="selectedStudentCPs">
				<tr><td><br></br></td></tr>
	
				<tr>
					<td colspan=5>
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td bgcolor="#FFFFFF" class="infoselected">
									<bean:message key="label.person.name" />:
									<bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/>
									<br/>
									<bean:message key="label.degree.name" />:
									<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>
									<br/>
									<bean:message key="label.curricularplan" bundle="STUDENT_RESOURCES" />:
									<%  
															String label = "";  
									    
									    label += ((InfoStudentCurricularPlan)infoStudentCurricularPlan).getStartDate()+" - ";
									    label += "("+((InfoStudentCurricularPlan)infoStudentCurricularPlan).getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso()+")";
									    			    if (((InfoStudentCurricularPlan)infoStudentCurricularPlan).getSpecialization() != null)
								        label += " - "+((InfoStudentCurricularPlan)infoStudentCurricularPlan).getSpecialization();
									%>
									<%=label%>
									<br/>
									<bean:message key="label.number" />:
									<bean:write name="infoStudentCurricularPlan" property="infoStudent.number"/>
								</td>
							</tr>
						</table>
					</td>
				</tr>				
				
				
				<tr><td><br></br></td></tr>
				
				
		
				<bean:define id="id" name="infoStudentCurricularPlan" property="idInternal"/>
				<bean:define id="curriculum" 			name="infoStudentCPs" property='<%="infoEnrollmentsForStudentCPById("+ id +")"%>'/>
			
		
				<%
					String actualYear = "0";
					String actualSemester = "0";			
				%>
			
				<bean:size id="numEnrollments" name="curriculum"/>
				
				<logic:notEqual name="numEnrollments" value="0">
				  	<logic:iterate id="enrolment" name="curriculum"> 
				
						<%
							if(!actualYear.equals(((InfoEnrolment)enrolment).getInfoExecutionPeriod().getInfoExecutionYear().getYear()) ||
							   !actualSemester.equals(((InfoEnrolment)enrolment).getInfoExecutionPeriod().getSemester().toString()))
							{
								actualYear = ((InfoEnrolment)enrolment).getInfoExecutionPeriod().getInfoExecutionYear().getYear();
								actualSemester = ((InfoEnrolment)enrolment).getInfoExecutionPeriod().getSemester().toString();
						%>	
							<tr>
							  	<td class="listClasses-header">
							  		<bean:message key="label.executionYear" />
							  	</td >
							  	<td class="listClasses-header">
							  		<bean:message key="label.semester" />
							  	</td >
							  	<td class="listClasses-header">
							  		<bean:message key="label.degree.name" />
							  	</td>
							  	<td class="listClasses-header">
							  		<bean:message key="label.curricular.course.name" />
							  	</td>
							  	<td class="listClasses-header">
							  		<bean:message key="label.finalEvaluation" />
							  	</td>
						  	</tr>	
								
						<%		
							}
						%>
				
				
				
										
				  		<tr>
						  <td class="listClasses">
						    <bean:write name="enrolment" property="infoExecutionPeriod.infoExecutionYear.year"/>
						  </td>
						  				 
						  <td class="listClasses">
						    <bean:write name="enrolment" property="infoExecutionPeriod.semester"/>
						  </td>
						  <td class="listClasses">
						    <bean:write name="enrolment" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.sigla"/>
						  </td>
						  <td class="listClasses" style="text-align:left">
						    <bean:write name="enrolment" property="infoCurricularCourse.name"/>
							<% if ( !((InfoEnrolment) enrolment).getEnrollmentTypeResourceKey().equals("option.curricularCourse.normal") ) {%>
								(<bean:message name="enrolment" property="enrollmentTypeResourceKey" bundle="DEFAULT"/>)
							<% } %>
						  </td>
						  <td class="listClasses">
							<logic:notEqual name="enrolment" property="enrollmentState" value="<%= EnrollmentState.APROVED.toString() %>">
								<bean:message name="enrolment" property="enrollmentState.name" bundle="ENUMERATION_RESOURCES" />
							</logic:notEqual>
							
							<logic:equal name="enrolment" property="enrollmentState" value="<%= EnrollmentState.APROVED.toString() %>">
								<bean:write name="enrolment" property="infoEnrolmentEvaluation.grade"/>
							</logic:equal>
						  </td>
				  		</tr>
					</logic:iterate>
				
				</logic:notEqual>
			    <logic:equal name="numEnrollments" value="0">
					<tr><td>
						<span class="error"><bean:message key="message.no.enrolments" bundle="STUDENT_RESOURCES"/></span>
					</td></tr>
				</logic:equal>
				<tr><td><br></br><br></br></td></tr>			
				
			</logic:iterate>
		</table>
	
	
	</logic:notEqual>
  	<logic:equal name="numCPs" value="0">
		<span class="error"><bean:message key="message.no.curricularplans" bundle="STUDENT_RESOURCES"/></span>
 	 </logic:equal>



