<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<br />
		<table width="100%">
			<tr>
				<td class="infoop">
					<bean:message key="label.objectives.explanation" />
				</td>
			</tr>
		</table>	


<logic:present name="siteView">
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="infoCurriculums" name="component" property="infoCurriculums"/>

<logic:empty name="infoCurriculums">
<h2><bean:message key="message.teacher.scientificCouncilControl.objectives"/></h2>
</logic:empty>	
<logic:iterate id="objectives" name="infoCurriculums">


<h2><bean:message key="title.objectives"/></h2>
<h3><bean:write name="objectives" property="infoCurricularCourse.name"/></h3>
<h3><bean:write name="objectives" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome"/></h3>
<table>
	<tr>
		<td><strong><bean:message key="label.generalObjectives" /></strong>
		</td>
	</tr>
	<tr>
		<td> 
			<bean:write name="objectives" property="generalObjectives" filter="false"/>
	 	</td>
	</tr>
	<logic:notEmpty name="objectives" property="generalObjectivesEn">
		<tr>
			<td><strong><bean:message key="label.generalObjectives.eng" /></strong>
			</td>
		</tr>
		<tr>
			<td>
				<bean:write name="objectives" property="generalObjectivesEn" filter="false"/>
		 	</td>
		</tr>
	</logic:notEmpty>

</table>
<table>
	<tr>
		<td><strong><bean:message key="label.operacionalObjectives" /></strong>
		</td>
	</tr>
	<tr>
		<td>
			<bean:write name="objectives" property="operacionalObjectives" filter="false"/>
		</td>
	</tr>
	<tr>
	</tr>
	<logic:notEmpty name="objectives" property="operacionalObjectivesEn">
	<tr>
		<td><strong><bean:message key="label.operacionalObjectives.eng" /></strong>
		</td>
	</tr>
	<br />
	<tr>
		<td>
			<bean:write name="objectives" property="operacionalObjectivesEn" filter="false"/>
		</td>
	</tr>
	</logic:notEmpty>
</table>
<br />	
<bean:define id="curriculumId" name="objectives" property="idInternal"/>
<div class="gen-button">
	<html:link page="<%= "/objectivesManagerDA.do?method=prepareEditObjectives&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;curriculumCode="+ pageContext.findAttribute("curriculumId")%>">
		<bean:message key="button.edit"/>
	</html:link>
</div>
</logic:iterate>

</logic:present>