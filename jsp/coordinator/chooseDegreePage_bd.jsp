<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlanWithDegree"%>

<span class="error"><html:errors/></span>

<bean:define id="coordinatedInfoDegreeCurricularPlans" name="<%= SessionConstants.MASTER_DEGREE_LIST %>" scope="session" />
<bean:define id="link">/chooseDegree.do?degreeCurricularPlanID=</bean:define>

<p><span class="emphasis"><%= ((List) coordinatedInfoDegreeCurricularPlans).size()%></span> <bean:message key="label.masterDegree.administrativeOffice.degreesFound"/></p>
<p><bean:message key="label.masterDegree.chooseOne"/></p>

<table>
	<tr>
		<td class="listClasses-header">Nome</td>
		<td class="listClasses-header">Plano Curricular</td>
	</tr>
	
	<logic:iterate id="infoDegreeCurricularPlan" name="coordinatedInfoDegreeCurricularPlans">
		<bean:define id="degreeLink">
			<bean:write name="link"/><bean:write name="infoDegreeCurricularPlan" property="idInternal"/>
		</bean:define>            	

		<tr>
		   <td class="listClasses">
				<bean:message bundle="ENUMERATION_RESOURCES" key="<%=((InfoDegreeCurricularPlanWithDegree) infoDegreeCurricularPlan).getInfoDegree().getTipoCurso().toString()%>" />&nbsp;<bean:write name="infoDegreeCurricularPlan" property="infoDegree.nome" />
		   </td>
		   <td class="listClasses">
				<html:link page='<%= pageContext.findAttribute("degreeLink").toString() %>'>
					<bean:write name="infoDegreeCurricularPlan" property="name" /> 
				</html:link>
		   </td>	   
		</tr>
	</logic:iterate>

</table>
