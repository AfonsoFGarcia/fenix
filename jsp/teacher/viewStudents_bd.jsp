<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="DataBeans.InfoGroupProperties" %>
<%@ page import="DataBeans.InfoGroupProjectStudents" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Date" %>
<%@ page import="Util.Data" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="DataBeans.InfoLesson" %>

<logic:present name="attendacies">
<logic:present name="siteView">
<logic:present name="shifts">
<bean:define id="shifts" name="shifts" property="infoShifts" type="java.util.List"/>
<bean:define id="studentsComponent" name="siteView" property="component" type="DataBeans.InfoSiteStudents"/>
<bean:define id="commonComponent" name="siteView" property="commonComponent" type="DataBeans.InfoSiteCommon"/>
<bean:define id="infosGroups" name="infosGroups" type="java.util.List"/>
<bean:define id="projects" name="projects" type="java.util.List"/>
    <span class="error"><html:errors/></span>
	<bean:size id="studentsListSize" name="studentsComponent" property="students"/>

<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop">
			<bean:message key="message.students.explanation"/>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td class="infoop">
			<bean:message key="message.enrollmentsWarning" />
		</td>
	</tr>
	
</table>
<h2>
	<bean:write name="commonComponent" property="executionCourse.nome" />
</h2>


<html:form action="/studentsByCurricularCourse.do" method="get">
<html:hidden property="objectCode"/>
<html:hidden property="method" value="readStudents"/>
<table>
	<tr valign="top">
		<td>
			<bean:message key="label.selectShift"/>
		</td>
		<td colspan="3">

			<html:select property="shiftCode">
							<option value="null">
								<bean:message key="label.select.SelectShift"/>
							</option>
							<logic:iterate type="DataBeans.InfoShift" name="shifts" id="shift">
								<option value=<bean:write name="shift" property="idInternal"/>
									<%
									String text = new String();
									text += shift.getNome();
									text += "   (";
									for (Iterator iterator= shift.getInfoLessons().iterator(); iterator.hasNext();)
									{
										InfoLesson lesson= (InfoLesson) iterator.next();
										text += lesson.getDiaSemana().toString() + "  ";
										text += lesson.getInicio().get(Calendar.HOUR_OF_DAY) + ":";
										text += lesson.getInicio().get(Calendar.MINUTE) + "-";
										text += lesson.getFim().get(Calendar.HOUR_OF_DAY) + ":";
										text += lesson.getFim().get(Calendar.MINUTE) + " ";
										text += lesson.getInfoSala().getNome();
										if (iterator.hasNext())
											text += " ;";
									}
									text += ")"; 
									Integer shiftCode = null;
									try
									{
										shiftCode = new Integer((String)request.getParameter("shiftCode"));
									}
									catch (NumberFormatException ex)
									{}
									if ((shiftCode != null) && (shiftCode.intValue() == shift.getIdInternal().intValue()))
										out.print("selected");
									out.print(">"+text);									
									%>
								</option>
							</logic:iterate>
						</html:select>
						<html:submit property="submition"><bean:message key="button.selectShift"/></html:submit>
					</td>
		</tr>
