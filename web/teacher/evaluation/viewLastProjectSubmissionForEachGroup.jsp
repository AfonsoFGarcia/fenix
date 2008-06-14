<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<html:messages id="message" message="true">
	<span class="error"><!-- Error messages go here --> <bean:write name="message" /> </span>
</html:messages>

<em><bean:message key="message.evaluationElements" /></em>
<h2><bean:message key="label.teacher.executionCourseManagement.evaluation.project.viewLastProjectSubmissionForEachGroup.title" /></h2>

<fr:view name="project" schema="evaluation.project.view-with-name-description-and-grouping">
	<fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2 thlight thright"/>
        <fr:property name="rowClasses" value="bold,,,,,,"/>
    </fr:layout>
</fr:view>


<bean:define id="executionCourseID" name="executionCourseID" />
<logic:empty name="projectSubmissions">
	<p>
		<span class="warning0"><!-- Error messages go here -->
			<bean:message key="label.teacher.executionCourseManagement.evaluation.project.viewLastProjectSubmissionForEachGroup.noProjectSubmissions"/>
		</span>
	</p>
</logic:empty>
<logic:notEmpty name="projectSubmissions">
	<bean:define id="projectID" value="<%= request.getParameter("projectID") %>"/>

	<p class="mtop1 mbottom05">
		<bean:message key="label.teacher.executionCourseManagement.evaluation.project.downloadProjectsInZipFormat"/>:
		<html:link page="<%="/projectSubmissionsManagement.do?method=downloadProjectsInZipFormat&amp;projectID=" + projectID %>">
			<bean:message key="link.common.download"/>
		</html:link>
	</p>
	<p class="mtop05">
		<bean:message key="label.teacher.executionCourseManagement.evaluation.project.partsDownload"/>:
		<html:link page="<%= "/projectSubmissionsManagement.do?method=prepareSelectiveDownload&executionCourseID=" + executionCourseID + "&projectID=" + projectID %>">
			<bean:message key="link.common.download"/>
		</html:link>
	</p>
	
	<fr:view name="projectSubmissions" schema="projectSubmission.view-full">
		<fr:layout name="tabular">
	        <fr:property name="classes" value="tstyle2"/>
	        <fr:property name="columnClasses" value="acenter,acenter,nowrap,nowrap acenter,smalltxt,aright,nowrap"/>
	        <fr:property name="linkFormat(viewProjectSubmissionsForGroup)" value="<%="/projectSubmissionsManagement.do?method=viewProjectSubmissionsByGroup&studentGroupID=${studentGroup.idInternal}&projectID=${project.idInternal}&executionCourseID=" + executionCourseID %>"/>
			<fr:property name="key(viewProjectSubmissionsForGroup)" value="link.teacher.executionCourseManagement.evaluation.project.viewLastProjectSubmissionForEachGroup.viewProjectSubmissionsByGroup"/>
			<fr:property name="linkFormat(groupComment)" value="<%= "/projectSubmissionsManagement.do?method=prepareGroupComment&studentGroupID=${studentGroup.idInternal}&projectID=${project.idInternal}&executionCourseID=" + executionCourseID  + "&edit=false" %>"/>
			<fr:property name="key(groupComment)" value="link.teacher.executionCourseManagement.evaluation.project.viewLastProjectSubmissionForEachGroup.groupComment"/>
			<fr:property name="visibleIfNot(groupComment)" value="project.submissionPeriodOpen"/>
	    </fr:layout>
	</fr:view>
</logic:notEmpty>

