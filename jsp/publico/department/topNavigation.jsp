<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.organizationalStructure.Unit" %>
<%@ page import="net.sourceforge.fenixedu.domain.RootDomainObject" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%
    Integer departmentUnitId;
    Unit unit = (Unit) request.getAttribute("unit");
    if (unit == null) {
        departmentUnitId = new Integer(request.getParameter("selectedDepartmentUnitID"));
        unit = (Unit) RootDomainObject.getInstance().readPartyByOID(departmentUnitId);
        
        request.setAttribute("unit", unit);
    }
    else {
    	departmentUnitId = unit.getIdInternal();
    }
%>


<ul>
	<li>
		<html:link page="<%= "/department/departmentSite.do?method=presentation&amp;selectedDepartmentUnitID=" + departmentUnitId %>">
			<bean:message key="label.department.section.start" bundle="MESSAGING_RESOURCES"/>
		</html:link>
	</li>
</ul>

<fr:view name="unit" property="site" type="net.sourceforge.fenixedu.domain.UnitSite" layout="unit-top-menu">
    <fr:layout>
        <fr:property name="sectionUrl" value="<%= request.getContextPath() + "/publico/department/departmentSite.do?method=section" %>"/>
        <fr:property name="contextParam" value="selectedDepartmentUnitID"/>
        <fr:property name="contextRelative" value="false"/>
    </fr:layout>
</fr:view>