</table>
<table>
	<tr>
		<td colspan="3">
			<h3>
				<%=studentsComponent.getStudents().size()%> <bean:message key="message.enrolledStudents"/>
			</h3>
		</td>
	</tr>
	<tr valign="top">
	       <td colspan="3">
		<%Map sendMailParameters = new TreeMap(request.getParameterMap());
              sendMailParameters.put("method","prepare");
		request.setAttribute("sendMailParameters",sendMailParameters);%>
		<bean:define id="sendMailLinkParameters" type="java.util.Map" name="sendMailParameters"/>
		   <html:link page="/sendMailToAllStudents.do" name="sendMailLinkParameters">
			<bean:message key="link.sendEmailToAllStudents"/><br/><br/>
		   </html:link>

		<%Map spreadSheetArgs = new TreeMap(request.getParameterMap());
		request.setAttribute("spreadSheetArgs",spreadSheetArgs);%>
			<bean:define id="spreadSheetLinkArgs" type="java.util.Map" name="spreadSheetArgs"/>
				<html:link page="/getTabSeparatedStudentList.do" name="spreadSheetLinkArgs">
				<bean:message key="link.getExcelSpreadSheet"/><br/>
			</html:link>

			 <h2>
				<logic:present name="studentsComponent" property="infoCurricularCourseScope">  		
						&nbsp;-&nbsp;&nbsp;-&nbsp;				
						<bean:define id="ano" name="studentsComponent" property="infoCurricularCourseScope.infoCurricularSemester.infoCurricularYear.year" />
						<bean:define id="semestre" name="studentsComponent" property="infoCurricularCourseScope.infoCurricularSemester.semester" />					
						<bean:write name="studentsComponent" property="infoCurricularCourseScope.infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.sigla" />
						<logic:notEqual name="studentsComponent" property="infoCurricularCourseScope.infoBranch.name" value="">
							<bean:message key="property.curricularCourse.branch" />
							<bean:write name="studentsComponent" property="infoCurricularCourseScope.infoBranch.name"/>&nbsp;
						</logic:notEqual>						
						<bean:message key="label.year" arg0="<%= String.valueOf(ano) %>"/>
						<bean:message key="label.period" arg0="<%= String.valueOf(semestre) %>"/>
			   </logic:present>
	   		</h2><br />
		   </td>
		</tr> 
		<tr>
			<td class="listClasses-header">
				<bean:message key="label.number" /> 
		   </td>
			<td class="listClasses-header">
				<bean:message key="label.enrollmentStatus" /> 
		   </td>
			<td class="listClasses-header">
				<bean:message key="label.Degree" /> 
		   </td>
			<td class="listClasses-header">
				<bean:message key="label.name" />
		   </td>
		   <logic:present name="projects">
			<logic:notEmpty name="projects">
				<logic:iterate name="projects" id="project" type="DataBeans.InfoGroupProperties">
					<td class="listClasses-header">
					<bean:message key="label.projectGroup"/>&nbsp;
					<bean:write name="project" property="name"/>
					</td>
				</logic:iterate>
			</logic:notEmpty>
		   </logic:present>
			<td class="listClasses-header">
				<bean:message key="label.mail" />
		   </td>
		</tr>    		
		<bean:define id="mailingList" value=""/>

    	<logic:iterate id="attendacy" type="DataBeans.InfoFrequenta" name="attendacies"> 
			
			<tr>
				<td class="listClasses">
					<bean:write name="attendacy" property="aluno.number"/>&nbsp;
				</td>
				<td class="listClasses">
					<logic:present name="attendacy" property="infoEnrolment">
						<bean:message key="message.yes"/>
					</logic:present>
					<logic:notPresent name="attendacy" property="infoEnrolment">
						<bean:message key="message.no"/>
					</logic:notPresent>
				</td>
				<td class="listClasses">
					<logic:present name="attendacy" property="infoEnrolment">
						<bean:write name="attendacy" property="infoEnrolment.infoStudentCurricularPlan.infoDegreeCurricularPlan.name"/>
					</logic:present>
					<logic:notPresent name="attendacy" property="infoEnrolment">
						N/A
					</logic:notPresent>
				</td>
				<td class="listClasses">
					<bean:write name="attendacy" property="aluno.infoPerson.nome"/>
				</td>
				<%
				for (Iterator projectsIterator= projects.iterator(); projectsIterator.hasNext();)
				{
					InfoGroupProperties element= (InfoGroupProperties) projectsIterator.next();
					int projectIdInternal = element.getIdInternal().intValue();
					int groupNumber =-1;
					int shiftCode = -1;
					int executionCourseCode = -1;
					int groupPropertiesCode = -1;
					int studentGroupCode = -1;
					Integer studentNumber = attendacy.getAluno().getNumber();
				
					for (Iterator groupsIterator= infosGroups.iterator(); groupsIterator.hasNext();)		
					{
					
						InfoGroupProjectStudents groupInfo= (InfoGroupProjectStudents) groupsIterator.next();
						if (projectIdInternal == groupInfo.getStudentGroup().getGroupProperties().getIdInternal().intValue() &&
							groupInfo.isStudentMemberOfThisGroup(studentNumber))
						{
							groupNumber = groupInfo.getStudentGroup().getGroupNumber().intValue();
							studentGroupCode = groupInfo.getStudentGroup().getIdInternal().intValue();
							shiftCode = groupInfo.getStudentGroup().getShift().getIdInternal().intValue();
							executionCourseCode = groupInfo.getStudentGroup().getGroupProperties().getExecutionCourse().getIdInternal().intValue();
							break;
						}
					}
				%>
				<td class="listClasses">
				<%request.setAttribute("parameters",new TreeMap());%>
				<bean:define name="parameters" type="java.util.TreeMap" id="parameters"/>
				<%
					if (groupNumber != -1)
					{
					parameters.put("shiftCode",new Integer(shiftCode));
					parameters.put("studentGroupCode",new Integer(studentGroupCode));
					parameters.put("method","viewStudentGroupInformation");
					parameters.put("objectCode",new Integer(executionCourseCode));
					parameters.put("groupPropertiesCode",new Integer(projectIdInternal));
				%>
					<html:link page="/viewStudentGroupInformation.do" name="parameters">
				<%
							out.print(groupNumber);
				%>
					</html:link>
				<%
					}
					else
						out.print("N/A");
				%>
				</td>
				<%
				 }
				%>
				<td class="listClasses">
				<%--	<logic:notEmpty name="student"  property="infoPerson.email"> --%>
					<bean:define id="mail" name="attendacy" property="aluno.infoPerson.email"/>
					<html:link href="<%= "mailto:"+ mail %>"><bean:write name="attendacy" property="aluno.infoPerson.email"/></html:link>
				<%--	</logic:notEmpty>
					<logic:empty name="student"  property="infoPerson.email">
					&nbsp;
					</logic:empty> --%>
				</td>
			</tr>
		<%--	<logic:notEmpty name="student"  property="infoPerson.email"> --%>
			<bean:define id="aux" name="mailingList"/>
			<logic:lessThan name="aux" value="1">
				<bean:define id="mailingList" value="<%= mail.toString() %>"/>	
			</logic:lessThan>
			<logic:greaterThan name="aux" value="0">
				<bean:define id="mailingList" value="<%= aux + ";"+ mail  %>"/>	
			</logic:greaterThan>
	<%--			</logic:notEmpty> --%>
    	</logic:iterate>
		
</table>
</html:form>
<br/>
<br/> 
<%-- TODO: See if works in IE --%>
<%--	<html:link href="<%= "mailto:" + mailingList %>"><bean:message key="message.emailStudents"/></html:link> --%>
    
</logic:present>
</logic:present>
</logic:present>