<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="Util.TipoCurso" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<div class="breadcumbs"><a href="http://www.ist.utl.pt/index.shtml">IST</a>
	&nbsp;&gt;&nbsp;<a href="http://www.ist.utl.pt/html/ensino/index.shtml">Ensino</a>	
	<bean:define id="degreeType" name="<%= SessionConstants.INFO_DEGREE_CURRICULAR_PLAN %>" property="infoDegree.tipoCurso" />	
	<bean:define id="infoDegreeCurricularPlan" name="<%= SessionConstants.INFO_DEGREE_CURRICULAR_PLAN%>"  />	
	<html:hidden property="<%=SessionConstants.INFO_DEGREE_CURRICULAR_PLAN %>" value="<%= pageContext.findAttribute(SessionConstants.INFO_DEGREE_CURRICULAR_PLAN).toString()%>"/>
	&nbsp;&gt;&nbsp;
	<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" + request.getAttribute("degreeID").toString() + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") + "&amp;index=" + request.getAttribute("index") %>">
		<bean:write name="infoDegreeCurricularPlan" property="infoDegree.sigla" />
	</html:link>
	&nbsp;&gt;&nbsp;
	<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID") + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;executionDegreeID="  + "&amp;index=" + request.getAttribute("index")  %>" >
		<bean:message key="label.curricularPlan"/>
	</html:link>
	&nbsp;&gt;&nbsp;<bean:message key="label.turmas"/> 	
</div>	
<%--
<!-- P�GINA EM INGL�S -->
	<div class="version">
		<span class="px10">
			<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;inEnglish=true&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" +  request.getAttribute("degreeID") + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") + "&amp;index=" + request.getAttribute("index") %>" >english version</html:link> <img src="<%= request.getContextPath() %>/images/icon_uk.gif" alt="Icon: English version!" width="16" height="12" />
	</span>	
	</div>--%>
	<div class="clear"></div> 
<h1><bean:write name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso" />&nbsp;<bean:write name="infoDegreeCurricularPlan" property="infoDegree.nome" /></h1>

<h2>
<span class="greytxt">
	<bean:message key="label.curricularPlan"/>
	<bean:message key="label.the" />
	<bean:define id="initialDate" name="infoDegreeCurricularPlan" property="initialDate" />		
	<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")+1) %>
	<logic:notEmpty name="infoDegreeCurricularPlan" property="endDate">
		<bean:define id="endDate" name="infoDegreeCurricularPlan" property="endDate" />	
		-<%= endDate.toString().substring(endDate.toString().lastIndexOf(" ")) %>
	</logic:notEmpty>
</span>
</h2>
<br />
<logic:present name="classList"  >	

<table align="center">
		<tr>
			<th class="listClasses-header">
				<bean:message key="property.name"/>
			</th>
			<th class="listClasses-header">
				<bean:message key="property.context.semester"/>
			</th>
			<th class="listClasses-header">
				<bean:message key="property.context.curricular.year"/> 
			</th>
		</tr>		
	<logic:iterate id="classview" name="classList"  >
		<tr>
		    <bean:define id="classId" name="classview" property="idInternal"/>
			<td class="listClasses">		
			<html:link page="<%= "/viewClassTimeTableNew.do?executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)+ "&amp;classId="+pageContext.findAttribute("classId") + "&amp;nameDegreeCurricularPlan=" +pageContext.findAttribute("nameDegreeCurricularPlan")+ "&amp;degreeInitials=" +pageContext.findAttribute("degreeInitials")+ "&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID") + "&amp;index=" + request.getAttribute("index") %>" paramId="className" paramName="classview" paramProperty="nome">
			<jsp:getProperty name="classview" property="nome"/>
			</html:link>
			</td>
			<td class="listClasses">
			<bean:write name="classview" property="infoExecutionPeriod.name"/>
			</td>
			<td class="listClasses">
			<jsp:getProperty name="classview" property="anoCurricular"/>
			</td>
		</tr>	
	</logic:iterate>
</table>
</logic:present>
<logic:notPresent name="classview"  >	
<table align="center" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					<span class="error"><bean:message key="message.public.notfound.classes"/></span>
				</td>
			</tr>
</table>
</logic:notPresent>
