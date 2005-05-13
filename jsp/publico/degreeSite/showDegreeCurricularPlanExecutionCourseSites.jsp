<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<logic:present name="infoDegree">
<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<div class="breadcumbs"><a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a> 
<bean:define id="institutionUrlTeaching" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/><bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/></bean:define>
&nbsp;&gt;&nbsp;<a href="<%= institutionUrlTeaching %>"><bean:message key="public.degree.information.label.education" bundle="PUBLIC_DEGREE_INFORMATION" /></a>
		<bean:define id="degreeType" name="infoDegree" property="tipoCurso" />	
		&nbsp;&gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString() %>">
			<bean:write name="infoDegree" property="sigla" />
		</html:link>
		&nbsp;&gt;&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.courseSites"/>		
	</div>

<p><span class="error"><html:errors/></span></p>

	<h1>
	    <bean:define id="degreeType" name="infoDegree" property="tipoCurso.name"/>
	    <logic:equal name="degreeType" value="DEGREE" >
	   		 <bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.degreeType" />
		</logic:equal>    
		<logic:equal name="degreeType" value="MASTER_DEGREE" >
		    <bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.masterDegreeType" />
		</logic:equal>   
		<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.in" />
		<bean:write name="infoDegree" property="nome" />
	</h1>


<h2 class="greytxt">
	<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.courseSites"/>		
</h2>

<ul>
	<li><a href="#currentSem">
			<bean:write name="execution_period" property="infoExecutionYear.year" />,
			<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester.abbr" />
			<bean:write name="execution_period" property="semester"/>
		</a>
	</li>
	<li><a href="#otherSem">
			<bean:write name="previousInfoExecutionPeriod" property="infoExecutionYear.year" />,
			<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester.abbr" />
			<bean:write name="previousInfoExecutionPeriod" property="semester"/>
		</a>
	</li>
</ul>

<p id="currentSem">
<table class="tab_lay" cellspacing="0" width="90%">
	<tr>
		<th colspan="2" scope="col">
			<bean:write name="execution_period" property="infoExecutionYear.year" />,
			<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester.abbr" />
			<bean:write name="execution_period" property="semester"/>
		</th>
	</tr>

	<bean:define id="numberRowsCurrent1_2" name="executionCourseViewsTableCurrent1_2" property="numberRows" />	
	<logic:greaterThan name="numberRowsCurrent1_2" value="0">
		<tr>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.first.year"/></td>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.second.year"/></td>
		</tr>
	
		<% for (int rowIndex=0; rowIndex < new Integer(pageContext.findAttribute("numberRowsCurrent1_2").toString()).intValue(); rowIndex++) { %>
		<% String rowColor = rowIndex % 2 == 0 ? "white" : "bluecell" ; %>
		<tr>
			<logic:iterate id="executionCourseView" name="executionCourseViewsTableCurrent1_2" property='<%= "row[" + rowIndex + "]" %>'>	
			<td class="<%= rowColor %>" width="50%">
				<logic:notEmpty name="executionCourseView">
					<bean:define id="executionCourseOID" name="executionCourseView" property="executionCourseOID"/>
					
					<html:link page="<%= "/viewSiteExecutionCourse.do?method=firstPage&objectCode="
										 + pageContext.findAttribute("executionCourseOID").toString()
									%>" >
						<bean:write name="executionCourseView" property="executionCourseName"/>
					</html:link>
				</logic:notEmpty>
				<logic:empty name="executionCourseView">&nbsp;</logic:empty>
			</td>
			
			</logic:iterate>
		</tr>
		<% } %>
	</logic:greaterThan>

	<bean:define id="numberRowsCurrent3_4" name="executionCourseViewsTableCurrent3_4" property="numberRows" />
	<logic:greaterThan name="numberRowsCurrent3_4" value="0">
		<tr>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.third.year"/></td>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.fourth.year"/></td>
		</tr>

		<% for (int rowIndex=0; rowIndex < new Integer(pageContext.findAttribute("numberRowsCurrent3_4").toString()).intValue(); rowIndex++) { %>
		<% String rowColor = rowIndex % 2 == 0 ? "white" : "bluecell" ; %>
		<tr>
			<logic:iterate id="executionCourseView" name="executionCourseViewsTableCurrent3_4" property='<%= "row[" + rowIndex + "]" %>'>	
			<td class="<%= rowColor %>" width="50%">
				<logic:notEmpty name="executionCourseView">
					<bean:define id="executionCourseOID" name="executionCourseView" property="executionCourseOID"/>
					<html:link page="<%= "/viewSiteExecutionCourse.do?method=firstPage&objectCode="
										 + pageContext.findAttribute("executionCourseOID").toString()
									%>" >
						<bean:write name="executionCourseView" property="executionCourseName"/>
					</html:link>
				</logic:notEmpty>
				<logic:empty name="executionCourseView">&nbsp;</logic:empty>
			</td>
			</logic:iterate>
		</tr>
		<% } %>
	</logic:greaterThan>
	
	<bean:define id="numberRowsCurrent5" name="executionCourseViewsTableCurrent5" property="numberRows" />
	<logic:greaterThan name="numberRowsCurrent5" value="0">
		<tr>
			<td colspan="2" class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.fiveth.year" /></td>
		</tr>
	
		<% for (int rowIndex=0; rowIndex < new Integer(pageContext.findAttribute("numberRowsCurrent5").toString()).intValue(); rowIndex++) { %>
		<% String rowColor = rowIndex % 2 == 0 ? "white" : "bluecell" ; %>
		<tr>
			<logic:iterate id="executionCourseView" name="executionCourseViewsTableCurrent5" property='<%= "row[" + rowIndex + "]" %>'>	
			<td class="<%= rowColor %>" colspan="2">
				<logic:notEmpty name="executionCourseView">
					<bean:define id="executionCourseOID" name="executionCourseView" property="executionCourseOID"/>
					<html:link page="<%= "/viewSiteExecutionCourse.do?method=firstPage&objectCode="
										 + pageContext.findAttribute("executionCourseOID").toString()
									%>" >
						<bean:write name="executionCourseView" property="executionCourseName"/>
					</html:link>
				</logic:notEmpty>
				<logic:empty name="executionCourseView">&nbsp;</logic:empty>
			</td>
			</logic:iterate>
		</tr>
		<% } %>
	</logic:greaterThan>
