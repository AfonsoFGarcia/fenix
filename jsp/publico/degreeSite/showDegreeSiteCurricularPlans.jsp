<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.TipoCurso" %>
<%@ page import="Util.DegreeCurricularPlanState" %>

<p><span class="error"><html:errors/></span></p>

<logic:present name="infoDegreeCurricularPlanList">
<logic:notEmpty name="infoDegreeCurricularPlanList">
	<logic:iterate id="infoDegreeCurricularPlan" name="infoDegreeCurricularPlanList" length="1">
	
		<div  class="breadcumbs"><a href="http://www.ist.utl.pt/index.shtml">IST</a> > <a href="http://www.ist.utl.pt/html/ensino/ensino.shtml">Ensino</a> &gt;&nbsp;
			<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" + request.getAttribute("degreeID").toString() %>">
				<bean:write name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso" />&nbsp<bean:write name="infoDegreeCurricularPlan" property="infoDegree.nome" />
			</html:link>
			 &gt;&nbsp;<bean:message key="label.curricularPlan"/>
		</div>		
		
		<!-- P�GINA EM INGL�S -->
		<div class="version"><span class="px10"><a href="#">english version</a> <img src="<%= request.getContextPath() %>/images/icon_uk.gif" alt="Icon: English version!" width="16" height="12" /></span></div>
		<div class="clear"></div> 
	
		<h1><bean:write name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso" />&nbsp<bean:write name="infoDegreeCurricularPlan" property="infoDegree.nome" /></h1>
	
		<bean:define id="firstDCP" name="infoDegreeCurricularPlan" property="name" />			
	</logic:iterate>
	
	
	<!-- ANO LECTIVO -->
	<logic:notEmpty name="schoolYear">
	  <h2><span class="redbox"><bean:write name="schoolYear" /></span>
  </logic:notEmpty>  
  <span class="greytxt">&nbsp;<bean:message key="label.curricularPlan"/></span></h2>
  <br />

	<div class="col_right">
  <table class="box" cellspacing="0">
		<tr>
			<td class="box_header"><strong><bean:message key="label.courses" /></strong></td>
		</tr>
		<tr>
			<td class="box_cell">
				<logic:iterate id="infoDegreeCurricularPlan" name="infoDegreeCurricularPlanList" length="1">
					<bean:define id="degreeCurricularPlanID" name="infoDegreeCurricularPlan" property="idInternal" />
					<p><html:link page="<%= "/showDegreeCurricularPlan.do?method=showCurricularPlan&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" ><bean:message key="link.curricularPlan" /></html:link>
					<bean:message key="text.curricularPlan" />
					<br /><br />
					</p>
				</logic:iterate>
			</td>
		</tr>

	  <bean:size id="listSize" name="infoDegreeCurricularPlanList" />
		<!-- LISTA DOS OUTROS PLANOSCURRICULARES -->
		<logic:greaterThan name="listSize" value="1">
			<!-- verify if the others plans are actives -->
			<logic:iterate id="infoDegreeCurricularPlan" name="infoDegreeCurricularPlanList" indexId="index"> 
				<logic:notEqual name="index" value="0">
				<logic:equal name="infoDegreeCurricularPlan" property="state" value="<%= DegreeCurricularPlanState.ACTIVE_OBJ.toString() %>" >
					<bean:define id="plansActives" value="true" />											
				</logic:equal>
				</logic:notEqual>
			</logic:iterate>
			
			<!-- links for the others degree curricular plan if they are actives -->
			<logic:present name="plansActives">		
			<tr>
				<td class="box_header"><strong><bean:message key="label.othersCurricularPlans" /></strong></td>
			</tr>
			<tr>
				<td class="box_cell">
				<ul>		
					<logic:iterate id="infoDegreeCurricularPlan" name="infoDegreeCurricularPlanList" indexId="index"> 
						<bean:define id="otherDegreeCurricularPlanID" name="infoDegreeCurricularPlan" property="idInternal" />
						<logic:notEqual name="index" value="0"> <!-- If isn't the first in the list -->
							<logic:equal name="infoDegreeCurricularPlan" property="state" value="<%= DegreeCurricularPlanState.ACTIVE_OBJ.toString() %>" > <!-- If is active -->
								<li><html:link page="<%= "/showDegreeCurricularPlan.do?method=showCurricularPlan&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("otherDegreeCurricularPlanID").toString() + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" ><bean:write name="infoDegreeCurricularPlan" property="name" /></html:link></li>
							</logic:equal>														
						</logic:notEqual>						
					</logic:iterate>
				</ul>
			</tr>
			</logic:present>
		</logic:greaterThan>	
	</table> 
	</div>
	
			
	<!-- DESCRI��O DO PLANO CURRICULAR(activo e o mais recente) -->
	<logic:iterate id="infoDegreeCurricularPlan" name="infoDegreeCurricularPlanList" length="1">
		<logic:notEmpty name="infoDegreeCurricularPlan" property="description">
		  <p><bean:write name="infoDegreeCurricularPlan" property="description" filter="false" /></p>
	  </logic:notEmpty>	
	</logic:iterate>
	
</logic:notEmpty>  
</logic:present>

<logic:notPresent name="infoDegreeCurricularPlanList">
	<h1><p><bean:message key="error.impossibleDegreeSite" /></p></h1>
</logic:notPresent>