<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<jsp:include page="/i18n.jsp"/>

<h1 class="mtop0 mbottom03 cnone">
	<bean:write name="executionCourse" property="nome"/>
	<span class="greytxt" style="font-size: 11px;">
		(<bean:write name="executionCourse" property="executionPeriod.semester" />
		<bean:message bundle="PUBLIC_DEGREE_INFORMATION" locale="pt_PT" key="public.degree.information.label.ordinal.semester.abbr" />
		<bean:write name="executionCourse" property="executionPeriod.executionYear.year" />)
	</span>
</h1>

<!--
<p class="greytxt" style="">
	<bean:write name="executionCourse" property="executionPeriod.semester" />
	<bean:message bundle="PUBLIC_DEGREE_INFORMATION" locale="pt_PT" key="public.degree.information.label.ordinal.semester.abbr" />
	<bean:write name="executionCourse" property="executionPeriod.executionYear.year" />
</p>
-->

<p style="margin-top: 0;">
<span>
<logic:iterate id="curricularCourse" name="executionCourse" property="curricularCoursesSortedByDegreeAndCurricularCourseName" indexId="i">
	<logic:notEqual name="i" value="0">,</logic:notEqual>
	<logic:equal name="curricularCourse" property="bolonhaDegree" value="true">
		<bean:define id="url" type="java.lang.String">/degreeSite/viewCurricularCourse.faces?curricularCourseID=<bean:write name="curricularCourse" property="idInternal"/>&amp;executionYearID=<bean:write name="executionCourse" property="executionPeriod.executionYear.idInternal"/>&amp;organizeBy=groups&amp;showRules=false&amp;hideCourses=false&amp;degreeCurricularPlanID=<bean:write name="curricularCourse" property="degreeCurricularPlan.idInternal"/>&amp;degreeID=<bean:write name="curricularCourse" property="degreeCurricularPlan.degree.idInternal"/></bean:define>
		<html:link page="<%= url %>">
			<bean:write name="curricularCourse" property="degreeCurricularPlan.degree.sigla"/>
		</html:link>
	</logic:equal>
	<logic:notEqual name="curricularCourse" property="bolonhaDegree" value="true">
		<bean:define id="url" type="java.lang.String">/showCourseSite.do?method=showCurricularCourseSite&amp;curricularCourseID=<bean:write name="curricularCourse" property="idInternal"/>&amp;executionPeriodOID=<bean:write name="executionCourse" property="executionPeriod.idInternal"/>&amp;degreeID=<bean:write name="curricularCourse" property="degreeCurricularPlan.degree.idInternal"/></bean:define>
		<html:link action="<%= url %>">
			<bean:write name="curricularCourse" property="degreeCurricularPlan.degree.sigla"/>
		</html:link>
	</logic:notEqual>
</logic:iterate>
</span>
</p>



