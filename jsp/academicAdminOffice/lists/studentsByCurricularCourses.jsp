<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@page import="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.ExecutionDegreeListBean"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionYear"%>
<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>


<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>
<bean:define id="executionBean" name="executionDegreeBean"/>
<bean:define id="semester" name="semester"/>
<bean:define id="year" name="year"/>


<h2>
		<bean:write name="executionBean" property="curricularCourse.name"/>
			&nbsp;- &nbsp;
		<bean:write name="executionBean" property="curricularCourse.degreeCurricularPlan.name"/>
	</h2>

	<p class="mtop15 mbottom1"><em class="highlight5">
	<bean:write name="executionBean" property="executionYear.year" /> - <bean:message key="label.period" arg0="<%=year.toString()%>" arg1="<%=semester.toString()%>"  bundle="ACADEMIC_OFFICE_RESOURCES"/></em></p>
	
<logic:present name="enrolmentList">
<bean:size id="enrolmentListSize" name="enrolmentList" />
<p class="mtop2">
 <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.studentCurricularPlan.lists.total"/> <%= enrolmentListSize %>
</p>
	<fr:view schema="enrolmentStudentsList.view" name="enrolmentList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thright thlight thcenter"/>
		</fr:layout>	
		
	</fr:view>
</logic:present>
