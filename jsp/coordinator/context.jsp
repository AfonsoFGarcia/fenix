<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.TipoCurso" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" class="infoselected">
				<logic:present name="<%= SessionConstants.MASTER_DEGREE %>"  >
					<bean:define id="infoExecutionDegree" name="<%= SessionConstants.MASTER_DEGREE %>" scope="session"/>
					<p>
						<strong><bean:message key="label.masterDegree.coordinator.selectedDegree"/></strong> 
						<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome" />
						<br />
						<strong><bean:message key="label.masterDegree.coordinator.executionYear"/></strong>
						<bean:write name="infoExecutionDegree" property="infoExecutionYear.year" />
					</p>
					<logic:equal name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" value="<%= TipoCurso.MESTRADO_OBJ.toString() %>">
						<bean:define id="candidates" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE_AMMOUNT %>" scope="session"/>
					<p>	
						<strong><bean:message key="label.masterDegree.coordinator.candidates"/></strong>
						<bean:write name="candidates" />
					</p>
					</logic:equal>
				</logic:present>
		</td>
	</tr>
</table>