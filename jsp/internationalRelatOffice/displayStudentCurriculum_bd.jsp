<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page language="java" %>
<%@page import="java.math.BigDecimal"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionYear"%>
<%@page import="net.sourceforge.fenixedu.domain.student.curriculum.Curriculum"%>
<%@page import="org.apache.struts.util.LabelValueBean"%>
<html:xhtml/>

<em><bean:message key="label.interRelaOffice" bundle="INTERNATIONAL_RELATION_OFFICE"/></em>
<h2><bean:message key="message.student.curriculum" bundle="STUDENT_RESOURCES" /></h2>

<bean:define id="registration" name="registration" type="net.sourceforge.fenixedu.domain.student.Registration"/>

<logic:present role="INTERNATIONAL_RELATION_OFFICE">
	<ul class="mtop2 printhidden">
		<li>
		<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registration" paramProperty="idInternal">
			<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
		</li>
	</ul>
</logic:present>

<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>

<%-- Foto --%>
<div style="float: right;" class="printhidden">
	<bean:define id="personID" name="registration" property="student.person.idInternal"/>
	<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<%-- Person and Student short info --%>
<p class="mvert2">
	<span class="showpersonid">
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
	<h3 class="separator2 mbottom1"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<fr:view name="registration" schema="student.registrationsWithStartData" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thright thlight mtop0"/>
			<fr:property name="rowClasses" value=",,,,,,"/>
		</fr:layout>
	</fr:view>
</logic:notPresent>
<logic:present name="registration" property="ingressionEnum">
	<h3 class="separator2 mbottom1"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<fr:view name="registration" schema="student.registrationDetail" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thright thlight mtop0"/>
			<fr:property name="rowClasses" value=",,,,,,"/>
		</fr:layout>
	</fr:view>
</logic:present>

<%-- Registration Average and Curricular Year calculations --%>

<logic:notPresent role="INTERNATIONAL_RELATION_OFFICE">

	<logic:equal name="registration" property="concluded" value="true">
		<h3 class="separator2"><bean:message key="final.degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
			<p class="mvert05"><bean:message key="final.degree.average.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
			<p>
				<span class="highlight1">
					<bean:message key="final.degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/>:
					<b><bean:write name="registration" property="average"/></b>
				</span>
			</p>
	</logic:equal>

	<logic:equal name="registration" property="concluded" value="false">

		<logic:equal name="registration" property="degree.bolonhaDegree" value="false">

			<p class="mtop1 mbottom1">
				<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
				<bean:define id="url">
					<%="/registration.do?method=viewRegistrationCurriculum&amp;registrationID=" + registration.getIdInternal()%>
					<logic:present name="degreeCurricularPlanID">
						<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID"/>
						<%="&amp;degreeCurricularPlanID=" + degreeCurricularPlanID%>
					</logic:present>
				</bean:define>
				<html:link target="_blank" page="<%=url%>">
					<bean:message key="link.registration.viewCurriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/>
				</html:link>
			</p>
<%--
			<script language="JavaScript">	
			function check(e,v){
				if (e.className == "dnone")
			  	{
				  e.className = "dblock";
				  v.value = "-";
				}
				else {
				  e.className = "dnone";
			  	  v.value = "+";
				}
			}
			</script>
	
			<div class="mtop1 mbottom15">
		
				<p class="mbottom05">
					<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
					<a href="#" class="dnone" id="instructionsButton" onclick="javascript:check(document.getElementById('instructions'), document.getElementById('instructionsButton')); return false;">Consultar M�dia</a>
				</p>
			
				<div id="instructions" class="dblock mvert0">
			
					<div class="infoop3">
					
							<%
								// average
								Curriculum curriculum = registration.getCurriculum();

								final BigDecimal sumPiCi = curriculum.getSumPiCi();
								request.setAttribute("sumPiCi", sumPiCi);
							
								final BigDecimal sumPi = curriculum.getSumPi();
								request.setAttribute("sumPi", sumPi);
				
								final BigDecimal average = curriculum.getAverage();
								request.setAttribute("average", average);
				
								// curricular year
								final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
								curriculum = registration.getCurriculum(currentExecutionYear);
								
								final BigDecimal sumEctsCredits = curriculum.getSumEctsCredits();
								request.setAttribute("sumEctsCredits", sumEctsCredits);

								final Integer curricularYear = curriculum.getCurricularYear();
								request.setAttribute("curricularYear", curricularYear);
							%>

							<p class="mvert05"><strong><bean:message key="legal.value.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
							<p class="mvert05"><bean:message key="rules.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
							<p class="mvert05"><bean:message key="bolonha.special.case.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>

							<p class="mtop1 mbottom05"><strong><bean:message key="degree.average.is.current.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
							
							<ul>
								<li><bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <b class="highlight1"><bean:write name="average"/></b></li>
								<li><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="average.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
								<li><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="degree.average.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:write name="sumPiCi"/> / <bean:write name="sumPi"/> = <b class="highlight1"><bean:write name="average"/></b></li>
							</ul>
							
							<p class="mtop1 mbottom05"><strong><bean:message key="curricular.year.in.begin.of.execution.year.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
							<ul>
								<li><bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <b class="highlight1"><bean:write name="curricularYear"/></b></li>
								<li><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
								<li><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:message key="minimum" bundle="ACADEMIC_OFFICE_RESOURCES"/> (<bean:message key="int" bundle="ACADEMIC_OFFICE_RESOURCES"/> ( (<bean:write name="sumEctsCredits"/> + 24) / 60 + 1) ; <bean:write name="registration" property="degreeType.years"/>) = <b class="highlight1"><bean:write name="curricularYear"/></b>;</li>
							</ul>
					</div>
	
				</div>
	
			</div>
	
			<script>
				check(document.getElementById('instructions'), document.getElementById('instructionsButton'));
				document.getElementById('instructionsButton').className="dinline";
			</script>
