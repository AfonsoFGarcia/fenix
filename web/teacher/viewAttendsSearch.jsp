<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp" %>
<html:xhtml/>


<logic:present name="searchBean">
	<h2>Alunos de <bean:write name="searchBean" property="executionCourse.nome" /></h2>
	
	<span class="error"><!-- Error messages go here --><html:errors /></span>
	<div class="infoop2">
		<bean:message key="message.students.explanation"/>
	</div>
	
	<fr:form action="<%="/searchECAttends.do?method=search&amp;objectCode=" + request.getParameter("objectCode") %>">
		<%  if (request.getAttribute("degreeCurricularPlanID") != null) { %>
			    <html:hidden property="degreeCurricularPlanID" value="<%= request.getAttribute("degreeCurricularPlanID").toString() %>"/>
		<%  } %>
		<fr:edit id="searchBean" name="searchBean" layout="search-execution-course-attends">
			<fr:layout>
				<fr:property name="searchTableClasses" value="tstyle4 mtop15 tdtop inobullet"/>
			</fr:layout>
		</fr:edit>
	</fr:form>
	<logic:notEmpty name="searchBean" property="attendsResult">
		<bean:size id="size" name="searchBean" property="attendsResult"/>
		<h3> <bean:write name="size"/> Aluno(s) </h3>
	
		<fr:form id="sendMailForm" action="/searchECAttends.do?method=sendEmail&contentContextPath_PATH=/comunicacao/comunicacao">
			<fr:edit name="searchBean" id="mailViewState" visible="false"/>
			<!-- HAS_CONTEXT --><a href="javascript:document.getElementById('sendMailForm').submit()"><bean:message key="link.sendEmailToAllStudents"/></a>
		</fr:form>
		
		<fr:form id="downloadStudentListForm" action="/getTabSeparatedStudentList.do">
			<fr:edit name="searchBean" id="downloadViewState" visible="false"/>
			<a href="javascript:document.getElementById('downloadStudentListForm').submit()"><bean:message key="link.getExcelSpreadSheet"/></a>
		</fr:form>

		<bean:define id="bean" name="searchBean" property="searchElementsAsParameters"/>
		<cp:collectionPages
			url="<%="/teacher/searchECAttends.do?method=prepare&amp;objectCode=" + request.getParameter("objectCode") + bean %>" 
			pageNumberAttributeName="pageNumber"
			numberOfPagesAttributeName="numberOfPages"/>
		<fr:view name="attendsPagesBean">
			<fr:layout name="execution-course-attends-spreadsheet">
				<fr:property name="attendsListTableClasses" value="tstyle4 tdcenter"/>
				<fr:property name="summaryClasses" value="tstyle1 tdcenter mtop05"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<logic:empty name="searchBean" property="attendsResult">
		<p><em><bean:message key="message.students.attendsSearch.noResults"/>.</em></p>
	</logic:empty>
	
</logic:present>


