<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<ul>
	<li><html:link page="/sendEmail.do?method=prepare&amp;allowChangeSender=false&amp;fromName=Conselho Directivo&amp;from=cd@ist.utl.pt"><bean:message key="label.send.mail"/></html:link></li>
    <li><html:link page="/assiduousnessStructure.do?method=showAssiduousnessStructure"><bean:message key="link.assiduousnessStructure"/></html:link></li>    <li><html:link page="/gratuitySection.do"><bean:message key="link.masterDegree.administrativeOffice.gratuity" /></html:link></li>    <li><html:link page="/studentStatistics.do?method=showStatistics"><bean:message key="link.statistics.students" /></html:link></li>
<!--
    <li><html:link page="/searchPeople.do?method=search"><bean:message key="link.students" /></html:link></li>
-->

	<li class="navheader"><bean:message key="link.control"/></li>
    <li><html:link page="/summariesControl.do?method=prepareSummariesControl&amp;page=0"><bean:message key="link.summaries.control" /></html:link></li>
    <li><html:link page="/evaluationMethodControl.do?method=search"><bean:message key="label.evaluationMethodControl"/></html:link></li></ul>   
