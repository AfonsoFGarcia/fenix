<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<html:xhtml />

<style>
* { margin: 0; padding: 0; }

#button_results span {
background: none;
background: url("<%= request.getContextPath() %>/images/inq_button_left.gif") no-repeat center left;
padding: 8px 0 8px 12px; 
margin: 0;
}
 
#button_results span a {
background: none;
background: url("<%= request.getContextPath() %>/images/inq_button_right.gif") no-repeat center right;
padding: 8px 12px 8px 0;
margin: 0;
font-family: "Lucida Sans", Arial, sans-serif;
font-size: 11px;
color: #fff;
text-decoration: none;
font-weight: bold; 
}
 
#button_results span a:hover {
color: #fff;
text-decoration: underline;
}

form label {
color:#999;
font-family: "Lucida Sans", Arial, sans-serif;
font-size: 11px;
}

form select {
color:#555;
font-family: "Lucida Sans", Arial, sans-serif;
font-size: 11px;
}

</style>

<html:form action="/searchInquiriesResultPage.do">
	<html:hidden property="method" value="prepare"/>
	
	<p>
	<label for="executionSemesterID">Semestre e Ano:</label><br/>
		<html:select property="executionSemesterID" onchange="this.form.method.value='selectExecutionSemester';this.form.submit();">
			<html:option value="">Escolha uma op��o</html:option>
	 		<html:options collection="executionSemesters" property="idInternal" labelProperty="qualifiedName"/>
		</html:select>
	</p><br/>
	
	<p>
		<label for="executionDegreeID">Curso:</label><br/>
		<html:select property="executionDegreeID" onchange="this.form.method.value='selectExecutionDegree';this.form.submit();">
			<html:option value="">Escolha uma op��o</html:option>
	 		<html:options collection="executionDegrees" property="idInternal" labelProperty="presentationName"/>
		</html:select>
	</p><br/>

	<p>
		<label for="executionCourseID">Unidade Curricular:</label><br/>
		<html:select property="executionCourseID" onchange="this.form.method.value='selectExecutionCourse';this.form.submit();">
			<html:option value="">Escolha uma op��o</html:option>
	 		<html:options collection="executionCourses" property="idInternal" labelProperty="nome"/>
		</html:select>
	</p><br/>
		
</html:form>

<c:if test="${not empty executionCourse}">
	<br/>
	<bean:define id="executionCourseLink"><c:out value="${pageContext.request.contextPath}" /><c:out value="${executionCourse.site.reversePath}" />/resultados-quc</bean:define>
	<p id="button_results"><span>
		<!-- NO_CHECKSUM --><!-- HAS_CONTEXT --><a href="<%= executionCourseLink %>" target="_blank" title="Ir para p&aacute;gina de Resultados">Ir para p&aacute;gina de Resultados</a>	
	</span></p>
</c:if>