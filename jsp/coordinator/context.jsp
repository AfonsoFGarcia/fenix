<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.TipoCurso" %>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" bgcolor="#FFFFFF" class="infoselected">
			<strong>
				<logic:present name="<%= SessionConstants.MASTER_DEGREE %>"  >
					<bean:define id="infoExecutionDegree" name="<%= SessionConstants.MASTER_DEGREE %>" scope="session"/>

					<bean:message key="label.masterDegree.coordinator.selectedDegree"/> 
					<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome" />
					<br/>
					<br/>
					<bean:message key="label.masterDegree.coordinator.executionYear"/> 
					<bean:write name="infoExecutionDegree" property="infoExecutionYear.year" />
				
					<logic:equal name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" value="<%= TipoCurso.MESTRADO_OBJ.toString() %>">
						<br/>
						<br/>
						<bean:define id="candidates" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE_AMMOUNT %>" scope="session"/>
						<bean:message key="label.masterDegree.coordinator.candidates"/> 
						<bean:write name="candidates" />
					</logic:equal>
				</logic:present>
			</strong>
		</td>
	</tr>
</table>
