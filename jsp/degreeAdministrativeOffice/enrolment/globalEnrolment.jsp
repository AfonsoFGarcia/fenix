<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="tilte.welcome"/></h2>
<ul>
<%--	<li>
		<html:link page="/functionRedirect.do?method=chooseStudentAndDegreeTypeForEnrolmentWithRules"><bean:message key="link.student.enrolment.with.rules"/></html:link>
	</li>--%>
	<li>
		<html:link page="/curricularCoursesEnrollment.do?method=prepareEnrollmentChooseStudent"><bean:message key="link.student.LEEC.enrollment"/></html:link>
	</li>
	<li>
		<html:link page="/courseEnrolmentWithoutRulesManagerDA.do?method=prepareEnrollmentChooseStudentAndExecutionYear&amp;degreeType=1"><bean:message key="link.student.LEEC.enrollment.without.rules"/></html:link>
	</li>
<%--
	<li>
		<html:link page="/optionalCoursesEnrolmentManager.do?method=chooseStudentAndExecutionYear&amp;degreeType=1"><bean:message key="title.student.LEEC.optional.enrollment"/></html:link>
	</li>
--%>
</ul>
<%--
<%	IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
	if (userView.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) { %>
		<b><bean:message key="title.student.enrolment.without.rules"/>:</b>
		<ul>
			<li>
				<html:link page="/functionRedirect.do?method=chooseStudentAndDegreeTypeForEnrolmentWithoutRules"><bean:message key="link.student.enrolment.without.rules"/></html:link>
			</li>
			<li>
				<html:link page="/functionRedirect.do?method=chooseStudentAndDegreeTypeForEnrolmentInOptionalWithoutRules"><bean:message key="link.student.enrolment.in.optional.curricular.course.without.rules"/></html:link>
			</li>
		</ul>
<%	}%>
--%>