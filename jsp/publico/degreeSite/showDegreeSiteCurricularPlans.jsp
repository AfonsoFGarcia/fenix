<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.TipoCurso" %>
<%@ page import="Util.DegreeCurricularPlanState" %>

<p><span class="error"><html:errors/></span></p>

<logic:present name="infoDegreeCurricularPlan">

	<div  class="breadcumbs"><a href="http://www.ist.utl.pt/index.shtml">IST</a> > <html:link page="<%= "/showDegrees.do?method=nonMaster&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >Ensino</html:link> &gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" + request.getAttribute("degreeID").toString() %>">
			<bean:write name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso" />&nbsp;<bean:write name="infoDegreeCurricularPlan" property="infoDegree.nome" />
		</html:link>
		 &gt;&nbsp;<bean:message key="label.curricularPlan"/>
	</div>		
		
	<!-- P�GINA EM INGL�S -->
	<div class="version"><!--<span class="px10"><a href="#">english version</a> <img src="<%= request.getContextPath() %>/images/icon_uk.gif" alt="Icon: English version!" width="16" height="12" /></span>--></div> 
	<div class="clear"></div> 
	
	<h1><bean:write name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso" />&nbsp;<bean:write name="infoDegreeCurricularPlan" property="infoDegree.nome" /></h1>

	<h2>
	<span class="greytxt">
	<bean:message key="label.curricularPlan"/>
	&nbsp;<bean:message key="label.the" />
	<bean:define id="initialDate" name="infoDegreeCurricularPlan" property="initialDate" />		
	&nbsp;<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")+1) %>
	<logic:notEmpty name="infoDegreeCurricularPlan" property="endDate">
		<bean:define id="endDate" name="infoDegreeCurricularPlan" property="endDate" />	
		-<%= endDate.toString().substring(endDate.toString().lastIndexOf(" ")) %>
	</logic:notEmpty>
	</span>
	</h2>
	<br />

<div class="col_right">
  <table class="box" cellspacing="0">
	<tr>
		<td class="box_header"><strong><bean:message key="label.courses" /></strong></td>
	</tr>
	<tr>
		<td class="box_cell">
			<bean:define id="degreeCurricularPlanID" name="infoDegreeCurricularPlan" property="idInternal" />
			<p><html:link page="<%= "/showDegreeCurricularPlan.do?method=showCurricularPlan&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" ><bean:message key="link.curricularPlan" /></html:link>
			<bean:message key="text.curricularPlan" />
			<br /><br />
			</p>
		</td>
	</tr>

	<logic:present name="infoDegreeCurricularPlanList">
	<logic:notEmpty name="infoDegreeCurricularPlanList">
		<bean:size id="listSize" name="infoDegreeCurricularPlanList" />
		<!-- LISTA DOS OUTROS PLANOSCURRICULARES -->
		<logic:greaterThan name="listSize" value="1">
			<!-- verify if the others plans are actives -->
			<logic:iterate id="infoDegreeCurricularPlanElem" name="infoDegreeCurricularPlanList" indexId="index"> 
				<logic:notEqual name="index" value="0">
				<logic:equal name="infoDegreeCurricularPlanElem" property="state" value="<%= DegreeCurricularPlanState.ACTIVE_OBJ.toString() %>" >
					<bean:define id="plansActives" value="true" />											
				</logic:equal>
				</logic:notEqual>
			</logic:iterate>
			
			<!-- links for the others degree curricular plan if they are actives -->
			<logic:present name="plansActives">		
			<tr>
				<td class="box_header"><strong><bean:message key="label.allCurricularPlans" /></strong></td>
			</tr>
			<tr>
				<td class="box_cell">
				<ul>		
					<logic:iterate id="infoDegreeCurricularPlanElem" name="infoDegreeCurricularPlanList" indexId="index"> 
						<bean:define id="otherDegreeCurricularPlanID" name="infoDegreeCurricularPlanElem" property="idInternal" />						
	  					<logic:equal name="infoDegreeCurricularPlanElem" property="state" value="<%= DegreeCurricularPlanState.ACTIVE_OBJ.toString() %>" > <!-- If is active -->
	  						<li><html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("otherDegreeCurricularPlanID").toString() + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >
								<bean:define id="initialDate" name="infoDegreeCurricularPlanElem" property="initialDate" />		
								<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")) %>
								<logic:notEmpty name="infoDegreeCurricularPlanElem" property="endDate">
									<bean:define id="endDate" name="infoDegreeCurricularPlanElem" property="endDate" />	
									-<%= endDate.toString().substring(endDate.toString().lastIndexOf(" ")) %>
								</logic:notEmpty>
							</html:link></li>
						</logic:equal>																				
					</logic:iterate>
				</ul>
			</tr>
			</logic:present>
		</logic:greaterThan>	
	</logic:notEmpty>  
	</logic:present>		
  </table> 
</div>
			
<!-- DESCRI��O DO PLANO CURRICULAR(activo e o mais recente) -->
<logic:notEmpty name="infoDegreeCurricularPlan" property="description">
		  <p><bean:write name="infoDegreeCurricularPlan" property="description" filter="false" /></p>
</logic:notEmpty>	
</logic:present>	
