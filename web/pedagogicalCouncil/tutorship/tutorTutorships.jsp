<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL" /></em>
<h2><bean:message key="label.tutorshipSummary" bundle="APPLICATION_RESOURCES" /></h2>

<html:link page="/tutorshipSummaryPeriod.do?method=prepareCreate">
Definir Per�odo de Preenchimento das Fichas
</html:link>

<br />

<fr:form action="/tutorshipSummary.do">
	<html:hidden property="method" value="searchTeacher" />

	<fr:edit id="tutorateBean" name="tutorateBean" schema="tutorship.summary.search" layout="tabular">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright mbottom0 thmiddle"/>
	        <fr:property name="columnClasses" value="width125px,,tderror1 tdclear"/>
		</fr:layout>
        <fr:destination name="postback" path="/tutorshipSummary.do?method=postback"/>
	</fr:edit>

	<logic:notEmpty name="tutorateBean" property="searchType">
		<logic:equal name="tutorateBean" property="searchType" value="true">
			<fr:edit id="tutorateBeanDepartment" name="tutorateBean" schema="tutorship.summary.searchByDepartment">
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thright gluetop mtop0 mbottom05 thmiddle"/>
			        <fr:property name="columnClasses" value="width125px,,tderror1 tdclear"/>
				</fr:layout>
		        <fr:destination name="postback" path="/tutorshipSummary.do?method=postback"/>
			</fr:edit>
		</logic:equal>
		<logic:equal name="tutorateBean" property="searchType" value="false">
			<fr:edit id="tutorateBeanDegree" name="tutorateBean" schema="tutorship.summary.searchByDegree">
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thright gluetop mtop0 mbottom05"/>
			        <fr:property name="columnClasses" value="width125px,,tderror1 tdclear"/>
				</fr:layout>
		        <fr:destination name="postback" path="/tutorshipSummary.do?method=postback"/>
			</fr:edit>
		</logic:equal>
	</logic:notEmpty>
    
    <!-- <html:submit><bean:message key="button.filter" bundle="APPLICATION_RESOURCES" /></html:submit> -->
    <html:submit onclick="this.form.method.value='exportSummaries';this.form.submit();">Exportar esta listagem (Excel)</html:submit>
</fr:form>

<br/>

<html:messages id="message" message="true" bundle="PEDAGOGICAL_COUNCIL">
	<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
</html:messages>

<p class="mtop2 mbottom1 separator2">
	<b><bean:message key="label.tutorshipSummary.ableToCreate" bundle="APPLICATION_RESOURCES"/></b>
</p>

<!-- available summaries to create -->
<logic:empty name="tutorateBean" property="availableSummaries">
	<bean:message key="message.tutorshipSummary.empty" bundle="APPLICATION_RESOURCES" />
</logic:empty>

<logic:notEmpty name="tutorateBean" property="availableSummaries">
	<ul>
	<logic:iterate id="createSummaryBean" name="tutorateBean" property="availableSummaries">

		<li>
		
		<logic:equal value="true" name="createSummaryBean" property="persisted">
			<bean:define id="summaryId" name="createSummaryBean" property="externalId" type="java.lang.String" />
						
			<html:link page="<%= "/tutorshipSummary.do?method=createSummary&summaryId=" + summaryId %>">
				<bean:message key="label.curricular.course.semester" bundle="APPLICATION_RESOURCES" /> 
				<strong><bean:write name="createSummaryBean" property="executionSemester.semester"/> - <bean:write name="createSummaryBean" property="executionSemester.executionYear.year"/></strong>,
				<bean:message key="label.degree.name" bundle="APPLICATION_RESOURCES" />:
				<strong><bean:write name="createSummaryBean" property="degree.sigla" /></strong>
			</html:link>
			<!--  (created) -->
		</logic:equal>
		
		
		<logic:equal value="false" name="createSummaryBean" property="persisted">			
			<bean:define id="teacherId" name="tutor" property="externalId" type="java.lang.String" />
			<bean:define id="degreeId" name="createSummaryBean" property="degree.externalId" type="java.lang.String" />
			
			<html:link page="<%= "/tutorshipSummary.do?method=createSummary&teacherId=" + teacherId + "&degreeId=" + degreeId %>">
				<bean:message key="label.curricular.course.semester" bundle="APPLICATION_RESOURCES" /> 
				<strong><bean:write name="createSummaryBean" property="executionSemester.semester"/> - <bean:write name="createSummaryBean" property="executionSemester.executionYear.year"/></strong>,
				<bean:message key="label.degree.name" bundle="APPLICATION_RESOURCES" />:
				<strong><bean:write name="createSummaryBean" property="degree.sigla" /></strong>
			</html:link>
			<!--  (new) -->
		</logic:equal>
		
		</li>

	</logic:iterate>
	</ul>
</logic:notEmpty>

<!--  finished summaries -->

<p class="mtop2 mbottom1 separator2">
	<b><bean:message key="label.tutorshipSummary.past" bundle="APPLICATION_RESOURCES"/></b>
</p>

<logic:empty name="tutorateBean" property="pastSummaries">
	<bean:message key="message.tutorshipSummary.empty" bundle="APPLICATION_RESOURCES" />
</logic:empty>
<logic:notEmpty name="tutorateBean" property="pastSummaries">
	<ul>
	<logic:iterate id="summary" name="tutorateBean" property="pastSummaries">
		<li>
			<bean:define id="summaryId" name="summary" property="externalId" />
			<html:link page="<%= "/tutorshipSummary.do?method=viewSummary&summaryId=" + summaryId %>">
				<bean:message key="label.curricular.course.semester" bundle="APPLICATION_RESOURCES" /> 
				<strong><bean:write name="summary" property="semester.semester"/> - <bean:write name="summary" property="semester.executionYear.year"/></strong>,
				<bean:message key="label.degree.name" bundle="APPLICATION_RESOURCES" />:
				<strong><bean:write name="summary" property="degree.sigla" /></strong>
			</html:link>
		</li>
	</logic:iterate>
	</ul>
</logic:notEmpty>


