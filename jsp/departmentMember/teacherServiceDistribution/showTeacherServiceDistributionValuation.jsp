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
	<tr class='left'>
		<td>
			<b><bean:message key="label.teacherServiceDistribution.ValuationGrouping"/>:</b>
		</td>
		<td colspan="2">
			<html:select property="valuationGrouping" onchange="this.form.method.value='loadTeacherServiceDistribution'; this.form.submit();">
				<html:options collection="valuationGroupingOptionEntryList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<td colspan=4>
			<table>
				<tr>
					<td align=left>
						<html:checkbox property="viewCurricularInformation"  onclick="this.form.method.value='loadTeacherServiceDistribution'; this.form.submit()"/>
						<bean:message key="label.teacherService.viewCourseInfo"/>
					</td>
				</tr>
				<tr>
					<td align=left>
						<html:checkbox property="viewStudentsEnrolments"  onclick="this.form.method.value='loadTeacherServiceDistribution'; this.form.submit()"/>		
						<bean:message key="label.teacherService.viewStudentsEnrolments"/>
					</td>
				</tr>
				<tr>
					<td align=left>
						<html:checkbox property="viewShiftHours"  onclick="this.form.method.value='loadTeacherServiceDistribution'; this.form.submit()"/>				
						<bean:message key="label.teacherService.viewHoursPerShift"/>
					</td>
				</tr>
				<tr>
					<td align=left>
						<html:checkbox property="viewStudentsEnrolmentsPerShift"  onclick="this.form.method.value='loadTeacherServiceDistribution'; this.form.submit()"/>				
						<bean:message key="label.teacherService.viewStudentsPerShift"/>
					</td>
				</tr>
			</table>
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

<b> <bean:message key="label.teacherService.navigateByCourse"/> </b> | 
<html:link href="javascript:document.teacherServiceDistributionValuationForm.method.value='changeToViewTeachers'; document.teacherServiceDistributionValuationForm.submit();">
	<bean:message key="label.teacherService.navigateByTeacher"/>
</html:link> | 
<html:link href="javascript:document.teacherServiceDistributionValuationForm.method.value='changeToViewTeacherAndCourses'; document.teacherServiceDistributionValuationForm.submit();">
	<bean:message key="label.teacherService.viewByCoursesAndTeachers"/>
</html:link> |
<html:link href="javascript:document.teacherServiceDistributionValuationForm.method.value='changeToViewCharts'; document.teacherServiceDistributionValuationForm.submit();">
	<bean:message key="label.teacherServiceDistribution.viewByCharts"/>
</html:link>
<br/>
<br/>

<table class='vtsbc'>
	<logic:notEmpty name="courseValuationDTOEntryList">
	<tr>
		<th>
			<bean:message key="label.teacherService.course.name"/>
		</th>
<logic:equal name="viewCurricularInformation" value="true">
		<th>
			<bean:message key="label.teacherService.course.campus"/>
		</th>
		<th>
			<bean:message key="label.teacherService.course.degrees"/>
		</th>
</logic:equal>
<logic:equal name="viewStudentsEnrolments" value="true">
		<th>
			<bean:message key="label.teacherService.course.firstTimeEnrolledStudentsNumber"/>
		</th>
		<th>
			<bean:message key="label.teacherService.course.secondTimeEnrolledStudentsNumber"/>
		</th>
</logic:equal>		
		<th>
			<bean:message key="label.teacherService.course.totalStudentsNumber"/>
		</th>
<logic:equal name="viewShiftHours" value="true">
		<th>
			<bean:message key="label.teacherService.course.theoreticalHours"/>
		</th>
		<th>
			<bean:message key="label.teacherService.course.praticalHours"/>
		</th>
		<th>
			<bean:message key="label.teacherService.course.theoPratHours"/>
		</th>
		<th>
			<bean:message key="label.teacherService.course.laboratorialHours"/>
		</th>
</logic:equal>		
		<th>
			<bean:message key="label.teacherService.course.totalHours"/>
		</th>
		<th>
			<bean:message key="label.teacherService.course.availability"/>
		</th>
<logic:equal name="viewStudentsEnrolmentsPerShift" value="true">		
		<th>
			<bean:message key="label.teacherService.course.studentsNumberByTheoreticalShift"/>
		</th>
		<th>
			<bean:message key="label.teacherService.course.studentsNumberByPraticalShift"/>
		</th>
		<th>
			<bean:message key="label.teacherService.course.studentsNumberByTheoPraticalShift"/>
		</th>
		<th>
			<bean:message key="label.teacherService.course.studentsNumberByLaboratorialShift"/>
		</th>
