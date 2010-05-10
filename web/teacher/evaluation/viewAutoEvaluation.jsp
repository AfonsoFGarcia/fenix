<%@ page language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<h2><bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.autoevaluation.title" /></h2>

<p><bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.autoevaluation.topHelpText" /></p>

<logic:iterate id="process" name="processes">
	<h3 class="separator2 mtop15"><fr:view name="process" property="facultyEvaluationProcess.title" /></h3>

	<fr:view name="process">
		<fr:schema bundle="RESEARCHER_RESOURCES"
			type="net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationProcess">
			<fr:slot name="facultyEvaluationProcess.autoEvaluationInterval"
				key="label.teacher.evaluation.facultyEvaluationProcess.autoEvaluationInterval" layout="format">
				<fr:property name="format"
					value="${start.dayOfMonth,02d}-${start.monthOfYear,02d}-${start.year} a ${end.dayOfMonth,02d}-${end.monthOfYear,02d}-${end.year}" />
			</fr:slot>
			<fr:slot name="evaluator" key="label.teacher.evaluation.evaluator" layout="name-with-alias" />
			<fr:slot name="coEvaluatorsAsString" key="label.teacher.evaluation.coEvaluator" />
			<fr:slot name="state" key="label.teacher.evaluation.state">
				<fr:property name="eachSchema" value="" />
				<fr:property name="eachLayout" value="values" />
			</fr:slot>
			<fr:slot name="type" key="label.teacher.evaluation.type" layout="null-as-label" />
			<fr:slot name="evaluationMark" key="label.teacher.evaluation.mark" layout="null-as-label" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05" />
		</fr:layout>
	</fr:view>

	<bean:define id="processId" name="process" property="externalId" />
	<logic:equal name="process" property="inAutoEvaluation" value="true">
		<p><html:link action="/teacherEvaluation.do?method=changeEvaluationType" paramId="process" paramName="process"
			paramProperty="externalId">
			<logic:empty name="process" property="type">
				<bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.autoevaluation.setEvaluationType" />
			</logic:empty>
			<logic:notEmpty name="process" property="type">
				<bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.autoevaluation.changeEvaluationType" />
			</logic:notEmpty>
		</html:link><logic:equal name="process" property="possibleToLockAutoEvaluation" value="true"> | <a href="#"
				style="cursor: pointer;"
				onclick="<%="check(document.getElementById('warning"
								+ processId + "'));return false;"%>"> <bean:message
				bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.autoevaluation.lock" /> </a>

		</logic:equal></p>
	</logic:equal>

	<div id="<%="warning" + processId%>" class="dnone">
	<div class="warning1">
	<p class="mvert05"><bean:message key="label.teacher.evaluation.evaluation.lock.confirm"
		bundle="RESEARCHER_RESOURCES" /></p>
	<p class="mtop1 mbottom05">
	<form method="post" id="lockMark"
		action="<%=request.getContextPath()
								+ "/researcher/teacherEvaluation.do?method=lockAutoEvaluation&process="
								+ processId%>">
	<html:submit> Lacrar</html:submit> <input value="Cancelar"
		onclick="check(document.getElementById('<%="warning" + processId%>'));return false;" type="button"></form>
	</p>
	</div>
	</div>

	<script type="text/javascript">
		function check(e,v){
			if (e.className == "dnone") {
			  e.className = "dblock";
			  v.value = "-";
			} else {
			  e.className = "dnone";
		  	  v.value = "+";
			}
		}
	</script>

	<logic:present name="process" property="currentTeacherEvaluation">
		<p class="mbottom05"><strong><fr:view name="process" property="type" layout="null-as-label" /> (<fr:view
			name="process" property="facultyEvaluationProcess.title" />)</strong></p>

		<logic:notEmpty name="process" property="teacherEvaluationFileBeanSet">
			<bean:define id="externalId" name="process" property="externalId" />
			<fr:view name="process" property="teacherEvaluationFileBeanSet">
				<fr:schema bundle="RESEARCHER_RESOURCES"
					type="net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationFileBean">
					<fr:slot name="teacherEvaluationFileType" key="label.teacher.evaluation.empty" layout="null-as-label" />
					<fr:slot name="teacherEvaluationFile" layout="link" key="label.teacher.evaluation.file" />
					<fr:slot name="teacherEvaluationFileUploadDate" key="label.teacher.evaluation.date" layout="null-as-label" />
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thlight mtop05" />
					<fr:property name="link(upload)"
						value="<%= "/teacherEvaluation.do?method=prepareUploadAutoEvaluationFile&OID="+externalId %>" />
					<fr:property name="key(upload)" value="label.teacher.evaluation.upload" />
					<fr:property name="param(upload)" value="teacherEvaluationFileType/type" />
					<fr:property name="bundle(upload)" value="RESEARCHER_RESOURCES" />
					<fr:property name="visibleIf(upload)" value="canUploadAutoEvaluationFile" />
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
		
		<logic:empty name="process" property="teacherEvaluationFileBeanSet">
			<p><bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.noFilesNeeded.warning"/></p>
		</logic:empty>
	</logic:present>
</logic:iterate>