<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<logic:present name="<%= SessionConstants.MASTER_DEGREE %>"  >
	<bean:define id="infoExecutionDegree" name="<%= SessionConstants.MASTER_DEGREE %>" scope="session"/>
	<bean:define id="infoExecutionDegreeId" name="infoExecutionDegree" property="idInternal"/>
	
	</br>
	</br>
	<hr></hr>
	<h2><center><bean:message key="link.coordinator.degreeSite.management"/></center></h2>
	
	<p>
	<ul>
		<li>
			<b><bean:message key="link.coordinator.degreeSite.edit" /></b>
			<ul>
				<li>
					<html:link page="<%= "/degreeSiteManagement.do?method=viewInformation&amp;info=description&amp;infoExecutionDegreeId=" + infoExecutionDegreeId.toString()%>">
					    <bean:message key="label.description" />&nbsp;<bean:message key="label.degree" />
					</html:link>
					<br/>
					<br/>
					</html:link>
				</li>
				<li>
					<html:link page="<%= "/degreeSiteManagement.do?method=viewInformation&amp;info=acess&amp;infoExecutionDegreeId=" + infoExecutionDegreeId.toString()%>">
					    <bean:message key="label.accessRequirements" />
					</html:link>
					<br/>
					<br/>
					</html:link>
				</li>
				<li>
					<html:link page="<%= "/degreeSiteManagement.do?method=viewDescriptionCurricularPlan&amp;infoExecutionDegreeId=" + infoExecutionDegreeId.toString()%>">
					    <bean:message key="label.description" />&nbsp;<bean:message key="label.curricularPlan" /> 
					</html:link>
					<br/>
					<br/>
					</html:link>
				</li>																	
			</ul>
		</li>
		<li>
			<html:link page="<%= "/degreeSiteManagement.do?method=viewHistoric&amp;infoExecutionDegreeId=" + infoExecutionDegreeId.toString()%>">
			    <bean:message key="link.coordinator.degreeSite.historic" /></html:link>
			    <br/>
				<br/>
			</html:link>
		</li>
		<li>
			<html:link href="<%= request.getContextPath()+"/publico/showDegreeSite.do?method=showDescription&amp;executionDegreeId=" + infoExecutionDegreeId.toString() %>" target="_blank">
			    <bean:message key="link.coordinator.degreeSite.viewSite" /></html:link>
			    <br/>
				<br/>
			</html:link>
		</li>		
	</ul>
	</p>
</logic:present>