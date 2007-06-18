<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

	<bean:define id="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan" name="degreeCurricularPlan"/>
	<bean:define id="equivalencePlan" type="net.sourceforge.fenixedu.domain.EquivalencePlan" name="degreeCurricularPlan" property="equivalencePlan"/>

	<div style="background: #fafafa; border: 2px solid #eee; padding: 1em; color: #555;">
		<logic:equal name="numElements" value="0">
			<bean:message key="message.curricular.course.has.no.equivalencies"/>
		</logic:equal>
		<logic:iterate id="entry" type="net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry" indexId="n" name="entries">
			<logic:notEqual name="n" value="0">
				<br/>
				<br/>
			</logic:notEqual>
			<logic:iterate id="curricularCourseFromList1" indexId="i1" name="entry" property="oldCurricularCourses">
				<span style="border-bottom: 1px dotted #aaa;">
					<logic:notEqual name="i1" value="0">
						<strong>
							<bean:message key="AND" bundle="ENUMERATION_RESOURCES"/>
						</strong>
					</logic:notEqual>
					<bean:write name="curricularCourseFromList1" property="name"/>
				</span>
			</logic:iterate>
			==>
			<logic:iterate id="curricularCourseFromList2" indexId="i2" name="entry" property="newCurricularCourses">
				<span style="border-bottom: 1px dotted #aaa;">
					<logic:notEqual name="i2" value="0">
						<strong>
							<bean:message name="entry" property="newCurricularCoursesOperator.name" bundle="ENUMERATION_RESOURCES"/>
						</strong>
					</logic:notEqual>
					<bean:write name="curricularCourseFromList2" property="name"/>
				</span>
			</logic:iterate>
			<html:link page="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=deleteEquivalency&amp;degreeCurricularPlanID="
					+ degreeCurricularPlan.getIdInternal() + "&amp;equivalencePlanID="
					+ equivalencePlan.getIdInternal() + "&amp;curricularCourseEquivalencePlanEntryID="
					+ entry.getIdInternal() %>">
				<bean:message key="link.delete"/>
			</html:link>
		</logic:iterate>
	</div>
