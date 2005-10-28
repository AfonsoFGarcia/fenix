<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>Gest&atilde;o de Exames</h2>
<p>Seleccione a op&ccedil;&atilde;o pretendida para criar, editar ou visualisar a calendariza&ccedil&atilde;o dos exames. <br />
Pode alterar em baixo o per�odo de execu&ccedil&atilde;o seleccionado.</p>
<span class="error"><html:errors /></span>
<html:form action="/mainExamsNew">
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td nowrap="nowrap" width="125"><bean:message key="property.executionPeriod"/>:</td>
    <td nowrap="nowrap"><jsp:include page="../selectExecutionPeriodList.jsp"/></td>
  </tr>
</table>
<br />
<html:hidden property="method" value="choose"/>
<html:hidden property="page" value="1"/>
<html:submit styleClass="inputbutton">
<bean:message key="label.choose"/>
</html:submit>
</html:form>

<br/>

<html:form action="/publishExams">
	<html:hidden property="method" value="switchPublishedState"/>
	<html:hidden property="page" value="1"/>
	<html:hidden property="<%= net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants.EXECUTION_PERIOD_OID %>"
				 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>

	<bean:message key="publish.exams.map" />
	<br/>
	<html:submit styleClass="inputbutton">
		<bean:message key="label.change.published.state"/>
	</html:submit>
	<br />

	<logic:present name="executionDegrees">
		<table>
			<tr>
				<th>
					<bean:message key="label.manager.degree.tipoCurso"/>
				</th>
				<th>
					<bean:message key="label.degree"/>
				</th>
				<th>
					<bean:message key="label.exams.map.temp.state"/>
				</th>
			</tr>			
			<logic:iterate id="executionDegree" name="executionDegrees">
				<tr>
					<td>
						<bean:message name="executionDegree" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
					</td>
					<td>
						<bean:write name="executionDegree" property="degreeCurricularPlan.degree.nome"/>
					</td>
					<td>
						<logic:equal name="executionDegree" property="temporaryExamMap" value="true">
							<bean:message key="label.change.published.state.temp"/>
						</logic:equal>
						<logic:equal name="executionDegree" property="temporaryExamMap" value="false">
							<bean:message key="label.change.published.state.published"/>
						</logic:equal>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:present>

</html:form>
