<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.welcome"/></h2>
<ul>
	<li>
		<html:link page="/curricularCoursesEnrollment.do?method=prepareEnrollmentChooseStudent"><bean:message key="link.student.enrollment"/></html:link>
	</li>
	<li>
		<html:link page="/courseEnrolmentWithoutRulesManagerDA.do?method=prepareEnrollmentChooseStudentAndExecutionYear&amp;degreeType=1"><bean:message key="link.student.enrollment.without.rules"/></html:link>
	</li>

	<li>
		<html:link page="/optionalCoursesEnrolmentManagerDA.do?method=chooseStudentAndExecutionYear&amp;degreeType=1"><bean:message key="title.student.optional.enrollment"/></html:link>
	</li>

</ul>