<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<ul>
	<li class="navheader"><bean:message key="link.masterDegree.administrativeOffice.gratuity"/></li>
    <li><html:link page="/studentsGratuityList.do?method=chooseExecutionYear&amp;page=0"><bean:message key="link.masterDegree.administrativeOffice.gratuity.listStudents"/></html:link></li>
</ul>   
  	