<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:xhtml/>

<script type="text/javascript" language="JavaScript">
<!--
	function disableAllElementsInEnrollment(form, unenrolledElement, enrolledElement){
		var elements = form.elements;
		var i = 0;
		for (i = 0; i < elements.length ; i++){
			var element = elements[i];
 			if (element.name && ((element.name == unenrolledElement && !element.checked) || (element.name == enrolledElement))){
				element.disabled = true;
			}
		}
	}
	function disableAllElementsInUnenrollment(form, unenrolledElement, enrolledElement){
		var elements = form.elements;
		var i = 0;
		for (i = 0; i < elements.length ; i++){
			var element = elements[i];
 			if (element.name && ((element.name == unenrolledElement) || (element.name == enrolledElement && element.checked))){
				element.readonly = true;
			}
		}
	}
	
	function disableAllElements(form){
		var elements = form.elements;
		var i = 0;
		for (i = 0; i < elements.length ; i++){
			var element = elements[i];
 			if(element.type == 'button'){
 				element.disabled = true;
 			}
		}
	}
// -->
</script>


<h2><bean:message key="title.student.enrollment.simple" bundle="STUDENT_RESOURCES"/></h2>
<span class="error0"><html:errors/></span>
<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>
<html:form action="/curricularCoursesEnrollment">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="enrollmentConfirmation" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" property="studentNumber" />
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" />
	
	<bean:define id="studentCurricularPlanId" name="studentCurricularPlan" property="idInternal" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentCurricularPlanId" property="studentCurricularPlanId" value="<%=studentCurricularPlanId.toString()%>"/>
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.courseType" property="courseType"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourse" property="curricularCourse"/>
	
	<logic:present name="executionDegreeId">
		<bean:define id="executionDegreeId" name="executionDegreeId" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeId" property="executionDegreeId" value="<%=executionDegreeId.toString() %>"/>
	</logic:present>
	
	<div class="infoselected">
	<p><b><bean:message bundle="APPLICATION_RESOURCES" key="label.student"/>:</b> <bean:write name="studentCurricularPlan" property="student.person.nome" /> / 
	<bean:message key="label.student.number"/> <bean:write name="studentCurricularPlan" property="student.number" /></p>
	<p><b><bean:message key="label.student.enrollment.executionPeriod"/></b>: <bean:write name="executionPeriod" property="name" /> <bean:write name="executionPeriod" property="executionYear.year" /></p>
	</div>
	
	<table>
		<tr>
			<td>
				<logic:equal name="studentCurricularPlan" property="student.payedTuition" value="false">
					<logic:equal name="studentCurricularPlan" property="student.interruptedStudies" value="false">
						<span class="error"><bean:message key="message.student.noPayed.tuition" bundle="DEGREE_ADM_OFFICE"/></span>
					</logic:equal>
				</logic:equal>
			</td>
		</tr>
		<tr>
			<td>
				<logic:equal name="studentCurricularPlan" property="student.flunked" value="true">
					<span class="error"><bean:message key="message.student.flunked" bundle="DEGREE_ADM_OFFICE"/></span>
				</logic:equal>
			</td>
		</tr>
		<tr>
			<td>
				<logic:equal name="studentCurricularPlan" property="student.requestedChangeDegree" value="true">
					<span class="error"><bean:message key="message.student.change.degree" bundle="DEGREE_ADM_OFFICE"/></span>
				</logic:equal>
			</td>
		</tr>
	</table>

	<br/>

	<table class="style1">	
		<tr>
			<th class="listClasses-header"><bean:message key="label.student.enrollment.specializationArea" />/<bean:message key="label.student.enrollment.branch" bundle="STUDENT_RESOURCES" /></th>
			<th class="listClasses-header"><bean:message key="label.student.enrollment.secondaryArea" /></th>
			<th class="listClasses-header">&nbsp;</th>
		</tr>
		<logic:present name="studentCurricularPlan" property="branch">
			<bean:define id="name" name="studentCurricularPlan" property="student.person.nome"/>
			<bean:define id="number" name="studentCurricularPlan" property="student.number"/>
			<bean:define id="executionPeriodName" name="executionPeriod" property="name"/>
			<bean:define id="executionYear" name="executionPeriod" property="executionYear.year"/>	
			<tr>
				<td class="listClasses">
					<bean:write name="studentCurricularPlan" property="branch.name" />
				</td>
				<bean:define id="specialization" name="studentCurricularPlan" property="branch.idInternal"/>
				<logic:present name="studentCurricularPlan" property="secundaryBranch">
				<td class="listClasses">
					<bean:write name="studentCurricularPlan" property="secundaryBranch.name" />
				</td>
					<bean:define id="secondary" name="studentCurricularPlan" property="secundaryBranch.idInternal"/>
					<td class="listClasses">
					<logic:present name="executionDegreeId">
						<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;specializationArea=" + specialization +"&amp;secondaryArea=" + secondary + "&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriodName + "&amp;executionYear=" + executionYear + "&amp;executionDegreeId=" + pageContext.findAttribute("executionDegreeId") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") %>">
							<bean:message key="button.student.modify"/>
						</html:link>
					</logic:present>
					<logic:notPresent name="executionDegreeId">
						<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;specializationArea=" + specialization +"&amp;secondaryArea=" + secondary + "&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriodName + "&amp;executionYear=" + executionYear + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") %>">
							<bean:message key="button.student.modify"/>
						</html:link>
					</logic:notPresent>
				</td>
				</logic:present>
				<logic:notPresent name="studentCurricularPlan" property="secundaryBranch">
					<td class="listClasses">&nbsp;</td>
					<td class="listClasses">
						<logic:present name="executionDegreeId">
							<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;specializationArea=" + specialization +"&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriodName + "&amp;executionYear=" + executionYear + "&amp;executionDegreeId=" + pageContext.findAttribute("executionDegreeId") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") %>">
								<bean:message key="button.student.modify"/>
							</html:link>
						</logic:present>
						<logic:notPresent name="executionDegreeId">
							<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;specializationArea=" + specialization +"&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriodName + "&amp;executionYear=" + executionYear + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") %>">
								<bean:message key="button.student.modify"/>
							</html:link>
						</logic:notPresent>
					</td>
				</logic:notPresent>
			</tr>
		</logic:present>
		
		<logic:notPresent name="studentCurricularPlan" property="branch">
			<tr>
				<td class="listClasses">
					<bean:message key="label.student.enrollment.no.area" />
				</td>
				<td class="listClasses">
					<bean:message key="label.student.enrollment.no.area" />
				</td>
				<td class="listClasses">
				
					<bean:define id="name" name="studentCurricularPlan" property="student.person.nome"/>
					<bean:define id="number" name="studentCurricularPlan" property="student.number"/>
					<bean:define id="executionPeriodName" name="executionPeriod" property="name"/>
					<bean:define id="executionYear" name="executionPeriod" property="executionYear.year"/>
					
					<logic:present name="executionDegreeId">
						<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriodName + "&amp;executionYear=" + executionYear + "&amp;executionDegreeId=" + pageContext.findAttribute("executionDegreeId") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID")%>">
							<bean:message key="button.student.modify"/>
						</html:link>
					</logic:present>
					<logic:notPresent name="executionDegreeId">
						<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriodName + "&amp;executionYear=" + executionYear + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID")%>">
							<bean:message key="button.student.modify"/>
						</html:link>
					</logic:notPresent>
				</td>
			</tr>
		</logic:notPresent>
	</table>

