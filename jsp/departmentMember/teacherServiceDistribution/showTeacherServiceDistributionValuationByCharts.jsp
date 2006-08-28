<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.CourseValuation" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ProfessorshipValuation" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<style>
table.vtsbc {
margin-bottom: 1em;
border: 2px solid #aaa;
text-align: center;
border-collapse: collapse;
}
table.vtsbc th {
padding: 0.2em 0.2em;
border: 1px solid #bbb;
border-bottom: 1px solid #aaa;
background-color: #cacaca;
font-weight: bold;
}
table.vtsbc td {
background-color: #eaeaea;
border: none;
border: 1px solid #ccc;
padding: 0.25em 0.5em;
}
table.vtsbc td.courses {
background-color: #f4f4f8;
width: 300px;
padding: 0.25em 0.25em;
text-align: left;
}
table.vtsbc td.green {
background-color: #ccddcc;
}
table.vtsbc td.red {
background-color: #ffddcc;
}
table.vtsbc td.yellow {
background-color: #ffffdd;
}
.center {
text-align: center;
}

.right td {
text-align: right;
}

.left td {
text-align: left;
}

.backwhite {
text-align: left;
background-color: #fff;
}
.backwhite a {
/*color: #888;*/
}
.backwhite ul {
margin: 0.3em 0;
}
.backwhite ul li {
padding: 0.2em 0.5em;
color: #458;
}
.backwhite ul li a {
/*text-decoration: none;*/
/*border-bottom: 1px solid #ddd;*/
}
table.search {
background-color: #f5f5f5;
border-collapse: collapse;
}
table.search tr td {
border: 1px solid #fff;
padding: 0.3em;
}
.leftcolumn {
text-align: right;
}
</style>

<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>
	<bean:define id="teacherServiceDistributionId" name="teacherServiceDistribution" property="idInternal"/>
	<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + teacherServiceDistributionId %>'>
		<bean:write name="teacherServiceDistribution" property="name"/>&nbsp;
		<bean:write name="teacherServiceDistribution" property="executionYear.year"/>
	</html:link>
	>
	<bean:message key="link.teacherServiceDistribution.teacherServiceDistributionValuation"/>
</h3>

<html:form action="/teacherServiceDistributionValuation">
<html:hidden property="method" value=""/>
<html:hidden property="teacherServiceDistribution"/>
<html:hidden property="viewType"/>

<table class='search'>
	<tr class='left'>
		<td>
			<b><bean:message key="label.teacherServiceDistribution.valuationPhase"/>:</b>
		</td>
		<td>
			<html:select property="valuationPhase" onchange="this.form.method.value='loadValuationPhase'; this.form.submit();">
				<html:options collection="valuationPhaseList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
		<td>
			<b><bean:message key="label.teacherServiceDistribution.semester"/>:</b>
			&nbsp;
			<html:select property="executionPeriod" onchange="this.form.method.value='loadTeacherServiceDistribution'; this.form.submit();">
				<html:option value="-1"><bean:message key="label.teacherServiceDistribution.both"/></html:option>
				<html:options collection="executionPeriodList" property="idInternal" labelProperty="semester"/>
			</html:select>
		</td>
	</tr>
</table>
<br/>

<logic:present name="nonValuatedCompetenceCourses">
	<b>&bull;</b>&nbsp;
	<html:link href="#noValuationCourses">
		<bean:message key="label.teacherServiceDistribution.coursesWithoutValuations"/>
	</html:link>
	<br/>
</logic:present>
<logic:notEmpty name="courseValuationDTOEntryList">
	<b>&bull;</b>&nbsp;
	<html:link href="javascript:document.teacherServiceDistributionValuationForm.method.value='exportTeacherServiceDistributionValuationToExcel'; document.teacherServiceDistributionValuationForm.submit();">
		<bean:message key="label.teacherService.exportToExcel"/>
	</html:link>
</logic:notEmpty>
<br/>
<br/>
<br/>

<html:link href="javascript:document.teacherServiceDistributionValuationForm.method.value='changeToViewCourses'; document.teacherServiceDistributionValuationForm.submit();">
	<bean:message key="label.teacherService.navigateByCourse"/>
</html:link> | 
<html:link href="javascript:document.teacherServiceDistributionValuationForm.method.value='changeToViewTeachers'; document.teacherServiceDistributionValuationForm.submit();">
	<bean:message key="label.teacherService.navigateByTeacher"/>
</html:link> | 
<html:link href="javascript:document.teacherServiceDistributionValuationForm.method.value='changeToViewTeacherAndCourses'; document.teacherServiceDistributionValuationForm.submit();">
	<bean:message key="label.teacherService.viewByCoursesAndTeachers"/>
</html:link> |
<b> <bean:message key="label.teacherServiceDistribution.viewByCharts"/> </b>
<br/>
<br/>

<bean:define id="valuationPhaseId" name="teacherServiceDistributionValuationForm" property="valuationPhase"/>
<bean:define id="executionPeriodId" name="teacherServiceDistributionValuationForm" property="executionPeriod"/>
<html:img page='<%= "/teacherServiceDistributionValuation.do?method=generateValuatedHoursPerGrouping&valuationPhase=" + valuationPhaseId + "&executionPeriod=" + executionPeriodId %>'/>
<br/>
<br/>
<html:img page='<%= "/teacherServiceDistributionValuation.do?method=generateValuatedNumberStudentsPerGrouping&valuationPhase=" + valuationPhaseId + "&executionPeriod=" + executionPeriodId %>'/>
<br/>
<br/>

<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + teacherServiceDistributionId %>'>
	<bean:message key="link.back"/>
</html:link>

</html:form>
