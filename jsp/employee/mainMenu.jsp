<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants"%>
<%@ page import="net.sourceforge.fenixedu.applicationTier.IUserView"%>
<%@ page import="net.sourceforge.fenixedu.domain.person.RoleType"%>

<ul>
	<li class="navheader"><bean:message key="title.assiduousness" /></li>
	<li><html:link page="/assiduousnessRecords.do?method=showEmployeeInfo">
		<bean:message key="label.schedule" />
	</html:link></li>

	<li><html:link page="/assiduousnessRecords.do?method=showWorkSheet">
		<bean:message key="link.workSheet" />
	</html:link></li>

	<li><html:link page="/assiduousnessRecords.do?method=showClockings">
		<bean:message key="link.clockings" />
	</html:link></li>
	<li><html:link
		page="/assiduousnessRecords.do?method=showJustifications">
		<bean:message key="link.justifications" />
	</html:link></li>

	
    <% IUserView userView = (IUserView) session.getAttribute(pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE);
    if (userView.getPerson().hasFunctionType(net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType.ASSIDUOUSNESS_RESPONSIBLE,
        		    net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum.ASSIDUOUSNESS_STRUCTURE)) {%>
	<li class="navheader"><bean:message
		key="title.assiduousnessResponsible" /></li>
	<li><html:link
		page="/assiduousnessResponsible.do?method=showEmployeeList">
		<bean:message key="label.employees" />
	</html:link></li>
	<% } %>
	
	<%net.sourceforge.fenixedu.applicationTier.IUserView user = (net.sourceforge.fenixedu.applicationTier.IUserView) session
                    .getAttribute(pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE);
            if (net.sourceforge.fenixedu.domain.ManagementGroups.isProtocolManagerMember(user.getPerson())) {
    %>
		<li class="navheader">
			<bean:message key="label.protocols.navigation.header" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
		</li>
		<li>
			<html:link page="/protocols.do?method=showProtocolAlerts">
		  		<bean:message key="link.protocols.showAlerts" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
		  	</html:link>
		</li>
		<li>
			<html:link page="/protocols.do?method=showProtocols">
		  		<bean:message key="link.protocols.view" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
		  	</html:link>
		</li>
		<li>
			<html:link page="/protocols.do?method=searchProtocols&amp;showAllNationalityTypes">
		  		<bean:message key="link.protocols.search" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
		  	</html:link>
		</li>			
	<%}%>
</ul>

