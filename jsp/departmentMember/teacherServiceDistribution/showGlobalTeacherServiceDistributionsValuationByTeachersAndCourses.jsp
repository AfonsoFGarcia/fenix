<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.formbeans.teacherServiceDistribution.GlobalTeacherServiceDistributionValuationForm" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.ValuationTeacherDTOEntry" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.CourseValuationDTOEntry" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.ProfessorshipValuationDTOEntry" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<jsp:useBean id="globalTeacherServiceDistributionValuationForm" scope="request" class="net.sourceforge.fenixedu.presentationTier.formbeans.teacherServiceDistribution.GlobalTeacherServiceDistributionValuationForm" />

<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>
	<bean:message key="link.teacherServiceDistribution.teacherServiceDistributionVisualization"/>
</h3>

<html:form action="/globalTeacherServiceDistributionValuation">
<html:hidden property="method" value=""/>
<html:hidden property="viewType"/>

<logic:iterate name="globalTeacherServiceDistributionValuationForm" property="selectedTeacherServiceDistributions" id="teacherServiceDistribution">
	<bean:define name="teacherServiceDistribution" id="teacherServiceDistribution" type="String"/>
	<% 
		String teacherServiceDistributionProperty = "teacherServiceDistribution(" + (String) teacherServiceDistribution + ")";
		String valuationPhaseProperty = "valuationPhase(" + (String) teacherServiceDistribution + ")";
		String valuationGroupingProperty = "valuationGrouping(" + (String) teacherServiceDistribution + ")"; 
	%>
	
	<html:hidden property="<%= teacherServiceDistributionProperty %>"/>
	<html:hidden property="<%= valuationPhaseProperty %>"/>
	<html:hidden property="<%= valuationGroupingProperty %>"/>		
</logic:iterate>

<table class='search'>
	<tr class='tdleft'>
		<td>
			<b><bean:message key="label.teacherServiceDistribution.semester"/>:</b>
		</td>
		<td>
 			<html:select property="executionPeriod" onchange="this.form.method.value='viewGlobalTeacherServiceDistributionValuation'; this.form.submit();">
				<html:option value="-1"><bean:message key="label.teacherServiceDistribution.both"/></html:option>
				<html:options collection="executionPeriodList" property="idInternal" labelProperty="semester"/>
			</html:select>
		</td>
	</tr>
</table>
<br/>

<html:link href="javascript:document.globalTeacherServiceDistributionValuationForm.method.value='changeToViewTeachers'; document.globalTeacherServiceDistributionValuationForm.submit();">
	<bean:message key="label.teacherService.navigateByTeacher"/>
</html:link> | 
<html:link href="javascript:document.globalTeacherServiceDistributionValuationForm.method.value='changeToViewCourses'; document.globalTeacherServiceDistributionValuationForm.submit();">
	<bean:message key="label.teacherService.navigateByCourse"/>
</html:link> | 
<b>	<bean:message key="label.teacherService.viewByCoursesAndTeachers"/> </b>
<br/>

<table class='vtsbc'>
	<tr>
		<th>
		</th>
<logic:iterate name="courseValuationDTOEntryList" id="courseValuationDTOEntry">
		<th>
			<bean:write name="courseValuationDTOEntry" property="courseValuation.acronym"/>
		</th>
</logic:iterate>
	</tr>
	
<logic:iterate name="valuationTeacherDTOEntryList" id="valuationTeacherDTOEntry">
<bean:define id="valuationTeacherDTOEntry" name="valuationTeacherDTOEntry" type="net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.ValuationTeacherDTOEntry"/>
	<tr >
		<th>
			<bean:write name="valuationTeacherDTOEntry" property="acronym"/>
		</th>
		<logic:iterate name="courseValuationDTOEntryList" id="courseValuationDTOEntry">
		<bean:define id="courseValuationDTOEntry" name="courseValuationDTOEntry" type="net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.CourseValuationDTOEntry"/>
		<td class="aright">
			<%
				ProfessorshipValuationDTOEntry professorshipValuationDTOEntry = ((ValuationTeacherDTOEntry) valuationTeacherDTOEntry).getProfeshipValuationDTOEntryByCourseValuationDTOEntry((CourseValuationDTOEntry) courseValuationDTOEntry);
				
				if(professorshipValuationDTOEntry != null) {
			%>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= professorshipValuationDTOEntry.getProfessorshipValuation().getTotalHours() %>
			</fmt:formatNumber>
			<%
				}
			%>
		</td>
		</logic:iterate>
		<th>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="valuationTeacherDTOEntry" property="totalHoursLectured"/>
			</fmt:formatNumber>
		</th>				
	</tr>
</logic:iterate>

	<tr>
		<th>
		</th>
	<logic:iterate name="courseValuationDTOEntryList" id="courseValuationDTOEntry">
		<th>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="courseValuationDTOEntry" property="courseValuation.totalHoursLectured"/>
			</fmt:formatNumber>
		</th>
	</logic:iterate>
		<th>
		</th>
	</tr>

</table>


<br/>

<html:link page="/globalTeacherServiceDistributionValuation.do?method=prepareForGlobalTeacherServiceDistributionValuation">
	<bean:message key="link.back"/>
</html:link>	
</html:form>