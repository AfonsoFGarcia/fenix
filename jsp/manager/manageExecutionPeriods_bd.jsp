<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<h2><bean:message bundle="MANAGER_RESOURCES" key="title.manage.execution.periods"/></h2>
<br />

<strong>Nota:</strong> As opera��es dispon�veis nesta p�gina envolvem a manipula��o
de uma elevada quantidade de dados pelo que demoram alguns minutos a processar.
<br /><br />


<logic:present name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>" scope="request">
<%--
	<html:form action="/createExecutionPeriodForm">
		Criar Periodo Execu��o: 
		<span class="error"><html:errors /></span>
		<br />
		<table>
			<tr>
				<td>
				</td>
				<td>
				Semestre
				</td>
				<td>
				Ano Lectivo
				</td>
			</tr>
			<tr>
				<td>
				Para o periodo:
				</td>
				<td>
				<html:text property="semesterToCreate" size="2"/>
				</td>
				<td>
				<html:text property="yearToCreate"  size="10"/>
				</td>
			</tr>
			<tr>
				<td>
				Importar dados do periodo:
				</td>
				<td>
				<html:text property="semesterToExportDataFrom" size="2"/>
				</td>
				<td>
				<html:text property="yearToExportDataFrom" size="10"/>
				</td>
			</tr>
			<tr>
				<td>
				</td>
				<td>
					<html:hidden property="method" value="createExecutionPeriod"/>
					<html:hidden property="page" value="1"/>
					<html:submit property="operation" styleClass="inputbutton">
						<bean:message bundle="MANAGER_RESOURCES" key="label.create"/>
					</html:submit>
				</td>
				<td>
					<html:reset value="Limpar" styleClass="inputbutton">
						<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
					</html:reset>
				</td>
			</tr>
		</table>
	</html:form>
 --%>
	<bean:message bundle="MANAGER_RESOURCES" key="list.title.execution.periods"/>
	<br />
	<table>
		<tr>
			<td class="listClasses-header">
				Semestre
			</td>
			<td class="listClasses-header">
				Ano Lectivo
			</td>
			<td class="listClasses-header">
				Estado
			</td>
			<td class="listClasses-header">
			</td>
		</tr>
		<logic:iterate id="infoExecutionPeriod" name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>">
			<bean:define id="year" name="infoExecutionPeriod" property="infoExecutionYear.year"/>
			<bean:define id="semester" name="infoExecutionPeriod" property="semester"/>
			<bean:define id="periodState" name="infoExecutionPeriod" property="state.stateCode"/>
			<tr>
				<td class="listClasses">
					<bean:write name="infoExecutionPeriod" property="name" />
				</td>
				<td class="listClasses">
					<bean:write name="infoExecutionPeriod" property="infoExecutionYear.year" />
				</td>
				<td class="listClasses">
					<bean:write name="infoExecutionPeriod" property="state" />
				</td>
				<td class="listClasses">
				
				<logic:equal name="infoExecutionPeriod" property="state.stateCode" value="NO">
					<html:link module="/manager" page="<%= "/manageExecutionPeriods.do?method=alterExecutionPeriodState"
								+ "&amp;year="
								+ year
								+ "&amp;semester="
								+ semester
								+ "&amp;periodState="
								+ "O" %>" >
						<bean:message bundle="MANAGER_RESOURCES" key="link.open.execution.period"/>
					</html:link>
				</logic:equal>

				<logic:equal name="infoExecutionPeriod" property="state.stateCode" value="O">
					<html:link module="/manager" page="<%= "/manageExecutionPeriods.do?method=alterExecutionPeriodState"
								+ "&amp;year="
								+ year
								+ "&amp;semester="
								+ semester
								+ "&amp;periodState="
								+ "C" %>" >
						<bean:message bundle="MANAGER_RESOURCES" key="link.current.execution.period"/>
					</html:link>
					<br />
					<html:link module="/manager" page="<%= "/manageExecutionPeriods.do?method=alterExecutionPeriodState"
								+ "&amp;year="
								+ year
								+ "&amp;semester="
								+ semester
								+ "&amp;periodState="
								+ "CL" %>" >
						<bean:message bundle="MANAGER_RESOURCES" key="link.close.execution.period"/>
					</html:link>

				</logic:equal>

				<logic:equal name="infoExecutionPeriod" property="state.stateCode" value="C">
				</logic:equal>

				<logic:equal name="infoExecutionPeriod" property="state.stateCode" value="CL">
					<html:link module="/manager" page="<%= "/manageExecutionPeriods.do?method=alterExecutionPeriodState"
								+ "&amp;year="
								+ year
								+ "&amp;semester="
								+ semester
								+ "&amp;periodState="
								+ "O" %>" >
						<bean:message bundle="MANAGER_RESOURCES" key="link.open.execution.period"/>
					</html:link>
				</logic:equal>

				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>

<logic:notPresent name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>" scope="request">
	<span class="error">
		<html:errors /><bean:message bundle="MANAGER_RESOURCES" key="errors.execution.period.none"/>
	</span>
</logic:notPresent>
