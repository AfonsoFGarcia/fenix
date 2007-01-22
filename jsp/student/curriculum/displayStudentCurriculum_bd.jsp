<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@page import="net.sourceforge.fenixedu.domain.student.StudentCurriculum"%>
<html:xhtml/>

<h2><bean:message key="message.student.curriculum" bundle="STUDENT_RESOURCES" /></h2>

<bean:define id="registration" name="registration" type="net.sourceforge.fenixedu.domain.student.Registration"/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
	<ul class="mtop2">
		<li>
		<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registration" paramProperty="idInternal">
			<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
		</li>
	</ul>
</logic:present>

<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>

<%-- Foto --%>
<div style="float: right;">
	<bean:define id="personID" name="registration" property="student.person.idInternal"/>
	<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<%-- Person and Student short info --%>
<p class="mvert2">
	<span style="background-color: #ecf3e1; border-bottom: 1px solid #ccdeb2; padding: 0.4em 0.6em;">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>


<%-- Registration Details --%>
<logic:notPresent name="registration" property="ingressionEnum">
	<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<fr:view name="registration" schema="student.registrationsWithStartData" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
			<fr:property name="rowClasses" value=",tdhl1,,,"/>
		</fr:layout>
	</fr:view>
</logic:notPresent>
<logic:present name="registration" property="ingressionEnum">
	<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<fr:view name="registration" schema="student.registrationDetail" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
			<fr:property name="rowClasses" value=",tdhl1,,,"/>
		</fr:layout>
	</fr:view>
</logic:present>

<%-- Registration Average and Curricular Year calculations --%>
<logic:notPresent role="ACADEMIC_ADMINISTRATIVE_OFFICE">
	<logic:equal name="registration" property="degreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree" value="true">
		<%
			final StudentCurriculum studentCurriculum = new StudentCurriculum(registration);
			request.setAttribute("studentCurriculum", studentCurriculum);
		
			final double totalEctsCredits = studentCurriculum.getTotalEctsCredits(null);
			request.setAttribute("totalEctsCredits", totalEctsCredits);
			
			final double average = studentCurriculum.getRoundedAverage(null, true);
			request.setAttribute("average", average);
		
			final int curricularYear = studentCurriculum.calculateCurricularYear(null);
			request.setAttribute("curricularYear", curricularYear);
		
			final double sumPiCi = studentCurriculum.getSumPiCi(null);
			request.setAttribute("sumPiCi", sumPiCi);
		
			final double sumPi = studentCurriculum.getSumPi(null);
			request.setAttribute("sumPi", sumPi);
		%>
	
		<p class="mtop2">
			<span class="warning0">
				<bean:message key="rules.info" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</span>
		</p>
		<table class="tstyle4 thright thlight mtop0">
			<tr>
				<th><bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
				<td><bean:write name="average"/></td>
			</tr>	
			<tr>
				<th><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
				<td><bean:message key="average.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></td>
			</tr>	
			<tr>
				<th><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
				<td>
					<bean:message key="degree.average.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:write name="sumPiCi"/> / <bean:write name="sumPi"/> = <bean:write name="average"/>
				</td>
			</tr>	
		</table>
		<table class="tstyle4 thright thlight mtop0">
			<tr>
				<th><bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
				<td><bean:write name="curricularYear"/></td>
			</tr>	
			<tr>
				<th><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
				<td>
					<bean:message key="curricular.year.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>
				</td>
			</tr>	
			<tr>
				<th><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
				<td>
					<bean:message key="curricular.year.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:message key="minimum" bundle="ACADEMIC_OFFICE_RESOURCES"/> (<bean:message key="int" bundle="ACADEMIC_OFFICE_RESOURCES"/> ( (<bean:write name="totalEctsCredits"/> + 24) / 60 + 1) ; <bean:write name="registration" property="degreeType.years"/>) = <bean:write name="curricularYear"/>;
				</td>
			</tr>	
		</table>
	</logic:equal>
</logic:notPresent>


