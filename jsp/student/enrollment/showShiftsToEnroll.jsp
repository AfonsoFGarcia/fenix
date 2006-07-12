<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<span class="error"><html:errors/></span>
<bean:define id="hoursPattern">HH : mm</bean:define>

<bean:define id="infoLessons" name="infoLessons"/>
<bean:define id="studentId" name="studentId"/>
<bean:define id="classId" name="classId"/>
<bean:define id="infoClasslessonsEndTime" name="infoClasslessonsEndTime"/>
<bean:define id="infoLessonsEndTime" name="infoLessonsEndTime"/>

<bean:define id="infoClasslessons" name="infoClasslessons"/>	

<h2><bean:message key="message.showShiftsToEnroll.title" /></h2>

<div class="infoselected">		
<bean:message key="message.showShiftsToEnroll.instructions0" />
	<ul>
	<li><bean:message key="message.showShiftsToEnroll.instructions1" /> <img src="<%= request.getContextPath() + "/images/add1.gif" %>" alt="<bean:message key="add1" bundle="IMAGE_RESOURCES" />" /> <bean:message key="message.showShiftsToEnroll.instructions2" /> <img src="<%= request.getContextPath() + "/images/remove1.gif" %>" alt="<bean:message key="remove1" bundle="IMAGE_RESOURCES" />" /> <bean:message key="message.showShiftsToEnroll.instructions3" /></li>
	<li><bean:message key="message.showShiftsToEnroll.instructions4" /></li>
	</ul>
</div>

<logic:present name="executionCourseID">
<ul>
	<li><span>Disciplina: <strong><bean:write name="infoExecutionCourse" property="nome"/></strong></span></li>
</ul>
</logic:present>

<h2 class="redtxt"><bean:message key="label.class" /> <bean:write name="infoClass" property="nome"/></h2>

<logic:present name="executionCourseID">
	<bean:define id="executionCourseID" name="executionCourseID" type="java.lang.String"/>
	<app:gerarHorario name="infoClasslessons" type="<%= TimeTableType.SHIFT_ENROLLMENT_TIMETABLE %>" studentID="<%= studentId.toString() %>"
		application="<%= request.getContextPath() %>" classID="<%= classId.toString() %>" executionCourseID="<%= executionCourseID.toString() %>"  
	 endTime="<%= infoClasslessonsEndTime.toString() %>" action="add"/>
</logic:present>

<logic:notPresent name="executionCourseID">
<app:gerarHorario name="infoClasslessons" type="<%= TimeTableType.SHIFT_ENROLLMENT_TIMETABLE %>" studentID="<%= studentId.toString() %>"
		application="<%= request.getContextPath() %>" classID="<%= classId.toString() %>" endTime="<%= infoClasslessonsEndTime.toString() %>" action="add"/>
</logic:notPresent>	 

<br/>
<p><strong><bean:message key="message.showShiftsToEnroll.actual.timetable" /></strong></p>

	
<logic:present name="executionCourseID">
	<bean:define id="executionCourseID" name="executionCourseID" type="java.lang.String"/>
	<app:gerarHorario name="infoLessons" type="<%= TimeTableType.SHIFT_ENROLLMENT_TIMETABLE %>" studentID="<%= studentId.toString() %>"
		application="<%= request.getContextPath() %>" classID="<%= classId.toString() %>" executionCourseID="<%= executionCourseID.toString() %>"
	 endTime="<%= infoLessonsEndTime.toString() %>" action="remove"/>
</logic:present>

<logic:notPresent name="executionCourseID">
<app:gerarHorario name="infoLessons" type="<%= TimeTableType.SHIFT_ENROLLMENT_TIMETABLE %>" studentID="<%= studentId.toString() %>"
		application="<%= request.getContextPath() %>" classID="<%= classId.toString() %>" endTime="<%= infoLessonsEndTime.toString() %>" action="remove"/>
</logic:notPresent>

<br />
<ul>
<li><html:link page="/studentShiftEnrollmentManager.do?method=start"><strong><bean:message key="button.finish" /></strong></html:link></li>
</ul>