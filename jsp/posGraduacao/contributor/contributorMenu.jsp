<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<ul>
	<li><html:link page="/candidateSection.do"><bean:message key="link.masterDegree.administrativeOffice.candidate" /></html:link></li>
</ul>
	<p><b><bean:message key="label.masterDegree.administrativeOffice.contributor" /></b></p>
<ul>
	<li><html:link page="/createContributorDispatchAction.do?method=prepare"><bean:message key="link.masterDegree.administrativeOffice.createContributor" /></html:link></li>
	<li><html:link page="/visualizeContributors.do?method=prepare&action=visualize&page=0"><bean:message key="link.masterDegree.administrativeOffice.visualizeContributor" /></html:link></li>
	<li><html:link page="/editContributors.do?method=prepare&action=edit&page=0"><bean:message key="link.masterDegree.administrativeOffice.editContributor" /></html:link></li>
</ul>
<ul>	
	<li><html:link page="/guideSection.do"><bean:message key="link.masterDegree.administrativeOffice.guide" /></html:link></li>
	<li><html:link page="/certificateSection.do"><bean:message key="label.coordinator.student" /></html:link></li>
</ul>