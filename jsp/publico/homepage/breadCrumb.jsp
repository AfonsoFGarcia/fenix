<%@ page language="java" %>
<%@page import="net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html:xhtml/>

<bean:define id="homepage" name="<%= FunctionalityContext.CONTEXT_KEY %>" property="selectedContainer" toScope="request"/>

<logic:present name="homepage">
    <logic:present name="homepage" property="person.employee.currentDepartmentWorkingPlace">
        <logic:present name="homepage" property="person.teacher">
            <bean:define id="institutionUrl" type="java.lang.String">
                <bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>
            </bean:define>
            <bean:define id="institutionUrlStructure" type="java.lang.String">
                <bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/><bean:message key="link.institution.structure" bundle="GLOBAL_RESOURCES"/>
            </bean:define>
            <bean:define id="departmentUnitID" type="java.lang.String">
                <bean:write name="homepage" property="person.employee.currentDepartmentWorkingPlace.departmentUnit.idInternal"/>
            </bean:define>
        
            <div class="breadcumbs mvert0">
                <html:link href="<%= institutionUrl %>">
                    <bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/>
                </html:link>
                &nbsp;&gt;&nbsp;
                <html:link href="<%=institutionUrlStructure%>">
                    <bean:message bundle="PUBLIC_DEPARTMENT_RESOURCES" key="structure"/>
                </html:link>
                &nbsp;&gt;&nbsp;
                <html:link page="/publico/department/showDepartments.faces" module="">
                    <bean:message bundle="PUBLIC_DEPARTMENT_RESOURCES" key="academic.units"/>
                </html:link>
                &nbsp;&gt;&nbsp;            
                <bean:define id="unitId" name="homepage" property="person.employee.currentDepartmentWorkingPlace.departmentUnit.idInternal"/>
                <html:link page="<%= "/department/departmentSite.do?method=presentation&amp;selectedDepartmentUnitID=" + unitId %>" module="/publico">
                    <bean:write name="homepage" property="person.employee.currentDepartmentWorkingPlace.realName"/>
                </html:link>
                &nbsp;&gt;&nbsp;
                <html:link page="<%= "/publico/department/teachers.do?selectedDepartmentUnitID=" + departmentUnitID %>" module="">
                    <bean:message bundle="PUBLIC_DEPARTMENT_RESOURCES" key="department.faculty"/>
                </html:link>
                &nbsp;&gt;&nbsp;
                <bean:write name="homepage" property="ownersName"/>
            </div>
        </logic:present>
    </logic:present>
    
    <bean:define id="site" name="homepage" toScope="request"/>
    <bean:define id="siteActionName" value="/viewHomepage.do" toScope="request"/>
    <bean:define id="siteContextParam" value="homepageID" toScope="request"/>
    <bean:define id="siteContextParamValue" name="homepage" property="idInternal" toScope="request"/>
    
</logic:present>