</table>
</p>

<p id="otherSem">
<table class="tab_lay" cellspacing="0" width="90%">
	<tr>
		<th colspan="2" scope="col">
			<bean:write name="previousInfoExecutionPeriod" property="infoExecutionYear.year" />,
			<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester.abbr" />
			<bean:write name="previousInfoExecutionPeriod" property="semester"/>
		</th>
	</tr>
	
	<bean:define id="numberRowsPrevious1_2" name="executionCourseViewsTablePrevious1_2" property="numberRows" />
	<logic:greaterThan name="numberRowsPrevious1_2" value="0">
		<tr>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.first.year"/></td>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.second.year"/></td>
		</tr>	
	
		<% for (int rowIndex=0; rowIndex < new Integer(pageContext.findAttribute("numberRowsPrevious1_2").toString()).intValue(); rowIndex++) { %>
		<% String rowColor = rowIndex % 2 == 0 ? "white" : "bluecell" ; %>
		<tr>
			<logic:iterate id="executionCourseView" name="executionCourseViewsTablePrevious1_2" property='<%= "row[" + rowIndex + "]" %>'>	
			<td class="<%= rowColor %>" width="50%">
				<logic:notEmpty name="executionCourseView">
					<bean:define id="executionCourseOID" name="executionCourseView" property="executionCourseOID"/>
					<html:link page="<%= "/viewSiteExecutionCourse.do?method=firstPage&objectCode="
										 + pageContext.findAttribute("executionCourseOID").toString()
									%>" >
						<bean:write name="executionCourseView" property="executionCourseName"/>
					</html:link>
				</logic:notEmpty>
				<logic:empty name="executionCourseView">&nbsp;</logic:empty>
			</td>
			</logic:iterate>
		</tr>
		<% } %>
	</logic:greaterThan>
	
	<bean:define id="numberRowsPrevious3_4" name="executionCourseViewsTablePrevious3_4" property="numberRows" />
	<logic:greaterThan name="numberRowsPrevious3_4" value="0">
		<tr>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.third.year"/></td>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.fourth.year"/></td>
		</tr>
	
		<% for (int rowIndex=0; rowIndex < new Integer(pageContext.findAttribute("numberRowsPrevious3_4").toString()).intValue(); rowIndex++) { %>
		<% String rowColor = rowIndex % 2 == 0 ? "white" : "bluecell" ; %>
		<tr>
			<logic:iterate id="executionCourseView" name="executionCourseViewsTablePrevious3_4" property='<%= "row[" + rowIndex + "]" %>'>	
			<td class="<%= rowColor %>" width="50%">
				<logic:notEmpty name="executionCourseView">
					<bean:define id="executionCourseOID" name="executionCourseView" property="executionCourseOID"/>
					<html:link page="<%= "/viewSiteExecutionCourse.do?method=firstPage&objectCode="
										 + pageContext.findAttribute("executionCourseOID").toString()
									%>" >
						<bean:write name="executionCourseView" property="executionCourseName"/>
					</html:link>
				</logic:notEmpty>
				<logic:empty name="executionCourseView">&nbsp;</logic:empty>
			</td>
			</logic:iterate>
		</tr>
		<% } %>
	</logic:greaterThan>
	
	<bean:define id="numberRowsPrevious5" name="executionCourseViewsTablePrevious5" property="numberRows" />
	<logic:greaterThan name="numberRowsPrevious5" value="0">
		<tr>
			<td colspan="2" class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.fiveth.year" /></td>
		</tr>	
	
		<% for (int rowIndex=0; rowIndex < new Integer(pageContext.findAttribute("numberRowsPrevious5").toString()).intValue(); rowIndex++) { %>
		<% String rowColor = rowIndex % 2 == 0 ? "white" : "bluecell" ; %>
		<tr>
			<logic:iterate id="executionCourseView" name="executionCourseViewsTablePrevious5" property='<%= "row[" + rowIndex + "]" %>'>	
			<td class="<%= rowColor %>" colspan="2">
				<logic:notEmpty name="executionCourseView">
					<bean:define id="executionCourseOID" name="executionCourseView" property="executionCourseOID"/>
					<html:link page="<%= "/viewSiteExecutionCourse.do?method=firstPage&objectCode="
										 + pageContext.findAttribute("executionCourseOID").toString()
									%>" >
						<bean:write name="executionCourseView" property="executionCourseName"/>
					</html:link>
				</logic:notEmpty>
				<logic:empty name="executionCourseView">&nbsp;</logic:empty>
			</td>
			</logic:iterate>
		</tr>
		<% } %>
	</logic:greaterThan>
	
	
</table>

</logic:present>

</p>