<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoStudent" %>

<bean:define id="student" name="<%= SessionConstants.STUDENT %>" scope="request" />

<%
	java.util.Hashtable params = new java.util.Hashtable();
	InfoStudent infoStudent = (InfoStudent) student;
	params.put("degreeType", infoStudent.getDegreeType().getTipoCurso());
	params.put("studentNumber", infoStudent.getNumber());
	params.put("method", "getStudentForCreateMasterDegreeThesis");
	pageContext.setAttribute("parameters", params, PageContext.PAGE_SCOPE);
%>
  
<ul>
    <li><html:link page="/candidateSection.do"><bean:message key="link.masterDegree.administrativeOffice.candidate" /></html:link></li>
    <li><html:link page="/contributorSection.do"><bean:message key="link.masterDegree.administrativeOffice.contributor" /></html:link></li>
	<li><html:link page="/guideSection.do"><bean:message key="link.masterDegree.administrativeOffice.guide" /></html:link></li>
	<li><html:link page="/studentSection.do"><bean:message key="label.coordinator.student" /></html:link></li>
</ul>
    <p><b><bean:message key="link.masterDegree.administrativeOffice.thesis.title" /></b></p>
<ul>
	<blockquote>
	    <li><html:link page="/createMasterDegreeThesis.do" name="parameters"><bean:message key="link.masterDegree.administrativeOffice.thesis.create" /></html:link></li>
	    <li><html:link page="/thesisSection.do"><bean:message key="link.masterDegree.administrativeOffice.thesis.changeStudent"/></html:link></li>
	</blockquote>
</ul>
<ul>
	<li><html:link page="/gratuitySection.do"><bean:message key="link.masterDegree.administrativeOffice.gratuity" /></html:link></li>
    <li><html:link page="/externalPersonSection.do"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.title" /></html:link></li>
    <li><html:link page="/chooseExecutionYearToManageMarks.do?method=prepareChooseExecutionYear&jspTitle=title.masterDegree.administrativeOffice.marksManagement"><bean:message key="link.masterDegree.administrativeOffice.marksManagement" /></html:link></li>
    <li><html:link page="/listingSection.do"><bean:message key="link.masterDegree.administrativeOffice.listing" /></html:link></li>    
</ul>
	   
  	