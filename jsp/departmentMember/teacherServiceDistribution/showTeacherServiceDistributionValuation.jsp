<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.CourseValuation" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ProfessorshipValuation" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="link.teacherServiceDistribution.teacherServiceDistributionValuation"/></h2>

<p class="breadcumbs">
	<em>
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
	</em>
</p>

<html:form action="/teacherServiceDistributionValuation">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherServiceDistribution" property="teacherServiceDistribution"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewType" property="viewType"/>

<table class='tstyle5'>
	<tr>
		<td>
			<bean:message key="label.teacherServiceDistribution.valuationPhase"/>:
		</td>
		<td>
			<html:select property="valuationPhase" onchange="this.form.method.value='loadValuationPhase'; this.form.submit();">
				<html:options collection="valuationPhaseList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.teacherServiceDistribution.semester"/>:
		</td>
		<td>
			<html:select property="executionPeriod" onchange="this.form.method.value='loadTeacherServiceDistribution'; this.form.submit();">
				<html:option value="-1"><bean:message key="label.teacherServiceDistribution.both"/></html:option>
				<html:options collection="executionPeriodList" property="idInternal" labelProperty="semester"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.teacherServiceDistribution.ValuationGrouping"/>:
		</td>
		<td>
			<html:select property="valuationGrouping" onchange="this.form.method.value='loadTeacherServiceDistribution'; this.form.submit();">
				<html:options collection="valuationGroupingOptionEntryList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<td></td>
		<td>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.viewCurricularInformation" property="viewCurricularInformation"  onclick="this.form.method.value='loadTeacherServiceDistribution'; this.form.submit()"/>
			<bean:message key="label.teacherService.viewCourseInfo"/>
		</td>
	</tr>
	<tr>
		<td></td>
		<td>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.viewStudentsEnrolments" property="viewStudentsEnrolments"  onclick="this.form.method.value='loadTeacherServiceDistribution'; this.form.submit()"/>		
			<bean:message key="label.teacherService.viewStudentsEnrolments"/>
		</td>
	</tr>
	<tr>
		<td></td>
		<td>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.viewShiftHours" property="viewShiftHours"  onclick="this.form.method.value='loadTeacherServiceDistribution'; this.form.submit()"/>				
			<bean:message key="label.teacherService.viewHoursPerShift"/>
		</td>
	</tr>
	<tr>
		<td></td>
		<td>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.viewStudentsEnrolmentsPerShift" property="viewStudentsEnrolmentsPerShift"  onclick="this.form.method.value='loadTeacherServiceDistribution'; this.form.submit()"/>				
			<bean:message key="label.teacherService.viewStudentsPerShift"/>
		</td>
	</tr>
</table>

<ul>
	<li>
		<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + teacherServiceDistributionId %>'>
			<bean:message key="link.back"/>
		</html:link>
	</li>
	<logic:present name="nonValuatedCompetenceCourses">
		<li>
			<html:link href="#noValuationCourses">
				<bean:message key="label.teacherServiceDistribution.coursesWithoutValuations"/>
			</html:link>
		</li>
	</logic:present>
	<logic:notEmpty name="courseValuationDTOEntryList">
		<li>
			<html:link href="javascript:document.forms[0].method.value='exportTeacherServiceDistributionValuationToExcel'; document.forms[0].submit();">
				<bean:message key="label.teacherService.exportToExcel"/>
			</html:link>
		</li>
	</logic:notEmpty>
</ul>

<p class="mtop15 mbottom1">
	<bean:message key="label.teacherService.navigateBy"/>:
	<b> <bean:message key="label.teacherService.navigateByCourse"/> </b> | 
	<html:link href="javascript:document.forms[0].method.value='changeToViewTeachers'; document.forms[0].submit();">
		<bean:message key="label.teacherService.navigateByTeacher"/>
	</html:link> | 
	<html:link href="javascript:document.forms[0].method.value='changeToViewTeacherAndCourses'; document.forms[0].submit();">
		<bean:message key="label.teacherService.viewByCoursesAndTeachers"/>
	</html:link> |
	<html:link href="javascript:document.forms[0].method.value='changeToViewCharts'; document.forms[0].submit();">
		<bean:message key="label.teacherServiceDistribution.viewByCharts"/>
	</html:link>
