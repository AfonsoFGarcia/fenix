<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<ul>
    <li><html:link page="/candidateSection.do"><bean:message key="link.masterDegree.administrativeOffice.candidate" /></html:link></li>
    <li><html:link page="/contributorSection.do"><bean:message key="link.masterDegree.administrativeOffice.contributor" /></html:link></li>
    <li><html:link page="/guideSection.do"><bean:message key="link.masterDegree.administrativeOffice.guide" /></html:link></li>
    <li><html:link page="/studentSection.do"><bean:message key="label.coordinator.student" /></html:link></li>
    <li><html:link page="/thesisSection.do"><bean:message key="link.masterDegree.administrativeOffice.thesis.title" /></html:link></li>
	<li><html:link page="/gratuitySection.do"><bean:message key="link.masterDegree.administrativeOffice.gratuity" /></html:link></li>
    <li><html:link page="/externalPersonSection.do"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.title" /></html:link></li>
    <li><html:link page="/marksManagement.do?method=prepareChooseMasterDegree"><bean:message key="link.masterDegree.administrativeOffice.marksManagement" /></html:link></li>

    <li><bean:message key="link.masterDegree.administrativeOffice.listing" /></li>
	<blockquote>
		<li><html:link page="/listMasterDegrees.do?method=chooseDegreeFromList&jspTitle=title.studentListByDegree&page=0"><bean:message key="link.studentListByDegree" /></html:link></dd>
		<li><html:link page="/chooseExecutionYearToListCourseStudents.do?method=prepareChooseExecutionYear&jspTitle=title.studentListByCourse&page=0"><bean:message key="link.studentListByCourse" /></html:link></dd>
	</blockquote>
</ul>

