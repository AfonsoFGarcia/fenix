<%@ page language="java" %>
<%@ page import ="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="DataBeans.InfoExam" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
	   	<table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#FFFFFF" class="infoselected"><p>A licenciatura seleccionada
              &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
            </td>
          </tr>
        </table>
        <br/>
   		<% ArrayList iE = (ArrayList) session.getAttribute(SessionConstants.INFO_EXAMS_KEY); %>
        <h2><bean:message key="title.exams.list"/></h2>
        <span class="error"><html:errors/></span>
		<logic:notPresent name="<%= SessionConstants.INFO_EXAMS_KEY %>" scope="session">
			<table align="center"  cellpadding='0' cellspacing='0'>
				<tr align="center">
					<td>
						<font color='red'> <bean:message key="message.exams.none.for.executionDegree.CurricularYear"/> </font>
					</td>
				</tr>
			</table>
		</logic:notPresent>
		
		<logic:present name="<%= SessionConstants.INFO_EXAMS_KEY %>" scope="session">
			<table align="center" border='1' cellpadding='10'>
				<tr align="center">
					<td>
						<bean:message key="property.course.initials"/>
					</td>
					<td>
						<bean:message key="property.course"/>
					</td>
					<td>
						<bean:message key="property.number.students.attending.course"/>
					</td>
					<td>
						<bean:message key="property.exam.1stExam"/>
					</td>
					<td>
						<bean:message key="property.exam.2stExam"/>
					</td>
				</tr>
	            <% int i = 0; %>
				<logic:iterate id="infoExam" name="<%= SessionConstants.INFO_EXAMS_KEY %>" scope="session">
                       <% Integer iH = new Integer(((InfoExam) iE.get(i)).getBeginning().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer iM = new Integer(((InfoExam) iE.get(i)).getBeginning().get(Calendar.MINUTE)); %>
                       <% Integer fH = new Integer(((InfoExam) iE.get(i)).getEnd().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer fM = new Integer(((InfoExam) iE.get(i)).getEnd().get(Calendar.MINUTE)); %>
				
					<tr align="center">
						<td>
							<bean:write name="infoExam" property="infoExecutionCourse.sigla"/>
						</td>
						<td>
							<bean:write name="infoExam" property="infoExecutionCourse.nome"/>
						</td>
						<td>
							TBA
						</td>
						<td>
							<bean:write name="infoExam" property="day"/>
                           	<%= iH.toString()%> : <%= iM.toString()%>
						</td>
						<td>
							TBA
						</td>
					</tr>
						</logic:iterate>
			</table>
				
		</logic:present>