</p>

<table class='tstyle4'>
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
	<tr class='acenter' id=<%= courseValuationId %>>
		<td class='highlight7'>
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
					<td>
						<bean:write name="curricularCourseInformation" property="key"/>
					</td>
					<td width='5%' class="aright">
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
		<td class="aright">
			<bean:write name="courseValuationDTOEntry" property="courseValuation.firstTimeEnrolledStudents"/>
		</td>
		<td class="aright">
			<bean:write name="courseValuationDTOEntry" property="courseValuation.secondTimeEnrolledStudents"/>
		</td>
</logic:equal>		
		<td class="aright">
			<bean:write name="courseValuationDTOEntry" property="courseValuation.totalEnrolledStudents"/>		
		</td>
<logic:equal name="viewShiftHours" value="true">		
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<bean:write name="courseValuationDTOEntry" property="courseValuation.theoreticalHours"/>
			</fmt:formatNumber>
		</td>
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="courseValuationDTOEntry" property="courseValuation.praticalHours"/></fmt:formatNumber>
		</td>
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="courseValuationDTOEntry" property="courseValuation.theoPratHours"/></fmt:formatNumber>
		</td>
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="courseValuationDTOEntry" property="courseValuation.laboratorialHours"/></fmt:formatNumber>
		</td>
</logic:equal>
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="courseValuationDTOEntry" property="courseValuation.totalHours"/></fmt:formatNumber>
		</td>
		<logic:greaterThan name="courseValuationDTOEntry" property="courseValuation.totalHoursNotLectured" value="0.0"> 
		<td class="aright" class="yellow">
		</logic:greaterThan>
		<logic:lessThan  name="courseValuationDTOEntry" property="courseValuation.totalHoursNotLectured" value="0.0"> 
		<td class="aright" class="red">
		</logic:lessThan>
		<logic:equal name="courseValuationDTOEntry" property="courseValuation.totalHoursNotLectured" value="0.0"> 
		<td class="aright" class="green">
		</logic:equal>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="courseValuationDTOEntry" property="courseValuation.totalHoursNotLectured"/></fmt:formatNumber>
		</td>
<logic:equal name="viewStudentsEnrolmentsPerShift" value="true">
		<td class="aright">
			<bean:write name="courseValuationDTOEntry" property="courseValuation.studentsPerTheoreticalShift"/>
		</td>
		<td class="aright">
			<bean:write name="courseValuationDTOEntry" property="courseValuation.studentsPerPraticalShift"/>
		</td>
		<td class="aright">
			<bean:write name="courseValuationDTOEntry" property="courseValuation.studentsPerTheoPratShift"/>
		</td>
		<td class="aright">
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
</table>

<table class="tstyle4">
<logic:present name="nonValuatedCompetenceCourses">
	<tr id="noValuationCourses">
		<th>
			<b><bean:message key="label.teacherServiceDistribution.coursesWithoutValuations"/></b>
		</th>
	</tr>

	<logic:iterate name="nonValuatedCompetenceCourses" id="valuationCompetenceCourse">
		<tr>
			<td>
				<bean:write name="valuationCompetenceCourse" property="name"/>
			</td>
		</tr>
	</logic:iterate>
</logic:present>
</table>


<logic:empty name="courseValuationDTOEntryList">
	<logic:notPresent name="nonValuatedCompetenceCourses">
		<span class="error">
			<bean:message key="label.teacherServiceDistribution.noCompetenceCoursesForExecutionPeriod"/>
		</span>
		<br/>
	</logic:notPresent>
</logic:empty>

</html:form>