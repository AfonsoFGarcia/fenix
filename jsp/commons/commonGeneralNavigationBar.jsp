<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@page import="net.sourceforge.fenixedu.domain.Role"%>
<%@page import="net.sourceforge.fenixedu.domain.person.RoleType"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>	
<%@ page import="org.apache.struts.Globals" %>	

<logic:present name="<%= Globals.MODULE_KEY %>">
	<bean:define id="modulePrefix" name="<%= Globals.MODULE_KEY %>" property="prefix" type="java.lang.String"/>
	<div id="navgeral">
	<ul>
		<bean:define id="userView" name="<%= SessionConstants.U_VIEW %>" scope="session"/>
		<logic:iterate id="roleType" type="net.sourceforge.fenixedu.domain.person.RoleType" name="userView" property="roleTypes">
			<%
				final Role role = Role.getRoleByRoleType(roleType);
				pageContext.setAttribute("role", role);
			%>
			<logic:notEqual name="role" property="roleType" value="net.sourceforge.fenixedu.domain.person.RoleType.GRANT_OWNER_MANAGER">
				<logic:notEqual name="role" property="roleType" value="net.sourceforge.fenixedu.domain.person.RoleType.CREDITS_MANAGER">
					<logic:notEqual name="role" property="roleType" value="net.sourceforge.fenixedu.domain.person.RoleType.DEPARTMENT_CREDITS_MANAGER">
						<bean:define id="bundleKeyPageName"><bean:write name="role" property="pageNameProperty"/>.name</bean:define>
						<bean:define id="link"><%= request.getContextPath() %>/dotIstPortal.do?prefix=<bean:write name="role" property="portalSubApplication"/>&amp;page=<bean:write name="role" property="page"/></bean:define>					
							<logic:equal name="role" property="portalSubApplication" value="<%= modulePrefix %>">
								<li class="navheader selected">
						    		<html:link href='<%= link %>'>
										<span>
							    			<bean:message name="bundleKeyPageName" bundle="PORTAL_RESOURCES"/>
										</span>
						    		</html:link>
						    	</li>
							</logic:equal>
							<logic:notEqual name="role" property="portalSubApplication" value="<%= modulePrefix %>">
								<li class="navheader">
						    		<html:link href='<%= link %>'>
										<span>
							    			<bean:message name="bundleKeyPageName" bundle="PORTAL_RESOURCES"/>
										</span>
				    				</html:link>
				    			</li>
							</logic:notEqual>
					</logic:notEqual>
				</logic:notEqual>
			</logic:notEqual>
		</logic:iterate>	
		<logic:present role="GRANT_OWNER_MANAGER,CREDITS_MANAGER">
			<logic:equal name="modulePrefix" value="/facultyAdmOffice">
				<li class="navheader selected">
					<html:link href='<%= request.getContextPath() + "/dotIstPortal.do?prefix=/facultyAdmOffice&amp;page=/index.do" %>'>
						<span>
							<bean:message key="portal.facultyAdmOffice.name" bundle="PORTAL_RESOURCES"/>
						</span>
					</html:link>
				</li>
			</logic:equal>
			<logic:notEqual name="modulePrefix" value="/facultyAdmOffice">
				<li class="navheader">
					<html:link href='<%= request.getContextPath() + "/dotIstPortal.do?prefix=/facultyAdmOffice&amp;page=/index.do" %>' >
						<span>
							<bean:message key="portal.facultyAdmOffice.name" bundle="PORTAL_RESOURCES"/>
						</span>
					</html:link>
				</li>
			</logic:notEqual>
			</li>
		</logic:present>
	</ul>
	</div>
</logic:present>
<logic:notPresent name="<%= Globals.MODULE_KEY %>">
	<span class="error"><!-- Error messages go here -->N�o passou pelo RequestProcessor</span>
</logic:notPresent>


