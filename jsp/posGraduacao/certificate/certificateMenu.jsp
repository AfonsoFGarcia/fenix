<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<ul>
    <li><html:link page="/candidateSection.do"><bean:message key="link.masterDegree.administrativeOffice.candidate" /></html:link></li>
    <li><html:link page="/contributorSection.do"><bean:message key="link.masterDegree.administrativeOffice.contributor" /></html:link></li>
	<li><html:link page="/guideSection.do"><bean:message key="link.masterDegree.administrativeOffice.guide" /></html:link></li>
</ul>
    <p><b><bean:message key="label.coordinator.student" /></b></p>
<ul>
    <li><html:link page="/chooseCertificateInfoAction.do?method=prepare&page=0"><bean:message key="link.certificate" /></html:link></li>
    <li><html:link page="/chooseDeclarationInfoAction.do?method=prepare&page=0"><bean:message key="link.declarations" /></html:link></li>
    <li><html:link page="/chooseFinalResultInfoAction.do?method=prepare&page=0"><bean:message key="link.finalResult" /></html:link></li>
</ul>
<ul>
    <li><html:link page="/chooseExecutionYearToManageMarks.do?method=prepareChooseExecutionYear&jspTitle=title.masterDegree.administrativeOffice.marksManagement"><bean:message key="link.masterDegree.administrativeOffice.marksManagement" /></html:link></li>
</ul>
	   
  	
   