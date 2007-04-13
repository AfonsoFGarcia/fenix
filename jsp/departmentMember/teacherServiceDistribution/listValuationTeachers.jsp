<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationTeacher" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCompetenceCourse" %>
<%@ page import="net.sourceforge.fenixedu.domain.Department" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>
	<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
		<bean:write name="teacherServiceDistribution" property="name"/>&nbsp;
		<bean:write name="teacherServiceDistribution" property="executionYear.year"/>
	</html:link>
	>
	<bean:message key="link.teacherServiceDistribution.manageRootGrouping"/>
</h3>

<br/>

<html:form action="/valuationTeachersGroup">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationGrouping" property="valuationGrouping"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationCourse" property="valuationCourse" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationTeacher" property="valuationTeacher" value=""/>

<ul>
	<li>
		<html:link href="javascript:document.forms[0].method.value='showDepartmentTeachers'; document.forms[0].submit()">
		  	<bean:message key="label.teacherServiceDistribution.addTeacher"/>
		</html:link>
	</li>
	<li>	
		<html:link href="javascript:document.forms[0].method.value='showFormToCreateTeacher'; document.forms[0].submit()">
		  	<bean:message key="label.teacherService.createTeacher"/>
		</html:link>
	</li>
	<li>
		<html:link href="javascript:document.forms[0].method.value='showDepartmentCourses'; document.forms[0].submit()">
		  	<bean:message key="label.teacherServiceDistribution.addCourse"/>
		</html:link>
	</li>
	<li>	
		<html:link href="javascript:document.forms[0].method.value='showFormToCreateCourse'; document.forms[0].submit()">
		  	<bean:message key="label.teacherServiceDistribution.createCourse"/>
		</html:link>
	</li>
</ul>
<br/>
<br/>

<b><bean:message key="label.teacherServiceDistribution.availableTeachersAndCourses"/>:</b>
<br/>

<table style="width: 60em;">
<tr valign="top">
<td width="50%">

<table class='vtsbc' width="100%">
	<tr>
		<th colspan="4">
			<bean:message key="label.teacherServiceDistribution.valuationTeacher"/>
		</th>
	</tr>
<logic:iterate name="valuationTeachersList" id="valuationTeacher">
	<bean:define id="valuationTeacher" name="valuationTeacher" type="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationTeacher"/>
	<tr>
		<td class="courses">
			<bean:write name="valuationTeacher" property="name"/>
		</td>
		<td>
			<bean:write name="valuationTeacher" property="category.code"/>
		</td>
		<td>
			<bean:write name="valuationTeacher" property="department.acronym"/>
		</td>
		<td>
			<html:link href='<%= "javascript:document.forms[0].valuationTeacher.value=" + ((ValuationTeacher) valuationTeacher).getIdInternal() + ";document.forms[0].method.value='removeTeacher'; document.forms[0].submit()" %>'>
				<bean:message key="link.remove"/>
			</html:link>
		</td>			
	</tr>
</logic:iterate>
</table>

<br/>
<br/>

</td>
<td width="50%">
<table class='vtsbc' width="100%">
	<tr>
		<th colspan="3">
			<b><bean:message key="label.teacherServiceDistribution.competenceCourse"/></b>
		</th>
	</tr>
<logic:iterate name="valuationCoursesList" id="valuationCompetenceCourse">
	<bean:define id="valuationCompetenceCourse" name="valuationCompetenceCourse" />			
	<tr>
		<td class="courses">
			<bean:write name="valuationCompetenceCourse" property="name"/>
		</td>
		<td>
			<bean:write name="valuationCompetenceCourse" property="competenceCourseDepartment.acronym"/>
		</td>
		<td>
			<html:link href='<%= "javascript:document.forms[0].valuationCourse.value=" + ((ValuationCompetenceCourse) valuationCompetenceCourse).getIdInternal() + ";document.forms[0].method.value='removeCourse'; document.forms[0].submit()" %>'>
				<bean:message key="link.remove"/>
			</html:link>
		</td>						
	</tr>
</logic:iterate>
</table>

</td>
</tr>
</table>
</html:form>

<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
	<bean:message key="link.back"/>
</html:link>