<%-- Choose Student Curricular Plan form --%>
<html:form action="<%="/viewCurriculum.do?method=prepare&registrationOID=" + registration.getIdInternal()%>">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID"/>
	<logic:present property="studentNumber" name="studentCurricularPlanAndEnrollmentsSelectionForm">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" name="studentCurricularPlanAndEnrollmentsSelectionForm" property="studentNumber"/>
	</logic:present>
	
	<p class="mbottom025"><strong><bean:message key="label.visualize" bundle="STUDENT_RESOURCES" />:</strong></p>
	<table class="tstyle4 thright thlight mtop025">
		<tr>
			<th><bean:message key="label.studentCurricularPlan.basic" bundle="STUDENT_RESOURCES" /></th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" property="studentCPID" onchange='this.form.submit();'> 
					<html:options collection="scpsLabelValueBeanList" property="value" labelProperty="label" />
				</html:select>
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.enrollmentsFilter.basic" bundle="STUDENT_RESOURCES" /></th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" property="select" onchange='this.form.submit();' >
					<html:options collection="enrollmentOptions" property="value" labelProperty="label"/>
				</html:select>
				<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</tr>
		<tr>
			<th><bean:message key="organize.by" bundle="STUDENT_RESOURCES" />:</th>
			<td>
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.organizedBy" property="organizedBy" value="groups" onclick='this.form.submit();'/><bean:message key="groups" bundle="BOLONHA_MANAGER_RESOURCES"/>
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.organizedBy" property="organizedBy" value="executionYears" onclick='this.form.submit();'/><bean:message key="label.execution.year" bundle="STUDENT_RESOURCES"/>
			</td>
		</tr>
	</table>
</html:form>


<%-- Show Student Curricular Plans --%>
<logic:empty name="selectedStudentCurricularPlans">
	<p>
		<span class="warning0">
			<bean:message key="message.no.curricularplans" bundle="STUDENT_RESOURCES"/>
		</span>
	</p>
</logic:empty>

<logic:notEmpty name="selectedStudentCurricularPlans">
	<bean:define id="organizedBy" name="organizedBy" scope="request" type="java.lang.String" />
	<bean:define id="enrolmentStateSelectionType" name="enrolmentStateSelectionType" scope="request" type="java.lang.Integer" />	
	<logic:iterate id="studentCurricularPlan" name="selectedStudentCurricularPlans" indexId="index">
		
		<logic:greaterThan name="index" value="0">
			<div class="mvert3"></div>
		</logic:greaterThan>

		<bean:define id="dateFormated">
			<dt:format pattern="dd.MM.yyyy">
				<bean:write name="studentCurricularPlan" property="startDate.time"/>
			</dt:format>
		</bean:define>

		<div class="mvert2 mtop0">
			<p class="mvert05">
				<strong>
					<bean:message key="label.curricularplan" bundle="STUDENT_RESOURCES" />: 
				</strong> 
				<bean:message bundle="ENUMERATION_RESOURCES" name="studentCurricularPlan" property="degreeType.name"/>
				<bean:message bundle="APPLICATION_RESOURCES" key="label.in"/> 
				<bean:write name="studentCurricularPlan" property="degree.name"/>,
				<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.name"/>
				<logic:present name="studentCurricularPlan" property="specialization">
					- <bean:message name="studentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
				</logic:present>
			</p>
			<logic:present name="studentCurricularPlan" property="branch">
				<p class="mvert05">
					<strong>
						Grupo: 
					</strong> 
					<bean:write name="studentCurricularPlan" property="branch.name"/>
				</p>
			</logic:present>
			<p class="mvert05">
				<strong>
					<bean:message key="label.beginDate" bundle="STUDENT_RESOURCES" />: 
				</strong> 
				<bean:write name="dateFormated"/>
			</p>
		</div>



