<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />
<link href="<%= request.getContextPath() %>/CSS/quc_resume_boards.css" rel="stylesheet" type="text/css" />


<em><bean:message key="title.teacherPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.inquiry.quc.teacher" bundle="INQUIRIES_RESOURCES"/></h2>

<h3><bean:write name="executionCourse" property="name"/> - <bean:write name="executionCourse" property="sigla"/> (<bean:write name="executionSemester" property="semester"/>� Semestre <bean:write name="executionSemester" property="executionYear.year"/>)</h3>

<p><bean:message key="message.teacher.resume.inquiry" bundle="INQUIRIES_RESOURCES"/></p>

<logic:notPresent name="readMode">
	<html:link action="/teachingInquiry.do?method=showTeacherInquiry" paramName="professorship" paramProperty="externalId" paramId="professorshipOID">
		<b><bean:message key="link.inquiry.fillIn" bundle="INQUIRIES_RESOURCES"/> <bean:message key="label.inquiry.teaching" bundle="INQUIRIES_RESOURCES"/></b>
	</html:link>
	<logic:present name="completionState">
		(<bean:write name="completionState"/>)
	</logic:present>
</logic:notPresent>	

<logic:present name="readMode">
	<html:link action="/viewQUCInquiryAnswers.do?method=showTeacherInquiry" paramName="professorship" paramProperty="externalId" paramId="professorshipOID"
		module="/publico" target="_blank">
		<b><bean:message key="link.inquiry.view" bundle="INQUIRIES_RESOURCES"/> <bean:message key="label.inquiry.teaching" bundle="INQUIRIES_RESOURCES"/></b>
	</html:link>
</logic:present>

<logic:notEmpty name="teacherResults">
	<p class="mtop15">
		<strong><bean:message key="title.inquiry.resume.teacher" bundle="INQUIRIES_RESOURCES"/></strong>:
		<bean:write name="professorship" property="person.name"/>
	</p>	
	<fr:view name="teacherResults">
		<fr:layout name="teacher-shiftType-inquiry-resume">
			<fr:property name="classes" value="teacher-resume"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:notEmpty name="coursesResultResume">
	<p class="mtop15"><strong><bean:message key="title.inquiry.resume.course" bundle="INQUIRIES_RESOURCES"/></strong></p>
	<fr:view name="coursesResultResume">
		<fr:layout name="course-degrees-inquiry-resume">
			<fr:property name="classes" value="teacher-resume"/>
			<fr:property name="showMandatoryQuestions" value="false"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<ul class="legend-general" style="margin-top: 20px;"> 
	<li><bean:message key="label.inquiry.legend" bundle="INQUIRIES_RESOURCES"/>:</li> 
	<li><span class="legend-bar-1">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.excelent" bundle="INQUIRIES_RESOURCES"/></li>
	<logic:notPresent name="first-sem-2010">
		<li><span class="legend-bar-7">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.veryGood" bundle="INQUIRIES_RESOURCES"/></li>	
	</logic:notPresent>
	<li><span class="legend-bar-2">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.regular" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-3">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.toImprove" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-4">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.indequate" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-5">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.withoutRepresentation" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><bean:message key="label.inquiry.mandatoryCommentsNumber" bundle="INQUIRIES_RESOURCES"/></li> 
</ul>

<ul class="legend-general" style="margin-top: 0px;"> 
	<li><bean:message key="label.inquiry.workLoad" bundle="INQUIRIES_RESOURCES"/>:</li> 
	<li><span class="legend-bar-2">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.asPredicted" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-3">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.higherThanPredicted" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-6">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.lowerThanPredicted" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-5">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.withoutRepresentation" bundle="INQUIRIES_RESOURCES"/></li> 
</ul> 