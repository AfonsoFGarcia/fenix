<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.TipoCurso" %>

<p><span class="error"><html:errors/></span></p>

<logic:present name="infoCurriculum" >

<bean:define id="infoCurricularCourse" name="infoCurriculum" property="infoCurricularCourse" />
<bean:define id="infoDegree" name="infoCurriculum" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree" />
<bean:define id="degreeCurricularPlanID" name="infoCurriculum" property="infoCurricularCourse.infoDegreeCurricularPlan.idInternal" />

<div  class="breadcumbs"><a href="http://www.ist.utl.pt/index.shtml">IST</a> > 
		<bean:define id="degreeType" name="infoDegree" property="tipoCurso" />	
		<logic:equal name="degreeType" value="<%= TipoCurso.MESTRADO_OBJ.toString() %>">
			 <%--<html:link page="<%= "/showDegrees.do?method=master&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >--%>
			 	Ensino Mestrados
			 <%--</html:link>--%>
		</logic:equal>
		<logic:equal name="degreeType" value="<%= TipoCurso.LICENCIATURA_OBJ.toString() %>">
			<%--<html:link page="<%= "/showDegrees.do?method=nonMaster&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >--%>
				Ensino Licenciaturas
			<%--</html:link>--%>
		</logic:equal>
		&gt;&nbsp;
		<%--<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" + request.getAttribute("degreeID").toString() + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") %>">--%>
			<bean:write name="infoDegree" property="sigla" />
		<%--</html:link>--%>
		&gt;&nbsp;
		<%--<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" +  request.getAttribute("degreeID") + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") %>" > --%>
			<bean:message key="label.curricularPlan"/>
		<%--</html:link>--%>
		&gt;&nbsp;
		<%--<html:link page="<%= "/showDegreeCurricularPlan.do?method=showCurricularPlan&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") %>" >--%>
			<bean:message key="label.curriculum"/>
		<%--</html:link>--%>
		&gt;&nbsp;
		<bean:write name="infoCurricularCourse" property="name" />	
</div>	

<!-- P�GINA EM INGL�S -->
<div class="version">
	<span class="px10">
		<html:link page="<%= "/showCourseSite.do?method=showCurricularCourseSite&amp;inEnglish=true&amp;curricularCourseID=" +  pageContext.findAttribute("curricularCourseID") + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" +  request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.getAttribute("degreeCurricularPlanID") + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID")%>" >english version</html:link> <img src="<%= request.getContextPath() %>/images/icon_uk.gif" alt="Icon: English version!" width="16" height="12" />
	</span>
</div> 
<div class="clear"></div> 
<h1><bean:write name="infoDegree" property="tipoCurso" />&nbsp;<bean:write name="infoDegree" property="nome" /></h1>

<h2><span class="greytxt"><bean:write name="infoCurricularCourse" property="name" /></span></h2>
<br />


<!-- EXECUTION COURSES LINK  -->
<logic:present name="infoCurricularCourse" property="infoAssociatedExecutionCourses" />
<bean:size id="listSize" name="infoCurricularCourse" property="infoAssociatedExecutionCourses" />

<logic:greaterThan name="listSize" value="0">
<div class="col_right">
  <table class="box" cellspacing="0">
		<tr>
			<td class="box_header"><strong><bean:message key="label.courses" /></strong></td>
		</tr>
		<tr>
			<td class="box_cell">
				<logic:iterate id="infoExecutionCourse" name="infoCurricularCourse" property="infoAssociatedExecutionCourses">
					<bean:define id="executionCourseID" name="infoExecutionCourse" property="idInternal" />
					<p>
					<html:link page="<%= "/viewSiteExecutionCourse.do?method=firstPage&amp;objectCode=" + pageContext.findAttribute("executionCourseID").toString() %>" target="_blank" >
						<bean:write name="infoExecutionCourse" property="nome" />&nbsp;(<bean:write name="infoExecutionCourse" property="infoExecutionPeriod.infoExecutionYear.year"/>&nbsp;-&nbsp;<bean:write name="infoExecutionCourse" property="infoExecutionPeriod.name"/>)
					</html:link>
					<br />
					</p>
				</logic:iterate>
			</td>
		</tr>  
  </table>
</div>
</logic:greaterThan>
</logic:present>

<!-- 	CURRICULAR COURSE SCOPES  -->
<logic:present name="infoCurricularCourse" property="infoScopes">
<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="label.scope" />	</h2>
<p>
	<ul>
		<logic:iterate id="infoCurricularCourseScope" name="infoCurricularCourse" property="infoScopes">
			<li>
				<logic:notEmpty name="infoCurricularCourseScope" property="infoBranch.name">
					<logic:notEqual name="infoCurricularCourseScope" property="infoBranch.name" value="">	
						<bean:write name="infoCurricularCourseScope" property="infoBranch.name"/>&nbsp;
	 				</logic:notEqual>
				</logic:notEmpty>
				
				<logic:notEmpty name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year">
					<logic:notEqual name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year" value="">					
						<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year"/>�&nbsp;<bean:message key="property.curricularCourse.curricularYear"/>&nbsp;
		 			</logic:notEqual>
				</logic:notEmpty>
				
				<logic:notEmpty name="infoCurricularCourseScope" property="infoCurricularSemester.semester">
					<logic:notEqual name="infoCurricularCourseScope" property="infoCurricularSemester.semester" value="">					
						<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.semester"/>�&nbsp;<bean:message key="property.curricularCourse.semester"/>&nbsp;
	 				</logic:notEqual>
				</logic:notEmpty>
			</li>
		</logic:iterate>		
	</ul>
</p>
</logic:present>

<!-- CURRICULAR COURSE INFO -->
<logic:present name="infoCurriculum" property="generalObjectives">
<logic:notEmpty name="infoCurriculum" property="generalObjectives">
		<logic:notEqual name="infoCurriculum" property="generalObjectives" value="">
		<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="label.generalObjectives" />	</h2>
		<p>
			<bean:write name="infoCurriculum" property="generalObjectives" filter="false"/>
		</p>
		</logic:notEqual>
</logic:notEmpty>
</logic:present>
<logic:present name="infoCurriculum" property="operacionalObjectives">
<logic:notEmpty name="infoCurriculum" property="operacionalObjectives">
		<logic:notEqual name="infoCurriculum" property="operacionalObjectives" value="">
		<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="label.operacionalObjectives" /></h2>
		<p>
			<bean:write name="infoCurriculum" property="operacionalObjectives" filter="false"/>
		</p>
		</logic:notEqual>
</logic:notEmpty> 
</logic:present>
<br/>
<br/>
<logic:present name="infoCurriculum" property="program">
<logic:notEmpty name="infoCurriculum" property="program">
	<logic:notEqual name="infoCurriculum" property="program" value="">
		<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="label.program" /></h2>	
		<p>
			<bean:write name="infoCurriculum" property="program" filter="false" />
		</p>	
	</logic:notEqual>
</logic:notEmpty>
</logic:present>
<br/>
<br/>

</logic:present>

<logic:notPresent name="infoCurriculum" >
	<p><span class="error"><bean:message key="error.impossibleCurricularCourseInfo" /></span></p>
</logic:notPresent>