</logic:equal>
	</tr>
	</logic:notEmpty>
<logic:iterate name="courseValuationDTOEntryList" id="courseValuationDTOEntry">
	<bean:define id="courseValuation" name="courseValuationDTOEntry" property="courseValuation"/>
	<bean:define id="courseValuationId" name="courseValuationDTOEntry" property="courseValuation.idInternal"/>
	<tr class='center' id=<%= courseValuationId %>>
		<td class='courses'>
			<%
				if(((CourseValuation) courseValuation).getHavePermissionToValuate(SessionUtils.getUserView(request).getPerson())) {
			%>
			<html:link page="<%= "/courseValuation.do?method=prepareLinkForCourseValuation&amp;teacherServiceDistribution=" + 
			teacherServiceDistributionId + "&amp;courseValuation=" + courseValuationId %>">
				<bean:write name="courseValuationDTOEntry" property="courseValuation.name"/>
			 </html:link>			
			<%
				} else {
			%>
				<bean:write name="courseValuationDTOEntry" property="courseValuation.name"/>
			<%
				}
			%>
		</td>	
<logic:equal name="viewCurricularInformation" value="true">
		<td>
			<logic:iterate name="courseValuationDTOEntry" property="courseValuation.campus" id="campusEntry">
				<bean:write name="campusEntry"/>&nbsp;
			</logic:iterate>
		</td>
		<td>
			<table width='100%'>
			<logic:iterate name="courseValuationDTOEntry" property="curricularCoursesInformation" id="curricularCourseInformation">
				<tr>
					<td align='left'>
						<bean:write name="curricularCourseInformation" property="key"/>
					</td>
					<td width='5%' align='right'>
						<logic:iterate name="curricularCourseInformation" property="value" id="curricularYear">
							<bean:write name="curricularYear"/>�&nbsp;
						</logic:iterate>
					</td>
				</tr>				
			</logic:iterate>
			</table>
		</td>
</logic:equal>

<logic:equal name="viewStudentsEnrolments" value="true">
		<td align='right'>
			<bean:write name="courseValuationDTOEntry" property="courseValuation.firstTimeEnrolledStudents"/>
		</td>
		<td align='right'>
			<bean:write name="courseValuationDTOEntry" property="courseValuation.secondTimeEnrolledStudents"/>
		</td>
</logic:equal>		
		<td align='right'>
			<bean:write name="courseValuationDTOEntry" property="courseValuation.totalEnrolledStudents"/>		
		</td>
<logic:equal name="viewShiftHours" value="true">		
		<td align='right'>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<bean:write name="courseValuationDTOEntry" property="courseValuation.theoreticalHours"/>
			</fmt:formatNumber>
		</td>
		<td align='right'>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="courseValuationDTOEntry" property="courseValuation.praticalHours"/></fmt:formatNumber>
		</td>
		<td align='right'>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="courseValuationDTOEntry" property="courseValuation.theoPratHours"/></fmt:formatNumber>
		</td>
		<td align='right'>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="courseValuationDTOEntry" property="courseValuation.laboratorialHours"/></fmt:formatNumber>
		</td>
</logic:equal>
		<td align='right'>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="courseValuationDTOEntry" property="courseValuation.totalHours"/></fmt:formatNumber>
		</td>
		<logic:greaterThan name="courseValuationDTOEntry" property="courseValuation.totalHoursNotLectured" value="0.0"> 
		<td align='right' class="yellow">
		</logic:greaterThan>
		<logic:lessThan  name="courseValuationDTOEntry" property="courseValuation.totalHoursNotLectured" value="0.0"> 
		<td align='right' class="red">
		</logic:lessThan>
		<logic:equal name="courseValuationDTOEntry" property="courseValuation.totalHoursNotLectured" value="0.0"> 
		<td align='right' class="green">
		</logic:equal>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="courseValuationDTOEntry" property="courseValuation.totalHoursNotLectured"/></fmt:formatNumber>
		</td>
<logic:equal name="viewStudentsEnrolmentsPerShift" value="true">
		<td align='right'>
			<bean:write name="courseValuationDTOEntry" property="courseValuation.studentsPerTheoreticalShift"/>
		</td>
		<td align='right'>
			<bean:write name="courseValuationDTOEntry" property="courseValuation.studentsPerPraticalShift"/>
		</td>
		<td align='right'>
			<bean:write name="courseValuationDTOEntry" property="courseValuation.studentsPerTheoPratShift"/>
		</td>
		<td align='right'>
			<bean:write name="courseValuationDTOEntry" property="courseValuation.studentsPerLaboratorialShift"/>
		</td>