<ul>
	<li><a href="@enrollment.faq.url@" target="_blank">FAQ Inscri��o em Disciplinas - Gloss�rio</a></li>
	<li>Para mais esclarecimentos consultar <em>"II.7. Normas a observar na Inscri��o"</em> no documento <a href="http://www.ist.utl.pt/html/destaques/regulamento0506.pdf">Licenciaturas: Regulamentos e Calend�rio Escolar 2005/2006 (.pdf)</a></li>
</ul>


	<h4><bean:message key="message.student.enrolled.curricularCourses" />:</h4>
	<table class="style1">
		<tr class="header">
		<th class="listClasses-header"><bean:message key="label.courses" bundle="STUDENT_RESOURCES"/></th>
		<th class="listClasses-header"><bean:message key="label.degree.name" bundle="STUDENT_RESOURCES"/></th>		
		<th class="listClasses-header"><bean:message key="label.course.type" bundle="STUDENT_RESOURCES"/></th>		
		<th class="listClasses-header"><bean:message key="label.course.enrollment.ectsCredits" bundle="STUDENT_RESOURCES"/></th>
		<th class="listClasses-header"><bean:message key="label.course.enrollment.state" bundle="STUDENT_RESOURCES"/></th>
		<th class="listClasses-header"><bean:message key="label.course.enrollment.cancel" bundle="STUDENT_RESOURCES"/></th>		
		</tr>
		<logic:iterate id="enrollmentElem" name="studentCurrentSemesterEnrollments" type="net.sourceforge.fenixedu.domain.Enrolment">
			<tr>
				<td class="listClasses courses"><bean:write name="enrollmentElem" property="curricularCourse.name"/></td>
				
				<td class="listClasses courses"><bean:write name="enrollmentElem" property="curricularCourse.degreeCurricularPlan.degree.sigla"/></td>
				
	            <td class="listClasses"><bean:message bundle="ENUMERATION_RESOURCES" name="enrollmentElem" property="enrolmentTypeName"/></td>
				
				<td class="listClasses"><bean:write name="enrollmentElem" property="ectsCredits"/></td>
				<td class="listClasses">
					<bean:message name="enrollmentElem" property="condition.name" bundle="ENUMERATION_RESOURCES"/>
				</td>
				<td class="listClasses">
					<bean:define id="enrollmentIndex" name="enrollmentElem" property="idInternal"/>
					<bean:define id="onClickString">this.form.method.value='unenrollFromCurricularCourse'; this.form.curricularCourse.value='<bean:write name="enrollmentIndex" />'; disableAllElements(this.form); this.form.submit();</bean:define>
					<html:button alt="<%=enrollmentIndex.toString()%>" property="<%=enrollmentIndex.toString()%>"   onclick="<%= onClickString %>">
						<bean:message key="button.anull" bundle="STUDENT_RESOURCES"/>
					</html:button>
				</td>
			</tr>
		</logic:iterate>
	</table>
	

	<h4><bean:message key="message.student.unenrolled.curricularCourses" />:</h4>
	<table class="style1">
		<logic:present name="warnings">
			<tr>
				<td colspan="5">
					<ul>
						<logic:iterate id="messageKey" name="warnings">
							<li><span style="color: red"><b><bean:message key='<%=messageKey.toString()%>' bundle="STUDENT_RESOURCES"/></b></span></li>
						</logic:iterate>
					</ul>
				</td>
			</tr>
		</logic:present>	
		<tr class="header">
		<th class="listClasses-header"><bean:message key="label.course" bundle="STUDENT_RESOURCES"/></th>
		<th class="listClasses-header"><bean:message key="label.degree.name" bundle="STUDENT_RESOURCES"/></th>				
		<th class="listClasses-header"><bean:message key="label.course.type" bundle="STUDENT_RESOURCES"/></th>	
		<th class="listClasses-header"><bean:message key="label.course.enrollment.ectsCredits" bundle="STUDENT_RESOURCES"/></th>
		<th class="listClasses-header"><bean:message key="label.course.enrollment.curricularYear" bundle="STUDENT_RESOURCES"/></th>
		<th class="listClasses-header"><bean:message key="label.course.enrollment.state" bundle="STUDENT_RESOURCES"/></th>
		<th class="listClasses-header"><bean:message key="label.course.enrollment.enroll" bundle="STUDENT_RESOURCES"/></th>		
		</tr>
		<logic:iterate id="curricularCourse2Enrol" name="curricularCourses2Enroll" type="net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll">
			<bean:define id="curricularCourse2EnrolIndex" name="curricularCourse2Enrol" property="curricularCourse.idInternal"/>
			<tr>
				<td class="listClasses courses"><bean:write name="curricularCourse2Enrol" property="curricularCourse.name"/></td>
				<td class="listClasses courses"><bean:write name="curricularCourse2Enrol" property="curricularCourse.degreeCurricularPlan.degree.sigla"/></td>
				<td class="listClasses">
					<% if (curricularCourse2Enrol.isOptionalCurricularCourse()) {%>
						<bean:message bundle="APPLICATION_RESOURCES" key="option.curricularCourse.optional" />
						
					<% } else { %>
						<bean:message bundle="APPLICATION_RESOURCES" name="curricularCourse2Enrol" property="curricularCourse.type.keyName" />
						
					<% } %>
				</td>
				<td class="listClasses"><bean:write name="curricularCourse2Enrol" property="ectsCredits"/></td>
				<td class="listClasses"><bean:write name="curricularCourse2Enrol" property="curricularYear.year"/></td>
				<td class="listClasses"><bean:message name="curricularCourse2Enrol" property="enrollmentType.name" bundle="ENUMERATION_RESOURCES"/></td>
				<td class="listClasses">
					<bean:define id="curricularCourse2EnrolString">
						<bean:write name="curricularCourse2EnrolIndex"/>-<bean:write name="curricularCourse2Enrol" property="enrollmentType"/>
					</bean:define>
					<bean:define id="onClickString" >
						<% if (curricularCourse2Enrol.isOptionalCurricularCourse()) {%>
							this.form.method.value='enrollInCurricularCourse';  this.form.curricularCourse.value='<bean:write name="curricularCourse2EnrolString" />'; this.form.courseType.value='2'; disableAllElements(this.form); this.form.submit();
							
						<% } else { %>
							this.form.method.value='enrollInCurricularCourse';  this.form.curricularCourse.value='<bean:write name="curricularCourse2EnrolString" />'; this.form.courseType.value='1'; disableAllElements(this.form); this.form.submit();
							
						<% } %>
					</bean:define>
					<html:button bundle="HTMLALT_RESOURCES" altKey="button.curricularCourse2EnrolString" property="curricularCourse2EnrolString" onclick="<%=onClickString%>">
						<bean:message key="button.enroll" bundle="STUDENT_RESOURCES"/>
					</html:button>
				</td>
			</tr>
		</logic:iterate>
	</table>
	
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.finish"/>
	</html:submit>
</html:form>