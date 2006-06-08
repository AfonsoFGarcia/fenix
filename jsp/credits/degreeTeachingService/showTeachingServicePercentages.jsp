<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<bean:define id="hoursPattern">HH : mm</bean:define>

<bean:define id="teacher" name="professorship" property="teacher" scope="request" />
<bean:define id="teacherId" name="teacher" property="idInternal" />
<bean:define id="executionCourse" name="professorship" property="executionCourse" scope="request" />
<bean:define id="executionPeriodId" name="executionCourse" property="executionPeriod.idInternal" />

<p class="infoselected">
	<b><bean:message key="label.teacher.name" />:</b> <bean:write name="teacher" property="person.nome"/><br />
	<bean:define id="teacherNumber" name="teacher" property="teacherNumber"/>
	<b><bean:message key="label.teacher.number" />:</b> <bean:write name="teacherNumber"/> <br />

	<b> <bean:message key="label.execution-course.name" />:</b> <bean:write name="executionCourse" property="nome"/> <br />
	<b><bean:message key="label.execution-period" />:</b> <bean:write name="executionCourse" property="executionPeriod.name"/> - <bean:write name="executionCourse" property="executionPeriod.executionYear.year"/> <br />	
</p>

<span class="error"><html:errors/></span>
<html:messages id="message" message="true">
	<span class="error">
		<bean:write name="message" filter="false"/>
	</span>
</html:messages>

<html:form action="/degreeTeachingServiceManagement">
	<html:hidden property="teacherNumber" value="<%= teacherNumber.toString() %>"/>
	<html:hidden property="teacherId" value="<%= teacherId.toString() %>" />
	<html:hidden property="executionPeriodId" />
	<html:hidden property="executionCourseId"/>
	<html:hidden property="professorshipID"/>
	<html:hidden property="method" value="updateTeachingServices"/>

	<table class="tstyle4">
		<%-- ********************************* HEADER *********************************************** --%>
		<tr>
			<th rowspan="2" width="10%"><bean:message key="label.shift"/></th>
			<th rowspan="2" width="5%"><bean:message key="label.shift.type"/></th>
			<th colspan="4" width="40%"><bean:message key="label.lessons"/></th>
			<th rowspan="2"><bean:message key="label.professorship.percentage"/></th>
			<th><bean:message key="label.teacher.applied"/></th>			
		</tr>
		<tr>
			<th><bean:message key="label.day.of.week"/></th>
			<th><bean:message key="label.lesson.start"/></th>
			<th><bean:message key="label.lesson.end"/></th>
			<th><bean:message key="label.lesson.room"/></th>			
			<th><bean:message key="label.teacher"/> - <bean:message key="label.professorship.percentage"/></th>
		</tr> 
		<%-- ********************************* SHIFTS *********************************************** --%>	
		<logic:iterate id="teachingServicePercentage" name="teachingServicePercentages">
			<bean:define id="shift" name="teachingServicePercentage" property="shift"/>
			<bean:define id="availablePercentage" name="teachingServicePercentage" property="availablePercentage"/>
			
			<bean:size id="lessonsSize" name="shift" property="associatedLessons" />	

				<logic:equal name="lessonsSize" value="0">
					<tr>
						<td><bean:write name="shift" property="nome"/></td>
						<td><bean:write name="shift" property="tipo.siglaTipoAula"/></td>
						<td colspan="7"> N�o tem aulas </td>
					</tr>
				</logic:equal>

				<logic:notEqual name="lessonsSize" value="0">
					<logic:iterate id="lesson" name="shift" property="lessonsOrderedByWeekDayAndStartTime" indexId="indexLessons" >
			            <logic:equal name="indexLessons" value="0">

							<tr>
							<td rowspan="<%= lessonsSize %>"><bean:write name="shift" property="nome"/></td>
							<td rowspan="<%= lessonsSize %>"><bean:write name="shift" property="tipo.siglaTipoAula"/></td>
							<td>
								<bean:write name="lesson" property="diaSemana"/>
							</td>
							<td>
								<dt:format patternId="hoursPattern">
									<bean:write name="lesson" property="inicio.time.time"/>
								</dt:format>
							</td>
							<td>
								<dt:format patternId="hoursPattern">
									<bean:write name="lesson" property="fim.time.time"/>
								</dt:format>
							</td>
							<td>
								<bean:write name="lesson" property="sala.nome"/>					
							</td>
							<td rowspan="<%= lessonsSize %>">
								<logic:greaterThan name="availablePercentage" value="0">
									<bean:define id="propertyName">
										teacherPercentageMap(<bean:write name="shift" property="idInternal"/>)
									</bean:define>
									<html:text property='<%= propertyName %>' size="4" /> %
								</logic:greaterThan>
								<logic:equal name="availablePercentage" value="0">
									&nbsp;
								</logic:equal>
							</td>
							<td rowspan="<%= lessonsSize %>">
								<bean:size id="teachingServiceSize" name="shift" property="degreeTeachingServices"/>
								<logic:equal name="teachingServiceSize" value="0">&nbsp;</logic:equal>
								<logic:notEqual name="teachingServiceSize" value="0">
									<logic:iterate id="teachingService"	name="shift" property="degreeTeachingServices" 
														  indexId="indexPercentage">						
							    		<bean:write name="teachingService" property="professorship.teacher.person.nome" />
				 						&nbsp;-&nbsp;<bean:write name="teachingService" property="percentage" />
				 						<br />
									</logic:iterate> 					
								</logic:notEqual>
							</td>						
							</tr>
						</logic:equal>
						
						<logic:greaterThan name="indexLessons" value="0">
							<tr>
								<td>
									<bean:write name="lesson" property="diaSemana"/>
								</td>
								<td>
									<dt:format patternId="hoursPattern">
										<bean:write name="lesson" property="inicio.time.time"/>
									</dt:format>
								</td>
								<td>
									<dt:format patternId="hoursPattern">
										<bean:write name="lesson" property="fim.time.time"/>
									</dt:format>
								</td>
								<td>
									<bean:write name="lesson" property="sala.nome"/>					
								</td>						
							</tr>
						</logic:greaterThan>
					</logic:iterate>
				</logic:notEqual>	
		</logic:iterate>
	</table>
	<p>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
	<html:submit styleClass="inputbutton" onclick="this.form.method.value='cancel';this.form.page.value='0'">
			<bean:message key="button.cancel"/>
	</html:submit>
	</p>
</html:form>
