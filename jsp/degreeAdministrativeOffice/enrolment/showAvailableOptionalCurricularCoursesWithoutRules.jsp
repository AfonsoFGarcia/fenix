<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants, Util.CurricularCourseType" %>

<script type="text/javascript" language="JavaScript">
	function disableAllElements(form, elementName){
		var elements = form.elements;
		var i = 0;
		for (i = 0; i < elements.length ; i++){
			var element = elements[i];
			if (element.name == elementName && !element.checked){
				element.disabled = true;
			}
		}
	}
</script>

<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>"/>
<bean:define id="actualEnrolment" name="infoEnrolmentContext" property="actualEnrolment"/>
<bean:size id="sizeAutomaticalyEnroled" name="infoEnrolmentContext" property="infoCurricularCoursesScopesAutomaticalyEnroled"/>
<bean:size id="sizeToBeEnroled" name="infoEnrolmentContext" property="infoFinalCurricularCoursesScopesSpanToBeEnrolled"/>

<logic:notEqual name="sizeAutomaticalyEnroled" value="0">
	<b><bean:message key="label.mandatory.enrolment.curricular.courses" bundle="STUDENT_RESOURCES"/></b><br/>
	<logic:iterate id="curricularCourseScope" name="infoEnrolmentContext" property="infoCurricularCoursesScopesAutomaticalyEnroled">
		<bean:write name="curricularCourseScope" property="infoCurricularCourse.name"/><br/>
	</logic:iterate>
</logic:notEqual>

<logic:notEqual name="sizeToBeEnroled" value="0">
	<html:form action="/optionalCurricularCourseEnrolmentWithoutRulesManager.do">
		<html:hidden property="method" value="verifyEnrolment"/>
		<html:hidden property="optionalCourseIndex" value=""/>
		<b><bean:message key="label.enrolment.curricular.courses" bundle="STUDENT_RESOURCES"/></b>
		<br/>
		<br/>
		<table border="0" cellpadding="2" cellspacing="0">
			<tr>
				<td>&nbsp;</td>
				<td><u><bean:message key="label.curricular.course.name" bundle="STUDENT_RESOURCES"/></u></td>
				<td align="center"><u><bean:message key="label.curricular.course.year" bundle="STUDENT_RESOURCES"/></u></td>
			</tr>
			<logic:iterate id="curricularScope" name="infoEnrolmentContext" property="infoFinalCurricularCoursesScopesSpanToBeEnrolled" indexId="index">
				<bean:define id="optionalEnrolmentString" value=""></bean:define>
				<logic:equal name="curricularScope" property="infoCurricularCourse.type" value="<%= CurricularCourseType.OPTIONAL_COURSE_OBJ.toString() %>">
					<bean:define id="onclick">
						if (this.checked == true) {this.form.method.value='startEnrolmentInOptional'; document.forms[0].optionalCourseIndex.value='<bean:write name="index"/>'; disableAllElements(this.form,this.name);this.form.submit();}	
					</bean:define>
					<bean:define id="optionalCourse" name="curricularScope" property="infoCurricularCourse"/>
					<logic:iterate id="optionalEnrolment" name="infoEnrolmentContext" property="infoOptionalCurricularCoursesEnrolments">
						<logic:equal name="optionalEnrolment" property="infoCurricularCourseScope.infoCurricularCourse" value="<%= pageContext.findAttribute("optionalCourse").toString() %>">
							<bean:define id="optionalEnrolmentString">
								-&nbsp;<bean:write name="optionalEnrolment" property="infoCurricularCourseForOption.name"/>
							</bean:define>
						</logic:equal> 
					</logic:iterate>
				</logic:equal>
				<logic:notEqual name="curricularScope" property="infoCurricularCourse.type" value="<%= CurricularCourseType.OPTIONAL_COURSE_OBJ.toString() %>">
					<bean:define id="onclick" value=""></bean:define>
				</logic:notEqual>
				<tr>
					<td>
						<html:multibox property='<%= "curricularCourses[" + index +"]" %>' onclick="<%= pageContext.findAttribute("onclick").toString() %>">
							<bean:write name="index"/>
						</html:multibox>
					</td>
					<td>
						<bean:write name="curricularScope" property="infoCurricularCourse.name"/>
						<logic:present name="optionalEnrolmentString">
							<bean:write name="optionalEnrolmentString" filter="false"/>
						</logic:present>
					</td>
					<td align="center">
						<bean:write name="curricularScope" property="infoCurricularSemester.infoCurricularYear.year"/>
					</td>
				</tr>
			</logic:iterate>
		</table>
		<br/>
		<br/>
		<html:submit styleClass="inputbutton"><bean:message key="button.continue.enrolment" bundle="STUDENT_RESOURCES"/></html:submit>
		<html:cancel styleClass="inputbutton"><bean:message key="button.cancel"/></html:cancel>		
	</html:form>
</logic:notEqual>