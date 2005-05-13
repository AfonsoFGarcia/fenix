<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<logic:present name="infoDegree">
	<bean:define id="institutionUrl" type="java.lang.String">
		<bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>
	</bean:define>
	<div class="breadcumbs">
		<a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a> 
		<bean:define id="institutionUrlTeaching" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/><bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/></bean:define>
		&nbsp;&gt;&nbsp;<a href="<%= institutionUrlTeaching %>"><bean:message key="public.degree.information.label.education" bundle="PUBLIC_DEGREE_INFORMATION" /></a>
		<bean:define id="degreeType" name="infoDegree" property="tipoCurso" />	
		&nbsp;&gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString() %>">
			<bean:write name="infoDegree" property="sigla" />
		</html:link>
		&nbsp;&gt;&nbsp; <bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.classes"/>		
	</div>

	<h1>
		<bean:define id="degreeType" name="infoDegree" property="tipoCurso.name"/>
	    <logic:equal name="degreeType" value="DEGREE" >
		    <bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.degreeType" />
		</logic:equal>    
		<logic:equal name="degreeType" value="MASTER_DEGREE" >
		    <bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.masterDegreeType" />
		</logic:equal>    
		<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.in"/>
		<bean:write name="infoDegree" property="nome" />
	</h1>

	<h2><span class="greytxt"><bean:message  key="public.degree.information.label.classes"  bundle="PUBLIC_DEGREE_INFORMATION"/></span></h2>
</logic:present>


<p><span class="error"><html:errors/></span></p>


<bean:define id="currentSemester" name="execution_period" property="semester"/>

<table class="tab_lay" cellspacing="0" width="75%">
	<tr>
		<th colspan="5" scope="col">
			<bean:write name="execution_period" property="infoExecutionYear.year"/>,
			<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester.abbr"/>
			<bean:write name="execution_period" property="semester"/>
		</th>	
	</tr>

	<tr>
	<% for (int year = 1; year <= 5; year++) { %>
		<td class="subheader" width="75px"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/> <%= String.valueOf(year) %></td>
	<% } %>
	</tr>

	<bean:define id="numberRowsCurrent" name="classViewsTableCurrent" property="numberRows" />
	<% for (int rowIndex=0; rowIndex < new Integer(pageContext.findAttribute("numberRowsCurrent").toString()).intValue(); rowIndex++) { %>
	<% String rowColor = rowIndex % 2 == 0 ? "white" : "bluecell" ; %>
	<tr>
		<logic:iterate id="classView" name="classViewsTableCurrent" property='<%= "row[" + rowIndex + "]" %>'>
			<td class="<%= rowColor %>">
				<logic:notEmpty name="classView">
					<bean:define id="classOID" name="classView" property="classOID"/>
					<bean:define id="className" name="classView" property="className"/>
					<bean:define id="degreeCurricularPlanID" name="classView" property="degreeCurricularPlanID"/>
					<bean:define id="degreeInitials" name="classView" property="degreeInitials"/>
					<bean:define id="nameDegreeCurricularPlan" name="classView" property="nameDegreeCurricularPlan"/>

					<html:link page="<%= "/viewClassTimeTableNew.do?classId="
											+ pageContext.findAttribute("classOID").toString()
											+ "&amp;className="
											+ pageContext.findAttribute("className").toString()
											+ "&amp;degreeInitials="
											+ pageContext.findAttribute("degreeInitials").toString()
											+ "&amp;nameDegreeCurricularPlan="
											+ pageContext.findAttribute("nameDegreeCurricularPlan").toString()
											+ "&amp;degreeCurricularPlanID="
											+ pageContext.findAttribute("degreeCurricularPlanID").toString()
											+ "&amp;degreeID="
											+ pageContext.findAttribute("degreeID").toString()
										%>" >
						<bean:write name="classView" property="className"/>
					</html:link>
				</logic:notEmpty>
				<logic:empty name="classView">
					&nbsp;
				</logic:empty>
			</td>
		</logic:iterate>
		
	</tr>
	<% } %>

<logic:present name="nextInfoExecutionPeriod">
	<tr>
		<th colspan="5" scope="col">
			<bean:write name="nextInfoExecutionPeriod" property="infoExecutionYear.year"/>,
			<bean:write name="nextInfoExecutionPeriod" property="semester"/>
			<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester.abbr"/>
		</th>	
	</tr>

	<tr>
	<% for (int year = 1; year <= 5; year++) { %>
		<td class="subheader"><%= String.valueOf(year) %><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/></td>
	<% } %>
	</tr>

	<bean:define id="numberRowsNext" name="classViewsTableNext" property="numberRows" />
	<% for (int rowIndex=0; rowIndex < new Integer(pageContext.findAttribute("numberRowsNext").toString()).intValue(); rowIndex++) { %>
	<% String rowColor = rowIndex % 2 == 0 ? "white" : "bluecell" ; %>
	<tr>	
		<logic:iterate id="classView" name="classViewsTableNext" property='<%= "row[" + rowIndex + "]" %>'>
			<td class="<%= rowColor %>">
				<logic:notEmpty name="classView">
					<bean:define id="classOID" name="classView" property="classOID"/>
					<bean:define id="className" name="classView" property="className"/>
					<bean:define id="degreeCurricularPlanID" name="classView" property="degreeCurricularPlanID"/>
					<bean:define id="degreeInitials" name="classView" property="degreeInitials"/>
					<bean:define id="nameDegreeCurricularPlan" name="classView" property="nameDegreeCurricularPlan"/>

					<html:link page="<%= "/viewClassTimeTableNew.do?classId="
											+ pageContext.findAttribute("classOID").toString()
											+ "&amp;className="
											+ pageContext.findAttribute("className").toString()
											+ "&amp;degreeInitials="
											+ pageContext.findAttribute("degreeInitials").toString()
											+ "&amp;nameDegreeCurricularPlan="
											+ pageContext.findAttribute("nameDegreeCurricularPlan").toString()
											+ "&amp;degreeCurricularPlanID="
											+ pageContext.findAttribute("degreeCurricularPlanID").toString()
											+ "&amp;degreeID="
											+ pageContext.findAttribute("degreeID").toString()
										%>" >
						<bean:write name="classView" property="className"/>
					</html:link>
				</logic:notEmpty>
				<logic:empty name="classView">
					&nbsp;
				</logic:empty>
			</td>
		</logic:iterate>		
	</tr>
	<% } %>
</logic:present>

</table>

