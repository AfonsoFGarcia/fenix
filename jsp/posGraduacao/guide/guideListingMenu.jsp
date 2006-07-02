<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<ul>
	<li><html:link page="/candidateSection.do"><bean:message key="link.masterDegree.administrativeOffice.candidate" /></html:link></li>
	<li><html:link page="/contributorSection.do"><bean:message key="link.masterDegree.administrativeOffice.contributor" /></html:link></li>
</ul>
<p><b><bean:message key="label.masterDegree.administrativeOffice.guide" /></b></p>
<ul>
	<blockquote>
		<li><html:link page="/chooseDataToCreateGuide.do?method=chooseDegreeFromList"><bean:message key="link.masterDegree.administrativeOffice.createGuide" /></html:link></li>
		<li><html:link page="/chooseGuideDispatchAction.do?method=prepareChoose&page=0&action=visualize"><bean:message key="link.masterDegree.administrativeOffice.visualizeGuide" /></html:link></li>
		<li><bean:message key="link.masterDegree.administrativeOffice.guideListing" /></li>
		<dd><html:link page="/guideListingByYear.do?method=prepareChooseYear"><bean:message key="link.masterDegree.administrativeOffice.guideListingByYear" /></html:link></dd>
		<dd><html:link page="/guideListingByPerson.do?method=prepareChoosePerson&amp;page=0"><bean:message key="link.masterDegree.administrativeOffice.guideListingByPerson" /></html:link></dd>
		<dd><html:link page="/guideListingByState.do?method=prepareChooseState&amp;page=0"><bean:message key="link.masterDegree.administrativeOffice.guideListingByState" /></html:link></dd>
	</blockquote>

</ul>
<ul>
	<li><html:link page="/studentSection.do"><bean:message key="label.coordinator.student" /></html:link></li>
    <li><html:link page="/thesisSection.do"><bean:message key="link.masterDegree.administrativeOffice.thesis.title" /></html:link></li>
	<li><html:link page="/gratuitySection.do"><bean:message key="link.masterDegree.administrativeOffice.gratuity" /></html:link></li>
    <li><html:link page="/externalPersonSection.do"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.title" /></html:link></li>
    <li><html:link page="/marksManagement.do?method=prepareChooseMasterDegree"><bean:message key="link.masterDegree.administrativeOffice.marksManagement" /></html:link></li>
    <li><html:link page="/listingSection.do"><bean:message key="link.masterDegree.administrativeOffice.listing" /></html:link></li>
    <li><html:link page="/dfaCandidacySection.do"><bean:message key="link.masterDegree.administrativeOffice.dfaCandidacyManagement" bundle="ADMIN_OFFICE_RESOURCES"/> </html:link></li>

</ul>
   