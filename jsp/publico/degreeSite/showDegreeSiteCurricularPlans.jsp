<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.TipoCurso" %>
<%@ page import="Util.DegreeCurricularPlanState" %>

<p><span class="error"><html:errors/></span></p>

<logic:present name="infoDegreeCurricularPlan">

<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<div class="breadcumbs"><a href="<%= institution.url %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a> >
<bean:define id="institutionUrlTeaching" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>ensino/index.shtml</bean:define>
&nbsp;&gt;&nbsp;<a href="<%= institutionUrlTeaching %>"><bean:message key="label.education" /></a>
		<bean:define id="degreeType" name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso" />	
		&nbsp;&gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString()  %>">
			<bean:write name="infoDegreeCurricularPlan" property="infoDegree.sigla" />
		</html:link>
		 &nbsp;&gt;&nbsp;<bean:message key="label.curricularPlan"/>
	</div>		
		
	<!-- COURSE NAME -->
	<h1>
		<bean:write name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso" />
		<bean:message key="label.in" />
		<bean:write name="infoDegreeCurricularPlan" property="infoDegree.nome" />
	</h1>

	<!-- CURRICULAR PLAN -->
	<h2 class="greytxt">
		<bean:message key="label.curricularPlan"/>
		<bean:message key="label.of" />
		<logic:notEmpty name="infoDegreeCurricularPlan" property="initialDate">
			<bean:define id="initialDate" name="infoDegreeCurricularPlan" property="initialDate" />		
			<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")+1) %>
			<logic:notEmpty name="infoDegreeCurricularPlan" property="endDate">
				<bean:define id="endDate" name="infoDegreeCurricularPlan" property="endDate" />	
				-<%= endDate.toString().substring(endDate.toString().lastIndexOf(" ")) %>
			</logic:notEmpty>
		</logic:notEmpty>	
	</h2>

	<div class="col_right">
		<table class="box" cellspacing="0">
			<tr>
				<td class="box_header">
					<bean:define id="degreeCurricularPlanID" name="infoDegreeCurricularPlan" property="idInternal" />
					<bean:define id="degreeInitials" name="infoDegreeCurricularPlan" property="infoDegree.sigla"/>
					<bean:define id="nameDegreeCurricularPlan" name="infoDegreeCurricularPlan" property="name"/>
					<html:link page="<%= "/prepareConsultCurricularPlanNew.do?method=prepare&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() + "&amp;nameDegreeCurricularPlan=" + pageContext.findAttribute("nameDegreeCurricularPlan") + "&amp;degreeInitials=" + pageContext.findAttribute("degreeInitials") + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" >
						<strong><bean:message key="label.courses" /></strong>
					</html:link>
				</td>
			</tr>		
			<tr>
				<td class="box_cell">
					<bean:message key="label.coursesText" />
				</td>
			</tr>

			<tr>
				<td class="box_header">
					<bean:define id="degreeCurricularPlanID" name="infoDegreeCurricularPlan" property="idInternal" />
					<html:hidden property="<%=SessionConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />	
					<html:link page="<%= "/chooseExamsMapContextDANew.do?degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() + "&amp;page=1&amp;method=prepare&amp;selectAllCurricularYears=on" %>">
						<strong><bean:message key="label.exams" /></strong>
					</html:link>
				</td>
			</tr>
			<tr>
				<td class="box_cell">
					<bean:message key="label.examsText" /> 
				</td>
			</tr>
			<tr>
				<td class="box_header">
					<bean:define id="degreeCurricularPlanID" name="infoDegreeCurricularPlan" property="idInternal" />
					<html:link page="<%= "/chooseContextDANew.do?method=prepare&nextPage=classSearch&inputPage=chooseContext&amp;degreeID=" + request.getAttribute("degreeID")+ "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString()%>" >
						<strong><bean:message key="label.classes" /></strong>
					</html:link>
				</td>
			</tr>
			<tr>
				<td class="box_cell">
					<bean:message key="label.classesText" />
				</td>
			</tr>


			<logic:present name="infoDegreeCurricularPlanList">
				<logic:notEmpty name="infoDegreeCurricularPlanList">
					<bean:size id="listSize" name="infoDegreeCurricularPlanList" />
					<logic:greaterThan name="listSize" value="1">
						<logic:iterate id="infoDegreeCurricularPlanElem" name="infoDegreeCurricularPlanList" indexId="index"> 
							<logic:notEqual name="index" value="0">
								<logic:equal name="infoDegreeCurricularPlanElem" property="state" value="<%= DegreeCurricularPlanState.ACTIVE_OBJ.toString() %>" >
									<bean:define id="plansActives" value="true" />											
								</logic:equal>
							</logic:notEqual>
						</logic:iterate>
			
						<logic:present name="plansActives">		
						<tr>
							<td class="box_header">
								<strong><bean:message key="label.curricularPlans" /></strong>
							</td>
						</tr>
						<tr>
							<td class="box_cell">
							<ul>		
								<logic:iterate id="infoDegreeCurricularPlanElem" name="infoDegreeCurricularPlanList" indexId="index"> 
									<bean:define id="otherDegreeCurricularPlanID" name="infoDegreeCurricularPlanElem" property="idInternal" />						
									<logic:equal name="infoDegreeCurricularPlanElem" property="state" value="<%= DegreeCurricularPlanState.ACTIVE_OBJ.toString() %>" > <!-- If is active -->
										<li><html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("otherDegreeCurricularPlanID").toString() + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >
											<logic:notEmpty name="infoDegreeCurricularPlan" property="initialDate">
											<bean:define id="initialDate" name="infoDegreeCurricularPlanElem" property="initialDate" />		
											<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")) %>
											<logic:notEmpty name="infoDegreeCurricularPlanElem" property="endDate">
												<bean:define id="endDate" name="infoDegreeCurricularPlanElem" property="endDate" />	
												-<%= endDate.toString().substring(endDate.toString().lastIndexOf(" ")) %>
											</logic:notEmpty>
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
				
	<logic:notEmpty name="infoDegreeCurricularPlan" property="description">
		<p><bean:write name="infoDegreeCurricularPlan" property="description" filter="false" /></p>
	</logic:notEmpty>	

	<span class="px10"><p><bean:message key="label.responsability.information.degree" /></p></span>				 

</logic:present>	