</logic:equal>
	</tr>
	<tr>
		<td colspan="20" class='backwhite' style="background-color: #fdfdfd;">
			<logic:iterate name="courseValuationDTOEntry" property="professorshipValuationDTOEntries" id="professorshipValuationDTOEntry">
			<bean:define id="professorshipValuation" name="professorshipValuationDTOEntry" property="professorshipValuation"/>
				<ul>
					<li>
						<bean:define id="valuationTeacherId" name="professorshipValuationDTOEntry" property="professorshipValuation.valuationTeacher.idInternal"/>
						<%
							if(((ProfessorshipValuation) professorshipValuation).getHavePermissionToValuate(SessionUtils.getUserView(request).getPerson())) {
						%>
						<html:link page="<%= "/professorshipValuation.do?method=prepareLinkForProfessorshipValuationByCourse&amp;teacherServiceDistribution=" + 
						teacherServiceDistributionId  + "&amp;valuationTeacher=" + valuationTeacherId + "&amp;courseValuation=" + courseValuationId  %>"> 
							<bean:write name="professorshipValuationDTOEntry" property="professorshipValuation.valuationTeacher.name"/>
						</html:link>
						<%
							} else {
						%>
							<bean:write name="professorshipValuationDTOEntry" property="professorshipValuation.valuationTeacher.name"/>
						<%
							}
						%>
						&nbsp;-&nbsp;
						<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="professorshipValuationDTOEntry" property="professorshipValuation.totalHours"/></fmt:formatNumber>
						<bean:message key="label.teacherService.hours"/>
						<logic:notEqual name="professorshipValuationDTOEntry" property="professorshipValuation.totalHours" value="0.0">
							&nbsp;(
							<% boolean firstSymbol = true; %>
							<logic:notEqual name="professorshipValuationDTOEntry" property="professorshipValuation.theoreticalHours" value="0.0">
								<% if(firstSymbol){ firstSymbol = false; } %>
								<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="professorshipValuationDTOEntry" property="professorshipValuation.theoreticalHours"/></fmt:formatNumber>
								<bean:message key="label.teacherService.theoretical"/>
							</logic:notEqual>
							<logic:notEqual name="professorshipValuationDTOEntry" property="professorshipValuation.praticalHours" value="0.0">
								<% if(firstSymbol){ 
									firstSymbol = false; 
								} else { 
									out.print("+");
								}%>
								<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="professorshipValuationDTOEntry" property="professorshipValuation.praticalHours"/></fmt:formatNumber>
								<bean:message key="label.teacherService.pratical"/>
							</logic:notEqual>
							<logic:notEqual name="professorshipValuationDTOEntry" property="professorshipValuation.theoPratHours" value="0.0">
								<% if(firstSymbol){ 
									firstSymbol = false; 
								} else { 
									out.print("+");
								}%>
								<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="professorshipValuationDTOEntry" property="professorshipValuation.theoPratHours"/></fmt:formatNumber>
								<bean:message key="label.teacherService.theopratical"/>
							</logic:notEqual>
							<logic:notEqual name="professorshipValuationDTOEntry" property="professorshipValuation.laboratorialHours" value="0.0">
								<% if(!firstSymbol) out.print("+");  %>
								<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="professorshipValuationDTOEntry" property="professorshipValuation.laboratorialHours"/></fmt:formatNumber>
								<bean:message key="label.teacherService.laboratorial"/>
							</logic:notEqual>
							)
						</logic:notEqual>
					</li>					
				</ul>
			</logic:iterate>
		</td>	
	</tr>
</logic:iterate>
	
<logic:present name="nonValuatedCompetenceCourses">
	<tr id="noValuationCourses">
		<td colspan="20" align="center" style="color: red;">
			<br/>
			<hr/>
			<b><u><bean:message key="label.teacherServiceDistribution.coursesWithoutValuations"/></u></b>
			<br/>
		</td>
	</tr>

	<logic:iterate name="nonValuatedCompetenceCourses" id="valuationCompetenceCourse">
		<tr>
			<td class="courses" colspan="20">
				<bean:write name="valuationCompetenceCourse" property="name"/>
			</td>
		</tr>
	</logic:iterate>
</logic:present>
	
</table>
<br/>

<logic:empty name="courseValuationDTOEntryList">
	<logic:notPresent name="nonValuatedCompetenceCourses">
		<span class="error">
			<bean:message key="label.teacherServiceDistribution.noCompetenceCoursesForExecutionPeriod"/>
		</span>
		<br/>
	</logic:notPresent>
</logic:empty>

<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + teacherServiceDistributionId %>'>
	<bean:message key="link.back"/>
</html:link>

</html:form>