<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
<%@ page import="DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons"%>
<%@ page import="DataBeans.InfoLesson"%>
<%@ page import="java.util.Calendar" %>
</br>
	</br>
		
<logic:present name="siteView" property="component" >
	<bean:define id="component" name="siteView" property="component"/>
	<bean:define id="ePName" name="component" property="infoExecutionPeriodName"/>
	<bean:define id="eYName" name="component" property="infoExecutionYearName" />
	
			<table align="center" width="95%" cellspacing='1' cellpadding='1'>
					<tr >
						<td class="listClasses-header" width="20%" rowspan="2">
							<bean:message key="property.turno"/>
						</td>
						<td class="listClasses-header" colspan="4" width="60%">
							<bean:message key="property.lessons"/>
						</td>
						<td class="listClasses-header" width="20%" rowspan="2">
							<bean:message key="property.classes"/>
						</td>
					</tr>
					<tr>
						<td class="listClasses-header" width="20%">
							<bean:message key="property.lesson.weekDay"/>
						</td>
						<td class="listClasses-header" width="10%">
							<bean:message key="property.lesson.beginning"/>
						</td>
						<td class="listClasses-header" width="10%">
							<bean:message key="property.lesson.end"/>
						</td>
						<td class="listClasses-header" width="20%">
							<bean:message key="property.lesson.room"/>
						</td>
					</tr>
			
				<logic:iterate id="infoShift" name="component" property="shifts"  indexId="infoShiftIndex">

					<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" length="1" indexId="infoLessonIndex">
                       <% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
                       <% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
						<tr >
							<td  class="listClasses" rowspan="<%=((InfoShiftWithAssociatedInfoClassesAndInfoLessons) infoShift).getInfoLessons().size() %>">
									<bean:write name="infoShift" property="infoShift.nome"/>
							</td>
							<td class="listClasses">
							<bean:write name="infoLesson" property="diaSemana"/> &nbsp;
							</td>
							<td class="listClasses">
								<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
							</td>
							<td class="listClasses">
								<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>								
							</td>
							
							<td class="listClasses">
								<a href='siteViewer.do?method=roomViewer&amp;roomName=<bean:write name="infoLesson" property="infoSala.nome"/>&amp;objectCode=<bean:write name="executionPeriodCode" />'>
									<bean:write name="infoLesson" property="infoSala.nome"/>
								</a>
							</td>

							<td  class="listClasses" rowspan=<%=((InfoShiftWithAssociatedInfoClassesAndInfoLessons) infoShift).getInfoLessons().size() %>>
								<logic:iterate id="infoClass" name="infoShift" property="infoClasses">

										<bean:define id="className" name="infoClass" property="nome" toScope="request"/>
										<bean:define id="degreeInitials" name="infoClass" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.sigla" toScope="request"/>
										<bean:define id="nameDegreeCurricularPlan" name="infoClass" property="infoExecutionDegree.infoDegreeCurricularPlan.name" toScope="request"/>
										<a href="viewClassTimeTableWithClassNameAndDegreeInitialsAction.do?className=<%= request.getAttribute("className").toString() %>&amp;ePName=<bean:write name="ePName" />&amp;eYName=<bean:write name="eYName" />&amp;degreeInitials=<%= request.getAttribute("degreeInitials").toString() %>&amp;nameDegreeCurricularPlan=<%= request.getAttribute("nameDegreeCurricularPlan").toString() %>">
											<bean:write name="infoClass" property="nome" /> <br/>
										</a>
								</logic:iterate>
							</td>
						</tr>
					</logic:iterate>
					

					<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" offset="1">
                       <% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
                       <% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
						<tr >
							<td class="listClasses">
							<bean:write name="infoLesson" property="diaSemana"/> &nbsp;
							</td>
							<td class="listClasses">
								<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
							</td>
							<td class="listClasses">
								<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>
							</td>
							<td class="listClasses">
								<a href='siteViewer.do?method=roomViewer&amp;roomName=<bean:write name="infoLesson" property="infoSala.nome"/>&amp;objectCode=<bean:write name="executionPeriodCode" />'>
									<bean:write name="infoLesson" property="infoSala.nome"/>
								</a>
							</td>
						</tr>
					</logic:iterate>

				</logic:iterate>
			</table>			
	</logic:present>
	
	<logic:notPresent name="siteView" property="component" >
		<bean:message key="message.public.notfound.infoShifts"/>
	</logic:notPresent>		



