<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<p><b><bean:message key="label.masterDegree.administrativeOffice.candidate" /></b></p>
<ul>
 <li><html:link page="/chooseExecutionYear.do?method=prepareChooseExecutionYear&page=0"><bean:message key="link.masterDegree.administrativeOffice.createCandidate" /></html:link></li>
 <li><html:link page="/chooseExecutionYearToVisualizeCandidates.do?method=prepareChooseExecutionYear&page=0"><bean:message key="link.masterDegree.administrativeOffice.visualizeCandidateInformations" /></html:link></li>
 <li><html:link page="/chooseExecutionYearToEditCandidates.do?method=prepareChooseExecutionYear&page=0"><bean:message key="link.masterDegree.administrativeOffice.editCandidateInformations" /></html:link></li>
 <li><html:link page="/chooseExecutionYearToSelectCandidates.do?method=prepareChooseExecutionYear&page=0"><bean:message key="link.masterDegree.administrativeOffice.selectCandidates" /></html:link></li>
</ul>
<ul>
 <li><html:link page="/contributorSection.do"><bean:message key="link.masterDegree.administrativeOffice.contributor" /></html:link></li>
 <li><html:link page="/guideSection.do"><bean:message key="link.masterDegree.administrativeOffice.guide" /></html:link></li>

 <li><html:link page="/studentSection.do"><bean:message key="label.coordinator.student" /></html:link></li>
 <li><html:link page="/chooseExecutionYearToManageMarks.do?method=prepareChooseExecutionYear&jspTitle=title.masterDegree.administrativeOffice.marksManagement"><bean:message key="link.masterDegree.administrativeOffice.marksManagement" />
    </html:link>
 </li>
 <li><html:link page="/listingSection.do"><bean:message key="link.masterDegree.administrativeOffice.listing" /></html:link></li>
</ul> 