<%-- inline styles to remove --%>
<style type="text/css">
.bgcolor01 { background: #efe; }
.bgcolor02 { background: #eff; }
.bgcolor03 { background: #ffe; }
.bgcolor04 { background: #eef; }
.bgcolor05 { background: #fee; }
.bgcolor06 { background: #fde; }
.bgcolor07 { background: #efd; }
.bgcolor08 { background: #dfe; }
.bgcolor09 { background: #edf; }
.bgcolor10 { background: #def; }
.bgcolor11 { background: #fed; }
.bgcolor12 { background: #fcd; }

.bgcolor01 { background: #fdfdfa; }
.bgcolor02 { background: #fdfdfa; }
.bgcolor03 { background: #fdfdfa; }
.bgcolor04 { background: #fdfdfa; }
.bgcolor05 { background: #fdfdfa; }
.bgcolor06 { background: #fdfdfa; }
.bgcolor07 { background: #fdfdfa; }
.bgcolor08 { background: #fdfdfa; }
.bgcolor09 { background: #fdfdfa; }
.bgcolor10 { background: #f9f9f5; }
.bgcolor11 { background: #fdfdfa; }
.bgcolor12 { background: #fdfdfa; }
</style>




		<fr:view name="studentCurricularPlan">
			<fr:layout>
				<fr:property name="organizedBy" value="<%=organizedBy%>"/>
				<fr:property name="initialWidth" value="800px"/>
				<fr:property name="widthDecreasePerLevel" value="10"/>
				<fr:property name="tablesClasses" value="showinfo3 mvert0"/>
				<%-- tableClasses--%>
				<fr:property name="groupRowClasses" value="bgcolor2"/>
				<%-- groupHeaderRowClasses--%>
				<fr:property name="groupNameClasses" value="aleft"/>
				<%-- groupHeaderClasses--%>
				<fr:property name="enrolmentClasses" value="
					width2em acenter bgcolor01,			<!-- Inscri��o Confirmada etc -->
					width6em acenter bgcolor02,			<!-- Data da Inscri��o -->
					width1p5em acenter bgcolor03,		<!-- �poca Normal -->
					aleft bgcolor04,					<!-- C�digo e Disciplina -->
					width7em acenter bgcolor05,			<!-- Curso  -->
					grupos bgcolor06,					<!--   -->
					grupos bgcolor07,					<!--   -->
					width7em acenter bgcolor08,			<!-- Opcional  -->
					width8em acenter bgcolor09,			<!-- Reprovado N�o Avaliado  -->
					width2em aright bgcolor10,			<!-- Nota  -->
					color888 width2em aright bgcolor11,			<!-- Peso  -->
					color888 width2em aright bgcolor12			<!-- ECTS  -->
				"/>
				<%-- enrolmentColumnClasses--%>
				<fr:property name="enrolmentStateSelectionType" value="<%=enrolmentStateSelectionType.toString()%>"/>
			</fr:layout>
		</fr:view>

	</logic:iterate>


<p class="mtop3 mbottom0"><strong><bean:message key="label.legend" bundle="STUDENT_RESOURCES"/></strong></p>
<div style="width: 250px; float: left;">
	<p class="mvert05"><em><bean:message key="EnrollmentCondition.TEMPORARY.acronym" bundle="ENUMERATION_RESOURCES"/>: <bean:message key="EnrollmentCondition.TEMPORARY" bundle="ENUMERATION_RESOURCES"/></em></p>
	<p class="mvert05"><em><bean:message key="EnrollmentCondition.FINAL.acronym" bundle="ENUMERATION_RESOURCES"/>: <bean:message key="EnrollmentCondition.FINAL" bundle="ENUMERATION_RESOURCES"/></em></p>
	<p class="mvert05"><em><bean:message key="EnrollmentCondition.IMPOSSIBLE.acronym" bundle="ENUMERATION_RESOURCES"/>: <bean:message key="EnrollmentCondition.IMPOSSIBLE" bundle="ENUMERATION_RESOURCES"/></em></p>
	<p class="mvert05"><em><bean:message key="EnrollmentCondition.VALIDATED.acronym" bundle="ENUMERATION_RESOURCES"/>: <bean:message key="EnrollmentCondition.VALIDATED" bundle="ENUMERATION_RESOURCES"/></em></p>
	<p class="mvert05"><em><bean:message key="EnrollmentCondition.INVISIBLE.acronym" bundle="ENUMERATION_RESOURCES"/>: <bean:message key="EnrollmentCondition.INVISIBLE" bundle="ENUMERATION_RESOURCES"/></em></p>
</div>
<div style="width: 250px; float: left;>
	<p class="mvert05"><em><bean:message key="EnrolmentEvaluationType.NORMAL.acronym" bundle="ENUMERATION_RESOURCES"/>: <bean:message key="EnrolmentEvaluationType.NORMAL" bundle="ENUMERATION_RESOURCES"/></em></p>
	<p class="mvert05"><em><bean:message key="EnrolmentEvaluationType.IMPROVEMENT.acronym" bundle="ENUMERATION_RESOURCES"/>: <bean:message key="EnrolmentEvaluationType.IMPROVEMENT" bundle="ENUMERATION_RESOURCES"/></em></p>
	<p class="mvert05"><em><bean:message key="EnrolmentEvaluationType.SPECIAL_SEASON.acronym" bundle="ENUMERATION_RESOURCES"/>: <bean:message key="EnrolmentEvaluationType.SPECIAL_SEASON" bundle="ENUMERATION_RESOURCES"/></em></p>
	<p class="mvert05"><em><bean:message key="EnrolmentEvaluationType.EQUIVALENCE.acronym" bundle="ENUMERATION_RESOURCES"/>: <bean:message key="EnrolmentEvaluationType.EQUIVALENCE" bundle="ENUMERATION_RESOURCES"/></em></p>
</div>
<div class="cboth"></div>

</logic:notEmpty>
