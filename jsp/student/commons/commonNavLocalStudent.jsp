<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<!-- NOTA: N�o foram incluidas tags do beans tipo <bean:message key="title.listClasses"/> -->
<p><strong>&raquo; <bean:message key="group.enrolment"/></strong></p>
<ul>
  <li><html:link page="/curricularCourseEnrolmentManager.do?method=start"><bean:message key="link.curricular.course.enrolment"/></html:link></li>
  <li><html:link page="/studentShiftEnrolmentManager.do?method=enrollCourses&amp;firstTime=yes"><bean:message key="link.shift.enrolment"/></html:link></li>
  <li><html:link page="/listAllSeminaries.do"> <bean:message key="link.seminaries.enrolment"/> </html:link> <a href='<bean:message key="link.seminaries.rules" />' target="_blank"> <bean:message key="label.seminairies.seeRules"/></li>
  <li><html:link page="/examEnrollmentManager.do?method=viewExamsToEnroll" ><bean:message key="link.exams.enrolment"/></html:link></li>
</ul>
<ul>
  <li><html:link page="/viewCurriculum.do?method=getStudentCP" ><bean:message key="link.student.curriculum"/></html:link></li>
</ul>