--%>
		</logic:equal>

	</logic:equal>

</logic:notPresent>


<%-- Choose Student Curricular Plan form --%>
<html:form action="<%="/viewCurriculum.do?method=prepare&registrationOID=" + registration.getIdInternal()%>">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID"/>
	<logic:present property="studentNumber" name="studentCurricularPlanAndEnrollmentsSelectionForm">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" name="studentCurricularPlanAndEnrollmentsSelectionForm" property="studentNumber"/>
	</logic:present>
	
	<h3 class="separator2 mbottom1 mtop2 printhidden"><bean:message key="label.visualize" bundle="STUDENT_RESOURCES" /></h3>
	<table class="tstyle5 thright thlight mtop025">
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
			<th><bean:message key="label.view" bundle="STUDENT_RESOURCES" />:</th>
			<td>
				<e:labelValues id="viewTypes" enumeration="net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer$ViewType" bundle="ENUMERATION_RESOURCES" />
				<html:select property="viewType" altKey="select.viewType" bundle="HTMLALT_RESOURCES" onchange="this.form.submit();">
					<html:options collection="viewTypes" labelProperty="label" property="value"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.enrollmentsFilter.basic" bundle="STUDENT_RESOURCES" /></th>
			<td>
				<e:labelValues id="enrolmentStateTypes" enumeration="net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer$EnrolmentStateFilterType" bundle="ENUMERATION_RESOURCES" />
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.enrolmentStateType" property="select" onchange='this.form.submit();' >
					<html:options collection="enrolmentStateTypes" property="value" labelProperty="label"/>
				</html:select>
				<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</tr>
		<tr>
			<th><bean:message key="organize.by" bundle="STUDENT_RESOURCES" />:</th>
			<td>
				<e:labelValues id="organizationTypes" enumeration="net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer$OrganizationType" bundle="ENUMERATION_RESOURCES" />
				<logic:iterate id="organizationType" name="organizationTypes">
					<bean:define id="label" name="organizationType" property="label" />
					<bean:define id="value" name="organizationType" property="value" />
					<html:radio property="organizedBy" altKey="radio.organizedBy" bundle="HTMLALT_RESOURCES" onclick="this.form.submit();" value="<%=value.toString()%>"/><bean:write name="label"/>
				</logic:iterate>
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.detailed" bundle="STUDENT_RESOURCES" />:</th>
			<td>
				<html:radio property="detailed" altKey="radio.detailed" bundle="HTMLALT_RESOURCES" onclick="this.form.submit();" value="<%=Boolean.TRUE.toString()%>"/><bean:message  key="label.yes" bundle="STUDENT_RESOURCES"/>
				<html:radio property="detailed" altKey="radio.detailed" bundle="HTMLALT_RESOURCES" onclick="this.form.submit();" value="<%=Boolean.FALSE.toString()%>"/><bean:message  key="label.no" bundle="STUDENT_RESOURCES"/>
			</td>
		</tr>
	</table>


<%-- Show Student Curricular Plans --%>
<logic:empty name="selectedStudentCurricularPlans">
	<p>
		<span class="warning0">
			<bean:message key="message.no.curricularplans" bundle="STUDENT_RESOURCES"/>
		</span>
	</p>
</logic:empty>

<logic:notEmpty name="selectedStudentCurricularPlans">
		<bean:define id="organizedBy" name="studentCurricularPlanAndEnrollmentsSelectionForm" property="organizedBy" type="java.lang.String" />
		<bean:define id="enrolmentStateFilterType" name="studentCurricularPlanAndEnrollmentsSelectionForm" property="select" type="java.lang.String" />
		<bean:define id="detailed" name="studentCurricularPlanAndEnrollmentsSelectionForm" property="detailed" type="java.lang.Boolean" />
		<bean:define id="viewType" name="studentCurricularPlanAndEnrollmentsSelectionForm" property="viewType" type="java.lang.String" />
			
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

			<fr:edit name="studentCurricularPlan" nested="true">
			<fr:layout>
					<fr:property name="organizedBy" value="<%=organizedBy.toString()%>" />
					<fr:property name="enrolmentStateFilter" value="<%=enrolmentStateFilterType.toString()%>" />
					<fr:property name="viewType" value="<%=viewType.toString()%>" />
					<fr:property name="detailed" value="<%=detailed.toString()%>" />
			</fr:layout>
		</fr:edit>

	</logic:iterate>

<div class="print_fsize08">
	<p class="mtop2 mbottom0"><strong><bean:message key="label.legend" bundle="STUDENT_RESOURCES"/></strong></p>
	<div style="width: 250px; float: left;">
	    <e:labelValues id="enrolmentEvaluationTypes" enumeration="net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType" />
		<logic:iterate id="enrolmentEvaluationType" name="enrolmentEvaluationTypes" type="LabelValueBean">
			<p class="mvert05"><em><bean:message key="<%="EnrolmentEvaluationType." + enrolmentEvaluationType.getValue() + ".acronym"%>" bundle="ENUMERATION_RESOURCES"/>: <bean:message key="<%="EnrolmentEvaluationType." + enrolmentEvaluationType.getValue()%>" bundle="ENUMERATION_RESOURCES"/></em></p>
		</logic:iterate>
	</div>
</div>

<div class="cboth"></div>

<div class="mtop15">
	<bean:define id="graph" type="java.lang.String"><%= request.getContextPath() %>/student/viewCurriculumGraph.do?method=createAreaXYChart&registrationOID=<%= registration.getIdInternal() %></bean:define>
	<html:img align="middle" src="<%= graph %>"/>
</div>

</logic:notEmpty>
</html:form>
