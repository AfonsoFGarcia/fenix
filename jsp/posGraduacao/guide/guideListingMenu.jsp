<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<ul>
	<li><html:link page="/candidateSection.do"><bean:message key="link.masterDegree.administrativeOffice.candidate" /></html:link></li>
	<li><html:link page="/contributorSection.do"><bean:message key="link.masterDegree.administrativeOffice.contributor" /></html:link></li>
</ul>
<p><b><bean:message key="label.masterDegree.administrativeOffice.guide" /></b></p>
<ul>
	<li><html:link page="/chooseExecutionYearToCreateGuide.do?method=prepareChooseExecutionYear"><bean:message key="link.masterDegree.administrativeOffice.createGuide" /></html:link></li>
	<li><html:link page="/chooseGuideDispatchAction.do?method=prepareChoose&page=0&action=visualize"><bean:message key="link.masterDegree.administrativeOffice.visualizeGuide" /></html:link></li>
	<li><bean:message key="link.masterDegree.administrativeOffice.guideListing" /></li>
<dd><html:link page="/guideListingByYear.do?method=prepareChooseYear"><bean:message key="link.masterDegree.administrativeOffice.guideListingByYear" /></html:link></dd>
</ul>
<ul>
	<li><html:link page="/studentSection.do"><bean:message key="label.coordinator.student" /></html:link></li>
	<li><html:link page="/chooseExecutionYearToManageMarks.do?method=prepareChooseExecutionYear&jspTitle=title.masterDegree.administrativeOffice.marksManagement"><bean:message key="link.masterDegree.administrativeOffice.marksManagement" /></html:link></li>
</ul>
   