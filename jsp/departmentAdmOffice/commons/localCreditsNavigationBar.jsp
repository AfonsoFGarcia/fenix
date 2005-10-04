<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:parameter id="executionPeriodId" name="executionPeriodId" value="-1"/>
<logic:equal name="executionPeriodId" value="-1">
	<logic:present name="infoExecutionPeriod">
		<bean:define id="executionPeriodId">
			<bean:write name="infoExecutionPeriod" property="idInternal"/>
		</bean:define>
	</logic:present>
	<logic:notPresent name="infoExecutionPeriod">
		<h3>Missing semester! (localCreditsNavigationBar.jsp)</h3>
	</logic:notPresent>
</logic:equal>

<logic:present role="DEPARTMENT_ADMINISTRATIVE_OFFICE">
	<logic:present role="DEPARTMENT_CREDITS_MANAGER">
		<strong>&raquo; <html:link action="/creditsManagementIndex"><bean:message key="link.group.creditsManagement"/></html:link></strong>
		<br />
		<br />
		
		<ul>
			<li>
				<html:link page="/teacherSearchForShiftManagement.do?method=searchForm&amp;page=0" paramId="executionPeriodId" paramName="executionPeriodId">
					<bean:message key="link.lessons"/>
				</html:link>
			</li>
			<li>
				<html:link page="/teacherSearchForSupportLessonsManagement.do?method=searchForm&amp;page=0" paramId="executionPeriodId" paramName="executionPeriodId">			
					<bean:message key="link.support.lessons"/>
				</html:link>					
			</li>						
			<li>
				<html:link page="/teacherSearchForDFPStudentManagement.do?method=searchForm&amp;page=0" paramId="executionPeriodId" paramName="executionPeriodId">			
					<bean:message key="link.degree.final.project.students"/>
				</html:link>					
			</li>
			<li>
				<html:link page="/teacherSearchForTeacherInstitutionWorkingTimeManagement.do?method=searchForm&amp;page=0" paramId="executionPeriodId" paramName="executionPeriodId">
					<bean:message key="link.teacher-working-time-management"/>
				</html:link>
			</li>
			<li>
				<html:link page="/teacherSearchForOtherTypeCreditLine.do?method=searchForm&amp;page=0" paramId="executionPeriodId" paramName="executionPeriodId">
					<bean:message key="link.other-type-credit-line" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				</html:link>
			</li>
			<li>
				<html:link page="/teacherSearchForTeacherCreditsSheet.do?method=searchForm&amp;page=0" paramId="executionPeriodId" paramName="executionPeriodId">
					<bean:message key="link.teacher.sheet"/>
				</html:link>
			</li>
			<li>
				<html:link page="/prepareListDepartmentTeachers.do" paramId="executionPeriodId" paramName="executionPeriodId">
					<bean:message key="link.list-department-teachers"/>
				</html:link>
			</li>
		</ul>
		<br/>
	</logic:present>
</logic:present>