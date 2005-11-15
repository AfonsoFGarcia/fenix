<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoBranch" %>

<html:form action="/manageCurricularCourseGroups">
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="insertCurricularCourseGroup" />
	<html:hidden property="type" value="area"/>
	<bean:define id="dcpId" name="infoDegreeCurricularPlan" property="idInternal"/>
	<html:hidden property="degreeCurricularPlanId" value="<%= dcpId.toString() %>"/>
	
<table>
<tr>	
<td><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.name"/></td><td><html:text property="name"/></td>
</tr>
<tr>	
<td><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.branch"/></td>
<td>	<html:select property="branchId">
		<html:options collection="branches" property="idInternal" labelProperty="name"/>
	</html:select>
</td>	
</tr>
<tr>	
<td><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.areaType"/></td>
<td>	
	<html:select property="areaType">
		<html:options collection="areas" property="value" labelProperty="label"/>
	</html:select>
</td>	
</tr>
<tr>
<td><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.minimumCredits"/></td><td>	<html:text property="minimumValue"/></td>
</tr>
<tr>	
<td><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.maximumCredits"/></td><td><html:text property="maximumValue"/></td>
</tr>
	
</table>	

<br/>
<br/>
	<html:submit styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
	<html:reset  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>			
</html:form>