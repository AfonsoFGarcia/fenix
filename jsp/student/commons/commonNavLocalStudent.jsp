<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<!-- NOTA: N�o foram incluidas tags do beans tipo <bean:message key="title.listClasses"/> -->
<%--<p><strong>&raquo; <bean:message key="group.enrolment"/></strong></p>--%>
<ul>
	<%-- <li><html:link page="/curricularCourseEnrolmentManager.do?method=start"><bean:message key="link.curricular.course.enrolment"/></html:link></li>--%>
 	<li><html:link page="/studentShiftEnrolmentManager.do?method=prepareStartViewWarning"><bean:message key="link.shift.enrolment"/></html:link></li>
  	<li><html:link page="/listAllSeminaries.do"> <bean:message key="link.seminaries.enrolment"/></html:link> <a href='<bean:message key="link.seminaries.rules" />' target="_blank"><bean:message key="label.seminairies.seeRules"/></a></li>
  	<li><html:link page="/examEnrollmentManager.do?method=viewExamsToEnroll" ><bean:message key="link.exams.enrolment"/></html:link></li>
  	<li><html:link page="/viewEnroledExecutionCourses.do" ><bean:message key="link.groupEnrolment" /></html:link></li>
	<%--<li><html:link page="<%="/studentCurricularCoursesEnrollment.do?method=prepareEnrollment"%>"><bean:message key="link.student.enrollment"/></html:link></li>--%>
	<li><html:link page="<%="/warningFirst.do"%>"><bean:message key="link.student.enrollment"/></html:link></li>
</ul>
<ul>
	<li><html:link page="/studentTimeTable.do" target="_blank" ><bean:message key="link.my.timetable"/></html:link></li>
  	<li><html:link page="/viewCurriculum.do?method=getStudentCP" ><bean:message key="link.student.curriculum"/></html:link></li>
  	<li><html:link page="/studentTests.do?method=viewStudentExecutionCoursesWithTests" ><bean:message key="link.tests"/></html:link></li>
  	<li><html:link page="/studentGaugingTestResults.do"><bean:message key="link.results.test"/></html:link></li>
</ul>
<ul>
	<li><bean:message key="link.finalDegreeWork"/></li>
	<ul>
		<li><html:link page="/finalDegreeWorkCandidacy.do?method=prepareCandidacy&amp;page=0"><bean:message key="link.finalDegreeWork.candidacy"/></html:link></li>
		<li><html:link page="/finalDegreeWorkAttribution.do?method=prepare&amp;page=0"><bean:message key="link.finalDegreeWork.confirmAttribution"/></html:link></li>
	</ul>
</ul>