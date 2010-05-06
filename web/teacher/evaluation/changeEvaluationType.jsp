<%@ page language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<h2><bean:message bundle="RESEARCHER_RESOURCES"
	key="label.teacher.evaluation.autoevaluation.changeEvaluationType.title" /></h2>

<h3 class="mtop15"><fr:view name="typeSelection" property="process.facultyEvaluationProcess.title" /></h3>

<p><bean:message bundle="RESEARCHER_RESOURCES"
	key="label.teacher.evaluation.autoevaluation.changeEvaluationType.topHelpText" /></p>

<bean:define id="action" name="action"/>
<fr:edit id="process-selection" name="typeSelection" action="<%= "/teacherEvaluation.do?method=selectEvaluationType&action="+action%>">
	<fr:schema bundle="RESEARCHER_RESOURCES"
		type="net.sourceforge.fenixedu.presentationTier.Action.teacher.evaluation.TeacherEvaluationTypeSelection">
		<fr:slot name="type" key="label.teacher.evaluation.autoevaluation.type">
			<fr:property name="defaultOptionHidden" value="true" />
		</fr:slot>
	</fr:schema>
	<fr:destination name="cancel" path="<%= "/teacherEvaluation.do?method="+action%>" />
	<fr:destination name="invalid" path="<%= "/teacherEvaluation.do?method="+action%>" />
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thleft mtop05" />
	</fr:layout>
</fr:edit>
