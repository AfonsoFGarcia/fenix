<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<logic:present name="jspTitle">
	<h2><bean:write name="jspTitle" /></h2>
	<br />
</logic:present>
<span class="error"><html:errors/></span>
<logic:present name="<%= SessionConstants.DEGREE_LIST %>">
	<logic:present name="executionYear" >
	<bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:<bean:write name="executionYear" />
	<br />
	</logic:present>
	<bean:message key="title.masterDegree.administrativeOffice.chooseMasterDegree" />
	<br /><br />
	<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Action.MAPPING_KEY %>" />
	<table>
	   <!-- MasterDegree -->
		<logic:iterate id="masterDegreeElem" name="<%= SessionConstants.DEGREE_LIST %>" type="DataBeans.InfoExecutionDegree">
			<tr>
				<td>
					<logic:present name="jspTitle">
						<html:link page="<%= path + ".do?method=chooseMasterDegree&amp;degree=" + masterDegreeElem.getInfoDegreeCurricularPlan().getInfoDegree().getSigla() + "&amp;executionYear=" + pageContext.findAttribute("executionYear") + "&amp;jspTitle=" + pageContext.findAttribute("jspTitle") %>">
							<bean:message key="label.masterDegree.administrativeOffice.masterDegree"/>&nbsp;<bean:write name="masterDegreeElem" property="infoDegreeCurricularPlan.infoDegree.nome"/>
						</html:link>
					</logic:present>
					<logic:notPresent name="jspTitle">
						<html:link page="<%= path + ".do?method=chooseMasterDegree&amp;degree=" + masterDegreeElem.getInfoDegreeCurricularPlan().getInfoDegree().getSigla() + "&amp;executionYear=" + pageContext.findAttribute("executionYear") %>">
							<bean:message key="label.masterDegree.administrativeOffice.masterDegree"/>&nbsp;<bean:write name="masterDegreeElem" property="infoDegreeCurricularPlan.infoDegree.nome"/>
						</html:link>
					</logic:notPresent>
				</td>
	   		</tr>
	   	</logic:iterate>
	</table>
	<br />
</logic:present>