<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="person" name="professorship" property="person" />

<h2><bean:message bundle="APPLICATION_RESOURCES" key="professorship.permissions.options"/> <bean:write name="person" property="name" /></h2>

<bean:define id="teacherOID" name="professorship" property="externalId"/>
<ul>
<li><html:link page="<%= "/teacherManagerDA.do?method=viewTeachersByProfessorship&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
<bean:message key="button.back" bundle="APPLICATION_RESOURCES"/>
</html:link></li>
<li><html:link page="<%= "/teachersManagerDA.do?method=removeTeacher&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;teacherOID=" + teacherOID %>">
		<bean:message key="link.removeTeacher"/>
</html:link></li>
</ul>
<h3><bean:message bundle="APPLICATION_RESOURCES" key="professorship.permissions"/>:</h3> 
<fr:form action="<%= "/teacherManagerDA.do?method=viewTeachersByProfessorship&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
<fr:edit id="permissions" name="professorship" property="permissions">
	<fr:schema type="net.sourceforge.fenixedu.domain.ProfessorshipPermissions" bundle="APPLICATION_RESOURCES">
		<fr:slot name="personalization" key="link.personalizationOptions"/>
		<fr:slot name="siteArchive"  key="link.executionCourse.archive.generate"/>
		<fr:slot name="announcements" key="link.announcements"/>
		<fr:slot name="sections" key="link.teacher.executionCourseManagement.foruns"/>
		<fr:slot name="summaries" key="link.summaries"/>
		<fr:slot name="students" key="link.students"/>
		<fr:slot name="planning" key="link.lessonPlannings"/>
		<fr:slot name="evaluationSpecific" key="link.adHocEvaluations"/>
		<fr:slot name="evaluationWorksheets" key="link.onlineTests"/>
		<fr:slot name="evaluationProject" key="link.projects"/>
		<fr:slot name="evaluationTests" key="link.writtenTests"/>
		<fr:slot name="evaluationExams" key="link.exams"/>
		<fr:slot name="evaluationFinal" key="link.finalEvaluation"/>
		<fr:slot name="worksheets" key="link.testsManagement"/>
		<fr:slot name="groups" key="link.groupsManagement"/>
		<fr:slot name="shift" key="label.shifts"/>

		<fr:slot name="evaluationMethod" key="link.evaluationMethod"/>
		<fr:slot name="bibliografy" key="link.bibliography"/>
	</fr:schema>
   	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thleft tdleft thlight"/>
		<fr:property name="columnClasses" value=",,tdclear"/>
	</fr:layout>
</fr:edit>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Submeter" styleClass="inputbutton" property="ok"/> 
</fr:form>